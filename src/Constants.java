/**
 * Global constants.
 */

public class Constants {
  public static String TITLE = "Flood It Project";
  public static final String HINT = "Initially, the tile in the upper left corner is flooded. Clicking a tile recolors\nthe flooded region with the selected color, then floods all tiles adjacent\nto the flooded region that are in the selected color.\n\nThe player wins when all tiles are flooded, and loses when the step\nlimit is reached.\n";

  public static final int MAX_BOARD_SIZE_FOR_AUTOPLAY = 10000;
  public static final int SIZE_INC_FOR_AUTOPLAY = 500;
  public static final int NUM_GAMES_TO_AUTOPLAY = 2;
  public static final int MAX_DIM = 101;
  public static final String TEST_GRAPH = "results.png";

  public static final int DEFAULT_SIZE = 30;

  public static final long MILLISECONDS_IN_SECONDS = 1000;
  public static final long MICROSECONDS_IN_SECONDS = 1000000;
  public static final long NANOSECONDS_IN_SECONDS = 1000000000;

}

