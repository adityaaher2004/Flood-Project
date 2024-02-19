// Imports for the parameters of flood

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;


public class Flood {

    static double flood = 0;

    static double flood1 = 0;

    // Students implement this flood function.
    // Time complexity : O(n^2)
    public static void flood(WaterColor color, LinkedList<Coord> flooded_list,
                             Tile[][] tiles, Integer board_size) {
        flooded_list = recurve_flooding_TR(color, flooded_list, tiles, board_size);

        // System.out.println("Number of non-boundary tiles : " + non_boundary_tiles.size());
        // System.out.println("Number of boundary tiles : " + boundary_tiles.size());
        // System.out.println("Boundary and non-boundary tiles : " + (boundary_tiles.size() + non_boundary_tiles.size()));
        // System.out.println("Actual evaluated tiles : " + evaluated_tiles.size());
        // System.out.println("Total evaluated tiles : " + flood);
        // System.out.println(flooded_list);
    }

    public static LinkedList<Coord> recurve_flooding_TR(WaterColor color, LinkedList<Coord> flooded_list,
                                                        Tile[][] tiles, Integer board_size) {

        return recurve_flooding_helper(color, flooded_list, tiles, board_size, flooded_list);

    }

    private static LinkedList<Coord> recurve_flooding_helper(WaterColor color, LinkedList<Coord> flooded_list,
                                                             Tile[][] tiles, Integer board_size,
                                                             LinkedList<Coord> acc) {
        if (flooded_list.isEmpty()) {

            // System.out.println(calls);
            // System.out.println(acc);

            HashSet<Coord> final_list = new HashSet<>(acc);
            acc.clear();
            acc.addAll(final_list);
            return acc;
        }

        LinkedList<Coord> new_tiles = new LinkedList<>();

        for (Coord c : flooded_list) {

            for (Coord neighbour : c.neighbors(board_size)) {
                int n_x = neighbour.getX();
                int n_y = neighbour.getY();
                WaterColor n_color = tiles[n_y][n_x].getColor();
                flood++;
                if (n_color.equals(color) && !acc.contains(neighbour))
                    new_tiles.add(neighbour);
            }

        }

        acc.addAll(new_tiles);

        return recurve_flooding_helper(color, new_tiles, tiles, board_size, acc);

    }

    // An alternative implementation goes here.
    public static void flood1(WaterColor color, LinkedList<Coord> flooded_list,
                              Tile[][] tiles, Integer board_size) {

        flooded_list = recurve_flooding_TR_optimized(color, flooded_list, tiles, board_size);

        // System.out.println("Number of non-boundary tiles : " + non_boundary_tiles.size());
        // System.out.println("Number of boundary tiles : " + boundary_tiles.size());
        // System.out.println("Boundary and non-boundary tiles : " + (boundary_tiles.size() + non_boundary_tiles.size()));
        // System.out.println("Actual evaluated tiles : " + evaluated_tiles.size());
        // System.out.println("Total evaluated optimized tiles : " + flood1);

    }

    public static LinkedList<Coord> recurve_flooding_TR_optimized(WaterColor color, LinkedList<Coord> flooded_list,
                                                                  Tile[][] tiles, Integer board_size) {

        return recurve_flooding_helper_optimized(color, flooded_list, tiles, board_size, flooded_list);

    }

    private static LinkedList<Coord> recurve_flooding_helper_optimized(WaterColor color, LinkedList<Coord> flooded_list,
                                                                       Tile[][] tiles, Integer board_size,
                                                                       LinkedList<Coord> acc) {

        if (flooded_list.isEmpty()) {

            return acc;

        }

        Set<Coord> neighbours = new HashSet<>();

        for (Coord c : flooded_list) {

            neighbours.addAll(c.neighbors(board_size));

        }

        Set<Coord> new_tiles = new HashSet<>();

        for (Coord neighbour : neighbours) {

            int n_x = neighbour.getX();
            int n_y = neighbour.getY();
            WaterColor n_color = tiles[n_y][n_x].getColor();
            flood1++;
            if (n_color.equals(color) && !acc.contains(neighbour)) {
                new_tiles.add(neighbour);
                tiles[n_y][n_x].setColor(color);
            }

        }

        acc.addAll(new_tiles);

        return recurve_flooding_helper_optimized(color, new LinkedList<>(new_tiles), tiles, board_size, acc);
    }

    public static LinkedList<Coord> flood(WaterColor color, Board board) {
        Tile[][] tiles = board.tiles;
        int board_size = board.size;

        LinkedList<Coord> flooded_list = board.flooded;

        return recurve_flooding_TR_optimized_with_tiles(color, flooded_list, tiles, board_size);
    }

    public static LinkedList<Coord> recurve_flooding_TR_optimized_with_tiles(WaterColor color, LinkedList<Coord> flooded_list,
                                                                  Tile[][] tiles, Integer board_size) {

        return recurve_flooding_helper_optimized_with_tiles(color, flooded_list, tiles, board_size, flooded_list);

    }

    private static LinkedList<Coord> recurve_flooding_helper_optimized_with_tiles(WaterColor color, LinkedList<Coord> flooded_list,
                                                                       Tile[][] tiles, Integer board_size,
                                                                       LinkedList<Coord> acc) {

        if (flooded_list.isEmpty()) {

            return acc;

        }

        Set<Coord> neighbours = new HashSet<>();

        for (Coord c : flooded_list) {

            neighbours.addAll(c.neighbors(board_size));

        }

        Set<Coord> new_tiles = new HashSet<>();

        for (Coord neighbour : neighbours) {

            int n_x = neighbour.getX();
            int n_y = neighbour.getY();
            WaterColor n_color = tiles[n_y][n_x].getColor();
            flood1++;
            if (n_color.equals(color) && !acc.contains(neighbour)) {
                new_tiles.add(neighbour);
                tiles[n_y][n_x].setColor(color);
            }

        }

        acc.addAll(new_tiles);

        return recurve_flooding_helper_optimized(color, new LinkedList<>(new_tiles), tiles, board_size, acc);
    }

}
