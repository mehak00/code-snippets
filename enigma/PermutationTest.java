package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Mehak Sharma
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void testInvertChar() {
        Permutation p = new Permutation("(PNH) "
                + "(ABDFIKLZYXW) (JC)", new CharacterRange('A', 'Z'));

        assertEquals(p.invert('B'), 'A');
        assertEquals(p.invert('G'), 'G');
        assertEquals(p.invert('P'), 'H');
        assertEquals(p.invert('A'), 'W');

        Permutation i = new Permutation("(M)", new CharacterRange('A', 'Z'));
        assertEquals(i.invert('M'), 'M');
        assertEquals(i.invert('G'), 'G');

        Permutation j = new Permutation("(AELTPHQXRU) (BKNW)"
                + " (CMOY) (DFG) (IV) (JZ) (S)", UPPER);
        assertEquals(j.invert('S'), 'S');

    }


    @Test
    public void testPermuteChar() {
        Permutation p = new Permutation("(ABCDE)(FGH)(M)", UPPER);
        assertEquals(p.permute('A'), 'B');
        assertEquals(p.permute('M'), 'M');
        assertEquals(p.permute('E'), 'A');
        assertEquals(p.permute('F'), 'G');
        assertEquals(p.permute('S'), 'S');

    }

    @Test
    public void testDerangement() {
        Permutation p = new Permutation("(ABCDE)(FGH)(M)", UPPER);
        assertEquals(p.derangement(), false);

    }


}
