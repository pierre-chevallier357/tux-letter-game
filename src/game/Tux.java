package game;

import env3d.Env;
import env3d.advanced.EnvNode;
import org.lwjgl.input.Keyboard;

public class Tux extends EnvNode {
    private Env env;
    private Room room;
    
    public Tux(Env env, Room room) {
        this.env = env;
        this.room = room;
        setScale(5.0);
        setX(this.room.getWidth()/2); // Positionnement au milieu de la largeur de la room
        setY(getScale() * 1.1); // Positionnement en hauteur basé sur la taille de Tux
        setZ(this.room.getDepth()/2); // Positionnement au milieu de la profondeur de la room
        setTexture("models/tux/tux.png");
        setModel("models/tux/tux.obj");    
    }
    
    public void deplace() {
        // Haut
        // Flèche du haut ou Z
        if (env.getKeyDown(Keyboard.KEY_Z) || env.getKeyDown(Keyboard.KEY_UP)) {
            this.setRotateY(180);
            if (this.getZ() >= 6) {
                this.setZ(this.getZ() - 1.0);
            }
        }
        
        // Gauche
        // Flèche de gauche ou Q
        if (env.getKeyDown(Keyboard.KEY_Q) || env.getKeyDown(Keyboard.KEY_LEFT)) { // Fleche 'gauche' ou Q
            this.setRotateY(270);
            if (this.getX() >= 6) {
                this.setX(this.getX() - 1.0);
            }
        }
        
        // Bas
        // Flèche du bas ou S
        if (env.getKeyDown(Keyboard.KEY_S) || env.getKeyDown(Keyboard.KEY_DOWN)) {
            this.setRotateY(0);
            if (this.getZ() <= room.getDepth() - 6) {
                this.setZ(this.getZ() + 1.0);
            }
        }
        
        // Droite
        // Flèche de droite ou D
        if (env.getKeyDown(Keyboard.KEY_D) || env.getKeyDown(Keyboard.KEY_RIGHT)) {
            this.setRotateY(90);
            if (this.getX() <= room.getWidth() - 6) {
                this.setX(this.getX() + 1.0);
            }
        }
    }
}
    
