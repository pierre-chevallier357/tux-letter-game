package game;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Partie {
	private String date;
	private String mot;
	private int niveau;
	private int trouve;
	private int temps;
	
	public Partie(String date, String mot, int niveau){
		this.date = date;
		this.mot = mot;
		this.niveau = niveau;
	}

	public Partie(Element partieElt){
		this.date = partieElt.getAttribute("date");
		try {
			String chaine = partieElt.getAttribute("trouvé");
			this.trouve = Integer.parseInt(chaine.substring(0,chaine.length()-1));
		} catch (Exception e) {
			this.trouve = 100;
		}
		try {
			Element tempsP = (Element) partieElt.getElementsByTagName("temps").item(0); //getChildNodes().item(0);
			String s = tempsP.getFirstChild().getNodeValue();
			this.temps= (int)Float.parseFloat(s);
		} catch (Exception e) {
			this.temps = 0;
		}
		
		Element niveauP = (Element) partieElt.getElementsByTagName("niveau").item(0);
		this.niveau = Integer.parseInt(niveauP.getAttribute("difficulté"));
		
		Element motP = (Element) partieElt.getElementsByTagName("mot").item(0);
		this.mot = motP.getFirstChild().getNodeValue();
	}
	
	public Element getPartie(Document doc) {
		
		//creation des noeuds
		Element motP = doc.createElement("mot");
		motP.setTextContent(this.mot);
		
		Element niveauP = doc.createElement("niveau");
		niveauP.getAttribute("difficulté");
		niveauP.setAttribute("difficulté", String.valueOf(this.niveau));
		
		Element tempsP = doc.createElement("temps");
		tempsP.setTextContent(String.valueOf(this.temps));
		
		Element partie = doc.createElement("partie");
		partie.getAttribute("date");
		partie.setAttribute("date", this.date);
		
		if (this.trouve != 100) {
		partie.getAttribute("trouvé");
		partie.setAttribute("trouvé", String.format("%02d",this.trouve) + "%");
		}
		
		
		//lien entre les Elements
		niveauP.appendChild(motP);
		if (this.temps != 0) {
			partie.appendChild(tempsP);
		}
		partie.appendChild(niveauP);
		
		
		return partie;
	}
	
	public void setTemps(int temps) {
		this.temps = temps;
	}
	
	public void setTrouve(int nbLettresRestantes) {
		 if (this.mot != null) {
			 int n = mot.length();
			 //on utilise des double pour effectuer une division floatante
			 this.trouve = (int)((1.0 - (Double.valueOf(nbLettresRestantes) / Double.valueOf(n)))*100);
		 }
	}
	
	public int getNiveau() {
		return this.niveau;
	}
	
	@Override
	public String toString() {
		String str = String.format("[Partie, date=%s , trouvé=%d /100, temps=%d, diff=%d,mot=%s]", 
				date, trouve, temps, niveau, mot);
		return str;
		
	}
}

