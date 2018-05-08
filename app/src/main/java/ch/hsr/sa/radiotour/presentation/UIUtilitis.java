package ch.hsr.sa.radiotour.presentation;

import java.util.HashMap;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import io.realm.RealmList;

public final class UIUtilitis {

    private UIUtilitis() {
        // To Hide public Constructor
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
            case " ITA":
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
                drawable = R.drawable.pt;
                break;
            case "RUS":
                drawable = R.drawable.ru;
                break;
            case "NED":
                drawable = R.drawable.nl;
                break;
            case "U":
                drawable = R.drawable.u;
                break;
            default:
                break;
        }
        return drawable;
    }

    public static int getLastDigit(int startNr) {
        return startNr % 10;
    }

    public static int getFirstDigit(int startNr) {
        int firstNumber;
        if (startNr < 10) {
            firstNumber = 0;
        } else {
            String numberString = Integer.toString(startNr);
            if (startNr < 100) {
                firstNumber = Integer.parseInt(numberString.substring(0, 1));
            } else {
                firstNumber = Integer.parseInt(numberString.substring(0, 2));
            }
        }
        return firstNumber;
    }

    public static HashMap<Integer, Integer> getCountsPerLine(RealmList<Rider> riders) {
        HashMap<Integer, Integer> number = new HashMap<>();
        if (!riders.isEmpty()) {
            for (Rider r : riders) {
                int startnr = r.getStartNr();
                int firstNumber = UIUtilitis.getFirstDigit(startnr);
                int temp = 1;
                if (number.containsKey(firstNumber)) {
                    temp = number.get(firstNumber) + 1;
                }
                number.put(firstNumber, temp);
            }
        }
        number.put(30, 1);
        return number;
    }
}
