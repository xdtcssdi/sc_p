package com.example.music163.common;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.music163.bean.Image;
import com.youth.banner.loader.ImageLoader;


public class GlideImageLoaderPath extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {

        Image s = (Image) path;
        String newPath = s.getPath();
        if (!newPath.startsWith("http")) {
            newPath = Url.UpImg + newPath;
        }
        Log.d(TAG, "displayImage: " + newPath);
        Glide.with(context).load(newPath).into(imageView);
    }

    private static final String TAG = "GlideImageLoaderPath";
}