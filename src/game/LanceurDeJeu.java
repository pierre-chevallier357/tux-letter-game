package game;

public class LanceurDeJeu {
    public static void main(String args[]) {
        // Déclare un Jeu
        Jeu jeu;
        //Instancie un nouveau jeu
        jeu = new JeuDevineLeMotOrdre();
        //Execute le jeu
        jeu.execute();
    }
}
