package game;

import java.util.ArrayList;

//import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.*;

import org.w3c.dom.*;
import javax.xml.parsers.*;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
//for SAX

public class Dico extends DefaultHandler{
    private ArrayList<String> listeNiveau1;
    private ArrayList<String> listeNiveau2;
    private ArrayList<String> listeNiveau3;
    private ArrayList<String> listeNiveau4;
    private ArrayList<String> listeNiveau5;
    private String cheminFichierDico;
    
    protected StringBuffer buffer;
    
    private STATE state;
    private int difficulte;
    
    private enum STATE {
    	START,
    	DICTIONNAIRE,
    	NIVEAU,
    	MOT,
    	OTHER
    }
    
    public Dico(String cheminFichierDico) {
    	super(); //for SAX
        this.cheminFichierDico = cheminFichierDico;
    }
    
    public void lireDictionnaire() {
    	try {
    		XMLReader parser = XMLReaderFactory.createXMLReader();
    		parser.setContentHandler(this);
    		parser.parse(this.cheminFichierDico);
    		/*
	    	// création d'une fabrique de parseurs SAX 
			SAXParserFactory builder = SAXParserFactory.newInstance(); 
			
			// création d'un parseur SAX 
			SAXParser parseur = builder.newSAXParser(); 
	
			// lecture d'un fichier XML avec un DefaultHandler 
			File fichier = new File(cheminFichierDico);
			
			DefaultHandler gestionnaireDico = new Dico(cheminFichierDico); 
			parseur.parse(fichier, gestionnaireDico); 
			
			*/
    		
    	} catch (Exception e) {
    		
    	}
    	
    }
    
    public String getMotDepuisListeNiveaux(int niveau) {
        String mot;
        switch(verifieNiveau(niveau)) {
            case 5 :
                mot = getMotDepuisListe(this.listeNiveau5);
                break;
            case 4 :
                mot = getMotDepuisListe(this.listeNiveau4);
                break;
            case 3 :
                mot = getMotDepuisListe(this.listeNiveau3);
                break;
            case 2 :
                mot = getMotDepuisListe(this.listeNiveau2);
                break;
            // Par défaut si le niveau n'est pas 2, 3, 4 ou 5 c'est que c'est 1 (vérifié auparavant dans vérifieNiveau(niveau))
            default :
                mot = getMotDepuisListe(this.listeNiveau1);
                break;
        }
        return mot;
    }
    
    public void ajouteMotADico(int niveau, String mot) {
        switch(verifieNiveau(niveau)) {
            case 5 :
                this.listeNiveau5.add(mot);
                break;
            case 4 :
                this.listeNiveau4.add(mot);
                break;  
            case 3 :
                this.listeNiveau3.add(mot);
                break;
            case 2 :
                this.listeNiveau2.add(mot);
                break;
            // Par défaut si le niveau n'est pas 2, 3, 4 ou 5 c'est que c'est 1 (vérifié auparavant dans vérifieNiveau(niveau))
            default :
                this.listeNiveau1.add(mot);
                break;
        }
    }
    
    public String getCheminFichierDico() {
        return cheminFichierDico;
    }
    
    private int verifieNiveau(int niveau) {
        // Si niveau entre 1 et 5 on le laisse inchangé
        if (niveau >= 1 && niveau <= 5) {
            return niveau;
        // Si niveau plus petit que 1 on met à 1
        } else if (niveau < 1) {
            return 1;
        // Sinon niveau plus grand que 5 et on met à 5
        } else {
            return 5;
        }
    }
    
    private String getMotDepuisListe(ArrayList<String> list) {
        // Si la liste n'est pas vide on retourne un mot aléatoire
        if (!(list.isEmpty())){
            int aleatoire = (int) (Math.random() * list.size());
            return list.get(aleatoire);
        // Sinon on retourne le mot par défaut qui est "mot"
        } else {
            return "mot";
        }
    }
    
