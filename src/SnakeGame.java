import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    int boardWidth;
    int boardHeight;
    int tileSize = 25;
    int velocityX = 0;
    int velocityY = 1;
    boolean gameOver = false;
    Tile snakeHead = new Tile(5,5);
    Tile snakeFood = new Tile(10,10);
    Timer gameLoop = new Timer(100, this);
    ArrayList<Tile> snakeBody = new ArrayList<Tile>();

    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Grid Lines
//        for (int i = 0; i < boardWidth/tileSize; i++){
//            // (x1, y1, x2, y2)
//            g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
//            g.drawLine(0,i* tileSize, boardWidth,i*tileSize);
//        }

        // Food
        g.setColor(Color.red);
//        g.fillRect(snakeFood.x * tileSize, snakeFood.y * tileSize, tileSize, tileSize);
        g.fill3DRect(snakeFood.x * tileSize, snakeFood.y * tileSize, tileSize, tileSize, true);

        // Snake Head
        g.setColor(Color.green);
//        g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

        // Snake Body
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
//            g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
        }

        // Score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf((snakeBody.size())),tileSize - 16, tileSize);

        } else {
            g.drawString("Score: " + String.valueOf((snakeBody.size())),tileSize - 16, tileSize);
        }
    }

    public void placeFood() {
        Random random = new Random();
        snakeFood.x = random.nextInt(boardWidth/tileSize); //1000/25 = 40
        snakeFood.y = random.nextInt(boardHeight/tileSize);
    }

    public void move() {
        // Eating the food
        if (collision(snakeHead, snakeFood)) {
            snakeBody.add(new Tile(snakeFood.x, snakeFood.y));
            placeFood();
        }

        // Move snake body
        for(int i = snakeBody.size()-1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;

            } else {
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        // Snake Head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        // game over conditions
        for(int i =0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);

            // collide with the snake head
            if(collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }

        if (snakeHead.x * tileSize < 0 || snakeHead.x * tileSize > boardWidth || snakeHead.y * tileSize < 0 || snakeHead.y * tileSize > boardHeight) {
            gameOver = true;
        }
    }


    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }


    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1){
            velocityX = 0;
            velocityY = -1;

        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;

        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;

        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
