package javaDoUnitex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

//            http://localhost/vidal1/vidal1/vidal-Sommaires-Substances-A.htm
public class Vidal extends HelpVidal {

	public Vidal() throws IOException {
		super();
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		
	HelpVidal myObject=new HelpVidal();
	JFrame frame= new JFrame("Extraction d'informations"); 
	JProgressBar barre=new JProgressBar();
	JProgressBar barre2=new JProgressBar();
	JLabel lab1=new JLabel();
	JLabel lab2=new JLabel();
	JLabel lab3=new JLabel();
	
	String urlstart=JOptionPane.showInputDialog("URL","http://localhost/home/said/Documents/Cproject/FinalCompil");
	int n=urlstart.length();
	String url = urlstart.substring(0,n-5);
	
	PrintWriter outPutFile= myObject.createFile("Fichier_sortie.dic","UTF-16LE");
	PrintWriter myDic=myObject.createFile("substance.dic","UTF-16LE");
	
	
	myObject.designFrame(frame,850,230);
	myObject.designJPB(barre,600,35,15,85,105);
	myObject.designJPB(barre2,600,35,15,85,105);
	myObject.designJL(lab1, "",16,150,20);
	myObject.designJL(lab2, "Nom de Substance",15,150,20);
	myObject.designJL(lab3, "Progression...",20,755,70);
	
	frame.add(lab3);frame.add(lab1);frame.add(barre);frame.add(lab2);frame.add(barre2);

	int actualPage=0;
	for(char c='A' ; c<='Z' ;++c){	
		
		String StdLien=url+c+".htm";
		
		lab1.setText("Page     "+c);
		String line = null ;
	
		URL urLien = new URL(StdLien);
		String regExp="\\s+<a href=\"Substance.+>(.+)</a>";
		
		BufferedReader lire = new BufferedReader(new InputStreamReader(urLien.openStream(),"UTF-8"));
		int actualLine=1;
		int nbLine=(int)lire.lines().count();
		lire=new BufferedReader(new InputStreamReader(urLien.openStream(),"UTF-8"));
		
			while((line =lire.readLine()) != null)
			{
				barre2.setValue(actualLine*100/nbLine);
				String substance=myObject.myMatches(regExp,line);	
				if(substance.length()!=0)
				    myDic.write(substance+",.N\n");
				Thread.sleep(1);
				actualLine++;
				outPutFile.write(line+"\n");	
			}
			actualPage++;
		    barre.setValue(actualPage*100/26);	
		}
        outPutFile.close();
        myDic.close();
        frame.dispose();

        String corp ="corpus-medical.txt";
        String sub ="substance.dic";
        myObject.recherche(sub,corp);
}
}