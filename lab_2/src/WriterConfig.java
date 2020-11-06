import ru.spbstu.pipeline.RC;

public class WriterConfig extends ConfigFile
{
    private int blockLength;

    public RC processLine(String[] params)
    {
        switch (WriterParam.valueOf(params[numLiteral]))
        {
            case blockSize:
                blockLength = Integer.parseInt(params[numValue]);
                break;
            default:
                return RC.CODE_CONFIG_SEMANTIC_ERROR;
        }
        return RC.CODE_SUCCESS;
    }

    public int GetWriteBlockSize()
    {
        return blockLength;
    }

    enum WriterParam
    {
        blockSize
    }
}
