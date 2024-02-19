import java.util.ArrayList;
import java.util.Arrays;

public class Player {
    private int STOP = -1;
    private int CONTINUE = 0;
    private int SUCCESS = 1;

    public static void main(String[] args) throws InterruptedException {
        try {
            new Player().start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() throws InterruptedException {

        Board game_board = new Board(Constants.DEFAULT_SIZE);
        Board game_board_2 = game_board.copy();

        Game game_search_1 = new Game(game_board);
        Board board = game_search_1.getBoard();
        game_search_1.theView.setTitle("IDA* AI");
        GUI ids_button = game_search_1.theView;

        WaterColor start_color = board.tiles[0][0].getColor();
        State start = new State(board, null, 1, 0, start_color);

        Game game_search_2 = new Game(game_board_2);
        Board board2 = game_search_2.getBoard();
        game_search_2.theView.setTitle("Greedy AI");
        GUI greedy_button = game_search_2.theView;

        WaterColor greedy_start_color = board2.tiles[0][0].getColor();
        State greedy_start = new State(board2, null, 1, 0, greedy_start_color);

        Search solver = new Search();
        int step_limit = game_search_1.getStepLimit();

        State greedy_solution = solver.greedy_solver(greedy_start, step_limit);
        System.out.println("Greedy Search Finished");

        State ids_solution = solver.uninformed_solver(start, step_limit);
        System.out.println("IDA* Search Finished");

        boolean greedy_solution_check = (greedy_solution != null);

        boolean ids_solution_check = (ids_solution != null);

        if (!greedy_solution_check) System.out.println("Greedy could not find a solution");

        if (!ids_solution_check) System.out.println("IDA* could not find a solution");

        int greedy_steps = (greedy_solution_check) ? greedy_solution.solutionStepCount() : step_limit;
        int ids_steps = (ids_solution_check) ? ids_solution.solutionStepCount() : step_limit;

        ArrayList<WaterColor> greedy_moves_list = (greedy_solution_check) ? greedy_solution.getStepsList() : null;
        ArrayList<WaterColor> ids_moves_list = (ids_solution_check) ? ids_solution.getStepsList() : null;

        System.out.println("Greedy Steps : " + greedy_steps);
        System.out.println("IDA* Steps : " + ids_steps);

        State solution;

        if (greedy_steps < ids_steps) {
            solution = greedy_solution;
            System.out.println("Greedy Solution Wins");
        } else if (ids_steps < greedy_steps) {
            solution = ids_solution;
            System.out.println("IDA* Solution Wins");
        } else {
            solution = ids_solution;
            System.out.println("Both Lose");
        }

        int best_moves = solution.solutionStepCount();

        ArrayList<WaterColor> moves_list = solution.getStepsList();

        System.out.println("Best Move Count = " + best_moves);
        System.out.println();
        System.out.println("Best Moves List : " + moves_list);

        Thread.sleep(1000);
        System.out.println("Playing Games Now...");
        Thread.sleep(1000);

        Thread t1 = new Thread(() -> {
            try {
                assert greedy_moves_list != null;
                play_game(greedy_moves_list, greedy_button, "Greedy");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                assert ids_moves_list != null;
                play_game(ids_moves_list, ids_button, "IDA*");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

    }

    public static void play_game(ArrayList<WaterColor> moves_list, GUI button, String na) throws InterruptedException {
        System.out.println("Playing as " + na);

        for (WaterColor move : moves_list) {
            button.PlayerButton(move);
            Thread.sleep(250);
        }

        System.out.println(na + " Finished");

    }
}
