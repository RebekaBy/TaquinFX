package View;

import javafx.scene.image.ImageView;

import java.util.Timer;

import static sample.Main.*;

public class Cell {

    private int x;
    private int y;

    private ImageView initialImageView;
    private ImageView currentImageView;

    private double TILE_SIZE;
    private double offsetX;
    private double offsetY;

    public Cell(int x, int y, ImageView initialImageView, double TILE_SIZE, double offsetX, double offsetY) {
        super();
        this.setX(x);
        this.setY(y);
        this.initialImageView = initialImageView;
        this.currentImageView = initialImageView;

        this.TILE_SIZE = TILE_SIZE;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public double getLayoutX() {
        return getX() * TILE_SIZE + offsetX;
    }

    public double getLayoutY() {
        return getY() * TILE_SIZE + offsetY;
    }

    public ImageView getImageView() {
        return currentImageView;
    }

    public void setImageView(ImageView imageView) {
        this.currentImageView = imageView;
    }

    public boolean isEmpty() {
        return currentImageView == null;
    }

    public boolean isSolved() {
        return this.initialImageView == currentImageView;
    }

    public String toString() {
        return "[" + getX() + "," + getY() + "]";
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}