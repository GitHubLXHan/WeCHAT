package com.example.hany.wechat.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author 6小h
 * @e-mail 1026310040@qq.com
 * @date 2019/1/3 20:54
 * @filName MyService
 * @describe ...
 */
public class MyService extends Service {

    static String TAG = "MyService";
    private InfoBinder binder = new InfoBinder();
    private static Messenger mMessenger;
    private static boolean over = true;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mMessenger == null) {
            mMessenger = (Messenger) intent.getExtras().get("messenger");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class InfoBinder extends Binder {
        public void sendInfo(final String msg) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Socket socket = new Socket("192.168.137.1", 30000);
                        if (socket.isConnected()) {
                            Log.d(TAG, "run: sendInfo");
                            OutputStream os = socket.getOutputStream();
                            os.write(msg.getBytes("utf-8"));
                            os.flush();
                            os.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        public void receiveInfo() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d(TAG, "run: receiveInfo");
                        ServerSocket serverSocket = new ServerSocket(20000);
                        Socket socket = null;
                        while (over) {
                            socket = serverSocket.accept();
                            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            String line;
                            StringBuilder sb = new StringBuilder();

                            while ((line = br.readLine()) != null) {
                                sb.append(line);
                            }
                            if (!TextUtils.isEmpty(sb.toString())) {
                                Message message = Message.obtain();
                                message.what = 1;
                                message.obj =sb;
                                try {
                                    mMessenger.send(message);
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        socket.shutdownInput();
                        socket.close();
                        serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        public void stop() {
            stopSelf();
            over = false;
        }

    }

}
