package nl.ags.picum.mapManagement.routeCalculation;

import java.util.List;

/**
 * Interface used to callback the response of calculating a route by the
 * RouteCalculator
 */
public interface RouteCalculatorListener {

    /**
     * Method is called when there is a response from calculating a route using
     * open route service.
     * @param points  The points calculated in the route
     */
    void onRoutePointsCalculated(List<PointWithInstructions> points);

    void onRouteCalculationError();

}
