package nl.ags.picum.mapManagement.routeCalculation;

//Todo remove when implemented
interface Route {}

public class RouteCalculator {

    private RouteCalculatorListener listener;

    public RouteCalculator(RouteCalculatorListener listener) {
        this.listener = listener;
    }

    public void calculate(Route route) {
        
    }
}
