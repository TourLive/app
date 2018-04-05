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
    private static HashMap<Integer, Object> classMapper;

    public PostHandler(Context context) {
        super("PostHandler", 1);
        this.mContext = context;
        classMapper = new HashMap<>();
        classMapper.put(1, RaceGroup.class);
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();

        mHandler = new Handler(getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (classMapper.get(msg.arg1).toString()){
                    case "RaceGroup.class":
                        RaceGroup raceGroup = (RaceGroup)msg.obj;
                        Toast.makeText(mContext, String.valueOf(raceGroup.getActualGapTime()), Toast.LENGTH_LONG).show();
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

}