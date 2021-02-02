import ru.spbstu.pipeline.IExecutable;
import ru.spbstu.pipeline.IExecutor;
import ru.spbstu.pipeline.RC;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class MyExecutor implements IExecutor
{
    private IExecutable consumer;
    private ExecutorConfig config;

    private byte[] key;

    public RC setConfig(String cfg)
    {
        config = new ExecutorConfig();
        return config.readConfig(cfg);
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

    private RC readKey(String nameKeyFile)
    {
        File keyFile = new File(nameKeyFile);
        try (FileInputStream keyReader = new FileInputStream(keyFile))
        {
            key = new byte[(int)keyFile.length()];
            keyReader.read(key);
        }
        catch(IOException readerError)
        {
            return RC.CODE_FAILED_TO_READ;
        }
        return RC.CODE_SUCCESS;
    }

    public RC execute(byte[] data)
    {
        if (readKey(config.getNameKeyFile()) != RC.CODE_SUCCESS)
            return RC.CODE_FAILED_TO_READ;

        byte[] destination = new byte[data.length];
        int j = 0;
        while(j < data.length)
            for (byte symbol : key)
            {
                if (j >= data.length)
                    break;
                destination[j] = (byte) (data[j] ^ symbol);
                j++;
            }

        return consumer.execute(destination);
    }
}
