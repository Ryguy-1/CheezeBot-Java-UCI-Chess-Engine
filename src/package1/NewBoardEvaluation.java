package package1;

public class NewBoardEvaluation { //Come back to this...

    /*Game states:
        1. Early Game: 0
        2. Mid Game: 1
        3. End Game: 2
     */
    byte gameState = 0;

    private static final int checkmateWeight = (Integer.MAX_VALUE/2); //HAS TO BE -1 BECAUSE MIN AND MAX VALUES IN MINIMAX ARE INTEGER.MAX_VALUE. THROWS ERROR OTHERWISE.
    private static final int stalemateWeight = (Integer.MAX_VALUE/4);

    /*
        Middle and End Games
     */
    /** Piece/square table for king during middle game. */
    static final int[] kt1b =
            { -22,-35,-40,-40,-40,-40,-35,-22,
            -22,-35,-40,-40,-40,-40,-35,-22,
            -25,-35,-40,-45,-45,-40,-35,-25,
            -15,-30,-35,-40,-40,-35,-30,-15,
            -10,-15,-20,-25,-25,-20,-15,-10,
            4, -2, -5,-15,-15, -5, -2,  4,
            16, 14,  7, -3, -3,  7, 14, 16,
            24, 24,  9,  0,  0,  9, 24, 24 };

    /** Piece/square table for king during end game. */
    static final int[] kt2b =
            {  0,  8, 16, 24, 24, 16,  8,  0,
            8, 16, 24, 32, 32, 24, 16,  8,
            16, 24, 32, 40, 40, 32, 24, 16,
            24, 32, 40, 48, 48, 40, 32, 24,
            24, 32, 40, 48, 48, 40, 32, 24,
            16, 24, 32, 40, 40, 32, 24, 16,
            8, 16, 24, 32, 32, 24, 16,  8,
            0,  8, 16, 24, 24, 16,  8,  0 };

    /** Piece/square table for pawns during middle game. */
    static final int[] pt1b =
            {  0,  0,  0,  0,  0,  0,  0,  0,
            8, 16, 24, 32, 32, 24, 16,  8,
            3, 12, 20, 28, 28, 20, 12,  3,
            -5,  4, 10, 20, 20, 10,  4, -5,
            -6,  4,  5, 16, 16,  5,  4, -6,
            -6,  4,  2,  5,  5,  2,  4, -6,
            -6,  4,  4,-15,-15,  4,  4, -6,
            0,  0,  0,  0,  0,  0,  0,  0 };

    /** Piece/square table for pawns during end game. */
    static final int[] pt2b =
            {   0,  0,  0,  0,  0,  0,  0,  0,
            25, 40, 45, 45, 45, 45, 40, 25,
            17, 32, 35, 35, 35, 35, 32, 17,
            5, 24, 24, 24, 24, 24, 24,  5,
            -9, 11, 11, 11, 11, 11, 11, -9,
            -17,  3,  3,  3,  3,  3,  3,-17,
            -20,  0,  0,  0,  0,  0,  0,-20,
            0,  0,  0,  0,  0,  0,  0,  0 };

    /** Piece/square table for knights during middle game. */
    static final int[] nt1b =
            { -53,-42,-32,-21,-21,-32,-42,-53,
            -42,-32,-10,  0,  0,-10,-32,-42,
            -21,  5, 10, 16, 16, 10,  5,-21,
            -18,  0, 10, 21, 21, 10,  0,-18,
            -18,  0,  3, 21, 21,  3,  0,-18,
            -21,-10,  0,  0,  0,  0,-10,-21,
            -42,-32,-10,  0,  0,-10,-32,-42,
            -53,-42,-32,-21,-21,-32,-42,-53 };

    /** Piece/square table for knights during end game. */
    static final int[] nt2b =
            { -56,-44,-34,-22,-22,-34,-44,-56,
            -44,-34,-10,  0,  0,-10,-34,-44,
            -22,  5, 10, 17, 17, 10,  5,-22,
            -19,  0, 10, 22, 22, 10,  0,-19,
            -19,  0,  3, 22, 22,  3,  0,-19,
            -22,-10,  0,  0,  0,  0,-10,-22,
            -44,-34,-10,  0,  0,-10,-34,-44,
            -56,-44,-34,-22,-22,-34,-44,-56 };

