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

        mainBoard.visualizeBitboardArray(mainBoard.mainPosition.getCurrentBoard());

        mainBoard.visualizeBitboard(checkValidConditions.getCapitalKingMoves(mainBoard.mainPosition));



//        mainBoard.visualizeBitboardArray(mainBoard.mainPosition.getCurrentBoardHistory());
//        mainBoard.visualizeBitboardArray(mainBoard.mainPosition.getCurrentBoard());

    }
}

