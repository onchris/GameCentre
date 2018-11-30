package fall2018.csc2017.slidingtiles;

import org.junit.Test;

import static org.junit.Assert.*;

public class ScoringSystemTest {

    @Test
    public void calculateScore() {
        ScoringSystem score = new ScoringSystem();
        assertEquals(800, score.calculateScore(100, 0));
        assertEquals(100, score.calculateScore(900, 0));
        assertEquals(900, score.calculateScore(0, 100));
        assertEquals(100, score.calculateScore(0, 900));
    }

}