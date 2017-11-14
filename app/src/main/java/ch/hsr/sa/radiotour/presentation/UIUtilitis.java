package ch.hsr.sa.radiotour.presentation;

import ch.hsr.sa.radiotour.R;

public final class UIUtilitis {

    public UIUtilitis() {

    }
    public static int getCountryFlag(String countryCode) {
        int drawable = 0;
        switch (countryCode) {
            case "SUI":
                drawable = R.drawable.ch;
                break;
            case "USA":
                drawable = R.drawable.us;
                break;
            case "POL":
                drawable = R.drawable.pl;
                break;
            case "GER":
                drawable = R.drawable.de;
                break;
            case "COL":
                drawable = R.drawable.co;
                break;
            case "ESP":
                drawable = R.drawable.es;
                break;
            case "DEN":
                drawable = R.drawable.dk;
                break;
            case "KAZ":
                drawable = R.drawable.kz;
                break;
            case "ITA":
                drawable = R.drawable.it;
                break;
            case "BEL":
                drawable = R.drawable.be;
                break;
            case "AUS":
                drawable = R.drawable.au;
                break;
            case "LTU":
                drawable = R.drawable.lt;
                break;
            case "FRA":
                drawable = R.drawable.fr;
                break;
            case "NOR":
                drawable = R.drawable.no;
                break;
            case "CAN":
                drawable = R.drawable.ca;
                break;
            case "SVK":
                drawable = R.drawable.sk;
                break;
            case "CZE":
                drawable = R.drawable.cz;
                break;
            case "AUT":
                drawable = R.drawable.at;
                break;
            case "GBR":
                drawable = R.drawable.gb;
                break;
            case "IRL":
                drawable = R.drawable.ie;
                break;
            case "NZL":
                drawable = R.drawable.nz;
                break;
            case "RSA":
                drawable = R.drawable.za;
                break;
            case "ERI":
                drawable = R.drawable.er;
                break;
            case "SLO":
                drawable = R.drawable.si;
                break;
            case "BLR":
                drawable = R.drawable.by;
                break;
            case "EST":
                drawable = R.drawable.ee;
                break;
            case "ETH":
                drawable = R.drawable.et;
                break;
            case "POR":
                drawable = R.drawable.pr;
                break;
            case "RUS":
                drawable = R.drawable.ru;
                break;
            case "NED":
                drawable = R.drawable.nl;
                break;
            default:
                break;
        }
        return drawable;
    }
}
