package com.appzum.objectdetection;

import static org.opencv.imgproc.Imgproc.rectangle;
import static org.opencv.imgproc.Imgproc.resize;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.videoio.VideoCapture;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

// AppCompatActivity, CameraActivity
public class MainActivity extends AppCompatActivity {

    final String TAG = "markzum";
    String videoChunkPath1 = Environment.getExternalStorageDirectory() + "/Download/object_detection_stream1.webm";
    String videoChunkPath2 = Environment.getExternalStorageDirectory() + "/Download/object_detection_stream2.webm";

    boolean isOnlyCarsTracking = false;
    boolean isOnlyPeopleTracking = false;
    AtomicBoolean isOneObjectTracking = new AtomicBoolean(false);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        setContentView(R.layout.activity_main);

        ImageView trackAllBtn = findViewById(R.id.trackAllBtn);
        ImageView trackCarsBtn = findViewById(R.id.trackCarsBtn);
        ImageView trackPeopleBtn = findViewById(R.id.trackPeopleBtn);

        trackAllBtn.setOnClickListener(v -> {
            isOneObjectTracking.set(false);
            isOnlyCarsTracking = false;
            isOnlyPeopleTracking = false;
        });
        trackCarsBtn.setOnClickListener(v -> {
            isOneObjectTracking.set(false);
            isOnlyCarsTracking = true;
            isOnlyPeopleTracking = false;
        });
        trackPeopleBtn.setOnClickListener(v -> {
            isOneObjectTracking.set(false);
            isOnlyCarsTracking = false;
            isOnlyPeopleTracking = true;
        });

        ImageView screen = findViewById(R.id.videoPlayer);

        isStoragePermissionGranted();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        // Realtime cam stream
        /*Thread thread = new Thread(() -> {
            Log.i(TAG, "onCreate: 1");
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
            Log.i(TAG, "onCreate: 2");

            while (true) {
                Log.i(TAG, "onCreate: 3");

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
                Log.i(TAG, "onCreate: 4");

                VideoCapture videoCapture = new VideoCapture();
                videoCapture.open(currentVideoChunk);
                Log.i(TAG, "onCreate: 5");

                int framesCount = 0;

                if (videoCapture.isOpened()) {
                    Mat frame = new Mat();
                    Log.i(TAG, "onCreate: 6");

                    while (videoCapture.read(frame)) {
                        Log.i(TAG, "onCreate: 7");

                        // Обработка каждого кадра с помощью машинного зрения

                        // Например, можно применить фильтр или алгоритм распознавания объектов

                        // Код для обработки кадра

                        // Отображение обработанного кадра
                        // Это только пример, ты можешь заменить эту часть кода на свою реализацию
                        //if (framesCount >= 150) continue;

                        Bitmap bitmap = Bitmap.createBitmap(frame.cols(), frame.rows(), Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(frame, bitmap);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            if (bitmap.getColor(10, 10).toString().equals("Color(0.0, 0.6039216, 0.0, 1.0, sRGB IEC61966-2.1)") &&
                                    bitmap.getColor(100, 100).toString().equals("Color(0.0, 0.6039216, 0.0, 1.0, sRGB IEC61966-2.1)")) {
                                continue;
                            }
                        } else {
                            if (framesCount >= 150) continue;
                        }

                        // Отображение bitmap в ImageView или другом компоненте пользовательского интерфейса
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

                videoCapture.release();

            }

        });

        thread.start();*/

