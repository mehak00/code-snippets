package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Mehak Sharma
 */
class Permutation {

    /**
     * Set this Permutation to that specified by CYCLES, a string in the
     * form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     * is interpreted as a permutation in cycle notation.  Characters in the
     * alphabet that are not included in any cycle map to themselves.
     * Whitespace is ignored.
     */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        int numCycles = 0;
        String temp = cycles.replaceAll(" ", "");
        cycles = temp;
        _cycles = cycles.split("([)]//s*[(])|(\\s*[(])|[)]");
    }

    /**
     * Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     * c0c1...cm.
     */
    private void addCycle(String cycle) {
    }

    /**
     * Return the value of P modulo the size of this permutation.
     */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /**
     * Returns the size of the alphabet I permute.
     */
    int size() {
        return _alphabet.size();
    }

    /**
     * Return the result of applying this permutation to P modulo the
     * alphabet size.
     */
    int permute(int p) {
        return _alphabet.toInt(permute(_alphabet.toChar(wrap(p))));
    }

    /**
     * Return the result of applying the inverse of this permutation
     * to  C modulo the alphabet size.
     */
    int invert(int c) {
        return _alphabet.toInt(invert(_alphabet.toChar(wrap(c))));
    }

    /**
     * Return the result of applying this permutation to the index of P
     * in ALPHABET, and converting the result to a character of ALPHABET.
     */
    char permute(char p) {
        for (int i = 0; i < _cycles.length; i += 1) {
            if (_cycles[i].length() != 0) {
                if (_cycles[i].indexOf(p) == _cycles[i].length() - 1) {
                    return _cycles[i].charAt(0);
                } else if (_cycles[i].contains(String.valueOf(p))) {
                    int nextIndex = wrap(_cycles[i].indexOf(p)) + 1;
                    return _cycles[i].charAt(nextIndex);
                }
            }
        }
        return p;
    }


    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        for (int i = 0; i < _cycles.length; i += 1) {
            if (_cycles[i].length() != 0) {
                if (_cycles[i].indexOf(c) == 0) {
                    return _cycles[i].charAt(_cycles[i].length() - 1);
                } else if (_cycles[i].contains(String.valueOf(c))) {
                    int prevIndex = wrap(_cycles[i].indexOf(c) - 1);
                    return _cycles[i].charAt(prevIndex);
                }
            }
        }
        return c;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        int cycleLength = 0;
        for (int i = 0; i < _cycles.length; i += 1) {
            for (int j = 0; j < _cycles[i].length(); j += 1) {
                cycleLength += 1;
            }
        }
        return cycleLength == _alphabet.size();
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Cycle representation in the form of a string (cccccc)(cccc)(cc)... */
    private String[] _cycles;

}
