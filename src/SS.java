import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.util.Calendar;
import java.util.Date;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


/**
 * This is a sample class to execute scheduler on specific date based on
 * <code>java.util.Calendar</code>. Over here, <code>executor</code> is a
 * runnable which run on everyday basis from 8:00 AM to 5:00 PM.
 *
 * @author Chintan Patel
 */

public class SS {

    public static void main(String[] args) {

        // Create a calendar instance
        Calendar calendar = Calendar.getInstance();

        // Set time of execution. Here, we have to run every day 4:20 PM; so,
        // setting all parameters.
        calendar.set(Calendar.HOUR, 2);
        calendar.set(Calendar.MINUTE, 15);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.AM_PM, Calendar.PM);

        Long currentTime = new Date().getTime();

        // Check if current time is greater than our calendar's time. If So,
        // then change date to one day plus. As the time already pass for
        // execution.
        if (calendar.getTime().getTime() < currentTime) {
            calendar.add(Calendar.DATE, 1);
        }

        // Calendar is scheduled for future; so, it's time is higher than
        // current time.
        long startScheduler = calendar.getTime().getTime() - currentTime;

        // Setting stop scheduler at 4:21 PM. Over here, we are using current
        // calendar's object; so, date and AM_PM is not needed to set
        calendar.set(Calendar.HOUR, 2);
        calendar.set(Calendar.MINUTE, 16);
        calendar.set(Calendar.AM_PM, Calendar.PM);

        // Calculation stop scheduler
        long stopScheduler = calendar.getTime().getTime() - currentTime;

        // Executor is Runnable. The code which you want to run periodically.
        Runnable task = new Runnable() {

            @Override
            public void run() {

                System.out.println("test");
                String cmd = "open /Application/Safari.app";
                //Process p = Runtime.getRuntime().exec(cmd);

                System.out.println("Cmd = " + cmd);
                ProcessBuilder pb = new ProcessBuilder("open", "-a", cmd);
                pb.redirectErrorStream(true);
                try {
                    Process p = pb.start();
                    Thread t = new Thread(new InputStreamConsumer(p.getInputStream()));
                    t.start();
                    int exitCode = p.waitFor();
                    t.join();
                    System.out.println("Exited with " + exitCode);
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            public static class InputStreamConsumer implements Runnable {

                private InputStream is;

                public InputStreamConsumer(InputStream is) {
                    this.is = is;
                }

                @Override
                public void run() {
                    int read = -1;
                    try {
                        while ((read = is.read()) != -1) {
                            System.out.print((char)read);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

            }

        }

            }
        };

        // Get an instance of scheduler
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        // execute scheduler at fixed time.
        scheduler.scheduleAtFixedRate(task, startScheduler, stopScheduler, MILLISECONDS);
    }
}

