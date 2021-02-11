import ru.spbstu.pipeline.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class MyWriter implements IWriter
{
    private IMediator mediator;
    private final static TYPE[] inputTypes = {TYPE.BYTE, TYPE.CHAR};
    private TYPE targetType;
    private FileOutputStream output;
    private WriterConfig config;
    private ArrayList<Byte> codedText;

    public MyWriter()
    {
        codedText = new ArrayList<>(0);
    }
    public TYPE[] getOutputTypes()
    {
        return null;
    }

    public RC setConsumer(IConsumer c)
    {
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

    private TYPE typesIntersection(TYPE[] producerTypes)
    {
        for (TYPE producer : producerTypes)
            for (TYPE consumer : inputTypes)
                if (producer == consumer)
                    return producer;
        return null;
    }

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
        byte[] data = convert(mediator.getData());

        try
        {
            if (data == null)
            {
                if (codedText.size() != 0)
                {
                    for (Byte symbol : codedText)
                        output.write(symbol);
                    codedText.clear();
                }
                return RC.CODE_SUCCESS;
            }

            for (byte symbol : data)
                codedText.add(symbol);

            if (config.getWriteBlockSize() == 0)
                return RC.CODE_FAILED_TO_WRITE;



            while (codedText.size() >= config.getWriteBlockSize())
            {
                for (int i = 0; i < config.getWriteBlockSize(); i++)
                {
                    output.write(codedText.get(0));
                    codedText.remove(0);
                }

            }
        }
        catch(IOException writerError)
        {
            return RC.CODE_FAILED_TO_WRITE;
        }
        return RC.CODE_SUCCESS;
    }
}
