import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;


public class PictureProcessorTest {
    private PictureProcessor _pictureProcessor = PictureProcessor.getInstance();
    private PictureProcessor.Exposer _pictureProcessorExposer = _pictureProcessor. new Exposer();
    private InputProcessor.Exposer _inputProcessorExposer = InputProcessor.getInstance(). new Exposer();

    private ByteArrayOutputStream _errText;

    @Before
    public void setUp() {
        _errText = new ByteArrayOutputStream();
        System.setErr(new PrintStream(_errText));

        _pictureProcessorExposer.setLetters(new HashSet<>() );
    }


    //processDimensionLine


    //Structure basis
    //1st branch entered
    @Test
    public void Should_set_row_count_for_first_line_number() {
        _pictureProcessorExposer.setRowCountAndColCount(10,1);
        assertEquals(10, _pictureProcessorExposer.getRowCount() );

        _inputProcessorExposer.setLineIndex(1);
        _pictureProcessor.processDimensionLine("20");
        assertEquals(20, _pictureProcessorExposer.getRowCount() );
    }

    //Structured Basis
    //1st branch passed
    @Test
    public void Should_set_col_count_for_second_line_number() {
        _pictureProcessorExposer.setRowCountAndColCount(1,10);
        assertEquals(10, _pictureProcessorExposer.getColCount() );

        _inputProcessorExposer.setLineIndex(2);
        _pictureProcessor.processDimensionLine("20");
        assertEquals(20, _pictureProcessorExposer.getColCount() );
    }

    //bad data is blocked by the barricade


    //processPictureLine


    // Structured Basis
    // takes 1st branch
    @Test (expected=IllegalArgumentException.class)
    public void Should_error_if_process_picture_line_should_be_processing_an_empty_line() {
        _pictureProcessorExposer.setShouldBeEmptyLine(true);

        _pictureProcessor.processPictureLine("A A . A A .");
        assertEquals("Error", _errText.toString() );
    }

    // Structured Basis
    // Skips 1st branch
    // Enters 2nd and 3rd branch
    @Test
    public void Should_process_the_last_line_of_a_picture() {
        _pictureProcessorExposer.setRowCountAndColCount(1,6);
        _pictureProcessorExposer.setPictureRowIndex(0);

        _pictureProcessorExposer.setIsLastLine(false);
        _pictureProcessorExposer.setShouldBeEmptyLine(false);

        _pictureProcessorExposer.setCurrPicture(
                _pictureProcessorExposer.createNewTempPicture(new ArrayList<>() ) );

        assertFalse(_pictureProcessorExposer.getShouldBeEmptyLine() );
        _pictureProcessor.processPictureLine("A A . A A .");
        assertTrue(_pictureProcessorExposer.getShouldBeEmptyLine() );
    }

    //I will not test the 3rd if is false as it runs through the rest of the program, casusing issues
    //Also, the processLastLine tests exist elsewhere

    // Structured Basis
    // All branches skipped
    @Test
    public void Should_process_a_standard_picture_line() {
        _pictureProcessorExposer.setRowCountAndColCount(5,6);
        _pictureProcessorExposer.setPictureRowIndex(0);
        assertEquals(0, _pictureProcessorExposer.getPictureRowIndex() );

        _pictureProcessorExposer.setIsLastLine(true);
        _pictureProcessorExposer.setShouldBeEmptyLine(false);
        _pictureProcessorExposer.setPictures(new ArrayList<>(Arrays.asList(
                new Picture('A', new char[1][1]) ) ) );

        _pictureProcessor.processPictureLine("A A . A A .");
        assertEquals(1, _pictureProcessorExposer.getPictureRowIndex() );
        assertEquals("", _errText.toString() );
    }


    //validatePictureWidth


    //Structured Basis
    //1st branch passed
    @Test
    public void Should_not_error_if_picture_width_is_correct() {
        _pictureProcessorExposer.setRowCountAndColCount(1,10);

        _pictureProcessorExposer.validatePictureWidthExposed("abcdefghij");
        assertEquals("", _errText.toString() );
    }

    //structure basis
    //1st branch enter
    @Test (expected=IllegalArgumentException.class)
    public void Should_error_if_picture_width_is_wrong() {
        _pictureProcessorExposer.setRowCountAndColCount(1,11);

        _pictureProcessorExposer.validatePictureWidthExposed("abcdefghij");
        assertEquals("Error", _errText.toString());
    }


    //validatePictureHeight


    //boundary test, structured basis
    //picture height < row count
    //skip branch
    @Test
    public void Should_be_valid_if_picture_height_is_smaller_than_num_rows() {
        _pictureProcessorExposer.setPictureRowIndex(8);
        _pictureProcessorExposer.setRowCountAndColCount(10,1);

        _pictureProcessorExposer.validatePictureHeightExposed();
        assertEquals("", _errText.toString() );
    }

