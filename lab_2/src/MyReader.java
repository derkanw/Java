import ru.spbstu.pipeline.IExecutable;
import ru.spbstu.pipeline.IReader;
import ru.spbstu.pipeline.RC;

import java.io.FileInputStream;
import java.io.IOException;

public class MyReader implements IReader
{
    private IExecutable consumer;
    private FileInputStream input;
    private ReaderConfig config;

    public RC setConfig(String cfg)
    {
        config = new ReaderConfig();
        config.readConfig(cfg);

        return RC.CODE_SUCCESS;
    }

    public RC setInputStream(FileInputStream fis)
    {
        input = fis;
        return RC.CODE_SUCCESS;
    }

    public RC setConsumer(IExecutable c)
    {
        consumer = c;
        return RC.CODE_SUCCESS;
    }

    public RC setProducer(IExecutable p)
    {
        return RC.CODE_SUCCESS;
    }

    public RC execute(byte[] data)
    {
        try
        {
            long sizeInput = input.getChannel().size();

            byte[] text = new byte[config.GetReadBlockSize()];
            byte[] remainder = new byte [(int)(sizeInput % text.length)];

            for (int i = 0; i < sizeInput / text.length; i++)
            {
                input.read(text);
                consumer.execute(text);
            }

            input.read(remainder);
            consumer.execute(remainder);
        }
        catch (IOException error)
        {
            return RC.CODE_FAILED_TO_READ;
        }
        return RC.CODE_SUCCESS;
    }
}
