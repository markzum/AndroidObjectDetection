package com.appzum.objectdetection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

public class UdpReceiver implements Runnable {
    private static final String TAG = "UdpReceiver";
    private static final int PORT = 24236;
    private static final int BUFFER_SIZE = 65536;

    private DatagramSocket socket;
    private Handler handler;

    public UdpReceiver(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            socket = new DatagramSocket(PORT);
            socket.setBroadcast(true);

            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            while (true) {
                socket.receive(packet);

                byte[] data = new byte[packet.getLength()];
                System.arraycopy(packet.getData(), 0, data, 0, packet.getLength());

                outputStream.write(data, 0, data.length);

                // Проверяем, является ли последний пакет последним фрагментом кадра
                if (data[data.length - 1] == -1) {
                    byte[] imageData = outputStream.toByteArray();
                    outputStream.reset();

                    MatOfByte matOfByte = new MatOfByte(imageData);
                    Mat mat = Imgcodecs.imdecode(matOfByte, Imgcodecs.IMREAD_COLOR);

                    Message message = handler.obtainMessage();
                    message.obj = mat;
                    message.sendToTarget();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error receiving UDP packet", e);
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }
}
