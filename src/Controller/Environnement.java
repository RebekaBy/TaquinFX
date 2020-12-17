package Controller;


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


    public Environnement(int n, int nbAgents){
        this.n = n;
        this.nbAgents = nbAgents;

        plateau = new Agent[n][n];
        listeAgents = new ArrayList<>();
        semaphore = new Semaphore(1);
        initPlateau();
        initialisationAgents();
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
            pFinal = new Position(i%n, i/n);
            a = new Agent(i, this, p, pFinal);
            listeAgents.add(a);
            plateau[p.getX()][p.getY()] = a;
        }
    }

    public void runAgents(){
        for(Agent a: listeAgents){
            a.start();
//            a.join();
        }
    }

    public Agent getContent(Position p){
        try {
            semaphore.acquire();
            if(p.getX() < 0 || p.getX() >= n || p.getY() < 0 || p.getY() >= n){
                semaphore.release();
                return null;
            }
            semaphore.release();
            return plateau[p.getX()][p.getY()];
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
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

            plateau[newP.getX()][newP.getY()] = a;
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
}
