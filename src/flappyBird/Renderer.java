package flappyBird;

import java.awt.Graphics;
import javax.swing.JPanel;

public class Renderer extends JPanel {

    private static final long SerialVersionUID = 1L;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        FlappyBird2.flappyBird.repaint(g);
        FlappyBird.flappyBird.repaint(g);
    }
}
