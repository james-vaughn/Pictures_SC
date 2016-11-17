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


    //processPictureLine


    // Structured Basis
    // takes 1st branch
    @Test (expected=IllegalArgumentException.class)
    public void testProcessPictureLine_() {
        _pictureProcessorExposer.setShouldBeEmptyLine(true);
        _pictureProcessor.processPictureLine("A A . A A .");
        assertEquals("Error", _errText.toString() );
    }

    // Structured Basis
    // Skips 1st branch
    // Enters 2nd and 3rd branch
    @Test
    public void testProcessPictureLine() {
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

    // Structured Basis 3: First if is false, second if is true,
    //  third if is false
    @Test
    public void testProcessPictureLineSB3() {
        _pictureProcessorExposer.setRowCountAndColCount(1,6);
        _pictureProcessorExposer.setPictureRowIndex(0);
        // validatePictureList requires at least one picture in PICTURE_LIST
        _pictureProcessorExposer.setPictures(new ArrayList<>(Arrays.asList(
                new Picture('A', new char[1][1]) ) ) );

        _pictureProcessorExposer.setLetters(new HashSet<>(
                Arrays.asList('A') ) );

        _pictureProcessorExposer.setCurrPicture(
                _pictureProcessorExposer.createNewTempPicture(new ArrayList<>() ) );

        _pictureProcessorExposer.setIsLastLine(true);
        _pictureProcessorExposer.setShouldBeEmptyLine(false);

        _pictureProcessor.processPictureLine("A A . A A .");
    }

    // Structured Basis 4: First if is false, second if is false
    @Test
    public void testProcessPictureLineSB4() {
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


    // Structured Basis 1: First if is true, so processDimensionLine sets the
    //  number of rows
    @Test
    public void testProcessDimensionLineSB1() {
        _pictureProcessorExposer.setRowCountAndColCount(10,1);
        assertEquals(10, _pictureProcessorExposer.getRowCount() );

        _inputProcessorExposer.setLineIndex(1);
        _pictureProcessor.processDimensionLine("20");
        assertEquals(20, _pictureProcessorExposer.getRowCount() );
    }

    // Structured Basis 2: First if is false, so processDimensionLine sets the
    //  number of columns
    @Test
    public void testProcessDimensionLineSB2() {
        _pictureProcessorExposer.setRowCountAndColCount(1,10);
        assertEquals(10, _pictureProcessorExposer.getColCount() );

        _inputProcessorExposer.setLineIndex(2);
        _pictureProcessor.processDimensionLine("20");
        assertEquals(20, _pictureProcessorExposer.getColCount() );
    }

    // Structured Basis 1: If is true
    @Test (expected=IllegalArgumentException.class)
    public void testProcessEmptyLineSB1() {
        _pictureProcessorExposer.setShouldBeEmptyLine(false);
        _pictureProcessorExposer.processEmptyLineExposed();

        assertEquals("Error", _errText.toString() );
    }

    // Structured Basis 2: If is false
    @Test
    public void testProcessEmptyLineSB2() {
        _pictureProcessorExposer.setShouldBeEmptyLine(true);
        _pictureProcessorExposer.processEmptyLineExposed();
        assertFalse(_pictureProcessorExposer.getShouldBeEmptyLine());
    }

    // Structured Basis 1: Picture width is equal to the number of columns
    @Test
    public void testValidatePictureWidthNominal() {
        _pictureProcessorExposer.setRowCountAndColCount(1,10);

        _pictureProcessorExposer.validatePictureWidthExposed("abcdefghij");
        assertEquals("", _errText.toString() );
    }

    // Structured Basis 2: Picture width is not equal to the number of columns
    @Test (expected=IllegalArgumentException.class)
    public void testValidatePictureWidthUnequal() {
        _pictureProcessorExposer.setRowCountAndColCount(1,11);

        _pictureProcessorExposer.validatePictureWidthExposed("abcdefghij");
        assertEquals("Error", _errText.toString());
    }

    // Structured Basis 1: Picture height is less than the number of rows
    // Boundary 1: Picture height is less than the number of rows
    @Test
    public void testValidatePictureHeightBound1() {
        _pictureProcessorExposer.setPictureRowIndex(8);
        _pictureProcessorExposer.setRowCountAndColCount(10,1);

        _pictureProcessorExposer.validatePictureHeightExposed();
        assertEquals("", _errText.toString() );
    }

    // Structured Basis 1: Picture height equals the number of rows
    // Boundary 2: Picture height equals the number of rows
    @Test
    public void testValidatePictureHeightBound2() {
        _pictureProcessorExposer.setPictureRowIndex(10);
        _pictureProcessorExposer.setRowCountAndColCount(10,1);

        _pictureProcessorExposer.validatePictureHeightExposed();
        assertEquals("", _errText.toString() );
    }

    // Structured Basis 2: Picture height is greater than the number of rows
    // Boundary 3: Picture height is greater than the number of rows
    @Test (expected=IllegalArgumentException.class)
    public void testValidatePictureHeightBound3() {
        _pictureProcessorExposer.setPictureRowIndex(12);
        _pictureProcessorExposer.setRowCountAndColCount(10,1);

        _pictureProcessorExposer.validatePictureHeightExposed();
        assertEquals("Error", _errText.toString() );
    }

    // Structured Basis 1: Empty line not expected
    @Test (expected=IllegalArgumentException.class)
    public void testProcessEmptyLineNotExpected() {
        _pictureProcessorExposer.setShouldBeEmptyLine(false);

        _pictureProcessorExposer.processEmptyLineExposed();
        assertEquals("Error", _errText.toString());
    }

    // Structured Basis 2: Empty line expected
    @Test
    public void testProcessEmptyLineExpected() {
        _pictureProcessorExposer.setShouldBeEmptyLine(true);

        _pictureProcessorExposer.processEmptyLineExposed();
        assertEquals(false, _pictureProcessorExposer.getShouldBeEmptyLine());
    }


    // Structured Basis 1: Picture list size less than 1, first if is true
    // Boundary 1: Picture list size less than 1
    @Test (expected=IllegalArgumentException.class)
    public void testValidatePictureListBound1() {
        _pictureProcessorExposer.setPictures(new ArrayList<>());

        _pictureProcessorExposer.validatePictureListExposed();
        assertEquals("Error", _errText.toString() );
    }

    // Structured Basis 2: First if is false, else if is true
    // Boundary 2: Picture list size is equal to 1, stacked picture is null
    @Test (expected=IllegalArgumentException.class)
    public void testValidatePictureListBound2() {
        ArrayList<Picture> pictureList = new ArrayList<>(Arrays.asList(
                new Picture('A', new char[1][1]) ) );
        _pictureProcessorExposer.setPictures(pictureList);
        _pictureProcessorExposer.setFinalPicture(null);

        _pictureProcessorExposer.validatePictureListExposed();
        assertEquals("Error", _errText.toString() );
    }

    // Structured Basis 2: First if is false, else if is true
    // Boundary 3: Picture list size greater than 1, stacked picture is null
    @Test (expected=IllegalArgumentException.class)
    public void testValidatePictureListBound3() {
        ArrayList<Picture> pictureList = new ArrayList<>(Arrays.asList(
                new Picture('A', new char[1][1]),
                new Picture('A', new char[1][1]) ) );
        _pictureProcessorExposer.setPictures(pictureList);
        _pictureProcessorExposer.setFinalPicture(null);

        _pictureProcessorExposer.validatePictureListExposed();
        assertEquals("Error", _errText.toString() );
    }

    // Structured Basis 3: First if is false, else if is false
    // Boundary 4: Picture list size greater than 1, stacked picture not null
    @Test
    public void testValidatePictureListBound4() {
        ArrayList<Picture> pictureList = new ArrayList<>(Arrays.asList(
                new Picture('A', new char[1][1]),
                new Picture('A', new char[1][1]) ) );
        _pictureProcessorExposer.setPictures(pictureList);
        _pictureProcessorExposer.setFinalPicture(new Picture('A', new char[1][1]) );

        _pictureProcessorExposer.validatePictureListExposed();
        assertEquals("" , _errText.toString() );
    }

    // Structured Basis 1: If is true
    @Test
    public void testCreateNewPictureSB1() {
        _pictureProcessorExposer.setPictures(new ArrayList<Picture>());
        assertEquals(new ArrayList<Picture>(), _pictureProcessorExposer.getPictures() );
        _pictureProcessorExposer.setIsLastLine(false);

        _pictureProcessorExposer.setRowCountAndColCount(1,1);

        _pictureProcessorExposer.setCurrPicture(
                _pictureProcessorExposer.createNewTempPicture(
                        new ArrayList<>(Arrays.asList("A") ) ) );

        _pictureProcessorExposer.createNewPictureExposed();
        assertEquals('A', _pictureProcessorExposer.getPictures().get(0).getLetter() );
    }

    // Structured Basis 2: If is false
    @Test
    public void testCreateNewPictureSB2() {
        _pictureProcessorExposer.setFinalPicture(new Picture('A', new char[1][1]) );
        assertEquals('A', _pictureProcessorExposer._getFinalPicture().getLetter());
        // Because last line is set to true, this new picture will be a stacked
        //  picture
        _pictureProcessorExposer.setIsLastLine(true);

        _pictureProcessorExposer.setRowCountAndColCount(1,1);

        _pictureProcessorExposer.setLetters(new HashSet<Character>(
                Arrays.asList('B') ) );
        _pictureProcessorExposer.setCurrPicture(
                _pictureProcessorExposer.createNewTempPicture(
                        new ArrayList<String>(Arrays.asList("B"))));

        _pictureProcessorExposer.createNewPictureExposed();
        assertEquals('-', _pictureProcessorExposer._getFinalPicture().getLetter() );
    }

    // Structured Basis 1: If is true
    @Test
    public void testProcessedTemporaryPictureSB1() {
        _pictureProcessorExposer.setIsLastLine(false);

        _pictureProcessorExposer.setRowCountAndColCount(1,1);

        char[][] testPictureMatrix = {{'A'}};
        Picture testPicture = new Picture('A', testPictureMatrix);

        _pictureProcessorExposer.setCurrPicture(
                _pictureProcessorExposer.createNewTempPicture(
                        new ArrayList<String>(Arrays.asList("A"))));
        assertEquals(testPicture.getLetter(),
                _pictureProcessorExposer.processTemporaryPictureExposed().getLetter());
    }

    // Structured Basis 2: If is false
    @Test (expected=IllegalArgumentException.class)
    public void testProcessedTemporaryPictureSB2() {
        _pictureProcessorExposer.setIsLastLine(false);

        _pictureProcessorExposer.setRowCountAndColCount(1,1);

        _pictureProcessorExposer.setCurrPicture(
                _pictureProcessorExposer.createNewTempPicture(
                        new ArrayList<>(Arrays.asList("|"))));

        _pictureProcessorExposer.processTemporaryPictureExposed();
        assertEquals("Error", _errText.toString());
    }

    // Structured Basis 1: If is true
    @Test
    public void testImageLetterSB1() {
        _pictureProcessorExposer.setIsLastLine(false);
        char[][] testPictureMatrix = {{'A'}};
        assertEquals('A', _pictureProcessorExposer.pictureLetterExposed(testPictureMatrix) );
    }

    // Structured Basis 2: If is false
    @Test
    public void testImageLetterSB2() {
        _pictureProcessorExposer.setIsLastLine(true);
        char[][] testPictureMatrix = {{'A'}};
        _pictureProcessorExposer.setLetters(new HashSet<>(
                Arrays.asList('A') ) );

        assertEquals('-', _pictureProcessorExposer.pictureLetterExposed(testPictureMatrix) );
    }

    // Structured Basis 1: If is true
    @Test
    public void testImageLetterForNonStackedPictureSB1() {
        assertEquals('A', _pictureProcessorExposer.pictureLetterForNonFinalPictureExposed(
                new HashSet<>(Arrays.asList('A') ) ) );
    }

    // Structured Basis 2: If is false
    @Test (expected=IllegalArgumentException.class)
    public void testImageLetterForNonStackedPictureSB2() {
        assertEquals('A', _pictureProcessorExposer.pictureLetterForNonFinalPictureExposed(
                new HashSet<>(Arrays.asList('A', 'B') ) ) );
        assertEquals("Error", _errText.toString());
    }

    // Structured Basis 1: If is true (add method returns false if the
    //  element already exists in the set, true otherwise)
    @Test
    public void testUniqueLetterSB1() {
        _pictureProcessorExposer.setLetters(new HashSet<>(
                Arrays.asList('A') ) );

        assertEquals('B', _pictureProcessorExposer.uniqueLetterExposed('B') );
    }

    // Structured Basis 2: If is false
    @Test (expected=IllegalArgumentException.class)
    public void testUniqueLetterSB2() {
        _pictureProcessorExposer.setLetters(new HashSet<>(
                Arrays.asList('A') ) );
        _pictureProcessorExposer.uniqueLetterExposed('A');

        assertEquals("Error", _errText.toString());
    }

    // Structured Basis 1: If is true
    @Test
    public void testImageLetterForStackedPictureSB1() {
        _pictureProcessorExposer.setLetters(new HashSet<>(
                Arrays.asList('A', 'B', 'C') ) );
        HashSet<Character> testSet = new HashSet<>(
                Arrays.asList('A', 'B', 'C') );

        assertEquals('-', _pictureProcessorExposer.pictureLetterForFinalPictureExposed(
                testSet) );
    }

    // Structured Basis 2: If is false
    // I don't know how the return '|' would occur
    @Test (expected=IllegalArgumentException.class)
    public void testImageLetterForStackedPictureSB2() {
        _pictureProcessorExposer.setLetters(new HashSet<>(
                Arrays.asList('A', 'B', 'C') ) );
        HashSet<Character> testSet = new HashSet<>(
                Arrays.asList('A', 'B', 'Z') );
        _pictureProcessorExposer.pictureLetterForFinalPictureExposed(testSet);

        assertEquals("Error", _errText.toString() );
    }

    // Structured Basis 1: Normal matrix, one letter
    // Good Data
    @Test
    public void testUniqueLetterSetSB1() {
        char[][] testPictureMatrix =
                       {{'A', 'A', '.'},
                        {'A', '.', 'A'},
                        {'A', '.', 'A'}, };

        assertEquals(new HashSet<>(Arrays.asList('A') ),
                _pictureProcessorExposer.uniqueLettersExposed(testPictureMatrix) );
    }

    // Structured Basis 2: Normal matrix, multiple letters
    // Good Data
    @Test
    public void testUniqueLetterSetSB2() {
        char[][] testPictureMatrix =
                        {{'A', 'B', '.'},
                        {'A', '.', 'C'},
                        {'D', '.', 'A'}, };

        assertEquals(new HashSet<>(Arrays.asList('A', 'B', 'C', 'D') ),
                _pictureProcessorExposer.uniqueLettersExposed(testPictureMatrix) );
    }

    // Structured Basis 3: All dots
    // Good Data
    @Test
    public void testUniqueLetterSetSB3() {
        char[][] testPictureMatrix =
                       {{'.', '.', '.'},
                        {'.', '.', '.'},
                        {'.', '.', '.'}, };

        assertEquals(new HashSet<>(),
                _pictureProcessorExposer.uniqueLettersExposed(testPictureMatrix) );
    }

    // Structured Basis 4: All Letters
    // Good Data
    @Test
    public void testUniqueLetterSetSB4() {
        char[][] testPictureMatrix =
                       {{'A', 'A', 'A'},
                        {'A', 'A', 'A'},
                        {'A', 'A', 'A'}, };

        assertEquals(new HashSet<>(Arrays.asList('A') ),
                _pictureProcessorExposer.uniqueLettersExposed(testPictureMatrix) );
    }

    // Structured Basis 5: Empty matrix
    // Good Data
    @Test
    public void testUniqueLetterSetSB5() {
        char[][] testPictureMatrix = {{}};
        assertEquals(new HashSet<Character>(),
                _pictureProcessorExposer.uniqueLettersExposed(testPictureMatrix) );
    }

    // Bad Data: 1 null matrix
    @Test (expected=NullPointerException.class)
    public void testUniqueLetterSetBD1() {
        char[][] testPictureMatrix = null;
        _pictureProcessorExposer.uniqueLettersExposed(testPictureMatrix);
    }

    // Structured Basis 1: Temporary picture has one line, one column
    @Test
    public void testProcessTemporaryMatrixSB1() {
        _pictureProcessorExposer.setRowCountAndColCount(1,1);

        _pictureProcessorExposer.setCurrPicture(
                _pictureProcessorExposer.createNewTempPicture(
                        new ArrayList<>(Arrays.asList("B"))));

        char[][] testPictureMatrix = {{'B'}};

        assertArrayEquals(testPictureMatrix,
                _pictureProcessorExposer.processTemporaryMatrixExposed());
    }

    // Structured Basis 2: Temporary picture has multiple lines and columns
    @Test
    public void testProcessTemporaryMatrixSB2() {
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

    // Structured Basis 3: Temporary picture has multiple lines, one column
    @Test
    public void testProcessTemporaryMatrixSB3() {
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