package package1;

import java.util.Scanner;

public class UCI {
    Thread t1;
    public static boolean programRunning;
    UCI(){
        initiateCommunication();
        programRunning = true;
    }

    private void initiateCommunication(){
        t1 = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);

            //draw initial game board
            Runner.mainBoard.drawGameBoard(Runner.mainBoard.mainBoard);

            while(programRunning){
                if(Runner.search.capitalIsInCheckmate(Runner.mainBoard.mainBoard)){
                    programRunning = false;
                    System.out.println("Capital Is In Check!! Good Game");
                    break;
                } else if (Runner.search.lowerCaseIsInCheckmate(Runner.mainBoard.mainBoard)) {
                    programRunning = false;
                    System.out.println("Capital Is In Check!! Good Game");
                    break;
                }
                    //if there are no checkmates
                    //my turn as capital (have to make valid moves because don't need to check if my move as a user is valid or not for uci)
                    System.out.println();
                    System.out.print("Move From: ");
                    String from = scanner.nextLine();
                    System.out.print("Move To: ");
                    String to = scanner.nextLine();
                    Runner.mainBoard.mainBoard = Runner.controlAndSeparation.fromToMove(from, to, Runner.mainBoard.mainBoard);
                    //print board after my move
                    Runner.mainBoard.drawGameBoard(Runner.mainBoard.mainBoard);
                    System.out.println();
                if(Runner.search.capitalIsInCheckmate(Runner.mainBoard.mainBoard)){
                    programRunning = false;
                    System.out.println("Capital Is In Checkmate!! Good Game");
                    break;
                } else if (Runner.search.lowerCaseIsInCheckmate(Runner.mainBoard.mainBoard)) {
                    programRunning = false;
                    System.out.println("Capital Is In Checkmate!! Good Game");
                    break;
                }
                    //computer turn as lower case
                    Runner.mainBoard.mainBoard = Runner.mainBoard.moveRandom("l", Runner.mainBoard.mainBoard);
                    //Print board for computer move
                    Runner.mainBoard.drawGameBoard(Runner.mainBoard.mainBoard);

            }

        });
        t1.start();
    }

}
