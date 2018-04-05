package ch.hsr.sa.radiotour.controller.api;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.widget.Toast;

import ch.hsr.sa.radiotour.dataaccess.models.Rider;

public final class PostHandler extends HandlerThread {
    private static Handler mHandler;
    private static Context mContext;

    public PostHandler(Context context) {
        super("PostHandler", 1);
        this.mContext = context;
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();

        mHandler = new Handler(getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Rider r = (Rider)msg.obj;
                Toast.makeText(mContext, String.valueOf(r.getStartNr()), Toast.LENGTH_LONG).show();
                int x = 0;
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