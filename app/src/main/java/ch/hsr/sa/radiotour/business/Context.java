package ch.hsr.sa.radiotour.business;

import ch.hsr.sa.radiotour.dataaccess.interfaces.IRiderRepository;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.repositories.RiderRepository;

public class Context {
    private RiderRepository riderRepository;
    private IRiderRepository.OnSaveRiderCallback onSaveRiderCallback;

    public Context(){
        this.riderRepository = new RiderRepository();
    }

    public void addRider(Rider rider){
        riderRepository.addRider(rider, onSaveRiderCallback);
    }
}
