import ru.spbstu.pipeline.IExecutor;
import ru.spbstu.pipeline.IReader;
import ru.spbstu.pipeline.IWriter;
import ru.spbstu.pipeline.RC;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.logging.Logger;

public class Manager
{
    private final static int numTypes = 1;
    private IReader reader;
    private IExecutor[] executors;
    private IWriter writer;

    private Object builtWorker(String name, Logger logger)
    {
        try
        {
            Class workerClass = Class.forName(name);
            Constructor[] constructors = workerClass.getConstructors();
            for (Constructor constructor : constructors)
            {
                boolean isNumTypes = constructor.getParameterCount() == numTypes;
                if (isNumTypes && constructor.getParameterTypes()[numTypes - 1] == Logger.class)
                    return constructor.newInstance(logger);
                else
                    return constructor.newInstance();
            }
            return null;
        }
        catch (Exception error)
        {
            return null;
        }
    }

    private RC buildPipeline(String[] readerParams, String[] writerParams,
                             String[] orderExecutors)
    {
        Logger logger = Logger.getAnonymousLogger();

        reader = (IReader)builtWorker(readerParams[ConfigFile.numLiteral], logger);
        writer = (IWriter)builtWorker(writerParams[ConfigFile.numLiteral], logger);
        executors = new IExecutor[orderExecutors.length];
        for (int i = 0; i < orderExecutors.length; ++i)
            executors[i] = (IExecutor)builtWorker(orderExecutors[i], logger);

        if (reader == null && writer == null && executors[numTypes - 1] == null)
            return RC.CODE_FAILED_PIPELINE_CONSTRUCTION;
        return RC.CODE_SUCCESS;
    }

    private RC setParams(String[] readerParams, String[] writerParams,
                         HashMap<String, String> executorsMap, String[] order)
    {
        RC error = reader.setConfig(readerParams[ConfigFile.numValue]);
        if (error != RC.CODE_SUCCESS)
            return error;

        error = writer.setConfig(writerParams[ConfigFile.numValue]);
        if (error != RC.CODE_SUCCESS)
            return error;

        error = reader.setConsumer(executors[0]);
        if (error != RC.CODE_SUCCESS)
            return error;

        for (int i = 0; i < executors.length; ++i)
        {
            error = executors[i].setConfig(executorsMap.get(order[i]));
            if (error != RC.CODE_SUCCESS)
                return error;

            if (i < executors.length - 1) {
                error = executors[i].setConsumer(executors[i + 1]);
                if (error != RC.CODE_SUCCESS)
                    return error;
            }
        }

        return executors[executors.length - 1].setConsumer(writer);
    }

    private RC coding (String nameInputFile, String nameOutputFile)
    {
        try
        {
            reader.setInputStream(new FileInputStream(nameInputFile));
        }
        catch (IOException error)
        {
            return RC.CODE_INVALID_INPUT_STREAM;
        }

        try
        {
            writer.setOutputStream(new FileOutputStream(nameOutputFile));
        }
        catch (IOException error)
        {
            return RC.CODE_INVALID_OUTPUT_STREAM;
        }

        return reader.execute(null);
    }

    public RC run(String nameConfigFile)
    {
        ManagerConfig config = new ManagerConfig();
        RC error = config.readConfig(nameConfigFile);
        if (error != RC.CODE_SUCCESS)
            return error;

        error = buildPipeline(config.getReaderParams(), config.getWriterParams(), config.getOrderExecutors());
        if (error != RC.CODE_SUCCESS)
            return error;

        error = setParams(config.getReaderParams(), config.getWriterParams(),
                config.getExecutorsMap(), config.getOrderExecutors());
        if (error != RC.CODE_SUCCESS)
            return error;

        return coding(config.getNameTextFile(), config.getNameCodedFile());
    }
}
