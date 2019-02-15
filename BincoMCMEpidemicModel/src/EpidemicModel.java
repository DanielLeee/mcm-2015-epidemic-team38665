/*
 * 
 * This is a program for a model designed for testing purpose in the 2015 MCM Contest.
 * All rights reserved by Daniel Lee from team 38665 in the aforementioned contest.
 * 
 * 
 * 
 * */



import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import javax.naming.InitialContext;
import javax.swing.JFrame;


public class EpidemicModel {

	/*
	 * Definition: 0 Normal With Vaccine    1 Normal    2 Patient Pre     3 Patient Post   4 Latent     5 Dead    6 Iso Pre 
	 *             7 Iso Post     8 Iso Latent     9  Super Pre    10  Super Post    11  Super Latent   12  Quit
	 * 
	 * 
	 * */
	
	public static JFrame frame;
	public static MyDrawPanel drawPanel = null;
	public static JFrame dataFrame;
	public static MyDrawPanel dataPanel = null;
	public static TextArea ta = null;
	public static Button button = null;
	public static FileWriter fw = null;
	
	public static int tottype = 13;
	public static int[] needarray = new int[tottype];
	public static int plsize = 4;
	public static Grid[][] map = null;
	public static int[][] dire = new int[][]{{0,1},{0,-1},{-1,0},{1,0}};
	public static int caldays = 0;
	public static int deathnum = 0;
	public static int ininum = 0;
	public static int everisolatednum = 0;
	public static int everinfectednum = 0;
	public static int[] newinfected = new int[1000];
	public static int[] newdeath = new int[1000];
	public static int normalnum = 0;

	//  Constant
	public static int SN = 150;
	public static int SM = 150;
	
	public static double probMove = 0.3;
	public static final int consT = 60;
	public static final double consA1 = 0.8;
	public static final double consA2 = 0.7;
	public static final int conszz = 5;
	public static final double consP = 0;
	public static final int conssmallt = 3;
	public static final double consX = 0;
	public static final int constt1 = 5;
	public static final int constt2 = 10;
	public static final double consk = 0.02;
	public static final int consL = 12;
	public static final int consY = 2;
	public static final double consM1 = 2;
	public static final double consM2 = 1;
	
	public static void init () {
		
		needarray[0] = 0;
		needarray[1] = 15000;
		needarray[2] = 0;
		needarray[3] = 0;
		needarray[4] = 10;
		needarray[5] = 0;
		needarray[6] = 0;
		needarray[7] = 0;
		needarray[8] = 0;
		needarray[9] = 0;
		needarray[10] = 0;
		needarray[11] = 0;
		needarray[12] = 0;
		
		everinfectednum = needarray[2] + needarray[3]+ needarray[4]+ needarray[9]+ needarray[10]+ needarray[11];
		normalnum = needarray[0] + needarray[1];
		
		return;
	}
	
	public static double functionE(int dd) {
		
		return Math.exp((double)(-consY+dd) / consY);
		
	}
	
	public static double functionD(int T) {

		return Math.exp((-conssmallt + T)/conssmallt);
		
	}
	
	public static double functionJ(int dddd) {
		
		return 1 - Math.exp((-consT + dddd) / consT);
	}
	
	public static double functionH(int d) {
		
		return Math.exp((double)(-consL+d)/consL);
	}
	
	public static int calctypenum(int t) {
		
		int ret = 0;
		
		for(int i = 0;i < SN; i++) {
			for(int j = 0;j < SM; j++) {
				if(map[i][j].occupied && map[i][j].ptype == t)
					ret++;
			}
		}
		
		
		return ret;
		
	}
	
	public static int getRandomInt(int a, int b){
		Random r = new Random();
		
		return ((r.nextInt() % (b-a+1) + (b-a+1)) % (b-a+1)  + a);
		
	}
	
	public static double getRandomDouble(double a, double b){
		Random r = new Random();
		
		return (r.nextDouble() * (b-a) + a);
		
	}
	
	public static void randomSetPeople(Grid[][] map, int[] need){
		
		for(int i = 0; i < need.length; i ++){
			
			int nowneed = need[i];
			
			ininum += nowneed;
			
			while(nowneed > 0){
				
				int x = 0;
				int y = 0;
				
				do {
					x = getRandomInt(0, SN - 1);
					y = getRandomInt(0, SM - 1);
				} while(map[x][y].occupied);
				
				map[x][y].occupied = true;
				map[x][y].ptype = i;
				map[x][y].days = 0;
				
				nowneed--;
				
			}
			
		}
		
		return;
	}
	
	public static Grid[][] NewGrid() {
		
		Grid[][] map = new Grid[SN][];
		for(int i = 0; i < SN; i ++){
			map[i] = new Grid[SM];
			for(int j = 0; j < SM; j ++) {
				map[i][j] = new Grid();
				map[i][j].occupied = false;
			}
				
		}
		
		return map;
	}
	
