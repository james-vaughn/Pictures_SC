package pictures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class PictureSolver {

    public void solve() {
        HashMap<Character, HashSet<Character>> overlappingPictures = overlappingPicturesMap(null, null);
        List<Character> overlapOrder = overlapSequence(overlappingPictures);
        writeSolution(overlapOrder);
    }

    private HashMap<Character, HashSet<Character>> overlappingPicturesMap(ArrayList<Picture> pictures, Picture finalPicture) {
        HashMap<Character, HashSet<Character>> overlappingMap = new HashMap<>();
        List<Point> pointQueue;

        char pictureChar;

        for(Picture picture : pictures) {

            HashSet<Character> overlappingPictures = new HashSet<>();

            pointQueue = picture.pointsCovered();
            pictureChar = picture.getLetter();


            for(Point point : pointQueue) {

                if(finalPicture.at(point) != pictureChar) {
                    overlappingPictures.add(finalPicture.at(point));
                }
            }

            overlappingMap.put(pictureChar, overlappingPictures);
        }
    }

    private List<Character> overlapSequence(HashMap<Character, HashSet<Character>> overlapMap) {
        for()
    }

    private void writeSolution(List<Character> overlapOrder) {

    }

}
