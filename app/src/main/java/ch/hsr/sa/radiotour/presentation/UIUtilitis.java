package ch.hsr.sa.radiotour.presentation;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import io.realm.RealmList;

public final class UIUtilitis {

    private static final HashMap<String, Integer> countryFlagMap = new HashMap(){{
        put("AFG", R.drawable.af);
        put("ALB", R.drawable.al);
        put("ALG", R.drawable.dz);
        put("ASA", R.drawable.as);
        put("AND", R.drawable.ad);
        put("ANG", R.drawable.ao);
        put("ANT",R.drawable.ag);
        put("ARG",R.drawable.ar);
        put("ARM",R.drawable.am);
        put("ARU",R.drawable.aw);
        put("AUS",R.drawable.au);
        put("AUT",R.drawable.at);
        put("AZE",R.drawable.az);
        put("BAH",R.drawable.bs);
        put("BRN",R.drawable.bh);
        put("BAN",R.drawable.bd);
        put("BAR",R.drawable.bb);
        put("BLR",R.drawable.by);
        put("BEL",R.drawable.be);
        put("BIZ",R.drawable.bz);
        put("BEN",R.drawable.bj);
        put("BER",R.drawable.bm);
        put("BHU",R.drawable.bt);
        put("BOL",R.drawable.bo);
        put("BIH",R.drawable.ba);
        put("BOT",R.drawable.bw);
        put("BRA",R.drawable.br);
        put("IVB",R.drawable.vg);
        put("BRU",R.drawable.bn);
        put("BUL",R.drawable.bg);
        put("BUR",R.drawable.bf);
        put("BDI",R.drawable.bi);
        put("CAM",R.drawable.kh);
        put("CMR",R.drawable.cm);
        put("CAN",R.drawable.ca);
        put("CPV",R.drawable.cv);
        put("CAY",R.drawable.ky);
        put("CAF",R.drawable.cf);
        put("CHA",R.drawable.td);
        put("CHI",R.drawable.cl);
        put("CHN",R.drawable.cn);
        put("COL",R.drawable.co);
        put("COM",R.drawable.km);
        put("COD",R.drawable.cd);
        put("CGO",R.drawable.cg);
        put("COK",R.drawable.ck);
        put("CRC",R.drawable.cr);
        put("CIV",R.drawable.ci);
        put("CRO",R.drawable.hr);
        put("CUB",R.drawable.cu);
        put("CYP",R.drawable.cy);
        put("CZE",R.drawable.cz);
        put("DEN",R.drawable.dk);
        put("DJI",R.drawable.dj);
        put("DMA",R.drawable.dm);
        put("ECU",R.drawable.ec);
        put("EGY",R.drawable.eg);
        put("ESA",R.drawable.sv);
        put("GEQ",R.drawable.gq);
        put("ERI",R.drawable.er);
        put("EST",R.drawable.ee);
        put("ETH",R.drawable.et);
        put("FIJ",R.drawable.fj);
        put("FIN",R.drawable.fi);
        put("FRA",R.drawable.fr);
        put("PYF",R.drawable.pf);
        put("GAB",R.drawable.ga);
        put("GAM",R.drawable.gm);
        put("GEO",R.drawable.ge);
        put("GER",R.drawable.de);
        put("GHA",R.drawable.gh);
        put("GRE",R.drawable.gr);
        put("GRN",R.drawable.gd);
        put("GUM",R.drawable.gu);
        put("GUA",R.drawable.gt);
        put("GUI",R.drawable.gn);
        put("GBS",R.drawable.gw);
        put("GUY",R.drawable.gy);
        put("HAI",R.drawable.ht);
        put("HON",R.drawable.hn);
        put("HUN",R.drawable.hu);
        put("ISL",R.drawable.is);
        put("IND",R.drawable.in);
        put("INA",R.drawable.id);
        put("IRI",R.drawable.ir);
        put("IRQ",R.drawable.iq);
        put("IRL",R.drawable.ie);
        put("ISR",R.drawable.il);
        put("ITA",R.drawable.it);
        put("JAM",R.drawable.jm);
        put("JPN",R.drawable.jp);
        put("JOR",R.drawable.jo);
        put("KAZ",R.drawable.kz);
        put("KEN",R.drawable.ke);
        put("KIR",R.drawable.ki);
        put("PRK",R.drawable.kp);
        put("KOR",R.drawable.kr);
        put("KUW",R.drawable.kw);
        put("KGZ",R.drawable.kg);
        put("LAO",R.drawable.la);
        put("LAT",R.drawable.lv);
        put("LBN",R.drawable.lb);
        put("LES",R.drawable.ls);
        put("LBR",R.drawable.lr);
        put("LBA",R.drawable.ly);
        put("LIE",R.drawable.li);
        put("LTU",R.drawable.lt);
        put("LUX",R.drawable.lu);
        put("MKD",R.drawable.mk);
        put("MAD",R.drawable.mg);
        put("MAW",R.drawable.mw);
        put("MAS",R.drawable.my);
        put("MDV",R.drawable.mv);
        put("MLI",R.drawable.ml);
        put("MLT",R.drawable.mt);
        put("MHL",R.drawable.mh);
        put("MTN",R.drawable.mr);
        put("MRI",R.drawable.mu);
        put("MEX",R.drawable.mx);
        put("FSM",R.drawable.fm);
        put("MDA",R.drawable.md);
        put("MON",R.drawable.mc);
        put("MGL",R.drawable.mn);
        put("MNE",R.drawable.me);
        put("MAR",R.drawable.ma);
        put("MOZ",R.drawable.mz);
        put("MYA",R.drawable.mm);
        put("NAM",R.drawable.na);
        put("NRU",R.drawable.nr);
        put("NEP",R.drawable.np);
        put("NED",R.drawable.nl);
        put("NZL",R.drawable.nz);
        put("NCA",R.drawable.ni);
        put("NIG",R.drawable.ne);
        put("NGR",R.drawable.ng);
        put("NOR",R.drawable.no);
        put("OMA",R.drawable.om);
        put("PAK",R.drawable.pk);
        put("PLW",R.drawable.pw);
        put("PLE",R.drawable.ps);
        put("PAN",R.drawable.pa);
        put("PNG",R.drawable.pg);
        put("PAR",R.drawable.py);
        put("PER",R.drawable.pe);
        put("PHI",R.drawable.ph);
        put("POL",R.drawable.pl);
        put("POR",R.drawable.pt);
        put("PUR",R.drawable.pr);
        put("QAT",R.drawable.qa);
        put("ROU",R.drawable.ro);
        put("RUS",R.drawable.ru);
        put("RWA",R.drawable.rw);
        put("SKN",R.drawable.kn);
        put("LCA",R.drawable.lc);
        put("VIN",R.drawable.vc);
        put("SAM",R.drawable.ws);
        put("SMR",R.drawable.sm);
        put("STP",R.drawable.st);
        put("KSA",R.drawable.sa);
        put("SEN",R.drawable.sn);
        put("SRB",R.drawable.rs);
        put("SEY",R.drawable.sc);
        put("SLE",R.drawable.sl);
        put("SGP",R.drawable.sg);
        put("SVK",R.drawable.sk);
        put("SLO",R.drawable.si);
        put("SOL",R.drawable.sb);
        put("SOM",R.drawable.so);
        put("RSA",R.drawable.za);
        put("SSD",R.drawable.ss);
        put("ESP",R.drawable.es);
        put("SRI",R.drawable.lk);
        put("SUD",R.drawable.sd);
        put("SUR",R.drawable.sr);
        put("SWZ",R.drawable.sz);
        put("SWE",R.drawable.se);
        put("SUI",R.drawable.ch);
        put("SYR",R.drawable.sy);
        put("TPE",R.drawable.tw);
        put("TJK",R.drawable.tj);
        put("TAN",R.drawable.tz);
        put("THA",R.drawable.th);
        put("TLS",R.drawable.tl);
        put("TOG",R.drawable.tg);
        put("TGA",R.drawable.to);
        put("TTO",R.drawable.tt);
        put("TUN",R.drawable.tn);
        put("TUR",R.drawable.tr);
        put("TKM",R.drawable.tm);
        put("TUV",R.drawable.tv);
        put("UGA",R.drawable.ug);
        put("UKR",R.drawable.ua);
        put("UAE",R.drawable.ae);
        put("GBR",R.drawable.gb);
        put("USA",R.drawable.us);
        put("URU",R.drawable.uy);
        put("UZB",R.drawable.uz);
        put("VAN",R.drawable.vu);
        put("VEN",R.drawable.ve);
        put("VIE",R.drawable.vn);
        put("YEM",R.drawable.ye);
        put("ZAM",R.drawable.zm);
        put("ZIM",R.drawable.zw);
    }};

    private UIUtilitis() {
        // To Hide public Constructor
    }

    public static int getCountryFlag(String countryCode) {
        try{
            return countryFlagMap.get(countryCode);
        } catch (Exception ex){
            return 0;
        }
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
