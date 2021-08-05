package package1;


import javafx.geometry.Pos;

import java.lang.reflect.Array;
import java.time.LocalTime;
import java.util.ArrayList;

public class Minimax extends Thread{

    //Capital = maximize
    //Lower Case = minimize

    public static final int MAX = Integer.MAX_VALUE;
    public static final int MIN = -Integer.MAX_VALUE; //Double.MIN_VALUE is still a positive number because of rollovers or something

    int processorNum = Runtime.getRuntime().availableProcessors(); //number of threads available
    final int timeAvailable = 10; //in seconds

////////////////////////////////////////////////////////////////////////
    //using the position object as three things...
    //1) A way to store the board and the board history to be able to generate moves
    //2) A way to hold the best move calculated with minimax
    //3) An object that can be used to calculate the goodness of that node (a position object = a node in the minimax tree)
    /////////////////////////////////////////////////////////////////////

    private Position pos;
    private int depth;
    private boolean isMaximizingPlayer;
    private int alpha;
    private int beta;
    private Position returnedPosition;

    Minimax(Position pos, int depth, boolean isMaximizingPlayer, int alpha, int beta){
        this.pos = pos;
        this.depth = depth;
        this.isMaximizingPlayer = isMaximizingPlayer;
        this.alpha = alpha;
        this.beta = beta;
//        run();
    }

    @Override
    public void run() {
        super.run();
        minimax();
    }

    private Position minimax(){
        System.out.println("mini");

        if(depth==0){
            this.returnedPosition = pos;
            System.out.println("end");
        }

        if(isMaximizingPlayer) { //Capital
            int bestValue = MIN; //initialize a best value
            int leastMoves = MAX;
            Position bestPosition = null; //initialize a best position
            String[] possibleMoves = Runner.search.getPossibleMovesByCasing(pos, 'c');

            //if there are no moves, you have reached a leaf, so return it
            if (possibleMoves.length == 0) {
                return pos;
            }

            //get all possible moves here...
            for (int i = 0; i < possibleMoves.length; i++) {

                //clone position object into new child objects which can have their own moves done on them
                Position childPos = pos.getPositionCopy();

                //create a new child position to move and evaluate
                childPos.fromToMove(possibleMoves[i]); //make the move
                childPos.addMove(possibleMoves[i]); //adds the move to that arraylist for that child

                //Gets the result from minimax
                Minimax min = new Minimax(childPos, depth - 1, false, alpha, beta);
                min.start();
                Position tempPosition = min.getReturnedPosition();


                //checks if the move is better
                if (tempPosition.getBoardEvaluation() > bestValue) {
                    leastMoves = tempPosition.getMovesToCurrent().size();
                    bestValue = tempPosition.getBoardEvaluation();
                    bestPosition = tempPosition;
                    //if it is the same evaluation and less moves, it should also go
                } else if (tempPosition.getBoardEvaluation() == bestValue && tempPosition.getMovesToCurrent().size() < leastMoves) {
                    leastMoves = tempPosition.getMovesToCurrent().size();
                    bestValue = tempPosition.getBoardEvaluation();
                    bestPosition = tempPosition;
                }
//                //need to come back to understand the alpha beta pruning
                alpha = Math.max(alpha, bestValue);
                if (beta <= alpha) {
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

                //Gets the result from minimax
                Minimax min = new Minimax(childPos, depth - 1, false, alpha, beta);
                min.start();
                Position tempPosition = min.getReturnedPosition();

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


    public Position getReturnedPosition(){
        while(true){
            if(returnedPosition!=null){
                System.out.println("sfd");
                return returnedPosition;
            }
        }
    }


}