	public static int getneighbornum(int x, int y, int t) {
		
		int ret = 0;
		
		for(int i = 0; i < 4; i++){
			int xx = x + dire[i][0];
			int yy = y + dire[i][1];
			if(inbound(xx, yy) && map[xx][yy].ptype == t)
				ret ++;
		}
		
		return ret;
		
	}
	
	public static void RunTheMap() {
		
		for(int i = 0; i < SN; i++){
			for(int j = 0; j < SM; j++)
			if(map[i][j].occupied)
			{
				switch (map[i][j].ptype) {
					case 0:
						
						// To normal people
						if(map[i][j].days == consT){
							map[i][j].newptype = 1;
							map[i][j].newdays = 0;
						} else {
							map[i][j].newptype = map[i][j].ptype;
							map[i][j].newdays = map[i][j].days + 1;
						};
						
						int patientnum = getneighbornum(i,j,2) + getneighbornum(i,j,3);
						int latentnum = getneighbornum(i,j,4);
						int supernum = getneighbornum(i,j,9) + getneighbornum(i,j,10) + getneighbornum(i,j,11);
						double notinfectedprob = 1;
						double notsuperprob = 1;
						
						for(int k = 0; k < patientnum; k++)
							notinfectedprob *= (1 - consA1 * functionJ(map[i][j].days));
						for(int k = 0; k < latentnum; k++)
							notinfectedprob *= (1 - consA2 * functionJ(map[i][j].days));
						for(int k = 0; k < supernum; k++)
							notsuperprob *= (1 - functionJ(map[i][j].days));
						
						// Test if infected
						if(getRandomDouble(0, 1) < (1 - notinfectedprob) ) {
							map[i][j].newptype = 4;
							map[i][j].newdays = 0;
						}
						
						// Test if superly infected
						if(getRandomDouble(0, 1) < (1 - notsuperprob) ) {
							map[i][j].newptype = 11;
							map[i][j].newdays = 0;
						}
						
						if(map[i][j].newptype == 4 || map[i][j].newptype == 11) {
							newinfected[caldays] ++;
							everinfectednum ++;
							normalnum--;
						}
							
						
						
						break;
						
					case 1:
						
						// Vaccination
						if(caldays == conszz && getRandomDouble(0, 100) < consP) {
							map[i][j].newptype = 0;
							map[i][j].newdays = 0;
						} else {
							map[i][j].newptype = map[i][j].ptype;
							map[i][j].newdays = map[i][j].days + 1;
						}
						
						patientnum = getneighbornum(i,j,2) + getneighbornum(i,j,3);
						latentnum = getneighbornum(i,j,4);
						supernum = getneighbornum(i,j,9) + getneighbornum(i,j,10) + getneighbornum(i,j,11);
						notinfectedprob = 1;
						notsuperprob = 1;
						
						for(int k = 0; k < patientnum; k++)
							notinfectedprob *= (1 - consA1);
						for(int k = 0; k < latentnum; k++)
							notinfectedprob *= (1 - consA2);
						for(int k = 0; k < supernum; k++)
							notsuperprob *= (1 - 1);
						
						// Test if infected
						if(getRandomDouble(0, 1) < (1 - notinfectedprob) ) {
							map[i][j].newptype = 4;
							map[i][j].newdays = 0;
						}
						
						// Test if superly infected
						if(getRandomDouble(0, 1) < (1 - notsuperprob) ) {
							map[i][j].newptype = 11;
							map[i][j].newdays = 0;
						}
						
						if(map[i][j].newptype == 4 || map[i][j].newptype == 11) {
							newinfected[caldays] ++;
							everinfectednum ++;
							normalnum--;
						}
						
						break;
						
					case 2:
						
						boolean fl = false;
						
						if(getRandomDouble(0, 1) < functionE(map[i][j].days)) {
							map[i][j].newptype = 3;
							map[i][j].newdays = 0;
							fl = true;
						} else {
							map[i][j].newptype = map[i][j].ptype;
							map[i][j].newdays = map[i][j].days + 1;
						}
						
						if(getRandomDouble(0, 1) < consX) {
							if(fl) {
								map[i][j].newptype = 7;
								map[i][j].newdays = 0;
							} else {
								map[i][j].newptype = 6;
								map[i][j].newdays = 0;
							}
							everisolatednum ++;
						}
						
						break;
						
						
					case 3:
						
						double deathprob = functionD(map[i][j].days);
						
						if(getRandomDouble(0, 1) < consX) {
							map[i][j].newptype = 7;
							map[i][j].newdays = 0;
						} else {
							map[i][j].newptype = map[i][j].ptype;
							map[i][j].newdays = map[i][j].days + 1;
						}
						
						if(getRandomDouble(0, 1) < deathprob) {
							map[i][j].newptype = 5;
							map[i][j].newdays = 0;
						}
						
						if(map[i][j].newptype == 7)
							everisolatednum ++;
						
						break;
						
						
					case 4:
						
						boolean flag = false;
						boolean flag0 = false;
						
						
						// Patient Pre
						if(getRandomDouble(0, 1) < functionH(map[i][j].days)) {
							map[i][j].newptype = 2;
							map[i][j].newdays = 0;
							flag = true;
						} else {
							map[i][j].newptype = map[i][j].ptype;
							map[i][j].newdays = map[i][j].days + 1;
						}
						
						// Super latent
						if(map[i][j].days == 1 && getRandomDouble(0, 1) < consk) {
							map[i][j].newptype = 11;
							map[i][j].newdays = 0;
							flag0 = true;
						}
						
						// Isolated
						if(getRandomDouble(0, 1) < consX) {
							if(flag && !flag0) {
								map[i][j].newptype = 6;
								map[i][j].newdays = 0;
							} else {
								map[i][j].newptype = 8;
								map[i][j].newdays = 0;
							}
							everisolatednum ++;
						}
						
						
					
						break;
						
						
					case 5:
						
						// Dead
						if(map[i][j].occupied) {
							deathnum ++;
							newdeath[caldays] ++;
							map[i][j].occupied = false;
						}
						 
						
						break;
						
						
					case 6:
						
						if(map[i][j].days == constt1) {
							map[i][j].newptype = 8;
							map[i][j].newdays = 0;
						} else {
							map[i][j].newptype = map[i][j].ptype;
							map[i][j].newdays = map[i][j].days + 1;
						} 
							
						
						break;
						
						
					case 7:
						
						deathprob = functionD(map[i][j].days);
						
						if(getRandomDouble(0, 1) < deathprob) {
							map[i][j].newptype = 5;
							map[i][j].newdays = 0;
						} else {
							map[i][j].newptype = map[i][j].ptype;
							map[i][j].newdays = map[i][j].days + 1;
						}
						
						break;
						
						
					case 8:
						
						if(map[i][j].days == constt2) {
							map[i][j].newptype = 12;
							map[i][j].newdays = 0;
						} else {
							map[i][j].newptype = map[i][j].ptype;
							map[i][j].newdays = map[i][j].days + 1;
						} 
						
						break;
						
						
					case 9:
						
						flag = false;
						
						if(getRandomDouble(0, 1) < functionE(map[i][j].days)) {
							map[i][j].newptype = 10;
							map[i][j].newdays = 0;
							flag = true;
						} else {
							map[i][j].newptype = map[i][j].ptype;
							map[i][j].newdays = map[i][j].days + 1;
						} 
						
						if(getRandomDouble(0, 1) < consX) {
							if(flag) {
								map[i][j].newptype = 7;
								map[i][j].newdays = 0;
							} else {
								map[i][j].newptype = 6;
								map[i][j].newdays = 0;
							}
							everisolatednum ++;	
						}
						
						break;
						
						
					case 10:
						
						if(getRandomDouble(0, 1) < consX) {
								map[i][j].newptype = 7;
								map[i][j].newdays = 0;
						} else {
							map[i][j].newptype = map[i][j].ptype;
							map[i][j].newdays = map[i][j].days + 1;
						}
						
						deathprob = functionD(map[i][j].days);
						
						if(getRandomDouble(0, 1) < deathprob) {
							map[i][j].newptype = 5;
							map[i][j].newdays = 0;
						}
						
						if(map[i][j].newptype == 7)
							everisolatednum ++;
						
						break;
						
					case 11:
						
						flag = false;
						
						if(getRandomDouble(0, 1) < functionH(map[i][j].days)) {
							map[i][j].newptype = 9;
							map[i][j].newdays = 0;
							flag = true;
						} else {
							map[i][j].newptype = map[i][j].ptype;
							map[i][j].newdays = map[i][j].days + 1;
						}
										
						if(getRandomDouble(0, 1) < consX) {
							if(flag) {
								map[i][j].newptype = 6;
								map[i][j].newdays = 0;
							} else {
								map[i][j].newptype = 8;
								map[i][j].newdays = 0;
							}
							everisolatednum ++;
						}	
						
						break;
											
					case 12:
						
						// Quit already.
						
						break;
						
						
					default:
						
						
						break;
				}
			}
		}
		
		
		return;
	}
	
