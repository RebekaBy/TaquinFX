package Controller;


import Astar.Astar;

import java.security.PrivateKey;
import java.util.Objects;
import java.util.Queue;

public class Agent extends Thread{
    private Environnement e;
    private int id;

    private Direction d;
    private int date;

    private Position positionCurrent, positionFinal;
    private Queue<Message> boiteAuxLettres;


    public Agent(Environnement e, Position positionCurrent, Position positionFinal) {
        this.e = e;
        this.positionCurrent = positionCurrent;
        this.positionFinal = positionFinal;
        date= 0;
    }

    /*
    Après avoir trouver la case grâce à raisonner ET après avoir vérifier si la case est libre,
    on se déplace vers la case choisie
    */
    public void seDeplacer(){
        e.deplacer(this, d);
    }

    //Observe l'environnement (en haut, à droite, à gauche,
    public void observer(){

    }

    //TODO LOUIS AVEC A*
    // Utilisation de A* pour savoir quel est le meilleur chemin pour aller à la position final
    public Direction raisonner(){
        Astar as = new Astar();
        Position nextPosition = as.cheminPlusCourt(e, this, true);
        if(nextPosition.equals(positionCurrent)){
            nextPosition = as.cheminPlusCourt(e, this, false);
            Agent a = e.getContent(nextPosition);
            if(a != null){
                a.addBoiteAuxLettres(new Message(this, "Request", a.getDate(), "Move", ""));
            }
        }
        else{
            return findDirection(nextPosition);
        }
        return null;
    }

    public void decider(){
        if(isPlacedGood()){
            Position tmpPosition = lireMessages();
            if(tmpPosition == null){
                d = null;
            }else{
                Agent a = e.getContent(tmpPosition);
                if(a != null){
                    a.addBoiteAuxLettres(new Message(this, "Request", a.getDate(), "Move", ""));
                    d = null;
                }else{
                    d = findDirection(tmpPosition);
                }
            }
        }else{
            d = raisonner();
        }
        boiteAuxLettres.clear();
        if(d != null){
            seDeplacer();
        }
    }

    public Position lireMessages(){
        for(Message m : boiteAuxLettres){
            if(m.getAction().equals("Move")){
                return findCaseVoisine(m.getEmmeteur());
            }
        }
        return null;
    }

    public Position findCaseVoisine(Agent a){
        Position tmpPosition;
        for(Direction direction: Direction.values()){
            tmpPosition = e.calcPosition(positionCurrent, direction);
            if(isCaseDisponible(tmpPosition, a)){
                return tmpPosition;
            }
        }

        Direction rand = Direction.values()[(int)(Math.random()*Direction.values().length)];
        tmpPosition = e.calcPosition(positionCurrent, rand);;
        while(!isCaseDisponible(tmpPosition, a)){
            rand = Direction.values()[(int)(Math.random()*Direction.values().length)];
            tmpPosition = e.calcPosition(positionCurrent, rand);;
        }
        return tmpPosition;
    }

    public Boolean isCaseDisponible(Position p, Agent a){
        if(!p.equals(a.positionCurrent)){
            return e.getContent(p) == null;
        }
        return false;
    }

    public void appliquer(){

    }

    @Override
    public void run() {
        while(!e.isTaquinOk()){
            d = null;
            decider();
        }
    }

    public Direction findDirection(Position p){
        if(p.equals(positionCurrent)){
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
        boiteAuxLettres.add(m);
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
}
