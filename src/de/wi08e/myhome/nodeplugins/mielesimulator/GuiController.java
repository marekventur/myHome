package de.wi08e.myhome.nodeplugins.mielesimulator;

public class GuiController extends Thread
{
	private GUI gui = null;
	private int ist = 0;
	private int soll = 0;
	private int fuellstand = 100;
	
	
	public GuiController(GUI gui) {
		this.gui=gui;
		ist=gui.getIST();
		soll=gui.getSoll();
	}
	
	public void increasTemperatur()
	{
		ist++;
		gui.setIST(String.valueOf(ist));
	}

	@Override
	public void run() 
	{
		int zahl=0;
		while(true)
		{
			ist=gui.getIST();
			soll=gui.getSoll();
			if(ist<soll&&gui.istRunning()) this.increasTemperatur();
			if(ist>0&&!gui.istRunning()) this.decreasTemperatur();
			if(ist>soll&&gui.istRunning()) this.decreasTemperatur();
			if(zahl>7&&gui.istRunning()) gui.setFuellstand(fuellstand=fuellstand-3);
			try 
			{
				Thread.sleep(1500);
			} catch (InterruptedException e) {e.printStackTrace();}
			zahl++;
		}
	}
	
	public void decreasTemperatur()
	{
		ist--;
		gui.setIST(String.valueOf(ist));
	}
	

}
