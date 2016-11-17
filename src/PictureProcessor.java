import java.util.ArrayList;
import java.util.HashSet;

public class PictureProcessor {


    private final Writer WRITER_INSTANCE = Writer.getInstance();

    private int _rowCount = 0;
    private int _colCount = 0;

    private static final PictureProcessor PICTURE_PROCESSOR_INSTANCE = new PictureProcessor();

    // Stores the index of the row that is currently being processed
    // Row 1 of the picture has index of 0
    private int _pictureRowIndex = 0;

    private Boolean _shouldBeEmptyLine = false;

    private ArrayList<Picture> _pictures = new ArrayList<>();

    private Picture _finalPicture;

    private HashSet<Character> _letters = new HashSet<>();

    private TemporaryPicture _currPicture;

    private boolean _isLastLine = false;

    private PictureProcessor() {
        _currPicture = new TemporaryPicture();
    }

    public static PictureProcessor getInstance() {
        return PICTURE_PROCESSOR_INSTANCE;
    }

    public void setIsLastLineToTrue() {
        _isLastLine = true;
    }

    public ArrayList<Picture> getPictureList() {
        return _pictures;
    }

    public Picture getFinalPicture () {
        return _finalPicture;
    }

    public void processDimensionLine(String inputLine) {
        int lineNumber = InputProcessor.getInstance()._lineIndex;
        if (lineNumber == 1) {
            _rowCount = Integer.parseInt(inputLine);
        } else {
            _colCount = Integer.parseInt(inputLine);
        }
    }

    public void processPictureLine(String inputLine) {
        if (_shouldBeEmptyLine) {
            WRITER_INSTANCE.writeError();
        }

        String pictureLine = inputLine.replaceAll("\\s+",""); //remove spaces
        validatePictureWidth(pictureLine);
        validatePictureHeight();
        _currPicture.TEMP_PIC_LINES.add(_pictureRowIndex, pictureLine);

        // If this is the last row of the picture (-1 is because the
        //  indices start at different values)
        if (_pictureRowIndex == _rowCount - 1) {

            createNewPicture();

            if (!_isLastLine) {
                _shouldBeEmptyLine = true;
            } else {
                processLastLine();
            }
        }
        _pictureRowIndex++;
    }

    private void validatePictureWidth(String pictureLine) {
        if (pictureLine.length() != _colCount) {
            WRITER_INSTANCE.writeError();
        }
    }

    private void validatePictureHeight() {
        // First row indexed at 0, first column indexed at 1
        if (_pictureRowIndex > (_rowCount) ) {
            WRITER_INSTANCE.writeError();
        }
    }

    public void processEmptyLine() {
        if (!_shouldBeEmptyLine) {
            WRITER_INSTANCE.writeError();
        } else {
            _shouldBeEmptyLine = false;
        }
    }

    private void processLastLine() {
        validatePictureList();
        PictureSolver.getInstance().solve(_pictures, _finalPicture);
    }

    private void validatePictureList() {
        if (_pictures.size() < 1) {
            WRITER_INSTANCE.writeError();
        } else if (_finalPicture == null) {
            WRITER_INSTANCE.writeError();
        }
    }

    private void createNewPicture() {
        if (!_isLastLine) {
            _pictures.add(processedTemporaryPicture() );
        } else {
            _finalPicture = processedTemporaryPicture();
        }
        // Clears the matrix in the temporary picture
        _currPicture.TEMP_PIC_LINES = new ArrayList<>();
        // Resets the row index for the next new picture, set to -1 because the
        // _pictureRowIndex is incremented by 1 after this method call
        _pictureRowIndex = -1;
    }

    private Picture processedTemporaryPicture() {
        char[][] pictureMatrix = processTemporaryMatrix();
        char newPictureLetter = pictureLetter(pictureMatrix);
        // Vertical bar represents pictureLetter method failed
        if (newPictureLetter != '|') {
            return new Picture(newPictureLetter, pictureMatrix);
        }
        return null;
    }

    private char pictureLetter(char[][] pictureMatrix) {
        HashSet<Character> setOfUniqueLetters = uniqueLetters(pictureMatrix);
        // If this is the not last line, then this matrix is for
        //  a non-final picture
        if (!_isLastLine) {
            return pictureLetterForNonFinalPicture(setOfUniqueLetters);
        } else {
            return pictureLetterForFinalPicture(setOfUniqueLetters);
        }
    }

    private char pictureLetterForNonFinalPicture(HashSet<Character> setOfUniqueLetters) {

        if (setOfUniqueLetters.size() == 1) {
            char pictureLetter = setOfUniqueLetters.iterator().next();
            return uniqueLetter(pictureLetter);
        } else {
            WRITER_INSTANCE.writeError();
        }
        // Vertical bar represents method failed
        return '|';
    }

