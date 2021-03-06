package ch.hsr.sa.radiotour.controller.api;


public final class UrlLink {
    public static final String RIDERS = "riderstageconnections/stages/";
    public static final String STAGES = "stages/";
    public static final String RACE  = "races/";
    public static final String JUDGEMENTS = "judgments/stages/";
    public static final String REWARDS = "rewards";
    public static final String STATES = "wo/cars.json";
    public static final String GLOBALSETTINGS = "settings";
    public static final String MAILLOTS = "/maillots/stages/";
    public static final String STATUSPAGE = "/status";
    public static final String RIDERSTAGECONNECTION = "riderstageconnections/";
    public static final String JUDGMENTRIDERCONNECTION = "judgmentriderconnections";
    public static final String RACEGROUPS = "racegroups";
    public static final String NOTIFICATIONS = "notifications";
    private UrlLink() {
        throw new IllegalStateException("Static class");
    }
}
