package ch.hsr.sa.radiotour.controller.api;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.widget.Toast;

import java.util.HashMap;

import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;

public final class PostHandler extends HandlerThread {
    private static Handler mHandler;
    private static Context mContext;
    private static HashMap<Integer, String> classMapper;

    public PostHandler(Context context) {
        super("PostHandler", 1);
        this.mContext = context;
        classMapper = new HashMap<>();
        classMapper.put(1, "RaceGroup");
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();

        mHandler = new Handler(getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Object type = classMapper.get(msg.arg1).toString();

                switch (classMapper.get(msg.arg1).toString()){
                    case "RaceGroup":
                        RaceGroup raceGroup = (RaceGroup)msg.obj;
                        Toast.makeText(mContext, "RaceGroupTime has changed to: " + convertLongToTimeString(raceGroup.getActualGapTime()), Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    public static void test(Message msg) {
        mHandler.sendMessage(msg);
    }

    public void taskTwo() {
        mHandler.sendEmptyMessage(2);
    }

    private String convertLongToTimeString(long time) {
        long resMinutes = time / 60;
        long resSeconds = time - (resMinutes * 60);
        if (resMinutes < 10 && resSeconds < 10) {
            return "0" + Long.toString(resMinutes) + ":0" + Long.toString(resSeconds);
        } else if (resMinutes < 10 && resSeconds >= 10) {
            return "0" + Long.toString(resMinutes) + ":" + Long.toString(resSeconds);
        } else if (resMinutes >= 10 && resSeconds >= 10) {
            return Long.toString(resMinutes) + ":" + Long.toString(resSeconds);
        } else {
            return Long.toString(resMinutes) + ":0" + Long.toString(resSeconds);
        }
    }

}