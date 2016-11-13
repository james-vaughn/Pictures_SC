import java.io.PrintWriter;
import java.util.List;

//writer handles printing to the stdout the solution
public class Writer {

    private static Writer WRITER_INSTANCE = new Writer();
    private PrintWriter writer;

    private Writer() {
    }

    public static Writer getInstance() {
        return WRITER_INSTANCE;
    }

    //throw an error if the writer cannot write as there is no alternative
    public void writeSolution(List<Character> overlapOrder) {
        writer = new PrintWriter(System.out);

        for (Character layer : overlapOrder) {
            writer.print(layer);
        }

        writer.flush();
        writer.close();
    }

    //print error and then throw an error to terminate
    //changed from the System.exit(1) for testing purposes
    public void writeError() {
        System.err.println("Error");
        throw new IllegalArgumentException("Input was invalid");
    }
}
