package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Mehak Sharma
 */

public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  *  results to _output. */
    private void process() {
        Machine enigma = readConfig();
        String line = _input.nextLine().toUpperCase();
        if (!line.matches("\\s*[*].*")) {
            throw new EnigmaException("Settings line doesn't begin with *; "
                    + "incorrect format");
        }
        while (_input.hasNextLine()) {
            if (line.matches("[*].+")) {
                setUp(enigma, line);
            } else {
                String msg = line.replaceAll(" ", "").toUpperCase();
                printMessageLine(enigma.convert(msg));
            }
            line = _input.nextLine();
        }
        if (!line.matches("[*].+")) {
            printMessageLine(enigma.convert
                    (line.replaceAll(" ", "").toUpperCase()));
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        rotors = new ArrayList<>();
        try {
            if (_config.hasNext(".+")) {
                String alphabet = _config.nextLine();
                char first = alphabet.charAt(0);
                char last = alphabet.charAt(alphabet.length() - 1);
                _alphabet = new CharacterRange(first, last);

                if (_config.hasNextInt()) {
                    rotorSlots = _config.nextInt();
                    if (_config.hasNextInt()) {
                        _pawls = _config.nextInt();
                        while (_config.hasNext(".+")) {
                            rotors.add(readRotor());
                        }
                    } else {
                        throw new EnigmaException("Pawls not given.");
                    }
                } else {
                    throw new EnigmaException("Number of rotors not given.");
                }
            }
            if (rotorSlots < _pawls) {
                throw error("Number of moving rotors should be greater than "
                        + "number of pawls.");
            }

            return new Machine(_alphabet, rotorSlots, _pawls, rotors);
        } catch (NoSuchElementException excp) {
            throw error("Configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String nextToken = _config.next();
            String rotorName = "";
            if (nextToken.matches("\\w+")) {
                rotorName = nextToken;
            }

            String rotorType = "";
            nextToken = _config.next();
            if (!nextToken.matches("[(]")) {
                rotorType = nextToken.toUpperCase();
            }

            String cycles = "", notches = "";
            if (rotorType.charAt(0) == 'R') {
                while (_config.hasNext("\\s*[(]\\w+[)]\\s*")) {
                    cycles += _config.next();
                }
                return new Reflector(rotorName,
                        new Permutation(cycles, _alphabet));
            } else if (rotorType.charAt(0) == 'M') {
                notches += rotorType.substring(1);
                while (_config.hasNext("\\s*[(].+[)]\\s*")) {
                    cycles += _config.next();
                }
                return new MovingRotor(rotorName,
                        new Permutation(cycles, _alphabet), notches);
            } else if (rotorType.charAt(0) == 'N') {
                while (_config.hasNext("\\s*[(]\\w+[)]\\s*")) {
                    cycles += _config.next();
                }
                return new FixedRotor(rotorName,
                        new Permutation(cycles, _alphabet));
            } else {
                throw new EnigmaException("Not a proper rotor type.");
            }
        } catch (NoSuchElementException excp) {
            throw error("Bad rotor description.");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        String plugboard = "";
        String[] givenRotors = new String[M.numRotors()];
        Scanner set = new Scanner(settings);

        int i = 0;
        set.next();
        while (i < M.numRotors()) {
            if (set.hasNext("\\w+")) {
                givenRotors[i] = set.next();
                i += 1;
            }
        }
        M.insertRotors(givenRotors);

        if (set.hasNext("\\w+")) {
            M.setRotors(set.next());
        }

        while (set.hasNext("[(]\\w+[)]")) {
            plugboard += set.next();
        }
        if (plugboard.length() > 0) {
            M.setPlugboard(new Permutation(plugboard, _alphabet));
        }
    }


    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        msg = msg.toUpperCase();
        if (msg.length() <= 5) {
            _output.println(msg);
        } else {
            _output.print(msg.substring(0, 5) + " ");
            printMessageLine(msg.substring(5));
        }
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** All rotors. */
    private Collection<Rotor> rotors;

    /** My rotors. */
    private int rotorSlots;

    /** My setting. */
    private int _setting;

    /** My pawls. */
    private  int _pawls;

}
