import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class GameWindow extends JFrame {
    @FXML
    public Button start = new Button();
    private static GameWindow game_window;
    private static long last_frame_time;
    private static Image background;
    private static Image game_over;
    private static Image dropWater;
    private static Image dropFire;
    private static float drop_left_water = 300;
    private static float drop_top_water = -100;
    private static float drop_v_water = 200;
    private static float drop_left_fire = 200;
    private static float drop_top_fire = -50;
    private static float drop_v_fire = 200;
    private static int scoreWater = 0;
    private static int scoreFire = 0;

    @FXML
    public void mains() throws Exception {

        background = ImageIO.read(GameWindow.class.getResourceAsStream("resources/images/background.png"));
        game_over = ImageIO.read(GameWindow.class.getResourceAsStream("resources/images/game_over.png"));
        dropWater = ImageIO.read(GameWindow.class.getResourceAsStream("resources/images/drop.png"));
        dropFire = ImageIO.read(GameWindow.class.getResourceAsStream("resources/images/dropFire.png"));
        game_window = new GameWindow();
        game_window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        game_window.setLocation(200, 100);
        game_window.setSize(906, 478);
        game_window.setResizable(false);
        game_window.setTitle("Catch the drop");
        last_frame_time = System.nanoTime();
        GameField game_field = new GameField();
        game_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                float drop_right_water = drop_left_water + dropWater.getWidth(null);
                float drop_bottom_water = drop_top_water + dropWater.getHeight(null);
                float drop_right_fire = drop_left_fire + dropFire.getWidth(null);
                float drop_bottom_fire = drop_top_fire + dropFire.getHeight(null);
                boolean is_drop_water = x >= drop_left_water && x <= drop_right_water && y >= drop_top_water && y <= drop_bottom_water;
                boolean is_drop_fire = x >= drop_left_fire && x <= drop_right_fire && y >= drop_top_fire && y <= drop_bottom_fire;
                if (is_drop_water) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        drop_top_water = (int) (Math.random() * (game_field.getHeight() - dropWater.getHeight(null)) * (-0.9));
                        drop_left_water = (int) (Math.random() * (game_field.getWidth() - dropWater.getWidth(null)));
                        drop_v_water = drop_v_water + 20;
                        scoreWater++;
                    }
                }
                if (is_drop_fire) {
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        drop_top_fire = (int) (Math.random() * (game_field.getHeight() - dropFire.getHeight(null)) * (-1.25));
                        drop_left_fire = (int) (Math.random() * (game_field.getWidth() - dropFire.getWidth(null)));
                        drop_v_fire = drop_v_fire + 20;
                        scoreFire++;
                    }
                }
            }
        });
        game_window.add(game_field);
        game_window.setVisible(true);
        game_window.show();
        Stage stage = (Stage) start.getScene().getWindow();
        stage.close();
    }

    private static void onRepaint(Graphics g) {
        long current_time = System.nanoTime();
        float delta_time = (current_time - last_frame_time) * 0.000000001f;
        last_frame_time = current_time;
        drop_top_water = drop_top_water + drop_v_water * delta_time;
        drop_top_fire = drop_top_fire + drop_v_fire * delta_time;
        g.drawImage(background, 0, 0, null);
        g.drawImage(dropWater, (int) drop_left_water, (int) drop_top_water, null);
        g.drawImage(dropFire, (int) drop_left_fire, (int) drop_top_fire, null);
        g.setFont(new Font("Times New Roman", Font.BOLD, 28));
        g.setColor(Color.red);
        int score = scoreFire + scoreWater;
        g.drawString("SCORE: " + score, 10, 40);
        if (drop_top_fire > game_window.getHeight()) {
            g.drawImage(game_over, 280, 120, null);
            drop_top_water = 600;
        }
        if (drop_top_water > game_window.getHeight()) {
            g.drawImage(game_over, 280, 120, null);
            drop_top_fire = 600;
        }
    }

    public void records() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("resources/layouts/records.fxml"));
        Stage stageRecords = new Stage();
        stageRecords.setResizable(false);
        stageRecords.setTitle("R E C O R D S");
        stageRecords.setScene(new Scene(root, 699, 546));
        stageRecords.show();
    }

    public void control() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("resources/layouts/control.fxml"));
        Stage stageControl = new Stage();
        stageControl.setResizable(false);
        stageControl.setTitle("C O N T R O L");
        stageControl.setScene(new Scene(root, 601, 263));
        stageControl.show();
    }

    public void exit() {
        System.exit(0);
    }

    private static class GameField extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            onRepaint(g);
            repaint();
        }
    }
}