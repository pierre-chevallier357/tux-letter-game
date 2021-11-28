package game;

import env3d.Env;
import java.util.ArrayList;
import org.xml.sax.SAXException;
import java.io.*;
import javax.xml.parsers.*;
import org.lwjgl.input.Keyboard;
import java.io.File;

public abstract class Jeu {

    enum MENU_VAL {
        MENU_SORTIE, MENU_CONTINUE, MENU_JOUE
    }

    protected final Env env;
    private final Room mainRoom;
    private final Room menuRoom;
    private Tux tux;
    private Letter letter;
    private final ArrayList<Letter> lettres;
    private Profil profil;
    private final Dico dico;
    private String mot;
    private boolean finDePartie;
    protected EnvTextMap menuText; //text (affichage des texte du jeu)
    private int niveau;
    
    public Jeu() {
        // Crée un nouvel environnement
        env = new Env();
        // Instancie une Room
        mainRoom = new Room();
        // Instancie une autre Room pour les menus
        menuRoom = new Room();
        menuRoom.setTextureEast("textures/door.png");
        menuRoom.setTextureWest("textures/door.png");
        menuRoom.setTextureNorth("textures/door.png");
        menuRoom.setTextureBottom("textures/door.png");
        // Règle la camera
        env.setCameraXYZ(50, 50, 175);
        env.setCameraPitch(-10);
        // Désactive les contrôles par défaut
        env.setDefaultControl(false);
        // Instancie un profil par défaut
        profil = new Profil("Antoine","08/12/1998");
        // Instancie un dico
        dico = new Dico("src/xml/dico.xml");
        // Instancie le boolean finDePartie à faux
        finDePartie = false;
        // Instancie une liste de lettres
        lettres = new ArrayList<Letter>();
        // Instancie un boolean qui définit si notre partie est finie
        finDePartie = false;
        // Instancie le niveau par défaut du jeu à 1
        niveau = 1;
        // Instancie le menuText
        menuText = new EnvTextMap(env);
        // Textes affichés à l'écran
        menuText.addText("Voulez vous ?", "Question", 160, 300);
        menuText.addText("1. Commencer une nouvelle partie ?", "Jeu1", 210, 280);
        //menuText.addText("2. Charger une partie existante ?", "Jeu2", 210, 260);
        menuText.addText("2. Sortir de ce jeu ?", "Jeu2", 210, 260);
        menuText.addText("3. Quitter le jeu ?", "Jeu3", 210, 240);
        menuText.addText("Choisissez un nom de joueur : ", "NomJoueur", 180, 300);
        menuText.addText("1. Charger un profil de joueur existant ?", "Principal1", 210, 280);
        menuText.addText("2. Créer un nouveau joueur ?", "Principal2", 210, 260);
        menuText.addText("3. Sortir du jeu ?", "Principal3", 210, 240);
        menuText.addText("Choisissez le niveau de jeu :", "Selection", 160, 300);
        menuText.addText("1. Niveau 1 (Débutant)", "Niveau1", 210, 280);
        menuText.addText("2. Niveau 2 (Intermédiaire)", "Niveau2", 210, 260);
        menuText.addText("3. Niveau 3 (Avancé)", "Niveau3", 210, 240);
        menuText.addText("4. Niveau 4 (Expert)", "Niveau4", 210, 220);
        menuText.addText("5. Niveau 5 (DEMON)", "Niveau5", 210, 200);
        menuText.addText("Mot à deviner :", "motDeviner", 260, 300);
        menuText.addText("Appuyez sur entrée pour continuer", "entree", 180, 200);
    }
    
    public Env getEnv() {
        return env;
    }
    
    public Profil getProfil() {
        return profil;
    }

    public Room getMainRoom() {
        return mainRoom;
    }
    
    public Room getMenuRoom() {
        return menuRoom;
    }

    public Tux getTux() {
        return tux;
    }
        
    public ArrayList<Letter> getLettres(){
        return lettres;
    }

    public Dico getDico() {
        return dico;
    }
    
    public String getMot() {
        return mot;
    }
    
    public int getNiveau() {
        return niveau;
    }
    
    public void setMot(String mot) {
        this.mot = mot;
    }
    
    public void setFinDePartie(boolean finDePartie){
        this.finDePartie = finDePartie;
    }
    
    private String getNomJoueur() {
        String nomJoueur = "";
        menuText.getText("NomJoueur").display();
        menuText.getText("entree").display();
        nomJoueur = menuText.getText("NomJoueur").lire(true);
        menuText.getText("NomJoueur").clean();
        menuText.getText("entree").clean();
        return nomJoueur;
    }
    
