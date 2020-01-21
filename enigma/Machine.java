package enigma;

import java.util.Collection;
import java.util.ArrayList;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Mehak Sharma
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _plugboard = new Permutation("", alpha);
        _allRotors = new Rotor[allRotors.size()];

        int i = 0;
        for (Rotor r : allRotors) {
            _allRotors[i] = r;
            i += 1;
        }


    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        _usedRotors = new ArrayList<Rotor>();
        ArrayList<String> rotorNames = new ArrayList<>();
        for (int i = 0; i < rotors.length; i += 1) {
            for (Rotor r : _allRotors) {
                if ((r.name().toUpperCase()).equals
                        ((rotors[i]).toUpperCase())) {
                    _usedRotors.add(r);
                }
            }
        }
        if (!_usedRotors.get(0).reflecting()) {
            throw new EnigmaException("First rotor needs to be a reflector.");
        }
        if (_usedRotors.size() != rotors.length) {
            throw new EnigmaException("Misnamed rotors");
        }
        for (int i = 0; i < _usedRotors.size(); i++) {
            for (int j = 0; j < _usedRotors.size(); j++) {
                if (i != j && _usedRotors.get(i) == _usedRotors.get(j)) {
                    throw new EnigmaException("Duplicated rotors");
                }
            }
        }
    }


    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 upper-case letters. The first letter refers to the
     *  leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        for (int i = 1; i < numRotors(); i += 1) {
            _usedRotors.get(i).set(setting.charAt(i - 1));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(char c) {
        char encrypt = Character.toUpperCase(c);
        int encrypted = _alphabet.toInt(encrypt);

        for (int i = 1; i < numRotors() - 1; i += 1) {
            if (_usedRotors.get(i).rotates()
                    && (_usedRotors.get(i + 1).atNotch()
                    || _usedRotors.get(i - 1).rotates()
                    && _usedRotors.get(i).atNotch())) {
                _usedRotors.get(i).advance();
            }
        }

        _usedRotors.get(numRotors() - 1).advance();

        encrypted = _plugboard.permute(encrypted);

        for (int i = _usedRotors.size() - 1; i >= 0; i -= 1) {
            encrypted = _usedRotors.get(i).convertForward(encrypted);
        }

        for (int i = 1; i < numRotors(); i += 1) {
            encrypted = _usedRotors.get(i).convertBackward(encrypted);
        }

        encrypted = _plugboard.invert(encrypted);

        return encrypted;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
             *  the rotors accordingly. */
    String convert(String msg) {
        String convertmsg = "";
        for (char letter : msg.toCharArray()) {
            convertmsg +=
                    _alphabet.toChar(convert(letter));
        }
        return convertmsg;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of rotors. */
    private int _numRotors;

    /** Number of pawls/rotating rotors. */
    private int _pawls;

    /** Plugboard. */
    private Permutation _plugboard;

    /** ArrayList containing used rotors. */
    private ArrayList<Rotor> _usedRotors;

    /** Rotor array containing all rotors. */
    private Rotor[] _allRotors;

}