        // From video
        @SuppressLint("ClickableViewAccessibility") Thread thread = new Thread(() -> {
            // initLocal
            if (OpenCVLoader.initDebug()) {
                Log.i(TAG, "OpenCV loaded successfully");
            } else {
                Log.e(TAG, "OpenCV initialization failed!");
                return;
            }

            String videoUrl = "/storage/emulated/0/Download/cam_vid_1.mp4";


            /*InputStream is = getResources().openRawResource(R.raw.haarcascade_cars);
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            File cascFile = new File(cascadeDir, "haarcascade_cars.xml");

            FileOutputStream fos;
            try {
                fos = new FileOutputStream(cascFile);

                byte buffer[] = new byte[4096];
                int bytesRead;
                while((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }

                is.close();
                fos.close();

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }*/


            while (true) {

                VideoCapture videoCapture = new VideoCapture();
                videoCapture.open(videoUrl);

                if (videoCapture.isOpened()) {
                    Mat frame = new Mat();

                    int framesCount = 0;

                    Yolov8Ncnn yolov8ncnn = new Yolov8Ncnn();
                    yolov8ncnn.loadModel(getAssets(), 1, 1);

                    ArrayList<DetectObject> objects = new ArrayList<>();

                    screen.setOnTouchListener((v, event) -> {
                                int x = (int) event.getX();
                                int y = (int) event.getY();

                                // Is any object from objects is clicked.
                                for (DetectObject object : objects) {

                                    if ((object.rect.x < x && x < (object.rect.x + object.rect.width)) &&
                                            (object.rect.y < y && y < (object.rect.y + object.rect.height))) {
                                        Log.i(TAG, "onCreate: FOUND!!!");

                                        for (DetectObject object2 : objects) {
                                            object2.setTracking(false);
                                        }

                                        object.setTracking(true);

                                        isOneObjectTracking.set(true);
                                        isOnlyCarsTracking = false;
                                        isOnlyPeopleTracking = false;

                                        break;
                                    }
                                }

                                return false;
                            }
                    );

                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int windowHeight = displayMetrics.heightPixels;
                    int windowWidth = displayMetrics.widthPixels;

                    while (videoCapture.read(frame)) {
                        // Resize frame to phone window size. Save aspect ratio
                        int frameHeight = frame.rows();
                        int frameWidth = frame.cols();
                        float frameAspectRatio = (float) frameWidth / frameHeight;
                        resize(frame, frame, new Size((int) (windowHeight * frameAspectRatio), windowHeight));


                        DetectObject[] detectObjectsInput = yolov8ncnn.detect(frame.getNativeObjAddr());
                        ArrayList<DetectObject> detectObjects = new ArrayList<>(Arrays.asList(detectObjectsInput));

                        for (int i = 0; i < objects.size(); i++) {
                            // Find center for objects[i] and find min distance between centers of detectObjects
                            if (detectObjects.size() <= 0) {
                                objects.remove(i);
                                continue;
                            }
                            int minDistance = Integer.MAX_VALUE;
                            int minDistanceIndex = -1;
                            for (int j = 0; j < detectObjects.size(); j++) {
                                if (objects.get(i).label != detectObjects.get(j).label)
                                    continue;
                                int distance = (int) Math.sqrt(Math.pow(objects.get(i).rect.x + objects.get(i).rect.width / 2 - detectObjects.get(j).rect.x - detectObjects.get(j).rect.width / 2, 2) + Math.pow(objects.get(i).rect.y + objects.get(i).rect.height / 2 - detectObjects.get(j).rect.y - detectObjects.get(j).rect.height / 2, 2));
                                if (distance < minDistance) {
                                    minDistance = distance;
                                    minDistanceIndex = j;
                                }
                            }

                            // If minDistance is less than 100, update objects[i] with detectObjects[minDistanceIndex]
                            if (minDistance < 200) {
                                objects.get(i).rect = detectObjects.get(minDistanceIndex).rect;
                                objects.get(i).setLostCount(0);
                                objects.get(i).setVisible(true);
                                detectObjects.remove(minDistanceIndex);
                            } else {
                                if (objects.get(i).getLostCount() > DetectObject.lostThreshold) {
                                    objects.remove(i);
                                } else {
                                    objects.get(i).addLostCount();
                                    objects.get(i).setVisible(false);
                                }
                            }
                        }

                        for (DetectObject detectObject : detectObjects) {
                            objects.add(detectObject);
                        }

                        framesCount = 0;


                        // Draw objects rects
                        for (DetectObject object : objects) {
                            if (isOneObjectTracking.get() && !object.isTracking()) continue;
                            if (isOnlyCarsTracking &&
                                    !object.getLabelName().equals("car") &&
                                    !object.getLabelName().equals("truck")) continue;
                            if (isOnlyPeopleTracking && !object.getLabelName().equals("person")) continue;
                            if (!object.isVisible()) continue;

                            rectangle(frame, new Point(object.rect.x, object.rect.y),
                                    new Point(object.rect.x + object.rect.width, object.rect.y + object.rect.height),
                                    new Scalar(object.getColor()), 3);
                        }


                        /*HOGDescriptor hog = new HOGDescriptor();
                        //Получаем стандартный определитель людей и устанавливаем его нашему дескриптору
                        MatOfFloat descriptors = HOGDescriptor.getDefaultPeopleDetector();
                        hog.setSVMDetector(descriptors);
                        // Определяем переменные, в которые будут помещены результаты поиска ( locations - прямоугольные области, weights - вес (можно сказать релевантность) соответствующей локации)
                        MatOfRect locations = new MatOfRect();
                        MatOfDouble weights = new MatOfDouble();
                        // Собственно говоря, сам анализ фотографий. Результаты запишутся в locations и weights
                        hog.detectMultiScale(frame, locations, weights);

                        for (Rect car : locations.toArray()) {
                            //Log.i(TAG, "Rect: " + car.x + " " + car.y + " " + car.height + " " + car.width);
                            Imgproc.rectangle(frame, car, new Scalar(255, 0, 0));
                        }*/


                        //yolov8ncnn.detect(frame.getNativeObjAddr());
                        //Mat processed_frame = new Mat(yolov8ncnn.detect(frame.clone().getNativeObjAddr()));
                        /*Log.i(TAG, "FrameNum: " + framesCount);
                        Bitmap nb = Bitmap.createBitmap(frame.cols(), frame.rows(), Bitmap.Config.ARGB_8888);
                        Log.i(TAG, "onCreate: " + );
                        //nb.setPixels(yolov8ncnn.detect(frame.getNativeObjAddr()), 0, frame.cols(), 0, 0, frame.cols(), frame.rows());

                        Mat processed_frame = frame;
                        long x = 100;*/

                        /*Size sz = new Size(720,720);
                        Imgproc.resize(frame, frame, sz);*/


                        Bitmap bitmap = Bitmap.createBitmap(frame.cols(), frame.rows(), Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(frame, bitmap);

//                        yolov8ncnn.detect2(frame.getNativeObjAddr(), bitmap);
//                        Log.i(TAG, "onCreate: " + Arrays.toString(yolov8ncnn.detect(frame.getNativeObjAddr())));

                        // Отображение bitmap в ImageView или другом компоненте пользовательского интерфейса
                        runOnUiThread(() -> screen.setImageBitmap(bitmap));

                        /*try {
                            Thread.sleep(25);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }*/

                        framesCount++;
                    }

                } else {
                    // Обработка ошибки открытия видео
                }


                videoCapture.release();

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
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }


    public void downloadNextPart(String videoChunkPath) {
        // 720p
        String videoUrl = "https://rr5---sn-axq7sn7z.googlevideo.com/videoplayback?expire=1708461132&ei=7LfUZZymCruN_9EPkoyHuA8&ip=143.137.166.123&id=ybllmr12xXk.1&itag=247&aitags=242%2C243%2C244%2C247%2C248%2C271%2C278%2C313&source=yt_live_broadcast&requiressl=yes&xpc=EgVo2aDSNQ%3D%3D&spc=UWF9fySrqDXO3RhYiYms-idvzE_RJKxFNiDwPbLVt0xu9yo&vprv=1&live=1&hang=1&noclen=1&svpuc=1&mime=video%2Fwebm&gir=yes&keepalive=yes&fexp=24007246&c=ANDROID&sparams=expire%2Cei%2Cip%2Cid%2Caitags%2Csource%2Crequiressl%2Cxpc%2Cspc%2Cvprv%2Clive%2Chang%2Cnoclen%2Csvpuc%2Cmime%2Cgir&sig=AJfQdSswRAIgE2HGXtb0DtVjKAkK3Or9lwTcnchdeSrj5PrOgeL1WQMCIH3Bfyp0SeGzwQoeER3L4O5I9_lnAmNTLXynxAYgsY-m&redirect_counter=1&rm=sn-ab5ely7s&req_id=59fab8e5aafea3ee&cms_redirect=yes&cmsv=e&ipbypass=yes&mh=3D&mip=91.215.201.1&mm=44&mn=sn-axq7sn7z&ms=lva&mt=1708439789&mv=m&mvi=5&pl=24&lsparams=ipbypass,mh,mip,mm,mn,ms,mv,mvi,pl&lsig=APTiJQcwRgIhAMawyIdjj6PKun3g8ystD2uKFLyms4-gYRw9Udb6gOX-AiEAnMH6pno1od6jkpxgimgCzbOR6yV8grrv2p3yyH5_Zcw%3D";
        // 480p
        //String videoUrl = "https://rr5---sn-axq7sn7z.googlevideo.com/videoplayback?expire=1708461132&ei=7LfUZZymCruN_9EPkoyHuA8&ip=143.137.166.123&id=ybllmr12xXk.1&itag=244&aitags=242%2C243%2C244%2C247%2C248%2C271%2C278%2C313&source=yt_live_broadcast&requiressl=yes&xpc=EgVo2aDSNQ%3D%3D&spc=UWF9fySrqDXO3RhYiYms-idvzE_RJKxFNiDwPbLVt0xu9yo&vprv=1&live=1&hang=1&noclen=1&svpuc=1&mime=video%2Fwebm&gir=yes&keepalive=yes&fexp=24007246&c=ANDROID&sparams=expire%2Cei%2Cip%2Cid%2Caitags%2Csource%2Crequiressl%2Cxpc%2Cspc%2Cvprv%2Clive%2Chang%2Cnoclen%2Csvpuc%2Cmime%2Cgir&sig=AJfQdSswRgIhAI7-ZoaT-tQuu3BDsZuBs-gsJhAe7Ul1czJNAHExoutaAiEAgtce1vfUQ_R-0KqTLq9U3rpLhD6f0Omz317YdtTvba8%3D&redirect_counter=1&rm=sn-ab5ely7s&req_id=5960ed886008a3ee&cms_redirect=yes&cmsv=e&ipbypass=yes&mh=3D&mip=91.215.201.1&mm=44&mn=sn-axq7sn7z&ms=lva&mt=1708447712&mv=m&mvi=5&pl=24&lsparams=ipbypass,mh,mip,mm,mn,ms,mv,mvi,pl&lsig=APTiJQcwRgIhAKiI_nE-lOasR8GQDsnWHpt7heXDUnhmS4VQF71e9Nl8AiEA4bcumQzSrQnEy6Gy9U2ywixiKwK9DUhKdGnBBN-Ghng%3D";

        try {
            URL u = new URL(videoUrl);
            InputStream is = u.openStream();
            DataInputStream dis = new DataInputStream(is);

            byte[] buffer = new byte[1024];
            int length;

            FileOutputStream fos = new FileOutputStream(videoChunkPath);
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

    @Override
    public void onLowMemory() {
        Log.i(TAG, "onLowMemory: onLowMemory!!!");
        super.onLowMemory();
    }

    private String getAssetsPath(String file) {
        AssetManager assetManager = getAssets();
        BufferedInputStream inputStream;
        try {
            // read the defined data from assets
            inputStream = new BufferedInputStream(assetManager.open(file));
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
            // Create copy file in storage.
            File outFile = new File(getFilesDir(), file);
            FileOutputStream os = new FileOutputStream(outFile);
            os.write(data);
            os.close();
            // Return a path to file which may be read in common way.
            return outFile.getAbsolutePath();
        } catch (IOException ex) {
            Log.i(TAG, "Failed to upload a file");
        }
        return "";
    }
}