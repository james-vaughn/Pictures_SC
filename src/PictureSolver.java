import java.util.*;
import java.util.stream.Collectors;

public class PictureSolver {

    public void solve() {
        HashMap<Character, Set<Character>> overlappingPictures = overlappingPicturesMap(null, null);
        List<Character> overlapOrder = overlapSequence(overlappingPictures, new ArrayList<>());
        writeSolution(overlapOrder);
    }

    private HashMap<Character, Set<Character>> overlappingPicturesMap(ArrayList<Picture> pictures, Picture finalPicture) {
        HashMap<Character, Set<Character>> overlappingMap = new HashMap<>();
        List<Point> pointQueue;

        char pictureChar;

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

        Character topLayerChar;


        return overlapSequence(overlapMap, overlapOrder);
    }

    private void writeSolution(List<Character> overlapOrder) {

    }

}
