package flappyBird;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

public class FlappyBird implements ActionListener, MouseListener, KeyListener {

    public static FlappyBird flappyBird;
    public final int WIDTH = 800;
    public final int HEIGHT = 800;
    public int ticks, yMotion, score = 0, bestScore = 0;

    public Renderer renderer;
    public Random rand;
    public Rectangle bird;
    public ArrayList<Rectangle> columns;
    public boolean gameOver;
    public boolean started;

    public FlappyBird() {
        JFrame jFrame = new JFrame("Latający Ptak");
        Timer timer = new Timer(20, this);

        renderer = new Renderer();
        rand = new Random();

        jFrame.add(renderer);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.addMouseListener(this);
        jFrame.addKeyListener(this);
        jFrame.setSize(WIDTH, HEIGHT);
        jFrame.setVisible(true);
        jFrame.setLocationRelativeTo(null);
        jFrame.setResizable(false);

        bird = new Rectangle(WIDTH / 2 - 10, 2 - 10, 20, 20);
        columns = new ArrayList<>();

        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);

        timer.start();
    }

    public void addColumn(boolean start) {
        int space = 300;
        int width = 100;
        int height = 50 + rand.nextInt(300);

        if (start) {
            columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
        } else {
            columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
        }


    }


    public void paintColumn(Graphics2D g, Rectangle column) {
        g.setColor(new Color(89, 53, 9));
        g.fillRect(column.x, column.y, column.width, column.height);
    }


    public void jump() {
        if (gameOver) {
            bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
            columns.clear();
            yMotion = 0;
            score = 0;

            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);

            gameOver = false;
        }

        if (!started) {
            started = true;
        } else {
            if (yMotion > 0) {
                yMotion = 0;
            }
            yMotion -= 10;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int speed = 10;
        ticks++;

        if (started) {
            for (Rectangle column : columns) {
                column.x -= speed;
            }

            if (ticks % 2 == 0 && yMotion < 15) {
                yMotion += 2;
            }

            for (int i = 0; i < columns.size(); i++) {
                Rectangle column = columns.get(i);
                if (column.x + column.width < 0) {
                    columns.remove(column);
                    if (column.y == 0) {
                        addColumn(false);
                    }
                }
            }


            bird.y += yMotion;

            for (Rectangle column : columns) {

                if (column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - 10 && bird.x + bird.width / 2 < column.x + column.width / 2 + 10) {
                    score++;
                    if (score >= bestScore) bestScore = score;
                }

                if (column.intersects(bird)) {
                    gameOver = true;
                    if (bird.x <= column.x) {
                        bird.x = column.x - bird.width;

                    } else {
                        if (column.y != 0) {
                            bird.y = column.y - bird.height;
                        } else if (bird.y < column.height) {
                            bird.y = column.height;
                        }
                    }
                }
            }

            if (bird.y > HEIGHT - 120 || bird.y < 0) {
                gameOver = true;
            }
            if (bird.y + yMotion >= HEIGHT - 120) {
                bird.y = HEIGHT - 120 - bird.height;
                gameOver = true;
            }
        }
        renderer.repaint();
    }

    public void repaint(Graphics2D g) {
        g.setColor(new Color(165, 93, 4));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(new Color(89, 53, 9));
        g.fillRect(0, HEIGHT - 120, WIDTH, 120);
        g.setColor(new Color(242, 212, 186));
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Najlepszy wynik: " + bestScore, 20, HEIGHT - 75);

        g.setColor(new Color(216, 113, 54));
        g.fillRect(0, HEIGHT - 120, WIDTH, 20);


        g.setColor(new Color(242, 212, 186));
        g.fillRect(bird.x, bird.y, bird.width, bird.height);

        for (Rectangle column : columns) {
            paintColumn(g, column);
        }

        g.setColor(new Color(242, 212, 186));
        g.setFont(new Font("Arial", Font.BOLD, 100));

        if (!started) {
            g.drawString("Kliknij!", 75, HEIGHT / 2 - 50);
        }

        if (gameOver) {
            g.drawString("Upss!", 100, HEIGHT / 2 - 50);
        }

        if (!gameOver & started) {
            g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
        }
    }

    public static void main(String[] args) {
        flappyBird = new FlappyBird();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        jump();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            jump();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
