package package1;

public class Runner {
    public static MainBoard mainBoard;
    public static CheckValidConditions checkValidConditions;
    public static BitboardControlAndSeparation controlAndSeparation;
    public static Search search;
    public static void main(String[] args) {
        //initialize mainBoard FIRST
        mainBoard = new MainBoard();
        checkValidConditions = new CheckValidConditions();
        controlAndSeparation = new BitboardControlAndSeparation();
        search = new Search();


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//        System.out.println("Capital Is In Check: " + checkValidConditions.capitalIsInCheck(mainBoard.mainBoard));
//        System.out.println("Lower Case Is In Check: " + checkValidConditions.lowerCaseIsInCheck(mainBoard.mainBoard));

        Long[][] splitBoard = controlAndSeparation.splitBitboardArray(mainBoard.mainBoard);

        for (int i = 0; i < splitBoard.length; i++) {
            for (int j = 0; j < splitBoard[i].length; j++) {
                mainBoard.visualizeBitboard(splitBoard[i][j]);
            }
        }


//En Passant Testing
//        mainBoard.visualizeBitboard(checkValidConditions.getLowerCasePawnAttacks(mainBoard.mainBoard, mainBoard.mainBoardHistory));
//        mainBoard.mainBoardHistory = mainBoard.mainBoard;
//        mainBoard.mainBoard = controlAndSeparation.fromToMove("e4", "e5", mainBoard.mainBoard);
//
//        mainBoard.visualizeBitboardArray(mainBoard.mainBoardHistory);
//        mainBoard.visualizeBitboardArray(mainBoard.mainBoard);
//
//        mainBoard.visualizeBitboard(checkValidConditions.getLowerCasePawnAttacks(mainBoard.mainBoard, mainBoard.mainBoardHistory));

    }
}

