import java.util.ArrayList;
import java.util.Arrays;

//handles validation and creation of Picture objects to send the solver
//Singleton class
public class PictureProcessor {

    private int _rowCount = 0;
    private int _colCount = 0;

    private static final PictureProcessor PICTURE_PROCESSOR_INSTANCE = new PictureProcessor();

    //stores index of picture being processed
    private int _pictureIndex = 0;

    //stores line index of image being processed
    private int _lineIndex = 0;

    private Boolean _shouldBeEmptyLine = false;
    private ArrayList<Picture> _pictures = new ArrayList<>();
    private TemporaryPicture _currPicture;

    private PictureProcessor() {
        _currPicture = new TemporaryPicture();
    }

    public static PictureProcessor getInstance() {
        return PICTURE_PROCESSOR_INSTANCE;
    }

    public void processDimensionLine(String inputLine) {
        int lineNumber = InputProcessor.getInstance()._lineIndex;

        if (lineNumber == 1) {
            _rowCount = Integer.parseInt(inputLine);
        } else {
            _colCount = Integer.parseInt(inputLine);
        }
    }
//////////////////////////////////////////////////////////////////////////////
    public void processPictureLine(String inputLine) {
        if (_shouldBeEmptyLine){
            Writer.getInstance().writeError();
        }

        String lineWithoutSpaces = inputLine.replaceAll("\\s+","");
        ArrayList<String> pictureLine =
                new ArrayList<String>(Arrays.asList(lineWithoutSpaces.split("") ) );
        validatePictureWidth(pictureLine);
        validatePictureHeight();
        _currPicture.lines.add(_lineIndex, pictureLine);

        // If this is the last row of the picture (+1 is because the
        //  indices start at different values)
        if ( _lineIndex == (_rowCount - 1) ) {
            _shouldBeEmptyLine = true;
            createNewPicture();
        }
        _lineIndex++;
    }

    public void validatePictureWidth(ArrayList<String> pictureLine) {
        if (pictureLine.size() != _colCount) {
            Writer.getInstance().writeError();
        }
    }

    public void validatePictureHeight() {
        // First row indexed at 0, first column indexed at 1
        if (_lineIndex > (_colCount) ) {
            Writer.getInstance().writeError();
        }
    }

    public void processEmptyLine() {
        if (!_shouldBeEmptyLine) {
            Writer.getInstance().writeError();
        } else {
            _shouldBeEmptyLine = false;
        }
    }

    private void createNewPicture() {
        Picture newPicture = processTemporaryPicture();
        _pictures.add(newPicture);
        //TODO check if _pictureIndex matches _pictures index
        _pictureIndex++;
        // Clears the matrix in the temporary picture
        _currPicture.lines = new ArrayList<>();
        // Resets the row index for the next new picture
        _lineIndex = -1;
        printAllPictures();
    }

    private Picture processTemporaryPicture() {
        char[][] pictureMatrix = processTemporaryMatrix();
        //TODO iterate through matrix and find capital letter
        // report error if letter not found
        // Using letter A for now
        return new Picture('A', pictureMatrix);
    }

    private char[][] processTemporaryMatrix() {
        char[][] pictureMatrix = new char[_rowCount][_colCount];
        ArrayList<ArrayList<String>> listOfTemporaryLines =
                _currPicture.lines;

        for (int i = 0; i < listOfTemporaryLines.size(); i++) {
            for (int k = 0; k < listOfTemporaryLines.get(i).size(); k++) {
                char c = listOfTemporaryLines.get(i).get(k).charAt(0);
                pictureMatrix[i][k] = c;
            }
        }
        return pictureMatrix;
    }

    private void printAllPictures() {
        for (Picture picture : _pictures) {
            //picture.printPicture();
        }
    }

    public class TemporaryPicture {
        private ArrayList<ArrayList<String>> lines;

        public TemporaryPicture() {
            lines = new ArrayList<>();
        }
    }
}
