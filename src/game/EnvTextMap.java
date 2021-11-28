package game;

import env3d.Env;
import java.util.HashMap;
import java.util.Map;

// Cette classe gère l'ensemble des textes de type EnvText du jeu
public class EnvTextMap {

    public Env env = null; // Référence vers l'environnement
    Map<String, EnvText> map; // Contient les textes associés à une clef de type String

    public EnvTextMap(Env env) {
        this.map = new HashMap<String, EnvText>();
        this.env = env;
    }

    // Ajouter du texte (de type envtext) dans la hashmap
    public void addText(String phrase, String cle, int x, int y) {
        EnvText text = new EnvText(env, phrase, x, y);
        map.put(cle, text);
    }

    // Obtenir du texte à partir de sa clé
    public EnvText getText(String cle) {
        return map.get(cle);
    }
}
