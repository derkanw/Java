import ru.spbstu.pipeline.RC;

public class ReaderConfig extends ConfigFile
{
    private int blockLength;

    public RC processLine(String[] params)
    {
        switch (ReaderParam.valueOf(params[numLiteral]))
        {
            case blockSize:
                blockLength = Integer.parseInt(params[numValue]);
                break;
            default:
                return RC.CODE_CONFIG_SEMANTIC_ERROR;
        }
        return RC.CODE_SUCCESS;
    }

    public int getReadBlockSize()
    {
        return blockLength;
    }

    enum ReaderParam
    {
        blockSize
    }
}
