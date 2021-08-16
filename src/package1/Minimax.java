package package1;

import sun.invoke.empty.Empty;

import java.util.ArrayList;
import java.util.EmptyStackException;

public class Minimax {

    //Capital = maximize
    //Lower Case = minimize

    public static final int MAX = Integer.MAX_VALUE;
    public static final int MIN = -Integer.MAX_VALUE; //Integer.MIN_VALUE is still a positive number because of rollovers or something

    public static long positionsChecked = 0;

    // USES UCI.LOOKAHEAD!!!
    public Position minimax(Position pos, int depth, boolean isMaximizingPlayer, int alpha, int beta){

        if(depth==0){
            return pos;
        }

        if(isMaximizingPlayer){ //Capital
            int bestValue = MIN; //initialize a best value
            int leastMoves = MAX;
            Position bestPosition = null; //initialize a best position
            String[] possibleMoves = Runner.search.getPossibleMovesByCasing(pos, 'c');

            //if there are no moves, you have reached a leaf, so return it
            if (possibleMoves.length==0) {
                return pos;
            }

            //get all possible moves here...
            for (int i = 0; i < possibleMoves.length; i++) {

                //clone position object into new child objects which can have their own moves done on them
                Position childPos = pos.getPositionCopy();

                //create a new child position to move and evaluate
                childPos.fromToMove(possibleMoves[i]); //make the move
                childPos.addMove(possibleMoves[i]); //adds the move to that arraylist for that child

                //Gets the result from minimax / has function work
                Position tempPosition = minimax(childPos, depth-1, !isMaximizingPlayer, alpha, beta);


                /////////////////////////////////////////////////////////
//                Position tempPosition;
//
//                if (Runner.hash.isInTable(childPos)) {
//                    tempPosition = Runner.hash.getPositionFromHashedPosition(childPos).getPositionCopy();
//                }else{
//                    tempPosition = minimax(childPos, depth-1, !isMaximizingPlayer, alpha, beta);
//                    Runner.hash.addPositionToTable(tempPosition);
//                }



                ///////////////////////////////////////////////

                // Add position checked to counter
                positionsChecked++;

                //checks if the move is better
                if (tempPosition.getBoardEvaluation() > bestValue) {
                    leastMoves = tempPosition.getMovesToCurrent().size();
                    bestValue = tempPosition.getBoardEvaluation();
                    bestPosition = tempPosition;
                    //if it is the same evaluation and less moves, it should also go
                } else if (tempPosition.getBoardEvaluation()==bestValue && tempPosition.getMovesToCurrent().size()<leastMoves) {
                    leastMoves = tempPosition.getMovesToCurrent().size();
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
            int leastMoves = MAX;
            Position bestPosition = null;
            String[] possibleMoves = Runner.search.getPossibleMovesByCasing(pos, 'l');
            //if there are no moves, you have reached a leaf, so return it
            if (possibleMoves.length==0) {
                return pos;
            }

            //get all possible moves here...
            for (int i = 0; i < possibleMoves.length; i++) {

                //clone position object into new child objects which can have their own moves done on them
                Position childPos = pos.getPositionCopy();

                //create a new child position to move and evaluate
                childPos.fromToMove(possibleMoves[i]); //make the move
                childPos.addMove(possibleMoves[i]); //adds the move to that arraylist for that child

                //Gets the result from minimax / has function work
                Position tempPosition = minimax(childPos, depth-1, !isMaximizingPlayer, alpha, beta);

                /////////////////////////////////////////////////////////
//                Position tempPosition;
//
//                if (Runner.hash.isInTable(childPos)) {
//                    tempPosition = Runner.hash.getPositionFromHashedPosition(childPos).getPositionCopy();
//                }else{
//                    tempPosition = minimax(childPos, depth-1, !isMaximizingPlayer, alpha, beta);
//                    Runner.hash.addPositionToTable(tempPosition);
//                }

                ///////////////////////////////////////////////


                // Add position checked to counter
                positionsChecked++;

                //checks if the move is better
                //if it is a better evaluation, it should go every time.
                if (tempPosition.getBoardEvaluation() < bestValue) {
                    leastMoves = tempPosition.getMovesToCurrent().size();
                    bestValue = tempPosition.getBoardEvaluation();
                    bestPosition = tempPosition;
                    //if it is the same evaluation and less moves, it should also go
                } else if (tempPosition.getBoardEvaluation()==bestValue && tempPosition.getMovesToCurrent().size()<leastMoves) {
                    leastMoves = tempPosition.getMovesToCurrent().size();
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
