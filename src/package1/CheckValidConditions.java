package package1;

public class CheckValidConditions {
    //pawn on rank 2
    public final long rank1, rank2, rank3, rank4, rank5, rank6, rank7, rank8;
    public final long aFile, bFile, gFile, hFile;
    public final long long1;

    public final long longCastleCapitalIntermediateSpaces;
    public final long longCastleCapitalAllInvolvedSpaces;
            //5, 6, 7

    //>>> for right shift
    //<< for left shift because it is automatically unsigned

    CheckValidConditions(){
        //initialize rank longs
        rank1 = Runner.mainBoard.parseLong("0000000000000000000000000000000000000000000000000000000011111111", 2);
        rank2 = Runner.mainBoard.parseLong("0000000000000000000000000000000000000000000000001111111100000000", 2);
        rank3 = Runner.mainBoard.parseLong("0000000000000000000000000000000000000000111111110000000000000000", 2);
        rank4 = Runner.mainBoard.parseLong("0000000000000000000000000000000011111111000000000000000000000000", 2);
        rank5 = Runner.mainBoard.parseLong("0000000000000000000000001111111100000000000000000000000000000000", 2);
        rank6 = Runner.mainBoard.parseLong("0000000000000000111111110000000000000000000000000000000000000000", 2);
        rank7 = Runner.mainBoard.parseLong("0000000011111111000000000000000000000000000000000000000000000000", 2);
        rank8 = Runner.mainBoard.parseLong("1111111100000000000000000000000000000000000000000000000000000000", 2);
        //initialize file longs
        aFile = Runner.mainBoard.parseLong("1000000010000000100000001000000010000000100000001000000010000000", 2);
        bFile = Runner.mainBoard.parseLong("0100000001000000010000000100000001000000010000000100000001000000", 2);
        gFile = Runner.mainBoard.parseLong("0000001000000010000000100000001000000010000000100000001000000010", 2);
        hFile = Runner.mainBoard.parseLong("0000000100000001000000010000000100000001000000010000000100000001", 2);

        long1 = Runner.mainBoard.parseLong("0000000000000000000000000000000000000000000000000000000000000001", 2);

        longCastleCapitalIntermediateSpaces = Runner.mainBoard.parseLong("0000000000000000000000000000000000000000000000000000000001110000", 2);
        longCastleCapitalAllInvolvedSpaces = Runner.mainBoard.parseLong("0000000000000000000000000000000000000000000000000000000011111000", 2);
    }


    //1) Convert Pawns to Use Position Object - Check
    //2) Make Rooks and Kings Accept Castling: utilizing position object - check for position, not for castling!!
    //3) That should be it for these methods!! :)


    //////////////////////////////////////////////POSITION METHODS//////////////////////////////////////////////////////////

//////////USING POSITION OBJECT////////////////////////////////////////////////////////////////////////////////

    //PAWNS - tested
    //- getCapitalEnPassantMoves
    //- getLowerCaseEnPassantMoves
    //- getCapitalPawnCombined (both)
    //- getLowerCasePawnCombined (both)

    //KNIGHTS - tested
    //- getCapitalKnightMoves (both)
    //- getLowerCaseKnightMoves (both)

    //BISHOPS - tested
    //- getCapitalBishopMoves (both)
    //- getLowerCaseBishopMoves (both)

    //QUEENS - tested
    //- getCapitalQueenMoves (both)
    //- getLowerCaseQueenMoves (both)

    //ROOKS / KINGS WITHOUT CASTLING - tested
    //- getCapitalRookMoves (both)
    //- getLowerCaseRookMoves (both)
    //- getCapitalKingMoves
    //- getLowerCaseKingMoves



