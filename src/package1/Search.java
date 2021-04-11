package package1;

public class Search {

    Search(){


    }



    public boolean capitalIsInCheckmate(Long[] currentBoardSingle){
        //Position 10// (Capital King)
        //
        boolean isInCheckmate = true; //set to false if any single move makes you not in check


        Long[][] currentBoard = Runner.controlAndSeparation.splitBitboardArray(currentBoardSingle);

        Long[] condensedBoard = Runner.controlAndSeparation.condense2dBoard(currentBoard);

        //cycle through every CAPITAL piece.
        for (int i = currentBoard.length/2; i < currentBoard.length; i++) {
            for (int j = 0; j < currentBoard[i].length; j++) {
                // get all of its possible moves.
                long possibleMoves = 0l;
                switch(i){
                    case 6:
                        possibleMoves = Runner.checkValidConditions.getCapitalRookMoves(currentBoard[i][j], condensedBoard);
                        break;
                    case 7:
                        possibleMoves = Runner.checkValidConditions.getCapitalKnightMoves(currentBoard[i][j], condensedBoard);
                        break;
                    case 8:
                        possibleMoves = Runner.checkValidConditions.getCapitalBishopMoves(currentBoard[i][j], condensedBoard);
                        break;
                    case 9:
                        possibleMoves = Runner.checkValidConditions.getCapitalQueenMoves(currentBoard[i][j], condensedBoard);
                        break;
                    case 10:
                        possibleMoves = Runner.checkValidConditions.getCapitalKingMoves(currentBoard[i][j], condensedBoard);
                        break;
                    case 11:
                        possibleMoves = Runner.checkValidConditions.getCapitalPawnCombined(currentBoard[i][j], condensedBoard);
                        break;
                }
                // for each possible move, make a new array of single move bitboards.
                Long[] singleMoveBitboards = Runner.controlAndSeparation.splitBitboard(possibleMoves);
                //loop through all of the single moves in singleMoveBitboards and use the Move function to move them, and set the board equal to a new board array.
                for (int k = 0; k < singleMoveBitboards.length; k++) {
                    Long[] tempBoard = Runner.controlAndSeparation.fromToMove(currentBoard[i][j], singleMoveBitboards[k], condensedBoard);
                    //checks the moved board (tempBoard) if capital is in check or not.
                    //if capital is not in check for any one of them, we simply return false
                    if(!Runner.checkValidConditions.capitalIsInCheck(tempBoard)){
                        return false;
                    }
                }
            }
        }
        //return true if for every one of capital's possible moves
        return true;
    }

    public boolean capitalIsInCheckmate(Long[][] currentBoard){
        //Position 10// (Capital King)
        //

        //cycle through every CAPITAL piece.
        // get all of its possible moves.
            // for each possible move, make a new array of single move bitboards.
            // move it (use move methods!!!) individually and...
                // at the same time condense the board and check for check.
            // set moved bitboard back equal to its original bitboard.
        //if there is never a move which leads to a non-check position, you are in checkmate, congrats

        //r, n, b, q, k, p, R, N, B, Q, K, P


        //a 2d array of all the pieces bitboards invidually and an array of all the pieces. Also have methods which can take a whole board 1d array of bitboards and tell you the combined moves of one type of piece
        //write a method which takes the current board and the piece which you want's bitboard and tell you all the moves which that individual piece could do.
        //first find all the places for that type of piece, then substitute that piece in for the index of that piece in the greater bitboard and take only the slots which are in both of those bitboards.
        //order in array: r, n, b, q, k, p, R, N, B, Q, K, P


        //the problem is that when there is another of the same piece in the ray of the piece you want, it doesn't have a way to exclude the spaces after that piece like it usually would because it is just a simple and function.

        boolean isInCheckmate = true; //set to false if any single move makes you not in check

         Long[] condensedBoard = Runner.controlAndSeparation.condense2dBoard(currentBoard);

        //cycle through every CAPITAL piece.
         for (int i = currentBoard.length/2; i < currentBoard.length; i++) {
            for (int j = 0; j < currentBoard[i].length; j++) {
                // get all of its possible moves.
                long possibleMoves = 0l;
                switch(i){
                    case 6:
                        possibleMoves = Runner.checkValidConditions.getCapitalRookMoves(currentBoard[i][j], condensedBoard);
                        break;
                    case 7:
                        possibleMoves = Runner.checkValidConditions.getCapitalKnightMoves(currentBoard[i][j], condensedBoard);
                        break;
                    case 8:
                        possibleMoves = Runner.checkValidConditions.getCapitalBishopMoves(currentBoard[i][j], condensedBoard);
                        break;
                    case 9:
                        possibleMoves = Runner.checkValidConditions.getCapitalQueenMoves(currentBoard[i][j], condensedBoard);
                        break;
                    case 10:
                        possibleMoves = Runner.checkValidConditions.getCapitalKingMoves(currentBoard[i][j], condensedBoard);
                        break;
                    case 11:
                        possibleMoves = Runner.checkValidConditions.getCapitalPawnCombined(currentBoard[i][j], condensedBoard);
                        break;
                }
                // for each possible move, make a new array of single move bitboards.
                Long[] singleMoveBitboards = Runner.controlAndSeparation.splitBitboard(possibleMoves);
                //loop through all of the single moves in singleMoveBitboards and use the Move function to move them, and set the board equal to a new board array.
                for (int k = 0; k < singleMoveBitboards.length; k++) {
                    Long[] tempBoard = Runner.controlAndSeparation.fromToMove(currentBoard[i][j], singleMoveBitboards[k], condensedBoard);
                    //checks the moved board (tempBoard) if capital is in check or not.
                    //if capital is not in check for any one of them, we simply return false
                    if(!Runner.checkValidConditions.capitalIsInCheck(tempBoard)){
                        return false;
                    }
                }
            }
        }
         //return true if for every one of capital's possible moves
        return true;
    }



}
