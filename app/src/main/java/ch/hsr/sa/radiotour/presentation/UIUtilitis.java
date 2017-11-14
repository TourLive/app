package ch.hsr.sa.radiotour.presentation;

import ch.hsr.sa.radiotour.R;

public final class UIUtilitis {

    public UIUtilitis() {

    }
    public static int getCountryFlag(String countryCode) {
        int drawable = 0;
        switch (countryCode) {
            case "SUI":
                drawable =  R.drawable.ch;
                break;
            case "USA":
                drawable =  R.drawable.us;
                break;
            case "POL":
                drawable =  R.drawable.pl;
                break;
            case "GER":
                drawable =  R.drawable.de;
                break;
            case "COL":
                drawable =  R.drawable.de;
                break;
            case "ESP":
                drawable =  R.drawable.de;
                break;
            case "DEN":
                drawable =  R.drawable.de;
                break;
            case "KAZ":
                drawable =  R.drawable.de;
                break;
            case "ITA":
                drawable =  R.drawable.de;
                break;
            case "BEL":
                drawable =  R.drawable.de;
                break;
            case "AUS":
                drawable =  R.drawable.de;
                break;
            case "LTU":
                drawable =  R.drawable.de;
                break;
            case "FRA":
                drawable =  R.drawable.de;
                break;
            case "NOR":
                drawable =  R.drawable.de;
                break;
            case "CAN":
                drawable =  R.drawable.de;
                break;
            case "SVK":
                drawable =  R.drawable.de;
                break;
            case "CZE":
                drawable =  R.drawable.de;
                break;
            case "AUT":
                drawable =  R.drawable.de;
                break;
            case "GBR":
                drawable =  R.drawable.de;
                break;
            case "IRL":
                drawable =  R.drawable.de;
                break;
            case "NZL":
                drawable =  R.drawable.de;
                break;
            case "RSA":
                drawable =  R.drawable.de;
                break;
            case "ERI":
                drawable =  R.drawable.de;
                break;
            case "SLO":
                drawable =  R.drawable.de;
                break;
            case "BLR":
                drawable =  R.drawable.de;
                break;
            case "EST":
                drawable =  R.drawable.de;
                break;
            case "ETH":
                drawable =  R.drawable.de;
                break;
            case "POR":
                drawable =  R.drawable.pr;
                break;
            case "RUS":
                drawable =  R.drawable.de;
                break;
        }
        return drawable;
    }
}
