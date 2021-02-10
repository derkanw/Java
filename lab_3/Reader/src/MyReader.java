import ru.spbstu.pipeline.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class MyReader implements IReader
{
    private IConsumer consumer;
    private FileInputStream input;
    private ReaderConfig config;
    private final static TYPE[] outputTypes = {TYPE.BYTE, TYPE.CHAR};
    private byte[] data;

    public IMediator getMediator(TYPE type)
    {
        switch(type)
        {
            case BYTE:
                return new ByteMediator();
            case SHORT:
                return new ShortMediator();
            case CHAR:
                return new CharMediator();
            default:
                return null;
        }
    }
    
    public TYPE[] getOutputTypes()
    {
        return outputTypes;
    }

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

    public RC setConsumer(IConsumer c)
    {
        consumer = c;
        return RC.CODE_SUCCESS;
    }

    public RC setProducer(IProducer p)
    {
        return RC.CODE_SUCCESS;
    }

    public RC execute()
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
                data = text;
                error = consumer.execute();
                if (error != RC.CODE_SUCCESS)
                    return error;
            }

            input.read(remainder);
            data = remainder;
            consumer.execute();
            data = null;
            return consumer.execute();
        }
        catch (IOException error)
        {
            return RC.CODE_FAILED_TO_READ;
        }
    }

    class ByteMediator implements IMediator
    {
        public Object getData()
        {
            return data;
        }
    }

    class ShortMediator implements IMediator
    {
        public Object getData()
        {
            if (data == null)
                return null;

            int size = data.length / 2;
            short[] shortData = new short[size];
            ByteBuffer buffer = ByteBuffer.wrap(data);
            for (int i = 0; i < size; ++i)
                shortData[i] = buffer.getShort(2 * i);

            return shortData;
        }
    }

    class CharMediator implements IMediator
    {
        public Object getData()
        {
            if (data == null)
                return null;
            return new String(data, StandardCharsets.UTF_8).toCharArray();
        }
    }
}