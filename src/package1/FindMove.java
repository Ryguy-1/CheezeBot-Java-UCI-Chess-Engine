package package1;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.util.ArrayList;
import java.util.Arrays;

public class FindMove {

    public ArrayList<String[]> findMove(Position inputPos, boolean isMaximizingPlayer, int numSimulations){
        return monteCarloTreeSearch(inputPos, isMaximizingPlayer, numSimulations);
    }


    // https://www.youtube.com/watch?v=62nq4Zsn8vc&ab_channel=JoshVarty
    private ArrayList<String[]> monteCarloTreeSearch(Position root, boolean isMaximizingPlayer, int numSimulations){
        // keep track of the most promising paths
        ArrayList<String[]> mostPromisingPaths = new ArrayList<>();

        // Expand the initial node before going into the loop
        try {
            expandNode(root, isMaximizingPlayer);
        }catch(NullPointerException e){
            System.out.println("Search Called with Terminal Position. Exiting.");
            System.exit(0);
        }

        // Run Simulations
        for (int i = 0; i < numSimulations; i++) {
            // node = the root node (start from the top)
            Position node = root;
            // keep track of the current search path
            ArrayList<Position> searchPath = new ArrayList<>();

            // traverse back down the tree -> when reach a new leaf, try and expand it
            while(node.hasBeenExpanded()){
                node = getBestUCBPosition(node, node.getChildNodes()); // gets the child with the highest UCB score
                searchPath.add(node);
            }

            int value = node.getBoardEvaluation();
            expandNode(node, !isMaximizingPlayer); // swaps each node expanding from a different player's perspective

            // Back up the tree
            if(searchPath.size()>1) {
                backpropagation(searchPath);
            }
        }

        Position bestPosition = null;
        int bestValue = isMaximizingPlayer ? -Integer.MAX_VALUE : Integer.MAX_VALUE;
//        System.out.println(root.getChildNodes().length);
//        for (int i = 0; i < root.getChildNodes().length; i++) {
//            System.out.println(root.getChildNodes()[i].getCollectiveValue());
//        }
        root.getChildNodes()[0].print();
        for(Position childPosition : root.getChildNodes()){
            if(isMaximizingPlayer && childPosition.getCollectiveValue()>bestValue){ // cap
                bestPosition = childPosition;
                bestValue = childPosition.getCollectiveValue();
            }else if(childPosition.getCollectiveValue()<bestValue){ // lc
                bestPosition = childPosition;
                bestValue = childPosition.getCollectiveValue();
            }
        }
        mostPromisingPaths.add(new String[] {bestPosition.getMovesToCurrent().get(0)});
        return mostPromisingPaths;
    }


    private void backpropagation(ArrayList<Position> searchPath){
        // Go through the arraylist from bottom to top (most recently added (toward leaf) backward)
        for (int i = searchPath.size()-1; i >= 0; i--) {
            searchPath.get(i).addVisit();
            if(i==searchPath.size()-1) {
                // if on the very bottom, set your own value to your board eval first
                searchPath.get(i).addFoundValueToNode(searchPath.get(i).getBoardEvaluation());
                // add newfound value of this node to its parent
                searchPath.get(i-1).addFoundValueToNode(searchPath.get(i).getCollectiveValue());
            }else if(i>=1){
                // add collective value of node to its parent
                searchPath.get(i-1).addFoundValueToNode(searchPath.get(i).getCollectiveValue());
            } // if i==0, top node, so don't do anything
        }

    }

    private Position[] expandNode(Position node, boolean isMaximizingPlayer){
        node.setHasBeenExpanded(true);
        String[] validMoves = isMaximizingPlayer ? Runner.search.getPossibleMovesByCasing(node, 'c') : Runner.search.getPossibleMovesByCasing(node, 'l');
        if(validMoves.length!=0) {
            Position[] returnedChildPositions = new Position[validMoves.length];
            for (int i = 0; i < validMoves.length; i++) {
                returnedChildPositions[i] = node.getPositionCopy();
                returnedChildPositions[i].fromToMove(validMoves[i]);
                returnedChildPositions[i].addMove(validMoves[i]);
                returnedChildPositions[i].setParentNode(node);
            }

            node.setChildNodes(returnedChildPositions);
            setPriorProbabilities(returnedChildPositions);
            return returnedChildPositions;
        } return null;
    }

    // Gets the UCB Score -> Variables: Prior probability, visit count of parent, visit count of child
    private double getUCBScore(Position parent, Position child){
        double priorScore = child.getPriorProbability() * Math.sqrt(parent.getVisitCount()) / (child.getVisitCount() + 1);
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
            boardEvaluations[i] = Math.abs(childNodes[i].getBoardEvaluation()); // take the absolute value of it -> How interesting it is to EITHER side
        }

        // Set prior probabilities to normalized value (all >0 and sum to 1)
        int sumBoardEvaluations = Arrays.stream(boardEvaluations).sum();
        for (int i = 0; i < boardEvaluations.length; i++) {
            childNodes[i].setPriorProbability(boardEvaluations[i]/sumBoardEvaluations);
        }
    }
}