    //Public Pawn Methods
    ////////Pawn En Passant Moves (Used only in BitboardControlAndSeparation)
    public long getCapitalEnPassantMoves(Position pos){
        Long[] currentBoard = pos.getCurrentBoard();
        Long[] currentBoardHistory = pos.getCurrentBoardHistory();

        long allAttacks = getCapitalPawnAttacksWithoutEnPassant(pos);
        //En Passant  (Lower Case pawns at position 5)// - If my bitboards are all the same and an enemy pawn disappears from my attack, I can still attack that square and
        //remove that pawn through en passant
        long enPassantAttackBitboardAddition = 0l;
        boolean iWasCaptured = false;
        //iterates through capital pieces and checks using xOR statement whether any pieces were changed from the last bitboard to this one. If one was changed,
        //the xOR statement would show greater than 1 and this means a piece of yours was captured making an en passant impossible.
        for (int i = currentBoard.length/2; i < currentBoard.length; i++) {
            if((currentBoard[i]^currentBoardHistory[i])!=0){
                iWasCaptured=true;
            }
        }
        //sets boolean if it moved 2 spaces or not
        boolean moved2 = false;
        //finds the odd pawn out (does technically include starting space as well because of xOR, but doesn't matter) and checks that it is on rank 5 and not on rank6
        long movedToRank5 = ((currentBoard[5]^currentBoardHistory[5])&rank5); //Runner.mainBoard.visualizeBitboard(movedToRank5);
        long movedToRank6 = ((currentBoard[5]^currentBoardHistory[5])&rank6); //Runner.mainBoard.visualizeBitboard(movedToRank6);
        //for some reason have to put &rank6 or the operation doesn't work correctly entirely
        long intermediateSpace = ((currentBoard[5]^currentBoardHistory[5])>>>8)&rank6;
        if((movedToRank5!=0) && (movedToRank6==0)){
            moved2 = true;
        }

        //if you were not captured, an en passant is still possible, but you need to check to make sure that the available attack squares for your pawns
        //has changed. We do this with an xOR statment to check for difference between the current pawn attacking spaces and the ones one turn ago.
        if(moved2 && !iWasCaptured && ((intermediateSpace&getCapitalPawnThreatenedSpaces(pos))!=0)){ //there is an en passant available

            enPassantAttackBitboardAddition |= intermediateSpace;
        }

        //geometrically and with pieces bitboard returned
        return enPassantAttackBitboardAddition;
    }
    public long getLowerCaseEnPassantMoves(Position pos){
        Long[] currentBoard = pos.getCurrentBoard();
        Long[] currentBoardHistory = pos.getCurrentBoardHistory();

        long allAttacks = getLowerCasePawnAttacksWithoutEnPassant(pos);
        //En Passant  (Capital Pawns at Position 11)// - If my bitboards are all the same and an enemy pawn disappears from my attack, I can still attack that square and
        //remove that pawn through en passant
        long enPassantAttackBitboardAddition = 0l;
        boolean iWasCaptured = false;
        //iterates through capital pieces and checks using xOR statement whether any pieces were changed from the last bitboard to this one. If one was changed,
        //the xOR statement would show greater than 1 and this means a piece of yours was captured making an en passant impossible.
        for (int i = 0; i < currentBoard.length/2; i++) {
            if((currentBoard[i]^currentBoardHistory[i])!=0){
                iWasCaptured=true;
            }
        }
        //sets boolean if it moved 2 spaces or not
        boolean moved2 = false;
        //finds the odd pawn out (does technically include starting space as well because of xOR, but doesn't matter) and checks that it is on rank 5 and not on rank6
        long movedToRank5 = ((currentBoard[11]^currentBoardHistory[11])&rank4); //Runner.mainBoard.visualizeBitboard(movedToRank5);
        long movedToRank6 = ((currentBoard[11]^currentBoardHistory[11])&rank3); //Runner.mainBoard.visualizeBitboard(movedToRank6);
        //for some reason have to put &rank6 or the operation doesn't work correctly entirely
        long intermediateSpace = ((currentBoard[11]^currentBoardHistory[11])<<8)&rank3;
        if((movedToRank5!=0) && (movedToRank6==0)){
            moved2 = true;
        }


        //if you were not captured, an en passant is still possible, but you need to check to make sure that the available attack squares for your pawns
        //has changed. We do this with an xOR statment to check for difference between the current pawn attacking spaces and the ones one turn ago.
        if(moved2 && !iWasCaptured && ((intermediateSpace&getLowerCasePawnThreatenedSpaces(pos))!=0)){ //there is an en passant available
            enPassantAttackBitboardAddition |= intermediateSpace;
        }

        //geometrically and with pieces bitboard returned
        return enPassantAttackBitboardAddition;
    }
    ///////////////////////////////////////////////////////////////////////////////
    public long getCapitalPawnCombined(Position pos){
        return getCapitalPawnForwardMoves(pos)|getCapitalPawnAttacks(pos);
    }
    public long getLowerCasePawnCombined(Position pos){
        return getLowerCasePawnDownwardsMoves(pos)|getLowerCasePawnAttacks(pos);
    }
    /////////////////////////////////////////////////////////////////////////
    public long getCapitalPawnCombined(long thisPiece, Position pos){

        if(thisPiece==0l){
            return 0l;
        }

        Long[] currentBoard = pos.getCurrentBoard();
        Long[] currentBoardHistory = pos.getCurrentBoardHistory();

        long allAttacks = getCapitalPawnAttacksWithoutEnPassant(thisPiece, pos);
        //En Passant  (Lower Case pawns at position 5)// - If my bitboards are all the same and an enemy pawn disappears from my attack, I can still attack that square and
        //remove that pawn through en passant
        long enPassantAttackBitboardAddition = 0l;
        boolean iWasCaptured = false;
        //iterates through capital pieces and checks using xOR statement whether any pieces were changed from the last bitboard to this one. If one was changed,
        //the xOR statement would show greater than 1 and this means a piece of yours was captured making an en passant impossible.
        for (int i = currentBoard.length/2; i < currentBoard.length; i++) {
            if((currentBoard[i]^currentBoardHistory[i])!=0){
                iWasCaptured=true;
            }
        }
        //sets boolean if it moved 2 spaces or not
        boolean moved2 = false;
        //finds the odd pawn out (does technically include starting space as well because of xOR, but doesn't matter) and checks that it is on rank 5 and not on rank6
        long movedToRank5 = ((currentBoard[5]^currentBoardHistory[5])&rank5); //Runner.mainBoard.visualizeBitboard(movedToRank5);
        long movedToRank6 = ((currentBoard[5]^currentBoardHistory[5])&rank6); //Runner.mainBoard.visualizeBitboard(movedToRank6);
        //for some reason have to put &rank6 or the operation doesn't work correctly entirely
        long intermediateSpace = ((currentBoard[5]^currentBoardHistory[5])>>>8)&rank6;
        if((movedToRank5!=0) && (movedToRank6==0)){
            moved2 = true;
        }


        //if you were not captured, an en passant is still possible, but you need to check to make sure that the available attack squares for your pawns
        //has changed. We do this with an xOR statment to check for difference between the current pawn attacking spaces and the ones one turn ago.
        if(moved2 && !iWasCaptured && ((intermediateSpace&getCapitalPawnThreatenedSpaces(thisPiece, pos))!=0)){ //there is an en passant available
            enPassantAttackBitboardAddition |= intermediateSpace;
        }

        //geometrically and with pieces bitboard returned
        long attacksTotal = allAttacks|enPassantAttackBitboardAddition;

        ////////////////////////forward moves only/////////////////////////////

        //Position 11//
        long pawnMovesOne = thisPiece<<8;
        //excludes bad moves one move forward
        for (int i = 0; i < currentBoard.length; i++) {
            pawnMovesOne = pawnMovesOne^currentBoard[i]&pawnMovesOne;
        }
        //spaces that work in the fourth file because there are no pieces in the 3rd file before it
        long pawnMovesTwo = ((rank2<<8)&pawnMovesOne)<<8;
        //Excludes spaces in the fourth file with pieces on them
        for (int i = 0; i < currentBoard.length; i++) {
            pawnMovesTwo = pawnMovesTwo^currentBoard[i]&pawnMovesTwo;
        }
        long forwardOnly = pawnMovesOne|pawnMovesTwo;

        return attacksTotal | forwardOnly;

    }
    public long getLowerCasePawnCombined(long thisPiece, Position pos){

        if(thisPiece==0l){
            return 0l;
        }

        Long[] currentBoard = pos.getCurrentBoard();
        Long[] currentBoardHistory = pos.getCurrentBoardHistory();

        long allAttacks = getLowerCasePawnAttacksWithoutEnPassant(thisPiece, pos);
        //En Passant  (Capital Pawns at Position 11)// - If my bitboards are all the same and an enemy pawn disappears from my attack, I can still attack that square and
        //remove that pawn through en passant
        long enPassantAttackBitboardAddition = 0l;
        boolean iWasCaptured = false;
        //iterates through capital pieces and checks using xOR statement whether any pieces were changed from the last bitboard to this one. If one was changed,
        //the xOR statement would show greater than 1 and this means a piece of yours was captured making an en passant impossible.
        for (int i = 0; i < currentBoard.length/2; i++) {
            if((currentBoard[i]^currentBoardHistory[i])!=0){
                iWasCaptured=true;
            }
        }
        //sets boolean if it moved 2 spaces or not
        boolean moved2 = false;
        //finds the odd pawn out (does technically include starting space as well because of xOR, but doesn't matter) and checks that it is on rank 5 and not on rank6
        long movedToRank5 = ((currentBoard[11]^currentBoardHistory[11])&rank4); //Runner.mainBoard.visualizeBitboard(movedToRank5);
        long movedToRank6 = ((currentBoard[11]^currentBoardHistory[11])&rank3); //Runner.mainBoard.visualizeBitboard(movedToRank6);
        //for some reason have to put &rank3 or the operation doesn't work correctly entirely
        long intermediateSpace = ((currentBoard[11]^currentBoardHistory[11])<<8)&rank3;
        if((movedToRank5!=0) && (movedToRank6==0)){
            moved2 = true;
        }


        //if you were not captured, an en passant is still possible, but you need to check to make sure that the available attack squares for your pawns
        //has changed. We do this with an xOR statment to check for difference between the current pawn attacking spaces and the ones one turn ago.
        if(moved2 && !iWasCaptured && ((intermediateSpace&getLowerCasePawnThreatenedSpaces(thisPiece, pos))!=0)){ //there is an en passant available
            enPassantAttackBitboardAddition |= intermediateSpace;
        }

        //geometrically and with pieces bitboard returned
        long attacksTotal =  allAttacks|enPassantAttackBitboardAddition;

    //////////////////////////Forward Moves Only//////////////////////

        //Position 5//
        long pawnMovesOne = thisPiece>>>8;
        //excludes bad moves one move downwards
        for (int i = 0; i < currentBoard.length; i++) {
            pawnMovesOne = pawnMovesOne^currentBoard[i]&pawnMovesOne;
        }
        //spaces that work in the fourth file because there are no pieces in the 3rd file before it
        long pawnMovesTwo = ((rank7>>>8)&pawnMovesOne)>>>8;
        //Excludes spaces in the fourth file with pieces on them
        for (int i = 0; i < currentBoard.length; i++) {
            pawnMovesTwo = pawnMovesTwo^currentBoard[i]&pawnMovesTwo;
        }
        long forwardOnly = pawnMovesOne|pawnMovesTwo;


        return attacksTotal | forwardOnly;

//        Long[] currentBoard = pos.getCurrentBoard();
//
//        //index 5
////        //newBoard is a copy of currentBoard, but with just the moves that that thisPiece can make if it was the only piece of its type on the board
//        Long[] newBoard = Runner.controlAndSeparation.changeBitboardArrayIndex(currentBoard, 5, thisPiece);
//
//        //attacks recalculation
//        //Position 5//
//        //aFile makes sure right pawn still works. Take compliment of a file -> If it is not in the aFile, then it is okay
//        long pawnAttacksRight = (thisPiece>>>7)&(~hFile);
//        //h file makes sure left pawn still works. Same thing as aFile with hFile
//        long pawnAttacksLeft = (thisPiece>>>9)&(~aFile);
//        //bitboard of all places you can move to without taking into account if they have pieces or not
//        long combined = pawnAttacksLeft|pawnAttacksRight;
//        //spaces with pieces on them
//        long withPieces= 0l;
//        //for loop only iterates over capital pieces (See Capital Piece Method for Better Description)
//        for (int i = currentBoard.length/2; i < currentBoard.length; i++) {
//            withPieces = withPieces|currentBoard[i];
//        }
//
//        //geometrically and with pieces bitboard returned
//        long attacks = combined&withPieces;
//
//
//        //Position 5//
//        long pawnMovesOne = thisPiece>>>8;
//        //excludes bad moves one move downwards
//        for (int i = 0; i < currentBoard.length; i++) {
//            pawnMovesOne = pawnMovesOne^currentBoard[i]&pawnMovesOne;
//        }
//        //spaces that work in the fourth file because there are no pieces in the 3rd file before it
//        long pawnMovesTwo = ((rank7>>>8)&pawnMovesOne)>>>8;
//        //Excludes spaces in the fourth file with pieces on them
//        for (int i = 0; i < currentBoard.length; i++) {
//            pawnMovesTwo = pawnMovesTwo^currentBoard[i]&pawnMovesTwo;
//        }
//        long pawnDownwardsCombined = pawnMovesOne|pawnMovesTwo;
//
//
//        return pawnDownwardsCombined | attacks;
    }
    //Private (Supporting) Pawn Methods..
    ////////Pawn Moves Forward///////////////////////////////////
    private long getCapitalPawnForwardMoves(Position pos){
        Long[] currentBoard = pos.getCurrentBoard();
        //Position 11//
        long pawnMovesOne = currentBoard[11]<<8;
        //excludes bad moves one move forward
        for (int i = 0; i < currentBoard.length; i++) {
            pawnMovesOne = pawnMovesOne^currentBoard[i]&pawnMovesOne;
        }
        //spaces that work in the fourth file because there are no pieces in the 3rd file before it
        long pawnMovesTwo = ((rank2<<8)&pawnMovesOne)<<8;
        //Excludes spaces in the fourth file with pieces on them
        for (int i = 0; i < currentBoard.length; i++) {
            pawnMovesTwo = pawnMovesTwo^currentBoard[i]&pawnMovesTwo;
        }
        return pawnMovesOne|pawnMovesTwo;
    }
    private long getLowerCasePawnDownwardsMoves(Position pos){
        Long[] currentBoard = pos.getCurrentBoard();
        //Position 5//
        long pawnMovesOne = currentBoard[5]>>>8;
        //excludes bad moves one move downwards
        for (int i = 0; i < currentBoard.length; i++) {
            pawnMovesOne = pawnMovesOne^currentBoard[i]&pawnMovesOne;
        }
        //spaces that work in the fourth file because there are no pieces in the 3rd file before it
        long pawnMovesTwo = ((rank7>>>8)&pawnMovesOne)>>>8;
        //Excludes spaces in the fourth file with pieces on them
        for (int i = 0; i < currentBoard.length; i++) {
            pawnMovesTwo = pawnMovesTwo^currentBoard[i]&pawnMovesTwo;
        }
        return pawnMovesOne|pawnMovesTwo;
    }
    ////////Pawn Attacks Including En Passant////////////////////////////
    private long getCapitalPawnAttacks(Position pos){
        Long[] currentBoard = pos.getCurrentBoard();
        Long[] currentBoardHistory = pos.getCurrentBoardHistory();

        long allAttacks = getCapitalPawnAttacksWithoutEnPassant(pos);
        //En Passant  (Lower Case pawns at position 5)// - If my bitboards are all the same and an enemy pawn disappears from my attack, I can still attack that square and
        //remove that pawn through en passant
        long enPassantAttackBitboardAddition = 0l;
        boolean iWasCaptured = false;
        //iterates through capital pieces and checks using xOR statement whether any pieces were changed from the last bitboard to this one. If one was changed,
        //the xOR statement would show greater than 1 and this means a piece of yours was captured making an en passant impossible.
        for (int i = currentBoard.length/2; i < currentBoard.length; i++) {
            if((currentBoard[i]^currentBoardHistory[i])!=0){
                iWasCaptured=true;
            }
        }
        //sets boolean if it moved 2 spaces or not
        boolean moved2 = false;
        //finds the odd pawn out (does technically include starting space as well because of xOR, but doesn't matter) and checks that it is on rank 5 and not on rank6
        long movedToRank5 = ((currentBoard[5]^currentBoardHistory[5])&rank5); //Runner.mainBoard.visualizeBitboard(movedToRank5);
        long movedToRank6 = ((currentBoard[5]^currentBoardHistory[5])&rank6); //Runner.mainBoard.visualizeBitboard(movedToRank6);
        //for some reason have to put &rank6 or the operation doesn't work correctly entirely
        long intermediateSpace = ((currentBoard[5]^currentBoardHistory[5])>>>8)&rank6;
        if((movedToRank5!=0) && (movedToRank6==0)){
            moved2 = true;
        }

        //if you were not captured, an en passant is still possible, but you need to check to make sure that the available attack squares for your pawns
        //has changed. We do this with an xOR statment to check for difference between the current pawn attacking spaces and the ones one turn ago.
        if(moved2 && !iWasCaptured && ((intermediateSpace&getCapitalPawnThreatenedSpaces(pos))!=0)){ //there is an en passant available
            enPassantAttackBitboardAddition |= intermediateSpace;
        }

        //geometrically and with pieces bitboard returned
        return allAttacks|enPassantAttackBitboardAddition;
    }
    private long getLowerCasePawnAttacks(Position pos){
        Long[] currentBoard = pos.getCurrentBoard();
        Long[] currentBoardHistory = pos.getCurrentBoardHistory();

        long allAttacks = getLowerCasePawnAttacksWithoutEnPassant(pos);
        //En Passant  (Capital Pawns at Position 11)// - If my bitboards are all the same and an enemy pawn disappears from my attack, I can still attack that square and
        //remove that pawn through en passant
        long enPassantAttackBitboardAddition = 0l;
        boolean iWasCaptured = false;
        //iterates through capital pieces and checks using xOR statement whether any pieces were changed from the last bitboard to this one. If one was changed,
        //the xOR statement would show greater than 1 and this means a piece of yours was captured making an en passant impossible.
        for (int i = 0; i < currentBoard.length/2; i++) {
            if((currentBoard[i]^currentBoardHistory[i])!=0){
                iWasCaptured=true;
            }
        }
        //sets boolean if it moved 2 spaces or not
        boolean moved2 = false;
        //finds the odd pawn out (does technically include starting space as well because of xOR, but doesn't matter) and checks that it is on rank 5 and not on rank6
        long movedToRank5 = ((currentBoard[11]^currentBoardHistory[11])&rank4); //Runner.mainBoard.visualizeBitboard(movedToRank5);
        long movedToRank6 = ((currentBoard[11]^currentBoardHistory[11])&rank3); //Runner.mainBoard.visualizeBitboard(movedToRank6);
        //for some reason have to put &rank3 or the operation doesn't work correctly entirely
        long intermediateSpace = ((currentBoard[11]^currentBoardHistory[11])<<8)&rank3;
        if((movedToRank5!=0) && (movedToRank6==0)){
            moved2 = true;
        }


        //if you were not captured, an en passant is still possible, but you need to check to make sure that the available attack squares for your pawns
        //has changed. We do this with an xOR statment to check for difference between the current pawn attacking spaces and the ones one turn ago.
        if(moved2 && !iWasCaptured && ((intermediateSpace&getLowerCasePawnThreatenedSpaces(pos))!=0)){ //there is an en passant available
            enPassantAttackBitboardAddition |= intermediateSpace;
        }

        //geometrically and with pieces bitboard returned
        return allAttacks|enPassantAttackBitboardAddition;

    }
    ////////Pawn Attacks Excluding En Passant////////////////////////////
    private long getCapitalPawnAttacksWithoutEnPassant(Position pos){
        Long[] currentBoard = pos.getCurrentBoard();
        //Position 11//
        //aFile makes sure right pawn still works. Take compliment of a file -> If it is not in the aFile, then it is okay
        long pawnAttacksRight = (currentBoard[11]<<7)&(~aFile);
        //h file makes sure left pawn still works. Same thing as aFile with hFile
        long pawnAttacksLeft = (currentBoard[11]<<9)&(~hFile);
        //bitboard of all places you can move to without taking into account if they have pieces or not
        long combined = pawnAttacksLeft|pawnAttacksRight;
        //spaces with pieces on them
        long withPieces= 0l;
        //for loop only iterates over lower case pieces because capital can only capture lower case diagonally with pawns
        for (int i = 0; i < currentBoard.length/2; i++) {
            withPieces = withPieces|currentBoard[i];
        }
        return combined&withPieces;
    }
    private long getCapitalPawnAttacksWithoutEnPassant(long thisPiece, Position pos){

        if(thisPiece==0l){
            return 0l;
        }

        Long[] currentBoard = pos.getCurrentBoard();
        //Position 11//
        //aFile makes sure right pawn still works. Take compliment of a file -> If it is not in the aFile, then it is okay
        long pawnAttacksRight = (thisPiece<<7)&(~aFile);
        //h file makes sure left pawn still works. Same thing as aFile with hFile
        long pawnAttacksLeft = (thisPiece<<9)&(~hFile);
        //bitboard of all places you can move to without taking into account if they have pieces or not
        long combined = pawnAttacksLeft|pawnAttacksRight;
        //spaces with pieces on them
        long withPieces= 0l;
        //for loop only iterates over lower case pieces because capital can only capture lower case diagonally with pawns
        for (int i = 0; i < currentBoard.length/2; i++) {
            withPieces = withPieces|currentBoard[i];
        }
        return combined&withPieces;
    }
    private long getLowerCasePawnAttacksWithoutEnPassant(Position pos){
        Long[] currentBoard = pos.getCurrentBoard();
        //Position 5//
        //aFile makes sure right pawn still works. Take compliment of a file -> If it is not in the aFile, then it is okay
        long pawnAttacksRight = (currentBoard[5]>>>7)&(~hFile);
        //h file makes sure left pawn still works. Same thing as aFile with hFile
        long pawnAttacksLeft = (currentBoard[5]>>>9)&(~aFile);
        //bitboard of all places you can move to without taking into account if they have pieces or not
        long combined = pawnAttacksLeft|pawnAttacksRight;
        //spaces with pieces on them
        long withPieces= 0l;
        //for loop only iterates over capital pieces (See Capital Piece Method for Better Description)
        for (int i = currentBoard.length/2; i < currentBoard.length; i++) {
            withPieces = withPieces|currentBoard[i];
        }

        //geometrically and with pieces bitboard returned
        return combined&withPieces;
    }
    private long getLowerCasePawnAttacksWithoutEnPassant(long thisPiece, Position pos){

        if(thisPiece==0l){
            return 0l;
        }

        Long[] currentBoard = pos.getCurrentBoard();
        //Position 5//
        //aFile makes sure right pawn still works. Take compliment of a file -> If it is not in the aFile, then it is okay
        long pawnAttacksRight = (thisPiece>>>7)&(~hFile);
        //h file makes sure left pawn still works. Same thing as aFile with hFile
        long pawnAttacksLeft = (thisPiece>>>9)&(~aFile);
        //bitboard of all places you can move to without taking into account if they have pieces or not
        long combined = pawnAttacksLeft|pawnAttacksRight;
        //spaces with pieces on them
        long withPieces= 0l;
        //for loop only iterates over capital pieces (See Capital Piece Method for Better Description)
        for (int i = currentBoard.length/2; i < currentBoard.length; i++) {
            withPieces = withPieces|currentBoard[i];
        }

        //geometrically and with pieces bitboard returned
        return combined&withPieces;
    }
    //Pawn Threatening Squares// -> En Passant Not Important
    private long getCapitalPawnThreatenedSpaces(Position pos){
        Long[] currentBoard = pos.getCurrentBoard();
        //Position 11//
        //aFile makes sure right pawn still works. Take compliment of a file -> If it is not in the aFile, then it is okay
        long pawnAttacksRight = (currentBoard[11]<<7)&(~aFile);
        //h file makes sure left pawn still works. Same thing as aFile with hFile
        long pawnAttacksLeft = (currentBoard[11]<<9)&(~hFile);
        //bitboard of all places you can move to without taking into account if they have pieces or not
        return pawnAttacksLeft|pawnAttacksRight;
    }
    private long getLowerCasePawnThreatenedSpaces(Position pos){
        Long[] currentBoard = pos.getCurrentBoard();
        //Position 5//
        //aFile makes sure right pawn still works. Take compliment of a file -> If it is not in the aFile, then it is okay
        long pawnAttacksRight = (currentBoard[5]>>>7)&(~hFile);
        //h file makes sure left pawn still works. Same thing as aFile with hFile
        long pawnAttacksLeft = (currentBoard[5]>>>9)&(~aFile);
        //bitboard of all places you can move to without taking into account if they have pieces or not
        return pawnAttacksLeft|pawnAttacksRight;
    }
    private long getCapitalPawnThreatenedSpaces(long thisPiece, Position pos){

        if(thisPiece==0l){
            return 0l;
        }

        //Position 11//
        //aFile makes sure right pawn still works. Take compliment of a file -> If it is not in the aFile, then it is okay
        long pawnAttacksRight = (thisPiece<<7)&(~aFile);
        //h file makes sure left pawn still works. Same thing as aFile with hFile
        long pawnAttacksLeft = (thisPiece<<9)&(~hFile);
        //bitboard of all places you can move to without taking into account if they have pieces or not
        return pawnAttacksLeft|pawnAttacksRight;
    }
    private long getLowerCasePawnThreatenedSpaces(long thisPiece, Position pos){

        if(thisPiece==0l){
            return 0l;
        }

        //Position 5//
        //aFile makes sure right pawn still works. Take compliment of a file -> If it is not in the aFile, then it is okay
        long pawnAttacksRight = (thisPiece>>>7)&(~hFile);
        //h file makes sure left pawn still works. Same thing as aFile with hFile
        long pawnAttacksLeft = (thisPiece>>>9)&(~aFile);
        //bitboard of all places you can move to without taking into account if they have pieces or not
        return pawnAttacksLeft|pawnAttacksRight;
    }





