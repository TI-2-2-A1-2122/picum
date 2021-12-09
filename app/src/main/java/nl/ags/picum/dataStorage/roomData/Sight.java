package nl.ags.picum.dataStorage.roomData;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Sight {
    @PrimaryKey
    private String sightName;

    @ColumnInfo(name = "PhotoURL")
    private String photoURL;

    @ColumnInfo(name = "SightDescription")
    private String sightDescription;

    @ColumnInfo(name = "WebsiteURL")
    private String websiteURL;

    public Sight() {

    }

    public Sight(String sightName, String photoURL, String sightDescription, String websiteURL) {
        this.sightName = sightName;
        this.photoURL = photoURL;
        this.sightDescription = sightDescription;
        this.websiteURL = websiteURL;
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
        this.sightDescription = sightDescription;
    }

    public String getWebsiteURL() {
        return websiteURL;
    }

    public void setWebsiteURL(String websiteURL) {
        this.websiteURL = websiteURL;
    }
}
