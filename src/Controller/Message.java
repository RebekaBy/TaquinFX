package Controller;

import java.util.Date;

public class Message {
    private Agent emmeteur;
    private Agent recepteur;

    private String performatif; //Request, Odre, Inform

    private Date date;

    private String action;
    private String parametre;

    public Message(Agent emmeteur, Agent recepteur, String performatif, Date date, String action, String parametre) {
        this.emmeteur = emmeteur;
        this.recepteur = recepteur;
        this.performatif = performatif;
        this.date = date;
        this.action = action;
        this.parametre = parametre;
    }
}
