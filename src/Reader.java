import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Reader {

    //singleton
    private static final Reader READER_INSTANCE = new Reader();
    private final static String INPUT_FILE_PATH = "src/Hw10in.txt";

    private Reader() {

    }

    public static Reader getInstance() {
        return READER_INSTANCE;
    }

    //main for whole program
    public static void main(String [ ] args) {
        try {
            Reader.getInstance().processInputFile(INPUT_FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processInputFile(String filePath) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(filePath));

        List<String> fileLines = fileReader.lines().collect(Collectors.toList());
        fileLines.forEach(line -> InputProcessor.getInstance().processFileLine(line) );

    }
}