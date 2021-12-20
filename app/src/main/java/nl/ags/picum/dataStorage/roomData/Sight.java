package nl.ags.picum.dataStorage.roomData;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Sight {
    @PrimaryKey
    @NonNull
    private String sightName;

    @NonNull
    @ColumnInfo(name = "PhotoURL")
    private String photoURL;

    @NonNull
    @ColumnInfo(name = "SightDescription")
    private String sightDescription;

    @NonNull
    @ColumnInfo(name = "WebsiteURL")
    private String websiteURL;

    private int waypointID;

    public Sight() {

    }
    @Ignore
    public Sight(String sightName, String photoURL, String sightDescription, String websiteURL, int waypointID) {
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

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getSightDescription() {
        return sightDescription;
    }

    public void setSightDescription(String sightDescription) {
        this.sightDescription = sightDescription ;
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