    private char pictureLetterForFinalPicture(HashSet<Character> setOfUniqueLetters) {

        if (setOfUniqueLetters.equals(_letters) ) {
            // Hyphen represents that this picture is a stacked picture
            return '-';
        } else {
            WRITER_INSTANCE.writeError();
        }
        // Vertical bar represents method failed
        return '|';
    }

    //returns all unique letters in a picture
    private HashSet<Character> uniqueLetters(char[][] pictureMatrix) {
        HashSet<Character> setOfLetters = new HashSet<>();
        for (int i = 0; i < pictureMatrix.length; i++) {
            for (int k = 0; k < pictureMatrix[i].length; k++) {
                char currentLetter = pictureMatrix[i][k];
                if (currentLetter != '.') {
                    setOfLetters.add(currentLetter);
                }
            }
        }
        return setOfLetters;
    }

    private char uniqueLetter(char pictureLetter) {
        // If letters already contains this letter
        if (!_letters.add(pictureLetter) ) {
            WRITER_INSTANCE.writeError();
        }
        return pictureLetter;
    }

    private char[][] processTemporaryMatrix() {
        char[][] pictureMatrix = new char[_rowCount][_colCount];
        ArrayList<String> temporaryLines =
                _currPicture.TEMP_PIC_LINES;
        // Adds characters
        for (int i = 0; i < temporaryLines.size(); i++) {
            pictureMatrix[i] = temporaryLines.get(i).toCharArray();
        }
        return pictureMatrix;
    }


    private class TemporaryPicture {
        private ArrayList<String> TEMP_PIC_LINES;

        private TemporaryPicture() {
            TEMP_PIC_LINES = new ArrayList<>();
        }
    }


    public class Exposer {

        public int getRowCount() {
            return _rowCount;
        }

        public int getColCount() {
            return _colCount;
        }

        public void setPictureRowIndex(int pictureRowIndex) {
            _pictureRowIndex = pictureRowIndex;
        }

        public int getPictureRowIndex() {
            return _pictureRowIndex;
        }

        public void setRowCountAndColCount(int row, int col) {
            _rowCount = row;
            _colCount = col;
        }

        public boolean getShouldBeEmptyLine() {
            return _shouldBeEmptyLine;
        }

        public void setShouldBeEmptyLine(boolean shouldBeEmptyLine) {
            _shouldBeEmptyLine = shouldBeEmptyLine;
        }

        public void setLetters(HashSet<Character> letters) {
            _letters = letters;
        }

        public void setIsLastLine(boolean isLastLine) {
            _isLastLine = isLastLine;
        }

        public void setCurrPicture(TemporaryPicture currPicture) {
            _currPicture = currPicture;
        }

        public TemporaryPicture createNewTempPicture(ArrayList<String> pictureLines) {
            TemporaryPicture tempPic = new TemporaryPicture();
            tempPic.TEMP_PIC_LINES = pictureLines;

            return tempPic;
        }

        public ArrayList<Picture> getPictures() {
            return _pictures;
        }

        public void setPictures(ArrayList<Picture> pictureList) {
            _pictures = pictureList;
        }

        public Picture _getFinalPicture() {
            return _finalPicture;
        }

        public void setFinalPicture(Picture picture) {
            _finalPicture = picture;
        }

        public void validatePictureWidthExposed(String pictureLine) {
            validatePictureWidth(pictureLine);
        }

        public void validatePictureHeightExposed() {
            validatePictureHeight();
        }

        public void processEmptyLineExposed() {
            processEmptyLine();
        }

        public void processLastLineExposed() {
            processLastLine();
        }

        public void validatePictureListExposed() {
            validatePictureList();
        }

        public void createNewPictureExposed() {
            createNewPicture();
        }

        public Picture processTemporaryPictureExposed() {
            return processedTemporaryPicture();
        }

        public char pictureLetterExposed(char[][] pictureMatrix) {
            return pictureLetter(pictureMatrix);
        }

        public char pictureLetterForNonFinalPictureExposed(
                HashSet<Character> setOfUniqueLetters) {
            return pictureLetterForNonFinalPicture(setOfUniqueLetters);
        }

        public char uniqueLetterExposed(char pictureLetter) {
            return uniqueLetter(pictureLetter);
        }

        public char pictureLetterForFinalPictureExposed(
                HashSet<Character> setOfUniqueLetters) {
            return pictureLetterForFinalPicture(setOfUniqueLetters);
        }

        public HashSet<Character> uniqueLettersExposed(char[][] pictureMatrix) {
            return uniqueLetters(pictureMatrix);
        }

        public char[][] processTemporaryMatrixExposed() {
            return processTemporaryMatrix();
        }
    }
}

