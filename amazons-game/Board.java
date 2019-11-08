package amazons;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import static amazons.Piece.*;
import static java.lang.Math.*;


/** The state of an Amazons Game.
 *  @author Mehak Sharma
 */
class Board {

    /** The number of squares on a side of the board. */
    static final int SIZE = 10;

    /** Initializes a game board with SIZE squares on a side in the
     *  initial position. */
    Board() {
        init();
    }

    /** Initializes a copy of MODEL. */
    Board(Board model) {
        copy(model);
    }

    /** Copies MODEL into me. */
    void copy(Board model) {
        if (model == this) {
            return;
        }
        this._turn = model._turn;
        this._winner = model._winner;
        this._gameboard = new Piece[SIZE][SIZE];

        for (int col = 0; col < SIZE; col += 1) {
            for (int row = 0; row < SIZE; row += 1) {
                this._gameboard[col][row] = model._gameboard[col][row];
            }
        }
    }

    /** Clears the board to the initial position. */
    void init() {
        this._gameboard = new Piece[SIZE][SIZE];

        for (int col = 0; col < SIZE; col++) {
            for (int row = 0; row < SIZE; row++) {
                _gameboard[col][row] = EMPTY;
            }
        }

        this._gameboard[0][3] = WHITE;
        this._gameboard[6][0] = WHITE;
        this._gameboard[3][0] = WHITE;
        this._gameboard[9][3] = WHITE;

        this._gameboard[3][9] = BLACK;
        this._gameboard[6][9] = BLACK;
        this._gameboard[9][6] = BLACK;
        this._gameboard[0][6] = BLACK;

        this._turn = WHITE;
        this._winner = EMPTY;
    }

    /** Return the Piece whose move it is (WHITE or BLACK). */
    Piece turn() {
        return this._turn;
    }

    /** Return the number of moves (that have not been undone) for this
     *  board. */
    int numMoves() {
        return this._moves.size();
    }

    /** Return the winner in the current position, or null if the game is
     *  not yet finished. */
    final Piece winner() {
        if (_turn == WHITE && !legalMoves(_turn).hasNext()) {
            this._winner = BLACK;
        } else if (_turn == BLACK && !legalMoves(_turn).hasNext()) {
            this._winner = WHITE;
        }
        return this._winner;
    }

    /** Return the contents the square at S. */
    final Piece get(Square s) {
        return get(s.col(), s.row());
    }

    /** Return the contents of the square at (COL, ROW), where
     *  0 <= COL, ROW <= 9. */
    Piece get(int col, int row) {
        return this._gameboard[col][row];
    }

    /** Return the contents of the square at COL ROW. */
    final Piece get(char col, char row) {
        return get(col - 'a', row - '1');
    }

    /** Set square S to P. */
    final void put(Piece p, Square s) {
        put(p, s.col(), s.row());
    }

    /** Set square (COL, ROW) to P. */
    final void put(Piece p, int col, int row) {
        this._gameboard[col][row] = p;
    }

    /** Set square COL ROW to P. */
    final void put(Piece p, char col, char row) {
        put(p, col - 'a', row - '1');
    }

