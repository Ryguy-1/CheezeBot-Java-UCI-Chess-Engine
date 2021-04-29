package package1;

import java.util.Scanner;

public class UCI {
    Thread t1;
    public static boolean programRunning;
    public static final int lookAhead = 4;
    UCI(){
        initiateCommunication();
        programRunning = true;
    }

    private void initiateCommunication() {
        t1 = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);

            //draw initial game board
            Runner.mainBoard.drawGameBoard(Runner.mainBoard.mainPosition.getCurrentBoard());

            while(programRunning){
                if(Runner.search.capitalIsInCheckmate(Runner.mainBoard.mainPosition)){
                    programRunning = false;
                    System.out.println("Capital Is In Checkmate!! Good Game");
                    break;
                } else if (Runner.search.lowerCaseIsInCheckmate(Runner.mainBoard.mainPosition)) {
                    programRunning = false;
                    System.out.println("Lower Case Is In Checkmate!! Good Game");
                    break;
                }
                    //if there are no checkmates
                    //my turn as capital (have to make valid moves because don't need to check if my move as a user is valid or not for uci)
                    System.out.println();
                    System.out.print("Move FromTo (Algebraic Notation): ");
                    String fromTo = scanner.nextLine();
                    Runner.mainBoard.mainPosition.fromToMove(fromTo);
                    //print board after my move
                    Runner.mainBoard.drawGameBoard(Runner.mainBoard.mainPosition.getCurrentBoard());
                    System.out.println();
                if(Runner.search.capitalIsInCheckmate(Runner.mainBoard.mainPosition)){
                    programRunning = false;
                    System.out.println("Capital Is In Checkmate!! Good Game");
                    break;
                } else if (Runner.search.lowerCaseIsInCheckmate(Runner.mainBoard.mainPosition)) {
                    programRunning = false;
                    System.out.println("Lower Case Is In Checkmate!! Good Game");
                    break;
                }
                    //computer turn as lower case
                    Runner.mainBoard.mainPosition.fromToMove(Runner.minimax.minimax(Runner.mainBoard.mainPosition, 4, false, Minimax.MIN, Minimax.MAX).getMovesToCurrent().get(0));
                    //Print board for computer move
                    Runner.mainBoard.drawGameBoard(Runner.mainBoard.mainPosition.getCurrentBoard());

            }

        });
        t1.start();
    }

    }