    private void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    // Gère le menu principal
    public void execute() {
        MENU_VAL mainLoop;
        mainLoop = MENU_VAL.MENU_SORTIE;
        do {
            mainLoop = menuPrincipal();
        } while (mainLoop != MENU_VAL.MENU_SORTIE);
        this.env.setDisplayStr("Au revoir !", 300, 30);
        env.exit();
    }

    protected MENU_VAL menuJeu() {
        MENU_VAL playTheGame;
        playTheGame = MENU_VAL.MENU_JOUE;
        Partie partie;
        do {
            // Restaure la room du menu
            env.setRoom(menuRoom);
            // Affiche menu
            menuText.getText("Question").display();
            menuText.getText("Jeu1").display();
            menuText.getText("Jeu2").display();
            menuText.getText("Jeu3").display();
            //menuText.getText("Jeu4").display();
            
            // Vérifie qu'une touche 1, 2, 3 ou 4 est pressée
            int touche = 0;
            while (!(touche == Keyboard.KEY_1 || touche == Keyboard.KEY_2 || touche == Keyboard.KEY_3/* || touche == Keyboard.KEY_4*/)) {
                touche = env.getKey();
                env.advanceOneFrame();
            }

            // Nettoie l'environnement du texte
            menuText.getText("Question").clean();
            menuText.getText("Jeu1").clean();
            menuText.getText("Jeu2").clean();
            menuText.getText("Jeu3").clean();
            //menuText.getText("Jeu4").clean();

            // Restaure la room du jeu
            env.setRoom(mainRoom);

            // Et décide quoi faire en fonction de la touche pressée
            switch (touche) {
                // Touche 1 : Commencer une nouvelle partie             
                case Keyboard.KEY_1: // choisi un niveau et charge un mot depuis le dico
                    // Lit le dictionnaire est affecte les mots du fichier XML dans les listes de mots
                    try {
                        dico.lireDictionnaireDOM("src/xml/", "dico.xml");
                        }
                    catch (SAXException e1) {
                        System.out.println(e1);
                    }
                    catch (IOException e2) {
                        System.out.println(e2);
                    }
                    catch (ParserConfigurationException e3) {
                        System.out.println(e3);
                    }
                    // Selection du niveau
                    menuSelectionNiveau();
                    // Instancie un mot de notre dico
                    mot = getDico().getMotDepuisListeNiveaux(getNiveau());
                    setMot(mot);
                    // Affiche le mot à deviner jusqu'à ce que la touche "entrée" soit pressée
                    menuMotADeviner(mot);
                    // Restaure la room du jeu
                    env.setRoom(mainRoom);
                    partie = new Partie("1/12/2020", mot, 1);
                    // Joue
                    joue(partie);
                    // Enregistre la partie dans le profil --> enregistre le profil
                    profil.ajoutePartie(partie);
                    playTheGame = MENU_VAL.MENU_JOUE;
                    break;
                    
                /*
                // Touche 2 : Charger une partie existante           
                case Keyboard.KEY_2: // Charge une partie existante
                    partie = new Partie("2018-09-7", "test", 1);
                    // Recupère le mot de la partie existante
                    try {
                        profil.recupereMotPartieExistante("src/xml/", "profil.xml");
                        }
                    catch (SAXException e1) {
                        System.out.println(e1);
                    }
                    catch (IOException e2) {
                        System.out.println(e2);
                    }
                    catch (ParserConfigurationException e3) {
                        System.out.println(e3);
                    }
                    // Joue
                    joue(partie);
                    // Enregistre la partie dans le profil --> enregistre le profil
                    profil.ajoutePartie(partie);
                    playTheGame = MENU_VAL.MENU_JOUE;
                    break;
                    */

                // Touche 3 : Sortie de ce jeu             
                case Keyboard.KEY_2:
                    playTheGame = MENU_VAL.MENU_CONTINUE;
                    break;

                // Touche 4 : Quitter le jeu             
                case Keyboard.KEY_3:
                    playTheGame = MENU_VAL.MENU_SORTIE;
            }
        } while (playTheGame == MENU_VAL.MENU_JOUE);
        return playTheGame;
    }