    //Rooks//////////////// -> Castle with King with Board Position History
    public long getCapitalRookMoves(Position pos){

        Long[] currentBoard = pos.getCurrentBoard();

        //Position 6//

        long possibleMoves = 0l;

        long allPieces = 0l;
        long badPieces = 0l;
        long goodPieces = 0l;

        //initializes a bitboard of all pieces
        for (int i = 0; i < currentBoard.length; i++) {
            allPieces = allPieces|currentBoard[i];
        }
        for (int i = 0; i < currentBoard.length/2; i++) {
            badPieces = badPieces|currentBoard[i];
        }
        for (int i = currentBoard.length/2; i < currentBoard.length; i++) {
            goodPieces = goodPieces|currentBoard[i];
        }

        Long[] bitboards = Runner.controlAndSeparation.splitBitboard(currentBoard[6]);
        for (int i = 0; i < bitboards.length; i++) {
            possibleMoves |= getVerticalHorizontalMovesSingle(goodPieces, badPieces, bitboards[i]);
        }


        return possibleMoves;
    }
    public long getCapitalRookMoves(long thisPiece, Position pos){

        if(thisPiece==0l){
            return 0l;
        }

        Long[] currentBoard = pos.getCurrentBoard();

        //position 6//
        long allPieces = 0l;
        long badPieces = 0l;
        long goodPieces = 0l;

        //initializes a bitboard of all pieces
        for (int i = 0; i < currentBoard.length; i++) {
            allPieces = allPieces|currentBoard[i];
        }
        for (int i = 0; i < currentBoard.length/2; i++) {
            badPieces = badPieces|currentBoard[i];
        }
        for (int i = currentBoard.length/2; i < currentBoard.length; i++) {
            goodPieces = goodPieces|currentBoard[i];
        }

        return getVerticalHorizontalMovesSingle(goodPieces, badPieces, thisPiece);
    }

