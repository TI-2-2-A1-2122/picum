package nl.ags.picum.dataStorage.roomData;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Route {
    @PrimaryKey
    private String routeName;

    @ColumnInfo(name = "Description")
    private String description;

    @ColumnInfo(name = "PhotoURL")
    private String photoURL;

    @ColumnInfo(name = "InProgress")
    private boolean inProgress;

    public Route() {

    }

    public Route(String routeName, String description, String photoURL, boolean inProgress) {
        this.routeName = routeName;
        this.description = description;
        this.photoURL = photoURL;
        this.inProgress = inProgress;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }
}