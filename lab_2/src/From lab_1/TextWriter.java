import java.io.FileOutputStream;
import java.io.IOException;

public class TextWriter
{
    /**
     * Осуществляет запись результатов работы программы в файл
     * @param filename - название записываемого файла, полученного из файла конфигурации
     * @param outputString - строка, подлежащая записи в файл
     */
    public int WriteText(String filename, byte[] outputString)
    {
        try(FileOutputStream writer = new FileOutputStream(filename))
        {
            writer.write(outputString);
        }
        catch(IOException writerError)
        {
           Errors.WriterError.GetError();
           return Errors.WriterError.ordinal();
        }
        return Errors.Successful.ordinal();
    }
}