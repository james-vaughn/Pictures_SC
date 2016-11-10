import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by james on 11/10/16.
 */
public class Writer {

    private static Writer WRITER_INSTANCE;
    private final File outputFile;
    private final String fileName = "hw10out.txt";
    private PrintWriter writer;

    private Writer() {
        outputFile = new File(fileName);
    }

    public static Writer getInstance() {
        return WRITER_INSTANCE;
    }

    //throw an error if the writer cannot write as there is no alternative
    private void writeSolution(List<Character> overlapOrder) throws FileNotFoundException {
        try {
            PrintWriter writer = new PrintWriter(outputFile);
            for (Character layer : overlapOrder) {
                writer.print(layer);
            }
            writer.flush();
        } catch (FileNotFoundException e) {
            throw e;
        } finally {
            writer.close();
        }
    }

    private void writeError() throws FileNotFoundException {
        try {
            writer = new PrintWriter(outputFile);
            writer.println("Error");
            writer.flush();
        } catch (FileNotFoundException e) {
            throw e;
        } finally {
            writer.close();
        }
    }
}