    /** Return true iff FROM - TO is an unblocked queen move on the current
     *  board, ignoring the contents of ASEMPTY, if it is encountered.
     *  For this to be true, FROM-TO must be a queen move and the
     *  squares along it, other than FROM and ASEMPTY, must be
     *  empty. ASEMPTY may be null, in which case it has no effect. */
    boolean isUnblockedMove(Square from, Square to, Square asEmpty) {
        if (to == null) {
            return false;
        }
        int verticalDiff = to.row() - from.row();
        int horizontalDiff = to.col() - from.col();
        boolean validMove = false;
        if (from.isQueenMove(to)) {
            validMove = true;
            if (verticalDiff == 0) {
                if (horizontalDiff < 0) {
                    for (int i = -1; (i >= horizontalDiff) && validMove; i--) {
                        Square tSquare = from.queenMove
                                (from.direction(to), i);
                        if (!((asEmpty != null) && tSquare.equals(asEmpty))) {
                            if (!(this._gameboard[tSquare.col()]
                                    [tSquare.row()].equals(EMPTY))) {
                                validMove = false;
                            }
                        }
                    }
                } else {
                    for (int i = 1; (i <= horizontalDiff)
                            && validMove; i++) {
                        Square tSquare = from.queenMove(from.direction(to), i);
                        if (!((asEmpty != null)
                                && tSquare.equals(asEmpty))) {
                            if (!this._gameboard[tSquare.col()]
                                    [tSquare.row()].equals(EMPTY)) {
                                validMove = false;
                            }
                        }
                    }
                }
            } else if (horizontalDiff == 0) {
                if (verticalDiff < 0) {
                    for (int i = -1; (i >= verticalDiff)
                            && validMove; i--) {
                        Square tSquare = from.queenMove(from.direction(to),
                                i);
                        if (!((asEmpty != null) && tSquare.equals(asEmpty))) {
                            if (!this._gameboard[tSquare.col()]
                                    [tSquare.row()].equals(EMPTY)) {
                                validMove = false;
                            }
                        }
                    }
                } else {
                    for (int i = 1; (i <= verticalDiff) && validMove; i++) {
                        Square tSquare = from.queenMove(from.direction(to),
                                i);
                        if (!((asEmpty != null) && tSquare.equals(asEmpty))) {
                            if (!this._gameboard[tSquare.col()]
                                    [tSquare.row()].equals(EMPTY)) {
                                validMove = false;
                            }
                        }
                    }
                }
            } else if (abs(verticalDiff) == abs(horizontalDiff)) {
                for (int i = 1; (i <= abs(verticalDiff)) && validMove; i++) {
                    int tRow, tCol = 0;
                    if (verticalDiff < 0) {
                        tRow = from.row() - i;
                    } else {
                        tRow = from.row() + i;
                    }
                    if (horizontalDiff < 0) {
                        tCol = from.col() - i;
                    } else {
                        tCol = from.col() + i;
                    }
                    if (!((asEmpty != null)
                            && (Square.sq(tCol, tRow).equals(asEmpty)))) {
                        if (!this._gameboard[tCol][tRow].equals(EMPTY)) {
                            validMove = false;
                        }
                    }
                }
            } else {
                validMove = false;
            }
        }
        return validMove;
    }

    /** Return true iff FROM is a valid starting square for a move. */
    boolean isLegal(Square from) {
        int squareRow = from.row();
        int squareCol = from.col();
        return _gameboard[squareCol][squareRow].equals(_turn);
    }

    /** Return true iff FROM-TO is a valid first part of move, ignoring
     *  spear throwing. */
    boolean isLegal(Square from, Square to) {
        return isLegal(from) && isUnblockedMove(from, to, from);
    }

    /** Return true iff FROM-TO(SPEAR) is a legal move in the current
     *  position. */
    boolean isLegal(Square from, Square to, Square spear) {
        return isLegal(from, to) && isUnblockedMove(to, spear, from);
    }

    /** Return true iff MOVE is a legal move in the current
     *  position. */
    boolean isLegal(Move move) {
        return isLegal(move.from(), move.to(), move.spear());
    }

    /** Move FROM-TO(SPEAR), assuming this is a legal move. */
    void makeMove(Square from, Square to, Square spear) {
        if (isLegal(from, to, spear)) {
            if (_turn == WHITE) {
                this.put(WHITE, to);
            } else {
                this.put(BLACK, to);
            }
            this.put(EMPTY, from);
            this.put(SPEAR, spear);
            this._moves.add(Move.mv(from, to, spear));
            _turn = _turn.opponent();
        } else {
            System.out.println("Not a legal move.");
        }
    }

    /** Move according to MOVE, assuming it is a legal move. */
    void makeMove(Move move) {
        makeMove(move.from(), move.to(), move.spear());
    }

    /** Undo one move.  Has no effect on the initial board. */
    void undo() {
        if (this._moves.size() == 0) {
            System.out.println("No legal moves to undo.");
        } else {
            Move lastMove = this._moves.get(this._moves.size() - 1);
            Square startMove = lastMove.from();
            Square endMove = lastMove.to();
            Square spearPosition = lastMove.spear();
            this._moves.remove(this._moves.size() - 1);

            this.put(EMPTY, spearPosition);
            this.put(EMPTY, endMove);
            this.put(_turn.opponent(), startMove);
            _turn = _turn.opponent();
        }
    }