    public long getLowerCaseRookMoves(Position pos){

        Long[] currentBoard = pos.getCurrentBoard();

        //Position 0//

        long possibleMoves = 0l;

        long allPieces = 0l;
        long badPieces = 0l;
        long goodPieces = 0l;

        //initializes a bitboard of all pieces
        for (int i = 0; i < currentBoard.length; i++) {
            allPieces = allPieces|currentBoard[i];
        }
        for (int i = currentBoard.length/2; i < currentBoard.length; i++) {
            badPieces = badPieces|currentBoard[i];
        }
        for (int i = 0; i < currentBoard.length/2; i++) {
            goodPieces = goodPieces|currentBoard[i];
        }

        Long[] bitboards = Runner.controlAndSeparation.splitBitboard(currentBoard[0]);
        for (int i = 0; i < bitboards.length; i++) {
            possibleMoves |= getVerticalHorizontalMovesSingle(goodPieces, badPieces, bitboards[i]);
        }

        return possibleMoves;
    }
    public long getLowerCaseRookMoves(long thisPiece, Position pos){

        if(thisPiece==0l){
            return 0l;
        }

        Long[] currentBoard = pos.getCurrentBoard();

        //position 0//
        long allPieces = 0l;
        long badPieces = 0l;
        long goodPieces = 0l;

        //initializes a bitboard of all pieces
        for (int i = 0; i < currentBoard.length; i++) {
            allPieces = allPieces|currentBoard[i];
        }
        for (int i = currentBoard.length/2; i < currentBoard.length; i++) {
            badPieces = badPieces|currentBoard[i];
        }
        for (int i = 0; i < currentBoard.length/2; i++) {
            goodPieces = goodPieces|currentBoard[i];
        }

        return getVerticalHorizontalMovesSingle(goodPieces, badPieces, thisPiece);

    }
    ///////////////////////


    //King Moves -> Castle with Rook with Board Position History
    public long getCapitalKingMoves(Position pos){

        Long[] currentBoard = pos.getCurrentBoard();

        //position 10//
        long movesetGeneral = 0l;

        movesetGeneral |= (currentBoard[10]<<9 & (~hFile));
        movesetGeneral |= (currentBoard[10]<<1 & (~hFile));
        movesetGeneral |= (currentBoard[10]>>>7 & (~hFile));
        movesetGeneral |= (currentBoard[10]>>>8);
        movesetGeneral |= (currentBoard[10]>>>9 & (~aFile));
        movesetGeneral |= (currentBoard[10]>>>1 & (~aFile));
        movesetGeneral |= (currentBoard[10]<<7 & (~aFile));
        movesetGeneral |= (currentBoard[10]<<8);

        //capital pieces start at index len/2
        for (int i = currentBoard.length/2; i < currentBoard.length; i++) {
            movesetGeneral = movesetGeneral&(~currentBoard[i]);
        }

        return movesetGeneral & (~getAttackingSquaresByCasingNoKing(pos, 'l'));
    }
    public long getLowerCaseKingMoves(Position pos){

        Long[] currentBoard = pos.getCurrentBoard();

        //Position 4//
        long movesetGeneral = 0l;

        movesetGeneral |= (currentBoard[4]<<9 & (~hFile));
        movesetGeneral |= (currentBoard[4]<<1 & (~hFile));
        movesetGeneral |= (currentBoard[4]>>>7 & (~hFile));
        movesetGeneral |= (currentBoard[4]>>>8);
        movesetGeneral |= (currentBoard[4]>>>9 & (~aFile));
        movesetGeneral |= (currentBoard[4]>>>1 & (~aFile));
        movesetGeneral |= (currentBoard[4]<<7 & (~aFile));
        movesetGeneral |= (currentBoard[4]<<8);

        //capital pieces start at index len/2
        for (int i = 0; i < currentBoard.length/2; i++) {
            movesetGeneral = movesetGeneral&(~currentBoard[i]);
        }

        return movesetGeneral & (~getAttackingSquaresByCasingNoKing(pos, 'c'));


    }
    /////////////////////////

    private boolean capitalCanCastleLong(Position pos){
        //if either the AFile(left) rook or the king have moved, return false as this is immediately illegal
        if(pos.getCapitalAFileRookHasMoved() || pos.getCapitalKingHasMoved()){
            return false;
        }else{ //otherwise, if neither have moved it is legal as long as there are...
            //1) no pieces in the way
            //2) no pieces checking either the rook, king, or any of the empty squares in between
            long totalBoard = Runner.controlAndSeparation.condenseBoard(pos.getCurrentBoard());
            if(((totalBoard & longCastleCapitalIntermediateSpaces)==0) && ((getAttackingSquaresByCasing(pos, 'l') & longCastleCapitalAllInvolvedSpaces)==0)){
                //if there are no intermediate pieces and if the enemy is not attacking the origin squares of the rook, king, or any pieces in between
                return true;
            }return false; //if either of those conditions are false, you cannot castle
        }
    }

//////////////////////////////////////////////////////////////////////////////////////////







