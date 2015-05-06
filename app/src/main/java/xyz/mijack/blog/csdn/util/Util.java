package xyz.mijack.blog.csdn.util;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.kenny.snackbar.SnackBar;
import com.kenny.snackbar.SnackBarItem;

import xyz.mijack.blog.csdn.R;

/**
 * Created by MiJack on 2015/5/1.
 */
public class Util {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static void showSnackBar(Activity activity, String message, int duration) {
        SnackBarItem sbi = new SnackBarItem.Builder(activity)
                .setMessage(message)
//                .setActionMessage(activity.getString(R.string.snack_ok))
//                .setActionMessageColor(activity.getResources().getColor(R.color.material_design_color_grey_100))
//                .setActionMessagePressedColor(activity.getResources().getColor(R.color.material_design_color_blue_500))
                .setSnackBarMessageColor(activity.getResources().getColor(R.color.material_design_color_grey_200))
                .setSnackBarBackgroundColor(activity.getResources().getColor(R.color.material_design_color_lightblue_500))
                .setDuration(duration)
                .build();
        SnackBar.show(activity, sbi);
    }
}
