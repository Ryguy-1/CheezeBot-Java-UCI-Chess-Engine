package package1;

import java.util.ArrayList;
import java.util.Stack;

public class Position{
    //The intent of this class is to encapsulate whether the lower case and upper case kings, and all four rooks have moved.
    //Also, holds the history board and the chain of moves which have led up to that board in a STACK!! of moves.

    private static Long[] referenceArray;
    private static boolean referenceArrayHasBeenInitialized;

    private final static String[][] algebraicNotation = {
            {"a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8"},
            {"a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7"},
            {"a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6"},
            {"a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5"},
            {"a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4"},
            {"a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3"},
            {"a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2"},
            {"a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"}
    };

    private boolean capitalHasCastled = false;
    private boolean lowerCaseHasCastled = false;
    public void setCapitalHasCastled(boolean capitalHasCastled){
        this.capitalHasCastled = capitalHasCastled;
    }
    public void setLowerCaseHasCastled(boolean lowerCaseHasCastled){
        this.lowerCaseHasCastled = lowerCaseHasCastled;
    }
    public boolean getCapitalHasCastled(){
        return capitalHasCastled;
    }
    public boolean getLowerCaseHasCastled(){
        return lowerCaseHasCastled;
    }

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

    //////////////////////////////////////////////////
    //can be used later to track sequence of moves. Not used yet!!
    private ArrayList<String> movesToCurrent = new ArrayList<>();
    public ArrayList<String> getMovesToCurrent(){
        return movesToCurrent;
    }
    public void addMove(String fromTo){
        movesToCurrent.add(fromTo);
    }
    //////////////////////////////////

    public int getBoardEvaluation(){ //can be optimized later...
        return Runner.boardEvaluation.getBoardRanking(this);
    }


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

    private void forceUpdatePosition(Long[] newBoard){
        currentBoardHistory = currentBoard;
        currentBoard = newBoard;
    }

    //method used in the Position class for updating - Need to update
    public void fromToMove(String fromTo){

        String from = fromTo.substring(0, 2);
        String to = fromTo.substring(2, 4);
        String promotion; try{ promotion = fromTo.substring(4); }catch(Exception e){ promotion = ""; } //not always a promotion

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


        //you have two current bits in a long. you want one of those bits to change to a new bit, but want to keep the other the same.

        //CASTLING//
        //if the piece you selected is a king and the to square is bit shifted either two to the left (long castle) or two to the right (short castle), then it is castling
        //capital king = position 10; capital rook = position 6;

        //if the starting square is the king; and there is a rook on the AFile starting square for capital; and you are moving the king 2 spaces to the left; and the a file rook hasn't moved; and the king hasn't moved
        if(((fromBitboard & copyBoard[10])!=0l) && ((Runner.checkValidConditions.capitalAFileRookStartSquare & copyBoard[6])!=0) && (toBitboard == fromBitboard<<2) && !capitalAFileRookHasMoved && !capitalKingHasMoved){
            //you are castling to the left
            //set the king equal to the space it's moved to
            copyBoard[10] = toBitboard;
            //set the rook to the inner space (right of king one)
            copyBoard[6] = ((copyBoard[6] ^ Runner.checkValidConditions.capitalAFileRookStartSquare) | (toBitboard>>>1));
            //sets states for king and rook
            capitalAFileRookHasMoved = true;
            capitalKingHasMoved = true;
            capitalHasCastled = true;
            //returns before anything else is done...
            forceUpdatePosition(copyBoard);
            return;
        }else if(((fromBitboard & copyBoard[10])!=0l) && ((Runner.checkValidConditions.capitalHFileRookStartSquare & copyBoard[6])!=0) && (toBitboard == fromBitboard>>>2)  && !capitalHFileRookHasMoved && !capitalKingHasMoved){
            //you are castling to the right
            //set the king equal to the space it's moved to
            copyBoard[10] = toBitboard;
            //set the rook to the inner space (left of king one)
            copyBoard[6] = ((copyBoard[6] ^ Runner.checkValidConditions.capitalHFileRookStartSquare) | (toBitboard<<1));
            //sets states for king and rook
            capitalHFileRookHasMoved = true;
            capitalKingHasMoved = true;
            capitalHasCastled = true;
            //returns before anything else is done...
            forceUpdatePosition(copyBoard);
            return;
        }//Lower Case King = position 4; capital rook = position 0;
        else if(((fromBitboard & copyBoard[4])!=0l) && ((Runner.checkValidConditions.lowerCaseAFileRookStartSquare & copyBoard[0])!=0) && (toBitboard == fromBitboard<<2)  && !lowerCaseAFileRookHasMoved && !lowerCaseKingHasMoved){
            //you are castling to the left
            //set the king equal to the space it's moved to
            copyBoard[4] = toBitboard;
            //set the rook to the inner space (right of king one)
            copyBoard[0] = ((copyBoard[0] ^ Runner.checkValidConditions.lowerCaseAFileRookStartSquare) | (toBitboard>>>1));
            //sets states for king and rook
            lowerCaseAFileRookHasMoved = true;
            lowerCaseKingHasMoved = true;
            lowerCaseHasCastled = true;
            //returns before anything else is done...
            forceUpdatePosition(copyBoard);
            return;
        }else if(((fromBitboard & copyBoard[4])!=0l) && ((Runner.checkValidConditions.lowerCaseHFileRookStartSquare & copyBoard[0])!=0) && (toBitboard == fromBitboard>>>2)  && !lowerCaseHFileRookHasMoved && !lowerCaseKingHasMoved){
            //you are castling to the right
            //set the king equal to the space it's moved to
            copyBoard[4] = toBitboard;
            //set the rook to the inner space (left of king one)
            copyBoard[0] = ((copyBoard[0] ^ Runner.checkValidConditions.lowerCaseHFileRookStartSquare) | (toBitboard<<1));
            //sets states for king and rook
            lowerCaseHFileRookHasMoved = true;
            lowerCaseKingHasMoved = true;
            lowerCaseHasCastled = true;
            //returns before anything else is done...
            forceUpdatePosition(copyBoard);
            return;
        }


        //System.out.println("Promotion = |" + promotion + "| ");

        ////////////////////


        //STANDARD PROCEDURES...////////////////////////////////////// (if not a promotion or a castle)///////////////////////////////

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
            if(((currentBoard[i]&fromBitboard)!=0l) && promotion.equals("")){ //and make sure there is no promotion
                copyBoard[i] ^= fromBitboard;
                //this is also the bitboard in the array which we will add the toBitboard bit to... (save a step)
                copyBoard[i] |= toBitboard;
                //set casing while you have the array there
                if(i< copyBoard.length/2){
                    casing='l';
                }//else casing is already initialized to 'c'
            }else if(((currentBoard[i]&fromBitboard)!=0l) && !promotion.equals("")){
                copyBoard[i] ^= fromBitboard;
                //REMOVED THE STEP WHERE YOU ADDED THE TOBITBOARD TO -> DO IT AFTERWARDS
                //set casing while you have the array there
                if(i< copyBoard.length/2){
                    casing='l';
                }//else casing is already initialized to 'c'
            }
        }