    //Knight Moves///////////////////////////////////
    public long getCapitalKnightMoves(Position pos){
        Long[] currentBoard = pos.getCurrentBoard();
        //Position 7//
        long moveset = 0l;
        //Files to prevent moves being counted on the other side of the board as they are shifted.
        //Moving from top left counterclockwise
        moveset = moveset|((currentBoard[7]<<17) & (~hFile));
        moveset = moveset|((currentBoard[7]<<10) & (~hFile) & (~gFile));
        moveset = moveset|((currentBoard[7]>>>6) & (~hFile) & (~gFile));
        moveset = moveset|((currentBoard[7]>>>15) & (~hFile));

        moveset = moveset|((currentBoard[7]>>>17) & (~aFile));
        moveset = moveset|((currentBoard[7]>>>10) & (~aFile) & (~bFile));
        moveset = moveset|((currentBoard[7]<<6) & (~aFile) & (~bFile));
        moveset = moveset|((currentBoard[7]<<15) & (~aFile));

        //capital pieces start at index len/2
        for (int i = currentBoard.length/2; i < currentBoard.length; i++) {
            moveset = moveset&(~currentBoard[i]);
        }

        return moveset;
    }
    public long getCapitalKnightMoves(long thisPiece, Position pos){

        if(thisPiece==0l){
            return 0l;
        }

        Long[] currentBoard = pos.getCurrentBoard();
        //position 7//
        return getCapitalKnightMoves(pos) & getCapitalKnightMoves(Runner.controlAndSeparation.changeBitboardArrayIndex(currentBoard, 7, thisPiece));
    }
    //method for getCapitalKnightMoves (Non-Position Parameter)
    private long getCapitalKnightMoves(Long[] currentBoard){
        long moveset = 0l;
        //Files to prevent moves being counted on the other side of the board as they are shifted.
        //Moving from top left counterclockwise
        moveset = moveset|((currentBoard[7]<<17) & (~hFile));
        moveset = moveset|((currentBoard[7]<<10) & (~hFile) & (~gFile));
        moveset = moveset|((currentBoard[7]>>>6) & (~hFile) & (~gFile));
        moveset = moveset|((currentBoard[7]>>>15) & (~hFile));

        moveset = moveset|((currentBoard[7]>>>17) & (~aFile));
        moveset = moveset|((currentBoard[7]>>>10) & (~aFile) & (~bFile));
        moveset = moveset|((currentBoard[7]<<6) & (~aFile) & (~bFile));
        moveset = moveset|((currentBoard[7]<<15) & (~aFile));

        //capital pieces start at index len/2
        for (int i = currentBoard.length/2; i < currentBoard.length; i++) {
            moveset = moveset&(~currentBoard[i]);
        }

        return moveset;
    }

    public long getLowerCaseKnightMoves(Position pos){
        Long[] currentBoard = pos.getCurrentBoard();
        //Position 1//
        long moveset = 0l;
        //Files to prevent moves being counted on the other side of the board as they are shifted.
        //Moving from top left counterclockwise
        moveset = moveset|((currentBoard[1]<<17) & (~hFile));
        moveset = moveset|((currentBoard[1]<<10) & (~hFile) & (~gFile));
        moveset = moveset|((currentBoard[1]>>>6) & (~hFile) & (~gFile));
        moveset = moveset|((currentBoard[1]>>>15) & (~hFile));

        moveset = moveset|((currentBoard[1]>>>17) & (~aFile));
        moveset = moveset|((currentBoard[1]>>>10) & (~aFile) & (~bFile));
        moveset = moveset|((currentBoard[1]<<6) & (~aFile) & (~bFile));
        moveset = moveset|((currentBoard[1]<<15) & (~aFile));

        //Lower case pieces start at 0 and go to len/2
        for (int i = 0; i < currentBoard.length/2; i++) {
            moveset = moveset&(~currentBoard[i]);
        }

        return moveset;
    }
    public long getLowerCaseKnightMoves(long thisPiece, Position pos){

        if(thisPiece==0l){
            return 0l;
        }

        Long[] currentBoard = pos.getCurrentBoard();
        //position 1//
        return getLowerCaseKnightMoves(pos) & getLowerCaseKnightMoves(Runner.controlAndSeparation.changeBitboardArrayIndex(currentBoard, 1, thisPiece));
    }
    //method for getLowerCaseKnightMoves (Non-Position Parameter)
    private long getLowerCaseKnightMoves(Long[] currentBoard){
        //Position 1//
        long moveset = 0l;
        //Files to prevent moves being counted on the other side of the board as they are shifted.
        //Moving from top left counterclockwise
        moveset = moveset|((currentBoard[1]<<17) & (~hFile));
        moveset = moveset|((currentBoard[1]<<10) & (~hFile) & (~gFile));
        moveset = moveset|((currentBoard[1]>>>6) & (~hFile) & (~gFile));
        moveset = moveset|((currentBoard[1]>>>15) & (~hFile));

        moveset = moveset|((currentBoard[1]>>>17) & (~aFile));
        moveset = moveset|((currentBoard[1]>>>10) & (~aFile) & (~bFile));
        moveset = moveset|((currentBoard[1]<<6) & (~aFile) & (~bFile));
        moveset = moveset|((currentBoard[1]<<15) & (~aFile));

        //Lower case pieces start at 0 and go to len/2
        for (int i = 0; i < currentBoard.length/2; i++) {
            moveset = moveset&(~currentBoard[i]);
        }

        return moveset;
    }



    //Bishops///////////////
    public long getCapitalBishopMoves(Position pos){
        Long[] currentBoard = pos.getCurrentBoard();

        //Position 8//

        long possibleMoves = 0l;

        long allPieces = 0l;
        long badPieces = 0l;
        long goodPieces = 0l;

        //initializes a bitboard of all pieces
        for (int i = 0; i < currentBoard.length; i++) {
            allPieces = allPieces|currentBoard[i];
        }
        for (int i = 0; i < currentBoard.length/2; i++) {
            badPieces = badPieces|currentBoard[i];
        }
        for (int i = currentBoard.length/2; i < currentBoard.length; i++) {
            goodPieces = goodPieces|currentBoard[i];
        }

        Long[] bitboards = Runner.controlAndSeparation.splitBitboard(currentBoard[8]);
        for (int i = 0; i < bitboards.length; i++) {
            possibleMoves |= getDiagonalMovesSingle(goodPieces, badPieces, bitboards[i]);
        }


        return possibleMoves;
    }
    public long getCapitalBishopMoves(long thisPiece, Position pos){

        if(thisPiece==0l){
            return 0l;
        }

        Long[] currentBoard = pos.getCurrentBoard();
        //position 8//
        //Position 8//

        long possibleMoves = 0l;

        long allPieces = 0l;
        long badPieces = 0l;
        long goodPieces = 0l;

        //initializes a bitboard of all pieces
        for (int i = 0; i < currentBoard.length; i++) {
            allPieces = allPieces|currentBoard[i];
        }
        for (int i = 0; i < currentBoard.length/2; i++) {
            badPieces = badPieces|currentBoard[i];
        }
        for (int i = currentBoard.length/2; i < currentBoard.length; i++) {
            goodPieces = goodPieces|currentBoard[i];
        }

        return getDiagonalMovesSingle(goodPieces, badPieces, thisPiece);
    }

    public long getLowerCaseBishopMoves(Position pos){

        Long[] currentBoard = pos.getCurrentBoard();

        //Position 2//

        long possibleMoves = 0l;

        long allPieces = 0l;
        long badPieces = 0l;
        long goodPieces = 0l;

        //initializes a bitboard of all pieces
        for (int i = 0; i < currentBoard.length; i++) {
            allPieces = allPieces|currentBoard[i];
        }
        for (int i = currentBoard.length/2; i < currentBoard.length; i++) {
            badPieces = badPieces|currentBoard[i];
        }
        for (int i = 0; i < currentBoard.length/2; i++) {
            goodPieces = goodPieces|currentBoard[i];
        }

        Long[] bitboards = Runner.controlAndSeparation.splitBitboard(currentBoard[2]);
        for (int i = 0; i < bitboards.length; i++) {
            possibleMoves |= getDiagonalMovesSingle(goodPieces, badPieces, bitboards[i]);
        }


        return possibleMoves;
    }
    public long getLowerCaseBishopMoves(long thisPiece, Position pos){

        if(thisPiece==0l){
            return 0l;
        }

        Long[] currentBoard = pos.getCurrentBoard();
        //position 2//
        long allPieces = 0l;
        long badPieces = 0l;
        long goodPieces = 0l;

        //initializes a bitboard of all pieces
        for (int i = 0; i < currentBoard.length; i++) {
            allPieces = allPieces|currentBoard[i];
        }
        for (int i = currentBoard.length/2; i < currentBoard.length; i++) {
            badPieces = badPieces|currentBoard[i];
        }
        for (int i = 0; i < currentBoard.length/2; i++) {
            goodPieces = goodPieces|currentBoard[i];
        }

        return getDiagonalMovesSingle(goodPieces, badPieces, thisPiece);

    }
    /////////////////


    //Queen Moves//////////////////
    public long getCapitalQueenMoves(Position pos){

        Long[] currentBoard = pos.getCurrentBoard();

        //Position 9//

        long possibleMoves = 0l;

        long allPieces = 0l;
        long badPieces = 0l;
        long goodPieces = 0l;

        //initializes a bitboard of all pieces
        for (int i = 0; i < currentBoard.length; i++) {
            allPieces = allPieces|currentBoard[i];
        }
        for (int i = 0; i < currentBoard.length/2; i++) {
            badPieces = badPieces|currentBoard[i];
        }
        for (int i = currentBoard.length/2; i < currentBoard.length; i++) {
            goodPieces = goodPieces|currentBoard[i];
        }

        Long[] bitboards = Runner.controlAndSeparation.splitBitboard(currentBoard[9]);
        for (int i = 0; i < bitboards.length; i++) {
            possibleMoves |= (getDiagonalMovesSingle(goodPieces, badPieces, bitboards[i]) | getVerticalHorizontalMovesSingle(goodPieces, badPieces, bitboards[i]));
        }

        return possibleMoves;

    }
    public long getCapitalQueenMoves(long thisPiece, Position pos){

        if(thisPiece==0l){
            return 0l;
        }

        Long[] currentBoard = pos.getCurrentBoard();

        //position 9//
        long allPieces = 0l;
        long badPieces = 0l;
        long goodPieces = 0l;

        //initializes a bitboard of all pieces
        for (int i = 0; i < currentBoard.length; i++) {
            allPieces = allPieces|currentBoard[i];
        }
        for (int i = 0; i < currentBoard.length/2; i++) {
            badPieces = badPieces|currentBoard[i];
        }
        for (int i = currentBoard.length/2; i < currentBoard.length; i++) {
            goodPieces = goodPieces|currentBoard[i];
        }

        return (getDiagonalMovesSingle(goodPieces, badPieces, thisPiece) | getVerticalHorizontalMovesSingle(goodPieces, badPieces, thisPiece));

    }

