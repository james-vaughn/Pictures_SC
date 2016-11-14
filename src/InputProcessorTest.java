import jdk.nashorn.internal.objects.NativeUint8Array;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InputProcessorTest {

    private InputProcessor.Exposer _inputProcessorExposer;
    PictureProcessor.Exposer _pictureProcessorExposer;
    InputProcessor _inputProcessorInstance = InputProcessor.getInstance();
    ByteArrayOutputStream _errText = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        _inputProcessorExposer = InputProcessor.getInstance(). new Exposer();
        _pictureProcessorExposer = PictureProcessor.getInstance(). new Exposer();
        System.setErr(new PrintStream(_errText));
    }


    //processFileLine tests


    //Structured basis, good data
    //


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
        assertEquals(_errText.toString(), "");
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
        assertEquals(_errText.toString(), "");
    }


    //sendDimensionToProcessing tests


    //structured basis, good data
    @Test
    public void Should_send_dimension_to_processing() {
        assertEquals(_pictureProcessorExposer.getRowCount(), 0);
        _inputProcessorExposer.sendDimensionToProcessingExposed("7");
        assertEquals(_pictureProcessorExposer.getRowCount(), 7);
    }

    //dont need bad data testing as this method is only called after assuring good data


    //handlePictureLines tests


    //structure basis, good data
    //enters the 1st if branch
    @Test
    public void Should_send_to_processing_if_line_is_a_valid_picture_line() {
        _pictureProcessorExposer.setRowCountAndColCount(5,5);
        assertEquals(_pictureProcessorExposer.getPictureRowIndex(), 0);
        _inputProcessorExposer.handlePictureLinesExposed(". . . . .");
        assertEquals(_pictureProcessorExposer.getPictureRowIndex(), 1);

        _inputProcessorExposer.handlePictureLinesExposed("A . . A .");
        assertEquals(_pictureProcessorExposer.getPictureRowIndex(), 2);

        assertEquals(_errText.toString(), "");
    }

    //Structured basis, bad data
    //Doesnt enter the 1st branch, but instead errors
    @Test(expected = IllegalArgumentException.class)
    public void Should_error_if_picture_line_is_invalid() {
        _inputProcessorExposer.handlePictureLinesExposed("");
        assertEquals(_errText.toString(), "Error");
    }

    //Bad data
    //This should never be a reachable state due to earlier checking, so an error is expected
    @Test(expected = NullPointerException.class)
    public void Should_error_if_picture_input_is_null() {
        _inputProcessorExposer.handlePictureLinesExposed(null);
        assertEquals(_errText.toString(), "");
    }


    //isPicture tests


    //structured basis, good data
    //full coverage; covers matches
    @Test
    public void Should_return_true_if_line_is_a_valid_picture_row() {
        assertTrue(_inputProcessorExposer.isPictureExposed("A . A . A"));
        assertTrue(_inputProcessorExposer.isPictureExposed(". . . . ."));
        assertTrue(_inputProcessorExposer.isPictureExposed("B A C F .")); //valid for final, stacked picture
    }

    //structured basis, good data
    //full coverage; covers no matches
    @Test
    public void Should_return_false_if_line_is_not_a_valid_picture_row() {
        assertFalse(_inputProcessorExposer.isPictureExposed(" . . . .")); //begins with a space
        assertFalse(_inputProcessorExposer.isPictureExposed(". . . . ")); //ends with a space
        assertFalse(_inputProcessorExposer.isPictureExposed(". . % .")); //contains bad character
        assertFalse(_inputProcessorExposer.isPictureExposed(". w . .")); //contains lowercase character
        assertFalse(_inputProcessorExposer.isPictureExposed("")); //contains nothing
    }

    //bad data; due to prior checking this should never happen, so an error is thrown
    @Test(expected = NullPointerException.class)
    public void Should_error_if_input_line_to_isPicture_is_null() {
        _inputProcessorExposer.isPictureExposed(null);
    }


    //sendPictureToProcessing tests


    //structured basis, good data
    @Test
    public void Should_send_line_to_picture_processing() {
        _pictureProcessorExposer.setRowCountAndColCount(3,3);
        assertEquals(_pictureProcessorExposer.getPictureRowIndex(), 0);
        _inputProcessorExposer.sendPictureToProcessingExposed(". . .");
        assertEquals(_pictureProcessorExposer.getPictureRowIndex(), 1);
        _inputProcessorExposer.sendPictureToProcessingExposed(". . .");
        assertEquals(_pictureProcessorExposer.getPictureRowIndex(), 2);

    }

    //bad data, should never be a reachable state due to prior checks
    @Test(expected = NullPointerException.class)
    public void Should_error_if_input_string_to_picture_processor_is_null() {
        _inputProcessorExposer.sendPictureToProcessingExposed(null);
    }


    //processEmptyLine tests


    //structure basis, full coverage
    @Test
    public void Should_send_empty_line_to_processing() {
        _pictureProcessorExposer.setShouldBeEmptyLine(true);
        _inputProcessorExposer.processEmptyLineExposed();
        assertFalse(_pictureProcessorExposer.getShouldBeEmptyLine());
    }

}
