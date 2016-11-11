import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class PictureSolverTest {

    PictureSolver _solver = PictureSolver.getInstance();
    ArrayList<Picture> _pictures = new ArrayList<>();
    Picture _finalPicture;
    PictureSolver.Exposer _exposer = PictureSolver.getInstance(). new Exposer();
    HashMap<Character, Set<Character>> _overlayMap = new HashMap<>();

    @Before
    public void setUp() {
        char[][] charMatrixA = { ".A".toCharArray(),
                                 ".A".toCharArray()
                               };

        _pictures.add(new Picture('A', charMatrixA));

        char[][] charMatrixB = { "BB".toCharArray(),
                                 "..".toCharArray()};

        _pictures.add(new Picture('B', charMatrixB));

        char[][] charMatrixC = { "CC".toCharArray(),
                                 "C.".toCharArray()};

        _pictures.add(new Picture('C', charMatrixC));

        char[][] finalCharMatrix = { "CA".toCharArray(),
                                     "CA".toCharArray()};

        _finalPicture = new Picture('-', finalCharMatrix);

        _overlayMap.put('A', new HashSet<>());
        _overlayMap.put('B', new HashSet<>(Arrays.asList('A','C')));
        _overlayMap.put('C', new HashSet<>(Arrays.asList('A')));
    }


    //overlappingPicturesMap tests


    //Structured basis, Good data
    //full coverage
    //handles all viable branches. Skips and enters the filter and enters for loop
    @Test
    public void Should_properly_create_overlap_map() {
        Map<Character, Set<Character>> overlapMap = _exposer.overlappingPicturesMapExposed(_pictures, _finalPicture);

        assertEquals(overlapMap.get('A'), _overlayMap.get('A'));

        assertEquals(overlapMap.get('B'), _overlayMap.get('B'));

        assertEquals(overlapMap.get('C'), _overlayMap.get('C'));
    }

    //the case where we skip the for each loop is impossible as we can guarentee at least 1 picture exists thanks to the barricade
    //same with bad data


    //overlapSequence tests


    //Structured basis, good data
    //full coverage
    @Test
    public void Should_properly_compute_solution_list() {
        List<Character> solution = _exposer.overlapSequenceExposed(_overlayMap, new ArrayList<>());
        assertEquals(solution, Arrays.asList('B','C','A'));
    }

    

    @After
    public void tearDown() {

    }
}
