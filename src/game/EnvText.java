package game;

import env3d.Env;
import org.lwjgl.input.Keyboard;

public class EnvText {
    private int x; // x du texte
    private int y; // y du texte
    private String text; // Valeur du texte
    private double size; // Taille du texte
    private double currentR; // Couleur rouge du texte
    private double currentG; // Couleur verte du texte
    private double currentB; // Couleur bleue du texte
    private double savedR; // Couleur rouge du texte
    private double savedG; // Couleur verte du texte
    private double savedB; // Couleur bleue du texte
    private final Env env; // Environnement du texte   

    // Environnement du text
    public EnvText(Env env, String text, int x, int y) {
        this.x = adjustXValue(x);
        this.y = adjustXValue(y);
        this.text = text;
        size = 1.0;
        // By default current text is white
        currentR = adjustColorValue(255);
        currentG = adjustColorValue(255);
        currentB = adjustColorValue(255);
        // By default selected text is green
        savedR = adjustColorValue(40);
        savedG = adjustColorValue(100);
        savedB = adjustColorValue(0);
        this.env = env;
    }

    // Constructeur de l'environnement text
    public EnvText(Env env, String text, int x, int y, double size, double cr, double cg, double cb, double sr, double sg, double sb) {
        this.x = x;
        if (x < 0) {
            this.x = 0;
        }
        this.y = y;
        if (y < 0) {
            this.y = 0;
        }
        this.text = text;
        this.size = adjustSizeValue(size);
        currentR = adjustColorValue(cr);
        currentG = adjustColorValue(cg);
        currentB = adjustColorValue(cb);
        savedR = adjustColorValue(sr);
        savedG = adjustColorValue(sg);
        savedB = adjustColorValue(sb);
        this.env = env;
    }

    // Ajuster la valeur x
    private int adjustXValue(int x) {
        if (x < 0) {
            return 0;
        }
        return x;
    }

    // Ajuster la valeur y
    private int adjustYValue(int y) {
        if (y < 0) {
            return 0;
        }
        return y;
    }

    // Ajuster la valeur de la couleur
    private double adjustColorValue(double v) {
        if (v < 0.0) {
            return 0.0;
        }
        if (v > 1.0) {
            return 1.0;
        }
        return v;
    }

    // Ajuster la taille
    private double adjustSizeValue(double v) {
        if (v < 0.1) {
            return 0.1;
        }
        return v;
    }

    // Lorsque l'utilisateur selectionne ce texte
    public void select() {
        double temp;
        temp = savedR;
        savedR = currentR;
        currentR = temp;
        temp = savedG;
        savedG = currentG;
        currentG = temp;
        temp = savedB;
        savedB = currentB;
        currentB = temp;
    }

    // Etat du texte non selectionné
    public void unselect() {
        double temp;
        temp = currentR;
        currentR = savedR;
        savedR = temp;
        temp = currentG;
        currentG = savedG;
        savedG = temp;
        temp = currentB;
        currentB = savedB;
        savedB = temp;
    }

    // Affichage du texte
    public void display() {
        clean();
        env.setDisplayStr(text, x, y, size, currentR, currentG, currentB, 1.0);
    }

    // Affichage du texte
    public void display(double size, double r, double g, double b) {
        this.size = adjustSizeValue(size);
        currentR = adjustColorValue(r);
        currentG = adjustColorValue(g);
        currentB = adjustColorValue(b);
        display();
    }

    // Déplacement et affichage du texte
    public void moveAndDisplay(int x, int y) {
        this.x = adjustXValue(x);
        this.y = adjustXValue(y);
        display();
    }

    // Modification du texte
    public void modifyTextAndDisplay(String text) {
        this.text = text;
        display();
    }

    // Modification temporaire du texte
    public void addTextAndDisplay(String textPrefix, String textSuffix) {
        clean();
        env.setDisplayStr(textPrefix + text + textSuffix, x, y, size, currentR, currentG, currentB, 1.0);
    }

    // Suppression du texte
    public void destroy() {
        text = "";
    }

