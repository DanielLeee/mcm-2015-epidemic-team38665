import java.awt.BorderLayout;
import java.awt.Frame;
import java.io.*;
import java.util.Arrays;
import java.math.*;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class TransferSystem {

	
	public static JFrame frame;
	public static MyDrawPanel drawPanel;
	
	public static int MAXN = 200;
	public static int PREK = 5;
	public static int n = 0;
	
	public static Area[] areas = null;
	public static int[] pres = null;
	public static int[] meds = null;
	
	
	public static double calcdist(Area[] areas, int a, int b){
		
		double dist = Math.sqrt( (areas[a].x - areas[b].x) * (areas[a].x - areas[b].x) + 
					  (areas[a].y - areas[b].y) * (areas[a].y - areas[b].y));
		return dist;
	}
	
	public static double min(double x, double y){
		
		if(x<y) 
			return x;
		else {
			return y;
		}
	}
	
	
	public static void main(String[] args) throws Exception {

		// Declaration of useful information
		n = 0; // How many areas
		int i;
		
		
		
		// Input the data from the input file
		BufferedReader br = null;
		String inputString = null;
		
		
		// Calculate n
		br = new BufferedReader(new FileReader("input"));
		while ((inputString = br.readLine()) != null) {
			n ++;
        }
		areas = new Area[n];
		
		// Get information
		br.close();
		br = new BufferedReader(new FileReader("input"));
		i = 0;
		while ((inputString = br.readLine()) != null) {
            String[] paralist = inputString.split("\\s+");
            double x, y, s;
            x = Double.valueOf(paralist[1]);
            y = Double.valueOf(paralist[2]);
            s = Double.valueOf(paralist[3]);
            areas[i] = new Area(x, y, s);
            i ++;
        }
		
		Arrays.sort(areas, new AreaComparator());
		br.close();
		
		/*for(i = 0; i < n; i ++) {
			System.out.println(areas[i].x + " " + areas[i].y + " " + areas[i].ps);
		}*/
		
		pres = new int[n];
		meds = new int[n];
		pres[0] = -1;
		for(i = 1; i <= PREK; i ++) {
			pres[i] = 0;
		}
		
		for(i = PREK + 1; i < n; i ++) {
			double mind = Double.MAX_VALUE;
			int mink = 0;
			double d;
			for(int j = 1; j <= PREK; j ++ ){
				if((d = calcdist(areas, i, j)) < mind){
					mind = d; 
					mink = j;
				}
					
			}
			pres[i] = mink;
		}
		
		for(i = 0; i < n; i ++) {
			meds[i] = 0;
		}
		
		for(i = n - 1; i > 0; i --) {
			meds[i] += areas[i].ps;
			meds[pres[i]] += meds[i];
		}
		meds[0] += areas[0].ps;
		
		// Regulate the coordinates
		double dx = Double.MAX_VALUE;
		double dy = Double.MAX_VALUE;
		
		for(i = 0; i < n; i ++) {
			double r = Math.sqrt(areas[i].s / Math.PI);
			dx = min(dx, areas[i].x - r);
			dy = min(dy, areas[i].y - r);
		}
		
		for(i = 0; i < n; i ++) {
			dx = min(dx, areas[i].x - areas[i].r);
			dy = min(dy, areas[i].y - areas[i].r);
		}
		
		for(i = 0; i < n; i ++) {
			areas[i].x -= dx;
			areas[i].y -= dy;
		}
		
		// Output the distance matrix
		frame = new JFrame();
		frame.setTitle("Graph");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		drawPanel = new MyDrawPanel();
		frame.getContentPane().add(BorderLayout.CENTER, drawPanel);
		frame.setSize(800, 600);
		frame.setVisible(true);
		
		
		return;
	}

}
