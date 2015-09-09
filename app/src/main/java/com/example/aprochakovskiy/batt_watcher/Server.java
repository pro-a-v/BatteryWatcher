package com.example.aprochakovskiy.batt_watcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.InterruptedException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.ListView;

public class Server extends MainActivity {

    private ServerSocket serverSocket;
    Thread serverThread = null;
    DatabaseHandler db = new DatabaseHandler(this);

    // Лист на екране 1
    ArrayList<Battery> arrayOfBattary;
    MyBatteryAdapter itemsAdapter;
    ListView listView;
    // Лист на екране 2
    ArrayList<Battery> arrayOfBattary2;
    MyBatteryAdapter itemsAdapter2;
    ListView listView2;
    //-----------------------------
    Handler mHandler;
    Context context;

    public static final int SERVERPORT = 6000;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_battery);
        context = this;

        this.serverThread = new Thread(new ServerThread());
        this.serverThread.start();

        // Лист на екране
        arrayOfBattary = db.getAllContactsOdd();
        itemsAdapter = new MyBatteryAdapter(context, arrayOfBattary);
        listView = (ListView) findViewById(R.id.listView1);
        listView.setAdapter(itemsAdapter);

        arrayOfBattary2 = db.getAllContactsEven();
        itemsAdapter2 = new MyBatteryAdapter(context, arrayOfBattary2);
        listView2 = (ListView) findViewById(R.id.listView2);
        listView2.setAdapter(itemsAdapter2);
        //-----------------------------

        mHandler = new Handler();
       // new TimerBatteryList(5, this );

    }


    @Override
    protected void onStop() {
        super.onStop();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    class ServerThread implements Runnable {

        public void run() {
            Socket socket = null;
                try {
                    serverSocket = new ServerSocket(SERVERPORT);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            // поток для обновления екрана
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                            while(true) {
                                TimeUnit.MILLISECONDS.sleep(1000);
                                mHandler.post(updateList);

                            }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();

            // цикл сервера
            while (!Thread.currentThread().isInterrupted()) {

                if  (serverSocket.isClosed())
                try {
                    serverSocket = new ServerSocket(SERVERPORT);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    socket = serverSocket.accept();
                    CommunicationThread commThread = new CommunicationThread(socket);
                    new Thread(commThread).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    class CommunicationThread implements Runnable {

        private Socket clientSocket;

        private BufferedReader input;

        public CommunicationThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {

                String read = input.readLine();
                System.out.print(read);
                if (!read.isEmpty()) {
                    // BXXXYYYY    XXX - номер батареи      YYYY - значение напряжение
                    if (read.length()==8) {
                       // Запись в бд
                        if(read.substring(0, 1).equals("B")) {
                            String batt_num = read.substring(1,4);
                            String batt_val = read.substring(4,8);
                                if(TextUtils.isDigitsOnly(batt_num))
                                if(TextUtils.isDigitsOnly(batt_val)) {
                                    Battery battery = new Battery(Integer.parseInt(batt_num),Integer.parseInt(batt_val) );
                                        db.replaceContact(battery);
                                }
                        }
                    }
                }

                input.close();
                Thread.currentThread().interrupt();


            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    Runnable updateList = new Runnable() {
        public void run() {
            arrayOfBattary = db.getAllContactsOdd();
            itemsAdapter = new MyBatteryAdapter(context, arrayOfBattary);
            listView.setAdapter(itemsAdapter);

            arrayOfBattary2 = db.getAllContactsEven();
            itemsAdapter2 = new MyBatteryAdapter(context, arrayOfBattary2);
            listView2.setAdapter(itemsAdapter2);
        }
    };


    /*
    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params) {
            try {
                TimeUnit.SECONDS.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            itemsAdapter.notifyDataSetChanged();
            listView.setAdapter(itemsAdapter);
        }
    }
    */



}
