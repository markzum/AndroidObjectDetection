package com.appzum.objectdetection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import android.Manifest;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

// AppCompatActivity, CameraActivity
public class MainActivity extends AppCompatActivity {

    final String TAG = "markzum";
    String videoChunkPath1 = Environment.getExternalStorageDirectory() + "/Download/object_detection_stream1.webm";
    String videoChunkPath2 = Environment.getExternalStorageDirectory() + "/Download/object_detection_stream2.webm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView screen = findViewById(R.id.imageView);

        isStoragePermissionGranted();

        Thread thread = new Thread(() -> {
            // initLocal
            if (OpenCVLoader.initLocal()) {
                Log.i(TAG, "OpenCV loaded successfully");
            } else {
                Log.e(TAG, "OpenCV initialization failed!");
                // (Toast.makeText(this, "OpenCV initialization failed!", Toast.LENGTH_LONG)).show();
                return;
            }

            //String videoUrl = "/storage/emulated/0/Download/ping_vid2.mp4";

            String currentVideoChunk = videoChunkPath1;
            downloadNextPart(videoChunkPath1);

            while (true) {

                Thread downloadThread;
                if (currentVideoChunk.equals(videoChunkPath1)) {
                    downloadThread = new Thread(() -> {
                        downloadNextPart(videoChunkPath2);
                    });
                } else {
                    downloadThread = new Thread(() -> {
                        downloadNextPart(videoChunkPath1);
                    });
                }
                downloadThread.start();

                VideoCapture videoCapture = new VideoCapture();
                videoCapture.open(currentVideoChunk);

                int framesCount = 0;

                if (videoCapture.isOpened()) {
                    Mat frame = new Mat();

                    while (videoCapture.read(frame)) {

                        // Обработка каждого кадра с помощью машинного зрения

                        // Например, можно применить фильтр или алгоритм распознавания объектов

                        // Код для обработки кадра

                        // Отображение обработанного кадра
                        // Это только пример, ты можешь заменить эту часть кода на свою реализацию
                        if (framesCount >= 150) continue;

                        Bitmap bitmap = Bitmap.createBitmap(frame.cols(), frame.rows(), Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(frame, bitmap);

                        // Отображение bitmap в ImageView или другом компоненте пользовательского интерфейса
                        Log.i(TAG, "onCreate: " + bitmap);
                        runOnUiThread(() -> screen.setImageBitmap(bitmap));

                        try {
                            Thread.sleep(25);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                        framesCount++;
                    }

                    Log.i(TAG, "run: " + framesCount);
                } else {
                    // Обработка ошибки открытия видео
                }

                if (currentVideoChunk.equals(videoChunkPath1)) {
                    currentVideoChunk = videoChunkPath2;
                } else {
                    currentVideoChunk = videoChunkPath1;
                }

                try {
                    downloadThread.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }

        });

        thread.start();
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }


    public void downloadNextPart(String videoChunkPath) {
        String videoUrl = "https://rr5---sn-axq7sn7z.googlevideo.com/videoplayback?expire=1708461132&ei=7LfUZZymCruN_9EPkoyHuA8&ip=143.137.166.123&id=ybllmr12xXk.1&itag=247&aitags=242%2C243%2C244%2C247%2C248%2C271%2C278%2C313&source=yt_live_broadcast&requiressl=yes&xpc=EgVo2aDSNQ%3D%3D&spc=UWF9fySrqDXO3RhYiYms-idvzE_RJKxFNiDwPbLVt0xu9yo&vprv=1&live=1&hang=1&noclen=1&svpuc=1&mime=video%2Fwebm&gir=yes&keepalive=yes&fexp=24007246&c=ANDROID&sparams=expire%2Cei%2Cip%2Cid%2Caitags%2Csource%2Crequiressl%2Cxpc%2Cspc%2Cvprv%2Clive%2Chang%2Cnoclen%2Csvpuc%2Cmime%2Cgir&sig=AJfQdSswRAIgE2HGXtb0DtVjKAkK3Or9lwTcnchdeSrj5PrOgeL1WQMCIH3Bfyp0SeGzwQoeER3L4O5I9_lnAmNTLXynxAYgsY-m&redirect_counter=1&rm=sn-ab5ely7s&req_id=59fab8e5aafea3ee&cms_redirect=yes&cmsv=e&ipbypass=yes&mh=3D&mip=91.215.201.1&mm=44&mn=sn-axq7sn7z&ms=lva&mt=1708439789&mv=m&mvi=5&pl=24&lsparams=ipbypass,mh,mip,mm,mn,ms,mv,mvi,pl&lsig=APTiJQcwRgIhAMawyIdjj6PKun3g8ystD2uKFLyms4-gYRw9Udb6gOX-AiEAnMH6pno1od6jkpxgimgCzbOR6yV8grrv2p3yyH5_Zcw%3D";

        try {
            URL u = new URL(videoUrl);
        InputStream is = u.openStream();
        DataInputStream dis = new DataInputStream(is);

        byte[] buffer = new byte[1024];
        int length;

        FileOutputStream fos = new FileOutputStream(new File(videoChunkPath));
        while ((length = dis.read(buffer)) > 0) {
            fos.write(buffer, 0, length);
        }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}