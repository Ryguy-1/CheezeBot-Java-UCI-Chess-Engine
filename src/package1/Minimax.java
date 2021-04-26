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
            int bestValue = MIN;
            String[] possibleMoves = Runner.search.getPossibleMovesByCasing(pos, 'c');
            //get all possible moves here...
            for (int i = 0; i < possibleMoves.length; i++) {
                //clone position object into new child objects which can have their own moves done on them
                Position childPos;
                try{
                    childPos = (Position)pos.clone();
                }catch(Exception e){
                    childPos = null;
                    System.out.println("Could Not Clone Position Object. Fatal.");
                }
                //create a new child position to move and evaluate
                childPos.fromToMove(possibleMoves[i]);
                //evaluate that move and set it equal to a new position object to use in calculations -> important because this new one will come back with a best move embedded within.
                Position minimaxCalcPosition = minimax(childPos, depth-1, false, alpha, beta);
                //if the board of the new one is better than the bestValue board...
                if(minimaxCalcPosition.getBoardEvaluation()>bestValue){
                    //best value becomes this board evaluation
                    bestValue = minimaxCalcPosition.getBoardEvaluation();
                    //list of bestMoves
                    //first update the stack to that of its new best predecessor
                    pos.setBestMoves(minimaxCalcPosition.getBestMoves());
                    //then push onto the stack the new best move
                    pos.pushBestMove(possibleMoves[i]); //push the new move to the best moves of its predecessor
                    //this position's best move is now updated as a node with a best move to be returned later...
                    pos.updateBestMove(possibleMoves[i]);
                }
//                //need to come back to understand the alpha beta pruning
                alpha = Math.max(alpha, bestValue);
                if(beta <= alpha){
                    break;
                }
            }
            return pos;
        }else{ //Lower Case
            int bestValue = MAX;
            String[] possibleMoves = Runner.search.getPossibleMovesByCasing(pos, 'l');
            //get all possible moves here...
            for (int i = 0; i < possibleMoves.length; i++) {
                //clone position object into new child objects which can have their own moves done on them
                Position childPos;
                try {
                    childPos = (Position) pos.clone();
                } catch (Exception e) {
                    childPos = null;
                    System.out.println("Could Not Clone Position Object. Fatal.");
                }
                //create a new child position to move and evaluate
                childPos.fromToMove(possibleMoves[i]);
                //evaluate that move and set it equal to a new position object to use in calculations -> important because this new one will come back with a best move embedded within.
                Position minimaxCalcPosition = minimax(childPos, depth-1, true, alpha, beta);
                //if the board of the new one is better than the bestValue board...
                if(minimaxCalcPosition.getBoardEvaluation()<bestValue){
                    //best value becomes this board evaluation
                    bestValue = minimaxCalcPosition.getBoardEvaluation();
                    //list of bestMoves
                    //first update the stack to that of its new best predecessor
                    pos.setBestMoves(minimaxCalcPosition.getBestMoves());
                    //then push onto the stack the new best move
                    pos.pushBestMove(possibleMoves[i]); //push the new move to the best moves of its predecessor
                    //this position's best move is now updated as a node with a best move to be returned later...
                    pos.updateBestMove(possibleMoves[i]);
                }
//                //Need to come back to understand the alpha beta pruning
                beta = Math.min(beta, bestValue);
                if(beta <= alpha){
                    break;
                }
            }
            return pos;
        }
    }


}
