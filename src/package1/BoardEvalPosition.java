package package1;

public class BoardEvalPosition {
    /*Game states:
    1. Early/Mid Game: 0
    3. End Game: 1
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



    public int getBoardEvaluation(Position pos){
        int returningEval = 0;
        // if greater than 16 pieces on board, is early/mid game (0), and if is less, is end game (1)
        int gameState = Runner.controlAndSeparation.splitBitboard(Runner.controlAndSeparation.condenseBoard(Runner.mainBoard.mainPosition.getCurrentBoard())).length>16 ? 0: 1;

        returningEval+=pawnEval(pos, gameState);

        return returningEval;
    }

    private static int pawnEval(Position pos, int gameState){
        int returningEval = 0;
        //Capital
        if(gameState==0){
            String s = Runner.controlAndSeparation.singleBitBitboardToString(pos.getCurrentBoard()[11]);
            for (int i = 0; i < s.length(); i++) {
                if(s.charAt(i)=='1'){

                }
            }
        }

        return returningEval;
    }


}