	public static boolean inbound(int x, int y){
		if(0 <= x && x < SN && 0 <= y && y < SM)
			return true;
		else
			return false;
	}
	
	public static void MoveTheMap() {
		
		for(int i = 0; i < SN; i++){
			for(int j = 0; j < SM; j++){
				if( (map[i][j].ptype == 0 || map[i][j].ptype == 1 || map[i][j].ptype == 2
						|| map[i][j].ptype == 4  || map[i][j].ptype == 9
						|| map[i][j].ptype == 11  || map[i][j].ptype == 12) &&
						getRandomDouble(0, 1) < probMove) {
					// Move
					// Choose A direction
					int direction = getRandomInt(0, 3);
					int xx = 0, yy = 0;
					xx = i + dire[direction][0];
					yy = j + dire[direction][1];
					
					if(inbound(xx, yy) && !map[xx][yy].occupied){
						Grid temg = null;
						temg = map[i][j];
						map[i][j] = map[xx][yy];
						map[xx][yy] = temg;
					}
				}
			}
		}
		
		
		return;
	}
	
	public static void RefreshTheMap() {
		
		for(int i = 0; i < SN; i++){
			for(int j = 0; j < SM; j++){
				map[i][j].ptype = map[i][j].newptype;
				map[i][j].days = map[i][j].newdays;
			}
		}

		
		return;
	}
	