    // Rendre le texte "invisible"
    public void clean() {
        env.setDisplayStr("", x, y, size, currentR, currentG, currentB, 1.0);
    }

    // Teste si un code clavier correspond bien à une lettre
    public Boolean isLetter(int codeKey) {
        return codeKey == Keyboard.KEY_A || codeKey == Keyboard.KEY_B || codeKey == Keyboard.KEY_C || codeKey == Keyboard.KEY_D || codeKey == Keyboard.KEY_E
                || codeKey == Keyboard.KEY_F || codeKey == Keyboard.KEY_G || codeKey == Keyboard.KEY_H || codeKey == Keyboard.KEY_I || codeKey == Keyboard.KEY_J
                || codeKey == Keyboard.KEY_K || codeKey == Keyboard.KEY_L || codeKey == Keyboard.KEY_M || codeKey == Keyboard.KEY_N || codeKey == Keyboard.KEY_O
                || codeKey == Keyboard.KEY_P || codeKey == Keyboard.KEY_Q || codeKey == Keyboard.KEY_R || codeKey == Keyboard.KEY_S || codeKey == Keyboard.KEY_T
                || codeKey == Keyboard.KEY_U || codeKey == Keyboard.KEY_V || codeKey == Keyboard.KEY_W || codeKey == Keyboard.KEY_X || codeKey == Keyboard.KEY_Y
                || codeKey == Keyboard.KEY_0 || codeKey == Keyboard.KEY_1 || codeKey == Keyboard.KEY_2 || codeKey == Keyboard.KEY_3 || codeKey == Keyboard.KEY_4
                || codeKey == Keyboard.KEY_5 || codeKey == Keyboard.KEY_6 || codeKey == Keyboard.KEY_7 || codeKey == Keyboard.KEY_8 || codeKey == Keyboard.KEY_9
                || codeKey == Keyboard.KEY_Z || codeKey == Keyboard.KEY_BACK || codeKey == Keyboard.KEY_F1 || codeKey == Keyboard.KEY_F9
                || codeKey == Keyboard.KEY_SPACE;
    }

    // Récupère une lettre à partir d'un code clavier
    public String getLetter(int letterKeyCode) {
        Boolean shift = false;
        if (this.env.getKeyDown(Keyboard.KEY_LSHIFT) || this.env.getKeyDown(Keyboard.KEY_RSHIFT)) {
            shift = true;
        }
        env.advanceOneFrame();
        String letterStr = "";
        if ((letterKeyCode == Keyboard.KEY_SUBTRACT || letterKeyCode == Keyboard.KEY_MINUS)) {
            letterStr = "-";
        } else {
            letterStr = Keyboard.getKeyName(letterKeyCode);
        }
        if (shift) {
            return letterStr.toUpperCase();
        }
        return letterStr.toLowerCase();
    }

    // Gestion du "delete" lorsque l'utilisateur souhaite supprimer un caractere à la fin du mot
    private String delete(String mot) {
        String res = "";
        for (int i = 0; i < mot.length() - 1; i++) {
            res += mot.charAt(i);
        }
        return res;
    }

    // Cette fonction permet la gestion de la saisie par l'utilisateur
    public String lire(boolean affiche) {
        int touche = 0;
        String mot = "";
        while (!(mot.length() > 0 && touche == Keyboard.KEY_RETURN)) {
            touche = 0;
            while (!isLetter(touche) && touche != Keyboard.KEY_MINUS && touche != Keyboard.KEY_SUBTRACT && touche != Keyboard.KEY_RETURN) {
                touche = env.getKey();
                env.advanceOneFrame();
            }
            if (touche != Keyboard.KEY_RETURN) {
                if (touche == Keyboard.KEY_BACK) {
                    // supprime une lettre à la fin du mot
                    mot = delete(mot);
                } else if ((touche == Keyboard.KEY_SUBTRACT || touche == Keyboard.KEY_MINUS) && mot.length() > 0) {
                    mot += "-";
                } else {
                    // ajoute lettre
                    mot += getLetter(touche);
                }
                if (affiche) {
                    addTextAndDisplay("", mot);
                }
            }
        }
        return mot;
    }
}