    /** Piece/square table for bishops during middle game. */
    static final int[] bt1b =
            {  0,  0,  0,  0,  0,  0,  0,  0,
            0,  4,  2,  2,  2,  2,  4,  0,
            0,  2,  4,  4,  4,  4,  2,  0,
            0,  2,  4,  4,  4,  4,  2,  0,
            0,  2,  4,  4,  4,  4,  2,  0,
            0,  3,  4,  4,  4,  4,  3,  0,
            0,  4,  2,  2,  2,  2,  4,  0,
            -5, -5, -7, -5, -5, -7, -5, -5 };

    /** Piece/square table for bishops during middle game. */
    static final int[] bt2b =
            {  0,  0,  0,  0,  0,  0,  0,  0,
            0,  2,  2,  2,  2,  2,  2,  0,
            0,  2,  4,  4,  4,  4,  2,  0,
            0,  2,  4,  4,  4,  4,  2,  0,
            0,  2,  4,  4,  4,  4,  2,  0,
            0,  2,  4,  4,  4,  4,  2,  0,
            0,  2,  2,  2,  2,  2,  2,  0,
            0,  0,  0,  0,  0,  0,  0,  0 };

    /** Piece/square table for queens during middle game. */
    static final int[] qt1b =
            { -10, -5,  0,  0,  0,  0, -5,-10,
            -5,  0,  5,  5,  5,  5,  0, -5,
            0,  5,  5,  6,  6,  5,  5,  0,
            0,  5,  6,  6,  6,  6,  5,  0,
            0,  5,  6,  6,  6,  6,  5,  0,
            0,  5,  5,  6,  6,  5,  5,  0,
            -5,  0,  5,  5,  5,  5,  0, -5,
            -10, -5,  0,  0,  0,  0, -5,-10 };

    /** Piece/square table for rooks during middle game. */
    static final int[] rt1b =
            {  0,  3,  5,  5,  5,  5,  3,  0,
            15, 20, 20, 20, 20, 20, 20, 15,
            0,  0,  0,  0,  0,  0,  0,  0,
            0,  0,  0,  0,  0,  0,  0,  0,
            -2,  0,  0,  0,  0,  0,  0, -2,
            -2,  0,  0,  2,  2,  0,  0, -2,
            -3,  2,  5,  5,  5,  5,  2, -3,
            0,  3,  5,  5,  5,  5,  3,  0 };
    /////////////////////////////////////////////////



    // Looks to Openings if in early game and this method if not....

    public int getBoardEvaluation(Position pos){
        int totalEval = 0;
        switch(pos.getGameState()){
            case 0:
                // look to openings
                break;
            default:
                // figure it out
                totalEval+=getPawnValue(pos);
        }






        return totalEval;
    }