        //order in array: r, n, b, q, k, p, R, N, B, Q, K, P

        //add the toBitboard to the proper bitboard in copyBoard for promotions
        switch(promotion){
            case "q":
                if(casing == 'l'){
                    copyBoard[3] |= toBitboard; //3 = lc queen
                }else{
                    copyBoard[9] |= toBitboard; //9 = c queen
                }break;
            case "b":
                if(casing == 'l'){
                    copyBoard[2] |= toBitboard; //3 = lc bishop
                }else{
                    copyBoard[8] |= toBitboard; //9 = c bishop
                }break;
            case "n":
                if(casing == 'l'){
                    copyBoard[1] |= toBitboard; //3 = lc knight
                }else{
                    copyBoard[7] |= toBitboard; //9 = c knight
                }break;
            case "r":
                if(casing == 'l'){
                    copyBoard[0] |= toBitboard; //3 = lc rook
                }else{
                    copyBoard[6] |= toBitboard; //9 = c rook
                }break;
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

        //Sets states for kings//
        //capital king = position 10
        if((copyBoard[10] & toBitboard)!=0l){ //"toBitboard" because it has already been updated to that position by this time!
            capitalKingHasMoved = true;
        }else if((copyBoard[4] & toBitboard)!=0l){ //lower case king = position 4
            lowerCaseKingHasMoved = true;
        }
        //Sets states for rooks//
        //capital rooks = position 6
        //lower case rooks = position 0
        //if you previously had a rook on the a file for capital and now you don't, then you moved the a file rook. pogu
        else if(((currentBoard[6] ^ copyBoard[6]) ^ toBitboard) == Runner.checkValidConditions.capitalAFileRookStartSquare){ //"toBitboard" xor statement to get rid of the square that said piece was moved to. Isolates the square the rook should start on
            //the piece you moved was the Capital A File Rook
            capitalAFileRookHasMoved = true;
        }else if(((currentBoard[6] ^ copyBoard[6]) ^ toBitboard) == Runner.checkValidConditions.capitalHFileRookStartSquare){
            capitalHFileRookHasMoved = true;
        }else if(((currentBoard[0] ^ copyBoard[0]) ^ toBitboard) == Runner.checkValidConditions.lowerCaseAFileRookStartSquare){
            lowerCaseAFileRookHasMoved = true;
        }else if(((currentBoard[0] ^ copyBoard[0]) ^ toBitboard) == Runner.checkValidConditions.lowerCaseHFileRookStartSquare){
            lowerCaseHFileRookHasMoved = true;
        }


        //updates history as well...
        forceUpdatePosition(copyBoard);

    }

