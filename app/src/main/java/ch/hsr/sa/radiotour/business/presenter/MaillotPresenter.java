package ch.hsr.sa.radiotour.business.presenter;

import android.support.v4.app.Fragment;

import java.util.ArrayList;

import ch.hsr.sa.radiotour.business.presenter.interfaces.IMaillotPresenter;
import ch.hsr.sa.radiotour.dataaccess.interfaces.IMaillotRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Maillot;
import ch.hsr.sa.radiotour.dataaccess.repositories.MaillotRepository;

public class MaillotPresenter implements IMaillotPresenter {
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private static MaillotPresenter instance = null;
    private MaillotRepository maillotRepository = new MaillotRepository();

    private IMaillotRepository.OnSaveMaillotCallback onSaveMaillotCallback;

    public static MaillotPresenter getInstance() {
        if(instance == null){
            instance = new MaillotPresenter();
        }
        return instance;
    }

    public void addView(Fragment frag){
        this.fragments.add(frag);
    }


    @Override
    public void subscribeCallbacks() {
        onSaveMaillotCallback = new IMaillotRepository.OnSaveMaillotCallback() {
            @Override
            public void onSuccess() {
                for(Fragment frag : fragments){
                    // call specifc update function for each fragment type
                }
            }

            @Override
            public void onError(String message) {
                // Not needed and therefore not implemented
            }
        };
    }

    @Override
    public void unSubscribeCallbacks() {
        onSaveMaillotCallback = null;
    }


    @Override
    public void addMaillot(Maillot maillot) {
        maillotRepository.addMaillot(maillot, onSaveMaillotCallback);
    }

    @Override
    public void clearAllMaillots() {
        maillotRepository.clearAllMaillots();
    }
}
