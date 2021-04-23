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


        mainBoard.drawGameBoard(mainBoard.mainPosition.getCurrentBoard());

//        System.out.println(mainBoard.mainPosition.getLowerCaseAFileRookHasMoved());
//        System.out.println(mainBoard.mainPosition.getLowerCaseKingHasMoved());
        mainBoard.mainPosition.fromToMove("b2b1n");
//        System.out.println(mainBoard.mainPosition.getCapitalHFileRookHasMoved());
//        System.out.println(mainBoard.mainPosition.getCapitalKingHasMoved());
//        mainBoard.mainPosition.fromToMove("e7", "e8");
//        System.out.println(mainBoard.mainPosition.getCapitalHFileRookHasMoved());
//        System.out.println(mainBoard.mainPosition.getCapitalKingHasMoved());
//        mainBoard.mainPosition.fromToMove("e8", "c8");
//        System.out.println(mainBoard.mainPosition.getLowerCaseAFileRookHasMoved());
//        System.out.println(mainBoard.mainPosition.getLowerCaseKingHasMoved());

        //mainBoard.mainPosition.fromToMove("e2", "e4");

        mainBoard.drawGameBoard(mainBoard.mainPosition.getCurrentBoard());

//        mainBoard.visualizeBitboardArray(mainBoard.mainPosition.getCurrentBoardHistory());
//        mainBoard.visualizeBitboardArray(mainBoard.mainPosition.getCurrentBoard());

    }
}

