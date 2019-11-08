package amazons;

import org.junit.Test;

import static amazons.Piece.*;
import static org.junit.Assert.*;
import ucb.junit.textui;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/** The suite of all JUnit tests for the amazons package.
 *  @author Mehak Sharma
 */
public class UnitTest {

    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
    }

    /** Tests basic correctness of put and get on the initialized board. */
    @Test
    public void testBasicPutGet() {
        Board b = new Board();
        b.put(BLACK, Square.sq(3, 5));
        assertEquals(b.get(3, 5), BLACK);
        b.put(WHITE, Square.sq(9, 9));
        assertEquals(b.get(9, 9), WHITE);
        b.put(EMPTY, Square.sq(3, 5));
        assertEquals(b.get(3, 5), EMPTY);
    }

    /** Tests proper identification of legal/illegal queen moves. */
    @Test
    public void testIsQueenMove() {
        assertFalse(Square.sq(1, 5).isQueenMove(Square.sq(1, 5)));
        assertFalse(Square.sq(1, 5).isQueenMove(Square.sq(2, 7)));
        assertFalse(Square.sq(0, 0).isQueenMove(Square.sq(5, 1)));
        assertTrue(Square.sq(1, 1).isQueenMove(Square.sq(9, 9)));
        assertTrue(Square.sq(2, 7).isQueenMove(Square.sq(8, 7)));
        assertTrue(Square.sq(3, 0).isQueenMove(Square.sq(3, 4)));
        assertTrue(Square.sq(7, 9).isQueenMove(Square.sq(0, 2)));
    }

    /** Tests proper identification of unblocked queen moves. */
    @Test
    public void testIsUnblockedMove() {
        Board b = new Board();
        b.put(BLACK, Square.sq(3, 5));
        assertFalse(b.isUnblockedMove(Square.sq(0, 0),
                Square.sq(0, 3), Square.sq(0, 5)));
        assertTrue(b.isUnblockedMove(Square.sq(3, 0),
                Square.sq(6, 3), Square.sq(4, 1)));
        assertFalse(b.isUnblockedMove(Square.sq(3, 0),
                Square.sq(3, 6), Square.sq(4, 6)));
    }

    /** Tests correctness of makeMove and undo methods on board. */
    @Test
    public void testMakeMove() {
        Board b = new Board();
        b.makeMove(Square.sq("d1"), Square.sq("d3"), Square.sq("d1"));
        b.makeMove(Square.sq("g10"), Square.sq("g8"), Square.sq("g7"));
        assertEquals(SPEAR, b.get(6, 6));
        b.undo();
        assertEquals(EMPTY, b.get(6, 6));
        assertEquals(BLACK, b.get(6, 9));
    }

    /** Tests toString for initial board state and a smiling board state. :) */
    @Test
    public void testToString() {
        Board a = new Board();
        for (int col = 0; col < 10; col += 1) {
            for (int row = 0; row < 10; row += 1) {
                a.put(WHITE, Square.sq(col, row));
            }
        }
        assertEquals(LILBOARD, a.toString());

        Board b = new Board();
        assertEquals(INIT_BOARD_STATE, b.toString());
        makeSmile(b);
        assertEquals(SMILE, b.toString());
    }


    private void makeSmile(Board b) {
        b.put(EMPTY, Square.sq(0, 3));
        b.put(EMPTY, Square.sq(0, 6));
        b.put(EMPTY, Square.sq(9, 3));
        b.put(EMPTY, Square.sq(9, 6));
        b.put(EMPTY, Square.sq(3, 0));
        b.put(EMPTY, Square.sq(3, 9));
        b.put(EMPTY, Square.sq(6, 0));
        b.put(EMPTY, Square.sq(6, 9));
        for (int col = 1; col < 4; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(2, 7));
        for (int col = 6; col < 9; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(7, 7));
        for (int lip = 3; lip < 7; lip += 1) {
            b.put(WHITE, Square.sq(lip, 2));
        }
        b.put(WHITE, Square.sq(2, 3));
        b.put(WHITE, Square.sq(7, 3));
    }

    static final String LILBOARD =
            "   W W W W W W W W W W\n"
                    + "   W W W W W W W W W W\n"
                    + "   W W W W W W W W W W\n"
                    + "   W W W W W W W W W W\n"
                    + "   W W W W W W W W W W\n"
                    + "   W W W W W W W W W W\n"
                    + "   W W W W W W W W W W\n"
                    + "   W W W W W W W W W W\n"
                    + "   W W W W W W W W W W\n"
                    + "   W W W W W W W W W W\n";

    static final String INIT_BOARD_STATE =
            "   - - - B - - B - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   B - - - - - - - - B\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   W - - - - - - - - W\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - W - - W - - -\n";

    static final String SMILE =
            "   - - - - - - - - - -\n"
                    + "   - S S S - - S S S -\n"
                    + "   - S - S - - S - S -\n"
                    + "   - S S S - - S S S -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - W - - - - W - -\n"
                    + "   - - - W W W W - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n";

    /** Tests legalMovesIterator to make sure it returns all legal Moves.
     *  This method needs to be finished and may need to be changed
     *  based on your implementation. */
    @Test
    public void testLegalMoves() {
        Board c = new Board();
        buildBoard(c, LEGALMOVESTESTBOARD);
        int numMoves = 0;
        Set<Move> moves = new HashSet<>();
        Iterator<Move> legalMoves = c.legalMoves(Piece.WHITE);
        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            assertTrue(LEGALMOVESTESTMOVES.contains(m));
            numMoves += 1;
            moves.add(m);
        }
        assertEquals(LEGALMOVESTESTMOVES.size(), numMoves);
        assertEquals(LEGALMOVESTESTMOVES.size(), moves.size());
    }


    private void buildBoard(Board b, Piece[][] target) {
        for (int col = 0; col < Board.SIZE; col++) {
            for (int row = Board.SIZE - 1; row >= 0; row--) {
                Piece piece = target[Board.SIZE - row - 1][col];
                b.put(piece, Square.sq(col, row));
            }
        }
        System.out.println(b);
    }

    static final Piece E = Piece.EMPTY;

    static final Piece W = Piece.WHITE;

    static final Piece B = Piece.BLACK;

    static final Piece S = Piece.SPEAR;

    static final Piece[][] INITTESTBOARD =
        {
            { E, E, E, B, E, E, B, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { B, E, E, E, E, E, E, E, E, B },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { W, E, E, E, E, E, E, E, E, W },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, W, E, E, W, E, E, E },
        };

    static final Piece[][] REACHABLEFROMTESTBOARD =
        {
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, W, W },
            { E, E, E, E, E, E, E, S, E, S },
            { E, E, E, S, S, S, S, E, E, S },
            { E, E, E, S, E, E, E, E, B, E },
            { E, E, E, S, E, W, E, E, B, E },
            { E, E, E, S, S, S, B, W, B, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
        };

    static final Piece[][] LEGALMOVESTESTBOARD =
        {
            { S, S, S, S, S, S, S, S, S, S },
            { E, E, E, E, E, E, E, E, S, S },
            { E, E, E, E, E, E, E, S, E, S },
            { E, E, E, S, S, S, S, S, E, S },
            { E, E, E, S, S, E, E, S, B, S },
            { E, E, E, S, S, W, S, E, B, S },
            { E, E, E, S, S, S, B, W, B, S },
            { E, E, S, S, S, S, S, E, E, S },
            { E, E, E, E, S, S, S, E, E, S },
            { E, E, E, E, S, S, S, S, S, S },
        };


    static final Set<Square> REACHABLEFROMTESTSQUARES =
        new HashSet<>(Arrays.asList(
            Square.sq(5, 5),
            Square.sq(4, 5),
            Square.sq(4, 4),
            Square.sq(6, 4),
            Square.sq(7, 4),
            Square.sq(6, 5),
            Square.sq(7, 6),
            Square.sq(8, 7)));

    static final Set<Square> MOREREACHSQUARES =
        new HashSet<>(Arrays.asList(
            Square.sq(7, 4),
            Square.sq(7, 5),
            Square.sq(7, 6),
            Square.sq(6, 4),
            Square.sq(5, 5),
            Square.sq(7, 2),
            Square.sq(7, 1),
            Square.sq(7, 0),
            Square.sq(8, 2),
            Square.sq(9, 1),
            Square.sq(6, 2),
            Square.sq(5, 1),
            Square.sq(4, 0)));

    static final Set<Move> LEGALMOVESTESTMOVES =
        new HashSet<>(Arrays.asList(
            Move.mv("h4-h3(h4)"),
            Move.mv("h4-i3(h2)"),
            Move.mv("h4-h2(h3)"),
            Move.mv("f5-g6(f5)"),
            Move.mv("h4-h3(i2)"),
            Move.mv("h4-h2(h5)"),
            Move.mv("h4-h5(g6)"),
            Move.mv("h4-h3(h5)"),
            Move.mv("h4-h2(i2)"),
            Move.mv("h4-h3(h2)"),
            Move.mv("h4-h3(i3)"),
            Move.mv("h4-h2(i3)"),
            Move.mv("f5-g6(h5)"),
            Move.mv("h4-i3(i2)"),
            Move.mv("h4-i3(h4)"),
            Move.mv("h4-h5(h3)"),
            Move.mv("h4-h5(h4)"),
            Move.mv("f5-f6(g6)"),
            Move.mv("h4-h5(h2)"),
            Move.mv("h4-h2(h4)"),
            Move.mv("f5-f6(f5)"),
            Move.mv("h4-i3(h3)"),
            Move.mv("f5-g6(f6)")));
}


