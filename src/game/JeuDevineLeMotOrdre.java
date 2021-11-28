package game;

import java.util.ArrayList;
import org.lwjgl.input.Keyboard;

public class JeuDevineLeMotOrdre extends Jeu {
    private int nbLettresRestantes;
    private final Chronometre chrono;
    private final int tempsPartie;
    private boolean gagne;
    
    public JeuDevineLeMotOrdre() {
        super();
        // Instancie le temps limite de notre partie
        tempsPartie = 15;
        // Instancie un chrono avec le temps limite de la partie
        chrono = new Chronometre(tempsPartie);
        // Instancie un boolean qui définit si notre joueur a gagné 
        gagne = false;
    }
    
    public boolean isGagne() {
        return gagne;
    }
     
    public void setGagne(boolean gagne) {
        this.gagne = gagne;
    }
    
    private boolean tuxTrouveLettre() {
        // touchePremiere lettre regarde s'il y a collision entre Tux et la première lettre du mot restant
        boolean touchePremiereLettre = collision(getLettres().get(0));
        if (touchePremiereLettre == true) {
            getLettres().remove(0);
        }
        return touchePremiereLettre;
    }
    
    @Override
    protected void demarrePartie(Partie partie) {
        // Place le mot dans l'environnement
        placeMotAuHasardSurTerrain(getMot());
        chrono.start();
        // Instancie le nombre de lettres restantes au début de la partie grâce à la longueur du mot
        nbLettresRestantes = getMot().length();
    }
    
    @Override
    protected void appliqueRegles(Partie partie) {
        if (tuxTrouveLettre()) {
            nbLettresRestantes -= 1;
        }
        chrono.stop();
        if (chrono.remainsTime() == false && nbLettresRestantes > 0) {
            setGagne(false);
            setFinDePartie(true);
        } else if (chrono.remainsTime() == true && nbLettresRestantes == 0) {
            setGagne(true);
            setFinDePartie(true);
        }
    }
    
    @Override
    protected void terminePartie(Partie partie) {
        long tempsPourTrouverMot = chrono.getTime() / 1000;
        int nbLettresTrouvees = getMot().length()-nbLettresRestantes;
        // Affichage du menu de score en fonction de si le joueur a gagné ou non
        if (isGagne()) {
            affichageGagne(tempsPourTrouverMot, nbLettresTrouvees);
        } else {
            affichagePerdu(tempsPourTrouverMot, nbLettresTrouvees);
        }
        

    }
    
    private void affichageGagne(long tempsPourTrouverMot, int nbLettresTrouvees) {
        // Restaure la room du menu
        env.setRoom(getMenuRoom());
        // Affichage du menu de score
        // Affiche "Vous avez trouvé les X lettres du mot !"
        String affichageGagne1 = "Vous avez trouvé les " + nbLettresTrouvees + " lettres du mot !";
        menuText.addText(affichageGagne1, "affichageGagne1", 160, 300);
        menuText.getText("affichageGagne1").display();
        // Affiche "Bravo ! Vous avez gagné la partie !"
        String affichageGagne2 = "Bravo ! Vous avez gagné la partie !";
        menuText.addText(affichageGagne2, "affichageGagne2", 160, 280);
        menuText.getText("affichageGagne2").display();
        // Affiche "Temps pour trouver le mot : Xs"
        String affichageGagne3 = "Temps pour trouver le mot : " + tempsPourTrouverMot + "s";
        menuText.addText(affichageGagne3, "affichageGagne3", 160, 260);
        menuText.getText("affichageGagne3").display();
        // Affiche la possibilité de passer au menu suivant en appuyant sur "entrée"
        menuText.getText("entree").display();
        
        // Vérifie que la touche entrée est pressée
        int touche = 0;
        while (!(touche == Keyboard.KEY_RETURN)) {
            touche = getEnv().getKey();
            getEnv().advanceOneFrame();
        }
        
        // Nettoie l'environnement du texte
        menuText.getText("affichageGagne1").clean();
        menuText.getText("affichageGagne2").clean();
        menuText.getText("affichageGagne3").clean();
        menuText.getText("entree").clean();
    }
    
