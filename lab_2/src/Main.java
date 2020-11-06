import ru.spbstu.pipeline.RC;

public class Main
{
    public static void main(String[] args)
    {
        ArgsValidity checker = new ArgsValidity(args);
        if (checker.CheckArgs() != RC.CODE_SUCCESS)
            return;

        Manager manager = new Manager();
        if (manager.run(args[0]) != RC.CODE_SUCCESS)
            return;
    }
}