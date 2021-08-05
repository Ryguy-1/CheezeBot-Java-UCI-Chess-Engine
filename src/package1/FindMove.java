package package1;

import java.time.LocalTime;
import java.util.ArrayList;

public class FindMove{


    public Position findBestPosition(Position pos, int depth, boolean isMaximizingPlayer, int alpha, int beta){
//
//        LocalTime time = LocalTime.now();
//        LocalTime endTime = time.plusSeconds(Runner.minimax.timeAvailable);
//
//        // Alert User of Threading
//        if(Runner.minimax.processorNum>20){
//            System.out.println("Only Using 20/" + Runner.minimax.processorNum + " threads available");
//        }else{
//            System.out.println(Runner.minimax.processorNum + " threads available");
//        }
//
//        //find initial available moves (20 in standard chess)
//        String[] availableMoves;
//        availableMoves = isMaximizingPlayer ? Runner.search.getPossibleMovesByCasing(pos, 'c') : Runner.search.getPossibleMovesByCasing(pos, 'l');
//        int bestValue = isMaximizingPlayer ? Runner.minimax.MIN : Runner.minimax.MAX; //initialize a best value
//        for(String s : availableMoves){
//            System.out.println(s);
//        }
//
//        ArrayList<Position> firstPositions = new ArrayList<>();
//        // Thread first positions -> Good for any thread count under 20
//        for(String move : availableMoves){
//            Position childPos = pos.getPositionCopy();
//            //create a new child position to move and evaluate
//            childPos.fromToMove(move); //make the move
//            childPos.addMove(move); //adds the move to that arraylist for that child
//            //Gets the result from minimax
//            firstPositions.add(new Minimax(childPos, depth - 1, !isMaximizingPlayer, alpha, beta).getReturnedPosition()); // opposite of isMaximizingPlayer
//        }
//        Position bestPos = pos;
//        int cyclesChecked = 0;
//        while (LocalTime.now() != endTime) {
//            for (Position firstPos : firstPositions) {
//                if (isMaximizingPlayer) { // capital
//                    try{
//                        if (firstPos.getBoardEvaluation() > bestValue) {
//                            bestPos = firstPos;
//                            bestValue = firstPos.getBoardEvaluation();
//                        }
//                    }catch(NullPointerException e){
//                        // If nothing is returned yet
//                    }
//                } else {// lower case
//                    try{
//                        if (firstPos.getBoardEvaluation() < bestValue) {
//                            bestPos = firstPos;
//                            bestValue = firstPos.getBoardEvaluation();
//                        }
//                    }catch(NullPointerException e){
//                        // If nothing is returned yet
//                    }
//                }
//            }
//            cyclesChecked++;
//            if (cyclesChecked % 100000 == 0) {
//                System.out.println("Threads Finished = " + firstPositions.size());
//            }
//        }
//        return bestPos;


        Minimax min = new Minimax(pos, depth - 1, false, alpha, beta);
        min.start();
        return min.getReturnedPosition();

    }

}
