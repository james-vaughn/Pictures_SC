import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class PictureSolverTest {

    PictureSolver _solver = PictureSolver.getInstance();
    ArrayList<Picture> _pictures = new ArrayList<>();
    Picture _finalPicture;

    @Before
    public void setUp() {
        char[][] charMatrix = { ".A".toCharArray(),
                                ".A".toCharArray()
                              };

        _pictures.add(new Picture('A', charMatrix));

        charMatrix[0] = "BB".toCharArray();
        charMatrix[1] = "..".toCharArray();

        _pictures.add(new Picture('B', charMatrix));

        charMatrix[0] = "CC".toCharArray();
        charMatrix[1] = "C.".toCharArray();

        _pictures.add(new Picture('C', charMatrix));

        charMatrix[0] = "CA".toCharArray();
        charMatrix[1] = "CA".toCharArray();

        _finalPicture = new Picture('-', charMatrix);

    }


    //overlappingPicturesMap tests


    //Structured basis
    @Test
    public void Should_properly_create_overlap_map() {

    }


    @After
    public void tearDown() {

    }
}
