import ru.spbstu.pipeline.RC;

public class ArgsValidity
{
    private String[] args;
    ArgsValidity(String[] value)
    {
        args = value;
    }

    public RC CheckArgs()
    {
        if (args == null || args.length == 0)
            return RC.CODE_INVALID_ARGUMENT;
        return RC.CODE_SUCCESS;
    }
}
