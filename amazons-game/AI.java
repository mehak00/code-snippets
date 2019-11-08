package amazons;

import java.util.Iterator;

import static java.lang.Math.*;

import static amazons.Piece.*;
import static amazons.Utils.iterable;

/** A Player that automatically generates moves.
 *  @author Mehak Sharma
 */
class AI extends Player {

    /**
     * A position magnitude indicating a win (for white if positive, black
     * if negative).
     */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 1;
    /**
     * A magnitude greater than a normal value.
     */
    private static final int INFTY = Integer.MAX_VALUE;

    /**
     * A new AI with no piece or controller (intended to produce
     * a template).
     */
    AI() {
        this(null, null);
    }

    /**
     * A new AI playing PIECE under control of CONTROLLER.
     */
    AI(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new AI(piece, controller);
    }

    @Override
    String myMove() {
        Move move = findMove();
        _controller.reportMove(move);
        return move.toString();
    }

    /**
     * Return a move for me from the current position, assuming there
     * is a move.
     */
    private Move findMove() {
        Board b = new Board(board());
        if (_myPiece == WHITE) {
            findMove(b, maxDepth(b), true, 1, -INFTY, INFTY);
        } else {
            findMove(b, maxDepth(b), true, -1, -INFTY, INFTY);
        }
        return _lastFoundMove;
    }

    /**
     * The move found by the last call to one of the ...FindMove methods
     * below.
     */
    private Move _lastFoundMove;

    /**
     * Find a move from position BOARD and return its value, recording
     * the move found in _lastFoundMove iff SAVEMOVE. The move
     * should have maximal value or have value > BETA if SENSE==1,
     * and minimal value or value < ALPHA if SENSE==-1. Searches up to
     * DEPTH levels.  Searching at level 0 simply returns a static estimate
     * of the board value and does not set _lastFoundMove.
     */
    private int findMove(Board board, int depth, boolean saveMove, int sense,
                         int alpha, int beta) {
        int bestScore;
        Move bestMove = null;
        if (depth == 0 || board.winner() != EMPTY) {
            return staticScore(board);
        } else {
            if (sense == 1) {
                bestScore = -INFTY;
                for (Move trial : iterable(board.legalMoves())) {
                    board.makeMove(trial);
                    int scoreBoard = findMove(board,
                            depth - 1, false, -sense, alpha, beta);
                    board.undo();
                    if (scoreBoard >= bestScore) {
                        bestScore = scoreBoard;
                        bestMove = trial;
                        alpha = max(alpha, bestScore);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            } else {
                bestScore = INFTY;
                for (Move trial : iterable(board.legalMoves())) {
                    board.makeMove(trial);
                    int scoreBoard = findMove(board,
                            depth - 1, false, -sense, alpha, beta);
                    board.undo();
                    if (scoreBoard <= bestScore) {
                        bestScore = scoreBoard;
                        bestMove = trial;
                        beta = min(beta, bestScore);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
            if (saveMove) {
                _lastFoundMove = bestMove;
            }
        }
        return bestScore;
    }

    /**
     * Return a heuristically determined maximum search depth
     * based on characteristics of BOARD.
     */
    private int maxDepth(Board board) {
        int N = board.numMoves();

        if (N < 2 * 10) {
            return 1;
        } else if (N < 3 * 10) {
            return 2;
        } else if (N < 4 * 10) {
            return 3;
        } else if (N < 5 * 10) {
            return 4;
        } else {
            return 5;
        }
    }

    /**
     * Return a heuristic value for BOARD.
     */
    private int staticScore(Board board) {
        Piece winner = board.winner();
        if (winner == BLACK) {
            return -WINNING_VALUE;
        } else if (winner == WHITE) {
            return WINNING_VALUE;
        } else {
            int numWhiteMoves = 0;
            int numBlackMoves = 0;
            Iterator<Move> whiteMoves = board.legalMoves(WHITE);
            Iterator<Move> blackMoves = board.legalMoves(BLACK);
            while (whiteMoves.hasNext()) {
                numWhiteMoves++;
                whiteMoves.next();
            }
            while (blackMoves.hasNext()) {
                numBlackMoves++;
                blackMoves.next();
            }
            return numWhiteMoves - numBlackMoves;
        }
    }
}
