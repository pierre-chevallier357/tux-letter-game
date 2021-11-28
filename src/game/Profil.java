package game;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import game.xml.XMLUtil;

public class Profil {
	private String nom;
	private String dateNaissance;
	private String avatar;
	private ArrayList<Partie> parties;

    public Document _doc;
	
	public Profil(String nom, String dateNaissance) {
		this.nom = nom;
		this.dateNaissance = dateNaissance;
		this.avatar = "conan.jpg";
		this.parties = new ArrayList<Partie>();
	}
	
    // Cree un DOM à partir d'un fichier XML
    public Profil(String nomFichier) {
        _doc = fromXML(nomFichier);
        try {
        	Element PremierProfil = (Element)((Element)_doc.getElementsByTagName("profils").item(0)).getElementsByTagName("profil").item(0);
        	
	        Element nomELt = (Element)PremierProfil.getElementsByTagName("nom").item(0);
	        this.nom = nomELt.getFirstChild().getNodeValue();
	        
	        Element avatarElt = (Element)PremierProfil.getElementsByTagName("avatar").item(0);
	        this.avatar = avatarElt.getFirstChild().getNodeValue();
	        
	        Element annivElt = (Element)PremierProfil.getElementsByTagName("anniversaire").item(0);
	        String anniv = annivElt.getFirstChild().getNodeValue();
	        this.dateNaissance = xmlDateToProfileDate(anniv);
	        
	        Element partiesElt = (Element)PremierProfil.getElementsByTagName("parties").item(0);
	        NodeList ListeParties = partiesElt.getElementsByTagName("partie");
	         
	        this.parties = new ArrayList<Partie>();
	        
	        for (int i=0; i< ListeParties.getLength(); i++) {
	        	//recupere l'abre DOM de la i-eme partie 
	        	Element PartieCourenteElt = (Element)ListeParties.item(i);
	        	//creer une instance de partie, avec notre arbre DOM de partie
	        	Partie p = new Partie(PartieCourenteElt);
	        	//l'ajoute au tableau de partie
	        	ajoutePartie(p);
	        }
	        
        } catch (Exception ex) {
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void ajoutePartie(Partie p) {
    	parties.add(p);
    }
    
    public void sauvegarder(String filename) {
    	try {
    		Document doc = CreateDocument();
    		Element profilsElt = doc.createElement("profils");
    		Element profilElt = doc.createElement("profil");
    		Element nomElt = doc.createElement("nom");
    		Element avatarElt = doc.createElement("avatar");
    		Element annivElt = doc.createElement("anniversaire");
    		Element partiesElt = doc.createElement("parties");
    		
    		
    		profilsElt.getAttribute("xmlns");
    		profilsElt.getAttribute("xmlns:xsi");
    		profilsElt.getAttribute("xsi:schemaLocation");
    		
    		profilsElt.setAttribute("xmlns", "http://myGame/tux");
    		profilsElt.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
    		profilsElt.setAttribute("xsi:schemaLocation", "http://myGame/tux ../xsd/profil.xsd");
    		
    		
	    	nomElt.setTextContent(this.nom);
	    	
	    	avatarElt.setTextContent(this.avatar);
	    	
	    	String anniv = profileDateToXmlDate(this.dateNaissance);
	    	annivElt.setTextContent(anniv);
	    	
	    	//LIEN DES ELEMENTS ENTRE EUX
	    	
	    	for (Partie p : this.parties) {
	    		//ajouter à partiesElt toutes les parties
	    		partiesElt.appendChild(p.getPartie(doc));
	    	}
	    	
	    	profilElt.appendChild(nomElt);
	    	profilElt.appendChild(avatarElt);
	    	profilElt.appendChild(annivElt);
	    	profilElt.appendChild(partiesElt);
	    	
	    	profilsElt.appendChild(profilElt);
	    	
	    	doc.appendChild(profilsElt);
	    	
	    	this._doc = doc;
	    	toXML(filename);
    	
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

    // Sauvegarde un DOM en XML
    public void toXML(String nomFichier) {
        try {
            XMLUtil.DocumentTransform.writeDoc(_doc, nomFichier);
        } catch (Exception ex) {
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /// Takes a date in XML format (i.e. ????-??-??) and returns a date
    /// in profile format: dd/mm/yyyy
    public static String xmlDateToProfileDate(String xmlDate) {
        String date;
        // récupérer le jour
        date = xmlDate.substring(xmlDate.lastIndexOf("-") + 1, xmlDate.length());
        date += "/";
        // récupérer le mois
        date += xmlDate.substring(xmlDate.indexOf("-") + 1, xmlDate.lastIndexOf("-"));
        date += "/";
        // récupérer l'année
        date += xmlDate.substring(0, xmlDate.indexOf("-"));

        return date;
    }

    /// Takes a date in profile format: dd/mm/yyyy and returns a date
    /// in XML format (i.e. ????-??-??)
    public static String profileDateToXmlDate(String profileDate) {
        String date;
        // Récupérer l'année
        date = profileDate.substring(profileDate.lastIndexOf("/") + 1, profileDate.length());
        date += "-";
        // Récupérer  le mois
        date += profileDate.substring(profileDate.indexOf("/") + 1, profileDate.lastIndexOf("/"));
        date += "-";
        // Récupérer le jour
        date += profileDate.substring(0, profileDate.indexOf("/"));

        return date;
    }
    
    public Document CreateDocument() {
    	try {
	    	DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
	        domFactory.setNamespaceAware(true); // never forget this!
			DocumentBuilder builder = domFactory.newDocumentBuilder();
			return builder.newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}
    }
    
    public String toString() {
    	String str = String.format("[profil] nom: %s, dn: %s, avatar: %s Partie(s):\n",nom,dateNaissance,avatar);
    	for (Partie p : this.parties) {
    		str += "    " + p.toString() + "\n";
    	}
    	return str;
    }

} 