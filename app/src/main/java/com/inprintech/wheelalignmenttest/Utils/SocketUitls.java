package com.inprintech.wheelalignmenttest.Utils;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketUitls {
    private static final String TAG = "SocketUitls";

    private String ip = "192.168.4.1";
    private int port = 1001;
    private boolean socketStatus; //是否打开Socket标志
    private volatile boolean threadStatus; //线程状态，为了安全终止线程
    private Socket socket = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    private ReadThread readThread;

    /**
     * 连接
     *
     * @return
     */
    public boolean openSocket() {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip, Integer.valueOf(port)), 2000);
            if (socket.isConnected()) {
                this.socketStatus = true;
                threadStatus = false;
                outputStream = socket.getOutputStream();
                inputStream = socket.getInputStream();
                sendOrder(Constants.measure());
                readThread = new ReadThread();
                readThread.start();
                return true;
            } else {
                socketStatus = false;
                threadStatus = true;
                return false;
            }
        } catch (IOException e) {
            Log.e(TAG, "openSocket: Socket异常：" + e.toString());
            e.printStackTrace();
            socketStatus = false;
            threadStatus = true;
            return false;
        }
    }

    /**
     * 断开
     */
    public void closeSocket() {
        try {
            if (socketStatus) {
                inputStream.close();
                outputStream.close();
                this.socketStatus = false;
                threadStatus = true;
                socket.close();
                if (readThread != null) {
                    readThread.interrupt();
                    readThread = null;
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "closeSerialPort: 关闭Socket异常：" + e.toString());
            return;
        }
    }

    /**
     * 发送
     *
     * @param order
     */
    public void sendOrder(final byte[] order) {
        if (socket != null && socket.isConnected()) {
            try {
                if (outputStream != null && order.length > 0) {
                    outputStream.write(order);
                    outputStream.flush();
                    Log.i(TAG, "sendOrder: --发送成功--" + UITools.bytesToHexString(order));
                } else {
                    return;
                }
            } catch (IOException e) {
                Log.e(TAG, "sendOrder: Socket异常：" + e.toString());
                e.printStackTrace();
            }
        }
    }

    /* 接受数据 */
    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (!threadStatus) {
                //Log.i(TAG, "run:  进入接收数据线程");
                byte[] data = new byte[26];
                int size = 0;
                try {
                    size = inputStream.read(data);
                    if (size > 0) {
                        Log.i(TAG, "run: 接收到的数据--" + UITools.bytesToHexString(data));
                        Log.i(TAG, "run: 数据大小--" + size);
                        onDataReceiveListener.onDataReceive(data, size);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public OnDataReceiveListener onDataReceiveListener = null;

    public interface OnDataReceiveListener {
        void onDataReceive(byte[] buffer, int size);
    }

    public void setOnDataReceiveListener(OnDataReceiveListener dataReceiveListener) {
        onDataReceiveListener = dataReceiveListener;
    }
}
