import ru.spbstu.pipeline.RC;

import java.util.HashMap;

public class ManagerConfig extends ConfigFile
{
    private String nameTextFile, nameCodedFile;
    final private HashMap<String, String> executors = new HashMap<>();
    private String[] orderExecutors;
    private String[] workerParams, readerParams, writerParams;

    private final static String accord = ":";
    private final static String separator = ";";

    private RC readPair(String line)
    {
        if (line.indexOf(accord) == -1)
            return RC.CODE_CONFIG_GRAMMAR_ERROR;
        workerParams = line.split(accord, numGramUnits);
        return RC.CODE_SUCCESS;
    }

    private RC readExecutors(String line)
    {
        if (line.indexOf(separator) == -1)
            return RC.CODE_CONFIG_GRAMMAR_ERROR;

        String[] list = line.split(separator);
        for (String executor : list)
        {
            RC error = readPair(executor);
            if (error != RC.CODE_SUCCESS)
                return error;
            executors.put(workerParams[numLiteral], workerParams[numValue]);
        }

        if (executors.isEmpty())
            return RC.CODE_CONFIG_SEMANTIC_ERROR;
        return RC.CODE_SUCCESS;
    }

    private RC readOrderExecutors(String line)
    {
        if (line.indexOf(separator) == -1)
            return RC.CODE_CONFIG_GRAMMAR_ERROR;

        orderExecutors = line.split(separator);
        if (orderExecutors.length == 0)
            return RC.CODE_CONFIG_SEMANTIC_ERROR;
        return RC.CODE_SUCCESS;
    }

    public RC processLine(String[] params)
    {
        RC error;
        switch (ManagerParam.valueOf(params[numLiteral]))
        {
            case reader:
                error = readPair(params[numValue]);
                if (error != RC.CODE_SUCCESS)
                    return error;
                readerParams = workerParams;
                break;
            case executors:
                error = readExecutors(params[numValue]);
                if (error != RC.CODE_SUCCESS)
                    return error;
                break;
            case orderExecutors:
                error = readOrderExecutors(params[numValue]);
                if (error != RC.CODE_SUCCESS)
                    return error;
                break;
            case writer:
                error = readPair(params[numValue]);
                if (error != RC.CODE_SUCCESS)
                    return error;
                writerParams = workerParams;
                break;
            case textFile:
                nameTextFile = params[numValue];
                break;
            case codedFile:
                nameCodedFile = params[numValue];
                break;
            default:
                return RC.CODE_CONFIG_SEMANTIC_ERROR;
        }
        return RC.CODE_SUCCESS;
    }

    public String[] getReaderParams()
    {
        return readerParams;
    }

    public HashMap<String, String> getExecutorsMap()
    {
        return executors;
    }

    public String[] getOrderExecutors()
    {
        return orderExecutors;
    }

    public String[] getWriterParams()
    {
        return writerParams;
    }

    public String getNameTextFile()
    {
        return nameTextFile;
    }

    public String getNameCodedFile()
    {
        return nameCodedFile;
    }

    enum ManagerParam
    {
        reader,
        executors,
        orderExecutors,
        writer,
        textFile,
        codedFile,
    }
}
