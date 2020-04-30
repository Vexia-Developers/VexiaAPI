package fr.vexia.proxy.utils;

import java.text.SimpleDateFormat;
import java.util.*;

public class TimeFormat {

    private static final Map<String, Long> TIME_FORMAT = new HashMap<>();

    static {
        TIME_FORMAT.put("y", 31_536_000_000L);
        TIME_FORMAT.put("M", 2_592_000_000L);
        TIME_FORMAT.put("d", 86_400_000L);
        TIME_FORMAT.put("h", 3_600_000L);
        TIME_FORMAT.put("m", 60_000L);
        TIME_FORMAT.put("s", 1_000L);
        TIME_FORMAT.put("ms", 1L);
    }

    public static long parsePeriod(String parser) {
        Objects.requireNonNull(parser, "Parser cannot be nul");

        char[] chars = parser.toCharArray();

        StringBuilder timeBuilder = new StringBuilder();
        StringBuilder keyBuilder = new StringBuilder();

        long time = 0;

        for (int i = 0; i < chars.length; i++) {
            if (Character.isDigit(chars[i]))
                timeBuilder.append(chars[i]);
            else {
                keyBuilder.append(chars[i]);
                if (i == chars.length - 1 || Character.isDigit(chars[i + 1])) {
                    Long mul = TIME_FORMAT.get(keyBuilder.toString());

                    if (mul == null)
                        return -1;

                    time += (Integer.parseInt(timeBuilder.toString()) * mul);

                    timeBuilder = new StringBuilder();
                    keyBuilder = new StringBuilder();
                }
            }
        }

        if (keyBuilder.length() > 0 || timeBuilder.length() > 0)
            return -1;

        return time;
    }

    public static String formatDate(long timestamp) {

        Date date = new Date(timestamp);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy 'à' HH:mm:ss");
        return format.format(date);
    }

    public static String formatDateOnly(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(date);
    }

    public static Integer[] formatTime(long millis) {
        int secondeBase = (int) millis / 1000;
        int heure = secondeBase / 3600;
        int restant = secondeBase - heure * 3600;
        int minute = restant / 60;
        restant = restant - minute * 60;
        int seconde = restant;

        return new Integer[] {heure,minute,seconde};
    }


    private EnumMap<TimeUnit, Integer> getTimeLeft(long temp) {
        return getTimeLeft(temp, TimeUnit.MOIS);
    }

    private EnumMap<TimeUnit, Integer> getTimeLeft(long temp, TimeUnit max) {
        temp /= 1000;
        EnumMap<TimeUnit, Integer> value = new EnumMap<>(TimeUnit.class);
        if (max.getToSecond() > TimeUnit.MOIS.getToSecond()) {
            while (temp >= TimeUnit.MOIS.getToSecond()) {
                value.put(TimeUnit.MOIS, value.get(TimeUnit.MOIS) + 1);
                temp -= TimeUnit.MOIS.getToSecond();
            }
        }

        if (max.getToSecond() > TimeUnit.JOUR.getToSecond()) {
            while (temp >= TimeUnit.JOUR.getToSecond()) {
                value.put(TimeUnit.JOUR, value.get(TimeUnit.JOUR) + 1);
                temp -= TimeUnit.JOUR.getToSecond();
            }
        }

        if (max.getToSecond() > TimeUnit.HEURE.getToSecond()) {
            while (temp >= TimeUnit.HEURE.getToSecond()) {
                value.put(TimeUnit.HEURE, value.get(TimeUnit.HEURE) + 1);
                temp -= TimeUnit.HEURE.getToSecond();
            }
        }

        if (max.getToSecond() > TimeUnit.MINUTE.getToSecond()) {
            while (temp >= TimeUnit.MINUTE.getToSecond()) {
                value.put(TimeUnit.MINUTE, value.get(TimeUnit.MINUTE) + 1);
                temp -= TimeUnit.MINUTE.getToSecond();
            }
        }

        while (temp >= TimeUnit.SECONDE.getToSecond()) {
            value.put(TimeUnit.SECONDE, value.get(TimeUnit.SECONDE) + 1);
            temp -= TimeUnit.SECONDE.getToSecond();
        }

        return value;
    }

    public String getTimeLeft(long time, TimeUnit max, TimeUnit min) {
        EnumMap<TimeUnit, Integer> value = getTimeLeft(time, max);
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<TimeUnit, Integer> values : value.entrySet()) {
            if (values.getKey() == min)
                break;
            if (builder.length() > 0) {
                builder.append(":").append(values.getValue()).append(values.getKey().getName());
            } else {
                builder.append(values.getValue()).append(values.getKey().getName());
            }
        }

        return builder.toString();
    }

    public String getTimeLeft(long time, int adapteur) {
        EnumMap<TimeUnit, Integer> value = getTimeLeft(time);
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<TimeUnit, Integer> values : value.entrySet()) {
            if (adapteur <= 0)
                break;
            if (builder.length() > 0) {
                builder.append(":").append(values.getValue()).append(values.getKey().getName());
            } else {
                builder.append(values.getValue()).append(values.getKey().getName());
            }
        }

        return builder.toString();
    }
}

