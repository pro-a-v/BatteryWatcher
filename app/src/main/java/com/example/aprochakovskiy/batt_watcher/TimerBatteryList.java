package com.example.aprochakovskiy.batt_watcher;

import java.util.Timer;
import java.util.TimerTask;

public class TimerBatteryList extends Server{

    Timer timer;
    Server server;

    public TimerBatteryList(int seconds,  Server server) {
        this.server = server;
        timer = new Timer();
        timer.schedule(new RemindTask(), seconds * 1000, seconds*1000);
    }

    class RemindTask extends TimerTask {
        public void run() {
            server.mHandler.post(server.updateList);
        }
    }

}
