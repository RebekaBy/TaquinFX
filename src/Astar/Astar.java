package Astar;


import Controller.Agent;
import Controller.Environnement;
import Controller.Position;

import java.util.*;

public class Astar {
    private Environnement e;
    private Noeud objectif;
    private Boolean eviterAgents;

//    public int compare2Noeuds(Noeud n1, Noeud n2){
//        if(n1.getHeuristique() < n2.getHeuristique()){
//            return 1;
//        }else if(n1.getHeuristique() == n2.getHeuristique()){
//            return 0;
//        }else{
//            return -1;
//        }
//    }

    public Position cheminPlusCourt(Environnement e, Agent a, Boolean eviterAgents){
//        System.out.println("cheminpluscourt");
        this.e = e;
        this.eviterAgents = eviterAgents;
        objectif = new Noeud(a.getPositionFinal(), 0, 0);

        Queue<Noeud> closedList = new LinkedList<>();
        List<Noeud> openList = new ArrayList<>();
        openList.add(new Noeud(a.getPositionCurrent(), objectif, 0));
        Noeud u;

        while (!openList.isEmpty()){
//            System.out.println(openList.size());
            Collections.sort(openList);
            u = openList.get(openList.size()-1);
            if(u.positionEquals(objectif)){
                return reconstituerChemin(u);
            }
            openList.remove(u);
            for(Noeud v : getVoisins(u)){
                if(v != null){
                    if(!closedList.contains(v)) {
                        if (!existInWithLowerCost(openList, v)) {
                            v.setCout(u.getCout() + 1);
                            v.setDistance(v.calcDistance(objectif));
                            v.setHeuristique(v.getCout() + v.getDistance());
                            openList.add(v);
                            v.setParent(u);
                        }
                    }
                }
            }
            closedList.add(u);
        }
        return reconstituerChemin(closestReachablePosition(closedList));
    }

    //Si l'objectif est inatégnable, retourne le Noeud atégnable le plus proche de l'objectif.
    public Noeud closestReachablePosition(Queue<Noeud> closeList){
        Noeud noeudMin = closeList.peek();
        int min = noeudMin.getDistance();
        for(Noeud n : closeList){
            if(n.getDistance() < min){
                noeudMin = n;
                min = n.getDistance();
            }
        }
        return noeudMin;
    }

    //reconsitue le chemin de n au dernier parent connu, retourne la premier position pour l'atteindre
    public Position reconstituerChemin(Noeud n){
//        System.out.println("reconsituerChemin");

        Noeud suivant = n;
        while(n.getParent() != null){
            suivant = n;
            n = n.getParent();
        }
        return new Position(suivant.getX(), suivant.getY());
    }


//vérifie si l'agent existe dans la liste avec un cout plus faible
    public boolean existInWithLowerCost(List<Noeud> openList, Noeud n){
        if(openList.contains(n)){
            for(Noeud row : openList){
                if(row.equals(n)){
                    if(row.getCout() < n.getCout()){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //trouve les voisins du noeud
    public List<Noeud> getVoisins(Noeud n){
        List<Noeud> voisins = new ArrayList<>();
        voisins.add(getVoisin(new Position(n.getX()+1, n.getY()), n.getCout()));
        voisins.add(getVoisin(new Position(n.getX()-1, n.getY()), n.getCout()));
        voisins.add(getVoisin(new Position(n.getX(), n.getY()+1), n.getCout()));
        voisins.add(getVoisin(new Position(n.getX(), n.getY()-1), n.getCout()));

        return voisins;
    }

    //retourne le noeud voisins ou null si le voisins n'existe pas
    public Noeud getVoisin(Position p, int cout) {
        if(p.getX() < 0 || p.getX() >= e.getN() || p.getY() < 0 || p.getY() >= e.getN()){
            return null;
        }
        if(eviterAgents) {
            if (e.getContent(p) == null) {
                return new Noeud(p, objectif, cout);
            }
        }else{
            return new Noeud(p, objectif, cout);
        }
        return null;
    }

}
