package View;

import javafx.scene.image.ImageView;

import static sample.Main.*;

public class Cell {

    private int x;
    private int y;

    ImageView initialImageView;
    ImageView currentImageView;

    public Cell(int x, int y, ImageView initialImageView) {
        super();
        this.setX(x);
        this.setY(y);
        this.initialImageView = initialImageView;
        this.currentImageView = initialImageView;
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