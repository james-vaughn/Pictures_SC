import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

//reader handles reading in from stdin
public class Reader {

    private final static String INPUT_FILE_PATH = "src/hw10in.txt";

    //main for whole program
    public static void main(String[] args) {
        try {
            processInputFile(INPUT_FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processInputFile(String filePath) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(filePath));

        List<String> listOfLines = fileReader.lines().collect(Collectors.toList());
        int lengthOfTextFile = listOfLines.size();
        InputProcessor inputProc = InputProcessor.getInstance();
        for (int i = 0; i < (lengthOfTextFile); i++) {
            // If this is not the last line of the file
            if (i !=  (lengthOfTextFile - 1) ) {
                inputProc.processFileLine(listOfLines.get(i) );
            } else {
                PictureProcessor.getInstance().setIsLastLineToTrue();
                inputProc.processFileLine(listOfLines.get(i) );
            }
        }

    }
}
