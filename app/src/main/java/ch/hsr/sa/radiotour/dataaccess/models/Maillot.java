package ch.hsr.sa.radiotour.dataaccess.models;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Maillot extends RealmObject {
    @PrimaryKey
    private String id;

    private Integer dbIDd;
    @Required
    private String type;
    @Required
    private String name;
    @Required
    private String color;
    @Required
    private String partner;

    @LinkingObjects("stageConnections")
    private final RealmResults<Stage> stages = null;

    public String getId() {
        return id;
    }

    public Integer getDbIDd() {
        return dbIDd;
    }

    public void setDbIDd(Integer dbIDd) {
        this.dbIDd = dbIDd;
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
}
