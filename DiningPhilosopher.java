/**
* Authors: Chad Manning, Abraham Aldana
* Instrucor: Dr. Wang
* Course: CMPS 3390
* Created: March 19th, 2019
* Source: DiningPhilosopher.java
*/

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class DiningPhilosopher extends JFrame
{
   JPanel p = new JPanel();
   Graphics2D g;
   public DiningPhilosopher () {
	getContentPane().add(p);
	setSize(700, 700);
	p.setBackground(Color.black);
	setTitle("Dining Philosopher Problem: Multithread & Synchronization");
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
	g = (Graphics2D) p.getGraphics();
        g.setFont(new Font("Roman", Font.BOLD, 20));
	try { Thread.sleep(100); } catch(Exception e) {}
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setBackground(Color.black);
        g.setXORMode ( Color.green);
   }
   public static void main( String arg[] ) {
      DiningPhilosopher app = new DiningPhilosopher();
      //PhilosopherMonitor mtr = new PhilosopherMonitor( app.g ) ;
      for( int i = 0; i < 5; ++i )
		   new Philosopher(i, app.g ).start();
   }
}

class PhilosopherUtil
{
   static boolean forkIsOnTable[] = {true, true, true, true, true};
   static String philosopherName[] = {"SOCRATES", "PLATO", "PYTHAGORAS", "DESCARTES", "CONFUCIUS"};
   static int x[] = {300, 500, 500, 100, 100};
   static int y[] = {50, 150, 350, 350, 150};

   synchronized static void simulate (int philosopher, String state, Graphics2D g) {
      g.drawString(state, x[philosopher], y[philosopher] + 20);
   }
}

class Philosopher extends Thread
{
   String state;
   int id, leftFork, rightFork;
   Graphics2D g;

   Philosopher (int philosopher, Graphics2D g) {
      id = philosopher;
      leftFork = (id < 1) ? 4 : id-1;
      rightFork = id;
      this.g = g;
      state = "THINKING";
      drawName(id);
   }

   private void drawName (int philosopher) {
      g.drawString(PhilosopherUtil.philosopherName[id],
       PhilosopherUtil.x[id], PhilosopherUtil.y[id]);
   }

   private void getForks() {
      if (PhilosopherUtil.forkIsOnTable[rightFork] &&
         PhilosopherUtil.forkIsOnTable[leftFork]) {
            PhilosopherUtil.forkIsOnTable[rightFork] = false;
            PhilosopherUtil.forkIsOnTable[leftFork] = false;
            state = "EATING";
	    eat();
         }
   }
   
   private void eat() {
      // after 5 seconds, put forks down
      Timer timer = new Timer();
      timer.schedule(new TimerTask() {
         // @Override
         public void run() {
            PhilosopherUtil.forkIsOnTable[rightFork] = true;
            PhilosopherUtil.forkIsOnTable[leftFork] = true;
            state = "THINKING";
         }
      }, 5000);
   }

   public void run () {
      while (true) {
         PhilosopherUtil.simulate(id, state, g);
         if (state == "THINKING") {
            getForks();
         }
      }
   }
}
