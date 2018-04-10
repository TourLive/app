package ch.hsr.sa.radiotour.controller.api;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.models.JudgmentRiderConnection;
import ch.hsr.sa.radiotour.dataaccess.models.JudgmentRiderConnectionDTO;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroupDTO;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.repositories.StageRepository;

public final class PostHandler extends HandlerThread {
    private static Handler mHandler;
    private static HashMap<String, Integer> classMapper;
    private static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private StageRepository stageRepository = new StageRepository();

    public PostHandler() {
        super("PostHandler", 1);
        classMapper = new HashMap<>();
        classMapper.put("UpdateRaceGroups", 1);
        classMapper.put("UpdateRiderStageConnection", 2);;
        classMapper.put("CreateJudgmentRiderConnection", 3);
        classMapper.put("DeleteJudgmentRiderConnection", 4);
        classMapper.put("UpdateRaceGroupTime", 5);
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();

        mHandler = new Handler(getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.arg1){
                    case 1:
                        List<RaceGroup> aRG = (ArrayList<RaceGroup>) msg.obj;
                        List<RaceGroupDTO> raceGroupDTOS = new ArrayList<>();
                        for (RaceGroup r : aRG) {
                            raceGroupDTOS.add(new RaceGroupDTO(r));
                        }
                        String json = gson.toJson(raceGroupDTOS);
                        long stageId = stageRepository.getStage().getId();
                        APIClient.postRaceGroups(stageId, gson.toJson(json));
                        break;
                    case 2:
                        RiderStageConnection riderStageConnection = (RiderStageConnection)msg.obj;
                        APIClient.postRiderStageConnection(riderStageConnection.getId(), gson.toJson(riderStageConnection));
                        break;
                    case 3:
                        JudgmentRiderConnection judgmentRiderConnection = (JudgmentRiderConnection)msg.obj;
                        APIClient.postJudgmentRiderConnection(gson.toJson(new JudgmentRiderConnectionDTO(judgmentRiderConnection)));
                        break;
                    case 4:
                        JudgmentRiderConnection jRC = (JudgmentRiderConnection)msg.obj;
                        APIClient.deleteJudgmentRiderConnection(jRC.getId());
                        break;
                    case 5:
                        RaceGroupDTO raceGroupDTO = new RaceGroupDTO((RaceGroup) msg.obj);
                        APIClient.putRaceGroup(raceGroupDTO.getId(), gson.toJson(raceGroupDTO));
                    default:
                        break;
                }
            }
        };
    }

    public static void sendMessage(Message msg) {
        mHandler.sendMessage(msg);
    }

    public static void makeMessage(String type, final Object object) {
        Message message = Message.obtain();
        message.arg1 = classMapper.get(type);
        message.obj = object;
        PostHandler.sendMessage(message);
    }
}