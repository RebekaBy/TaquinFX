package sample;

import Controller.Environnement;
import Controller.Agent;
import View.Cell;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends Application {

    private static double SCENE_WIDTH = 500;
    private static double SCENE_HEIGHT = 500;

    private static int size = 5;
    private static int nbAgents = 5;

    //Dimension du taquin
    public static int TILE_ROW_COUNT = size;
    public static int TILE_COLUMN_COUNT = size;
    public static double TILE_SIZE = 100;

    public static double offsetX = (SCENE_WIDTH - TILE_ROW_COUNT * TILE_SIZE) / 2;
    public static double offsetY = (SCENE_HEIGHT - TILE_COLUMN_COUNT * TILE_SIZE) / 2;

    //Creation d'une image
  //  private Image image = new Image("\".\\img\\sda.png\"",600,600,false,true);
    Image image;
    {
        try {
            image = new Image(new FileInputStream(".\\img\\sda.png"), 500, 500, false,false);
            //TILE_SIZE =image.getHeight();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    List<Cell> cells = new ArrayList<>();


    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("board.fxml"));


        // create grid
        for (int x = 0; x < TILE_ROW_COUNT; x++) {
            for (int y = 0; y < TILE_COLUMN_COUNT; y++) {

                // create tile
                ImageView tile = new ImageView(image);
                Rectangle2D rect = new Rectangle2D(TILE_SIZE * x, TILE_SIZE * y, TILE_SIZE, TILE_SIZE);
                tile.setViewport(rect);

                // consider empty cell, let it remain empty
//                if (x == (TILE_ROW_COUNT - 1) && y == (TILE_COLUMN_COUNT - 1)) {
//                    tile = null;
//                }

                cells.add(new Cell(x, y, tile));
            }
        }

        // shuffle cells
//        shuffle();

        //Itialisation de l'environnement
        Environnement e = new Environnement(size, nbAgents, cells);

        // create playfield
        Pane pane = new Pane();

//        // put tiles on playfield, assign event handler
//        for (int i = 0; i < cells.size(); i++) {
//
//            Cell cell = cells.get(i);
//
//            Node imageView = cell.getImageView();
//
//            // consider empty cell
//            if (imageView == null)
//                continue;
//
//            // position images on scene
//            imageView.relocate(cell.getLayoutX(), cell.getLayoutY());
//
//            pane.getChildren().add(cell.getImageView());
//        }

        //générer récupérer les cells dans l'environnement

        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){

                Node imageView = e.getAgentImage(i, j);
                if (imageView == null)
                    continue;

                imageView.relocate(TILE_SIZE * i, TILE_SIZE * j);
                pane.getChildren().add(imageView);

            }
        }


        Scene scene = new Scene(pane, SCENE_WIDTH, SCENE_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Taquin by BY & COUDURIER-CURVEUR");
        primaryStage.show();

        primaryStage.show();


        e.runAgents();
    }

    /**
     * Swap images of cells randomly
     */
    public void shuffle() {

        Random rnd = new Random();

        for (int i = 0; i < 1000; i++) {

            int a = rnd.nextInt(cells.size());
            int b = rnd.nextInt(cells.size());

            if (a == b)
                continue;

            // skip bottom right cell swap, we want the empty cell to remain there
            if( cells.get(a).isEmpty() || cells.get(b).isEmpty())
                continue;

            swap( cells.get(a), cells.get(b));

        }

    }

    public void swap( Cell cellA, Cell cellB) {

        ImageView tmp = cellA.getImageView();
        cellA.setImageView(cellB.getImageView());
        cellB.setImageView(tmp);

    }



    public static void main(String[] args) {
        launch(args);


    }
}
