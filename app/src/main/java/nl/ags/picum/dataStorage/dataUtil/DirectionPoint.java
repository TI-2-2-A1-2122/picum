package nl.ags.picum.dataStorage.dataUtil;

public class DirectionPoint extends Point{

    public final Direction direction;
    public DirectionPoint(float longitude, float latitude, Direction direction) {
        super(longitude, latitude);
        this.direction = direction;
    }
}
