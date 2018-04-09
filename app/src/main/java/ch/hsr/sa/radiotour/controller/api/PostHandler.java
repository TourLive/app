package ch.hsr.sa.radiotour.controller.api;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.models.JudgmentRiderConnection;
import ch.hsr.sa.radiotour.dataaccess.models.JudgmentRiderConnectionDTO;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;

public final class PostHandler extends HandlerThread {
    private static Handler mHandler;
    private static Context mContext;
    private static HashMap<String, Integer> classMapper;
    private static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

    public PostHandler(Context context) {
        super("PostHandler", 1);
        this.mContext = context;
        classMapper = new HashMap<>();
        classMapper.put("CreateRaceGroup", 1);
        classMapper.put("UpdateRiderStageConnection", 2);
        classMapper.put("UpdateRaceGroup", 3);
        classMapper.put("CreateJudgmentRiderConnection", 4);
        classMapper.put("DeleteJudgmentRiderConnection", 5);
        classMapper.put("DeleteRaceGroup", 6);
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();

        mHandler = new Handler(getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.arg1){
                    case 1:
                        RaceGroup aRG = (RaceGroup)msg.obj;
                        APIClient.postRaceGroup(gson.toJson(aRG));
                        Toast.makeText(mContext, "Racegroups has been added", Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        RiderStageConnection riderStageConnection = (RiderStageConnection)msg.obj;
                        APIClient.postRiderStageConnection(riderStageConnection.getId(), gson.toJson(riderStageConnection));
                        Toast.makeText(mContext, "RiderStageConnection has been updated", Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        RaceGroup rG = (RaceGroup)msg.obj;
                        APIClient.putRaceGroup(Long.parseLong(rG.getId()), gson.toJson(rG));
                        Toast.makeText(mContext, "RaceGroup has been updated", Toast.LENGTH_LONG).show();
                        break;
                    case 4:
                        JudgmentRiderConnection judgmentRiderConnection = (JudgmentRiderConnection)msg.obj;
                        APIClient.postJudgmentRiderConnection(gson.toJson(new JudgmentRiderConnectionDTO(judgmentRiderConnection)));
                        Toast.makeText(mContext, "Judgment has been added", Toast.LENGTH_LONG).show();
                        break;
                    case 5:
                        JudgmentRiderConnection jRC = (JudgmentRiderConnection)msg.obj;
                        APIClient.deleteJudgmentRiderConnection(jRC.getId());
                        Toast.makeText(mContext, "Judgment has been deleted", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    public static void sendMessage(Message msg) {
        mHandler.sendMessage(msg);
    }

    public static void makeMessage(String type, Object object) {
        Message message = Message.obtain();
        message.arg1 = classMapper.get(type);
        message.obj = object;
        PostHandler.sendMessage(message);
    }
}