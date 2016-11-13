import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.*;
import static org.junit.Assert.*;


public class WriterTest {

    private List<Character> _charList;
    private List<Character> _emptyList = new ArrayList<>();
    private ArrayList<Character> _nullList;
    private ByteArrayOutputStream _outText = new ByteArrayOutputStream();
    private ByteArrayOutputStream _errText = new ByteArrayOutputStream();

    @Before
    public void setUp() throws IOException {
        _charList = Arrays.asList('t','e','s','t');
        System.setOut(new PrintStream(_outText));
        System.setErr(new PrintStream(_errText));
    }


    //writeSolution tests


    //Structured basis good data
    //full coverage
    @Test
    public void Should_properly_write_list_to_file() throws FileNotFoundException {
        Writer.getInstance().writeSolution(_charList);
        assertEquals(_outText.toString(), "test");
    }

    //structured basis, boundary test
    //testing with an empty list as a data structure boundary
    @Test
    public void Should_write_nothing_if_list_is_empty() throws Exception{
        Writer.getInstance().writeSolution(_emptyList);
        assertEquals(_outText.toString(), "");
    }

    //bad data
    //null list is never going to be passed in, however there is not an explicit condition preventing it
    @Test(expected = NullPointerException.class)
    public void Should_error_if_list_to_print_is_null() throws FileNotFoundException {
        Writer.getInstance().writeSolution(_nullList);
    }


    // writeError tests


    //structured basis
    //full coverage
    @Test(expected = IllegalArgumentException.class)
    public void writeErrorTest() throws IOException {
        Writer.getInstance().writeError();

        assertEquals(_errText.toString(), "Error");
    }
}