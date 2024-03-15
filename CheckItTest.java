import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.Assert.*;

public class CheckItTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        outContent.reset();
    }

    private void checkOutput(boolean expectedOutcome) {
        assertEquals(expectedOutcome, outContent.toString().trim().equals("P is true"));
        outContent.reset();
    }

    @Test
    public void testPredicateCoverageTrue() {
        CheckIt.checkIt(true, false, false);
        checkOutput(true);

        CheckIt.checkIt(false, true, true);
        checkOutput(true);
    }

    @Test
    public void testPredicateCoverageFalse() {
        CheckIt.checkIt(false, false, false);
        checkOutput(false);

        CheckIt.checkIt(false, false, true);
        checkOutput(false);
    }

    @Test
    public void testClauseCoverage() {
        // Clause a
        CheckIt.checkIt(true, false, false);
        checkOutput(true); // a=true

        CheckIt.checkIt(false, true, true);
        checkOutput(true); // a=false but b=true, c=true

        // Clause b
        CheckIt.checkIt(true, true, false);
        checkOutput(true); // b=true

        CheckIt.checkIt(false, false, true);
        checkOutput(false); // b=false

        // Clause c
        CheckIt.checkIt(true, true, true);
        checkOutput(true); // c=true

        CheckIt.checkIt(false, true, false);
        checkOutput(false); // c=false
    }

    @Test
    public void testCACC() {
        // Consider a as the major clause
        CheckIt.checkIt(true, false, false); // a=true makes p=true regardless of b and c
        checkOutput(true);

        CheckIt.checkIt(false, true, true); // a=false, but b=true and c=true makes p=true
        checkOutput(true);

        // we can add more tests for weather b or c are the mojor clauses.
    }

    @Test
    public void testRACC() {
        // Example where changing a doesn't change the outcome because b and c make p true
        CheckIt.checkIt(true, true, true); // a=true
        checkOutput(true);

        CheckIt.checkIt(false, true, true); // a=false, but b=true and c=true keeps p=true
        checkOutput(true);
    }
}
