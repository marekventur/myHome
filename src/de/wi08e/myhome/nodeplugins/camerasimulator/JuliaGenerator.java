package de.wi08e.myhome.nodeplugins.camerasimulator;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Random;


public class JuliaGenerator {
	private double reC, imC;

	private Random random = new Random();
	
    public int checkZo(double reZ_minus1,double imZ_minus1) {
        double reZ,imZ;
        int i;
        for (i=0;i<47;i++) {
            imZ=2*reZ_minus1*imZ_minus1+imC;
            reZ=reZ_minus1*reZ_minus1-imZ_minus1*imZ_minus1+reC;
            if (reZ*reZ+imZ*imZ>4) return i;
            reZ_minus1=reZ;
            imZ_minus1=imZ;
        }
        return i;
    }

    public Image generate() {
    	BufferedImage result = new BufferedImage ( 300, 300, BufferedImage.TYPE_INT_RGB );
    	Graphics2D gp = result.createGraphics();
    	
    	Color c = new Color(random.nextInt(200)+50,random.nextInt(200)+50,random.nextInt(200)+50);
    	gp.setColor(c);
    	gp.setBackground(c);
    	gp.fillRect(0, 0, 300, 300);
    	
        double reZo, imZo, zelle=0.0065; 
        int x,y;
        Color colJulia = new Color(random.nextInt(200),random.nextInt(200),random.nextInt(200));
        reC = -0.65175;
        imC = 0.41850;
        reC = -0.65175-random.nextFloat()*0.1;
        imC = 0.41850-random.nextFloat()*0.1;

        imZo=-0.96; 
        
        for (y=0;y<300;y++) {
        	reZo=-1.5; 
            for (x=0;x<300;x++) {
                if (checkZo(reZo,imZo)==47) {
                	gp.setColor(colJulia);
                	gp.drawLine(x,y,x,y);
                }
                reZo=reZo+zelle; 
            }
            imZo=imZo+zelle;
        }
        
        gp.dispose();
        
        return result;
    }
}
