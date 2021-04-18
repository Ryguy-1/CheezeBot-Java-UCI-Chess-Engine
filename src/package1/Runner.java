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
//NEXT: FINISH UP TESTING ALL THE PUBLIC METHODS IN CHECKVALIDCONDITIONS, THEN ADD CASTLING
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




    //methods that work in checkvalidconditions:
        //PAWNS - tested
        //- getCapitalEnPassantMoves
        //- getLowerCaseEnPassantMoves
        //- getCapitalPawnCombined (both)
        //- getLowerCasePawnCombined (both)

        //KNIGHTS - tested
        //- getCapitalKnightMoves (both)
        //- getLowerCaseKnightMoves (both)

        //


        long thisPiece = controlAndSeparation.splitBitboard(mainBoard.mainPosition.getCurrentBoard()[1])[0];

        mainBoard.visualizeBitboardArray(mainBoard.mainPosition.getCurrentBoard());

        mainBoard.visualizeBitboard(checkValidConditions.getLowerCaseKnightMoves(mainBoard.mainPosition));





//        mainBoard.visualizeBitboardArray(mainBoard.mainPosition.getCurrentBoardHistory());
//        mainBoard.visualizeBitboardArray(mainBoard.mainPosition.getCurrentBoard());

    }
}

