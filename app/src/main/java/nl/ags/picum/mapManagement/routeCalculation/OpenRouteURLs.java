package nl.ags.picum.mapManagement.routeCalculation;

import nl.ags.picum.dataStorage.dataUtil.Point;

public class OpenRouteURLs {

    // Key that is used to retrieve data from the api
    private static final String API_KEY = "5b3ce3597851110001cf624800ca244e157049a5aa73195ec969c0ae";

    /**
     * Given the action, this method wraps the API key and standard parts of the URL
     * to a correct url
     * @return  The base URL needed for ORS
     */
    private static String getWrapURL() {
        return "https://api.openrouteservice.org/v2/directions/foot-walking/geojson" + "?api_key=" + API_KEY;
    }

    /**
     * Returns the URL needed to request a route between two points.
     * Given the start and ending points
     * @param start  The point the route starts at
     * @param end  The end point of the route
     * @return  The url needed to request these route directions
     */
    public static String getURL2Points(Point start, Point end) {
        return getWrapURL() +
                "&start=" + start.getLongitude() + "," + start.getLatitude() +
                "&end=" + end.getLongitude() + "," + end.getLatitude();
    }

    public static String getURLMultiPoints() {
        return getWrapURL();
    }

}