    public long getLowerCaseQueenMoves(Position pos){

        Long[] currentBoard = pos.getCurrentBoard();

        //Position 3//

        long possibleMoves = 0l;

        long allPieces = 0l;
        long badPieces = 0l;
        long goodPieces = 0l;

        //initializes a bitboard of all pieces
        for (int i = 0; i < currentBoard.length; i++) {
            allPieces = allPieces|currentBoard[i];
        }
        for (int i = currentBoard.length/2; i < currentBoard.length; i++) {
            badPieces = badPieces|currentBoard[i];
        }
        for (int i = 0; i < currentBoard.length/2; i++) {
            goodPieces = goodPieces|currentBoard[i];
        }

        Long[] bitboards = Runner.controlAndSeparation.splitBitboard(currentBoard[3]);
        for (int i = 0; i < bitboards.length; i++) {
            possibleMoves |= (getDiagonalMovesSingle(goodPieces, badPieces, bitboards[i]) | getVerticalHorizontalMovesSingle(goodPieces, badPieces, bitboards[i]));
        }

        return possibleMoves;
    }
    public long getLowerCaseQueenMoves(long thisPiece, Position pos){

        if(thisPiece==0l){
            return 0l;
        }

        Long[] currentBoard = pos.getCurrentBoard();

        //position 3//
        long allPieces = 0l;
        long badPieces = 0l;
        long goodPieces = 0l;

        //initializes a bitboard of all pieces
        for (int i = 0; i < currentBoard.length; i++) {
            allPieces = allPieces|currentBoard[i];
        }
        for (int i = currentBoard.length/2; i < currentBoard.length; i++) {
            badPieces = badPieces|currentBoard[i];
        }
        for (int i = 0; i < currentBoard.length/2; i++) {
            goodPieces = goodPieces|currentBoard[i];
        }

        return (getDiagonalMovesSingle(goodPieces, badPieces, thisPiece) | getVerticalHorizontalMovesSingle(goodPieces, badPieces, thisPiece));
    }
    ////////////////////////////


    //Diagonal , Horizontal, and Vertical Sliders/////////////////////////
    private long getDiagonalMovesSingle(long goodPieces, long badPieces, long piece){

        long total = 0l;

        //LEFT UP
        //starts off shifted over 9 as to not count home square
        //also don't affect the original rook position for other directions...
        long leftUp = piece << 9;
        for (int i = 0; i < 7; i++) {
            //if it did not loop back around to right side of the board
            if((leftUp&(~hFile))!=0l){
                //if there is a good piece there, break
                if((leftUp&goodPieces)!=0l) {
                    break;
                }
                //if there is a bad piece there, add it to the rook's possible moves, then break
                if((leftUp&badPieces)!=0l){
                    total = total | leftUp;
                    break;
                }
                //if it didn't break, there is no piece there -> add it and keep moving
                total = total | leftUp;
                leftUp = leftUp << 9;
            }else{
                break;
            }
        }
        //RIGHT UP
        //(many comments the same from first section)
        long rightUp = piece << 7;
        for (int i = 0; i < 7; i++) {
            if((rightUp&(~aFile))!=0l){
                //if there is a good piece there, break
                if((rightUp&goodPieces)!=0l) {
                    break;
                }
                //if there is a bad piece there, add it to the rook's possible moves, then break
                if((rightUp&badPieces)!=0l){
                    total = total | rightUp;
                    break;
                }
                //if it didn't break, there is no piece there -> add it and keep moving
                total = total | rightUp;
                rightUp = rightUp << 7;
            }else{
                break;
            }
        }

        //LEFT DOWN
        //(many comments the same from first section)
        long leftDown = piece >>> 7;
        for (int i = 0; i < 7; i++) {
            if((leftDown&(~hFile))!=0l){
                //if there is a good piece there, break
                if((leftDown&goodPieces)!=0l) {
                    break;
                }
                //if there is a bad piece there, add it to the rook's possible moves, then break
                if((leftDown&badPieces)!=0l){
                    total = total | leftDown;
                    break;
                }
                //if it didn't break, there is no piece there -> add it and keep moving
                total = total | leftDown;
                leftDown = leftDown >>> 7;
            }else{
                break;
            }
        }

        //RIGHT DOWN
        //(many comments the same from first section)
        long rightDown = piece >>> 9;
        for (int i = 0; i < 7; i++) {
            if((rightDown&(~aFile))!=0l){
                //if there is a good piece there, break
                if((rightDown&goodPieces)!=0l) {
                    break;
                }
                //if there is a bad piece there, add it to the rook's possible moves, then break
                if((rightDown&badPieces)!=0l){
                    total = total | rightDown;
                    break;
                }
                //if it didn't break, there is no piece there -> add it and keep moving
                total = total | rightDown;
                rightDown = rightDown >>> 9;
            }else{
                break;
            }
        }

        return total;
    }
    private long getVerticalHorizontalMovesSingle(long goodPieces, long badPieces, long piece){

        long total = 0l;

        //LEFT ATTACK
        //start it out shifted one over so it doesn't immediately see itself and stop thinking it is invalid to move there (also doesn't count its home square)
        //also don't affect the original rook position for other directions...
        long rookLeft = piece<<1;
        for (int i = 0; i < 7; i++) {
            //if it did not loop back around to right side of the board
            if((rookLeft&(~hFile))!=0l){
                //if there is a good piece there, break
                if((rookLeft&goodPieces)!=0l) {
                    break;
                }
                //if there is a bad piece there, add it to the rook's possible moves, then break
                if((rookLeft&badPieces)!=0l){
                    total = total | rookLeft;
                    break;
                }
                //if it didn't break, there is no piece there -> add it and keep moving
                total = total | rookLeft;
                rookLeft = rookLeft << 1;
            }else{
                break;
            }
        }
        //RIGHT ATTACK
        //(many comments the same from first section)
        long rookRight = piece >>>1;
        for (int i = 0; i < 7; i++) {
            if((rookRight&(~aFile))!=0l){
                //if there is a good piece there, break
                if((rookRight&goodPieces)!=0l) {
                    break;
                }
                //if there is a bad piece there, add it to the rook's possible moves, then break
                if((rookRight&badPieces)!=0l){
                    total = total | rookRight;
                    break;
                }
                //if it didn't break, there is no piece there -> add it and keep moving
                total = total | rookRight;
                rookRight = rookRight >>> 1;
            }else{
                break;
            }
        }

        //UP ATTACK
        //(many comments the same from first section)
        long rookUp = piece << 8;
        for (int i = 0; i < 7; i++) {

            //if there is a good piece there, break
            if((rookUp&goodPieces)!=0l) {
                break;
            }
            //if there is a bad piece there, add it to the rook's possible moves, then break
            if((rookUp&badPieces)!=0l){
                total = total | rookUp;
                break;
            }
            //if it didn't break, there is no piece there -> add it and keep moving
            total = total | rookUp;
            rookUp = rookUp << 8;

        }

        //DOWN ATTACK
        //(many comments the same from first section)
        long rookDown = piece >>> 8;
        for (int i = 0; i < 7; i++) {
            //if there is a good piece there, break
            if((rookDown&goodPieces)!=0l) {
                break;
            }
            //if there is a bad piece there, add it to the rook's possible moves, then break
            if((rookDown&badPieces)!=0l){
                total = total | rookDown;
                break;
            }
            //if it didn't break, there is no piece there -> add it and keep moving
            total = total | rookDown;
            rookDown = rookDown >>> 8;
        }
        return total;
    }
    /////////////////////////////


    //Attacking Squares of Casing ('c' = capital piece attacks, 'l' = lower case piece attacks)//
    public long getAttackingSquaresByCasing(Position pos, char casing){
        long attackingSquares = 0l;
        //order in array: r, n, b, q, k, p, R, N, B, Q, K, P
        if(casing=='l'){
            //capital
            attackingSquares |= getLowerCaseRookMoves(pos);
            attackingSquares |= getLowerCaseKnightMoves(pos);
            attackingSquares |= getLowerCaseBishopMoves(pos);
            attackingSquares |= getLowerCaseQueenMoves(pos);
            attackingSquares |= getLowerCaseKingMoves(pos);
            attackingSquares |= getLowerCasePawnThreatenedSpaces(pos);
        }else if(casing=='c'){
            //capital
            attackingSquares |= getCapitalRookMoves(pos);
            attackingSquares |= getCapitalKnightMoves(pos);
            attackingSquares |= getCapitalBishopMoves(pos);
            attackingSquares |= getCapitalQueenMoves(pos);
            attackingSquares |= getCapitalKingMoves(pos);
            attackingSquares |= getCapitalPawnThreatenedSpaces(pos);
        }else{
            System.out.println("Fatal: Error in Get Attacking Squares By Casing. Invalid Character.");
        }

        return attackingSquares;
    }
    public long getAttackingSquaresByCasingNoKing(Position pos, char casing){
        long attackingSquares = 0l;
        //order in array: r, n, b, q, k, p, R, N, B, Q, K, P
        if(casing=='l'){
            //capital
            attackingSquares |= getLowerCaseRookMoves(pos);
            attackingSquares |= getLowerCaseKnightMoves(pos);
            attackingSquares |= getLowerCaseBishopMoves(pos);
            attackingSquares |= getLowerCaseQueenMoves(pos);
            attackingSquares |= getLowerCasePawnThreatenedSpaces(pos);
        }else if(casing=='c'){
            //lower case
            attackingSquares |= getCapitalRookMoves(pos);
            attackingSquares |= getCapitalKnightMoves(pos);
            attackingSquares |= getCapitalBishopMoves(pos);
            attackingSquares |= getCapitalQueenMoves(pos);
            attackingSquares |= getCapitalPawnThreatenedSpaces(pos);
        }else{
            System.out.println("Fatal: Error in Get Attacking Squares By Casing. Invalid Character.");
        }

        return attackingSquares;
    }






