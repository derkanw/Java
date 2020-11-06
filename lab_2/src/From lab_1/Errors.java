import java.util.logging.Logger;
import java.util.logging.Level;

enum Errors
{
    /**
     * Содержит предопределенные типы ошибок и соответсвующее значение сообщения об ошибке
     * Имеет метод GetError для логирования возникающей ошибки
     * Используется для уведомления пользователя при возникновении различных ошибок в методах решения задачи
     */
    Successful("Work completed successfully"),
    ReaderError("File read error"),
    WriterError("File write error"),
    FileError("File not found"),
    ArgsError("No program start arguments"),
    ConfigError("Configuration file read error"),
    SeparatorError("Required separator not found"),
    CasesError("Matching literal not found");

    private final String message;
    Errors(String message)
    {
        this.message = message;
    }

    private Logger logger = Logger.getLogger(Errors.class.getName());
    public void GetError()
    {
        logger.log(Level.WARNING, message);
    }
    public void GetSuccess()
    {
        logger.log(Level.INFO, message);
    }
}