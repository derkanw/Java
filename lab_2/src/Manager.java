import ru.spbstu.pipeline.RC;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Manager
{
    final private MyReader reader;
    final private MyExecutor executor;
    final private MyWriter writer;

    public Manager()
    {
        reader = new MyReader();
        executor = new MyExecutor();
        writer = new MyWriter();
    }

    public RC Coding (String nameInputFile, String nameOutputFile)
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
        config.readConfig(nameConfigFile);

        reader.setConfig(config.GetNameReaderConfig());
        executor.setConfig(config.GetNameExecutorConfig());
        writer.setConfig(config.GetNameWriterConfig());

        reader.setConsumer(executor);
        executor.setConsumer(writer);

        Coding(config.GetNameTextFile(), config.GetNameCodedFile());
        writer.clear();
        Coding(config.GetNameCodedFile(), config.GetNameDecodedFile());
        writer.clear();

        return RC.CODE_SUCCESS;
    }
}
