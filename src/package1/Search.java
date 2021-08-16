package package1;

import java.util.ArrayList;

public class Search {

    // returns 1 for capital wins by checkmate, -1 for lowercase win, and 0 for neither
    public int boardIsInCheckmate(Position pos){
        if(capitalIsInCheckmate(pos)){ // lc wins
            return -1;
        }else if(lowerCaseIsInCheckmate(pos)){ // cap wins
            return 1;
        }else{ // neither wins
            return 0;
        }
    }



    /////////////////Capital///////////////////

    public boolean capitalIsInCheckmate(Position pos){
        if(allCapitalMovesEqualCheck(pos)&&capitalIsInCheck(pos)){
            return true;
        }return false;
    }
    public boolean capitalIsInStalemate(Position pos){
        if(allCapitalMovesEqualCheck(pos) && !capitalIsInCheck(pos)){
            return true;
        }return false;
    }
    public boolean allCapitalMovesEqualCheck(Position pos){
        //Position 10// (Capital King)
        //
        Long[][] currentBoard = Runner.controlAndSeparation.splitBitboardArray(pos.getCurrentBoard());

        //cycle through every CAPITAL piece.
        for (int i = currentBoard.length/2; i < currentBoard.length; i++) {
            for (int j = 0; j < currentBoard[i].length; j++) {
                // get all of its possible moves.
                long possibleMoves = 0l;
                switch(i){
                    case 6:
                        possibleMoves = Runner.checkValidConditions.getCapitalRookMoves(currentBoard[i][j], pos);
                        break;
                    case 7:
                        possibleMoves = Runner.checkValidConditions.getCapitalKnightMoves(currentBoard[i][j], pos);
                        break;
                    case 8:
                        possibleMoves = Runner.checkValidConditions.getCapitalBishopMoves(currentBoard[i][j], pos);
                        break;
                    case 9:
                        possibleMoves = Runner.checkValidConditions.getCapitalQueenMoves(currentBoard[i][j], pos);
                        break;
                    case 10:
                        possibleMoves = Runner.checkValidConditions.getCapitalKingMoves(pos);
                        break;
                    case 11:
                        possibleMoves = Runner.checkValidConditions.getCapitalPawnCombined(currentBoard[i][j], pos);
                        break;
                }
                // for each possible move, make a new array of single move bitboards.
                Long[] singleMoveBitboards = Runner.controlAndSeparation.splitBitboard(possibleMoves);

                //loop through all of the single moves in singleMoveBitboards and use the Move function to move them, and set the board equal to a new board array.
                for (int k = 0; k < singleMoveBitboards.length; k++) {
                    if(!pos.moveLeadsToCheck(currentBoard[i][j], singleMoveBitboards[k], 'c', "")){
                        return false;
                    }
                }
            }
        }
        //returns true if the king has nowhere to go. Like this because can be used to calculate stalemates if it is in "checkmate" and not in check
        return true;
    }
    ////////////////////////////////////////////////

    public boolean lowerCaseIsInCheckmate(Position pos){
        if(allLowerCaseMovesEqualCheck(pos)&&lowerCaseIsInCheck(pos)){
            return true;
        }return false;
    }
    public boolean lowerCaseIsInStalemate(Position pos){
        if(allLowerCaseMovesEqualCheck(pos) && !lowerCaseIsInCheck(pos)){
            return true;
        }return false;
    }
    public boolean allLowerCaseMovesEqualCheck(Position pos){
        //Position 5// (Lower Case King)

        Long[][] currentBoard = Runner.controlAndSeparation.splitBitboardArray(pos.getCurrentBoard());

        //order in array: r, n, b, q, k, p, R, N, B, Q, K, P

        //cycle through every CAPITAL piece.
        for (int i = 0; i < currentBoard.length/2; i++) {
            for (int j = 0; j < currentBoard[i].length; j++) {
                // get all of its possible moves.
                long possibleMoves = 0l;
                switch(i){
                    case 0:
                        possibleMoves = Runner.checkValidConditions.getLowerCaseRookMoves(currentBoard[i][j], pos);
                        break;
                    case 1:
                        possibleMoves = Runner.checkValidConditions.getLowerCaseKnightMoves(currentBoard[i][j], pos);
                        break;
                    case 2:
                        possibleMoves = Runner.checkValidConditions.getLowerCaseBishopMoves(currentBoard[i][j], pos);
                        break;
                    case 3:
                        possibleMoves = Runner.checkValidConditions.getLowerCaseQueenMoves(currentBoard[i][j], pos);
                        break;
                    case 4:
                        possibleMoves = Runner.checkValidConditions.getLowerCaseKingMoves(pos);
                        break;
                    case 5:
                        possibleMoves = Runner.checkValidConditions.getLowerCasePawnCombined(currentBoard[i][j], pos);
                        break;
                }
                // for each possible move, make a new array of single move bitboards.
                Long[] singleMoveBitboards = Runner.controlAndSeparation.splitBitboard(possibleMoves);
                //loop through all of the single moves in singleMoveBitboards and use the Move function to move them, and set the board equal to a new board array.
                for (int k = 0; k < singleMoveBitboards.length; k++) {
                    if(!pos.moveLeadsToCheck(currentBoard[i][j], singleMoveBitboards[k], 'l', "")){
                        return false;
                    }
                }
            }
        }
        //return true if for every one of Lower Case's possible moves, the king is in check
        return true;
    }

