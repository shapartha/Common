package org.personal.partha.mylibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.io.File;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class SPDGoogleAPI {

    public static int REQUEST_CODE_SIGN_IN = 101;
    public static String APPLICATION_NAME;

    static {
        APPLICATION_NAME = "SPD Library";
    }

    public static void requestGoogleSignIn(Activity mContext) {
        SPDUtilities.writeLog(SPDUtilities.LOG_LEVEL.INFO, "Requesting sign-in");
        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                        .build();
        GoogleSignInClient client = GoogleSignIn.getClient(mContext, signInOptions);
        // The result of the sign-in Intent is handled in onActivityResult.
        mContext.startActivityForResult(client.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }

    public static void handleGoogleSignInResult(Intent result, Activity mContext, GoogleDrive.SignIn iGoogleDrive) {
        SharedPreferences prefs = mContext.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        GoogleSignIn.getSignedInAccountFromIntent(result)
                .addOnSuccessListener(googleAccount -> {
                    SPDUtilities.writeLog(SPDUtilities.LOG_LEVEL.INFO, "Signed in as " + googleAccount.getEmail());
                    GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(mContext, Collections.singleton(DriveScopes.DRIVE_FILE));
                    credential.setSelectedAccount(googleAccount.getAccount());
                    Drive googleDriveService = new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential)
                                    .setApplicationName(APPLICATION_NAME).build();
                    prefs.edit().putBoolean("googleSignedIn", true).commit();
                    // The SPDDriveServiceHelper encapsulates all REST API and SAF functionality.
                    // Its instantiation is required before handling any onClick actions.
                    SPDDriveServiceHelper mDriveServiceHelper = new SPDDriveServiceHelper(googleDriveService);
                    if (iGoogleDrive != null) {
                        iGoogleDrive.getDriveServiceHelperOnLogin(mDriveServiceHelper);
                    }
                })
                .addOnFailureListener(exception -> SPDUtilities.writeLog(SPDUtilities.LOG_LEVEL.ERROR, "Unable to sign in.. " + exception.getMessage()));
    }

    public static Drive getGoogleDriveService(Context mContext) {
        GoogleSignInAccount loggedInGoogleAccount = GoogleSignIn.getLastSignedInAccount(mContext);
        if (loggedInGoogleAccount != null) {
            GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(mContext, Collections.singleton(DriveScopes.DRIVE_FILE));
            credential.setSelectedAccount(loggedInGoogleAccount.getAccount());
            return new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential)
                    .setApplicationName(APPLICATION_NAME).build();
        } else {
            return null;
        }
    }

    public static void readGoogleDriveFile(String fileId, Context mContext, GoogleDrive.Read iGoogleDrive) {
        Drive googleDriveService = getGoogleDriveService(mContext);
        if (googleDriveService != null) {
            SPDDriveServiceHelper mDriveServiceHelper = new SPDDriveServiceHelper(googleDriveService);
            if (mDriveServiceHelper != null) {
                SPDUtilities.writeLog(SPDUtilities.LOG_LEVEL.INFO, "Reading file " + fileId);
                String filePath = mContext.getExternalFilesDir(null).getAbsolutePath();
                mDriveServiceHelper.readFile(fileId, filePath)
                        .addOnSuccessListener(nameAndContent -> {
                            SPDUtilities.writeLog(SPDUtilities.LOG_LEVEL.INFO, "File Downloaded... ");
                            iGoogleDrive.getDownloadedFilePath(filePath);
                        })
                        .addOnFailureListener(exception -> SPDUtilities.writeLog(SPDUtilities.LOG_LEVEL.ERROR, "Couldn't read file.. " + exception.getMessage()));
            }
        }
    }

    public static void createGoogleDriveFile(String mimeType, String fileName, String folderId, File fileToUpload, Context mContext, GoogleDrive.Upload iGoogleDrive) {
        Drive googleDriveService = getGoogleDriveService(mContext);
        if (googleDriveService != null) {
            SPDDriveServiceHelper mDriveServiceHelper = new SPDDriveServiceHelper(googleDriveService);
            if (mDriveServiceHelper != null) {
                SPDUtilities.writeLog(SPDUtilities.LOG_LEVEL.INFO, "Creating a file.");
                mDriveServiceHelper.createFile(mimeType, fileName, folderId)
                        .addOnSuccessListener(fileId -> {
                            if (iGoogleDrive != null) {
                                iGoogleDrive.onFileCreateSuccess();
                            }
                            saveGoogleDriveFile(fileName, mimeType, fileId, fileToUpload, mContext, iGoogleDrive);
                        })
                        .addOnFailureListener(exception -> {
                            SPDUtilities.writeLog(SPDUtilities.LOG_LEVEL.ERROR, "Couldn't create file.. " + exception.getMessage());
                            if (iGoogleDrive != null) {
                                iGoogleDrive.onFileCreateFailure();
                            }
                        });
            }
        }
    }

    public static void saveGoogleDriveFile(String fileName, String mimeType, String createdFileId, File fileToUpload, Context mContext, GoogleDrive.Upload iGoogleDrive) {
        Drive googleDriveService = getGoogleDriveService(mContext);
        if (googleDriveService != null) {
            SPDDriveServiceHelper mDriveServiceHelper = new SPDDriveServiceHelper(googleDriveService);
            if (mDriveServiceHelper != null && createdFileId != null) {
                SPDUtilities.writeLog(SPDUtilities.LOG_LEVEL.INFO, "Saving " + createdFileId);
                com.google.api.services.drive.model.File uploadedFile = new com.google.api.services.drive.model.File();
                uploadedFile.setName(fileName).setMimeType(mimeType);
                FileContent mediaContent = new FileContent(mimeType, fileToUpload);
                mDriveServiceHelper.saveFile(createdFileId, uploadedFile, mediaContent)
                        .addOnSuccessListener(fileId -> {
                            SPDUtilities.writeLog(SPDUtilities.LOG_LEVEL.INFO, "File uploaded successfully");
                            if (iGoogleDrive != null) {
                                iGoogleDrive.onFileUploadSuccess();
                            }
                        })
                        .addOnFailureListener(exception -> {
                            SPDUtilities.writeLog(SPDUtilities.LOG_LEVEL.ERROR, "Unable to save file via REST.. " + exception.getMessage());
                            if (iGoogleDrive != null) {
                                iGoogleDrive.onFileUploadFailure();
                            }
                        });
            }
        }
    }

    public static void deleteFileFromGDrive(String fileId, Context mContext, GoogleDrive.Delete iGoogleDrive) {
        Drive googleDriveService = getGoogleDriveService(mContext);
        if (googleDriveService != null) {
            SPDDriveServiceHelper mDriveServiceHelper = new SPDDriveServiceHelper(googleDriveService);
            if (mDriveServiceHelper != null) {
                SPDUtilities.writeLog(SPDUtilities.LOG_LEVEL.INFO, "Deleting file - " + fileId);
                mDriveServiceHelper.deleteFile(fileId)
                        .addOnSuccessListener(success -> {
                            SPDUtilities.writeLog(SPDUtilities.LOG_LEVEL.INFO, "Deleted the file successfully - " + fileId);
                            if (iGoogleDrive != null) {
                                iGoogleDrive.onFileDeleteSuccess();
                            }
                        })
                        .addOnFailureListener(exception -> {
                            SPDUtilities.writeLog(SPDUtilities.LOG_LEVEL.ERROR, "Couldn't create file.. " + exception.getMessage());
                            if (iGoogleDrive != null) {
                                iGoogleDrive.onFileDeleteFailure();
                            }
                        });
            }
        }
    }

    public static void getGoogleDriveFolderId(Context mContext, GoogleDrive.Fetch.Folder iGoogleDrive) {
        Drive googleDriveService = getGoogleDriveService(mContext);
        if (googleDriveService != null) {
            SPDDriveServiceHelper mDriveServiceHelper = new SPDDriveServiceHelper(googleDriveService);
            mDriveServiceHelper.queryFiles().addOnSuccessListener(fileList -> {
                boolean folderFound = false;
                for (com.google.api.services.drive.model.File file : fileList.getFiles()) {
                    String fileName = file.getName();
                    if (file.getMimeType().equalsIgnoreCase("application/vnd.google-apps.folder")) {
                        iGoogleDrive.setFolderId(file.getId());
                        folderFound = true;
                        break;
                    }
                }
                if (!folderFound) {
                    iGoogleDrive.onFolderNotFound();
                }
            }).addOnFailureListener(exception -> {
                SPDUtilities.writeLog(SPDUtilities.LOG_LEVEL.ERROR, "Unable to query files.. " + exception.getMessage());
                iGoogleDrive.onFolderNotFound();
            });
        }
    }

    public static void getGoogleDriveFiles(Context mContext, GoogleDrive.Fetch iGoogleDrive) {
        Drive googleDriveService = getGoogleDriveService(mContext);
        if (googleDriveService != null) {
            SPDDriveServiceHelper mDriveServiceHelper = new SPDDriveServiceHelper(googleDriveService);
            Map<String, String> files = new LinkedHashMap<>();
            mDriveServiceHelper.queryFiles().addOnSuccessListener(fileList -> {
                for (com.google.api.services.drive.model.File file : fileList.getFiles()) {
                    String fileName = file.getName();
                    if (!file.getMimeType().equalsIgnoreCase("application/vnd.google-apps.folder")) {
                        files.put(file.getId(), fileName);
                    }
                }
                iGoogleDrive.getGoogleDriveFiles(files);
            }).addOnFailureListener(exception -> SPDUtilities.writeLog(SPDUtilities.LOG_LEVEL.ERROR, "Unable to query files.. " + exception.getMessage()));
        }
    }

    public interface GoogleDrive {
        interface Fetch {
            void getGoogleDriveFiles(Map<String, String> files);
            interface Folder {
                void setFolderId(String folderId);
                void onFolderNotFound();
            }
        }
        interface Read {
            void getDownloadedFilePath(String filePath);
        }
        interface SignIn {
            void getDriveServiceHelperOnLogin(SPDDriveServiceHelper mDriveServiceHelper);
        }
        interface Upload {
            void onFileCreateSuccess();
            void onFileCreateFailure();
            void onFileUploadSuccess();
            void onFileUploadFailure();
        }
        interface Delete {
            void onFileDeleteSuccess();
            void onFileDeleteFailure();
        }
    }
}
