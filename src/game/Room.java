package game;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import game.xml.XMLUtil;

public class Room {
    private int depth;
    private int height;
    private int width;
    private String textureBottom;
    private String textureNorth;
    private String textureEast;
    private String textureWest;
    private String textureTop;
    private String textureSouth;
    
    public Room() {
        // Dimensions de la pièce
        depth=100;
        height=100;
        width=100;
        // Textures des murs de la pièce
        textureBottom="textures/floor/grass2.png";
        textureNorth="textures/skybox/default/north.png";
        textureEast="textures/skybox/default/east.png";
        textureWest="textures/skybox/default/west.png";
    }
    
    public Room(String nomFichier) {
    	Document doc = fromXML(nomFichier);
        try {
        	Element plateauElt = (Element)((Element)doc.getElementsByTagName("root").item(0)).getElementsByTagName("plateau").item(0);
        	
        	//parsing des dimensions
	        Element dimensionElt = (Element)plateauElt.getElementsByTagName("dimensions").item(0);
	        
	        Element heightElt = (Element)dimensionElt.getElementsByTagName("height").item(0);
	        Element widthElt = (Element)dimensionElt.getElementsByTagName("width").item(0);
	        Element depthElt = (Element)dimensionElt.getElementsByTagName("depth").item(0);
	        
	        String heightR = heightElt.getFirstChild().getNodeValue();
	        String widthR = widthElt.getFirstChild().getNodeValue();
	        String depthR = depthElt.getFirstChild().getNodeValue();
	        
	        this.height = Integer.parseInt(heightR);
	        this.width = Integer.parseInt(widthR);
	        this.depth = Integer.parseInt(depthR);
	        
	        //parsing des textures
	        Element mappingElt = (Element)plateauElt.getElementsByTagName("mapping").item(0);
	        
	        Element textureBottomElt = (Element) mappingElt.getElementsByTagName("textureBottom").item(0);
	        Element textureEastElt = (Element) mappingElt.getElementsByTagName("textureEast").item(0);
	        Element textureWestElt = (Element) mappingElt.getElementsByTagName("textureWest").item(0);
	        Element textureNorthElt = (Element) mappingElt.getElementsByTagName("textureNorth").item(0);
	        
	        this.textureBottom = textureBottomElt.getFirstChild().getNodeValue();
	        this.textureEast = textureEastElt.getFirstChild().getNodeValue();
	        this.textureWest = textureWestElt.getFirstChild().getNodeValue();
	        this.textureNorth = textureNorthElt.getFirstChild().getNodeValue();
	        
        } catch (Exception ex) {
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Cree un DOM à partir d'un fichier XML
    public Document fromXML(String nomFichier) {
        try {
            return XMLUtil.DocumentFactory.fromFile(nomFichier);
        } catch (Exception ex) {
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int getDepth() {
        return depth;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getTextureBottom() {
        return textureBottom;
    }

    public String getTextureNorth() {
        return textureNorth;
    }

    public String getTextureEast() {
        return textureEast;
    }

    public String getTextureWest() {
        return textureWest;
    }

    public String getTextureTop() {
        return textureTop;
    }

    public String getTextureSouth() {
        return textureSouth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setTextureBottom(String textureBottom) {
        this.textureBottom = textureBottom;
    }

    public void setTextureNorth(String textureNorth) {
        this.textureNorth = textureNorth;
    }

    public void setTextureEast(String textureEast) {
        this.textureEast = textureEast;
    }

    public void setTextureWest(String textureWest) {
        this.textureWest = textureWest;
    }

    public void setTextureTop(String textureTop) {
        this.textureTop = textureTop;
    }

    public void setTextureSouth(String textureSouth) {
        this.textureSouth = textureSouth;
    } 
}
