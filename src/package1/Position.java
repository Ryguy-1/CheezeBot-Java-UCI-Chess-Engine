package package1;

import java.util.Stack;

public class Position {
    //The intent of this class is to encapsulate whether the lower case and upper case kings, and all four rooks have moved.
    //Also, holds the history board and the chain of moves which have led up to that board in a STACK!! of moves.

    private static Long[] referenceArray;
    private static boolean referenceArrayHasBeenInitialized;

    private static String[][] algebraicNotation = {
            {"a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8"},
            {"a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7"},
            {"a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6"},
            {"a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5"},
            {"a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4"},
            {"a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3"},
            {"a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2"},
            {"a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"}
    };

    ////////////////////////
    private boolean capitalAFileRookHasMoved = false;
    private boolean capitalHFileRookHasMoved = false;
    ////////////////////////
    private boolean lowerCaseAFileRookHasMoved = false;
    private boolean lowerCaseHFileRookHasMoved = false;
    ////////////////////////
    private boolean capitalKingHasMoved = false;
    private boolean lowerCaseKingHasMoved = false;
    ///////////////////////

    private Long[] currentBoardHistory = new Long[12];
    private Long[] currentBoard = new Long[12];
    private Stack<String> moves = new Stack<>();


    Position(Long[] currentBoard){
        if(currentBoard!=null && currentBoard.length == 12) {
            this.currentBoard = currentBoard;
            this.currentBoardHistory = currentBoard;
        }else{
            System.out.println("Error initializing Position Object");
        }

        if(!referenceArrayHasBeenInitialized){
            referenceArray = new Long[64];
            referenceArray = populateReferenceArray(referenceArray);
        }
    }

    //overloaded constructor for setting custom board history
    Position(Long[] currentBoard, Long[] currentBoardHistory){
        if(currentBoard!=null && currentBoardHistory !=null && currentBoard.length == 12 && currentBoardHistory.length == 12) {
            this.currentBoard = currentBoard;
            this.currentBoardHistory = currentBoardHistory;
        }else{
            System.out.println("Error initializing Position Object Overloaded Constructor");
        }

        if(!referenceArrayHasBeenInitialized){
            referenceArray = new Long[64];
            referenceArray = populateReferenceArray(referenceArray);
        }
    }


    public boolean getCapitalAFileRookHasMoved() {
        return capitalAFileRookHasMoved;
    }
    public void setCapitalAFileRookHasMoved(boolean capitalAFileRookHasMoved) {
        this.capitalAFileRookHasMoved = capitalAFileRookHasMoved;
    }

    public boolean getCapitalHFileRookHasMoved() {
        return capitalHFileRookHasMoved;
    }
    public void setCapitalHFileRookHasMoved(boolean capitalHFileRookHasMoved) {
        this.capitalHFileRookHasMoved = capitalHFileRookHasMoved;
    }

    public boolean getLowerCaseAFileRookHasMoved() {
        return lowerCaseAFileRookHasMoved;
    }
    public void setLowerCaseAFileRookHasMoved(boolean lowerCaseAFileRookHasMoved) {
        this.lowerCaseAFileRookHasMoved = lowerCaseAFileRookHasMoved;
    }

    public boolean getLowerCaseHFileRookHasMoved() {
        return lowerCaseHFileRookHasMoved;
    }
    public void setLowerCaseHFileRookHasMoved(boolean lowerCaseHFileRookHasMoved) {
        this.lowerCaseHFileRookHasMoved = lowerCaseHFileRookHasMoved;
    }

    public boolean getCapitalKingHasMoved() {
        return capitalKingHasMoved;
    }
    public void setCapitalKingHasMoved(boolean capitalKingHasMoved) {
        this.capitalKingHasMoved = capitalKingHasMoved;
    }

    public boolean getLowerCaseKingHasMoved() {
        return lowerCaseKingHasMoved;
    }
    public void setLowerCaseKingHasMoved(boolean lowerCaseKingHasMoved) {
        this.lowerCaseKingHasMoved = lowerCaseKingHasMoved;
    }


    public Long[] getCurrentBoard(){
        return currentBoard;
    }
    public Long[] getCurrentBoardHistory() {
        return currentBoardHistory;
    }

//    public void revertOneMove(){ //you lose history when you do this...
//        for (int i = 0; i < currentBoardHistory.length; i++) {
//            currentBoard[i] = currentBoardHistory[i];
//        }
//    }