    protected MENU_VAL menuPrincipal() {
        MENU_VAL choix = MENU_VAL.MENU_CONTINUE;
        String nomJoueur;
        // Restaure la room du menu
        env.setRoom(menuRoom);

        menuText.getText("Question").display();
        menuText.getText("Principal1").display();
        menuText.getText("Principal2").display();
        menuText.getText("Principal3").display();
               
        // Vérifie qu'une touche 1, 2 ou 3 est pressée
        int touche = 0;
        while (!(touche == Keyboard.KEY_1 || touche == Keyboard.KEY_2 || touche == Keyboard.KEY_3)) {
            touche = env.getKey();
            env.advanceOneFrame();
        }

        menuText.getText("Question").clean();
        menuText.getText("Principal1").clean();
        menuText.getText("Principal2").clean();
        menuText.getText("Principal3").clean();

        // Et décide quoi faire en fonction de la touche pressée
        switch (touche) {
            // Touche 1 : Charger un profil existant
            case Keyboard.KEY_1:
                // Demande le nom du joueur existant
                nomJoueur = getNomJoueur();
                // Charge le profil de ce joueur si possible
                profil = new Profil("xml/" + nomJoueur + ".xml");
                File fichierJoueur = new File("xml/" + nomJoueur + ".xml");
                // Si le fichier du joueur existe
                if (fichierJoueur.exists()) {
                    // on lance le menu suivant
                    choix = menuJeu();
                } else {
                    // Sinon on retourne au menu principal"
                    menuPrincipal();
                }
                break;

            // Touche 2 : Créer un nouveau joueur
            case Keyboard.KEY_2:
                // demande le nom du nouveau joueur
                nomJoueur = getNomJoueur();
                // crée un profil avec le nom d'un nouveau joueur
                profil = new Profil(nomJoueur,"20/10/2000");
                choix = menuJeu();
                break;

            // Touche 3 : Sortir du jeu
            case Keyboard.KEY_3:
                choix = MENU_VAL.MENU_SORTIE;
        }
        return choix;
    }

    protected void menuSelectionNiveau() {
        // Restaure la room du menu
        env.setRoom(menuRoom);
        
        // Affiche menu
        menuText.getText("Selection").display();
        menuText.getText("Niveau1").display();
        menuText.getText("Niveau2").display();
        menuText.getText("Niveau3").display();
        menuText.getText("Niveau4").display();
        menuText.getText("Niveau5").display();
        
        // Vérifie qu'une touche 1, 2, 3, 4 ou 5 est pressée
        int touche = 0;
        while (!(touche == Keyboard.KEY_1 || touche == Keyboard.KEY_2 || touche == Keyboard.KEY_3 || touche == Keyboard.KEY_4 || touche == Keyboard.KEY_5)) {
            touche = env.getKey();
            env.advanceOneFrame();
        }
        
        // Nettoie l'environnement du texte
        menuText.getText("Selection").clean();
        menuText.getText("Niveau1").clean();
        menuText.getText("Niveau2").clean();
        menuText.getText("Niveau3").clean();
        menuText.getText("Niveau4").clean();
        menuText.getText("Niveau5").clean();

        // Et décide quoi faire en fonction de la touche pressée
        switch (touche) {
            // Touche 1 : Niveau 1
            case Keyboard.KEY_1:
                setNiveau(1);
                break;
            // Touche 2 : Niveau 2
            case Keyboard.KEY_2:
                setNiveau(2);
                break;
            // Touche 3 : Niveau 3
            case Keyboard.KEY_3:
                setNiveau(3);
                break;
            // Touche 4 : Niveau 4
            case Keyboard.KEY_4:
                setNiveau(4);
                break;
            // Touche 5 : Niveau 5
            case Keyboard.KEY_5:
                setNiveau(5);
                break;
        }
    }
    
    protected void menuMotADeviner(String mot) {        
        // Affiche menu
        menuText.addText(mot, "Mot", 280, 280);
        menuText.getText("motDeviner").display();
        menuText.getText("entree").display();
        menuText.getText("Mot").display();
        
        // Vérifie que la touche entrée est pressée
        int touche = 0;
        while (!(touche == Keyboard.KEY_RETURN)) {
            touche = env.getKey();
            env.advanceOneFrame();
        }
        
        // Nettoie l'environnement du texte
        menuText.getText("motDeviner").clean();
        menuText.getText("entree").clean();
        menuText.getText("Mot").clean();
    }
    
    public void joue(Partie partie) {
        // Instancie un Tux
        tux = new Tux(env, mainRoom);
        env.addObject(tux);

        // Ici, on peut initialiser des valeurs pour une nouvelle partie
        demarrePartie(partie);

        // Boucle de jeu
        while (finDePartie == false) {
            // Contrôles globaux du jeu (sortie, ...)
            // 1 is for escape key
            if (env.getKey() == 1) {
                finDePartie = true;
            }
            // Contrôles des déplacements de Tux (gauche, droite, ...)
            tux.deplace();
            // Ici, on applique les regles
            appliqueRegles(partie);
            // Fait avancer le moteur de jeu (mise à jour de l'affichage, de l'écoute des événements clavier...)
            env.advanceOneFrame();
        }
        // Ici on peut calculer des valeurs lorsque la partie est terminée
        terminePartie(partie);
    }

    protected abstract void demarrePartie(Partie partie);

    protected abstract void appliqueRegles(Partie partie);

    protected abstract void terminePartie(Partie partie);
}
