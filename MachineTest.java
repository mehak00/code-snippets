package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;



import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Machine class.
 *  @author Mehak Sharma
 */

public class MachineTest {

    /*** Testing time limit. */

    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Rotor rotor;
    private Machine machine;
    private String alpha = UPPER_STRING;
    private Alphabet _alphabet;


    @Test
    public void testDoubleStep() {
        Alphabet ac = new CharacterRange('A', 'D');
        Rotor one = new Reflector("R1", new Permutation("(AC) (BD)", ac));
        Rotor two = new MovingRotor("R2", new Permutation("(ABCD)", ac), "C");
        Rotor three = new MovingRotor("R3", new Permutation("(ABCD)", ac), "C");
        Rotor four = new MovingRotor("R4", new Permutation("(ABCD)", ac), "C");
        String setting = "AAA";
        Rotor[] machineRotors = {one, two, three, four};
        String[] rotors = {"R1", "R2", "R3", "R4"};
        Machine mach = new Machine(ac, 4, 3,
                new ArrayList<>(Arrays.asList(machineRotors)));
        mach.insertRotors(rotors);
        mach.setRotors(setting);

        assertEquals("AAAA", getSetting(ac, machineRotors));
        mach.convert("a");
        assertEquals("AAAB", getSetting(ac, machineRotors));

        mach.convert('a');
        assertEquals("AAAC", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AABD", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AABA", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AABB", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AABC", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AACD", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("ABDA", getSetting(ac, machineRotors));
    }

    /** Helper method to get the String representation of the
     *  current Rotor settings */
    private String getSetting(Alphabet alph, Rotor[] machineRotors) {
        String currSetting = "";
        for (Rotor r : machineRotors) {
            currSetting += alph.toChar(r.setting());
        }
        return currSetting;
    }

    @Test
    public void testConvert() {
        Alphabet all = new CharacterRange('A', 'Z');

        HashMap settings = TestUtils.NAVALA;
        String hello = String.valueOf(settings.get("I"));
        String these = String.valueOf(settings.get("II"));
        String are = String.valueOf(settings.get("III"));
        String cycles = String.valueOf(settings.get("Beta"));
        String forthe = String.valueOf(settings.get("Gamma"));
        String converttest = String.valueOf(settings.get("B"));
        String yay = String.valueOf(settings.get("C"));

        ArrayList<Rotor> rotors = new ArrayList<Rotor>();
        rotors.add(new MovingRotor("I",
                new Permutation(hello, all), "A"));
        rotors.add(new MovingRotor("II",
                new Permutation(these, all), "A"));
        rotors.add(new MovingRotor("III",
                new Permutation(are, all), "A"));
        rotors.add(new FixedRotor("Beta",
                new Permutation(cycles, all)));
        rotors.add(new Reflector("B",
                new Permutation(converttest, all)));

        Machine enigmaTest = new Machine(all, 5, 3, rotors);
        String[] machineRotors = {"B", "Beta", "III", "II", "I"};
        enigmaTest.setPlugboard(new Permutation("", all));
        enigmaTest.insertRotors(machineRotors);

        enigmaTest.setRotors("HAHA");
        String trial = enigmaTest.convert("ONEDIRECTION"
                        + "ONEDIRECTION");
        enigmaTest.setRotors("HAHA");
        String trial2 = enigmaTest.convert(trial);
        assertEquals(
                "ONEDIRECTION" + "ONEDIRECTION", trial2);
    }
}


