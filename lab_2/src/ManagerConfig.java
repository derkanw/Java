import ru.spbstu.pipeline.RC;

public class ManagerConfig extends ConfigFile
{
    private String nameReaderConfig, nameExecutorConfig, nameWriterConfig;
    private String nameTextFile, nameCodedFile, nameDecodedFile;

    public RC processLine(String[] params)
    {
        switch (ManagerParam.valueOf(params[numLiteral]))
        {
            case readerConfig:
                nameReaderConfig = params[numValue];
                break;
            case executorConfig:
                nameExecutorConfig = params[numValue];
                break;
            case writerConfig:
                nameWriterConfig = params[numValue];
                break;
            case textFile:
                nameTextFile = params[numValue];
                break;
            case codedFile:
                nameCodedFile = params[numValue];
                break;
            case decodedFile:
                nameDecodedFile = params[numValue];
                break;
            default:
                return RC.CODE_CONFIG_SEMANTIC_ERROR;
        }
        return RC.CODE_SUCCESS;
    }

    public String GetNameReaderConfig()
    {
        return nameReaderConfig;
    }

    public String GetNameExecutorConfig()
    {
        return nameExecutorConfig;
    }

    public String GetNameWriterConfig()
    {
        return nameWriterConfig;
    }

    public String GetNameTextFile()
    {
        return nameTextFile;
    }

    public String GetNameCodedFile()
    {
        return nameCodedFile;
    }

    public String GetNameDecodedFile()
    {
        return nameDecodedFile;
    }

    enum ManagerParam
    {
        readerConfig,
        executorConfig,
        writerConfig,
        textFile,
        codedFile,
        decodedFile
    }
}
