package nl.ags.picum.mapManagement.routeCalculation;


import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.*;
import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.ags.picum.dataStorage.dataUtil.Point;
import nl.ags.picum.dataStorage.roomData.Waypoint;
import okhttp3.*;

/**
 * Class handles the calculation of a route using the Open Route Service.
 * The calculated route is called back to the RouteCalculatorListener given in the constructor
 */
public class RouteCalculator {
    public static String LOG_TAG = RouteCalculator.class.getName();

    // The media type marked is JSON
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    // Non-static variables
    private final RouteCalculatorListener listener;

    /**
     * Constructor for RouteCalculator.
     * @param listener  The listener to call the calculated route back to
     */
    public RouteCalculator(RouteCalculatorListener listener) {
        this.listener = listener;
    }

    /**
     * Given a set of waypoints this method uses the Open Route Service to get a route between these
     * points. A list of Points is returned
     * @param waypointList  The list of waypoints to get a route in between
     */
    public void calculate(List<Waypoint> waypointList) {

        // Creating a new OkHTTP client
        OkHttpClient client = new OkHttpClient();

        // Creating the Request body
        RequestBody requestBody = buildMultiPointBody(waypointList);

        // Checking if the body is not null
        if(requestBody == null) {
            Log.e(LOG_TAG, "Returned request-body is null");
            return;
        }

        // Creating the request to the REST-API
        Request request = new Request.Builder()
                .url(OpenRouteURLs.getURLMultiPoints())
                .post(requestBody)
                .build();

        // Logging that a request will be send to the API
        Log.d(LOG_TAG, "Multi point request will be sent to ORS API: "+
                request.url().toString() +
                "\nwith body: " + requestBody.toString());

        // Adding the request to the queue, this call now is asynchronous
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                // Log the error and inform the listener
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                // Calling the handler method to convert the response
                handleMultiPointResponse(response);
            }
        });

    }

    /**
     * Given a list of waypoints this method build the body to sent in the POST request
     * @param waypointList  The list that needs to be in the post request
     * @return  The body for the POST request
     */
    @Nullable
    private RequestBody buildMultiPointBody(List<Waypoint> waypointList) {

        try {

            JSONObject object = new JSONObject();

            JSONArray waypointsArray = new JSONArray();

            // Going over the waypoints and adding them to the body
            for(Waypoint waypoint : waypointList) {
                JSONArray waypointArray = new JSONArray();

                waypointArray.put(waypoint.getLatitude());
                waypointArray.put(waypoint.getLongitude());

                waypointsArray.put(waypointArray);
            }

            // Put the JSONArray into the object
            object.put("coordinates", waypointsArray);

            // Returning the created Request body
            return RequestBody.create(object.toString(), JSON);

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error in creating the POST request: " + e.getMessage());
        }

        // Return nul on error
        return null;
    }

    /**
     * This method wil read the response from the Two point request and transform it to the correct
     * value's needed, either inform the listener of an error or return a list with all the route points
     * @param response  The response from the API
     * @throws IOException  Exception that can be thrown while reading the API response
     */
    private void handleMultiPointResponse(Response response) throws IOException {
        // Check if the response is successful, callback error if not
        if (!response.isSuccessful() || response.body() == null) {
            return;
        }

        // Try parsing the data to JSON
        try {
            // Ignore possible nullPointerException, this is not possible see above check
            String dataResponse = response.body().string();
            JSONObject jsonResponse = new JSONObject(dataResponse);

            // Getting the nested JSON array coordinates
            JSONArray coordinatesObject = jsonResponse
                    .getJSONArray("features")
                    .getJSONObject(0)
                    .getJSONObject("geometry")
                    .getJSONArray("coordinates");

            // Creating the list of GeoPoints from the JSON array given
            List<GeoPoint> routePoints = JSONtoGeoPointList(coordinatesObject);

            // Returning the value's to the listener
            listener.onRoutePointsCalculated(routePoints);

        } catch (JSONException e) {
            // Informing the listener of the error
        }

    }

    /**
     * Given a JSONArray of coordinates this method will read that list and
     * return a list of GeoPoints.
     * @param coordinates  The JSONArray that contains the coordinates
     * @return  A list of GeoPoints holding the given coordinates from the JSONArray
     */
    private List<GeoPoint> JSONtoGeoPointList(JSONArray coordinates) {
        List<GeoPoint> points = new ArrayList<>();

        for (int i = 0; i < coordinates.length(); i++) {
            JSONArray coordinateArray = coordinates.optJSONArray(i);

            // Checking if coordinateArray in not null
            if (coordinateArray == null) continue;

            // Getting the longitude and latitude
            float longitude = (float) coordinateArray.optDouble(0, 91);
            float latitude = (float) coordinateArray.optDouble(1, 91);

            // Checking if the value's are valid (they can never go over 90)
            if (longitude == 91 || latitude == 91) continue;

            // Creating a GeoPoint based on the latitude and longitude and adding it to the list
            points.add(new GeoPoint(latitude, longitude));
        }

        // Returning the new list
        return points;
    }

}
