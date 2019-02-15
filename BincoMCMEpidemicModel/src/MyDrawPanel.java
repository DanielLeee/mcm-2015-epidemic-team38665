import java.awt.Button;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.text.html.MinimalHTMLWriter;

public class MyDrawPanel extends JPanel {
	
	
	private boolean dataflag = true;
	private Grid[][] map = null;
	private Button button = null;
	private TextArea ta = null;
	
	private int  min(int a, int b){
		if(a<b)
			return a;
		else
			return b;
	}
	
	private int  max(int a, int b){
		if(b<a)
			return a;
		else
			return b;
	}

	
	public void paint(Graphics g) {
		
		super.paint(g);
		
		if(!dataflag){
			for(int i = 0; i < EpidemicModel.SN; i++){
				for(int j = 0; j < EpidemicModel.SM; j++){
					if(!map[i][j].occupied)
						g.setColor(Color.white);
					else
						switch (map[i][j].ptype) {
							case 0:
							case 1:g.setColor(Color.green); break;
							case 2:
							case 3:
							case 4:g.setColor(Color.gray); break;
							case 5:g.setColor(Color.red); break;
							case 6:
							case 7:
							case 8:g.setColor(Color.blue); break;
							case 9:
							case 10:
							case 11:g.setColor(Color.black); break;
							case 12:g.setColor(Color.darkGray); break;
							
							default:
								break;
						}
					g.fillRect(i * EpidemicModel.plsize, j * EpidemicModel.plsize, EpidemicModel.plsize, EpidemicModel.plsize);
					
				}
			}	
		} else {
			g.setColor(Color.white);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			
			int[] num = new int[EpidemicModel.tottype];
			for(int i = 0; i < EpidemicModel.tottype; i++)
				num[i] = EpidemicModel.calctypenum(i);
			int normal = num[0] + num[1];
			int past7infected = 0;
			int past7death = 0;
			String pnumString = "";
			
			for(int i = max(0, EpidemicModel.caldays - 6); i <= EpidemicModel.caldays; i++)
				past7infected += EpidemicModel.newinfected[i];
			for(int i = max(0, EpidemicModel.caldays - 6); i <= EpidemicModel.caldays; i++)
				past7death += EpidemicModel.newdeath[i];
			
			g.setColor(Color.black);
			g.drawString("Now it is day: " + EpidemicModel.caldays, 20, 20 * 1);
			g.drawString("Normal people: " + EpidemicModel.normalnum, 20, 20 * 2);
			g.drawString("All the infected ever: " + (EpidemicModel.everinfectednum), 20, 20 * 3);
			g.drawString("All the isolated ever: " + (EpidemicModel.everisolatednum), 20, 20 * 4);
			g.drawString("Death number is: " + EpidemicModel.deathnum, 20, 20 * 5);
			g.drawString("Today infected: " + EpidemicModel.newinfected[EpidemicModel.caldays], 20, 20 * 6);
			g.drawString("Today dead: " + EpidemicModel.newdeath[EpidemicModel.caldays], 20, 20 * 7);
			g.drawString("Past-7-day infected: " + past7infected, 20, 20 * 8);
			g.drawString("Past-7-day dead: " + past7death, 20, 20 * 9);
			g.drawString("M: " + (num[9] * EpidemicModel.consM1 + num[8] * EpidemicModel.consM2), 20, 20 * 10);
			
			for(int i = 0; i < EpidemicModel.tottype; i++)
				pnumString = pnumString + i + ":" + num[i] + " ";
			g.drawString("All: " + pnumString, 20, 20 * 11);
			
			pnumString = "";
			for(int i = 0; i < 7; i++)
				pnumString = pnumString + i + ":" + EpidemicModel.newinfected[i] + " ";
			g.drawString("All: " + pnumString + "caldays: " + EpidemicModel.caldays, 20, 20 * 12);
			
		}
		
		

		
	}
	

	public void makebutton() {
		
		ta = new TextArea();
		button = new Button("Go");
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		add(button);
		
		return;
	}
	
	public void setdataflag(boolean b) {
		
		this.dataflag = b;
		
		return;
	}
	
	public void setMap(Grid[][] map) {
		
		this.map = map;
		return;
		
	}
	
	
}
