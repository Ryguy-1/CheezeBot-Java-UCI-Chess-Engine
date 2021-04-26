package package1;

import javax.swing.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;

public class MainBoard {

    //capital = white
    //lower case = black
    //r = rook, n = knight, b = bishop, q = queen, k = king, p = pawn
    //order in array: r, n, b, q, k, p, R, N, B, Q, K, P


//    String[][] visualRepresentation = {
//            {"r", "n", "b", "q", "k", "b", "n", "r"},
//            {"p", "p", "p", "p", "p", "p", "p", "p"},
//            {"",  "",  "",  "",  "",  "",  "",  ""},
//            {"",  "",  "",  "",  "",  "",  "",  ""},
//            {"",  "",  "",  "",  "",  "",  "",  ""},
//            {"",  "",  "",  "",  "",  "",  "",  ""},
//            {"P", "P", "P", "P", "P", "P", "P", "P"},
//            {"R", "N", "B", "Q", "K", "B", "N", "R"}
//    };

    public Position mainPosition;

    private Long longEnd1 = parseLong("1000000000000000000000000000000000000000000000000000000000000000", 2);

    String[][] visualRepresentation = {
            {"r", "n", "b", "q", "k", "b", "n", "r"},
            {"p", "p", "p", "p", "p", "p", "p", "p"},
            {"",  "",  "",  "",  "",  "",  "",  ""},
            {"",  "",  "",  "",  "",  "",  "",  ""},
            {"",  "",  "",  "",  "",  "",  "",  ""},
            {"",  "",  "",  "",  "",  "",  "",  ""},
            {"P", "P", "P", "P", "P", "P", "P", "P"},
            {"R", "N", "B", "Q", "K", "B", "N", "R"}
    };



    //temp random generator
    Random random;



    MainBoard(){
        initializeBoard();
        random = new Random();
    }

    private void initializeBoard(){

        Long[] mainBoardInitializer = new Long[12];

        //string array that will be converted to longs later
        String[] tempStrings = new String[12];
        //initialize setter strings
        for (int i = 0; i < tempStrings.length; i++) {
            StringBuilder temp = new StringBuilder();
            for (int j = 0; j < 64; j++) {
                temp.append("0");
            }
            tempStrings[i] = temp.toString();
        }


        //what index you are at in the bitboard string
        int counter = 0;

        //set each bitboard string in tempStrings to equal its position in the visual Representation. Each Case sets the String representing each bitboard
        //at the index of counter equal to 1 to say there is a piece present.
        for (int i = 0; i < visualRepresentation.length; i++) {
            for (int j = 0; j < visualRepresentation[i].length; j++) {
                switch(visualRepresentation[i][j]){
                    case "r":
                        tempStrings[0] = insertOneAtIndex(tempStrings[0], counter);
                        break;
                    case "n":
                        tempStrings[1] = insertOneAtIndex(tempStrings[1], counter);
                        break;
                    case "b":
                        tempStrings[2] = insertOneAtIndex(tempStrings[2], counter);
                        break;
                    case "q":
                        tempStrings[3] = insertOneAtIndex(tempStrings[3], counter);
                        break;
                    case "k":
                        tempStrings[4] = insertOneAtIndex(tempStrings[4], counter);
                        break;
                    case "p":
                        tempStrings[5] = insertOneAtIndex(tempStrings[5], counter);
                        break;
                    case "R":
                        tempStrings[6] = insertOneAtIndex(tempStrings[6], counter);
                        break;
                    case "N":
                        tempStrings[7] = insertOneAtIndex(tempStrings[7], counter);
                        break;
                    case "B":
                        tempStrings[8] = insertOneAtIndex(tempStrings[8], counter);
                        break;
                    case "Q":
                        tempStrings[9] = insertOneAtIndex(tempStrings[9], counter);
                        break;
                    case "K":
                        tempStrings[10] = insertOneAtIndex(tempStrings[10], counter);
                        break;
                    case "P":
                        tempStrings[11] = insertOneAtIndex(tempStrings[11], counter);
                        break;
                }
                counter++;
            }
        }
        for (int i = 0; i < mainBoardInitializer.length; i++) {
            mainBoardInitializer[i] = parseLong(tempStrings[i], 2);

        }
        mainPosition = new Position(mainBoardInitializer);

    }