    public void forceUpdatePosition(Long[] newBoard){
        currentBoardHistory = currentBoard;
        currentBoard = newBoard;
    }

    //method used in the Position class for updating - Need to update
    public void fromToMove(String from, String to){

        //if to and from are the same, there is no move, so return the same bitboard array
        if(from.equals(to)){
            return;
        }

        long fromBitboard = 0l;
        long toBitboard = 0l;
        char casing = 'c'; //will either be 'c' for capital or 'l' for lower case

        //creates bitboards with the locations of the to and from square from algebraic notation
        byte counter = 0;
        for (int i = 0; i < algebraicNotation.length; i++) {
            for (int j = 0; j < algebraicNotation[i].length; j++) {
                if (algebraicNotation[i][j].equals(from)) {
                    //63-counter = place in the reference array with the appropriate bitboard (reference array is created from right to left, not left to right)
                    fromBitboard = referenceArray[63-counter];
                }else if(algebraicNotation[i][j].equals(to)){
                    toBitboard = referenceArray[63-counter];
                }
                counter++;
            }
        }
        ///////////////
        //board the work is done on//
        Long[] copyBoard = new Long[currentBoard.length];
        for (int i = 0; i < currentBoard.length; i++) {
            copyBoard[i]=currentBoard[i];
        }
        ////////////

        //removes the bits from each bitboard which matches the bits in the from or to bitboards/////////////

        //For loops done individually because need to remove the bit first from the "to" spot, then alter the bit in the other spot
        for (int i = 0; i < copyBoard.length; i++) {
            //replace the 1 of the matching from bitboard with a 0 -> moved To deleted and added to original bitboard back after loop
            if((currentBoard[i]&toBitboard)!=0l) {
                copyBoard[i] ^= toBitboard;
            }
        }
        for (int i = 0; i < copyBoard.length; i++) {
            //replace the 1 of the matching from bitboard with a 0 -> moved From simply deleted
            if((currentBoard[i]&fromBitboard)!=0l){
                copyBoard[i] ^= fromBitboard;
                //this is also the bitboard in the array which we will add the toBitboard bit to... (save a step)
                copyBoard[i] |= toBitboard;
                //set casing while you have the array there
                if(i< copyBoard.length/2){
                    casing='l';
                }//else casing is already initialized to 'c'

            }
        }

        //EN PASSANT SPECIAL REMOVE/////////
        if(casing=='c'){
            //if the capital en passant move and the toBitboard align, it is an en passant.
            if((Runner.checkValidConditions.getCapitalEnPassantMoves(this)&toBitboard)!=0){
                //remove the pawn a rank down from toBitboard (where the pawn supposedly would have gone to
                copyBoard[5] ^= (toBitboard>>>8);
            }
        }else{
            //if the lower case en passant move and the toBitboard align, it is an en passant.
            if((Runner.checkValidConditions.getLowerCaseEnPassantMoves(this)&toBitboard)!=0){
                //remove the pawn a rank down from toBitboard (where the pawn supposedly would have gone to
                copyBoard[11] ^= (toBitboard<<8);
            }
        }
        ////////////////////

        //updates history as well...
        forceUpdatePosition(copyBoard);

    }
    public void fromToMove(long from, long to){

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //ASSUMES ALL CALCULATIONS ABOUT IT BEING AN APPROPRIATE MOVE HAVE BEEN DONE PREVIOUSLY. JUST EXECUTES THE DIRECTIONS. CASEBLIND//
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        char casing = 'c'; //will either be 'c' for capital or 'l' for lower case

        if((from^to)==0){
            return;
        }

        ///////////////
        //board the work is done on//
        Long[] copyBoard = new Long[12];
        for (int i = 0; i < currentBoard.length; i++) {
            copyBoard[i]=currentBoard[i];
        }
        ////////////

        //removes the bits from each bitboard which matches the bits in the from or to bitboards/////////////

        //For loops done individually because need to remove the bit first from the "to" spot, then alter the bit in the other spot
        for (int i = 0; i < copyBoard.length; i++) {
            //replace the 1 of the matching from bitboard with a 0 -> moved To deleted and added to original bitboard back after loop
            if((currentBoard[i]&to)!=0l) {
                copyBoard[i] ^= to;
            }
        }
        for (int i = 0; i < copyBoard.length; i++) {
            //replace the 1 of the matching from bitboard with a 0 -> moved From simply deleted
            if((currentBoard[i]&from)!=0l){
                copyBoard[i] ^= from;
                //this is also the bitboard in the array which we will add the toBitboard bit to... (save a step)
                copyBoard[i] |= to;
                //set casing while you have the array there
                if(i< copyBoard.length/2){
                    casing='l';
                }//else casing is already initialized to 'c'

            }
        }


        //EN PASSANT SPECIAL REMOVE/////////
        if(casing=='c'){
            //if the capital en passant move and the toBitboard align, it is an en passant.
            if((Runner.checkValidConditions.getCapitalEnPassantMoves(this)&to)!=0){
                //remove the pawn a rank down from toBitboard (where the pawn supposedly would have gone to
                copyBoard[5] ^= (to>>>8);
            }
        }else{
            //if the lower case en passant move and the toBitboard align, it is an en passant.
            if((Runner.checkValidConditions.getLowerCaseEnPassantMoves(this)&to)!=0){
                //remove the pawn a rank down from toBitboard (where the pawn supposedly would have gone to
                copyBoard[11] ^= (to<<8);
            }
        }
        ////////////////////
        forceUpdatePosition(copyBoard);
    }

