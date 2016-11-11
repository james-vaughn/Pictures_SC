import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;



public class PictureTest {

    private Picture zeroImage;
    private char[][] zeroArray;

    private Picture emptyImage;

    private Picture filledImage;
    private char[][] filledArray;

    private Point point00;
    private Point point01;
    private Point point10;
    private Point point11;

    @Before
    public void setUp(){
        zeroArray = new char[0][0];
        zeroImage = new Picture('O',zeroArray);

        filledArray=new char[2][2];
        emptyImage=new Picture('B',filledArray);

        filledArray[0][0]='A';
        filledArray[1][1]='A';

        filledImage=new Picture ('A',filledArray);

        point00=new Point(0,0);
        point01=new Point(0,1);
        point10=new Point(1,0);
        point11=new Point(1,1);
    }

    @Test
    public void atTester(){
        //Test filledImage
        assertTrue(filledImage.at(point00)=='A');
        assertTrue(filledImage.at(point01)!='A');
        assertTrue(filledImage.at(point10)!='A');
        assertTrue(filledImage.at(point11)=='A');

        //Test emptyImage
        assertTrue(emptyImage.at(point00)!='B');
        assertTrue(emptyImage.at(point01)!='B');
        assertTrue(emptyImage.at(point10)!='B');
        assertTrue(emptyImage.at(point11)!='B');
    }

    @Test
    public void pointsCoveredTest(){
        List<Point> pointQueue = new ArrayList<>();
        assertTrue(zeroImage.pointsCovered().equals(pointQueue));
        assertTrue(emptyImage.pointsCovered().equals(pointQueue));
        pointQueue.add(point00);
        pointQueue.add(point11);
        assertTrue(filledImage.pointsCovered().equals(pointQueue));

    }

}
