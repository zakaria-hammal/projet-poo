public class Preferences {
    private boolean accepterFilles;
    private boolean accepterGarcons;
    private boolean accepterMusique;
    private boolean accepterBagages;
    private String autres;

    // Getteurs :
    public boolean getAccepterFilles() {
        return this.accepterFilles;
    }

    public boolean getAccepterGarcon() {
        return this.accepterGarcons;
    }

    public boolean getAccepterMusique() {
        return this.accepterMusique;
    }

    public boolean getaccepterBagages() {
        return this.accepterBagages;
    }

    public String getAutres() {
        return this.autres;
    }

    // Setteurs :

    public void setAccepterFilles(boolean acF) {
        this.accepterFilles = acF;
    }

    public void setAccepterGarcon(boolean acG) {
        this.accepterGarcons = acG;
    }

    public void setAccepterMusique(boolean acM) {
        this.accepterMusique = acM;
    }

    public void setAccepterBagages(boolean acB) {
        this.accepterBagages = acB;
    }

    public void setAutres(String aut) {
        this.autres = aut;
    }
}
