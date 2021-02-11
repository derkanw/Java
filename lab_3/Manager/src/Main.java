import ru.spbstu.pipeline.RC;

public class Main
{
    public static void main(String[] args)
    {
        String loggerFile = "logging.txt";
        RC.initLogger(loggerFile);

        ArgsValidity checker = new ArgsValidity(args);
        RC error = checker.CheckArgs();
        if (error != RC.CODE_SUCCESS)
        {
            error.getError();
            return;
        }

        Manager manager = new Manager();
        error = manager.run(args[0]);
        if (error != RC.CODE_SUCCESS)
        {
            error.getError();
            return;
        }

        RC.CODE_SUCCESS.getSuccess();
    }
}