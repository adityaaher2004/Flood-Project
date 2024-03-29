import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Builds the gui interface for an interactive run of the game.
 */

public class GUI extends JFrame {
    // Boilerplate setup.
    static {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    private JPanel grid;
    private JMenuBar menuBar = new JMenuBar();
    private JMenu steps = new JMenu();
    public Board board;
    public Game game;

    private String player_name;

    /**
     * Creates a gui to view the given game.
     */
    public GUI(Game game) {
        setTitle(player_name);
        this.game = game;
        board = game.getBoard();
        int size = board.getSize();

        grid = new JPanel(new GridLayout(size, size));
        for (int x = 0; x < size; x++)
            for (int y = 0; y < size; y++)
                grid.add(new TileButton(board.get(new Coord(x, y))));

        setSteps();
        setTitle(player_name);
        initMenu();
        setContentPane(grid);
        setPreferredSize(new Dimension(640, 640));
        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Sets up the menu.
     */
    private void initMenu() {

        JMenu gameMenu = new JMenu("Game");
        gameMenu.setMnemonic(KeyEvent.VK_G);
        menuBar.add(gameMenu);

        JMenuItem restart = new JMenuItem("Restart");
        restart.addActionListener(e -> game.resize(board.getSize()));
        gameMenu.add(restart);

        JMenuItem resize = new JMenuItem("Resize");
        resize.addActionListener(e -> {
            String msg = JOptionPane.showInputDialog("Please type in the size");
            game.resize(Integer.parseInt(msg));
        });
        gameMenu.add(resize);

        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> dispose());
        gameMenu.add(exit);

        JMenu help = new JMenu("Help");
        help.setMnemonic(KeyEvent.VK_H);
        menuBar.add(help);

        JMenuItem rules = new JMenuItem("HowToPlay");
        rules.addActionListener(e -> JOptionPane.showMessageDialog(this, Constants.HINT));
        help.add(rules);

        JMenuItem suggest = new JMenuItem("Suggest");
        suggest.addActionListener(e -> JOptionPane.showMessageDialog(this, board.suggest()));
        help.add(suggest);

        menuBar.add(steps);
        setJMenuBar(menuBar);
    }

    /**
     * Updates the display to show the number of steps used by the player so far.
     */
    public void setSteps() {
        steps.setText(game.getSteps() + "/" + game.getStepLimit());
    }

    public void hideGame(){
        setVisible(false);
    }

    public void PlayerButton(WaterColor move){
        TileButton tileButton = new TileButton(this.board.tiles[0][0]);
        game.select(move);
        // Update the view
        setSteps();
        GUI.this.repaint();
        if (this.board.fullyFlooded())
            tileButton.youWinNoRestart();
        else if (this.game.noMoreSteps())
            tileButton.youLoseNoRestart();
    }

    /**
     * An inner class to represent the physical manifestation of a Tile on a game board.
     * These are clickable objects, so we'll take advantage of the functionality already
     * implemented by JButton.
     */

    class TileButton extends JButton {
        private Tile tile;

        public TileButton(Tile tile) {
            this.tile = tile;
            // Add a listener to process player moves and deal with game win/lose conditions.
            addMouseListener(new MouseInputAdapter() {
                public void mouseClicked(MouseEvent e) {
                    // Update the model
                    game.select(tile.getColor());
                    // Update the view
                    setSteps();
                    GUI.this.repaint();
                    if (board.fullyFlooded())
                        youWin();
                    else if (game.noMoreSteps())
                        youLose();
                }
            });
        }


        /**
         * Draws the tile on this button.
         */
        public void paintComponent(Graphics gr) {
            setBackground(tile.getColor().get());
            super.paintComponent(gr);
        }

        /**
         * Handles a game loss condition.
         */
        public void youLose() {
            JOptionPane.showMessageDialog(GUI.this, player_name +" lose");
            game.resize(board.getSize());
        }

        /**
         * Handles a game win condition.
         */
        public void youWin() {
            JOptionPane.showMessageDialog(GUI.this, player_name + " Win!");
            game.resize(board.getSize());
        }

        public void youWinNoRestart() {
            JOptionPane.showMessageDialog(GUI.this, player_name + " Win!");
            dispose();
        }

        public void youLoseNoRestart() {
            JOptionPane.showMessageDialog(GUI.this, player_name + " Lose!");
            dispose();
        }
    }
}
