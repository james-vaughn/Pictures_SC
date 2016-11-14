import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InputProcessorTest {

    InputProcessor.Exposer _inputProcessorExposer = InputProcessor.getInstance(). new Exposer();
    PictureProcessor.Exposer _pictureProcessorExposer = PictureProcessor.getInstance(). new Exposer();
    InputProcessor _inputProcessorInstance = InputProcessor.getInstance();
    ByteArrayOutputStream _errText = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        System.setErr(new PrintStream(_errText));
    }


    //processFileLine tests





    //validateLine tests


    //structured basis, bad data
    //goes into if branch
    @Test(expected = IllegalArgumentException.class)
    public void Should_write_error_if_line_is_null() {
        _inputProcessorExposer.validateLineExposed(null);
        assertEquals(_errText.toString(), "Error");
    }

    //bounds testing with empty string, good data
    //structured basis for the second branch
    @Test
    public void Should_not_error_given_a_non_null_line() {
        _inputProcessorExposer.validateLineExposed("");
        _inputProcessorExposer.validateLineExposed("test");
        assertEquals(_errText.toString(), "");
    }


    //isLineIndicatingGridSize tests


    //structured basis, good data
    @Test
    public void Should_return_true_if_line_index_is_1_or_2() {
        assertTrue(_inputProcessorExposer.isLineIndicatingGridSizeExposed(1));
        assertTrue(_inputProcessorExposer.isLineIndicatingGridSizeExposed(2));
    }

    //structured basis, good data
    @Test
    public void Should_return_false_otherwise() {
        assertFalse(_inputProcessorExposer.isLineIndicatingGridSizeExposed(-4));
        assertFalse(_inputProcessorExposer.isLineIndicatingGridSizeExposed(0));
        assertFalse(_inputProcessorExposer.isLineIndicatingGridSizeExposed(3));
        assertFalse(_inputProcessorExposer.isLineIndicatingGridSizeExposed(100));
    }

    //there is no bad data as ints cannot be null


    //handleDimensions tests


    //Structured basis, good data
    //enters 1st branch
    @Test
    public void Should_send_dimension_to_processing_if_the_dimension_is_valid() {
        assertEquals(_pictureProcessorExposer.getRowCount(), 0);
        _inputProcessorExposer.handleDimensionsExposed("5");
        assertEquals(_pictureProcessorExposer.getRowCount(), 5);
        assertEquals(_errText.toString(), "");
    }

    //Structured basis, good data
    //does not enter the first branch
    @Test(expected = IllegalArgumentException.class)
    public void Should_error_if_dimension_line_is_invalid() {
        _inputProcessorExposer.handleDimensionsExposed("five");
        assertEquals(_errText.toString(), "Error");
    }

    //Bad data test
    //This condition should be unreachable
    @Test(expected = NullPointerException.class)
    public void Should_error_if_input_line_is_null() {
        _inputProcessorExposer.handleDimensionsExposed(null);
    }


    //isDimension tests


    //Structured basis, good data
    //Full coverage
    @Test
    public void Should_return_true_if_line_is_a_valid_number_but_not_starting_with_a_zero() {
        assertTrue(_inputProcessorExposer.isDimensionExposed("1"));
        assertTrue(_inputProcessorExposer.isDimensionExposed("421"));
        assertTrue(_inputProcessorExposer.isDimensionExposed("30"));
    }

    //Structured basis, good data
    //handles case where regex fails
    @Test
    public void Should_return_false_if_input_line_is_not_a_number_or_begins_with_a_zero() {
        assertFalse(_inputProcessorExposer.isDimensionExposed("five"));
        assertFalse(_inputProcessorExposer.isDimensionExposed("01"));
        assertFalse(_inputProcessorExposer.isDimensionExposed("0"));
        assertFalse(_inputProcessorExposer.isDimensionExposed(""));
    }

    //Bad data
    //This case should never be reached
    @Test(expected = NullPointerException.class)
    public void Should_error_if_input_is_null() {
        _inputProcessorExposer.isDimensionExposed(null);
    }


    //sendDimensionToProcessing tests





    //handlePictureLines tests





    //isPicture tests





    //sendPictureToProcessing tests





    //processEmptyLine tests



}