    public void sysoMainBoard(){
        for (Long l : mainPosition.getCurrentBoard()) {
            System.out.println(parseString(l));
        }
    }

    public void sysoMainBoardLong(){
        for (Long l : mainPosition.getCurrentBoard()) {
            System.out.println(l);
        }
    }

    public String insertOneAtIndex(String s, int index){
        String temp = "";
        for (int i = 0; i < s.length(); i++) {
            if(i==index){
                temp+="1";
            }else{
                temp+=s.charAt(i);
            }
        }
        return temp;
    }

    public long parseLong(String s, int base) {
        return new BigInteger(s, base).longValue();
    }

    public String parseString(Long l){
        //toBinaryString cuts off unecessarry zeros, so for loop adds them back on
        String parsedLong = Long.toBinaryString(l);
        String returned = "";
        for (int i = 0; i < 64-parsedLong.length(); i++) {
            returned+="0";
        }
        return returned+parsedLong;
    }

    public void visualizeBitboard(long l){
        String temp = parseString(l);
        for (int i = 0; i < temp.length(); i++) {
            if(i%8==0){
                System.out.println();
            }
            if(temp.charAt(i)=='0'){
                System.out.print("0, ");
            }else{
                System.out.print("1, ");
            }
        }
        System.out.println();
    }

    public void visualizeBitboardArray(Long[] bitboards){
        long combined = 0l;
        for (int i = 0; i < bitboards.length; i++) {
            combined |= bitboards[i];
        }
        visualizeBitboard(combined);
        System.out.println();

    }

//    public void updateMainBoardAndHistory(Long[] newBoard){
//        mainBoardHistory = mainBoard;
//        mainBoard=newBoard;
//    }


