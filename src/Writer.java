import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class Writer {

    private static Writer WRITER_INSTANCE = new Writer();
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
    public void writeSolution(List<Character> overlapOrder) {
        try {
            writer = new PrintWriter(outputFile);
            for (Character layer : overlapOrder) {
                writer.print(layer);
            }
            writer.flush();
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } finally {
            writer.close();
        }
    }

    public void writeError() {
        try {
            writer = new PrintWriter(outputFile);
            writer.println("Error");
            writer.flush();
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } finally {
            writer.close();
            System.exit(1);
        }
    }
}