    ///////////////////////////////////////////END POSITION METHODS/////////////////////////////////////////////////////////////




    ////////Pawn Moves Combined Without En Passant///////////////////////////////////
//    public long getCapitalPawnCombined(Long[] currentBoard){
//        return getCapitalPawnForwardMoves(currentBoard)|getCapitalPawnAttacksWithoutEnPassant(currentBoard);
//    }
//    public long getCapitalPawnCombined(long thisPiece, Long[] currentBoard){
//        //index 11
////        //newBoard is a copy of currentBoard, but with just the moves that that thisPiece can make if it was the only piece of its type on the board
//        Long[] newBoard = Runner.controlAndSeparation.changeBitboardArrayIndex(currentBoard, 11, thisPiece);
//
//        //attacks substituted with thisPiece
//
//        //Position 11//
//        //aFile makes sure right pawn still works. Take compliment of a file -> If it is not in the aFile, then it is okay
//        long pawnAttacksRight = (thisPiece<<7)&(~aFile);
//        //h file makes sure left pawn still works. Same thing as aFile with hFile
//        long pawnAttacksLeft = (thisPiece<<9)&(~hFile);
//        //bitboard of all places you can move to without taking into account if they have pieces or not
//        long combined = pawnAttacksLeft|pawnAttacksRight;
//        //spaces with pieces on them
//        long withPieces= 0l;
//        //for loop only iterates over lower case pieces because capital can only capture lower case diagonally with pawns
//        for (int i = 0; i < currentBoard.length/2; i++) {
//            withPieces = withPieces|currentBoard[i];
//        }
//        long attacks = combined&withPieces;
//
//        //moves need to be recalculated because can move twice forward -> (!!Substitute the array of pawns for just the one pawn thisPiece bitboard!!)
//        long pawnMovesOne = thisPiece<<8;
//        //excludes bad moves one move forward
//        for (int i = 0; i < currentBoard.length; i++) {
//            pawnMovesOne = pawnMovesOne^currentBoard[i]&pawnMovesOne;
//        }
//        //spaces that work in the fourth file because there are no pieces in the 3rd file before it
//        long pawnMovesTwo = ((rank2<<8)&pawnMovesOne)<<8;
//        //Excludes spaces in the fourth file with pieces on them
//        for (int i = 0; i < currentBoard.length; i++) {
//            pawnMovesTwo = pawnMovesTwo^currentBoard[i]&pawnMovesTwo;
//        }
//        long pawnForwardCombined = pawnMovesOne|pawnMovesTwo;
//
//
//        return pawnForwardCombined | attacks;
//    }
//    public long getLowerCasePawnCombined(Long[] currentBoard){
//        return getLowerCasePawnDownwardsMoves(currentBoard)|getLowerCasePawnAttacksWithoutEnPassant(currentBoard);
//    }
//    public long getLowerCasePawnCombined(long thisPiece, Long[] currentBoard){
//        //index 5
////        //newBoard is a copy of currentBoard, but with just the moves that that thisPiece can make if it was the only piece of its type on the board
//        Long[] newBoard = Runner.controlAndSeparation.changeBitboardArrayIndex(currentBoard, 5, thisPiece);
//
//        //attacks recalculation
//        //Position 5//
//        //aFile makes sure right pawn still works. Take compliment of a file -> If it is not in the aFile, then it is okay
//        long pawnAttacksRight = (thisPiece>>>7)&(~hFile);
//        //h file makes sure left pawn still works. Same thing as aFile with hFile
//        long pawnAttacksLeft = (thisPiece>>>9)&(~aFile);
//        //bitboard of all places you can move to without taking into account if they have pieces or not
//        long combined = pawnAttacksLeft|pawnAttacksRight;
//        //spaces with pieces on them
//        long withPieces= 0l;
//        //for loop only iterates over capital pieces (See Capital Piece Method for Better Description)
//        for (int i = currentBoard.length/2; i < currentBoard.length; i++) {
//            withPieces = withPieces|currentBoard[i];
//        }
//
//        //geometrically and with pieces bitboard returned
//        long attacks = combined&withPieces;
//
//
//        //Position 5//
//        long pawnMovesOne = thisPiece>>>8;
//        //excludes bad moves one move downwards
//        for (int i = 0; i < currentBoard.length; i++) {
//            pawnMovesOne = pawnMovesOne^currentBoard[i]&pawnMovesOne;
//        }
//        //spaces that work in the fourth file because there are no pieces in the 3rd file before it
//        long pawnMovesTwo = ((rank7>>>8)&pawnMovesOne)>>>8;
//        //Excludes spaces in the fourth file with pieces on them
//        for (int i = 0; i < currentBoard.length; i++) {
//            pawnMovesTwo = pawnMovesTwo^currentBoard[i]&pawnMovesTwo;
//        }
//        long pawnDownwardsCombined = pawnMovesOne|pawnMovesTwo;
//
//
//        return pawnDownwardsCombined | attacks;
//    }


//
//    //Knight Attacks Special//
//    public long getCapitalKnightAttacks(Long[] currentBoard){
//        long possibleMoves = getCapitalKnightMoves(currentBoard);
//        long withOppositePieces = 0l;
//
//        //lower case pieces (pieces you attack) go from 0 to len/2
//        for (int i = 0; i < currentBoard.length/2; i++) {
//            withOppositePieces = withOppositePieces|currentBoard[i];
//        }
//
//        return possibleMoves&withOppositePieces;
//    }
//    public long getLowerCaseKnightAttacks(Long[] currentBoard){
//        long possibleMoves = getLowerCaseKnightMoves(currentBoard);
//        long withOppositePieces = 0l;
//
//        //capital pieces (pieces you attack) go from len/2 to len
//        for (int i = currentBoard.length/2; i < currentBoard.length; i++) {
//            withOppositePieces = withOppositePieces|currentBoard[i];
//        }
//
//        return possibleMoves&withOppositePieces;
//    }
//    /////////////////////////
//
//
//
//
//
//
//

    //Pawns (Threatening Squares - Done, Possible Squares To Move To/Attack - Including En Passant)

    ////////Pawn Moves Combined With En Passant///////////////////////////////////
//    public long getCapitalPawnCombinedWithEnPassant(Long[] currentBoard, Long[] currentBoardHistory){
//        return getCapitalPawnForwardMoves(currentBoard)|getCapitalPawnAttacks(currentBoard, currentBoardHistory);
//    }
//    public long getLowerCasePawnCombinedWithEnPassant(Long[] currentBoard, Long[] currentBoardHistory){
//        return getLowerCasePawnDownwardsMoves(currentBoard)|getLowerCasePawnAttacks(currentBoard, currentBoardHistory);
//    }

    //Supporting Methods... Pawns Combined With EnPassant//


