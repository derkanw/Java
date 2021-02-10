import ru.spbstu.pipeline.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class MyExecutor implements IExecutor
{
    private IConsumer consumer;
    private ExecutorConfig config;
    private IMediator mediator;
    private final static TYPE[] inputTypes = {TYPE.BYTE, TYPE.SHORT};
    private final static TYPE[] outputTypes = {TYPE.BYTE, TYPE.CHAR};
    private TYPE targetType; 
    private byte[] key;
    private byte[] data;

    public TYPE[] getOutputTypes()
    {
        return outputTypes;
    }

    public RC setConfig(String cfg)
    {
        config = new ExecutorConfig();
        return config.readConfig(cfg);
    }

    public RC setConsumer(IConsumer c)
    {
        consumer = c;
        return RC.CODE_SUCCESS;
    }

    public RC setProducer(IProducer p)
    {
        targetType = typesIntersection(p.getOutputTypes());
        mediator = p.getMediator(targetType);
        if (mediator == null)
            return RC.CODE_FAILED_PIPELINE_CONSTRUCTION;
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

    private TYPE typesIntersection(TYPE[] producerTypes)
    {
        for (TYPE producer : producerTypes)
            for (TYPE consumer : inputTypes)
                if (producer == consumer)
                    return producer;
        return null;
    }

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

    private byte[] convert(Object data)
    {
        byte[] convertData;
        if (data == null)
            return null;

        switch (targetType)
        {
            case BYTE:
                return (byte[])data;
            case SHORT:
                int size = ((short[])data).length * 2;
                ByteBuffer buffer = ByteBuffer.allocate(size);
                for (int i = 0; i < size / 2; ++i)
                    buffer.putShort(((short[])data)[i]);
                return buffer.array();
            case CHAR:
                convertData = new byte[((char[])data).length];
                for (int i = 0; i < convertData.length; ++i)
                    convertData[i] = (byte)((char[])data)[i];
                return convertData;
            default:
                return null;
        }
    }

    public RC execute()
    {
        if (readKey(config.getNameKeyFile()) != RC.CODE_SUCCESS)
            return RC.CODE_FAILED_TO_READ;

        data = convert(mediator.getData());
        if (data != null)
        {
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
            data = destination;
        }

        return consumer.execute();
    }

    private class ByteMediator implements IMediator
    {
        public Object getData()
        {
            return data;
        }
    }

    private class ShortMediator implements IMediator
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

    private class CharMediator implements IMediator
    {
        public Object getData()
        {
            if (data == null)
                return null;
            return new String(data, StandardCharsets.UTF_8).toCharArray();
        }
    }
}
