import ru.spbstu.pipeline.RC;

public class ExecutorConfig extends ConfigFile
{
    private String nameKeyFile;

    public RC processLine(String[] params)
    {
        switch (ExecutorParam.valueOf(params[numLiteral]))
        {
            case keyFile:
                nameKeyFile = params[numValue];
                break;
            default:
                return RC.CODE_CONFIG_SEMANTIC_ERROR;
        }
        return RC.CODE_SUCCESS;
    }

    public String GetNameKeyFile()
    {
        return nameKeyFile;
    }

    enum ExecutorParam
    {
        keyFile
    }
}
