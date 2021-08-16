package package1;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.OptionalDouble;

public class FindMove {

    public ArrayList<String> findMove(Position inputPos, boolean isMaximizingPlayer, int numSimulations){
        return monteCarloTreeSearch(inputPos, isMaximizingPlayer, numSimulations);
    }


    // https://www.youtube.com/watch?v=62nq4Zsn8vc&ab_channel=JoshVarty
    private ArrayList<String> monteCarloTreeSearch(Position root, boolean isMaximizingPlayer, int numSimulations){
        ArrayList<String> mostPromisingPath = new ArrayList<String>();
        // get a root node (startPos) and expand it -> valid actions and probability of each
        Position[] childNodes = expandNode(root, isMaximizingPlayer);
        setPriorProbabilities(childNodes);

        // Run Simulations
        for (int i = 0; i < numSimulations; i++) {
            Position node = root;
            ArrayList<Position> searchPath = new ArrayList<>();

            while(node.hasBeenExpanded()){
                node = getBestUCBPosition(node, childNodes); // gets the child with the highest UCB score
                searchPath.add(node);
            }

            Position parentNode = node.getParentNode(); // Alternate: = searchPath.get(searchPath.size()-2);
            Position nextState = node;

            int value = Runner.search.boardIsInCheckmate(nextState);

            // if not a winner or loser here, let the boardEval decide what normalized value is
            if (value==0){
                // Expand and average those values...
                Position[] childPositions = expandNode(node, !isMaximizingPlayer);
                int counter = 0, totalValue = 0;
                for(Position pos : childPositions){
                    counter++; totalValue+=pos.getPriorProbability();
                }
            }

            // Back up the tree



        }


        return mostPromisingPath;
    }

    private Position[] expandNode(Position node, boolean isMaximizingPlayer){
        node.setHasBeenExpanded(true);
        Position[] returnedChildPositions;
        String[] validMoves;
        if(isMaximizingPlayer){
            validMoves = Runner.search.getPossibleMovesByCasing(node, 'c');
        }else{
            validMoves = Runner.search.getPossibleMovesByCasing(node, 'l');
        }
        returnedChildPositions = new Position[validMoves.length];
        for (int i = 0; i < validMoves.length; i++) {
            returnedChildPositions[i] = node.getPositionCopy();
            returnedChildPositions[i].fromToMove(validMoves[i]);
            returnedChildPositions[i].setParentNode(node);
        }
        return returnedChildPositions;

    }

    private double getUCBScore(Position parent, Position child){
        double priorScore = child.getPriorProbability() * Math.sqrt(parent.getVisitCount()) / (child.getVisitCount() + 1);
        //^^ raw curiosity / child.getPriorProbability takes into account the score of the node
        return priorScore;
        // LEAVING OUT VALUE ADDITION RIGHT NOW.... 21:56 of video above...
    }

    private Position getBestUCBPosition(Position parentNode, Position[] childNodes){
        for (Position childNode : childNodes){
            childNode.setUCBScore(getUCBScore(parentNode, childNode));
        }
        int bestScoreIndex = 0;
        for (int i = 0; i < childNodes.length; i++) {
            if(childNodes[i].getUCBScore()>childNodes[bestScoreIndex].getUCBScore()){
                bestScoreIndex = i;
            }
        }
        return childNodes[bestScoreIndex];
    }


    private void setPriorProbabilities(Position[] childNodes){
        // Find Board Evaluations
        int[] boardEvaluations = new int[childNodes.length];
        for (int i = 0; i < childNodes.length; i++) {
            boardEvaluations[i] = childNodes[i].getBoardEvaluation();
        }

        // Normalize values to sum to 1 (Find probabilities of each move) -> Policy Network (Can be NN Later On)
        int sumBoardEvaluations = Arrays.stream(boardEvaluations).sum();
        int[] normalizedEvaluations = new int[boardEvaluations.length];
        for (int i = 0; i < boardEvaluations.length; i++) {
            normalizedEvaluations[i] = boardEvaluations[i]/sumBoardEvaluations;
        }

        // Set the Prior probability
        for (int i = 0; i < childNodes.length; i++) {
            childNodes[i].setPriorProbability(normalizedEvaluations[i]);
        }
    }
}
