package game;
import env3d.advanced.EnvNode;

public class Letter extends EnvNode{
    private char letter;
    
    public Letter(char l, double x, double z) {
        setScale(3.0);
        setX(x);
        setY(getScale() * 1.1); // Positionnement en hauteur basé sur la taille d'un cube
        setZ(z);
        // Si c'est un espace on affiche un cube vide
        if (l == ' ') {
            setTexture("letter/cube.png");
        // Sinon on affiche le cube de la lettre
        } else {
            setTexture("letter/"+l+".png");
        }
        setModel("letter/cube.obj"); 
    }
}
