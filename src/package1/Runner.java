package package1;

public class Runner {
    public static MainBoard mainBoard;
    public static CheckValidConditions checkValidConditions;
    public static BitboardControlAndSeparation controlAndSeparation;
    public static Search search;
    public static BoardEvaluation boardEvaluation;
    public static NewBoardEvaluation newBoardEvaluation;
    public static ZobristHash zobristHash;
    public static FindMove findMove;
    public static UCI uci;
    public static Openings openings;

    public static void main(String[] args){
        //initialize mainBoard FIRST
        openings = new Openings();
        mainBoard = new MainBoard();
        checkValidConditions = new CheckValidConditions();
        controlAndSeparation = new BitboardControlAndSeparation();
        search = new Search();
        boardEvaluation = new BoardEvaluation();
        newBoardEvaluation = new NewBoardEvaluation();
        zobristHash = new ZobristHash();
        findMove = new FindMove();
        uci = new UCI();


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//NEXT: Beat 1500: Handily with 77 percent accuracy vs its 25 percent.

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        // Board Evaluation





//
//        long start = System.nanoTime();
//
//        // call the method
//        Runner.findMove.findMove(Runner.mainBoard.mainPosition, Runner.mainBoard.mainBoardMoves, 4, true);
//
//        // get the end time
//        long end = System.nanoTime();
//
//        // execution time
//        long execution = end - start;
//        System.out.println("Execution time: " + execution + " nanoseconds");



//        mainBoard.mainPosition.setCapitalAFileRookHasMoved(true);
//        mainBoard.mainPosition.setCapitalHFileRookHasMoved(true);
//        mainBoard.mainPosition.setLowerCaseAFileRookHasMoved(true);
//        mainBoard.mainPosition.setLowerCaseHFileRookHasMoved(true);
//        mainBoard.mainPosition.setCapitalKingHasMoved(true);
//        mainBoard.mainPosition.setLowerCaseKingHasMoved(true);
//        mainBoard.drawGameBoard(mainBoard.mainPosition.getCurrentBoard());
//        //to make a move, do it here for testing...
//        long thisPiece = mainBoard.parseLong("0000000000000000000000000000000000000000000000000000000000000000", 3); //if you want to reference a specific piece, just change a bit in this long and use the reference

        //System.out.println(controlAndSeparation.splitBitboard(checkValidConditions.getPseudoLegalMoves(mainBoard.mainPosition, 'c')).length);


//        System.out.println();
//        //call minimax
//        Position bestPositionEvaluated = minimax.minimax(mainBoard.mainPosition, 1, true, Minimax.MIN, Minimax.MAX);
//
//        //print out some values from the minimax evaluation
//        System.out.println(bestPositionEvaluated.getMovesToCurrent());
//        System.out.println("Best Move: " + bestPositionEvaluated.getMovesToCurrent().get(0));
//        System.out.println();
//        for (int i = 0; i < bestPositionEvaluated.getMovesToCurrent().size(); i++) {
//            mainBoard.mainPosition.fromToMove(bestPositionEvaluated.getMovesToCurrent().get(i));
//            mainBoard.drawGameBoard(mainBoard.mainPosition.getCurrentBoard());
//        }
//
//        //////////////////////////////////////////
//        mainBoard.drawGameBoard(mainBoard.mainPosition.getCurrentBoard());
//        System.out.println("Ranking: " + boardEvaluation.getBoardRanking(mainBoard.mainPosition));
//        System.out.println("Capital is in Checkmate: " + search.capitalIsInCheckmate(mainBoard.mainPosition));
//        System.out.println("Lower Case is in Checkmate: " + search.lowerCaseIsInCheckmate(mainBoard.mainPosition));
//        System.out.println();
//        System.out.println("Capital is in Check: " + search.capitalIsInCheck(mainBoard.mainPosition));
//        System.out.println("Lower Case is in Check: " + search.lowerCaseIsInCheck(mainBoard.mainPosition));
//        System.out.println();
//        System.out.println("Capital is in Stalemate: " + search.capitalIsInStalemate(mainBoard.mainPosition));
//        System.out.println("Lower Case is in Stalemate: " + search.lowerCaseIsInStalemate(mainBoard.mainPosition));



//        mainBoard.visualizeBitboardArray(mainBoard.mainPosition.getCurrentBoardHistory());
//        mainBoard.visualizeBitboardArray(mainBoard.mainPosition.getCurrentBoard());

    }
}
