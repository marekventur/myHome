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
		int tempTimer=0;

		while(true)
		{
			ist=gui.getIST();
			soll=gui.getSoll();
			if(ist<soll&&gui.istRunning()) this.increasTemperatur();
			if(ist>0&&!gui.istRunning()) this.decreasTemperatur();
			if(ist>soll&&gui.istRunning()) this.decreasTemperatur();
			if(zahl>20&&gui.istRunning()) {gui.setFuellstand(fuellstand=fuellstand-1); zahl=0;}
			try 
			{
				Thread.sleep(2000);
			} catch (InterruptedException e) {e.printStackTrace();}
			if(gui.getTimerActive()&&tempTimer>30) {gui.setTimer((gui.getTimer()-1));tempTimer=0;}
			zahl++;
			tempTimer++;
		}
	}
	
	public void decreasTemperatur()
	{
		ist--;
		gui.setIST(String.valueOf(ist));
	}
	

}
