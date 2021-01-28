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
        return config.readConfig(cfg);
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
            RC error;
            long sizeInput = input.getChannel().size();

            byte[] text = new byte[config.getReadBlockSize()];
            byte[] remainder = new byte [(int)(sizeInput % text.length)];

            for (int i = 0; i < sizeInput / text.length; i++)
            {
                input.read(text);
                error = consumer.execute(text);
                if (error != RC.CODE_SUCCESS)
                    return error;
            }

            input.read(remainder);
            return consumer.execute(remainder);
        }
        catch (IOException error)
        {
            return RC.CODE_FAILED_TO_READ;
        }
    }
}
