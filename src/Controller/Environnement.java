package Controller;


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
    }

    public List<Agent> initialisationAgents(int nbAgents){
        Random r = new Random();
        Position p;
        for (int i = 0; i <nbAgents; i++){
            p = new Position(r.nextInt(n), r.nextInt(n));
            listeAgents.add(new Agent(this, p, p));
        }
        return listeAgents;
    }

    public void runAgents(){
        for(Agent a: listeAgents){
            a.start();
//            a.join();
        }
    }

    public Agent getContent(Position p){
        if(p.getX() < 0 || p.getX() >= n || p.getY() < 0 || p.getY() >= n){
            return null;
        }
        return plateau[p.getX()][p.getY()];
    }

    public int getN() {
        return n;
    }

    public boolean isTaquinOk(){
        for(Agent a: listeAgents){
            if(!a.isPlacedGood()){
                return false;
            }
        }
        return true;
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
