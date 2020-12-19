package Controller;


import View.Cell;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import sample.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Environnement {
    private Agent[][] plateau;
    private int n;
    private int nbAgents;
    private List<Agent> listeAgents;
    private Semaphore semaphore;

    private List<Cell> cells;

    public Main app;


    public Environnement(int n, int nbAgents, List<Cell> cells, Main app){
        this.n = n;
        this.nbAgents = nbAgents;

        plateau = new Agent[n][n];
        listeAgents = new ArrayList<>();
        semaphore = new Semaphore(1);
        this.cells = cells;
        initPlateau();
        initialisationAgents();
        this.app = app;
    }

    public void initPlateau(){
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                plateau[i][j] = null;
            }
        }
    }

    public void initialisationAgents(){
        Random r = new Random();
        Position p, pFinal;
        Agent a;
        for (int i = 0; i <nbAgents; i++){
            p = new Position(r.nextInt(n), r.nextInt(n));
            while(plateau[p.getX()][p.getY()] != null){
                p = new Position(r.nextInt(n), r.nextInt(n));
            }
            pFinal = new Position(i%n, i/n);
            Cell agentCell = cells.get(0);
            for(Cell c: cells){
                if(c.getX() == pFinal.getX() && c.getY() == pFinal.getY()){
                    agentCell = c;
                }
            }
            a = new Agent(i, this, p, pFinal, agentCell);
            listeAgents.add(a);
            plateau[p.getX()][p.getY()] = a;

        }
    }

    public void runAgents(){
//        try {
            for(Agent a: listeAgents){
                a.start();
//                a.join();
            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    public Agent getContent(Position p){
        try {
            semaphore.acquire();
            if(p.getX() < 0 || p.getX() >= n || p.getY() < 0 || p.getY() >= n){
                semaphore.release();
                return null;
            }
            Agent content = plateau[p.getX()][p.getY()];
            semaphore.release();
            return content;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Agent getContent(Agent a, Direction d){
        Position p = calcPosition(a.getPositionCurrent(), d);
        return getContent(p);
    }

    public int getN() {
        return n;
    }

    public boolean isTaquinOk(){
        try {
            semaphore.acquire();
            for(Agent a: listeAgents){
                if(!a.isPlacedGood()){
                    semaphore.release();
                    return false;
                }
            }
            System.out.println("OKTAQUINOK");
            semaphore.release();
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deplacer(Agent a, Direction d){
        try {
            semaphore.acquire();
            Position p = a.getPositionCurrent();

            plateau[p.getX()][p.getY()] = null;

            Position newP = calcPosition(p,d);

            a.setPositionCurrent(newP);

            System.out.println("Deplacement agent : " + a.gettId() + " - iteration : " + a.getDate() + " pInit : " + p + " - Direction : " + d + " - pNew : " + newP);

            plateau[newP.getX()][newP.getY()] = a;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    app.updateView();
                }
            });
            semaphore.release();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    }

    //Détermine la position de l'agent apres déplacement dans la direction
    public Position calcPosition(Position p, Direction d){
        switch (d){
            case E:
                return new Position(p.getX(), p.getY()+1);
            case O:
                return new Position(p.getX(), p.getY()-1);
            case N:
                return new Position(p.getX()-1, p.getY());
            default:
                return new Position(p.getX()+1, p.getY());
        }
    }

    public Boolean isPositionInside(Position p){
        return (p.getX() >= 0 && p.getX() < n && p.getY() >= 0 && p.getY() < n);
    }

    public ImageView getAgentImage(int x, int y){
        if(plateau[x][y] == null){
            return null;
        }
        return plateau[x][y].getCell().getImageView();
    }
}
