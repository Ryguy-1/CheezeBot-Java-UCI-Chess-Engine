package package1;

import sun.invoke.empty.Empty;

import java.util.EmptyStackException;

public class Minimax {

    //Capital = maximize
    //Lower Case = minimize

    public static final int MAX = Integer.MAX_VALUE;
    public static final int MIN = -Integer.MAX_VALUE; //Double.MIN_VALUE is still a positive number because of rollovers or something

////////////////////////////////////////////////////////////////////////
    //using the position object as three things...
    //1) A way to store the board and the board history to be able to generate moves
    //2) A way to hold the best move calculated with minimax
    //3) An object that can be used to calculate the goodness of that node (a position object = a node in the minimax tree)
    /////////////////////////////////////////////////////////////////////

    public Position minimax(Position pos, int depth, boolean isMaximizingPlayer, int alpha, int beta){

        if(depth==0){ //need to also check if the game is over, likely through checkmate -> should do it for me though because taking king is 100_000 points...
            //System.out.println("reached bottom. value of position is: "+ Runner.boardEvaluation.getBoardRanking(pos));
            return pos;
        }

        if(isMaximizingPlayer){ //Capital
            int bestValue = MIN; //initialize a best value
            Position bestPosition = null; //initialize a best position
            String[] possibleMoves = Runner.search.getPossibleMovesByCasing(pos, 'c');
            //get all possible moves here...
            for (int i = 0; i < possibleMoves.length; i++) {
                //clone position object into new child objects which can have their own moves done on them
                Position childPos = pos.getPositionCopy();
                //create a new child position to move and evaluate
                childPos.fromToMove(possibleMoves[i]); //make the move
                childPos.addMove(possibleMoves[i]); //adds the move to that arraylist for that child
                //Gets the result from minimax
                Position tempPosition = minimax(childPos, depth-1, false, alpha, beta);
                //checks if the move is better
                if(tempPosition.getBoardEvaluation()>bestValue){
                    //if move is better, update bestValue and bestPosition (that already has the move list within it.
                    bestValue = tempPosition.getBoardEvaluation();
                    bestPosition = tempPosition;
                }
//                //need to come back to understand the alpha beta pruning
                alpha = Math.max(alpha, bestValue);
                if(beta <= alpha){
                    break;
                }
            }
            return bestPosition;
        }else{ //Lower Case
            int bestValue = MAX;
            String[] possibleMoves = Runner.search.getPossibleMovesByCasing(pos, 'l');
            Position bestPosition = null;
            //get all possible moves here...
            for (int i = 0; i < possibleMoves.length; i++) {
                //clone position object into new child objects which can have their own moves done on them
                Position childPos = pos.getPositionCopy();
                //create a new child position to move and evaluate
                childPos.fromToMove(possibleMoves[i]); //make the move
                childPos.addMove(possibleMoves[i]); //adds the move to that arraylist for that child
                //Gets the result from minimax
                Position tempPosition = minimax(childPos, depth-1, true, alpha, beta);
                //checks if the move is better
                if(tempPosition.getBoardEvaluation()<bestValue){
                    //if move is better, update bestValue and bestPosition (that already has the move list within it.
                    bestValue = tempPosition.getBoardEvaluation();
                    bestPosition = tempPosition;
                }
//                //Need to come back to understand the alpha beta pruning
                beta = Math.min(beta, bestValue);
                if(beta <= alpha){
                    break;
                }
            }
            return bestPosition;
        }
    }


}