    /** Return an Iterator over the Squares that are reachable by an
     *  unblocked queen move from FROM. Does not pay attention to what
     *  piece (if any) is on FROM, nor to whether the game is finished.
     *  Treats square ASEMPTY (if non-null) as if it were EMPTY.  (This
     *  feature is useful when looking for Moves, because after moving a
     *  piece, one wants to treat the Square it came from as empty for
     *  purposes of spear throwing.) */
    Iterator<Square> reachableFrom(Square from, Square asEmpty) {
        return new ReachableFromIterator(from, asEmpty);
    }

    /** Return an Iterator over all legal moves on the current board. */
    Iterator<Move> legalMoves() {
        return new LegalMoveIterator(_turn);
    }

    /** Return an Iterator over all legal moves on the current board for
     *  SIDE (regardless of whose turn it is). */
    Iterator<Move> legalMoves(Piece side) {
        return new LegalMoveIterator(side);
    }

    /** An iterator used by reachableFrom. */
    private class ReachableFromIterator implements Iterator<Square> {

        /** Iterator of all squares reachable by queen move from FROM,
         *  treating ASEMPTY as empty. */
        ReachableFromIterator(Square from, Square asEmpty) {
            _from = from;
            _dir = 0;
            _steps = 1;
            _asEmpty = asEmpty;
            _next = from;
            toNext();
        }

        @Override
        public boolean hasNext() {
            return _dir <= 7;

        }

        @Override
        public Square next() {
            Square next = _from.queenMove(_dir, _steps);
            toNext();
            return next;
        }

        /** Advance _dir and _steps, so that the next valid Square is
         *  _steps steps in direction _dir from _from. */
        private void toNext() {
            while (hasNext()) {
                _steps++;
                if (isUnblockedMove(_from, _from.queenMove(_dir, _steps),
                        _asEmpty)) {
                    return;
                } else {
                    _dir++;
                    _steps = 1;
                }
            }
        }

        /** Starting square. */
        private Square _from;
        /** Current direction. */
        private int _dir;
        /** Current distance. */
        private int _steps;
        /** Square treated as empty. */
        private Square _asEmpty;
        /** Next square. */
        private Square _next;
    }

    /** An iterator used by legalMoves. */
    private class LegalMoveIterator implements Iterator<Move> {

        /** All legal moves for SIDE (WHITE or BLACK). */
        LegalMoveIterator(Piece side) {
            _startingSquares = Square.iterator();
            _spearThrows = NO_SQUARES;
            _pieceMoves = NO_SQUARES;
            _fromPiece = side;
            _start = _startingSquares.next();
            _next = null;
            _spearDir = 0;
            _queenDir = 0;
            _lastQueenStep = 1;
            _lastSpearStep = 1;
            _spearPosition = null;
        }

        @Override
        public boolean hasNext() {
            boolean foundStart = false;
            boolean moveFound = false;
            _spearPosition = null;
            while (_startingSquares.hasNext() && !foundStart) {
                if (isLegal(_start)) {
                    foundStart = true;
                }
                while ((_queenDir <= 7) && !moveFound && foundStart) {
                    maxQueenSteps = switchQueenDir();
                    _next = null;
                    boolean queenFound = false;
                    int i = 0;
                    for (i = _lastQueenStep; ((i <= maxQueenSteps)
                            && !queenFound); i++) {
                        if (isUnblockedMove(_start,
                                _start.queenMove(_queenDir, i), null)) {
                            queenFound = true;
                            _next = _start.queenMove(_queenDir, i);
                        }
                    }
                    boolean spearFound = false;
                    while ((_spearDir <= 7) && !spearFound && queenFound) {
                        maxSpearSteps = switchSpearDir();
                        _spearPosition = null;
                        for (i = _lastSpearStep; ((i <= maxSpearSteps)
                                && !spearFound); i++) {
                            if (isUnblockedMove(_next,
                                    _next.queenMove(_spearDir, i), _start)) {
                                spearFound = true;
                                moveFound = true;
                                _spearPosition = _next.queenMove(_spearDir, i);
                            }
                        }
                        if (!spearFound) {
                            _spearDir++;
                            _lastSpearStep = 1;
                        } else {
                            _lastSpearStep++;
                        }
                        if ((_spearDir > 7) && (i > maxSpearSteps)) {
                            _lastQueenStep++;
                        }
                    }
                    if (!spearFound) {
                        _spearDir = 0;
                    }
                    if (!queenFound) {
                        _queenDir++;
                        _lastQueenStep = 1;
                        _spearDir = 0;
                    }
                }
                if ((_queenDir > 7) || !foundStart) {
                    _queenDir = 0;
                    _spearDir = 0;
                    _lastQueenStep = 1;
                    _lastSpearStep = 1;
                    _start = _startingSquares.next();
                    foundStart = false;
                }
            }
            if (!moveFound) {
                _next = null;
            }
            return moveFound;
        }

