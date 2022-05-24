package com.boes.moviedbweb.utils;

import com.boes.moviedbweb.entity.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public final class MovieHtmlHelper {
    private static final String allMoviesUrl = "http://localhost:8080/movies";
    private static final String allDirectorsUrl = "http://localhost:8080/alldirectors";
    private static final String allActorsUrl = "http://localhost:8080/allactors";
    private static final String allCollectionsUrl = "http://localhost:8080/allcollections";

    public static String getYearLinkString(String year) {
        if (year.isEmpty()) return year;

        return " (<a href=\"/year?id=" + year + "\">" + year + "</a>)";
    }

    public static String getTitleBoxString(String title, String year, String id, Integer duration,
                                           Set<Collection> collections, Set<ViewDate> viewDates)
    {
        long viewCount = viewDates.stream().count();
        String timeTimes = viewCount > 1 ? "times" : "time";

        return title + getYearLinkString(year)
                + "<div class=\"tiny\">" + MovieUtils.getDisplayDuration(duration)
                + "&nbsp&nbsp&nbsp&nbsp&nbsp[" + id
                + "]&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbspViewed " + viewCount + " "
                + timeTimes + ", last on " + getLastViewedDate(viewDates)
                + "</div><br><br>"
                + collections.stream().map(Collection::getLinkString).collect(Collectors.joining("<br>"));
    }

    public static String getLastViewedDate(Set<ViewDate> viewDates) {
        return viewDates.stream().map(u-> u.getLocalDate()).max(LocalDate::compareTo).get().toString();
    }

    public static String getDirectorLinkString(Set<Director> directors) {
        return directors.stream().map(Director::getLinkString).collect(Collectors.joining(" <br>"));
    }

    public static String getActorLinkString(Set<Actor> actors) {
        return actors.stream().map(Actor::getLinkString).collect(Collectors.joining(" <br>"));
    }

    public static String getActorLinkString(Actor actor) {
        StringBuilder sb = new StringBuilder("<a href=\"/actor?id=");
        sb.append(actor.getActorId());
        sb.append("\">");
        sb.append(actor.getName());
        sb.append("</a>");
        return sb.toString();
    }

    public static String getCollectionLinkString(Collection collection) {
        StringBuilder sb = new StringBuilder("<a href=\"/series?id=");
        sb.append(collection.getCollectionId());
        sb.append("\">");
        sb.append(collection.getName());
        sb.append("</a>");
        return sb.toString();
    }

    public static String getCountyLinkString(Country country) {
        StringBuilder sb = new StringBuilder("<a href=\"/country?id=");
        sb.append(country.getCountryId());
        sb.append("\">");
        sb.append(country.getName());
        sb.append("</a>");
        return sb.toString();
    }

    public static String getDirectorLinkString(Director director) {
        StringBuilder sb = new StringBuilder("<a href=\"/director?id=");
        sb.append(director.getDirectorId());
        sb.append("\">");
        sb.append(director.getName());
        sb.append("</a>");
        return sb.toString();
    }

    public static String getAllMoviesLink() {
        StringBuilder sb = new StringBuilder("<a href=\"");
        sb.append(allMoviesUrl);
        sb.append("\">");
        sb.append(" All Movies");
        sb.append("</a>");
        return sb.toString();
    }

    public static String getAllDirectorsLink() {
        StringBuilder sb = new StringBuilder("<a href=\"");
        sb.append(allDirectorsUrl);
        sb.append("\">");
        sb.append(" All Directors");
        sb.append("</a>");
        return sb.toString();
    }

    public static String getAllActorsLink() {
        StringBuilder sb = new StringBuilder("<a href=\"");
        sb.append(allActorsUrl);
        sb.append("\">");
        sb.append(" All Actors");
        sb.append("</a>");
        return sb.toString();
    }

    public static String getAllCollectionsLink() {
        StringBuilder sb = new StringBuilder("<a href=\"");
        sb.append(allCollectionsUrl);
        sb.append("\">");
        sb.append(" All Collections");
        sb.append("</a>");
        return sb.toString();
    }

    public static String getSearchedValue(String searchTerm, String numberOfResults) {
        StringBuilder sb = new StringBuilder("Current search:" + searchTerm + " (");
        sb.append(numberOfResults);
        sb.append(")<br><br><div class=\"tiny\">");
        sb.append(getAllMoviesLink());
        sb.append("&nbsp&nbsp&nbsp&nbsp&nbsp");
        sb.append(getAllDirectorsLink());
        sb.append("&nbsp&nbsp&nbsp&nbsp&nbsp");
        sb.append(getAllActorsLink());
        sb.append("&nbsp&nbsp&nbsp&nbsp&nbsp");
        sb.append(getAllCollectionsLink());
        sb.append("</div>");
        return sb.toString();
    }

}
