package net.shiftstudios.soundnotify.helper;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by shiftpsh on 2015-07-19.
 */
public class AppIconHelper {

    public Context c;
    public PackageManager pm;

    public AppIconHelper(Context c) {
        this.c = c;
        this.pm = c.getPackageManager();
    }

    public Drawable loadIcon(String packagename) {
        File iconFilePath = c.getFileStreamPath("icon-" + packagename + ".png");
        if (iconFilePath.exists()) {
            try {
                return Drawable.createFromPath(iconFilePath.toString());
            } catch (Exception e) {
                return loadIconFromPackageInfo(packagename);
            }
        } else {
            return loadIconFromPackageInfo(packagename);
        }
    }

    public Drawable loadIconFromPackageInfo(String packagename){
        Drawable icon;
        Bitmap iconBitmap;

        try {
            icon = pm.getApplicationIcon(packagename);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        FileOutputStream out = null;

        try {
            out = c.openFileOutput("icon-" + packagename + ".png", Context.MODE_PRIVATE);
            iconBitmap = drawableToBitmap(icon);
            iconBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return icon;
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    
}
