package nl.ags.picum.mapManagement.routeCalculation;

import android.util.Log;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import nl.ags.picum.dataStorage.roomData.Waypoint;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RouteCalculator {
    public static String LOG_TAG = RouteCalculator.class.getName();

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    private RouteCalculatorListener listener;

    public RouteCalculator(RouteCalculatorListener listener) {
        this.listener = listener;
    }

    public void calculate(List<Waypoint> waypointList) {

        // Creating a new OkHTTP client
        OkHttpClient client = new OkHttpClient();

        // Creating the Request body
        RequestBody requestBody = buildMultiPointBody(waypointList);

        // Checking if the body is not null
        if(requestBody == null) {
            return;
        }

        // Creating the request to the REST-API
        Request request = new Request.Builder()
                .url(OpenRouteURLs.getURLMultiPoints())
                .post(requestBody)
                .build();

        // Logging that a request will be send to the API
        Log.d(LOG_TAG, "Multi point request will be sent to ORS API: " + request.url().toString());

        // Adding the request to the queue, this call now is asynchronous
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                // Log the error and inform the listener
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                // Calling the handler method to convert the response
                Log.d(LOG_TAG, response.toString());
            }
        });

    }

    @Nullable
    private RequestBody buildMultiPointBody(List<Waypoint> waypointList) {

        try {

            JSONObject object = new JSONObject();

            JSONArray waypointsArray = new JSONArray();

            for(Waypoint waypoint : waypointList) {
                JSONArray waypointArray = new JSONArray();

                waypointArray.put(waypoint.getLongitude());
                waypointArray.put(waypoint.getLatitude());

                waypointsArray.put(waypointArray);
            }

            object.put("coordinates", waypointsArray);

            return RequestBody.create(object.toString(), JSON);
        } catch (JSONException e) {

            e.printStackTrace();
        }

        return null;
    }

}
