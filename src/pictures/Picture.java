package pictures;

public class Picture {

    private final char image[][];

    public Picture(char image[][]) {
        this.image = image;
    }

    public char at(int row, int col) {
        return image[row][col];
    }

    
}
