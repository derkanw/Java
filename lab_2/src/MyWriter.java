import ru.spbstu.pipeline.IExecutable;
import ru.spbstu.pipeline.IWriter;
import ru.spbstu.pipeline.RC;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MyWriter implements IWriter
{
    private FileOutputStream output;
    private WriterConfig config;
    private ArrayList<Byte> codedText;

    public MyWriter()
    {
        codedText = new ArrayList<>(0);
    }

    public RC setConfig(String cfg)
    {
        config = new WriterConfig();
        config.readConfig(cfg);
        return RC.CODE_SUCCESS;
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
        for (byte symbol : data)
            codedText.add(symbol);

        try
        {
            if (config.GetWriteBlockSize() == 0)
                return RC.CODE_FAILED_TO_WRITE;

            while (codedText.size() >= config.GetWriteBlockSize())
            {
                for (int i = 0; i < config.GetWriteBlockSize(); i++)
                {
                    output.write(codedText.get(0));
                    codedText.remove(0);
                }

            }

            for (Byte symbol : codedText)
                output.write(symbol);
        }
        catch(IOException writerError)
        {
            return RC.CODE_FAILED_TO_WRITE;
        }
        return RC.CODE_SUCCESS;
    }

    public void clear()
    {
        codedText.clear();
    }
}
