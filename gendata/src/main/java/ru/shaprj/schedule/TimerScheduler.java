package ru.shaprj.schedule;
/*
 * Created by O.Shalaevsky on 26.04.2020
 */

import java.util.Timer;
import java.util.TimerTask;

public class TimerScheduler implements Scheduler {

    private final Runnable task;
    private final int millsPeriod;
    private Timer timer;

    public TimerScheduler(int millsPeriod, Runnable task) {

        this.task = task;
        this.millsPeriod = millsPeriod;
    }

    @Override
    public void schedule() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                task.run();
            }
        };

        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(timerTask, 0, millsPeriod);
    }
}
