import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class TextReader
{
    /**
     * Осуществляет чтение текстового файла и файла, содержащего ключ в строковые буферы
     */
    private File textFile, keyFile;
    private byte[] text, key;

    /**
     * Конструктор класса, содержащий инициализацию и проверку валидности файловых переменных
     * @param nameTextFile,nameKeyFile - имена соответсвующих файлов, полученных из файла конфигурации
     */
    public TextReader(String nameTextFile, String nameKeyFile)
    {
        textFile = new File(nameTextFile);
        keyFile = new File(nameKeyFile);
    }

    public byte[] GetText()
    {
        return text;
    }

    public byte[] GetKey()
    {
        return key;
    }

    /**
     * Считывание текста и ключа в строковые буферы
     * Буферы заполняются полным содержанием соответствующих файлов
     * В случае ошибки происходит логирование
     */
    public int ReadText()
    {
        if (!textFile.exists() || !keyFile.exists())
        {
            Errors.FileError.GetError();
            return Errors.FileError.ordinal();
        }

        try (FileInputStream textReader = new FileInputStream(textFile);
             FileInputStream keyReader = new FileInputStream(keyFile))
        {
            text = new byte[(int)textFile.length()];
            key = new byte[(int)keyFile.length()];

            for (int i = 0; i < textFile.length(); i++)
                textReader.read(text);
            for (int i = 0; i < keyFile.length(); i++)
                keyReader.read(key);
        }
        catch(IOException readerError)
        {
            Errors.ReaderError.GetError();
            return Errors.ReaderError.ordinal();
        }
        return Errors.Successful.ordinal();
    }
}
