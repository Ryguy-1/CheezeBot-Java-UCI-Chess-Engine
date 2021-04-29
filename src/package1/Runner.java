package package1;

import java.util.Stack;

public class Runner {
    public static MainBoard mainBoard;
    public static CheckValidConditions checkValidConditions;
    public static BitboardControlAndSeparation controlAndSeparation;
    public static Search search;
    public static BoardEvaluation boardEvaluation;
    public static ZobristHash zobristHash;
    public static Minimax minimax;
    public static UCI uci;

    public static void main(String[] args){
        //initialize mainBoard FIRST
        mainBoard = new MainBoard();
        checkValidConditions = new CheckValidConditions();
        controlAndSeparation = new BitboardControlAndSeparation();
        search = new Search();
        boardEvaluation = new BoardEvaluation();
        zobristHash = new ZobristHash();
        minimax = new Minimax();
        uci = new UCI();


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//NEXT: Test all checkmate, mate, stalemates, etc..., along with making sure CheckValidConditions methods are perfect.

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        mainBoard.mainPosition.setCapitalAFileRookHasMoved(true);
        mainBoard.mainPosition.setCapitalHFileRookHasMoved(true);
        mainBoard.mainPosition.setLowerCaseAFileRookHasMoved(true);
        mainBoard.mainPosition.setLowerCaseHFileRookHasMoved(true);
        mainBoard.mainPosition.setCapitalKingHasMoved(true);
        mainBoard.mainPosition.setLowerCaseKingHasMoved(true);
        mainBoard.drawGameBoard(mainBoard.mainPosition.getCurrentBoard());
        //to make a move, do it here for testing...
        long thisPiece = mainBoard.parseLong("0000000000000000000000000000000000000000000000000000000000000000", 3); //if you want to reference a specific piece, just change a bit in this long and use the reference

        //System.out.println(controlAndSeparation.splitBitboard(checkValidConditions.getPseudoLegalMoves(mainBoard.mainPosition, 'c')).length);

        System.out.println();
        //call minimax
        Position bestPositionEvaluated = minimax.minimax(mainBoard.mainPosition, 4, false, Minimax.MIN, Minimax.MAX);

        //print out some values from the minimax evaluation
        System.out.println(bestPositionEvaluated.getMovesToCurrent());
        System.out.println("Best Move: " + bestPositionEvaluated.getMovesToCurrent().get(0));
        System.out.println();
        mainBoard.mainPosition = bestPositionEvaluated;

        //////////////////////////////////////////
        mainBoard.drawGameBoard(mainBoard.mainPosition.getCurrentBoard());
        System.out.println("Ranking: " + boardEvaluation.getBoardRanking(mainBoard.mainPosition));
        System.out.println("Capital is in Checkmate: " + search.capitalIsInCheckmate(mainBoard.mainPosition));
        System.out.println("Lower Case is in Checkmate: " + search.lowerCaseIsInCheckmate(mainBoard.mainPosition));
        System.out.println();
        System.out.println("Capital is in Check: " + search.capitalIsInCheck(mainBoard.mainPosition));
        System.out.println("Lower Case is in Check: " + search.lowerCaseIsInCheck(mainBoard.mainPosition));
        System.out.println();
        System.out.println("Capital is in Stalemate: " + search.capitalIsInStalemate(mainBoard.mainPosition));
        System.out.println("Lower Case is in Stalemate: " + search.lowerCaseIsInStalemate(mainBoard.mainPosition));



//        mainBoard.visualizeBitboardArray(mainBoard.mainPosition.getCurrentBoardHistory());
//        mainBoard.visualizeBitboardArray(mainBoard.mainPosition.getCurrentBoard());

    }
}
