import ru.spbstu.pipeline.RC;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public abstract class ConfigFile
{
    private final static String separator = "=";
    public final static int numGramUnits = 2;
    public final static int numLiteral = 0;
    public final static int numValue = 1;

    public RC readConfig(String nameConfigFile)
    {
        try (Scanner scanner = new Scanner(new FileReader(nameConfigFile))) {
            String line;
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (line.indexOf(separator) == -1)
                    return RC.CODE_CONFIG_GRAMMAR_ERROR;

                line = line.replaceAll("\\s+", "");
                String[] params = line.split(separator, numGramUnits);
                if (processLine(params) != RC.CODE_SUCCESS)
                    return RC.CODE_CONFIG_SEMANTIC_ERROR;
            }
        } catch (IOException configError) {
            return RC.CODE_FAILED_TO_READ;
        }
        return RC.CODE_SUCCESS;
    }

    public abstract RC processLine(String[] params);
}