    //Logic Crazy Chess Implementation of Drawing For Testing Purposes (Modified)
    public void drawGameBoard(Long[] currentBoard) {
        String chessBoard[][]=new String[8][8];
        for (int i=0;i<64;i++) {
            chessBoard[i/8][i%8]=" ";
        }
        //order in array: r, n, b, q, k, p, R, N, B, Q, K, P
        for (int i=0;i<64;i++) {
            if (((currentBoard[11]<<i)&longEnd1)==longEnd1) {chessBoard[i/8][i%8]="P";}
            if (((currentBoard[7]<<i)&longEnd1)==longEnd1) {chessBoard[i/8][i%8]="N";}
            if (((currentBoard[8]<<i)&longEnd1)==longEnd1) {chessBoard[i/8][i%8]="B";}
            if (((currentBoard[6]<<i)&longEnd1)==longEnd1) {chessBoard[i/8][i%8]="R";}
            if (((currentBoard[9]<<i)&longEnd1)==longEnd1) {chessBoard[i/8][i%8]="Q";}
            if (((currentBoard[10]<<i)&longEnd1)==longEnd1) {chessBoard[i/8][i%8]="K";}
            if (((currentBoard[5]<<i)&longEnd1)==longEnd1) {chessBoard[i/8][i%8]="p";}
            if (((currentBoard[1]<<i)&longEnd1)==longEnd1) {chessBoard[i/8][i%8]="n";}
            if (((currentBoard[2]<<i)&longEnd1)==longEnd1) {chessBoard[i/8][i%8]="b";}
            if (((currentBoard[0]<<i)&longEnd1)==longEnd1) {chessBoard[i/8][i%8]="r";}
            if (((currentBoard[3]<<i)&longEnd1)==longEnd1) {chessBoard[i/8][i%8]="q";}
            if (((currentBoard[4]<<i)&longEnd1)==longEnd1) {chessBoard[i/8][i%8]="k";}
        }
        for (int i=0;i<8;i++) {
            System.out.println(Arrays.toString(chessBoard[i]));
        }
    }

//    public Long[] moveRandom(String casing, Position pos){
//
//        Long[] currentBoard = pos.getCurrentBoard();
//
//        Long[] newBoard = new Long[12];
//        if(casing.equals("c")){
//            //capital
//
//            //NOT IMPLEMENTED
//
//        }else if(casing.equals("l")){
//
//            //splits the bitboard array into a 2d array for simplicity
//            Long[][] splitBitboardArray = Runner.controlAndSeparation.splitBitboardArray(currentBoard);
//            //takes only the lower case pieces from the 2d array
//            Long[][] lowerCasePieces2d = {splitBitboardArray[0], splitBitboardArray[1], splitBitboardArray[2], splitBitboardArray[3], splitBitboardArray[4], splitBitboardArray[5]};
//
//            //finds the total amount of pieces on the board
//            int totalPieces = 0;
//            for (int i = 0; i < lowerCasePieces2d.length; i++) {
//                for (int j = 0; j < lowerCasePieces2d[i].length; j++) {
//                    totalPieces++;
//                }
//            }
//
//            //chooses a random piece
//            int randomPiece = random.nextInt(totalPieces);
//
//            int chosenPiece = 0;
//            int counter = 0;
//            long possibleMoves = 0l;
//            long chosenMove = 0l;
//            //see comments above
//            boolean hasFoundMove = false;
//            while(!hasFoundMove){
//
//                randomPiece = random.nextInt(totalPieces);
//                //chosenPiece++;
//                counter=0;
//                OUTER: for (int i = 0; i < lowerCasePieces2d.length; i++) {
//                    for (int j = 0; j < lowerCasePieces2d[i].length; j++) {
//                        if(counter==randomPiece){ //was chosenPiece
//                            //this is the random piece chosen
//                            switch(i){
//                                case 0://lower case rook
//                                    //gets the possible moves for that individual piece of this piece type
//                                    possibleMoves = Runner.checkValidConditions.getLowerCaseRookMoves(lowerCasePieces2d[i][j], pos);
//                                    break;
//                                case 1://lower case knight
//                                    //gets the possible moves for that individual piece of this piece type
//                                    possibleMoves = Runner.checkValidConditions.getLowerCaseKnightMoves(lowerCasePieces2d[i][j], pos);
//                                    break;
//                                case 2://lower case Bishop
//                                    //gets the possible moves for that individual piece of this piece type
//                                    possibleMoves = Runner.checkValidConditions.getLowerCaseBishopMoves(lowerCasePieces2d[i][j], pos);
//                                    break;
//                                case 3://lower case Queen
//                                    //gets the possible moves for that individual piece of this piece type
//                                    possibleMoves = Runner.checkValidConditions.getLowerCaseQueenMoves(lowerCasePieces2d[i][j], pos);
//                                    break;
//                                case 4://lower case King
//                                    //gets the possible moves for that individual piece of this piece type
//                                    possibleMoves = Runner.checkValidConditions.getLowerCaseKingMoves(pos);
//                                    break;
//                                case 5://lower case Pawns
//                                    //gets the possible moves for that individual piece of this piece type
//                                    possibleMoves = Runner.checkValidConditions.getLowerCasePawnCombined(lowerCasePieces2d[i][j], pos);
//                                    break;
//                            }
//                            //pick a random possible move
//                            Long[] allIndividualMoves = Runner.controlAndSeparation.splitBitboard(possibleMoves);
//                            //pick a random individual move
//                            //if there are no possible moves, break outer and try again
//                            if(allIndividualMoves.length==0){
//                                break OUTER;
//                            }
//                            chosenMove = allIndividualMoves[random.nextInt(allIndividualMoves.length)];
//                            //gets the new board after the possible move was made
//                            newBoard = Runner.controlAndSeparation.fromToMove(lowerCasePieces2d[i][j], chosenMove, currentBoard);
//                            //if lower case is in check afterwards, then this is not a valid move, so break outer and try again
//                            if(Runner.checkValidConditions.lowerCaseIsInCheck(newBoard)){
//                                break OUTER;
//                            }else{ //if lower case is not in check afterwards, then this is a valid move, so you have found the move, can break outer, and continue onwards to make that move a reality :)
//                                hasFoundMove = true;
//                                break OUTER;
//                            }
//                        }
//                        counter++;
//                    }
//                }
//            }
//        }
//        return newBoard;
//    }


}