    //boundary test
    //height == row count
    @Test
    public void Should_be_valid_if_picture_height_is_equal_to_num_rows() {
        _pictureProcessorExposer.setPictureRowIndex(10);
        _pictureProcessorExposer.setRowCountAndColCount(10,1);

        _pictureProcessorExposer.validatePictureHeightExposed();
        assertEquals("", _errText.toString() );
    }

    //boundary test, structured basis
    //picture height > row count
    //enter branch
    @Test (expected=IllegalArgumentException.class)
    public void Should_not_be_valid_if_picture_over_row_count() {
        _pictureProcessorExposer.setPictureRowIndex(12);
        _pictureProcessorExposer.setRowCountAndColCount(10,1);

        _pictureProcessorExposer.validatePictureHeightExposed();
        assertEquals("Error", _errText.toString() );
    }


    //processEmptyLine


    //Structured Basis
    //1st branch entered
    @Test (expected=IllegalArgumentException.class)
    public void Should_error_if_non_empty_line_is_processed() {
        _pictureProcessorExposer.setShouldBeEmptyLine(false);
        _pictureProcessorExposer.processEmptyLineExposed();

        assertEquals("Error", _errText.toString() );
    }

    //Structured Basis
    //1st branch passed
    @Test
    public void Should_set_should_be_empty_line_back_to_false() {
        _pictureProcessorExposer.setShouldBeEmptyLine(true);

        _pictureProcessorExposer.processEmptyLineExposed();
        assertFalse(_pictureProcessorExposer.getShouldBeEmptyLine());
    }


    //validatePictureList


    //structured basis, boundary
    //1st branch entered
    //pictures size < 1
    @Test (expected=IllegalArgumentException.class)
    public void Should_error_if_pictures_does_not_contain_at_least_one_picture() {
        _pictureProcessorExposer.setPictures(new ArrayList<>());

        _pictureProcessorExposer.validatePictureListExposed();
        assertEquals("Error", _errText.toString() );
    }

    //structured basis, boundary
    //1st branch missed (pictures size == 1)
    //2nd branch entered
    @Test (expected=IllegalArgumentException.class)
    public void Should_error_if_picture_list_size_is_at_least_1_but_final_picture_is_missing() {
        ArrayList<Picture> pictureList = new ArrayList<>(Arrays.asList(
                new Picture('A', new char[1][1]) ) );
        _pictureProcessorExposer.setPictures(pictureList);
        _pictureProcessorExposer.setFinalPicture(null);

        _pictureProcessorExposer.validatePictureListExposed();
        assertEquals("Error", _errText.toString() );
    }


    //structured basis, boundary
    //picture size > 1, final picture not null
    @Test
    public void Should_not_error_if_picture_list_size_over_1_and_final_picture_not_null() {
        ArrayList<Picture> pictureList = new ArrayList<>(Arrays.asList(
                new Picture('A', new char[1][1]),
                new Picture('A', new char[1][1]) ) );
        _pictureProcessorExposer.setPictures(pictureList);
        _pictureProcessorExposer.setFinalPicture(new Picture('A', new char[1][1]) );

        _pictureProcessorExposer.validatePictureListExposed();
        assertEquals("" , _errText.toString() );
    }


    //createNewPicture


    //structured basis
    //enters branch
    @Test
    public void Should_add_new_picture_to_pictures_list_if_not_last_line() {
        _pictureProcessorExposer.setPictures(new ArrayList<>());
        assertEquals(new ArrayList<Picture>(), _pictureProcessorExposer.getPictures() );
        _pictureProcessorExposer.setIsLastLine(false);

        _pictureProcessorExposer.setRowCountAndColCount(1,1);

        _pictureProcessorExposer.setCurrPicture(
                _pictureProcessorExposer.createNewTempPicture(
                        new ArrayList<>(Arrays.asList("A") ) ) );

        _pictureProcessorExposer.createNewPictureExposed();
        assertEquals('A', _pictureProcessorExposer.getPictures().get(0).getLetter() );
    }

    // Structured Basis
    // passes branch
    @Test
    public void Should_add_new_picture_as_final_picture_if_last_line() {
        _pictureProcessorExposer.setFinalPicture(new Picture('A', new char[1][1]) );
        assertEquals('A', _pictureProcessorExposer._getFinalPicture().getLetter());

        _pictureProcessorExposer.setIsLastLine(true);

        _pictureProcessorExposer.setRowCountAndColCount(1,1);

        _pictureProcessorExposer.setLetters(new HashSet<>(
                Arrays.asList('B') ) );
        _pictureProcessorExposer.setCurrPicture(
                _pictureProcessorExposer.createNewTempPicture(
                        new ArrayList<>(Arrays.asList("B"))));

        _pictureProcessorExposer.createNewPictureExposed();
        assertEquals('-', _pictureProcessorExposer._getFinalPicture().getLetter() );
    }


