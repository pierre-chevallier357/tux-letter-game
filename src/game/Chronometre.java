package game;

public class Chronometre {
    private long begin;
    private long end;
    private long current;
    private int limite;
    
    public Chronometre(int limite) {
        //intialisation
        begin = 0;
        end = 0;
        current = 0;
        this.limite = limite;
    }

    public void start() {
        this.begin = System.currentTimeMillis();
    }

    public void stop() {
        this.end = System.currentTimeMillis();
    }

    public long getTime() {
        return (end - begin);
    }

    public long getMilliseconds() {
        return end - begin;
    }

    public int getSeconds() {
        return (int) ((end - begin) / 1000.0);
    }
 
    public double getMinutes() {
        return (end - begin) / 60000.0;
    }

    public double getHours() {
        return (end - begin) / 3600000.0;
    }

    //Méthode pour savoir s'il reste du temps
    public boolean remainsTime() {
        current = getTime();
        int timeSpent;
        timeSpent = (int) (current / 1000.0);
        return limite>timeSpent;
    }
}
