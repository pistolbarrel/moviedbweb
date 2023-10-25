package com.boes.moviedbweb.utils;

import com.boes.moviedbweb.entity.ViewDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class MovieUtils {

    public static Integer convertHHMMSSToInteger(String colonSeparatedHHMMSS) {
        if (colonSeparatedHHMMSS != null) {
            String[] parts = colonSeparatedHHMMSS.split(":");
            if (parts.length == 3) {
                return 3600 * Integer.parseInt(parts[0]) + 60 * Integer.parseInt(parts[1]) + Integer.parseInt(parts[2]);
            } else if (parts.length == 2) {
                return 60 * Integer.parseInt(parts[0]) + Integer.parseInt(parts[1]);
            } else if (parts.length == 1) {
                return Integer.parseInt(parts[0]);
            }
        }
        return 0;
    }

    public static String getDisplayDuration(Integer duration) {
        if (duration == null || duration == 0) return "";
        Integer hours = duration / 3600;
        Integer leftOver = duration % 3600;
        Integer minutes = leftOver / 60;
        Integer secs = leftOver % 60;
        StringBuilder sb = new StringBuilder();
        if (hours > 0) {
            sb.append(hours.toString() + ":");
        }
        if (minutes > 0 || hours > 0) {
            sb.append(String.format("%02d", minutes.intValue()));
        }
        sb.append(":" + String.format("%02d", secs.intValue()));
        return sb.toString();
    }

    public String getDisplayTitle(String title, String year) {
        return title + " (" + year + ")";
    }

    public static LocalDate convertStringToDate(String dateString) {
        return LocalDate.parse(dateString);
    }

    public static String getLastViewedDate(Set<ViewDate> viewDates) {
        if (viewDates != null && !viewDates.isEmpty())
            return viewDates.stream().map(ViewDate::getLocalDate).max(LocalDate::compareTo).get().toString();
        return "never";
    }

    public static String replaceNoneWithEmpty(String input) {
        return input.equals("NONE") ? "" : input;
    }

    public static String[] splitAndGroomNames(String delimitedNames) {
        String[] splitNames = delimitedNames.split(";");
        Set<String> newSet = new HashSet<>();
        for (String split : splitNames) {
            if (!split.isBlank()) {
                newSet.add(split.trim());
            }
        }
        return newSet.stream().toArray(String[]::new);
    }

}
