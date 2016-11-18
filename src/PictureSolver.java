import java.util.*;
import java.util.stream.Collectors;

//singleton which calculates the solution
public class PictureSolver {

    private static final PictureSolver PICTURE_SOLVER_INSTANCE = new PictureSolver();

    private PictureSolver() {

    }

    public static PictureSolver getInstance() {
        return PICTURE_SOLVER_INSTANCE;
    }

    //solve should crash if we cannot write as writing the file is mission critical
    public void solve(ArrayList<Picture> pictures, Picture finalPicture) {
        HashMap<Character, Set<Character>> overlappingPictures = overlappingPicturesMap(pictures, finalPicture);
        List<Character> overlapOrder = overlapSequence(overlappingPictures, new ArrayList<>());
        Writer.getInstance().writeSolution(overlapOrder);
    }

    //returns a map of picture letters to what picture letters overlap that letter in the final picture
    private HashMap<Character, Set<Character>> overlappingPicturesMap(ArrayList<Picture> pictures, Picture finalPicture) {
        HashMap<Character, Set<Character>> overlappingMap = new HashMap<>();

        for(Picture picture : pictures) {

            //get all pictures which overlap this picture in the stacked image
            Set<Character> overlappingPictures = picture.pointsCovered().parallelStream()
                                    .filter(point -> (finalPicture.at(point) != picture.getLetter())) //point of overlaps only
                                    .map(finalPicture::at) //get the overlapping picture's letter
                                    .collect(Collectors.toSet());

            overlappingMap.put(picture.getLetter(), overlappingPictures);
        }
        return overlappingMap;
    }

    //returns the solution in order bottom to top
    private List<Character> overlapSequence(HashMap<Character, Set<Character>> overlapMap, List<Character> overlapOrder) {

        //base case
        if(overlapMap.isEmpty()) {
            return overlapOrder;
        }

        Character topLayerChar = ' ';
        int minListLen = Integer.MAX_VALUE; //base comparison point for comparing to

        //find the character with the smallest number of characters which overlap it
        for(Character currentLayer : overlapMap.keySet()) {
            if(overlapMap.get(currentLayer).size() < minListLen) {
                topLayerChar = currentLayer;
                minListLen = overlapMap.get(currentLayer).size();
            }
        }

        overlapOrder.add(0, topLayerChar); //add the top layer to the front so it gets pushed to the back as layers peel off
        overlapMap.remove(topLayerChar); //peel off the top layer

        return overlapSequence(overlapMap, overlapOrder);
    }

    public class Exposer {

        public HashMap<Character, Set<Character>> overlappingPicturesMapExposed(ArrayList<Picture> pictures, Picture finalPicture) {
            return overlappingPicturesMap(pictures, finalPicture);
        }

        public List<Character> overlapSequenceExposed(HashMap<Character, Set<Character>> overlapMap, List<Character> overlapOrder) {
            return overlapSequence(overlapMap, overlapOrder);
        }
    }
}
