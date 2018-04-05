package ch.hsr.sa.radiotour.controller.api;


public final class UrlLink {
    public static final String RIDERS = "riderStageConnections/stages/";
    public static final String STAGES = "stages/";
    public static final String RACE  = "races/";
    public static final String JUDGEMENTS = "judgments/stages/";
    public static final String STATES = "json_public/status.php";
    public static final String GLOBALSETTINGS = "settings";
    public static final String MAILLOTS = "/maillots/stages/";
    public static final String RIDERJERSEY = "json_public/riderjerseystartstage.php?stage=";
    public static final String STATUSPAGE = "/status";
    private UrlLink() {
        throw new IllegalStateException("Static class");
    }
}