    ////////Pawn Attacks Including En Passant////////////////////////////
//    private long getCapitalPawnAttacks(Long[] currentBoard, Long[] currentBoardHistory){
//
//            long allAttacks = getCapitalPawnAttacksWithoutEnPassant(currentBoard);
//            //En Passant  (Lower Case pawns at position 5)// - If my bitboards are all the same and an enemy pawn disappears from my attack, I can still attack that square and
//            //remove that pawn through en passant
//            long enPassantAttackBitboardAddition = 0l;
//            boolean iWasCaptured = false;
//            //iterates through capital pieces and checks using xOR statement whether any pieces were changed from the last bitboard to this one. If one was changed,
//            //the xOR statement would show greater than 1 and this means a piece of yours was captured making an en passant impossible.
//            for (int i = currentBoard.length/2; i < currentBoard.length; i++) {
//                if((currentBoard[i]^currentBoardHistory[i])!=0){
//                    iWasCaptured=true;
//                }
//            }
//            //sets boolean if it moved 2 spaces or not
//            boolean moved2 = false;
//            //finds the odd pawn out (does technically include starting space as well because of xOR, but doesn't matter) and checks that it is on rank 5 and not on rank6
//            long movedToRank5 = ((currentBoard[5]^currentBoardHistory[5])&rank5); //Runner.mainBoard.visualizeBitboard(movedToRank5);
//            long movedToRank6 = ((currentBoard[5]^currentBoardHistory[5])&rank6); //Runner.mainBoard.visualizeBitboard(movedToRank6);
//            //for some reason have to put &rank6 or the operation doesn't work correctly entirely
//            long intermediateSpace = ((currentBoard[5]^currentBoardHistory[5])>>>8)&rank6;
//            if((movedToRank5!=0) && (movedToRank6==0)){
//                moved2 = true;
//            }
//            System.out.println(moved2);
//
//            //if you were not captured, an en passant is still possible, but you need to check to make sure that the available attack squares for your pawns
//            //has changed. We do this with an xOR statment to check for difference between the current pawn attacking spaces and the ones one turn ago.
//            if(moved2 && !iWasCaptured && ((intermediateSpace&getCapitalPawnThreatenedSpaces(currentBoard))!=0)){ //there is an en passant available
//                enPassantAttackBitboardAddition |= intermediateSpace;
//            }
//
//            //geometrically and with pieces bitboard returned
//            return allAttacks|enPassantAttackBitboardAddition;
//        }
//    private long getLowerCasePawnAttacks(Long[] currentBoard, Long[] currentBoardHistory){
//        long allAttacks = getLowerCasePawnAttacksWithoutEnPassant(currentBoard);
//        //En Passant  (Capital Pawns at Position 11)// - If my bitboards are all the same and an enemy pawn disappears from my attack, I can still attack that square and
//        //remove that pawn through en passant
//        long enPassantAttackBitboardAddition = 0l;
//        boolean iWasCaptured = false;
//        //iterates through capital pieces and checks using xOR statement whether any pieces were changed from the last bitboard to this one. If one was changed,
//        //the xOR statement would show greater than 1 and this means a piece of yours was captured making an en passant impossible.
//        for (int i = 0; i < currentBoard.length/2; i++) {
//            if((currentBoard[i]^currentBoardHistory[i])!=0){
//                iWasCaptured=true;
//            }
//        }
//        //sets boolean if it moved 2 spaces or not
//        boolean moved2 = false;
//        //finds the odd pawn out (does technically include starting space as well because of xOR, but doesn't matter) and checks that it is on rank 5 and not on rank6
//        long movedToRank5 = ((currentBoard[11]^currentBoardHistory[11])&rank4); //Runner.mainBoard.visualizeBitboard(movedToRank5);
//        long movedToRank6 = ((currentBoard[11]^currentBoardHistory[11])&rank3); //Runner.mainBoard.visualizeBitboard(movedToRank6);
//        //for some reason have to put &rank3 or the operation doesn't work correctly entirely
//        long intermediateSpace = ((currentBoard[11]^currentBoardHistory[11])<<8)&rank3;
//        if((movedToRank5!=0) && (movedToRank6==0)){
//            moved2 = true;
//        }
//        System.out.println(moved2);
//
//        //if you were not captured, an en passant is still possible, but you need to check to make sure that the available attack squares for your pawns
//        //has changed. We do this with an xOR statment to check for difference between the current pawn attacking spaces and the ones one turn ago.
//        if(moved2 && !iWasCaptured && ((intermediateSpace&getLowerCasePawnThreatenedSpaces(currentBoard))!=0)){ //there is an en passant available
//            enPassantAttackBitboardAddition |= intermediateSpace;
//        }
//
//        //geometrically and with pieces bitboard returned
//        return allAttacks|enPassantAttackBitboardAddition;
//
//    }

    ////////Pawn Attacks Excluding En Passant////////////////////////////
//    private long getCapitalPawnAttacksWithoutEnPassant(Long currentBoard[]){
//            //Position 11//
//            //aFile makes sure right pawn still works. Take compliment of a file -> If it is not in the aFile, then it is okay
//            long pawnAttacksRight = (currentBoard[11]<<7)&(~aFile);
//            //h file makes sure left pawn still works. Same thing as aFile with hFile
//            long pawnAttacksLeft = (currentBoard[11]<<9)&(~hFile);
//            //bitboard of all places you can move to without taking into account if they have pieces or not
//            long combined = pawnAttacksLeft|pawnAttacksRight;
//            //spaces with pieces on them
//            long withPieces= 0l;
//            //for loop only iterates over lower case pieces because capital can only capture lower case diagonally with pawns
//            for (int i = 0; i < currentBoard.length/2; i++) {
//                withPieces = withPieces|currentBoard[i];
//            }
//            return combined&withPieces;
//        }
//    private long getLowerCasePawnAttacksWithoutEnPassant(Long[] currentBoard){
//        //Position 5//
//        //aFile makes sure right pawn still works. Take compliment of a file -> If it is not in the aFile, then it is okay
//        long pawnAttacksRight = (currentBoard[5]>>>7)&(~hFile);
//        //h file makes sure left pawn still works. Same thing as aFile with hFile
//        long pawnAttacksLeft = (currentBoard[5]>>>9)&(~aFile);
//        //bitboard of all places you can move to without taking into account if they have pieces or not
//        long combined = pawnAttacksLeft|pawnAttacksRight;
//        //spaces with pieces on them
//        long withPieces= 0l;
//        //for loop only iterates over capital pieces (See Capital Piece Method for Better Description)
//        for (int i = currentBoard.length/2; i < currentBoard.length; i++) {
//            withPieces = withPieces|currentBoard[i];
//        }
//
//        //geometrically and with pieces bitboard returned
//        return combined&withPieces;
//    }

    ////////Pawn En Passant Moves (Used only in BitboardControlAndSeparation) (May remove in favor of position object later within BitboardControlAndSeparation)////////////////////////////
//    public long getCapitalEnPassantMoves(Long[] currentBoard, Long[] currentBoardHistory){
//
//        long allAttacks = getCapitalPawnAttacksWithoutEnPassant(currentBoard);
//        //En Passant  (Lower Case pawns at position 5)// - If my bitboards are all the same and an enemy pawn disappears from my attack, I can still attack that square and
//        //remove that pawn through en passant
//        long enPassantAttackBitboardAddition = 0l;
//        boolean iWasCaptured = false;
//        //iterates through capital pieces and checks using xOR statement whether any pieces were changed from the last bitboard to this one. If one was changed,
//        //the xOR statement would show greater than 1 and this means a piece of yours was captured making an en passant impossible.
//        for (int i = currentBoard.length/2; i < currentBoard.length; i++) {
//            if((currentBoard[i]^currentBoardHistory[i])!=0){
//                iWasCaptured=true;
//            }
//        }
//        //sets boolean if it moved 2 spaces or not
//        boolean moved2 = false;
//        //finds the odd pawn out (does technically include starting space as well because of xOR, but doesn't matter) and checks that it is on rank 5 and not on rank6
//        long movedToRank5 = ((currentBoard[5]^currentBoardHistory[5])&rank5); //Runner.mainBoard.visualizeBitboard(movedToRank5);
//        long movedToRank6 = ((currentBoard[5]^currentBoardHistory[5])&rank6); //Runner.mainBoard.visualizeBitboard(movedToRank6);
//        //for some reason have to put &rank6 or the operation doesn't work correctly entirely
//        long intermediateSpace = ((currentBoard[5]^currentBoardHistory[5])>>>8)&rank6;
//        if((movedToRank5!=0) && (movedToRank6==0)){
//            moved2 = true;
//        }
//
//        //if you were not captured, an en passant is still possible, but you need to check to make sure that the available attack squares for your pawns
//        //has changed. We do this with an xOR statment to check for difference between the current pawn attacking spaces and the ones one turn ago.
//        if(moved2 && !iWasCaptured && ((intermediateSpace&getCapitalPawnThreatenedSpaces(currentBoard))!=0)){ //there is an en passant available
//
//            enPassantAttackBitboardAddition |= intermediateSpace;
//        }
//
//        //geometrically and with pieces bitboard returned
//        return enPassantAttackBitboardAddition;
//    }
//    public long getLowerCaseEnPassantMoves(Long[] currentBoard, Long[] currentBoardHistory){
//        long allAttacks = getLowerCasePawnAttacksWithoutEnPassant(currentBoard);
//        //En Passant  (Capital Pawns at Position 11)// - If my bitboards are all the same and an enemy pawn disappears from my attack, I can still attack that square and
//        //remove that pawn through en passant
//        long enPassantAttackBitboardAddition = 0l;
//        boolean iWasCaptured = false;
//        //iterates through capital pieces and checks using xOR statement whether any pieces were changed from the last bitboard to this one. If one was changed,
//        //the xOR statement would show greater than 1 and this means a piece of yours was captured making an en passant impossible.
//        for (int i = 0; i < currentBoard.length/2; i++) {
//            if((currentBoard[i]^currentBoardHistory[i])!=0){
//                iWasCaptured=true;
//            }
//        }
//        //sets boolean if it moved 2 spaces or not
//        boolean moved2 = false;
//        //finds the odd pawn out (does technically include starting space as well because of xOR, but doesn't matter) and checks that it is on rank 5 and not on rank6
//        long movedToRank5 = ((currentBoard[11]^currentBoardHistory[11])&rank4); //Runner.mainBoard.visualizeBitboard(movedToRank5);
//        long movedToRank6 = ((currentBoard[11]^currentBoardHistory[11])&rank3); //Runner.mainBoard.visualizeBitboard(movedToRank6);
//        //for some reason have to put &rank6 or the operation doesn't work correctly entirely
//        long intermediateSpace = ((currentBoard[11]^currentBoardHistory[11])<<8)&rank3;
//        if((movedToRank5!=0) && (movedToRank6==0)){
//            moved2 = true;
//        }
//        System.out.println(moved2);
//
//        //if you were not captured, an en passant is still possible, but you need to check to make sure that the available attack squares for your pawns
//        //has changed. We do this with an xOR statment to check for difference between the current pawn attacking spaces and the ones one turn ago.
//        if(moved2 && !iWasCaptured && ((intermediateSpace&getLowerCasePawnThreatenedSpaces(currentBoard))!=0)){ //there is an en passant available
//            enPassantAttackBitboardAddition |= intermediateSpace;
//        }
//
//        //geometrically and with pieces bitboard returned
//        return enPassantAttackBitboardAddition;
//    }
    ///////////////////////////////////////////////////////////////////////////////





}