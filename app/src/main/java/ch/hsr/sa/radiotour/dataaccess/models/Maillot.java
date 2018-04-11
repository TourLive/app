package ch.hsr.sa.radiotour.dataaccess.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Maillot extends RealmObject {
    @LinkingObjects("maillotConnections")
    private final RealmResults<Stage> stages = null;
    @PrimaryKey
    private long id;
    @Required
    private String type;
    @Required
    private String name;
    @Required
    private String color;
    @Required
    private String partner;
    private RealmList<RiderStageConnection> mailRiderConnection;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public Rider getRider() {
        if (mailRiderConnection.isEmpty()) {
            return null;
        }
        return mailRiderConnection.first().getRiders();
    }

    public void setRider(Rider rider) {
        if (rider != null) {
            this.mailRiderConnection.clear();
            this.mailRiderConnection.add(rider.getRiderStages().first());
        }
    }
}
