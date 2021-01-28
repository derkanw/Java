import ru.spbstu.pipeline.IExecutable;
import ru.spbstu.pipeline.IWriter;
import ru.spbstu.pipeline.RC;

import java.io.FileOutputStream;
import java.io.IOException;

public class MyWriter implements IWriter
{
    private FileOutputStream output;
    private WriterConfig config;

    public RC setConfig(String cfg)
    {
        config = new WriterConfig();
        return config.readConfig(cfg);
    }

    public RC setOutputStream(FileOutputStream fos)
    {
        output = fos;
        return RC.CODE_SUCCESS;
    }

    public RC setConsumer(IExecutable c)
    {
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
            for (int i = 0; i < data.length; i += config.getWriteBlockSize())
                output.write(data, i, Math.min(config.getWriteBlockSize(), data.length - i));
            return RC.CODE_SUCCESS;
        }
        catch(IOException writerError)
        {
            return RC.CODE_FAILED_TO_WRITE;
        }
    }
}
