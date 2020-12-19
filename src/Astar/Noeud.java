package Astar;


import Controller.Position;

import java.util.Map;
import java.util.Objects;

public class Noeud implements Comparable{
    private int x, y;
    private int heuristique;
    private int cout;
    private int distance;
    private Noeud parent;

    public Noeud(Position p, int d, int c) {
        this.x = p.getX();
        this.y = p.getY();
        this.distance = d;
        this.heuristique = d + c;
        this.cout = c;
        parent = null;
    }

    public Noeud(Position p, Noeud n, int c){
        this.x = p.getX();
        this.y = p.getY();
        distance = calcDistance(n);
        heuristique = distance + c;
        cout = c;
        parent = null;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public int calcDistance(Noeud n){
        return Math.abs(x - n.getX()) + Math.abs(y - n.getY());
    }

    public int getHeuristique() {
        return heuristique;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getCout() {
        return cout;
    }

    public Noeud getParent() {
        return parent;
    }

    public void setCout(int cout) {
        this.cout = cout;
    }

    public void setHeuristique(int heuristique) {
        this.heuristique = heuristique;
    }

    public void setParent(Noeud parent) {
        this.parent = parent;
    }

    public Position getPosition(){
        return new Position(x, y);
    }

    @Override
    public int compareTo(Object o) {
        Noeud n = (Noeud)o;
        if(this.getHeuristique() < n.getHeuristique()){
            return 1;
        }else if(this.getHeuristique() == n.getHeuristique()){
            return 0;
        }else{
            return -1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Noeud noeud = (Noeud) o;
        return x == noeud.x &&
                y == noeud.y;
    }

    public Boolean positionEquals(Noeud n){
        return x == n.x &&
                y == n.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, heuristique, cout);
    }
}