    public void fromToMove(long from, long to, String optionalPromotion){

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

        //CASTLING//
        //if the piece you selected is a king and the to square is bit shifted either two to the left (long castle) or two to the right (short castle), then it is castling
        //capital king = position 10; capital rook = position 6;

        if(((from & copyBoard[10])!=0l) && (to == from<<2) && !capitalAFileRookHasMoved && !capitalKingHasMoved){
            //you are castling to the left
            //set the king equal to the space it's moved to
            copyBoard[10] = to;
            //set the rook to the inner space (right of king one)
            copyBoard[6] = ((copyBoard[6] ^ Runner.checkValidConditions.capitalAFileRookStartSquare) | (to>>>1));
            //sets states for king and rook
            capitalAFileRookHasMoved = true;
            capitalKingHasMoved = true;
            capitalHasCastled = true;
            //returns before anything else is done...
            forceUpdatePosition(copyBoard);
            return;
        }else if(((from & copyBoard[10])!=0l) && (to == from>>>2)  && !capitalHFileRookHasMoved && !capitalKingHasMoved){
            //you are castling to the right
            //set the king equal to the space it's moved to
            copyBoard[10] = to;
            //set the rook to the inner space (left of king one)
            copyBoard[6] = ((copyBoard[6] ^ Runner.checkValidConditions.capitalHFileRookStartSquare) | (to<<1));
            //sets states for king and rook
            capitalHFileRookHasMoved = true;
            capitalKingHasMoved = true;
            capitalHasCastled = true;
            //returns before anything else is done...
            forceUpdatePosition(copyBoard);
            return;
        }//Lower Case King = position 4; capital rook = position 0;
        else if(((from & copyBoard[4])!=0l) && (to == from<<2)  && !lowerCaseAFileRookHasMoved && !lowerCaseKingHasMoved){
            //you are castling to the left
            //set the king equal to the space it's moved to
            copyBoard[4] = to;
            //set the rook to the inner space (right of king one)
            copyBoard[0] = ((copyBoard[0] ^ Runner.checkValidConditions.lowerCaseAFileRookStartSquare) | (to>>>1));
            //sets states for king and rook
            lowerCaseAFileRookHasMoved = true;
            lowerCaseKingHasMoved = true;
            lowerCaseHasCastled = true;
            //returns before anything else is done...
            forceUpdatePosition(copyBoard);
            return;
        }else if(((from & copyBoard[4])!=0l) && (to == from>>>2)  && !lowerCaseHFileRookHasMoved && !lowerCaseKingHasMoved){
            //you are castling to the right
            //set the king equal to the space it's moved to
            copyBoard[4] = to;
            //set the rook to the inner space (left of king one)
            copyBoard[0] = ((copyBoard[0] ^ Runner.checkValidConditions.lowerCaseHFileRookStartSquare) | (to<<1));
            //sets states for king and rook
            lowerCaseHFileRookHasMoved = true;
            lowerCaseKingHasMoved = true;
            lowerCaseHasCastled = true;
            //returns before anything else is done...
            forceUpdatePosition(copyBoard);
            return;
        }


        ////////////////////




        //STANDARD PROCEDURES...////////////////////////////////////// (if not a promotion or a castle)///////////////////////////////

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
            if((currentBoard[i]&from)!=0l  && optionalPromotion.equals("")){
                copyBoard[i] ^= from;
                //this is also the bitboard in the array which we will add the toBitboard bit to... (save a step)
                copyBoard[i] |= to;
                //set casing while you have the array there
                if(i< copyBoard.length/2){
                    casing='l';
                }//else casing is already initialized to 'c'

            }else if(((currentBoard[i]&from)!=0l) && !optionalPromotion.equals("")){
                copyBoard[i] ^= from;
                //REMOVED THE STEP WHERE YOU ADDED THE TOBITBOARD TO -> DO IT AFTERWARDS
                //set casing while you have the array there
                if(i< copyBoard.length/2){
                    casing='l';
                }//else casing is already initialized to 'c'
            }
        }

