package ch.hsr.sa.radiotour.controller.api;


public final class UrlLink {
    private UrlLink() {
        throw new IllegalStateException("Static class");
    }

    public static final String RIDERS = "api/masterdata/riders/stage/";
    public static final String STAGES = "public/stages/";
    public static final String JUDGEMENTS = "api/masterdata/judgements/";
    public static final String STATES = "json_public/status.php";
    public static final String GLOBALSETTINGS = "api/getAllGlobalSettings";
}
