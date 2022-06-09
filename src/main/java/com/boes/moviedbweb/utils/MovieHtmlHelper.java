package com.boes.moviedbweb.utils;

import com.boes.moviedbweb.entity.*;

import java.util.Set;
import java.util.stream.Collectors;

public final class MovieHtmlHelper {
    private static final String allMoviesUrl = "/movies";
    private static final String allDirectorsUrl = "/alldirectors";
    private static final String allActorsUrl = "/allactors";
    private static final String allCollectionsUrl = "/allcollections";
    private static final String allCountriesUrl = "/allcountries";
    private static final String viewedLast30DaysUrl = "/viewedinlast30days";

    public static String getYearLinkString(String year) {
        if (year.isEmpty()) return year;

        return " (<a href=\"/year?id=" + year + "\">" + year + "</a>)";
    }

    public static String getTitleBoxString(String title, String year, String id, Integer duration,
                                           Set<Collection> collections, Set<ViewDate> viewDates,
                                           Set<Country> countries)
    {
        long viewCount = viewDates.stream().count();
        String timeTimes = viewCount > 1 ? "times" : "time";

        return title + getYearLinkString(year)
                + "<div class=\"tiny\">" + MovieUtils.getDisplayDuration(duration)
                + "&nbsp&nbsp&nbsp&nbsp&nbsp[" + id
                + "]&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbspViewed " + viewCount + " "
                + timeTimes + ", last on " + MovieUtils.getLastViewedDate(viewDates) + "<br><br>"
                + getCountiesLinkString(countries)
                + "</div><br>"
                + collections.stream().map(Collection::getLinkString).collect(Collectors.joining("<br>"));
    }

    private static String getCountiesLinkString(Set<Country> countries) {
        if (countries!= null && !countries.isEmpty())
            return countries.stream().map(Country::getLinkString).collect(Collectors.joining(", "));
        return "";
    }

    public static String getDirectorsLinkString(Set<Director> directors) {
        if (directors!= null && !directors.isEmpty())
            return directors.stream().map(Director::getLinkString).collect(Collectors.joining(" <br>"));
        return "";
    }

    public static String getActorLinkString(Set<Actor> actors) {
        return actors.stream().map(Actor::getLinkString).collect(Collectors.joining(" <br>"));
    }

    public static String getActorLinkString(Actor actor) {
        return linkGen("/actor?id=" + actor.getActorId(), actor.getName());
    }

    public static String getCollectionLinkString(Collection collection) {
        return linkGen("/series?id=" + collection.getCollectionId(), collection.getName());
    }

    public static String getCountyLinkString(Country country) {
        return linkGen("/country?id=" + country.getCountryId(), country.getName());
    }

    public static String getDirectorLinkString(Director director) {
        return linkGen("/director?id=" + director.getDirectorId(), director.getName());
    }

    public static String getAllMoviesLink() {
        return linkGen(allMoviesUrl, " All Movies");
    }

    public static String getAllDirectorsLink() {
        return linkGen(allDirectorsUrl, " All Directors");
    }

    public static String getAllActorsLink() {
        return linkGen(allActorsUrl, " All Actors");
    }

    public static String getAllCollectionsLink() {
        return linkGen(allCollectionsUrl, " All Collections");
    }

    public static String getAllCountriesLink() {
        return linkGen(allCountriesUrl, " All Countries");
    }

    private static String getViewedLast30DaysUrlLink() {
        return linkGen(viewedLast30DaysUrl, " Viewed in last 30 days");
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
        sb.append("&nbsp&nbsp&nbsp&nbsp&nbsp");
        sb.append(getAllCountriesLink());
        sb.append("&nbsp&nbsp&nbsp&nbsp&nbsp");
        sb.append(getViewedLast30DaysUrlLink());
        sb.append("</div>");
        return sb.toString();
    }

    private static String linkGen(String url, String link_text) {
        StringBuilder sb = new StringBuilder("<a href=\"");
        sb.append(url);
        sb.append("\">");
        sb.append(link_text);
        sb.append("</a>");
        return sb.toString();

    }

}
