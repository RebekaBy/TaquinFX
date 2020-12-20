package sample;

import Controller.Environnement;
import Controller.Agent;
import View.Cell;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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

    private static int size = 4;
    private static int nbAgents = 7;

    private static int ImageSize = 500;

    //Dimension du taquin
    public static int TILE_ROW_COUNT = size;
    public static int TILE_COLUMN_COUNT = size;
    public static double TILE_SIZE = ImageSize/size;

    public static double offsetX = (SCENE_WIDTH - TILE_ROW_COUNT * TILE_SIZE) / 2;
    public static double offsetY = (SCENE_HEIGHT - TILE_COLUMN_COUNT * TILE_SIZE) / 2;

    private Stage primaryStage;

    //Creation d'une image
  //  private Image image = new Image("\".\\img\\sda.png\"",600,600,false,true);
    private Image image;

    private List<Cell> cells;

    private Environnement env;


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("board.fxml"));

        this.primaryStage = primaryStage;


        try {
            image = new Image(new FileInputStream(".\\img\\sda.png"), ImageSize, ImageSize, false,false);
            //TILE_SIZE =image.getHeight();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        cells = new ArrayList<>();

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

                cells.add(new Cell(x, y, tile, TILE_SIZE, offsetX, offsetY));
            }
        }

        // shuffle cells
//        shuffle();

        //Itialisation de l'environnement
        env = new Environnement(size, nbAgents, cells, this);

        // create playfield
//        Pane pane = new Pane();
//
//        // put tiles on playfield, assign event handler
////        for (int i = 0; i < cells.size(); i++) {
////
////            Cell cell = cells.get(i);
////
////            Node imageView = cell.getImageView();
////
////            // consider empty cell
////            if (imageView == null)
////                continue;
////
////            // position images on scene
////            imageView.relocate(cell.getLayoutX(), cell.getLayoutY());
////
////            pane.getChildren().add(cell.getImageView());
////        }
//
////        //générer récupérer les cells dans l'environnement
////        Platform.runLater(new Runnable(){
////            @Override
////            public void run() {
//                for(int i = 0; i < size; i++){
//                    for(int j = 0; j < size; j++){
//
//                        Node imageView = e.getAgentImage(i, j);
//                        if (imageView == null)
//                            continue;
//
//                        imageView.relocate(TILE_SIZE * i, TILE_SIZE * j);
//                        pane.getChildren().add(imageView);
//
//                    }
//                }
////            }
////        });
//
//        Scene scene = new Scene(pane, SCENE_WIDTH, SCENE_HEIGHT);
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Taquin by BY & COUDURIER-CURVEUR");
//        primaryStage.show();
//
//        primaryStage.show();

        updateView();

        env.runAgents();
    }

    public void updateView(){
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(15, 20, 10, 10));
        pane.setId("pane");

        // BOTTOM
        Button btnPlay = new Button("Play");
        btnPlay.setPadding(new Insets(5, 5, 5, 5));
        pane.setBottom(btnPlay);

        // put tiles on playfield, assign event handler
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){

                Node imageView = env.getAgentImage(i, j);
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
    }



    public static void main(String[] args) {
        launch(args);


    }
}