    //processedTemporaryPicture


    //structured basis
    //enters 1st branch
    @Test
    public void Should_convert_temporary_picture_into_a_normal_picture() {
        _pictureProcessorExposer.setIsLastLine(false);

        _pictureProcessorExposer.setRowCountAndColCount(1,1);

        char[][] testPictureMatrix = {{'A'}};
        Picture testPicture = new Picture('A', testPictureMatrix);

        _pictureProcessorExposer.setCurrPicture(
                _pictureProcessorExposer.createNewTempPicture(
                        new ArrayList<>(Arrays.asList("A"))));
        assertEquals(testPicture.getLetter(),
                _pictureProcessorExposer.processTemporaryPictureExposed().getLetter());
    }

    //structured basis
    //passes 1st branch
    @Test (expected=IllegalArgumentException.class)
    public void Should_throw_error_if_temporary_picture_letter_is_pipe() {
        _pictureProcessorExposer.setIsLastLine(false);

        _pictureProcessorExposer.setRowCountAndColCount(1,1);

        _pictureProcessorExposer.setCurrPicture(
                _pictureProcessorExposer.createNewTempPicture(
                        new ArrayList<>(Arrays.asList("|"))));

        _pictureProcessorExposer.processTemporaryPictureExposed();
        assertEquals("Error", _errText.toString());
    }


    //pictureLetter


    //structured Basis
    //1st branch entered
    @Test
    public void Should_return_picture_letter_for_a_non_final_picture_if_it_isnt_the_last_line() {
        _pictureProcessorExposer.setIsLastLine(false);
        char[][] testPictureMatrix = {{'A'}};
        assertEquals('A', _pictureProcessorExposer.pictureLetterExposed(testPictureMatrix) );
    }

    //structured Basis
    //1st branch passed
    @Test
    public void Should_return_picture_letter_for_the_final_stacked_picture_if_we_are_at_the_last_line() {
        _pictureProcessorExposer.setIsLastLine(true);
        char[][] testPictureMatrix = {{'A'}};
        _pictureProcessorExposer.setLetters(new HashSet<>(
                Arrays.asList('A') ) );

        assertEquals('-', _pictureProcessorExposer.pictureLetterExposed(testPictureMatrix) );
    }


    //pictureLetterForNonFinalPicture


    // Structured Basis
    // 1st branch entered
    @Test
    public void Should_return_the_unique_letter_of_the_image() {
        assertEquals('A', _pictureProcessorExposer.pictureLetterForNonFinalPictureExposed(
                new HashSet<>(Arrays.asList('A') ) ) );
    }

    // Structured Basis
    // 1st branch passed
    @Test (expected=IllegalArgumentException.class)
    public void Should_error_if_multiple_unique_letters_are_within_an_image() {
        assertEquals('A', _pictureProcessorExposer.pictureLetterForNonFinalPictureExposed(
                new HashSet<>(Arrays.asList('A', 'B') ) ) );
        assertEquals("Error", _errText.toString());
    }


    //pictureLetterForFinalPicture


    //Structured basis
    //if branch entered
    @Test
    public void Should_return_the_character_used_for_final_images_if_the_set_of_unique_letters_matches_the_input_set() {
        _pictureProcessorExposer.setLetters(new HashSet<>(
                Arrays.asList('A', 'B', 'C') ) );
        HashSet<Character> testSet = new HashSet<>(
                Arrays.asList('A', 'B', 'C') );

        assertEquals('-', _pictureProcessorExposer.pictureLetterForFinalPictureExposed(testSet));
    }

    //Structured basis
    //if missed
    @Test (expected=IllegalArgumentException.class)
    public void Should_error_if_letters_set_differs_from_the_input_set() {
        _pictureProcessorExposer.setLetters(new HashSet<>(
                Arrays.asList('A', 'B', 'C') ) );
        HashSet<Character> testSet = new HashSet<>(
                Arrays.asList('A', 'B', 'Z') );
        _pictureProcessorExposer.pictureLetterForFinalPictureExposed(testSet);

        assertEquals("Error", _errText.toString() );
    }


    //uniqueLetters


    //structured basis
    //full coverage with 1 letter
    @Test
    public void Should_return_set_with_just_one_letter() {
        char[][] testPictureMatrix =
                       {{'A', 'A', '.'},
                        {'A', '.', 'A'},
                        {'A', '.', 'A'}, };

        assertEquals(new HashSet<>(Arrays.asList('A') ),
                _pictureProcessorExposer.uniqueLettersExposed(testPictureMatrix) );
    }

