package com.appzum.objectdetection;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import org.opencv.core.Rect;
import org.opencv.video.Tracker;

public class DetectObject {
    // Static
    public static String[] class_names = {
            "person", "bicycle", "car", "motorcycle", "airplane", "bus", "train", "truck", "boat", "traffic light",
            "fire hydrant", "stop sign", "parking meter", "bench", "bird", "cat", "dog", "horse", "sheep", "cow",
            "elephant", "bear", "zebra", "giraffe", "backpack", "umbrella", "handbag", "tie", "suitcase", "frisbee",
            "skis", "snowboard", "sports ball", "kite", "baseball bat", "baseball glove", "skateboard", "surfboard",
            "tennis racket", "bottle", "wine glass", "cup", "fork", "knife", "spoon", "bowl", "banana", "apple",
            "sandwich", "orange", "broccoli", "carrot", "hot dog", "pizza", "donut", "cake", "chair", "couch",
            "potted plant", "bed", "dining table", "toilet", "tv", "laptop", "mouse", "remote", "keyboard", "cell phone",
            "microwave", "oven", "toaster", "sink", "refrigerator", "book", "clock", "vase", "scissors", "teddy bear",
            "hair drier", "toothbrush"
    };

    public static double[][] colors = {
        { 54,  67, 244},
        { 99,  30, 233},
        {176,  39, 156},
        {183,  58, 103},
        {181,  81,  63},
        {243, 150,  33},
        {244, 169,   3},
        {212, 188,   0},
        {136, 150,   0},
        { 80, 175,  76},
        { 74, 195, 139},
        { 57, 220, 205},
        { 59, 235, 255},
        {  7, 193, 255},
        {  0, 152, 255},
        { 34,  87, 255},
        { 72,  85, 121},
        {158, 158, 158},
        {139, 125,  96}
    };

    public static String getClassNameByClassId(int id) {
        return class_names[id];
    }

    public static double[] getColorByClassId(int id) {
        id = id % colors.length;
        return colors[id];
    }



    /* Object */
    public int label;
    public float prob;
    public Rect rect;
    public Tracker tracker;

    public DetectObject(int label, float prob, int x, int y, int width, int height) {
        this.label = label;
        this.prob = prob;
        this.rect = new Rect(x, y, width, height);
    }

    public DetectObject(int label, float prob, Rect rect) {
        this.label = label;
        this.prob = prob;
        this.rect = rect;
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    public String toString() {
        return String.format("label: %d, prob: %f, rect: %s", label, prob, rect);
    }

    public String getLabelName() {
        return class_names[label];
    }

    public double[] getColor() {
        return getColorByClassId(label);
    }

    public void setTracker(Tracker tracker) {
        this.tracker = tracker;
    }
}
