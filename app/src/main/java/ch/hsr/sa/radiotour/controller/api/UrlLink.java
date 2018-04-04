package ch.hsr.sa.radiotour.controller.api;


public final class UrlLink {
    public static final String RIDERS = "/riderStageConnection";
    public static final String STAGES = "stage/";
    public static final String RACE  = "race/";
    public static final String JUDGEMENTS = "api/masterdata/judgements/";
    public static final String STATES = "json_public/status.php";
    public static final String GLOBALSETTINGS = "settings";
    public static final String MAILLOTS = "/maillot/stage/";
    public static final String RIDERJERSEY = "json_public/riderjerseystartstage.php?stage=";
    public static final String STATUSPAGE = "/status";
    private UrlLink() {
        throw new IllegalStateException("Static class");
    }
}