    //structured basis
    //full coverage with multiple letters
    @Test
    public void Should_return_set_with_all_used_letters() {
        char[][] testPictureMatrix =
                        {{'A', 'B', '.'},
                        {'A', '.', 'C'},
                        {'D', '.', 'A'}, };

        assertEquals(new HashSet<>(Arrays.asList('A', 'B', 'C', 'D') ),
                _pictureProcessorExposer.uniqueLettersExposed(testPictureMatrix) );
    }

    // Structured Basis
    // Only enter the for loops. Do not enter the if branch
    @Test
    public void Should_return_empty_set_for_all_dot_matrix() {
        char[][] testPictureMatrix =
                       {{'.', '.', '.'},
                        {'.', '.', '.'},
                        {'.', '.', '.'}, };

        assertEquals(new HashSet<>(),
                _pictureProcessorExposer.uniqueLettersExposed(testPictureMatrix) );
    }

    // Structured Basis
    // Never don't take the if branch
    @Test
    public void Should_return_proper_set_even_without_dots() {
        char[][] testPictureMatrix =
                       {{'A', 'A', 'A'},
                        {'A', 'A', 'A'},
                        {'A', 'A', 'A'}, };

        assertEquals(new HashSet<>(Arrays.asList('A') ),
                _pictureProcessorExposer.uniqueLettersExposed(testPictureMatrix) );
    }

    // Structured Basis
    // Do not enter the for loops
    @Test
    public void Should_return_empty_set_given_empty_input() {
        char[][] testPictureMatrix = {{}};
        assertEquals(new HashSet<Character>(),
                _pictureProcessorExposer.uniqueLettersExposed(testPictureMatrix) );
    }

    // Bad Data
    @Test (expected=NullPointerException.class)
    public void Should_error_given_null_data() {
        _pictureProcessorExposer.uniqueLettersExposed(null);
    }


    //uniqueLetter


    //structured basis
    //1st branch missed
    @Test
    public void Should_return_picture_letter_if_the_letter_has_not_been_used_earlier() {
        _pictureProcessorExposer.setLetters(new HashSet<>(Arrays.asList('A')));

        assertEquals('B', _pictureProcessorExposer.uniqueLetterExposed('B'));
    }

    //structured basis
    //1st branch entered
    @Test (expected=IllegalArgumentException.class)
    public void Should_error_if_the_letter_was_used_earlier() {
        _pictureProcessorExposer.setLetters(new HashSet<>(
                Arrays.asList('A') ) );
        _pictureProcessorExposer.uniqueLetterExposed('A');

        assertEquals("Error", _errText.toString());
    }


    //processTemporaryMatrix


    //structured basis
    //full coverage
    @Test
    public void Should_extract_the_picture_matrix_given_standard_input() {
        _pictureProcessorExposer.setRowCountAndColCount(1,1);

        _pictureProcessorExposer.setCurrPicture(
                _pictureProcessorExposer.createNewTempPicture(
                        new ArrayList<>(Arrays.asList("B"))));

        char[][] testPictureMatrix = {{'B'}};

        assertArrayEquals(testPictureMatrix,
                _pictureProcessorExposer.processTemporaryMatrixExposed());
    }

    // Structured Basis
    // Temporary matrix with multiple rows/cols
    @Test
    public void Should_extract_picture_from_multi_row_and_col_inputs() {
        _pictureProcessorExposer.setRowCountAndColCount(3,3);

        _pictureProcessorExposer.setCurrPicture(
                _pictureProcessorExposer.createNewTempPicture(
                        new ArrayList<>(Arrays.asList(
                                "B.B",
                                "...",
                                ".B."))));

        char[][] testPictureMatrix = {
                {'B', '.', 'B'},
                {'.', '.', '.'},
                {'.', 'B', '.'}};

        assertArrayEquals(testPictureMatrix,
                _pictureProcessorExposer.processTemporaryMatrixExposed());
    }

    // Structured Basis
    // multi rows, single col
    @Test
    public void Should_extract_picture_from_multi_row_but_single_col_input() {
        _pictureProcessorExposer.setRowCountAndColCount(3,1);

        _pictureProcessorExposer.setCurrPicture(
                _pictureProcessorExposer.createNewTempPicture(
                        new ArrayList<>(Arrays.asList(
                                "B",
                                ".",
                                "."))));

        char[][] testPictureMatrix = {
                {'B'},
                {'.'},
                {'.'}};

        assertArrayEquals(testPictureMatrix,
                _pictureProcessorExposer.processTemporaryMatrixExposed());
    }
}