import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Input processor serves as a barricade, handling all potential input errors
public class InputProcessor {

    private static final InputProcessor INPUT_PROCESSOR_INSTANCE = new InputProcessor();
    private static final String LINE_SEPARATOR = "";

    // The index of the first line is 1
    public int _lineIndex = 1;

    private InputProcessor() {
    }

    public static InputProcessor getInstance() {
        return INPUT_PROCESSOR_INSTANCE;
    }

    public void processFileLine(String inputLine) {
        validateLine(inputLine);
        if (isLineIndicatingGridSize() ) {
            examinePossibleDimension(inputLine);
        } else if(inputLine.equals(LINE_SEPARATOR) ) {
            processEmptyLine(inputLine);
        } else {
            examinePossiblePicture(inputLine);
        }
        _lineIndex++;
    }

    private void validateLine(String inputLine) {
        //TODO fill this
    }

    //the first 2 lines are the grid dimensions
    private boolean isLineIndicatingGridSize() {
        return ((_lineIndex == 1) || (_lineIndex == 2) );
    }

    private void examinePossibleDimension(String inputLine) {
        if (isDimension(inputLine) ) {
            sendDimensionToProcessing(inputLine);
        } else {
            //TODO throw error that a non-dimension was found
            // on line 1 or 2
        }
    }

    // Returns true if every character is a number
    private boolean isDimension(String inputLine) {
        String DIMENSION_REGEX = "^[0-9]+$"; //matches only lines with one or more numbers
        Pattern dimensionPattern = Pattern.compile(DIMENSION_REGEX);
        Matcher dimensionMatcher = dimensionPattern.matcher(inputLine);
        return dimensionMatcher.find();
    }

    private void sendDimensionToProcessing(String inputLine) {
        PictureProcessor.getInstance().processDimensionLine(inputLine);
    }

    private void examinePossiblePicture(String inputLine) {
        if ( isPicture(inputLine) ) {
            sendPictureToProcessing(inputLine);
        } else {
            //TODO throw an error, this is an invalid line
            System.out.println("Invalid line: " + inputLine);
        }
    }

    private boolean isPicture(String inputLine) {
        String pictureRegex = "^([A-Z]|\\.)( (\\.|[A-Z]))*$";
        Pattern picturePattern = Pattern.compile(pictureRegex);
        Matcher pictureMatcher = picturePattern.matcher(inputLine);
        return pictureMatcher.find();
    }

    private void sendPictureToProcessing(String inputLine) {
        PictureProcessor.getInstance().processPictureLine(inputLine);
    }

    private void processEmptyLine(String inputLine) {
        //TODO remove this debug line
        System.out.println("// Found empty line at line: " + _lineIndex);
        PictureProcessor.getInstance().processEmptyLine();
    }
}
