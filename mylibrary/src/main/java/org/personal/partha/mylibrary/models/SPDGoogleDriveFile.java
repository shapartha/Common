package org.personal.partha.mylibrary.models;

import com.google.api.client.json.GenericJson;
import com.google.api.services.drive.model.File;

public class SPDGoogleDriveFile extends GenericJson {
    File myFile;

    public SPDGoogleDriveFile(File myFile) {
        this.myFile = myFile;
    }

    public File getMyFile() {
        return myFile;
    }

    public void setMyFile(File myFile) {
        this.myFile = myFile;
    }
}