    //order in array: r, n, b, q, k, p, R, N, B, Q, K, P
    private int getPawnValue(Position pos){
        int totalPawnValue = 0;

        switch(pos.getGameState()){
            case 1: //mid game

                break;
            case 2: //end game

                break;
        }


        return totalPawnValue;
    }



































//    //bitboard of pyramid structure pawns at the beginning
//    long capitalPawnMoveGoalEarlyGame = Runner.mainBoard.parseLong("0000000000000000000000000000000000011000001111000000000000000000", 2);
//    long lowerCasePawnMoveGoalEarlyGame = Runner.mainBoard.parseLong("0000000000000000001111000001100000000000000000000000000000000000", 2);
//    //bitboard of expanded pyramid structure pawns mid game
//    long capitalPawnMoveGoalMidGame = Runner.mainBoard.parseLong("0000000000000000000000000001100000111100011111100000000000000000", 2);
//    long lowerCasePawnMoveGoalMidGame = Runner.mainBoard.parseLong  ("0000000000000000011111100011110000011000000000000000000000000000", 2);
//    //bitboard pawns want to move to end game (closer to the other side for promotion) -> Rank before promotion because promotions.
//    long capitalPawnMoveGoalEndGame1 = Runner.checkValidConditions.rank7;
//    long lowerCasePawnMoveGoalEndGame1 = Runner.checkValidConditions.rank2;
//    //
//    long capitalPawnMoveGoalEndGame2 = Runner.checkValidConditions.rank6;
//    long lowerCasePawnMoveGoalEndGame2 = Runner.checkValidConditions.rank3;
//    //
//    long capitalPawnMoveGoalEndGame3 = Runner.checkValidConditions.rank5;
//    long lowerCasePawnMoveGoalEndGame3 = Runner.checkValidConditions.rank4;
//    //
//    long capitalPawnMoveGoalEndGame4 = Runner.checkValidConditions.rank4;
//    long lowerCasePawnMoveGoalEndGame4 = Runner.checkValidConditions.rank5;
//    //
//    long capitalPawnMoveGoalEndGame5 = Runner.checkValidConditions.rank3;
//    long lowerCasePawnMoveGoalEndGame5 = Runner.checkValidConditions.rank6;
//    //
//    long capitalPawnMoveGoalEndGame6 = Runner.checkValidConditions.rank2;
//    long lowerCasePawnMoveGoalEndGame6 = Runner.checkValidConditions.rank7;
//    ///////////////////////
//
//
//
//
//
//
//    private static final int checkmateWeight = (Integer.MAX_VALUE/2); //HAS TO BE -1 BECAUSE MIN AND MAX VALUES IN MINIMAX ARE INTEGER.MAX_VALUE. THROWS ERROR OTHERWISE.
//    private static final int stalemateWeight = (Integer.MAX_VALUE/4);
//
//    private static final int stackedValue = 8; //Rooks: value you add per rook stacked
//    private static final int hasCastledValue = 10; //Rooks: value you add for each player if they have castled.
//
//    public int getBoardEvaluation(Position pos){
//        int totalEvaluation = 0;
//
//
//        //if one side in checkmate, look no further.
////        if(Runner.search.capitalIsInCheckmate(pos)){
////            return -checkmateWeight;
////        }else if(Runner.search.lowerCaseIsInCheckmate(pos)){
////            return checkmateWeight;
////        }
//        //if not in checkmate...
//
////        totalEvaluation+=getPawnAdvantage(pos); //typically maxes out somewhere around 5 ish. Should be rather constant throughout game states
////        totalEvaluation+=getRookAdvantage(pos); // maxes out somewhere around 25-40 ish
////        totalEvaluation+=getKnightAdvantage(pos); // maxes out at around 16.
////        totalEvaluation+=getBishopAdvantage(pos); //maxes out somewhere around 16 as well.
////        totalEvaluation+=getQueenAdvantage(pos); //maxes out somewhere around 30 ish (single queen no promotions)
//
//        return totalEvaluation;
//    }


//    /* Pawn Preferences:
//       - Set up good Pawn Structure (using matching bitboards above for pawn structure)
//       - Protect their own pieces
//       - Attack other pieces as long as they are supported - not implemented yet
//     */
//    private int getPawnAdvantage(Position pos){
//        //order in array: r, n, b, q, k, p, R, N, B, Q, K, P
//
//        int totalPawnAdvantage = 0;
//
//        long capitalPawnAttacks = Runner.checkValidConditions.getCapitalPawnThreatenedSpaces(pos);
//        long lowerCasePawnAttacks = Runner.checkValidConditions.getLowerCasePawnThreatenedSpaces(pos);
//
//        //Pawns like to protect their own pieces
//        totalPawnAdvantage += Runner.controlAndSeparation.splitBitboard(capitalPawnAttacks&Runner.controlAndSeparation.condenseBoard(Runner.controlAndSeparation.getCapitalPieces(pos))).length
//                - Runner.controlAndSeparation.splitBitboard(lowerCasePawnAttacks&Runner.controlAndSeparation.condenseBoard(Runner.controlAndSeparation.getLowerCasePieces(pos))).length;
//
//        //Pawns like to attack other color's pieces just as much, as long as they are supported
//        /*
//            Do later...
//         */
//
//
//
//        /*Game States:
//            1. Set up small protected pyramid structure
//            2. Set up large protected pyramid structure
//            3. Push Pawns for promotion
//         */
//        byte capitalPawnCount = 0;
//        byte lowerCasePawnCount = 0;
//        if(gameState==0){ //early game
//            capitalPawnCount+=Runner.controlAndSeparation.splitBitboard(capitalPawnMoveGoalEarlyGame & pos.getCurrentBoard()[11]).length;
//            lowerCasePawnCount+=Runner.controlAndSeparation.splitBitboard(lowerCasePawnMoveGoalEarlyGame & pos.getCurrentBoard()[5]).length;
//        }else if(gameState==1){ //mid game
//            capitalPawnCount+=Runner.controlAndSeparation.splitBitboard(capitalPawnMoveGoalMidGame & pos.getCurrentBoard()[11]).length;
//            lowerCasePawnCount+=Runner.controlAndSeparation.splitBitboard(lowerCasePawnMoveGoalMidGame & pos.getCurrentBoard()[5]).length;
//        }else if(gameState==2){ //end game
//            /* Get closer to other side for promotion*/
//            capitalPawnCount+=Runner.controlAndSeparation.splitBitboard(capitalPawnMoveGoalEndGame1 & pos.getCurrentBoard()[11]).length*3;
//            capitalPawnCount+=Runner.controlAndSeparation.splitBitboard(capitalPawnMoveGoalEndGame2 & pos.getCurrentBoard()[11]).length*2;
//            capitalPawnCount+=Runner.controlAndSeparation.splitBitboard(capitalPawnMoveGoalEndGame3 & pos.getCurrentBoard()[11]).length;
//            capitalPawnCount+=Runner.controlAndSeparation.splitBitboard(capitalPawnMoveGoalEndGame4 & pos.getCurrentBoard()[11]).length;
//            capitalPawnCount+=Runner.controlAndSeparation.splitBitboard(capitalPawnMoveGoalEndGame5 & pos.getCurrentBoard()[11]).length;
//            capitalPawnCount+=Runner.controlAndSeparation.splitBitboard(capitalPawnMoveGoalEndGame6 & pos.getCurrentBoard()[11]).length;
//
//            lowerCasePawnCount+=Runner.controlAndSeparation.splitBitboard(lowerCasePawnMoveGoalEndGame1 & pos.getCurrentBoard()[5]).length*3;
//            lowerCasePawnCount+=Runner.controlAndSeparation.splitBitboard(lowerCasePawnMoveGoalEndGame2 & pos.getCurrentBoard()[5]).length*2;
//            lowerCasePawnCount+=Runner.controlAndSeparation.splitBitboard(lowerCasePawnMoveGoalEndGame3 & pos.getCurrentBoard()[5]).length;
//            lowerCasePawnCount+=Runner.controlAndSeparation.splitBitboard(lowerCasePawnMoveGoalEndGame4 & pos.getCurrentBoard()[5]).length;
//            lowerCasePawnCount+=Runner.controlAndSeparation.splitBitboard(lowerCasePawnMoveGoalEndGame5 & pos.getCurrentBoard()[5]).length;
//            lowerCasePawnCount+=Runner.controlAndSeparation.splitBitboard(lowerCasePawnMoveGoalEndGame6 & pos.getCurrentBoard()[5]).length;
//        }
//
//        //adds the states where the pawns should be to the total advantage
//        totalPawnAdvantage += capitalPawnCount-lowerCasePawnCount;
//
//        return totalPawnAdvantage;
//
//    }
//
//    /*Rook Preferences:
//       - If you have castled, this is good
//       - Take control of as many squares as possible (largest amount of attacking squares available)
//       - Link themselves if possible for potential (Stacked or "attacking" each other)
//     */
//    private int getRookAdvantage(Position pos) {
//        //order in array: r, n, b, q, k, p, R, N, B, Q, K, P
//
//        int totalRookAdvantage = 0;
//
//        //castle adder -> Castling is good
//        if(pos.getCapitalHasCastled()) totalRookAdvantage+=hasCastledValue;
//        if(pos.getLowerCaseHasCastled()) totalRookAdvantage-=hasCastledValue;
//
//
//        int numCapitalRookAttackingSquares = Runner.controlAndSeparation.splitBitboard(Runner.checkValidConditions.getCapitalRookMoves(pos)).length;
//        int numLowerCaseRookAttackingSquares = Runner.controlAndSeparation.splitBitboard(Runner.checkValidConditions.getLowerCaseRookMoves(pos)).length;
//
//        totalRookAdvantage += numCapitalRookAttackingSquares - numLowerCaseRookAttackingSquares;
//
//
//        //Take some time though...
//        //stacked rooks - just want to know if they are in the same column
//        //add a linkedValue for every rook that is in the same column after the first
//        Long[] capitalRooks = Runner.controlAndSeparation.splitBitboard(pos.getCurrentBoard()[6]);
//        Long[] lowerCaseRooks = Runner.controlAndSeparation.splitBitboard(pos.getCurrentBoard()[0]);
//        //capital stacked
//        if (capitalRooks.length>1) {
//            for (int i = 0; i < capitalRooks.length - 1; i++) {
//                for (int j = i + 1; j < capitalRooks.length; j++) {
//                    if (Runner.controlAndSeparation.isInSameColumn(capitalRooks[i], capitalRooks[j])) {
//                        System.out.println("Capital Stacked");
//                        totalRookAdvantage += stackedValue;
//                    }
//                }
//            }
//        }
//        //lower case stacked
//        if(lowerCaseRooks.length>1) {
//            for (int i = 0; i < lowerCaseRooks.length - 1; i++) {
//                for (int j = i + 1; j < lowerCaseRooks.length; j++) {
//                    if (Runner.controlAndSeparation.isInSameColumn(lowerCaseRooks[i], lowerCaseRooks[j])) {
//                        System.out.println("Lower Case Stacked");
//                        totalRookAdvantage -= stackedValue;
//                    }
//                }
//            }
//        }
//        return totalRookAdvantage;
//    }
//
//    /*Knight Preferences:
//       - Attack as many squares as possible (edges are bad usually)
//     */
//    private int getKnightAdvantage(Position pos){
//        int totalKnightAdvantage = 0;
//
//        totalKnightAdvantage+=Runner.controlAndSeparation.splitBitboard(Runner.checkValidConditions.getCapitalKnightMoves(pos)).length;
//        totalKnightAdvantage-=Runner.controlAndSeparation.splitBitboard(Runner.checkValidConditions.getLowerCaseKnightMoves(pos)).length;
//
//        return totalKnightAdvantage;
//    }
//
//    /*Bishop Preferences:
//        - Attack as many squares as possible
//    */
//    private int getBishopAdvantage(Position pos){
//        int totalBishopAdvantage = 0;
//
//        totalBishopAdvantage+=Runner.controlAndSeparation.splitBitboard(Runner.checkValidConditions.getCapitalBishopMoves(pos)).length;
//        totalBishopAdvantage-=Runner.controlAndSeparation.splitBitboard(Runner.checkValidConditions.getLowerCaseBishopMoves(pos)).length;
//
//        return totalBishopAdvantage;
//    }
//
//    /*Queen Preferences:
//        - Attack as many squares as possible
//    */
//    private int getQueenAdvantage(Position pos){
//        int totalQueenAdvantage = 0;
//
//        totalQueenAdvantage+=Runner.controlAndSeparation.splitBitboard(Runner.checkValidConditions.getCapitalQueenMoves(pos)).length;
//        totalQueenAdvantage-=Runner.controlAndSeparation.splitBitboard(Runner.checkValidConditions.getLowerCaseQueenMoves(pos)).length;
//
//        return totalQueenAdvantage;
//    }
//
//    /*King Preferences:
//        - GS1: Corners of its own side
//        - GS2: Corners of its own side
//        - GS3: Center of board
//     */
//    private int getKingAdvantage(Position pos){
//        int totalKingAdvantage = 0;
//
//
//        return totalKingAdvantage;
//    }
}
