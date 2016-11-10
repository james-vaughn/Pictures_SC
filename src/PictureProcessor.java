import java.util.ArrayList;
import java.util.Arrays;

public class PictureProcessor {

    private int _rowCount = 0;
    private int _colCount = 0;

    private static final PictureProcessor PICTURE_PROCESSOR_INSTANCE =
            new PictureProcessor();

    //stores index of picture being processed
    private int _pictureIndex = 0;

    //stores line index of image being processed
    private int _lineIndex = 0;

    private Boolean _shouldBeEmptyLine = false;

    private ArrayList<Picture> _pictures = new ArrayList<>();

    private TemporaryPicture CURRENT_PICTURE;

    private PictureProcessor() {
        CURRENT_PICTURE = new TemporaryPicture();
    }

    public static PictureProcessor getInstance() {
        return PICTURE_PROCESSOR_INSTANCE;
    }

    public void processDimensionLine(String inputLine) {
        int lineNumber = InputProcessor.getInstance()._lineIndex;
        if (lineNumber == 1) {
            _rowCount = Integer.parseInt(inputLine);
            //TODO remove debug output
            //System.out.println("// Number of rows set to: " + _rowCount);
        } else if (lineNumber == 2) {
            _colCount = Integer.parseInt(inputLine);
            //TODO remove debug output
            //System.out.println("// Number of columns set to: " + _colCount);
        }
    }

    public void processPictureLine(String inputLine) {
        String lineWithoutSpaces = inputLine.replaceAll("\\s+","");
        ArrayList<String> pictureLine =
                new ArrayList<String>(Arrays.asList(lineWithoutSpaces.split("") ) );
        validatePictureWidth(pictureLine);
        validatePictureHeight();
        CURRENT_PICTURE.TEMP_PIC_LINES.add(_lineIndex, pictureLine);
        System.out.println("// Picture Row Index: " + _lineIndex +
                " Temp Picture List: " + CURRENT_PICTURE.TEMP_PIC_LINES);

        // If this is the last row of the picture (+1 is because the
        //  indices start at different values)
        if ( (_lineIndex + 1 ) == _rowCount) {
            _shouldBeEmptyLine = true;
            createNewPicture();
        }
        _lineIndex++;
    }

    public void validatePictureWidth(ArrayList<String> pictureLine) {
        if (pictureLine.size() != _colCount) {
            //TODO report fatal error
            System.out.println("// Number of picture columns " +
                    _colCount +
                    " does not match line length " + pictureLine.size());
        }
    }

    public void validatePictureHeight() {
        // First row indexed at 0, first column indexed at 1
        if (_lineIndex > (_colCount) ) {
            System.out.println("// Picture row " + _lineIndex +
                    " is greater than the the specified number of columns");
            //TODO report fatal error
        }
    }

    public void processEmptyLine() {
        if (!_shouldBeEmptyLine) {
            System.out.println("// Empty line not expected");
            //TODO report an error
        } else {
            _shouldBeEmptyLine = false;
            System.out.println("// Empty line found in correct place");
        }
    }

    private void createNewPicture() {
        Picture newPicture = processTemporaryPicture();
        _pictures.add(newPicture);
        //TODO check if _pictureIndex matches _pictures index
        _pictureIndex++;
        // Clears the matrix in the temporary picture
        CURRENT_PICTURE.TEMP_PIC_LINES = new ArrayList<ArrayList<String>>();
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
                CURRENT_PICTURE.TEMP_PIC_LINES;

        for (int i = 0; i < listOfTemporaryLines.size(); i++) {
            for (int k = 0; k < listOfTemporaryLines.get(i).size(); k++) {
                char c = listOfTemporaryLines.get(i).get(k).charAt(0);
                //TODO remove debugging line
                //System.out.println("//Row: " + i +
                //                    " Column: " + k +
                //                   " Char: " + c);
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
        private ArrayList<ArrayList<String>> TEMP_PIC_LINES;

        public TemporaryPicture() {
            TEMP_PIC_LINES = new ArrayList<ArrayList<String>>();
        }
    }
}
