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

        if(depth==0){
            return pos;
        }

        if(isMaximizingPlayer){ //Capital
            int bestValue = MIN; //initialize a best value
            Position bestPosition = null; //initialize a best position
            String[] possibleMoves = Runner.search.getPossibleMovesByCasing(pos, 'c');
            //both checkmates and stalemates cause null pointer exceptions
            //if you don't have any possible moves, it's because the opponent has put you in checkmate or stalemate
            if(possibleMoves.length==0){ //you have no moves
                return pos;
            }
            //get all possible moves here...
            for (int i = 0; i < possibleMoves.length; i++) {
                //clone position object into new child objects which can have their own moves done on them
                Position childPos = pos.getPositionCopy();
                //create a new child position to move and evaluate
                System.out.println(childPos.getMovesToCurrent() + " cap1");
                childPos.fromToMove(possibleMoves[i]); //make the move
                childPos.addMove(possibleMoves[i]); //adds the move to that arraylist for that child
                //Gets the result from minimax
                System.out.println("Depth cap: " + depth);

                Position tempPosition = minimax(childPos, depth-1, false, alpha, beta);
                System.out.println(childPos.getMovesToCurrent() + " cap2");
                //checks if the move is better
                if(tempPosition.getBoardEvaluation()>bestValue){
                    //if move is better, update bestValue and bestPosition (that already has the move list within it.
                    bestValue = tempPosition.getBoardEvaluation();
                    bestPosition = tempPosition;
                }
//                //need to come back to understand the alpha beta pruning
//                alpha = Math.max(alpha, bestValue);
//                if(beta <= alpha){
//                    break;
//                }
            }
            return bestPosition;
        }else{ //Lower Case
            int bestValue = MAX;
            Position bestPosition = null;
            String[] possibleMoves = Runner.search.getPossibleMovesByCasing(pos, 'l');
            for (int i = 0; i < possibleMoves.length; i++) {
                System.out.println(possibleMoves[i]);
            }
            //both checkmates and stalemates cause null pointer exceptions
            //if you don't have any possible moves, it's because the opponent has put you in checkmate or stalemate
            if(possibleMoves.length==0){ //you have no moves
                return pos;
            }
            //get all possible moves here...
            for (int i = 0; i < possibleMoves.length; i++) {
                System.out.println(i + " ==================================================================");
                //clone position object into new child objects which can have their own moves done on them
                Position childPos = pos.getPositionCopy();
                //create a new child position to move and evaluate
                childPos.fromToMove(possibleMoves[i]); //make the move
                childPos.addMove(possibleMoves[i]); //adds the move to that arraylist for that child
                //Gets the result from minimax
                System.out.println("Depth lc: " + depth);
                Position tempPosition = minimax(childPos, depth-1, true, alpha, beta);
                System.out.println(childPos.getMovesToCurrent() + " lc2");
                //checks if the move is better
                if(tempPosition.getBoardEvaluation()<bestValue){
                    //if move is better, update bestValue and bestPosition (that already has the move list within it.
                    bestValue = tempPosition.getBoardEvaluation();
                    bestPosition = tempPosition;
                }
//                //Need to come back to understand the alpha beta pruning
//                beta = Math.min(beta, bestValue);
//                if(beta <= alpha){
//                    break;
//                }
            }
            return bestPosition;
        }
    }


}