    private void affichagePerdu(long tempsPourTrouverMot, int nbLettresTrouvees) {
        // Restaure la room du menu
        env.setRoom(getMenuRoom());
        // Affichage du menu de score
        // Affiche "Vous avez atteint le temps limite de Xs..."
        String affichagePerdu1 = "Vous avez atteint le temps limite de " + tempsPourTrouverMot + "s...";
        menuText.addText(affichagePerdu1, "affichagePerdu1", 160, 300);
        menuText.getText("affichagePerdu1").display();
        // Affiche "Dommage... Vous avez perdu la partie..."
        String affichagePerdu2 = "Dommage... Vous avez perdu la partie...";
        menuText.addText(affichagePerdu2, "affichagePerdu2", 160, 280);
        menuText.getText("affichagePerdu2").display();
        // Affiche "Nombre de lettres trouvées : X sur Y
        String affichagePerdu3 = "Nombre de lettres trouvées : " + nbLettresTrouvees + " sur " + getMot().length();
        menuText.addText(affichagePerdu3, "affichagePerdu3", 160, 260);
        menuText.getText("affichagePerdu3").display();
        // Affiche la possibilité de passer au menu suivant en appuyant sur "entrée"
        menuText.getText("entree").display();
        
        // Vérifie que la touche entrée est pressée
        int touche = 0;
        while (!(touche == Keyboard.KEY_RETURN)) {
            touche = getEnv().getKey();
            getEnv().advanceOneFrame();
        }
        
        // Nettoie l'environnement du texte
        menuText.getText("affichagePerdu1").clean();
        menuText.getText("affichagePerdu2").clean();
        menuText.getText("affichagePerdu3").clean();
        menuText.getText("entree").clean();
    }
    
    protected void placeMotAuHasardSurTerrain(String mot) {
        // Instanciation de notre boucle
        boolean estDans;
        int x;
        int z;
        ArrayList<int[]> positionsLettresPlacees = new ArrayList<int[]>();
        char[] tableauCharDeMot = new char[mot.length()];
        
        // Pour chaque lettre de notre mot
        for (int i = 0; i < mot.length(); i++) {
            // Conversion de notre String en tableau de char
            tableauCharDeMot[i] = mot.charAt(i);
            // Placement de la lettre à une position aléatoire
            do {
                x = ((int) (1 + 9 * Math.random())) * 10;
                z = ((int) (1 + 9 * Math.random())) * 10;
                estDans = false;
                int[] positionLettreAPlacer = {x,z};
                for (int[] pos : positionsLettresPlacees) {
                    if (pos[0] == positionLettreAPlacer[0] && pos[1] == positionLettreAPlacer[1]) {
                        estDans = true;
                    }
                }
                if (!estDans) {
                    positionsLettresPlacees.add(positionLettreAPlacer);
                    // Ajout du tableau de char à la liste de lettre
                    getLettres().add(new Letter(tableauCharDeMot[i],positionLettreAPlacer[0],positionLettreAPlacer[1]));
                }
            } while (estDans);
        }
        // Ajoute les lettres à l'environnement
        for (Letter lettre : getLettres()) {
            getEnv().addObject(lettre);
        }
    }
    
    protected double distance(Letter lettre) {
        // Coordonnées de Tux
        double tuxX = getTux().getX();
        double tuxZ = getTux().getZ();
        // Coordonnées de la lettre
        double lettreX = lettre.getX();
        double lettreZ = lettre.getZ();
        // Théorème de pythagore pour obtenir la distance à partir des coordonnées
        double distance = Math.sqrt((Math.pow(Math.abs(lettreX-tuxX),2))+(Math.pow(Math.abs(lettreZ-tuxZ),2)));
        return distance;
    }
    
    protected boolean collision(Letter lettre) {
        boolean toucheLettre = distance(lettre) <= (6+3); // Car Tux a un "rayon" de 6 et une lettre un "rayon" de 2
        return toucheLettre;
    }
}
