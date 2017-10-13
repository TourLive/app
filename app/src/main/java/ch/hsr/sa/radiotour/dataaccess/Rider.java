package ch.hsr.sa.radiotour.dataaccess;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Dom on 12.10.2017.
 */

public class Rider extends RealmObject {

    @PrimaryKey
    private String id;

    private int startNr;
    @Required
    private String name;
    @Required
    private String country;

    public int getStartNr() {
        return startNr;
    }

    public void setStartNr(int startNr) { this.startNr = startNr; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
