package nl.ags.picum.dataStorage.roomData;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Route implements Serializable {
    @PrimaryKey
    @NonNull
    private String routeName;

    @NonNull
    @ColumnInfo(name = "Description")
    private String description;

    @NonNull
    @ColumnInfo(name = "PhotoURL")
    private int photoURL;

    @NonNull
    @ColumnInfo(name = "InProgress")
    private boolean inProgress;

    public Route() {

    }
    @Ignore
    public Route(String routeName, String description, int photoURL, boolean inProgress) {
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

    public int getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(int photoURL) {
        this.photoURL = photoURL;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    @NonNull
    @Override
    public String toString() {
        return "Route{" +
                "routeName='" + routeName + '\'' +
                ", description='" + description + '\'' +
                ", photoURL=" + photoURL +
                ", inProgress=" + inProgress +
                '}';
    }
}
