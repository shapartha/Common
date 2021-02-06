package org.personal.partha.mylibrary;

import android.content.Context;
import android.util.Pair;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import org.personal.partha.mylibrary.models.SPDGoogleDriveFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SPDDriveServiceHelper {
    private final Executor mExecutor = Executors.newSingleThreadExecutor();
    private final Drive mDriveService;
    public static String GOOGLE_DRIVE_FOLDER_NAME = "";
    private GoogleSignInClient mGoogleSignInClient;

    public SPDDriveServiceHelper(Drive driveService) {
        mDriveService = driveService;
        GOOGLE_DRIVE_FOLDER_NAME = "org.personal.partha.common";
    }

    public SPDDriveServiceHelper(Drive driveService, String folderName) {
        mDriveService = driveService;
        GOOGLE_DRIVE_FOLDER_NAME = folderName;
    }

    /**
     * Creates a text file in the user's My Drive folder and returns its file ID.
     */
    public Task<String> createFile(String mimeType, String fileName, String folderId) {
        return Tasks.call(mExecutor, () -> {
            String finalFolderId = folderId;
            if (finalFolderId.equalsIgnoreCase("")) {
                File metaDataFolder = new File()
                        .setParents(Collections.singletonList("root"))
                        .setMimeType("application/vnd.google-apps.folder")
                        .setName(GOOGLE_DRIVE_FOLDER_NAME);
                File googleFileFolder = mDriveService.files().create(metaDataFolder).setFields("id").execute();
                if (googleFileFolder == null) {
                    return null;
                }
                finalFolderId = googleFileFolder.getId();
            }
            File metaData = new File()
                    .setParents(Collections.singletonList(finalFolderId))
                    .setMimeType(mimeType)
                    .setName(fileName);

            File googleFile = mDriveService.files().create(metaData).execute();
            if (googleFile == null) {
                SPDUtilities.writeLog(SPDUtilities.LOG_LEVEL.ERROR, "Null result when requesting file creation.");
                throw new IOException("Null result when requesting file creation.");
            }
            return googleFile.getId();
        });
    }

    /**
     * Opens the file identified by {@code fileId} and returns a {@link Pair} of its name and
     * contents.
     */
    public Task<Void> readFile(String fileId, String filePath) {
        return Tasks.call(mExecutor, () -> {
            File metadata = mDriveService.files().get(fileId).execute();
            String fileName = metadata.getName();
            FileOutputStream fileStream = null;
            try {
                fileStream = new FileOutputStream(filePath + "/" + fileName);
                mDriveService.files().get(fileId).executeMediaAndDownloadTo(fileStream);
                return null;
            } catch (FileNotFoundException e) {
                SPDUtilities.writeLog(SPDUtilities.LOG_LEVEL.ERROR, ">>" + e.getMessage());
                return null;
            } finally {
                fileStream.flush();
                fileStream.close();
            }
        });
    }

    /**
     * Updates the file identified by {@code fileId} with the {@code uploadedFile} content.
     */
    public Task<Void> saveFile(String fileId, File uploadedFile, FileContent mediaContent) {
        return Tasks.call(mExecutor, () -> {
            mDriveService.files().update(fileId, uploadedFile, mediaContent).execute();
            return null;
        });
    }

    /**
     * Deletes the file identified by {@code fileId}.
     */
    public Task<Void> deleteFile(String fileId) {
        return Tasks.call(mExecutor, () -> {
            mDriveService.files().delete(fileId).execute();
            return null;
        });
    }

    /**
     * Returns a {@link FileList} containing all the visible files in the user's My Drive.
     *
     * <p>The returned list will only contain files visible to this app, i.e. those which were
     * created by this app. To perform operations on files not created by the app, the project must
     * request Drive Full Scope in the <a href="https://play.google.com/apps/publish">Google
     * Developer's Console</a> and be submitted to Google for verification.</p>
     */
    public Task<List<SPDGoogleDriveFile>> queryFiles() {
        return Tasks.call(mExecutor, new Callable<List<SPDGoogleDriveFile>>() {
            @Override
            public List<SPDGoogleDriveFile> call() throws Exception {
                FileList fileList = mDriveService.files().list().setSpaces("drive").execute();
                List<SPDGoogleDriveFile> returnFiles = new ArrayList<>();
                for (File driveFile : fileList.getFiles()) {
                    SPDGoogleDriveFile spdGoogleDriveFile = new SPDGoogleDriveFile(driveFile);
                    returnFiles.add(spdGoogleDriveFile);
                }
                return returnFiles;
            }
        });
    }
}
