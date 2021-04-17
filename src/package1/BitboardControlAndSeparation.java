package package1;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BitboardControlAndSeparation {

    Long[] referenceArray;

    String[][] algebraicNotation = {
            {"a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8"},
            {"a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7"},
            {"a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6"},
            {"a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5"},
            {"a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4"},
            {"a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3"},
            {"a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2"},
            {"a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"}
    };

    BitboardControlAndSeparation(){
        referenceArray = new Long[64];
        referenceArray = populateReferenceArray(referenceArray);
    }


//////////////////////////Position Methods////////////////////////////////////////////////////





////////////////////Non-Position Methods///////////////////////////////////////////////
    //This Class:
    //Method to take a from square, a to square, and a bitboard array and make the necessarry changes
    public Long[] fromToMoveWithEnPassant(String from, String to, Long[] currentBoard, Long[] currentBoardHistory){
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //ASSUMES ALL CALCULATIONS ABOUT IT BEING AN APPROPRIATE MOVE HAVE BEEN DONE PREVIOUSLY. JUST EXECUTES THE DIRECTIONS. CASEBLIND//
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //if to and from are the same, there is no move, so return the same bitboard array
        if(from.equals(to)){
            return currentBoard;
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

        System.out.println(casing);
        //EN PASSANT SPECIAL REMOVE/////////
        if(casing=='c'){
            //if the capital en passant move and the toBitboard align, it is an en passant.
            if((Runner.checkValidConditions.getCapitalEnPassantMoves(currentBoard, currentBoardHistory)&toBitboard)!=0){
                //remove the pawn a rank down from toBitboard (where the pawn supposedly would have gone to
                copyBoard[5] ^= (toBitboard>>>8);
            }
        }else{
            //if the lower case en passant move and the toBitboard align, it is an en passant.
            if((Runner.checkValidConditions.getLowerCaseEnPassantMoves(currentBoard, currentBoardHistory)&toBitboard)!=0){
                //remove the pawn a rank down from toBitboard (where the pawn supposedly would have gone to
                copyBoard[11] ^= (toBitboard<<8);
            }
        }
        ////////////////////

        return copyBoard;
    }

    //overridden to allow for bitboards to work as well.
    public Long[] fromToMoveWithEnPassant(long from, long to, Long[] currentBoard, Long[] currentBoardHistory){

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //ASSUMES ALL CALCULATIONS ABOUT IT BEING AN APPROPRIATE MOVE HAVE BEEN DONE PREVIOUSLY. JUST EXECUTES THE DIRECTIONS. CASEBLIND//
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        char casing = 'c'; //will either be 'c' for capital or 'l' for lower case

        if((from^to)==0){
            return currentBoard;
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

        System.out.println(casing);
        //EN PASSANT SPECIAL REMOVE/////////
        if(casing=='c'){
            //if the capital en passant move and the toBitboard align, it is an en passant.
            if((Runner.checkValidConditions.getCapitalEnPassantMoves(currentBoard, currentBoardHistory)&to)!=0){
                //remove the pawn a rank down from toBitboard (where the pawn supposedly would have gone to
                copyBoard[5] ^= (to>>>8);
            }
        }else{
            //if the lower case en passant move and the toBitboard align, it is an en passant.
            if((Runner.checkValidConditions.getLowerCaseEnPassantMoves(currentBoard, currentBoardHistory)&to)!=0){
                //remove the pawn a rank down from toBitboard (where the pawn supposedly would have gone to
                copyBoard[11] ^= (to<<8);
            }
        }
        ////////////////////
        return copyBoard;
    }

    public Long[] fromToMove(String from, String to, Long[] currentBoard){
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //ASSUMES ALL CALCULATIONS ABOUT IT BEING AN APPROPRIATE MOVE HAVE BEEN DONE PREVIOUSLY. JUST EXECUTES THE DIRECTIONS. CASEBLIND//
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //if to and from are the same, there is no move, so return the same bitboard array
        if(from.equals(to)){
            return currentBoard;
        }
        long fromBitboard = 0l;
        long toBitboard = 0l;

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
            }
        }
        return copyBoard;
    }

    public Long[] fromToMove(long from, long to, Long[] currentBoard){

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //ASSUMES ALL CALCULATIONS ABOUT IT BEING AN APPROPRIATE MOVE HAVE BEEN DONE PREVIOUSLY. JUST EXECUTES THE DIRECTIONS. CASEBLIND//
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        if((from^to)==0){
            return currentBoard;
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
            }
        }
        ////////////////////
        return copyBoard;
    }
/////////////////////////////////////////////////////////////////////////////////////////


    //Method to Separate the individual pieces in a bitboard into multiple of their own bitboards
    public Long[] splitBitboard(long bitboard){
        ArrayList<Long> bitboards = new ArrayList<>();
        for (int i = 0; i < referenceArray.length; i++) {
            if((referenceArray[i]&bitboard)!=0l){
                bitboards.add(referenceArray[i]);
            }
        }

        Long[] bitboardsArray = new Long[bitboards.size()];
        for (int i = 0; i < bitboards.size(); i++) {
            bitboardsArray[i] = bitboards.get(i);
        }

        return bitboardsArray;
    }

    private Long[] populateReferenceArray(Long[] referenceArray){
        long temp = 1l;
        for (int i = 0; i < referenceArray.length; i++) {
            referenceArray[i] = temp << i;
        }
        return referenceArray;
    }

    public Long[][] splitBitboardArray(Long[] currentBoard){

        //each small array has a different size
        Long[][] splitBoard = {splitBitboard(currentBoard[0]),
                splitBitboard(currentBoard[1]),
                splitBitboard(currentBoard[2]),
                splitBitboard(currentBoard[3]),
                splitBitboard(currentBoard[4]),
                splitBitboard(currentBoard[5]),
                splitBitboard(currentBoard[6]),
                splitBitboard(currentBoard[7]),
                splitBitboard(currentBoard[8]),
                splitBitboard(currentBoard[9]),
                splitBitboard(currentBoard[10]),
                splitBitboard(currentBoard[11])};

        return splitBoard;

    }

    public Long[] condense2dBoard(Long[][] currentBoard){

        Long[] condensedBoard = new Long[12];
        //initialize condensedBoard contents
        for (int i = 0; i < condensedBoard.length; i++) {
            condensedBoard[i] = 0l;
        }


        for (int i = 0; i < currentBoard.length; i++) {
            for (int j = 0; j < currentBoard[i].length; j++) {
                condensedBoard[i] |= currentBoard[i][j];
            }
        }

        return condensedBoard;
    }

    public Long[] changeBitboardArrayIndex(Long[] currentBoard, int index, long bitboard){
        Long[] copyBoard = new Long[currentBoard.length];
        for (int i = 0; i < currentBoard.length; i++) {
            if(i==index){
                copyBoard[i] = bitboard;
            }else{
                copyBoard[i] = currentBoard[i];
            }
        }
        return copyBoard;
    }

    public Long[] getCapitalPieces(Long[] currentBoard){
        Long[] halfPieces = new Long[currentBoard.length/2];
        byte counter = 0;
        for (int i = currentBoard.length/2; i < currentBoard.length; i++) {
            halfPieces[counter] = currentBoard[i];
            counter++;
        }
        return halfPieces;
    }

    public Long[] getLowerCasePieces(Long[] currentBoard){
        Long[] halfPieces = new Long[currentBoard.length/2];
        for (int i = 0; i < currentBoard.length/2; i++) {
            halfPieces[i] = currentBoard[i];
        }
        return halfPieces;
    }

}
