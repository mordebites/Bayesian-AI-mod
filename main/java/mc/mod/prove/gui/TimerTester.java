package mc.mod.prove.gui;

import java.util.Timer;
import java.util.TimerTask;

public class TimerTester {
    public static void main(String[] args) {
    	long freq = 1000;
    	
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
            	System.out.println("helo");
            }
        }, 0, freq);
    }
}
