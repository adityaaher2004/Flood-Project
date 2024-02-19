import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class State {

    public final State parent_state;
    public final Board current_board;
    public final int depth;
    public final int tiles_colored;
    public final WaterColor current_move_played;
    public int heuristic_cost;

    public State(Board board, State parent, int depth, int heuristic_cost,
                 WaterColor move) {
        this.current_board = board;
        this.parent_state = parent;
        this.depth = depth;
        this.heuristic_cost = heuristic_cost;
        this.current_move_played = move;
        this.tiles_colored = board.flooded.size();
    }

    public void print_path() {
        System.out.println(this.current_move_played);
        if (parent_state != null) parent_state.print_path();
    }

    public String toString() {

        return current_board + "\n Progress: " + tiles_colored + "\n Steps: " + depth;

    }

    public String getSteps() {
        if (this.parent_state == null) return this.current_move_played.toString();
        return this.current_move_played + " " + parent_state.getSteps();
    }

    public ArrayList<WaterColor> getStepsList() {
        return getStepsListHelper(new ArrayList<WaterColor>());
    }

    public ArrayList<WaterColor> getStepsListHelper(ArrayList<WaterColor> acc) {

        if (this.parent_state == null)
        {
            acc.add(this.current_move_played);
            Collections.reverse(acc);
            return acc;
        }

        acc.add(this.current_move_played);
        return this.parent_state.getStepsListHelper(acc);

    }

    public int solutionStepCount() {
        if (this.parent_state == null) return 1;
        return 1 + parent_state.solutionStepCount();
    }

    public int compareTo(State that) {
        return Integer.compare(this.depth, that.depth);
    }

    public State copy(State o) {

        Board current_board = o.current_board;

        int board_size = current_board.size;
        Tile[][] tiles = current_board.tiles;
        LinkedList<Coord> flooded_list = new LinkedList<>();
        flooded_list.addAll(current_board.flooded);

        Board new_board = new Board(board_size, tiles, flooded_list);

        State parent = o.parent_state;

        int heuristic_cost = o.heuristic_cost;

        int depth = o.depth;

        WaterColor move = o.current_move_played;

        return new State(new_board, parent, depth, heuristic_cost, move);

    }


}
