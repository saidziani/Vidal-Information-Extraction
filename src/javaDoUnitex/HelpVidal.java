package javaDoUnitex;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class HelpVidal   {
	
	JFrame frame= new JFrame("Corpus_medical VS Substitution"); 
	JProgressBar barre=new JProgressBar();
	JLabel lab1=new JLabel();

	public HelpVidal()throws IOException  {
		super();	
	}
	
	public PrintWriter createFile(String nom,String encoding) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter myFile=new PrintWriter(nom,encoding);
		return myFile ;
	}
	public BufferedReader openRead(String path, String encoding) throws FileNotFoundException, UnsupportedEncodingException{

		BufferedReader buff = new BufferedReader(new InputStreamReader (new FileInputStream(path),encoding));
			return buff;		
	}
	public String myMatches(String ExpReg,String fileLine){		
		String mot="";
		Pattern patron2= Pattern.compile(ExpReg);
		Matcher match2=patron2.matcher(fileLine);		
		if(match2.matches())
		{
		   mot=match2.group(1);   
		}
	  return mot;		
	}
	
	public void designJL(JLabel lab,String contenu,int size,int lg,int lr){
		lab.setText(contenu);
		lab.setForeground(Color.DARK_GRAY);
		lab.setFont(new Font("Tahoma", Font.BOLD, size));
		lab.setPreferredSize(new Dimension(lg,lr));	
	}
	
	public void designJPB(JProgressBar bar,int lg,int lr,int size,int c1,int c2){		
        Dimension dim=new Dimension(lg,lr);    
	    bar.setPreferredSize(new Dimension(dim));
	    bar.setBackground(SystemColor.controlHighlight);
		bar.setFont(new Font("Tahoma", Font.BOLD, size));
		bar.setStringPainted(true);
		bar.setForeground(new Color(0,c1,c2));	
	}
	
	public void designFrame(JFrame frame,int lg,int lr){
	    FlowLayout dispo = new FlowLayout();
			frame.setLayout(dispo);
			frame.setSize(lg,lr);
			frame.setLocation(250,300);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
			frame.setResizable(false);	
	}
	
	@SuppressWarnings("resource")
	public void recherche(String path1,String path2) throws IOException, InterruptedException{
		
		HelpVidal myObj=new HelpVidal();
		
	    BufferedReader subst=myObj.openRead(path1,"UTF-16LE");
	    BufferedReader corpus=myObj.openRead(path2,"UTF-16LE");
	    String corpusLine , subLine ;

		PrintWriter fich= new PrintWriter("myPage.html","UTF-8");
		
	    	    
		myObj.designFrame(frame,700,140);
		myObj.designJPB(barre,650,35,16,85,105);
		myObj.designJL(lab1, "Application du Dictionnaire...",19,650,40);

        frame.add(lab1);frame.add(barre);
				
		int nbLine=(int)corpus.lines().count();
		System.out.println(nbLine);
		corpus=myObj.openRead(path2,"UTF-16LE");	
		
	    String Regul="(.+),.N";     
	    int actualCorpus=0;
	    
		while((corpusLine = corpus.readLine())!= null) {	
			subst=myObj.openRead(path1,"UTF-16LE");
			while((subLine = subst.readLine()) != null) { 	
			   String subN=myObj.myMatches(Regul,subLine);	
			   Pattern patron = Pattern.compile("(\\b(|é|è|ê)"+subN+"\\b)",Pattern.CASE_INSENSITIVE);
			   Matcher m = patron.matcher(corpusLine);              
                   if(m.find(0)) {
                fich.write("<mot><span style=\"color:red\">" +subN+ "</span></mot><br/><line>"+actualCorpus+" : "+corpusLine+"</line><br/>");
                fich.write(System.getProperty("line.separator"));
                    }      
			}		
			actualCorpus++;
		    barre.setValue(actualCorpus*100/nbLine);	
		}	
		fich.flush();
		System.out.println(actualCorpus);
		subst.close();
		corpus.close();
		frame.dispose();
	}
}
