import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Input processor serves as a barricade, handling all potential input errors
//Singleton class
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
        if (isLineIndicatingGridSize(_lineIndex) ) {
            handleDimensions(inputLine);
        } else if(inputLine.equals(LINE_SEPARATOR) ) {
            processEmptyLine();
        } else {
            handlePictureLines(inputLine);
        }
        _lineIndex++;
    }

    //paranoia check
    private void validateLine(String inputLine) {
        if( inputLine == null ) {
            Writer.getInstance().writeError();
        }
    }

    //the first 2 lines are the grid dimensions
    private boolean isLineIndicatingGridSize(int lineIndex) {
        return ((lineIndex == 1) || (lineIndex == 2) );
    }

    private void handleDimensions(String inputLine) {
        if (isDimension(inputLine) ) {
            sendDimensionToProcessing(inputLine);
        } else {
            Writer.getInstance().writeError();
        }
    }

    // Returns true if every character is a number
    private boolean isDimension(String inputLine) {
        //matches only lines with one or more numbers; doesnt allow starting with 0
        String dimensionRegex = "^([1-9])([0-9]+)?$";
        Pattern dimensionPattern = Pattern.compile(dimensionRegex);
        Matcher dimensionMatcher = dimensionPattern.matcher(inputLine);
        return dimensionMatcher.find();
    }

    private void sendDimensionToProcessing(String inputLine) {
        PictureProcessor.getInstance().processDimensionLine(inputLine);
    }

    private void handlePictureLines(String inputLine) {
        if ( isPicture(inputLine) ) {
            sendPictureToProcessing(inputLine);
        } else {
            Writer.getInstance().writeError();
        }
    }

    private boolean isPicture(String inputLine) {
		//matches character or dot, then any number of (space then number or dot)
        String pictureRegex = "^([A-Z]|\\.)( (\\.|[A-Z]))*$";
        Pattern picturePattern = Pattern.compile(pictureRegex);
        Matcher pictureMatcher = picturePattern.matcher(inputLine);
        return pictureMatcher.find();
    }

    private void sendPictureToProcessing(String inputLine) {
        PictureProcessor.getInstance().processPictureLine(inputLine);
    }

    private void processEmptyLine() {
        PictureProcessor.getInstance().processEmptyLine();
    }


    public class Exposer {

        public void validateLineExposed(String inputLine) {
            validateLine(inputLine);
        }

        public boolean isLineIndicatingGridSizeExposed(int lineIndex) {
            return isLineIndicatingGridSize(lineIndex);
        }

        public void handleDimensionsExposed(String inputLine) {
            handleDimensions(inputLine);
        }

        public boolean isDimensionExposed(String inputLine) {
            return isDimension(inputLine);
        }

        public void sendDimensionToProcessingExposed(String inputLine) {
            sendDimensionToProcessing(inputLine);
        }

        public void handlePictureLinesExposed(String inputLine) {
            handlePictureLines(inputLine);
        }

        public boolean isPictureExposed(String inputLine) {
            return isPicture(inputLine);
        }

        public void sendPictureToProcessingExposed(String inputLine) {
            sendPictureToProcessing(inputLine);
        }

        public void processEmptyLineExposed() {
            processEmptyLine();
        }
    }
}