    public void lireDictionnaireDOM(String path, String filename) throws SAXException, IOException, ParserConfigurationException {
        // On charge notre document suivant son path et son filename
        /*
    	DOMParser parser = new DOMParser();
        parser.parse(path+filename);
        Document doc = parser.getDocument();
        */
    	
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(path+filename);
        
        
        // Affecte la racine "dictionnaire" au noeud "dictionnaire
        Node dictionnaire = doc.getChildNodes().item(0);
        // Affecte les noeuds "niveau" dans une liste de noeuds "niveaux"
        NodeList niveaux = ((Element) dictionnaire).getElementsByTagName("tx:niveau");
        
        // Affecte le noeud de niveau 1
        Node niveau1 = niveaux.item(0);
        // Affecte la liste des mots de niveau 1 à une liste de noeuds
        NodeList listeMotsNiveau1 = ((Element) niveau1).getElementsByTagName("tx:mot");
        // Charge les mots de niveau 1 dans le dico
        for (int i=0; i<listeMotsNiveau1.getLength(); i++) {
            ajouteMotADico(1,listeMotsNiveau1.item(i).getChildNodes().item(0).getNodeValue());
        }
        
        // Affecte le noeud de niveau 2
        Node niveau2 = niveaux.item(1);
        // Affecte la liste des mots de niveau 2 à une liste de noeuds
        NodeList listeMotsNiveau2 = ((Element) niveau2).getElementsByTagName("tx:mot");        
        // Charge les mots de niveau 2 dans le dico
        for (int i=0; i<listeMotsNiveau2.getLength(); i++) {
            ajouteMotADico(2,listeMotsNiveau2.item(i).getChildNodes().item(0).getNodeValue());
        }
        
        // Affecte le noeud de niveau 3
        Node niveau3 = niveaux.item(2);
        // Affecte la liste des mots de niveau 3 à une liste de noeuds
        NodeList listeMotsNiveau3 = ((Element) niveau3).getElementsByTagName("tx:mot");        
        // Charge les mots de niveau 3 dans le dico
        for (int i=0; i<listeMotsNiveau3.getLength(); i++) {
            ajouteMotADico(3,listeMotsNiveau3.item(i).getChildNodes().item(0).getNodeValue());
        }
        
        // Affecte le noeud de niveau 4
        Node niveau4 = niveaux.item(3);
        // Affecte la liste des mots de niveau 4 à une liste de noeuds
        NodeList listeMotsNiveau4 = ((Element) niveau4).getElementsByTagName("tx:mot");        
        // Charge les mots de niveau 4 dans le dico
        for (int i=0; i<listeMotsNiveau4.getLength(); i++) {
            ajouteMotADico(4,listeMotsNiveau4.item(i).getChildNodes().item(0).getNodeValue());
        }
        
        // Affecte le noeud de niveau 5
        Node niveau5 = niveaux.item(4);
        // Affecte la liste des mots de niveau 5 à une liste de noeuds
        NodeList listeMotsNiveau5 = ((Element) niveau5).getElementsByTagName("tx:mot");        
        // Charge les mots de niveau 5 dans le dico
        for (int i=0; i<listeMotsNiveau5.getLength(); i++) {
            ajouteMotADico(5,listeMotsNiveau5.item(i).getChildNodes().item(0).getNodeValue());
        }
    }
    
    @Override
    public void startDocument() throws SAXException {
    	state = STATE.START;
    	this.listeNiveau1 = new ArrayList<String>();
        this.listeNiveau2 = new ArrayList<String>();
        this.listeNiveau3 = new ArrayList<String>();
        this.listeNiveau4 = new ArrayList<String>();
        this.listeNiveau5 = new ArrayList<String>();
    }
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    	switch(state) {
    	case START:
    		//si l'on est dans l'etat start on ne peut que avoir un
    		//commentaire ou dictionnaire
    		if (localName.equals("dictionnaire")) {
    			state = STATE.DICTIONNAIRE;
    		}
    		break;
    		
    	case DICTIONNAIRE:
    		if (localName.equals("niveau")) {
    			int difficulteNiveau = Integer.parseInt(attributes.getValue(0));
    			this.difficulte = difficulteNiveau; //pour ecrir par la suite dans la bonne liste
    			state = STATE.NIVEAU;
    		}
    		break;
    	case NIVEAU:
    		if (localName.equals("mot")) {
    			state = STATE.MOT;
    		}
    		break;
    		
		default:
			break;
    		
    	}
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
	    switch(state) {
		case DICTIONNAIRE:
			state = STATE.OTHER; //fin de la lecture du doc
			break;
		case NIVEAU:
			state = STATE.DICTIONNAIRE;
			break;
		case MOT:
			state = STATE.NIVEAU;
			break;
		default:
			break;
			
		}
	}
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
    	if (state == STATE.MOT) {
    		String motLu = "";
    		for (int i= start; i< start+length; i++) {
    			motLu += ch[i];    			
    		}
    		switch (this.difficulte) {
    		case 1:
    			listeNiveau1.add(motLu);
    			break;
    		case 2:
    			listeNiveau2.add(motLu);
    			break;
    		case 3:
    			listeNiveau3.add(motLu);
    			break;
    		case 4:
    			listeNiveau4.add(motLu);
    			break;
    		case 5:
    			listeNiveau5.add(motLu);
    			break;
    		default:
    			break;		
    		}
    	}
    }

    @Override
    public void endDocument() throws SAXException {} 
    
    
    public String toString() {
    	String str = "Print: mots lvl1:\n";
    	for (String s : this.listeNiveau1) {
    		str += s + "\n";
    	}
    	str += "\n mots lvl2:\n";
    	for (String s : this.listeNiveau2) {
    		str += s + "\n";
    	}
    	str += "\n mots lvl3:\n";
    	for (String s : this.listeNiveau3) {
    		str += s + "\n";
    	}
    	str += "\n mots lvl4:\n";
    	for (String s : this.listeNiveau4) {
    		str += s + "\n";
    	}
    	str += "\n mots lvl5:\n";
    	for (String s : this.listeNiveau5) {
    		str += s + "\n";
    	}
		return str;
    	
    }
}
