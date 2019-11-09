package pl.tobynartowski.limfy.utils;

import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

public class ViewUtils {

    public static void makeFullscreen(Window window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.getAttributes().layoutInDisplayCutoutMode
                    = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

    }
}
