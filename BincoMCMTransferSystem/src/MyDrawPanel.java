import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class MyDrawPanel extends JPanel {

	
	
	public void paint(Graphics g) {
		
		super.paint(g);
		
		Area[] areas = TransferSystem.areas;
		int n = TransferSystem.n;
		g.setColor(Color.blue);
		for(int i = 0; i < n; i++){
			g.fillOval((int)areas[i].x, (int)areas[i].y, (int)areas[i].r * 2, (int)areas[i].r * 2);
		}
		
		for(int i = 1; i < n; i++){
			int pre = TransferSystem.pres[i];
			g.setColor(Color.black);
			g.drawLine((int)areas[pre].x, (int)areas[pre].y, (int)areas[i].x, (int)areas[i].y);
			
			int mpx = 0, mpy = 0;
			
			mpx = (int)(areas[pre].x + (areas[i].x - areas[pre].x) * 3 / 4);
			mpy = (int)(areas[pre].y + (areas[i].y - areas[pre].y) * 3 / 4);
			g.drawString(String.valueOf(TransferSystem.meds[i]), mpx, mpy);
			
			
		}
		
	}
	

}
