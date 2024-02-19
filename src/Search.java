import java.util.*;

public class Search {

    private final int STOP = -1;
    private final int CONTINUE = 0;
    private final int SUCCESS = 1;
    Comparator<State> c = new Comparator<State>() {
        @Override
        public int compare(State o1, State o2) {
            return Integer.compare(o1.heuristic_cost, o2.heuristic_cost);
        }
    };
    private int best_moves;
    private int MAX_ITER = 500000;
    private int searches = 0;
    private State solution;
    private int found;
    private PriorityQueue<State> fringe = new PriorityQueue<>(c);


    public State uninformed_solver(State start_state, int max_depth) {

        solution = null;

        best_moves = start_state.current_board.size * 25 / 14;

        fringe.clear();

        fringe.add(start_state);

        found = CONTINUE;

        while (found == CONTINUE) IDS(max_depth);

        return solution;

    }

    public State greedy_solver(State start_state, int max_depth) {

        solution = null;

        best_moves = start_state.current_board.size * 25 / 14;

        found = CONTINUE;

        while (found == CONTINUE) greedy_search(start_state, max_depth);

        return solution;
    }


    public void IDS(int max_depth) {

        if (fringe.isEmpty() || searches >= MAX_ITER) {
            found = STOP;
            return;
        }

        State current_state = fringe.poll();

        if (current_state == null || current_state.depth >= max_depth) return;

        if (current_state.current_board.flooded.size() == Math.pow(current_state.current_board.size, 2)
                && current_state.solutionStepCount() < best_moves) {

            best_moves = current_state.solutionStepCount();

            solution = current_state;
            MAX_ITER = (int) (MAX_ITER * 0.85);

            System.out.println("Solution Found");
            System.out.println("IDA* Best Moves : " + best_moves);
            System.out.println("Total Searches = " + searches);

            searches = 0;

            return;

        }

        if (searches % 50000 == 0) {
            System.out.println("\r Total Searches = " + searches + ", Depth = " + current_state.depth);
        }

        expand_fringe(current_state, best_moves);

    }

    public void greedy_search(State start_state, int max_depth) {

        State current_state = start_state.copy(start_state);

        if (current_state == null || current_state.depth >= max_depth) return;

        while (found == CONTINUE && searches < MAX_ITER) {

            current_state = get_best_move(current_state, best_moves);

            if (current_state.current_board.flooded.size() == Math.pow(current_state.current_board.size, 2)) {

                found = SUCCESS;
                best_moves = current_state.solutionStepCount();
                solution = current_state;

                System.out.println("Greedy Solution Found");
                System.out.println("Best Moves : " + best_moves);
                System.out.println("Total Searches = " + searches);

                return;

            }

        }

    }

    public State get_best_move(State current_state, int max_depth) {

        if (current_state == null) return null;

        if (current_state.depth >= max_depth) {
            return current_state;
        }

        if (searches % 20000 == 0) {
            System.out.println("Total Searches = " + searches);
            System.out.println("Depth = " + current_state.depth);
        }

        Board current_board = current_state.current_board;

        ArrayList<WaterColor> moves = get_moves(current_board);

        WaterColor best_move = null;
        int max_flooded = -1;
        LinkedList<Coord> max_flooded_tiles = new LinkedList<>();

        for (WaterColor move : moves) {

            State copy = current_state.copy(current_state);

            LinkedList<Coord> temp = new LinkedList<>();
            temp.addAll(current_board.flooded);

            LinkedList<Coord> flooded = Flood.flood(move, copy.current_board);

            int flooded_tiles_size = flooded.size();

            if (flooded_tiles_size > max_flooded) {
                max_flooded = flooded_tiles_size;
                best_move = move;
                max_flooded_tiles.clear();
                max_flooded_tiles.addAll(flooded);
            }

            searches++;

        }

        Board best_board = new Board(current_board.size, current_board.tiles, max_flooded_tiles);

        return new State(best_board, current_state, current_state.depth + 1,
                (int) (Math.pow(best_board.size, 2) - max_flooded_tiles.size() + current_state.depth + 1), best_move);

    }

    public void expand_fringe(State current_state, int max_depth) {

        if (current_state == null || current_state.depth >= max_depth) return;

        Board current_board = current_state.current_board;

        ArrayList<WaterColor> moves = get_moves(current_board);

        for (WaterColor move : moves) {

            State copy = current_state.copy(current_state);

            LinkedList<Coord> temp = new LinkedList<>();
            temp.addAll(current_board.flooded);

            LinkedList<Coord> flooded = Flood.flood(move, copy.current_board);

            Board new_board = new Board(current_board.size, current_board.tiles, flooded);

            int heuristic_cost = (int) (Math.pow((Math.pow(new_board.size, 2) - flooded.size()), 2));
            int depth_cost = copy.depth;

            State new_state = new State(new_board, copy, copy.depth + 1,
                    heuristic_cost + depth_cost, move);

            searches++;

            fringe.add(new_state);

        }
    }

    public ArrayList<WaterColor> get_moves(Board board) {

        LinkedList<Coord> flooded_list = board.flooded;
        int board_size = board.size;
        Tile[][] tiles = board.tiles;

        HashSet<WaterColor> moves = new HashSet<>();

        Set<Coord> neighbours = new HashSet<>();

        for (Coord c : flooded_list) {
            neighbours.addAll(c.neighbors(board_size));
        }

        for (Coord neighbour : neighbours) {
            int n_x = neighbour.getX();
            int n_y = neighbour.getY();
            WaterColor n_color = tiles[n_y][n_x].getColor();
            moves.add(n_color);
        }
        return new ArrayList<>(moves);
    }

    public int ucs_solver_heuristic(State current_state) {
        return current_state.depth;
    }

}
