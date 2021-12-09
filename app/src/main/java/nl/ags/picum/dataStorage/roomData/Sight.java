package nl.ags.picum.dataStorage.roomData;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Sight {
    @PrimaryKey
    @NonNull
    private String sightName;

    @ColumnInfo(name = "PhotoURL")
    private int photoURL;

    @ColumnInfo(name = "SightDescription")
    private String sightDescription;

    @ColumnInfo(name = "WebsiteURL")
    private String websiteURL;

    private int waypointID;

    public Sight() {

    }

    public Sight(String sightName, int photoURL, String sightDescription, String websiteURL, int waypointID) {
        this.sightName = sightName;
        this.photoURL = photoURL;
        this.sightDescription = sightDescription;
        this.websiteURL = websiteURL;
        this.waypointID = waypointID;
    }

    public String getSightName() {
        return sightName;
    }

    public void setSightName(String sightName) {
        this.sightName = sightName;
    }

    public int getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(int photoURL) {
        this.photoURL = photoURL;
    }

    public String getSightDescription() {
        return sightDescription;
    }

    public void setSightDescription(String sightDescription) {
        this.sightDescription = sightDescription;
    }

    public String getWebsiteURL() {
        return websiteURL;
    }

    public void setWebsiteURL(String websiteURL) {
        this.websiteURL = websiteURL;
    }

    public int getWaypointID() {
        return waypointID;
    }

    public void setWaypointID(int waypointID) {
        this.waypointID = waypointID;
    }
}
