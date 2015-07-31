import java.awt.AWTException;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;
import com.theeyetribe.client.GazeManager;
import com.theeyetribe.client.GazeManager.ApiVersion;
import com.theeyetribe.client.GazeManager.ClientMode;
import com.theeyetribe.client.IGazeListener;
import com.theeyetribe.client.data.GazeData;

public class EyeMouse {

	/**
	 * @param args
	 */
	static int gx = 0;
	static int gy = 0;
	static int upbound = 30;
	static int downbound = 650;
	static int leftbound;
	static int rightbound;
	static boolean on = true;
	static int uselessNumber = 0;

	public static void main(String[] args) throws AWTException,
			InterruptedException {

		Robot robot = new Robot();
		Point b;

		final GazeManager gm = GazeManager.getInstance();
		boolean success = gm.activate(ApiVersion.VERSION_1_0, ClientMode.PUSH);

		JFrame f = new JFrame();
		f.setSize(600, 600);
		f.setBackground(Color.BLACK);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		f.setLocationRelativeTo(null);
		f.setResizable(true);
		f.setAlwaysOnTop(false);
		f.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if (on == false) {
					switch (e.getKeyCode()) {
					case KeyEvent.VK_UP:
						upbound = gy;
						System.out.println("upper bound set at y = " + upbound);
						break;
					case KeyEvent.VK_DOWN:
						downbound = gy;
						System.out.println("lower bound set at y = "
								+ downbound);
						break;
					case KeyEvent.VK_LEFT:
						leftbound = (int) MouseInfo.getPointerInfo()
								.getLocation().getX();
						System.out
								.println("left bound set at x = " + leftbound);
						break;
					case KeyEvent.VK_RIGHT:
						rightbound = 0;
						System.out.println("right bound set at x = "
								+ rightbound);
						break;
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_M) {
					if (on == true){
						on = false;
					}	
					else if (on == false){
						on = true;
					}
					System.out.println(on);
				}
			}
		});
		System.out.println("starting up");

		gm.addGazeListener(new IGazeListener() {

			@Override
			public void onGazeUpdate(GazeData gazeData) {

				gx = (int) gazeData.leftEye.smoothedCoordinates.x;
				gy = (int) gazeData.leftEye.smoothedCoordinates.y;

			}
		});

		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() { // gm.removeGazeListener(this);
				gm.deactivate();
			}
		});
		while (true) {
			uselessNumber++;
			uselessNumber--;
			while(on == false) {
				
				b = MouseInfo.getPointerInfo().getLocation();

				int x = (int) b.getX();
				int y = (int) b.getY();
				 

				if (gx == 0 && gy == 0) {
					//robot.mouseMove(x, y);
					//System.out.println("Not Tracking");
				} else {
					if (gx < -20) {
						robot.mouseMove(x - 4, y);
					} else if (gx > 1200) {
						robot.mouseMove(x + 4, y); 
					} else if (gy < upbound) {
						robot.mouseMove(x, y - 4);
					} else if (gy > downbound) {
						robot.mouseMove(x, y + 4);
					} else {
					}
					Thread.sleep(10);
				}
				
			}
			if(on)
				System.out.println(gy);
		}

	}

	public static void move(Robot robot, double x, double y) {
		robot.mouseMove((int) x, (int) y);
	}

}
