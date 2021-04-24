package package1;

public class Runner {
    public static MainBoard mainBoard;
    public static CheckValidConditions checkValidConditions;
    public static BitboardControlAndSeparation controlAndSeparation;
    public static Search search;
    public static UCI uci;
    public static void main(String[] args) {
        //initialize mainBoard FIRST
        mainBoard = new MainBoard();
        checkValidConditions = new CheckValidConditions();
        controlAndSeparation = new BitboardControlAndSeparation();
        search = new Search();
        uci = new UCI();


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//NEXT: Look over castling code and understand the logic. Then once it is efficient, stress test it. Then add promotions and move logic for castling like for en passants.

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////







        long thisPiece = mainBoard.parseLong("0000000000000000000000000000000000000000000000000000000000000000", 2);


        mainBoard.mainPosition.setCapitalAFileRookHasMoved(true);
        mainBoard.mainPosition.setCapitalHFileRookHasMoved(true);
        mainBoard.mainPosition.setLowerCaseAFileRookHasMoved(true);
        mainBoard.mainPosition.setLowerCaseHFileRookHasMoved(true);
        mainBoard.mainPosition.setCapitalKingHasMoved(true);
        mainBoard.mainPosition.setLowerCaseKingHasMoved(true);
        mainBoard.drawGameBoard(mainBoard.mainPosition.getCurrentBoard());

        mainBoard.mainPosition.fromToMove("b2b4");

        //mainBoard.visualizeBitboard(checkValidConditions.getLowerCasePawnCombined(mainBoard.mainPosition));

        mainBoard.drawGameBoard(mainBoard.mainPosition.getCurrentBoard());

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
