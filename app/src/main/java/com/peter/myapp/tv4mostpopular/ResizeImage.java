package com.peter.myapp.tv4mostpopular;



import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ResizeImage {


    // Can only resize drawables. Bitmaps need to be converted. (or does it?)

    public static Drawable reSize(Drawable image) {

        if (image != null) {
            Bitmap b = ((BitmapDrawable) image).getBitmap();
            if(b == null){
                return image;
            }
            Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 300, 300, false);
            return new BitmapDrawable(Resources.getSystem(), bitmapResized);
        }
        return image;
    }



}
