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



        mainBoard.visualizeBitboard(checkValidConditions.getLowerCasePawnAttacksWithoutEnPassant(mainBoard.mainBoard));
        mainBoard.visualizeBitboard(checkValidConditions.getCapitalKingMoves(mainBoard.mainBoard));


        //mainBoard.visualizeBitboard(checkValidConditions.getLowerCasePawnCombined(Runner.mainBoard.parseLong("0000000001000000000000000000000000000000000000000000000000000000", 2), mainBoard.mainBoard));

        //mainBoard.visualizeBitboard(checkValidConditions.getCapitalPawnCombined(Runner.mainBoard.parseLong("0000000000000000000000000000000000000000000000000000001000000000", 2), mainBoard.mainBoard));

        //mainBoard.visualizeBitboard(checkValidConditions.getCapitalPawnForwardMoves(mainBoard.mainBoard));
        /////////////////////////////
        System.out.println();
        System.out.println("Capital Is In Check: " + checkValidConditions.capitalIsInCheck(mainBoard.mainBoard));
        System.out.println("Lower Case Is In Check: " + checkValidConditions.lowerCaseIsInCheck(mainBoard.mainBoard));
        System.out.println();
        System.out.println("All Capital Moves Lead To Check: " + search.allCapitalMovesEqualCheck(mainBoard.mainBoard));
        System.out.println("All Lower Case Moves Lead To Check: " + search.allLowerCaseMovesEqualCheck(mainBoard.mainBoard));
        System.out.println();
        System.out.println("Capital Is In CheckMATE: " + search.capitalIsInCheckmate(mainBoard.mainBoard));
        System.out.println("Lower Case Is In CheckMATE: " + search.lowerCaseIsInCheckmate(mainBoard.mainBoard));
        System.out.println();
        System.out.println("Capital Is In Stalemate: " + search.capitalIsInStalemate(mainBoard.mainBoard));
        System.out.println("Lower Case Is In Stalemate: " + search.lowerCaseIsInStalemate(mainBoard.mainBoard));
//
//        Long[][] splitBoard = controlAndSeparation.splitBitboardArray(mainBoard.mainBoard);
//
//        for (int i = 0; i < splitBoard.length; i++) {
//            for (int j = 0; j < splitBoard[i].length; j++) {
//                mainBoard.visualizeBitboard(splitBoard[i][j]);
//            }
//        }
//
//        mainBoard.visualizeBitboardArray(controlAndSeparation.condense2dBoard(splitBoard));


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

