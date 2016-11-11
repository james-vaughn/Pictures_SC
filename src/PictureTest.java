import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;



public class PictureTest {

    private Picture _badPicture;
    private Picture _filledPicture;

    private Point _topLeft;
    private Point _topRight;
    private Point _bottomLeft;
    private Point _bottomRight;

    @Before
    public void setUp(){
        char[][] charMatrixA = { ".A".toCharArray(),
                                 ".A".toCharArray()
                               };

        _filledPicture =new Picture ('A',charMatrixA);

        char[][] charMatrixBlank = new char[0][0];

        _badPicture = new Picture('-', charMatrixBlank);

        _topLeft = new Point(0,0);
        _topRight = new Point(0,1);
        _bottomLeft = new Point(1,0);
        _bottomRight = new Point(1,1);
    }

    //pointsCovered tests

    //structured basis
    //full coverage
    @Test
    public void Should_return_only_points_with_letters(){
        List<Point> points = _filledPicture.pointsCovered();
        assertEquals(points.size(), 2);
        assertEquals(points.get(0), _topRight);
        assertEquals(points.get(1), _bottomRight);
    }

    //bad data
    //no picture contents
    @Test
    public void Should_return_empty_list_for_empty_pictures() {
        assertEquals(_badPicture.pointsCovered(), new ArrayList<Point>());
    }


    //at Tests


    //structured basis full coverage
    @Test
    public void Should_yield_proper_chars_at_each_point_for_good_images() {
        assertEquals(_filledPicture.at(_topLeft), '.');
        assertEquals(_filledPicture.at(_topRight), 'A');
        assertEquals(_filledPicture.at(_bottomLeft), '.');
        assertEquals(_filledPicture.at(_bottomRight), 'A');
    }

    //bad data
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void Should_error_if_the_point_doesnt_exist_within_the_picture() {
        _badPicture.at(_topLeft);
    }
}