        //order in array: r, n, b, q, k, p, R, N, B, Q, K, P

        //add the toBitboard to the proper bitboard in copyBoard for promotions
        switch(optionalPromotion){
            case "q":
                if(casing == 'l'){
                    copyBoard[3] |= to; //3 = lc queen
                }else{
                    copyBoard[9] |= to; //9 = c queen
                }break;
            case "b":
                if(casing == 'l'){
                    copyBoard[2] |= to; //3 = lc bishop
                }else{
                    copyBoard[8] |= to; //9 = c bishop
                }break;
            case "n":
                if(casing == 'l'){
                    copyBoard[1] |= to; //3 = lc knight
                }else{
                    copyBoard[7] |= to; //9 = c knight
                }break;
            case "r":
                if(casing == 'l'){
                    copyBoard[0] |= to; //3 = lc rook
                }else{
                    copyBoard[6] |= to; //9 = c rook
                }break;
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

        //Sets states for kings//
        //capital king = position 10
        if((copyBoard[10] & to)!=0l){ //"toBitboard" because it has already been updated to that position by this time!
            capitalKingHasMoved = true;
        }else if((copyBoard[4] & to)!=0l){ //lower case king = position 4
            lowerCaseKingHasMoved = true;
        }
        //Sets states for rooks//
        //capital rooks = position 6
        //lower case rooks = position 0
        //if you previously had a rook on the a file for capital and now you don't, then you moved the a file rook. pogu
        else if(((currentBoard[6] ^ copyBoard[6]) ^ to) == Runner.checkValidConditions.capitalAFileRookStartSquare){ //"toBitboard" xor statement to get rid of the square that said piece was moved to. Isolates the square the rook should start on
            //the piece you moved was the Capital A File Rook
            capitalAFileRookHasMoved = true;
        }else if(((currentBoard[6] ^ copyBoard[6]) ^ to) == Runner.checkValidConditions.capitalHFileRookStartSquare){
            capitalHFileRookHasMoved = true;
        }else if(((currentBoard[0] ^ copyBoard[0]) ^ to) == Runner.checkValidConditions.lowerCaseAFileRookStartSquare){
            lowerCaseAFileRookHasMoved = true;
        }else if(((currentBoard[0] ^ copyBoard[0]) ^ to) == Runner.checkValidConditions.lowerCaseHFileRookStartSquare){
            lowerCaseHFileRookHasMoved = true;
        }

        forceUpdatePosition(copyBoard);
    }

    public boolean moveLeadsToCheck(String fromTo, char pieceInCheck){

        //create new Position with same history to check for check in new position once moved
        Position testingPosition = new Position(currentBoard, currentBoardHistory);
        //move the piece in new object from from to to.
        testingPosition.fromToMove(fromTo);

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

    public boolean moveLeadsToCheck(long from, long to, char pieceInCheck, String optionalPromotion){

        //create new Position with same history to check for check in new position once moved
        Position testingPosition = new Position(currentBoard, currentBoardHistory);
        //move the piece in new object from from to to.
        testingPosition.fromToMove(from, to, optionalPromotion);

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


    //Every time add new member variable, need to update this. Clone method wasn't working, so did it manually.
    public Position getPositionCopy(){
        Long[] newCurrentBoard = getBitboardArrayCopy(currentBoard);
        Long[] newCurrentBoardHistory = getBitboardArrayCopy(currentBoardHistory);
        Position newPosition = new Position(newCurrentBoard, newCurrentBoardHistory);
        newPosition.setCapitalAFileRookHasMoved(capitalAFileRookHasMoved);
        newPosition.setCapitalHFileRookHasMoved(capitalHFileRookHasMoved);
        newPosition.setCapitalKingHasMoved(capitalKingHasMoved);
        newPosition.setCapitalHasCastled(capitalHasCastled);
        newPosition.setLowerCaseAFileRookHasMoved(lowerCaseAFileRookHasMoved);
        newPosition.setLowerCaseHFileRookHasMoved(lowerCaseHFileRookHasMoved);
        newPosition.setLowerCaseKingHasMoved(lowerCaseKingHasMoved);
        newPosition.setLowerCaseHasCastled(lowerCaseHasCastled);
        for (int i = 0; i < movesToCurrent.size(); i++) {
            newPosition.addMove(movesToCurrent.get(i));
        }
        return newPosition;
    }

    private Long[] getBitboardArrayCopy(Long[] bitboardArray){
        Long[] newBitboard = new Long[bitboardArray.length];
        for (int i = 0; i < bitboardArray.length; i++) {
            newBitboard[i] = bitboardArray[i];
        }return newBitboard;
    }

}