    //Checking Methods///////////////////
    public boolean capitalIsInCheck(Position pos){

        Long[] currentBoard = pos.getCurrentBoard();

        //Position 10// (Capital King)
        long lowerCaseAttacks = Runner.checkValidConditions.getAttackingSquaresByCasing(pos, 'l');
        if((lowerCaseAttacks&currentBoard[10])!=0){
            return true;
        }
        return false;
    }
    public boolean lowerCaseIsInCheck(Position pos){

        Long[] currentBoard = pos.getCurrentBoard();

        //Position 4// (Lower Case King)
        long lowerCaseAttacks = Runner.checkValidConditions.getAttackingSquaresByCasing(pos, 'c');
        if((lowerCaseAttacks&currentBoard[4])!=0){
            return true;
        }
        return false;
    }

    //returns the possible moves for a casing given a position object in algebraic notation
    public String[] getPossibleMovesByCasing(Position pos, char casing){
        Long[][] currentBoard = Runner.controlAndSeparation.splitBitboardArray(pos.getCurrentBoard());
        ArrayList<String> possibleMovesList = new ArrayList<>();
        //order in array: r, n, b, q, k, p, R, N, B, Q, K, P

        //capital vs lower case
        if(casing == 'c') {
            for (int i = currentBoard.length / 2; i < currentBoard.length; i++) {
                for (int j = 0; j < currentBoard[i].length; j++) {
                    //only have to evaluate the piece string once now
                    String thisPieceString = Runner.controlAndSeparation.singleBitBitboardToString(currentBoard[i][j]);
                    // get all of its possible moves.
                    long possibleMoves = 0l;
                    switch (i) {
                        case 6:
                            possibleMoves = Runner.checkValidConditions.getCapitalRookMoves(currentBoard[i][j], pos);
                            break;
                        case 7:
                            possibleMoves = Runner.checkValidConditions.getCapitalKnightMoves(currentBoard[i][j], pos);
                            break;
                        case 8:
                            possibleMoves = Runner.checkValidConditions.getCapitalBishopMoves(currentBoard[i][j], pos);
                            break;
                        case 9:
                            possibleMoves = Runner.checkValidConditions.getCapitalQueenMoves(currentBoard[i][j], pos);
                            break;
                        case 10:
                            possibleMoves = Runner.checkValidConditions.getCapitalKingMoves(pos);
                            break;
                        case 11:
                            possibleMoves = Runner.checkValidConditions.getCapitalPawnCombined(currentBoard[i][j], pos);
                            break;
                    }
                    // for each possible moveset of a piece, make a new array of single move bitboards for that piece
                    Long[] singleMoveBitboards = Runner.controlAndSeparation.splitBitboard(possibleMoves);
                    for (int k = 0; k < singleMoveBitboards.length; k++) {
                        boolean canPromote = false;
                        //if there is a capital rook on the 8th rank, it can promote
                        if(((singleMoveBitboards[k] & Runner.checkValidConditions.rank8)!=0) && i==11){ //and it is a capital pawn (i==11)
                            canPromote = true;
                        }
                        //if the move does not lead to check for yourself
                        if(!pos.moveLeadsToCheck(currentBoard[i][j], singleMoveBitboards[k], 'c', "")){ //keep this with no optional promotion because it cannot prevent check or checkmate within one move... saves work
                            //turns the square the piece is on and the square it moves to into algebraic notation to add to the movelist
                            if(canPromote){ //if you can promote, it adds the promotions for queens and rooks as possibilities for promotion
                                possibleMovesList.add(thisPieceString + Runner.controlAndSeparation.singleBitBitboardToString(singleMoveBitboards[k])+"q");
                                possibleMovesList.add(thisPieceString + Runner.controlAndSeparation.singleBitBitboardToString(singleMoveBitboards[k])+"r");
                            }else {//otherwise, no promotion for you
                                possibleMovesList.add(thisPieceString + Runner.controlAndSeparation.singleBitBitboardToString(singleMoveBitboards[k]));
                            }
                        }
                    }
                }
            }
        }else if(casing == 'l'){
            for (int i = 0; i < currentBoard.length/2; i++) {
                for (int j = 0; j < currentBoard[i].length; j++) {
                    //only have to evaluate the piece string once now
                    String thisPieceString = Runner.controlAndSeparation.singleBitBitboardToString(currentBoard[i][j]);
                    // get all of its possible moves.
                    long possibleMoves = 0l;
                    switch (i) {
                        case 0:
                            possibleMoves = Runner.checkValidConditions.getLowerCaseRookMoves(currentBoard[i][j], pos);
                            break;
                        case 1:
                            possibleMoves = Runner.checkValidConditions.getLowerCaseKnightMoves(currentBoard[i][j], pos);
                            break;
                        case 2:
                            possibleMoves = Runner.checkValidConditions.getLowerCaseBishopMoves(currentBoard[i][j], pos);
                            break;
                        case 3:
                            possibleMoves = Runner.checkValidConditions.getLowerCaseQueenMoves(currentBoard[i][j], pos);
                            break;
                        case 4:
                            possibleMoves = Runner.checkValidConditions.getLowerCaseKingMoves(pos);
                            break;
                        case 5:
                            possibleMoves = Runner.checkValidConditions.getLowerCasePawnCombined(currentBoard[i][j], pos);
                            break;
                    }
                    // for each possible moveset of a piece, make a new array of single move bitboards for that piece
                    Long[] singleMoveBitboards = Runner.controlAndSeparation.splitBitboard(possibleMoves);
                    for (int k = 0; k < singleMoveBitboards.length; k++) {
                        boolean canPromote = false;
                        //if there is a capital rook on the 8th rank, it can promote
                        if(((singleMoveBitboards[k] & Runner.checkValidConditions.rank1)!=0) && i==5){ //and it is a lowercase pawn
                            canPromote = true;
                        }
                        //if the move does not lead to check for yourself
                        if(!pos.moveLeadsToCheck(currentBoard[i][j], singleMoveBitboards[k], 'l', "")){ //keep this with no optional promotion because it cannot prevent check or checkmate within one move... saves work
                            //turns the square the piece is on and the square it moves to into algebraic notation to add to the movelist
                            if(canPromote){ //if you can promote, it adds the promotions for queens and rooks as possibilities for promotion
                                possibleMovesList.add(thisPieceString + Runner.controlAndSeparation.singleBitBitboardToString(singleMoveBitboards[k])+"q");
                                possibleMovesList.add(thisPieceString + Runner.controlAndSeparation.singleBitBitboardToString(singleMoveBitboards[k])+"r");
                            }else {//otherwise, no promotion for you
                                possibleMovesList.add(thisPieceString + Runner.controlAndSeparation.singleBitBitboardToString(singleMoveBitboards[k]));
                            }
                        }
                    }
                }
            }
        }

        //change the ArrayList back to an Array to return
        String[] finalMoveset = new String[possibleMovesList.size()];
        for (int i = 0; i < finalMoveset.length; i++) {
            finalMoveset[i] = possibleMovesList.get(i);
        }

        //return the final list of moves
        return finalMoveset;
    }


}
