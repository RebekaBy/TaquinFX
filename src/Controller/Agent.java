package Controller;


import Astar.Astar;
import View.Cell;

import java.security.PrivateKey;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.SortedSet;
import java.util.concurrent.Semaphore;

public class Agent extends Thread{
    private Environnement e;
    private int id;

    private Direction d;
    private int date;

    private Position positionCurrent, positionFinal;
    private Queue<Message> boiteAuxLettres;
    private Cell cell;
    private Boolean bouge;
    private Semaphore semaphore;


    public Agent(int id, Environnement e, Position positionCurrent, Position positionFinal, Cell c) {
        this.e = e;
        this.id = id;
        this.positionCurrent = positionCurrent;
        this.positionFinal = positionFinal;
        date= 1;
        boiteAuxLettres = new LinkedList<>();
        this.cell = c;
        bouge = false;
        semaphore = new Semaphore(1);
    }

    /*
    Après avoir trouver la case grâce à raisonner ET après avoir vérifier si la case est libre,
    on se déplace vers la case choisie
    */
    public void seDeplacer() {
//        System.out.println("Agent " + id + " - Iteration : " + date + " - Deplace ! ");
        e.deplacer(this, d);
        clearBoite();

    }

    //Observe l'environnement (en haut, à droite, à gauche,
    public void observer(){

    }

    public void envoyerMessage(Agent a){
//        System.out.println("envoyer message");
        a.addBoiteAuxLettres(new Message(this, "Request", a.getDate(), "Move", ""));
    }

    //TODO LOUIS AVEC A*
    // Utilisation de A* pour savoir quel est le meilleur chemin pour aller à la position final
//    public Direction raisonner(){
//        System.out.println("raisonner");
//        Astar as = new Astar();
//        Position nextPosition = as.cheminPlusCourt(e, this, true);
//        if(nextPosition.equals(positionCurrent)){
//            nextPosition = as.cheminPlusCourt(e, this, false);
//            Agent a = e.getContent(nextPosition);
//            if(a != null){
//                a.addBoiteAuxLettres(new Message(this, "Request", a.getDate(), "Move", ""));
//            }
//        }
//        else{
//            return findDirection(nextPosition);
//        }
//        return null;
//    }

    public void raisonner(){
        if(d == null){
            if(isPlacedGood()){
                d = lireMessages();
            }else{
                d = findDirection(getAstarNextPosition());
                if(d == null){
                    d = lireMessages();
                }
            }
        }
    }

    public Position getAstarNextPosition(){
        Astar as = new Astar();
        Position astarNextPosition = as.cheminPlusCourt(e, this, true);
        if(astarNextPosition.equals(positionCurrent)){

            astarNextPosition = as.cheminPlusCourt(e, this, false);
            Agent a = e.getContent(astarNextPosition);
            if(a != null){
//                System.out.println("envoie message");
                envoyerMessage(a);
                return null;
            }
//            System.out.println("return null");
            return astarNextPosition;
        }
        return astarNextPosition;
    }

    public void decider(){
//        System.out.println(id + " - " + positionCurrent + " - Direction " + d );

        if(d != null){
            Agent content = e.getContent(this, d);
            if (content == null) {
                seDeplacer();
                d = null;
            }else{
                envoyerMessage(content);
            }
        }
    }

    public Direction lireMessages(){
        try {
            semaphore.acquire();
            for(Message m : boiteAuxLettres){
                if(m.getAction().equals("Move")){
                    semaphore.release();
                    return findCaseVoisine(m.getEmmeteur());
                }
            }
            boiteAuxLettres = new LinkedList<>();
            semaphore.release();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void clearBoite(){
        try {
            semaphore.acquire();
            boiteAuxLettres = new LinkedList<>();
            semaphore.release();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public Direction findCaseVoisine(Agent a){
//        Position tmpPosition;
        //cherche un case libre parmis les caes voisines
        for(Direction direction: Direction.values()){
//            tmpPosition = e.calcPosition(positionCurrent, direction);
            if(isCaseDisponible(this, direction)){
                return direction;
            }
        }
        Agent agentCible;
        //cherche un agent mal placé parmis les agents voisins
        for(Direction direction: Direction.values()){
//            tmpPosition = e.calcPosition(positionCurrent, direction);
            agentCible = e.getContent(this, direction);
            if(agentCible != null && !agentCible.equals(a)) {
                if (!agentCible.isPlacedGood()) {
                    return direction;
                }
            }
        }

        //retourne un agent au pif
        Direction rand;
        Boolean directionOk;
//        tmpPosition = e.calcPosition(positionCurrent, rand);;
        do {
            rand = Direction.values()[(int)(Math.random()*Direction.values().length)];
            directionOk = false;
            if(e.isPositionInside(e.calcPosition(positionCurrent, rand))) {
                agentCible = e.getContent(this, rand);
                if (agentCible == null) {
                    directionOk = true;
                } else {
                    if (!agentCible.equals(a)) {
                        directionOk = true;
                    }
                }
            }
        }
        while(!directionOk);
        return rand;
    }

    public Boolean isCaseDisponible(Agent a, Direction d){
        Position p = e.calcPosition(a.getPositionCurrent(), d);
        if(!e.isPositionInside(p)){
            return false;
        }
        if(!p.equals(a.positionCurrent)){
            return e.getContent(p) == null;
        }
        return false;
    }


    @Override
    public void run() {
        while(!e.isTaquinOk()){

//            System.out.println("Agent " + id + " - Iteration : " + date);
//            decider();
            raisonner();
            decider();
            date++;
            try {
                Thread.sleep(50);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
    }

    public Direction findDirection(Position p){
        if(p == null || p.equals(positionCurrent)){
            return null;
        }
        if(p.getX() < positionCurrent.getX()){
            return Direction.N;
        }
        if(p.getX() > positionCurrent.getX()){
            return Direction.S;
        }
        if(p.getY() < positionCurrent.getY()){
            return Direction.O;
        }else{
            return Direction.E;
        }
    }

    public Position getPositionCurrent() {
        return positionCurrent;
    }

    public void setPositionCurrent(Position positionCurrent) {
        this.positionCurrent = positionCurrent;
    }

    public Position getPositionFinal() {
        return positionFinal;
    }

    public void setPositionFinal(Position positionFinal) {
        this.positionFinal = positionFinal;
    }

    public void addBoiteAuxLettres(Message m){
        try {
            semaphore.acquire();
            boiteAuxLettres.add(m);
            semaphore.release();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public int getDate() {
        return date;
    }

    //Vérifie si y'a un agent ou pas sur la position voulue
    public boolean isCaseLibre(Environnement e,Position p){
        Agent a = null;
        //Récupère si y'a un agent à la position p
        a = e.getContent(p);

        if (a == null){
            //La case est libre
            return true;
        }else{
            //La case est occupée
            return false;
        }
    }

    public boolean isPlacedGood(){
        return positionCurrent.equals(positionFinal);
    }

    public Queue<Message> getBoiteAuxLettres() {
        return boiteAuxLettres;
    }

    public void setBoiteAuxLettres(Queue<Message> boiteAuxLettres) {
        this.boiteAuxLettres = boiteAuxLettres;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agent agent = (Agent) o;
        return id == agent.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Cell getCell() {
        return cell;
    }


    public int gettId() {
        return id;
    }
}
