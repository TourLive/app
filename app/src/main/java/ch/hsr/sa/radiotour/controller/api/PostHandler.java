package ch.hsr.sa.radiotour.controller.api;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.widget.Toast;

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
                Toast.makeText(mContext, msg.toString(), Toast.LENGTH_SHORT).show();
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