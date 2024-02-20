package com.appzum.objectdetection;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

// AppCompatActivity, CameraActivity
public class MainActivityOld1 extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    final String TAG = "markzum";

    /*@Override
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
    }*/

    private CameraBridgeViewBase mOpenCvCameraView;
    public MainActivityOld1() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        if (OpenCVLoader.initLocal()) {
            Log.i(TAG, "OpenCV loaded successfully");
        } else {
            Log.e(TAG, "OpenCV initialization failed!");
            (Toast.makeText(this, "OpenCV initialization failed!", Toast.LENGTH_LONG)).show();
            return;
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);


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
    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }
    @Override
    public void onResume()
    {
        super.onResume();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.enableView();
    }
    /*@Override
    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(mOpenCvCameraView);
    }*/
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }
    @Override
    public void onCameraViewStarted(int width, int height) {
    }
    @Override
    public void onCameraViewStopped() {
    }
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        return inputFrame.rgba();
    }
}