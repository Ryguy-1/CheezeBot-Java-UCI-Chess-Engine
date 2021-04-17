package package1;

import java.util.Stack;

public class Position {
    //The intent of this class is to encapsulate whether the lower case and upper case kings, and all four rooks have moved.
    //Also, holds the history board and the chain of moves which have led up to that board in a STACK!! of moves.

    ////////////////////////
    protected boolean capitalAFileRookHasMoved = false;
    protected boolean capitalHFileRookHasMoved = false;
    ////////////////////////
    protected boolean lowerCaseAFileRookHasMoved = false;
    protected boolean lowerCaseHFileRookHasMoved = false;
    ////////////////////////
    protected boolean capitalKingHasMoved = false;
    protected boolean lowerCaseKingHasMoved = false;
    ///////////////////////

    private Long[] previousBoard = new Long[12];
    private Stack<String> moves = new Stack<>();







}