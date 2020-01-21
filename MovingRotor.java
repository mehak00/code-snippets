package enigma;


import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Mehak Sharma
 */
class MovingRotor extends Rotor {

    /**
     * String with notches.
     */
    private String _notches;

    /**
     * A rotor named NAME whose permutation in its default setting is
     * PERM, and whose notches are at the positions indicated in NOTCHES.
     * The Rotor is initally in its 0 setting (first character of its
     * alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
        _setting = 0;
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    boolean atNotch() {
        return _notches.contains(String.valueOf(alphabet().toChar(setting())));
    }

    @Override
    void advance() {
        this.set(this.setting() + 1);
    }

    /** My setting. */
    private int _setting;

}