    public boolean moveLeadsToCheck(String from, String to, char pieceInCheck){

        //create new Position with same history to check for check in new position once moved
        Position testingPosition = new Position(currentBoard, currentBoardHistory);
        //move the piece in new object from from to to.
        testingPosition.fromToMove(from, to);

        //cannot do auto casing because it may choose the wrong piece on accident if you check the enemy, but you are not in check anymore -> Niche, but can be avoided easily.
        //char casing = getCasing(testingPosition, from);

        //checks if either capital or lower case (depending on who moved) is in check after the move
        if(pieceInCheck=='c' && Runner.search.capitalIsInCheck(testingPosition)){
            return true;
        }else if(pieceInCheck=='l' && Runner.search.lowerCaseIsInCheck(testingPosition)){
            return true;
        }

        return false;
    }

    public boolean moveLeadsToCheck(long from, long to, char pieceInCheck){

        //create new Position with same history to check for check in new position once moved
        Position testingPosition = new Position(currentBoard, currentBoardHistory);
        //move the piece in new object from from to to.
        testingPosition.fromToMove(from, to);

        //cannot do auto casing because it may choose the wrong piece on accident if you check the enemy, but you are not in check anymore -> Niche, but can be avoided easily.
        //char casing = getCasing(testingPosition, from);

        //checks if either capital or lower case (depending on who moved) is in check after the move
        if(pieceInCheck=='c' && Runner.search.capitalIsInCheck(testingPosition)){
            return true;
        }else if(pieceInCheck=='l' && Runner.search.lowerCaseIsInCheck(testingPosition)){
            return true;
        }

        return false;
    }

    private char getCasing(Position pos, long piece){
        for (int i = 0; i < pos.getCurrentBoard().length; i++) {
            //replace the 1 of the matching from bitboard with a 0 -> moved From simply deleted
            if((pos.getCurrentBoard()[i]&piece)!=0l){
                //set casing while you have the array there
                if(i < pos.getCurrentBoard().length/2){
                    return 'l';
                }return 'c';
            }
        }
        return 'X';
    }

    private char getCasing(Position pos, String piece){

        long pieceBitboard = 0l;

        //creates bitboards with the locations of the to and from square from algebraic notation
        byte counter = 0;
        for (int i = 0; i < algebraicNotation.length; i++) {
            for (int j = 0; j < algebraicNotation[i].length; j++) {
                if (algebraicNotation[i][j].equals(piece)) {
                    //63-counter = place in the reference array with the appropriate bitboard (reference array is created from right to left, not left to right)
                    pieceBitboard = referenceArray[63-counter];
                }
                counter++;
            }
        }

        for (int i = 0; i < pos.getCurrentBoard().length; i++) {
            //replace the 1 of the matching from bitboard with a 0 -> moved From simply deleted
            if((pos.getCurrentBoard()[i]&pieceBitboard)!=0l){
                //set casing while you have the array there
                if(i < pos.getCurrentBoard().length/2){
                    return 'l';
                }return 'c';
            }
        }
        return 'X';
    }

    private Long[] populateReferenceArray(Long[] referenceArray){
        long temp = 1l;
        for (int i = 0; i < referenceArray.length; i++) {
            referenceArray[i] = temp << i;
        }
        return referenceArray;
    }


}