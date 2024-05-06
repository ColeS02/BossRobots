// VAULT API CODE ALTERED

package com.unclecole.bossrobots.utils;

import com.google.common.primitives.Ints;
import com.unclecole.bossrobots.BossRobots;
import me.taleeko.bossenchants.BossEnchants;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class NumberFormat {

    private BossRobots plugin;

    public NumberFormat(BossRobots plugin) {
        this.plugin = plugin;
    }

    public static String k = "k";
    public static String m = "M";
    public static String b = "B"; //BILLION
    public static String t = "T"; //TRILLION
    public static String q = "Q"; //Quadrillion
    public static String qt = "QT"; //Quintillion
    public static String s = "S"; //Sextillion
    public static String st = "ST"; //Septillion
    public static String o = "O"; //Octallion
    public static String n = "N"; //Nonillion
    public static String de = "D"; //Decillion

    public static final Pattern BALANCE_DECIMAL_POINTS_PATTERN = Pattern.compile("tokens_(?<points>\\d+)dp");
    public final Map<Integer, DecimalFormat> decimalFormats = new HashMap<>();
    public final DecimalFormat format = new DecimalFormat("#,###");

    public String toLong(double amt) {
        return String.valueOf((long) amt);
    }


    public String fixMoney(BigInteger d) {

        if (d.compareTo(BigInteger.valueOf(1000L)) <= -1) {
            return String.valueOf(d);
        }
        if (d.compareTo(BigInteger.valueOf(1000000L)) <= -1) {
            return (d.divide(BigInteger.valueOf(1000L)) + k);
        }
        if (d.compareTo(BigInteger.valueOf(1000000000L)) <= -1) {
            return (d.divide(BigInteger.valueOf(1000000L)) + m);
        }
        if (d.compareTo(BigInteger.valueOf(1000000000000L)) <= -1) {
            return (d.divide(BigInteger.valueOf(1000000000L)) + b);
        }
        if (d.compareTo(BigInteger.valueOf(1000000000000000L)) <= -1) {
            return (d.divide(BigInteger.valueOf(1000000000000L)) + t);
        }
        if (d.compareTo(BigInteger.valueOf(1000000000000000000L)) <= -1) {
            return (d.divide(BigInteger.valueOf(1000000000000000L)) + q);
        }
        BigInteger num = new BigInteger("1000000000000000000000");
        if (d.compareTo(num) <= -1) {
            return (d.divide(BigInteger.valueOf(1000000000000000000L)) + qt);
        }
        if (d.compareTo(num.multiply(BigInteger.valueOf(1000L))) <= -1) {
            return (d.divide(num) + s);
        }
        if (d.compareTo(num.multiply(BigInteger.valueOf(1000000L))) <= -1) {
            return (d.divide(num.multiply(BigInteger.valueOf(1000L))) + st);
        }
        if (d.compareTo(num.multiply(BigInteger.valueOf(1000000000L))) <= -1) {
            return (d.divide(num.multiply(BigInteger.valueOf(1000000L))) + o);
        }
        if (d.compareTo(num.multiply(BigInteger.valueOf(1000000000000L))) <= -1) {
            return (d.divide(num.multiply(BigInteger.valueOf(1000000000L))) + n);
        }
        if (d.compareTo(num.multiply(BigInteger.valueOf(1000000000000000L))) <= -1) {
            return (d.divide(num.multiply(BigInteger.valueOf(1000000000000L))) + de);
        }

        return String.valueOf(d);
    }

    public String setDecimalPoints(double d, int points) {
        DecimalFormat decimalFormat = this.decimalFormats.get(points);

        if (decimalFormat != null) {
            return decimalFormat.format(d);
        }

        decimalFormat = (DecimalFormat) DecimalFormat.getIntegerInstance();
        decimalFormat.setMaximumFractionDigits(points);
        decimalFormat.setGroupingUsed(false);

        this.decimalFormats.put(points, decimalFormat);
        return decimalFormat.format(d);
    }

    public int getInt(String string) {
        final Integer integer = Ints.tryParse(string);
        return integer == null ? 0 : integer;
    }

}
