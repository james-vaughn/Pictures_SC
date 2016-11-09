import java.util.ArrayList;
import java.util.List;

public class Picture {

    private final char image[][];
    private final char letter;

    public Picture(char letter, char image[][]) {
        this.letter = letter;
        this.image = image;
    }

    public char getLetter() {
        return letter;
    }

    public List<Point> pointsCovered() {
        List<Point> pointQueue = new ArrayList<>();

        for( int row = 0; row < image.length; row++ ) {

            for( int col = 0; col < image[0].length; col++ ) {
                Point p = new Point(row, col);

                if ( at(p) == letter) {
                    pointQueue.add(p);
                }
            }
        }

        return pointQueue;
    }

    public char at(Point p) {
        return image[p.row][p.col];
    }

}
