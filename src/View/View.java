package View;

import Controller.Environnement;
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

public class View extends Application {

    private double SCENE_WIDTH = 500;
    private double SCENE_HEIGHT = 500;

    private int gridSize;
    private int nbAgents;

    private int ImageSize;

    //Dimension du taquin
    public int TILE_ROW_COUNT;
    public int TILE_COLUMN_COUNT;
    public double TILE_SIZE;

    public double offsetX = (SCENE_WIDTH - TILE_ROW_COUNT * TILE_SIZE) / 2;
    public double offsetY = (SCENE_HEIGHT - TILE_COLUMN_COUNT * TILE_SIZE) / 2;

    private Stage primaryStage;

    //Creation d'une image
    //  private Image image = new Image("\".\\img\\sda.png\"",600,600,false,true);
    private Image image;

    private List<Cell> cells;
    private Environnement env;

    public View(int gridSize, int nbAgents, String[] args){
        try {
            image = new Image(new FileInputStream(".\\img\\sda.png"), ImageSize, ImageSize, false,false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        cells = new ArrayList<>();

        this.gridSize =gridSize;
        this.nbAgents = nbAgents;

        TILE_ROW_COUNT = gridSize;
        TILE_COLUMN_COUNT = gridSize;
        TILE_SIZE = ImageSize/ gridSize;

    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("board.fxml"));

        this.primaryStage = primaryStage;

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
//        env = new Environnement(gridSize, nbAgents, cells);

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
//
//
//
//
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
        Pane pane = new Pane();

        // put tiles on playfield, assign event handler
        for(int i = 0; i < gridSize; i++){
            for(int j = 0; j < gridSize; j++){

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
}
