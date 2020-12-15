package Controller;

import java.util.Date;

public class Message {
    private Agent emmeteur;

    private String performatif; //Request, Odre, Inform

    private int date;

    private String action;
    private String parametre;

    public Message(Agent emmeteur, String performatif, int date, String action, String parametre) {
        this.emmeteur = emmeteur;
        this.performatif = performatif;
        this.date = date;
        this.action = action;
        this.parametre = parametre;
    }
}
