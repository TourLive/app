package ch.hsr.sa.radiotour.dataaccess.models;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class RiderState extends RealmObject {
    @PrimaryKey
    private String id;
    @Required
    private String type;

    @LinkingObjects("riderState")
    private final RiderStageConnection riderState = null;

    public String getId() {
        return id;
    }

    public RiderStateType getType() {
        return RiderStateType.valueOf(type);
    }

    public void setType(RiderStateType type) {
        this.type = type.toString();
    }
}
