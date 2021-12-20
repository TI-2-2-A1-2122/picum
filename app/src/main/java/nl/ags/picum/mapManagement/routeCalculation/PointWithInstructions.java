package nl.ags.picum.mapManagement.routeCalculation;

import nl.ags.picum.dataStorage.dataUtil.Point;

public class PointWithInstructions extends Point {

    private String instructions ;
    private int maneuverType;
    private String streetName;

    public PointWithInstructions(float longitude, float latitude, String id, String instructions, int maneuverType) {
        super(longitude, latitude, id);
        this.instructions = instructions;
        this.maneuverType = maneuverType;
    }

    public PointWithInstructions(float longitude, float latitude) {
        super(longitude, latitude);
    }

    public String getInstructions() {
        return instructions;
    }

    public int getManeuverType() {
        return maneuverType;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setManeuverType(int maneuverType) {
        this.maneuverType = maneuverType;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }
}