	public static int  max(int a, int b){
		if(b<a)
			return a;
		else
			return b;
	}

	
	public static void writetodaydatatofile() throws IOException {
		
		int past7infected = 0;
		int past7death = 0;

		for(int i = max(0, caldays - 6); i <= caldays; i++)
			past7infected += newinfected[i];
		for(int i = max(0, caldays - 6); i <= caldays; i++)
			past7death += newdeath[i];
		
		
		fw = new FileWriter("EpidemicOutput.txt", true);
		
		
		fw.write(String.format("%d", caldays) + "\t" + String.format("%d", normalnum) + "\t" + String.format("%d", everinfectednum) + "\t" +
				String.format("%d", everisolatednum) + "\t" + String.format("%d", deathnum) + "\t" + String.format("%d", newinfected[caldays]) + "\t" +
				String.format("%d", newdeath[caldays]) + "\t" + String.format("%d", past7infected) + "\t" + String.format("%d", past7death) + "\t" +
				String.format("%d", (int)(calctypenum(9) * consM1 + calctypenum(8) * consM2)) + "\r\n");
		
		
		fw.close();
		
		return;
	}
	
	public static void main(String[] args) throws IOException {
		
		// Initialize
		fw = new FileWriter("EpidemicOutput.txt");
		fw.write(String.format("%s", "day") + "\t" + String.format("%s", "normal") + "\t" + String.format("%s", "allinf") + "\t" +
				String.format("%s", "alliso") + "\t" + String.format("%s", "alldeath") + "\t" + String.format("%s", "newinf") + "\t" +
				String.format("%s", "newdead") + "\t" + String.format("%s", "7inf") + "\t" + String.format("%s", "7dead") + "\t" +
				String.format("%s", "M") + "\r\n");
		fw.close();
		
		
		
		map = NewGrid();
		
		for(int i = 0; i < tottype; i ++)
			needarray[i] = 0;
		
		// Config the population
		
		init();
		randomSetPeople(map, needarray);
		writetodaydatatofile();
		
		// Visualization
			
		frame = new JFrame();
		frame.setTitle("Graph");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		drawPanel = new MyDrawPanel();
		drawPanel.setdataflag(false);
		frame.getContentPane().add(BorderLayout.CENTER, drawPanel);
		frame.setSize(SN * plsize, SM * plsize);
		frame.setVisible(true);
		drawPanel.setMap(map);
		
		
		dataFrame = new JFrame();
		dataFrame.setTitle("Data");
		dataFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		dataPanel = new MyDrawPanel();
		dataPanel.setLayout(null);
		dataPanel.setdataflag(true);
		dataFrame.getContentPane().add(dataPanel);
		dataFrame.setSize(600, 400);
		dataFrame.setVisible(true);
		
		
		ta = new TextArea();
		button = new Button("Go");
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub

				int dura = Integer.parseInt(ta.getText());
				
				for(int i = 0; i < dura; i++){
					
					caldays++;
					
					newinfected[caldays] = 0;
					newdeath[caldays] = 0;
					
					MoveTheMap();
					RunTheMap();
					RefreshTheMap();
					try {
						writetodaydatatofile();
					} catch (IOException e) {
						e.printStackTrace();
					}
					drawPanel.repaint();
					dataPanel.repaint();
				}
				
				
			}
		});
		
		ta.setSize(100, 50);
		ta.setLocation(20, 250); 
		button.setSize(50, 50);
		button.setLocation(200, 250);
		
		dataPanel.add(ta);
		dataPanel.add(button);
		
		drawPanel.repaint();
		dataPanel.repaint();

		return;
	}

}
