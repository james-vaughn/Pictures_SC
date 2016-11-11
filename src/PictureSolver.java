import java.util.*;
import java.util.stream.Collectors;

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

    private HashMap<Character, Set<Character>> overlappingPicturesMap(ArrayList<Picture> pictures, Picture finalPicture) {
        HashMap<Character, Set<Character>> overlappingMap = new HashMap<>();

        for(Picture picture : pictures) {

            Set<Character> overlappingPictures = picture.pointsCovered().parallelStream()
                                    .filter(point -> (finalPicture.at(point) != picture.getLetter()))
                                    .map(finalPicture::at)
                                    .collect(Collectors.toSet());

            overlappingMap.put(picture.getLetter(), overlappingPictures);
        }
        return overlappingMap;
    }

    private List<Character> overlapSequence(HashMap<Character, Set<Character>> overlapMap, List<Character> overlapOrder) {

        if(overlapMap.isEmpty()) {
            return overlapOrder;
        }

        Character topLayerChar = ' ';
        int minListLen = Integer.MAX_VALUE;

        for(Character currentLayer : overlapMap.keySet()) {
            if(overlapMap.get(currentLayer).size() < minListLen) {
                topLayerChar = currentLayer;
                minListLen = overlapMap.get(currentLayer).size();
            }
        }

        overlapOrder.add(0, topLayerChar); //add the top layer to the front so it gets pushed to the back as layers peel off
        overlapMap.remove(topLayerChar);

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