        /** Returns switch DIR. */
        public int switchQueenDir() {
            switch (_queenDir) {
            case 0: return SIZE - _start.row() - 1;
            case 1: return SIZE - max(_start.row(),
                        _start.col()) - 1;
            case 2: return SIZE - _start.col() - 1;
            case 3: return min((SIZE - _start.col() - 1),
                        _start.row());
            case 4: return _start.row();
            case 5: return min(_start.row(), _start.col());
            case 6: return _start.col();
            case 7: return min((SIZE - _start.row() - 1),
                        _start.col());
            default: return 0;
            }
        }

        /** Returns switch DIR. */
        public int switchSpearDir() {
            switch (_spearDir) {
            case 0: return SIZE - _next.row() - 1;
            case 1: return SIZE - max(_next.row(),
                    _next.col()) - 1;
            case 2: return SIZE - _next.col() - 1;
            case 3: return min((SIZE
                    - _next.col() - 1), _next.row());
            case 4: return _next.row();
            case 5: return min(_next.row(), _next.col());

            case 6: return _next.col();
            case 7: return min((SIZE - _next.row() - 1),
                    _next.col());
            default: return 0;
            }
        }

        @Override
        public Move next() {
            if (_next != null) {
                return Move.mv(_start, _next, _spearPosition);
            } else {
                return null;
            }
        }

        /** Advance so that the next valid Move is
         *  _start-_nextSquare(sp), where sp is the next value of
         *  _spearThrows. */
        private void toNext() {
        }

        /** Color of side whose moves we are iterating. */
        private Piece _fromPiece;
        /** Current starting square. */
        private Square _start;
        /** Remaining starting squares to consider. */
        private Iterator<Square> _startingSquares;
        /** Current piece's new position. */
        private Square _nextSquare;
        /** Remaining moves from _start to consider. */
        private Iterator<Square> _pieceMoves;
        /** Remaining spear throws from _piece to consider. */
        private Iterator<Square> _spearThrows;
        /** Current position of spear. */
        private Square _spearPosition;

        /** Current direction of queen. */
        private int _queenDir;
        /** Current direction of spear. */
        private int _spearDir;
        /** Current queen steps. */
        private int _lastQueenStep;
        /** Current spear steps. */
        private int _lastSpearStep;
        /** Current 'to' square. */
        private Square _next;
        /** Max queen steps. */
        private int maxQueenSteps;
        /** Current 'to' square. */
        private int maxSpearSteps;
    }

    @Override
    public String toString() {
        StringBuffer printGame = new StringBuffer();
        for (int row = 0; row < SIZE; row++) {
            printGame.append("   ");
            for (int col = 0; col < SIZE; col++) {
                if (col == 0) {
                    printGame.append(_gameboard[col][9 - row].toString());
                } else {
                    printGame.append(" " + _gameboard[col][9 - row].toString());
                }
            }
            printGame.append("\n");
        }

        return printGame.toString();
    }

    /** An empty iterator for initialization. */
    private static final Iterator<Square> NO_SQUARES =
            Collections.emptyIterator();

    /** Piece whose turn it is (BLACK or WHITE). */
    private Piece _turn;

    /** Cached value of winner on this board, or EMPTY if it has not been
     *  computed. */
    private Piece _winner;

    /** Board. */
    private Piece[][] _gameboard;

    /** Moves. */
    private List<Move> _moves = new ArrayList<>();


}
