package com.appzum.objectdetection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import android.Manifest;

public class MainActivity extends AppCompatActivity {

    final String TAG = "markzum";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isStoragePermissionGranted();

        // initLocal
        if (OpenCVLoader.initDebug()) {
            Log.i(TAG, "OpenCV loaded successfully");
        } else {
            Log.e(TAG, "OpenCV initialization failed!");
            (Toast.makeText(this, "OpenCV initialization failed!", Toast.LENGTH_LONG)).show();
            return;
        }

        String videoUrl = "/storage/emulated/0/Download/ping_vid2.mp4";

        VideoCapture videoCapture = new VideoCapture();
        videoCapture.open(videoUrl);

        Log.i(TAG, "onCreate: " + videoCapture.isOpened());
        if (videoCapture.isOpened()) {
            Mat frame = new Mat();

            while (videoCapture.read(frame)) {

                Log.i(TAG, "onCreate: read");
                // Обработка каждого кадра с помощью машинного зрения

                // Например, можно применить фильтр или алгоритм распознавания объектов

                // Код для обработки кадра

                // Отображение обработанного кадра
                // Это только пример, ты можешь заменить эту часть кода на свою реализацию
                Bitmap bitmap = Bitmap.createBitmap(frame.cols(), frame.rows(), Bitmap.Config.RGB_565);
                Utils.matToBitmap(frame, bitmap);

                // Отображение bitmap в ImageView или другом компоненте пользовательского интерфейса
            }
        } else {
            // Обработка ошибки открытия видео
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }
}