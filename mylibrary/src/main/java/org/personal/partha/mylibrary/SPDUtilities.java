package org.personal.partha.mylibrary;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SPDUtilities {
    public static final int OPEN_APK_UPDATE_PACKAGE_CODE = 125;
    public static boolean JAVA_LOG_ENABLED;
    public static String LOGGER_NAME;
    private static Handler appHandler;
    public static ProgressDialog PROGRESS_DIALOG;
    public enum LOG_LEVEL {
        WARN, DEBUG, INFO, ERROR
    }
    public enum DATE_FORMAT {
        DEFAULT, MYSQL_DATETIME, ONLY_DATE, MYSQL_DATE_ONLY, ONLY_TIME
    }
    public enum REST_STATUS {
        IDLE, IN_PROGRESS, COMPLETED, FAILED
    }
    public static final Map<DATE_FORMAT, String> DATE_FORMATS;

    static {
        Map<DATE_FORMAT, String> map = new HashMap<>();
        map.put(DATE_FORMAT.DEFAULT, "dd-MM-yyyy HH:mm:ss");
        map.put(DATE_FORMAT.MYSQL_DATETIME, "yyyy-MM-dd HH:mm:ss");
        map.put(DATE_FORMAT.ONLY_DATE, "dd-MM-yyyy");
        map.put(DATE_FORMAT.MYSQL_DATE_ONLY, "yyyy-MM-dd");
        map.put(DATE_FORMAT.ONLY_TIME, "HH:mm:ss");
        DATE_FORMATS = Collections.unmodifiableMap(map);
        appHandler = new Handler();
    }

    /**
     * Writes Logging Message to the Logcat with supplied TAG information in the variable
     * <b>LOGGER_NAME</b>. If not initialized, system will use <u>SHAPARTHA_UTILITIES</u> as default TAG.
     *
     * @param logLevel Logging Level to be used (WARN, DEBUG, INFO, ERROR)
     * @param logMsg Logging Message to be printed to the system Logcat
     */
    public static void writeLog(LOG_LEVEL logLevel, String logMsg) {
        if (!JAVA_LOG_ENABLED) {
            JAVA_LOG_ENABLED = true;
        }
        writeLog(logLevel, logMsg, JAVA_LOG_ENABLED);
    }

    /**
     * Writes Logging Message to the Logcat with supplied TAG information in the variable
     * <b>LOGGER_NAME</b>. If not initialized, system will use <u>SHAPARTHA_UTILITIES</u> as default TAG.
     * This method will not work if {@code logEnabled} value is <b>false</b>
     *
     * @param logLevel Logging Level to be used (WARN, DEBUG, INFO, ERROR)
     * @param logMsg Logging Message to be printed to the system Logcat
     * @param logEnabled Whether the logging should be done or not
     */
    public static void writeLog(LOG_LEVEL logLevel, String logMsg, boolean logEnabled) {
        if (LOGGER_NAME == null || LOGGER_NAME.trim().equalsIgnoreCase("")) {
            LOGGER_NAME = "SHAPARTHA_UTILITIES";
        }
        if (logEnabled) {
            if (logLevel.equals(LOG_LEVEL.WARN)) {
                Log.w(LOGGER_NAME, logMsg);
            } else if (logLevel.equals(LOG_LEVEL.DEBUG)) {
                Log.d(LOGGER_NAME, logMsg);
            } else if (logLevel.equals(LOG_LEVEL.INFO)) {
                Log.i(LOGGER_NAME, logMsg);
            } else if (logLevel.equals(LOG_LEVEL.ERROR)) {
                Log.e(LOGGER_NAME, logMsg);
            }
        }
    }

    /**
     * Navigates to the Intent/Activity supplied in the <b>clazz</b> argument from the
     * current activity's context object - <b>ctx</b>
     *
     * ( Works on Android versions Jelly Bean & above )
     *
     * @param ctx Current Activity Context object
     * @param clazz Next Activity/Intent Class object
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void goToIntent(Context ctx, Class<?> clazz) {
        Intent intent = new Intent(ctx, clazz);
        ctx.startActivity(intent);
        ((Activity) ctx).setResult(Activity.RESULT_CANCELED);
        ((Activity) ctx).finishAffinity();
    }

    public static AlertDialog getAlertDialogBuilder(Context mContext, String title, String message) {
        return getAlertDialogBuilder(mContext, title, message, null, (Integer) null, null, null, null, null, null, null, null);
    }

    public static AlertDialog getAlertDialogBuilder(Context mContext, String title, String message, Boolean cancelable) {
        return getAlertDialogBuilder(mContext, title, message, cancelable, (Integer) null, null, null, null, null, null, null, null);
    }

    public static AlertDialog getAlertDialogBuilder(Context mContext, String title, String message, View viewObj) {
        return getAlertDialogBuilder(mContext, title, message, null, viewObj, null, null, null, null, null, null, null);
    }

    public static AlertDialog getAlertDialogBuilder(Context mContext, String title, String message, Boolean cancelable, View viewObj) {
        return getAlertDialogBuilder(mContext, title, message, cancelable, viewObj, null, null, null, null, null, null, null);
    }

    public static AlertDialog getAlertDialogBuilder(Context mContext, String title, String message,
                                                    String posBtnText, DialogInterface.OnClickListener posBtnOnClickListener) {
        return getAlertDialogBuilder(mContext, title, message, null, (Integer) null, null, posBtnText, posBtnOnClickListener, null, null, null, null);
    }

    public static AlertDialog getAlertDialogBuilder(Context mContext, String title, String message,
                                                    String posBtnText, DialogInterface.OnClickListener posBtnOnClickListener,
                                                    String negBtnText, DialogInterface.OnClickListener negBtnOnClickListener) {
        return getAlertDialogBuilder(mContext, title, message, null, (Integer) null, null, posBtnText, posBtnOnClickListener, negBtnText, negBtnOnClickListener, null, null);
    }

    public static AlertDialog getAlertDialogBuilder(Context mContext, String title, String message, Boolean cancelable, View viewObj,
                                                    Integer iconId, String posBtnText, DialogInterface.OnClickListener posBtnOnClickListener,
                                                    String negBtnText, DialogInterface.OnClickListener negBtnOnClickListener,
                                                    String neuBtnText, DialogInterface.OnClickListener neuBtnOnClickListener) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
        title = (title == null) ? "Title" : title;
        if (viewObj != null) {
            alertBuilder.setTitle(title).setView(viewObj);
        } else {
            message = (message == null) ? "This is Dummy Message" : message;
            alertBuilder.setTitle(title).setMessage(message);
        }
        if (cancelable != null) {
            alertBuilder.setCancelable(cancelable);
        } else {
            alertBuilder.setCancelable(true);
        }
        if (iconId != null) {
            alertBuilder.setIcon(iconId);
        }
        if (posBtnText != null && posBtnOnClickListener != null) {
            alertBuilder.setPositiveButton(posBtnText, posBtnOnClickListener);
        }
        if (negBtnText != null && negBtnOnClickListener != null) {
            alertBuilder.setNegativeButton(negBtnText, negBtnOnClickListener);
        }
        if (neuBtnText != null && neuBtnOnClickListener != null) {
            alertBuilder.setNeutralButton(neuBtnText, neuBtnOnClickListener);
        }
        return alertBuilder.create();
    }

    public static AlertDialog getAlertDialogBuilder(Context mContext, String title, String message, Boolean cancelable, Integer viewResId,
                                                    Integer iconId, String posBtnText, DialogInterface.OnClickListener posBtnOnClickListener,
                                                    String negBtnText, DialogInterface.OnClickListener negBtnOnClickListener,
                                                    String neuBtnText, DialogInterface.OnClickListener neuBtnOnClickListener) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
        title = (title == null) ? "Title" : title;
        if (viewResId != null && viewResId != 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                alertBuilder.setTitle(title).setView(viewResId);
            } else {
                alertBuilder.setTitle(title).setMessage("View ID not supported on Android Versions below " + Build.VERSION_CODES.LOLLIPOP);
            }
        } else {
            message = (message == null) ? "This is Dummy Message" : message;
            alertBuilder.setTitle(title).setMessage(message);
        }
        if (cancelable != null) {
            alertBuilder.setCancelable(cancelable);
        } else {
            alertBuilder.setCancelable(true);
        }
        if (iconId != null) {
            alertBuilder.setIcon(iconId);
        }
        if (posBtnText != null && posBtnOnClickListener != null) {
            alertBuilder.setPositiveButton(posBtnText, posBtnOnClickListener);
        }
        if (negBtnText != null && negBtnOnClickListener != null) {
            alertBuilder.setNegativeButton(negBtnText, negBtnOnClickListener);
        }
        if (neuBtnText != null && neuBtnOnClickListener != null) {
            alertBuilder.setNeutralButton(neuBtnText, neuBtnOnClickListener);
        }
        return alertBuilder.create();
    }

    /**
     * Common <code>PROGRESS_DIALOG</code> to display wherever required to show transaction in progress dialog
     *
     * @param ctx Application context object to access the application
     * @return <code><b>ProgressDialog</b></code> object
     */
    public static ProgressDialog getProgressDialog(Context ctx) {
        return getProgressDialog(ctx, "Processing", "Please wait while transaction is processed...", false);
    }

    /**
     * Common <code>PROGRESS_DIALOG</code> to display wherever required to show transaction in progress dialog
     *
     * @param ctx Application context object to access the application
     * @param message Message string to be displayed on progress dialog message box
     * @return <code><b>ProgressDialog</b></code> object with the customized properties set as per the supplied arguments
     */
    public static ProgressDialog getProgressDialog(Context ctx, String message) {
        return getProgressDialog(ctx, "Processing", message, false);
    }

    /**
     * Common <code>PROGRESS_DIALOG</code> to display wherever required to show transaction in progress dialog
     *
     * @param mContext Application context object to access the application
     * @param title Title string to be displayed on the progress dialog title
     * @param message Message string to be displayed on progress dialog message box
     * @param cancelable <code>boolean</code> value to determine whether progress dialog can be cancelled or not
     * @return <code><b>ProgressDialog</b></code> object with the customized properties set as per the supplied arguments
     */
    public static ProgressDialog getProgressDialog(Context mContext, String title, String message, boolean cancelable) {
        if (PROGRESS_DIALOG != null && PROGRESS_DIALOG.isShowing()) {
            PROGRESS_DIALOG.setMessage(message);
            return PROGRESS_DIALOG;
        }
        PROGRESS_DIALOG = new ProgressDialog(mContext);
        PROGRESS_DIALOG.setMessage(message);
        PROGRESS_DIALOG.setTitle(title);
        PROGRESS_DIALOG.setCancelable(cancelable);
        PROGRESS_DIALOG.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        PROGRESS_DIALOG.setIndeterminate(true);
        PROGRESS_DIALOG.setProgress(0);
        return PROGRESS_DIALOG;
    }

    /**
     * Hides the common <code>PROGRESS_DIALOG</code> object if it's currently displaying else nothing happens
     */
    public static void hideProgressDialog() {
        if (PROGRESS_DIALOG != null) {
            PROGRESS_DIALOG.dismiss();
        }
    }

    /**
     * Generates a random string of the given <b>length</b> argument. This can be used to generate IDs or tokens
     * in the application. The random string will comprise of only characters from all alphabets and digits.
     *
     * For specifying characters from which the random string will be generated, see <code>generateRandomID(int length, String characters)</code>
     *
     * @param length (int) Length of the generated string
     * @return A random string of the given length
     */
    public static String generateRandomID(int length) {
        return generateRandomID(length, null);
    }

    /**
     * Generates a random string of the given <b>length</b> argument. This can be used to generate IDs or tokens
     * in the application. The random string will comprise of only characters passed as an argument in <b>characters</b>
     *
     * @param length (int) Length of the generated string
     * @param characters String of characters from which the random string will be generated
     * @return A random string of the given <b>length</b> from the given set of <b>characters</b>
     */
    public static String generateRandomID(int length, String characters) {
        String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
        if (characters != null) {
            CHAR_LOWER = characters;
        }
        final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
        final String NUMBER = "0123456789";
        final String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);
            sb.append(rndChar);
        }
        return sb.toString();
    }

    /**
     * Formats a Date object in the <code>dateObj</code> argument in the given <code>format</code> argument
     * and returns a string representation of the formatted date
     *
     * @param dateObj Date object to be formatted
     * @param format {@link DATE_FORMAT} format in which <code>dateObj</code> will be formatted to
     * @return A string representation of the formatted date
     */
    public static String formatDateToString(Date dateObj, DATE_FORMAT format) {
        return formatDateToString(dateObj, DATE_FORMATS.get(format));
    }

    /**
     * Formats a Date object in the <code>dateObj</code> argument in the given <code>format</code> argument
     * and returns a string representation of the formatted date
     *
     * @param dateObj Date object to be formatted
     * @param format String format in which <code>dateObj</code> will be formatted to
     * @return A string representation of the formatted date
     */
    public static String formatDateToString(Date dateObj, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(dateObj);
    }

    /**
     * Formats a Date object in the <code>dateObj</code> argument in the format <code>dd-MM-yyyy HH:mm:ss</code>
     * and returns a string representation of the formatted date
     *
     * @param dateObj Date object to be formatted
     * @return A string representation of the formatted date
     */
    public static String formatDateToString(Date dateObj) {
        return formatDateToString(dateObj, DATE_FORMAT.DEFAULT);
    }

    /**
     * Returns a Date object by converting a string representation of the ISO Date passed in <b>dateObject</b> argument
     * in the format <code>{@link DATE_FORMAT}.DEFAULT</code>
     *
     * @param dateObject String representation of the ISO Date
     * @return Date object corresponding to the <code>dateObject</code> string
     * @throws ParseException Throws <code>{@link ParseException}</code> if the supplied string cannot be converted to a Date object
     */
    public static Date formatStringToDate(String dateObject) throws ParseException {
        return formatStringToDate(dateObject, DATE_FORMAT.DEFAULT);
    }

    /**
     * Returns a Date object by converting a string representation of the ISO Date passed in <b>dateObject</b> argument
     * in the format <code>{@link DATE_FORMAT}</code> passed as argument in <b>dateFormat</b>
     *
     * @param dateObject String representation of the ISO Date
     * @param dateFormat Format {@link DATE_FORMAT} in which the Date should be interpreted
     * @return Date object corresponding to the <code>dateObject</code> string
     * @throws ParseException Throws <code>{@link ParseException}</code> if the supplied string cannot be converted to a Date object
     */
    public static Date formatStringToDate(String dateObject, DATE_FORMAT dateFormat) throws ParseException {
        return formatStringToDate(dateObject, DATE_FORMATS.get(dateFormat), false);
    }

    /**
     * Returns a Date object by converting a string representation of the ISO Date passed in <b>dateObject</b> argument
     * in the String format passed as argument in <b>dateFormat</b>
     *
     * @param dateObject String representation of the ISO Date
     * @param dateFormat String format in which the Date should be interpreted
     * @param isCustomFormat Boolean object to denote custom date format other than specified in {@link DATE_FORMAT}
     * @return Date object corresponding to the <code>dateObject</code> string
     * @throws ParseException Throws <code>{@link ParseException}</code> if the supplied string cannot be converted to a Date object
     */
    public static Date formatStringToDate(String dateObject, String dateFormat, Boolean isCustomFormat) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        return formatter.parse(dateObject);
    }

    /**
     * Returns a Date object by converting a string representation of the ISO Date passed in <b>dateObject</b> argument
     * with the given <code>timeZoneString</code> timezone in the format <code>{@link DATE_FORMAT}</code> passed as argument in <b>dateFormat</b>
     *
     * @param dateObject String representation of the ISO Date
     * @param dateFormat Format {@link DATE_FORMAT} in which the Date should be interpreted
     * @param timeZoneString String representation of the ISO Timezone format
     * @return Date object corresponding to the <code>dateObject</code> string
     * @throws ParseException Throws <code>{@link ParseException}</code> if the supplied string cannot be converted to a Date object
     */
    public static Date formatStringToDate(String dateObject, DATE_FORMAT dateFormat, String timeZoneString) throws ParseException {
        return formatStringToDate(dateObject, DATE_FORMATS.get(dateFormat), timeZoneString);
    }

    /**
     * Returns a Date object by converting a string representation of the ISO Date passed in <b>dateObject</b> argument
     * with the given <code>timeZoneString</code> timezone in the String format passed as argument in <b>dateFormat</b>
     *
     * @param dateObject String representation of the ISO Date
     * @param dateFormat String format in which the Date should be interpreted
     * @param timeZoneString String representation of the ISO Timezone format
     * @return Date object corresponding to the <code>dateObject</code> string
     * @throws ParseException Throws <code>{@link ParseException}</code> if the supplied string cannot be converted to a Date object
     */
    public static Date formatStringToDate(String dateObject, String dateFormat, String timeZoneString) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);
        formatter.setTimeZone(timeZone);
        return formatter.parse(dateObject);
    }

    /**
     * Returns a Date object by converting a string representation of the ISO Date passed in <b>dateObject</b> argument
     * with the given <code>timeZoneString</code> timezone in the format <code>{@link DATE_FORMAT}</code>.DEFAULT
     *
     * @param dateObject String representation of the ISO Date
     * @param timeZoneString String representation of the ISO Timezone format
     * @return Date object corresponding to the <code>dateObject</code> string
     * @throws ParseException Throws <code>{@link ParseException}</code> if the supplied string cannot be converted to a Date object
     */
    public static Date formatStringToDate(String dateObject, String timeZoneString) throws ParseException {
        return formatStringToDate(dateObject, DATE_FORMAT.DEFAULT, timeZoneString);
    }

    /**
     * Downloads a file from the URL given in <code>urlLinkToDownloadFrom</code> argument and returns the absolute path
     * of the downloaded file. Additionally this is renamed to <code>downloadedFileName</code> after downloading
     *
     * @param mContext Application context object to access the application
     * @param urlLinkToDownloadFrom URL string to download file from
     * @param downloadedFileName File name string for the downloaded file in application internal data directory
     * @return <code>newFileLocation</code> Location of the downloaded file
     */
    public static String downloadFileFromURL(Context mContext, String urlLinkToDownloadFrom, String downloadedFileName) {
        int count;
        try {
            URL url = new URL(urlLinkToDownloadFrom);
            URLConnection connection = url.openConnection();
            connection.connect();
            InputStream input = new BufferedInputStream(url.openStream(), 8192);
            String newFileLocation = mContext.getExternalFilesDir(null).toString() + "/" + downloadedFileName;
            writeLog(LOG_LEVEL.INFO, "File downloaded at: " + newFileLocation);
            OutputStream output = new FileOutputStream(newFileLocation);
            byte[] data = new byte[1024];
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();
            hideProgressDialog();
            return newFileLocation;
        } catch (Exception e) {
            writeLog(LOG_LEVEL.ERROR, e.getMessage());
            hideProgressDialog();
            return null;
        }
    }

    /**
     * Reads data from a file stored at an URL denoted by <code>urlLink</code> argument
     * and returns the contents of the file
     *
     * @param mContext Application context object to access the application
     * @param urlLink URL string of the resource to read data from
     * @return Contents of the file / URL resource
     */
    public static String readFileFromURL(Context mContext, String urlLink) {
        return readFileFromURL(mContext, urlLink, null);
    }

    /**
     * Reads data from a file stored at an URL denoted by <code>urlLink</code> argument
     * and returns the contents of the file
     *
     * @param mContext Application context object to access the application
     * @param urlLink URL string of the resource to read data from
     * @param fileName <code>Optional</code>
     * @return Contents of the file / URL resource
     */
    public static String readFileFromURL(Context mContext, String urlLink, String fileName) {
        if (fileName == null || fileName.trim().equalsIgnoreCase("")) {
            fileName = "Dummy.spd";
        }
        try {
            String newFileLocation = downloadFileFromURL(mContext, urlLink, fileName);
            StringBuilder sb = new StringBuilder();
            File myObj = new File(newFileLocation);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String readData = myReader.nextLine();
                sb.append(readData);
                sb.append("\n");
            }
            myReader.close();
            hideProgressDialog();
            return sb.toString();
        } catch (Exception e) {
            writeLog(LOG_LEVEL.ERROR, e.getMessage());
            hideProgressDialog();
        }
        return null;
    }

    /**
     * Writes <b>data</b> to a file in the application internal data directory by the filename <b>fileName</b>
     * and returns the absolute path of the saved file
     *
     * @param data String data to write to the file
     * @param fileName Name of the file to write data to
     * @param context Application context object to access the application
     * @return Absolute path of the saved file or <code>Empty</code> string in case of any error
     */
    private static String writeToFile(String data, String fileName, Context context) {
        try {
            File documentsPath = new File(context.getExternalFilesDir(null).toString());
            if (!documentsPath.exists()) {
                documentsPath.mkdirs();
            }
            File myExternalFile = new File(documentsPath, fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(myExternalFile);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            fileOutputStream.close();
            return myExternalFile.getAbsolutePath();
        } catch (IOException e) {
            writeLog(LOG_LEVEL.ERROR, "File write failed: " + e.toString());
        }
        return "";
    }

    /**
     * Encodes a string passed as an argument in <b>text</b> parameter and returns its <u>Base64</u>
     * encoded string representation
     *
     * @param text Text to be encoded
     * @return Base64 encoded string representation of <b>text</b> argument
     */
    public static String returnEncoded(String text) {
        if (text == null) {
            return null;
        }
        return Base64.encodeToString(text.getBytes(), Base64.NO_WRAP);
    }

    /**
     * Decodes & Returns a Base64 encoded string passed as an argument in <b>encodedText</b> parameter
     *
     * @param encodedText Base64 encoded string to be decoded
     * @return Base64 decoded string representation of the <b>encodedText</b> (Base64 encoded)
     */
    public static String returnDecoded(String encodedText) {
        if (encodedText == null) {
            return null;
        }
        byte[] decoded = Base64.decode(encodedText, Base64.NO_WRAP);
        return new String(decoded);
    }

    /**
     * Unzips a zipped file of the name passed in <b>_zipFile</b> argument and puts the contents of the
     * zip file in the location passed in <b>_location</b> argument
     *
     * @param _location Location of the ZIP file
     * @param _zipFile Name of the ZIP file
     */
    public static boolean unZipFile(String _location, String _zipFile) {
        boolean unzipped = false;
        try {
            FileInputStream fin = new FileInputStream(_location + "/" + _zipFile);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze;
            while ((ze = zin.getNextEntry()) != null) {
                if (ze.isDirectory()) {
                    File f = new File(_location + "/" + ze.getName());
                    if(!f.isDirectory()) {
                        f.mkdirs();
                    }
                } else {
                    FileOutputStream fOut = new FileOutputStream(_location + "/" + ze.getName());
                    BufferedOutputStream bufOut = new BufferedOutputStream(fOut);
                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = zin.read(buffer)) != -1) {
                        bufOut.write(buffer, 0, read);
                    }
                    bufOut.close();
                    zin.closeEntry();
                    fOut.close();
                }
            }
            zin.close();
            unzipped = true;
        } catch (Exception e) {
            writeLog(LOG_LEVEL.ERROR, "Unzipping failed - " + e.getLocalizedMessage());
        }
        return unzipped;
    }

    /**
     * Opens the APK file by the name given in <b>fileName</b> argument and stored in the application's internal
     * data directory. A Context object <b>mContext</b> is passed as argument to retrieve the application's internal
     * data directory or package name or starting an activity/intent
     *
     * @param fileName Name of the APK file to update
     * @param mContext A context object to use &amp; work with application's context
     * @return Name of the APK file to update for further use in code
     */
    public static String openApkAndUpdatePackage(String fileName, Context mContext) {
        File file = new File(mContext.getExternalFilesDir(null), fileName);
        Intent unKnownSourceIntent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).setData(Uri.parse(String.format("package:%s", mContext.getPackageName())));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!mContext.getPackageManager().canRequestPackageInstalls()) {
                ((Activity) mContext).startActivityForResult(unKnownSourceIntent, OPEN_APK_UPDATE_PACKAGE_CODE);
            } else {
                Uri fileUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".provider", file);
                Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
                intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                mContext.startActivity(intent);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Intent intent1 = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            Uri uri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".provider", file);
            mContext.grantUriPermission(mContext.getPackageName(), uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            mContext.grantUriPermission(mContext.getPackageName(), uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.setDataAndType(uri, "application/*");
            intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent1.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            mContext.startActivity(intent1);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
        return fileName;
    }

    /**
     * Validates a given <b>text</b> with basic default text <u>Regex</u> pattern
     *
     * @param text String to be validated
     * @return <b>true</b> or <b>false</b> based on Regex validation of the supplied <b>text</b>
     */
    public static boolean BasicTextValidation(String text) {
        String pattern = "^[A-Za-z0-9.)*&,(\\-\\s]{2,}$";
        return text.matches(pattern);
    }

    /**
     * Validates a given <b>text</b> with basic default decimal <u>Regex</u> pattern.
     * Checks for only 2 digits of decimal. For decimal place as argument, use <code>BasicDecimalNumberValidation(String text, int decimalDigits)</code>
     *
     * @param text String to be validated
     * @return <b>true</b> or <b>false</b> based on Regex validation of the supplied <b>text</b> as decimal number upto 2 decimal digits
     */
    public static boolean BasicDecimalNumberValidation(String text) {
        return BasicDecimalNumberValidation(text, 2);
    }

    /**
     * Validates a given <b>text</b> with basic default decimal <u>Regex</u> pattern.
     *
     * @param text String to be validated
     * @return <b>true</b> or <b>false</b> based on Regex validation of the supplied <b>text</b> as decimal number
     */
    public static boolean BasicDecimalNumberValidation(String text, int decimalDigits) {
        String pattern = "^-?\\d*\\.?\\d{1," + decimalDigits + "}$";
        return text.matches(pattern);
    }

    /**
     * Formats &amp; Rounds Up a given Double / Decimal value upto 2 places of decimal digits
     *
     * @param value Double / Decimal value to be formatted &amp; rounded up
     * @return Formatted &amp; Rounded up Double / Decimal value supplied in <b>value</b> argument upto 2 decimal digits
     */
    public static double formatDecimalValue(double value) {
        return formatDecimalValue(value, 2);
    }

    /**
     * Formats &amp; Rounds Up a given Double / Decimal value upto <b>uptoDigits</b> places of decimal digits
     *
     * @param value Double / Decimal value to be formatted &amp; rounded up
     * @param uptoDigits Number of decimal digits to be rounded up to
     * @return Formatted &amp; Rounded up Double / Decimal value supplied in <b>value</b> argument
     */
    public static double formatDecimalValue(double value, int uptoDigits) {
        return formatDecimalValue(value, uptoDigits, RoundingMode.HALF_UP);
    }

    /**
     * Formats &amp; Rounds a given Double / Decimal value upto <b>uptoDigits</b> places of decimal digits
     * as per the <b>roundingMode</b> argument
     *
     * @param value Double / Decimal value to be formatted &amp; rounded up
     * @param uptoDigits Number of decimal digits to be rounded up to
     * @param roundingMode RoundingMode ENUM value to instruct the rounding mode action
     * @return Formatted &amp; Rounded (as per <code>roundingMode</code>) Double / Decimal value supplied in <b>value</b> argument
     */
    public static double formatDecimalValue(double value, int uptoDigits, RoundingMode roundingMode) {
        BigDecimal bd = BigDecimal.valueOf(value);
        if (bd != null) {
            bd = bd.setScale(uptoDigits, roundingMode);
            return bd.doubleValue();
        }
        return value;
    }

    /**
     * Checks for the running application package whether the supplied argument <code>serviceClass</code> service object is
     * running or not
     *
     * @param serviceClass Service Class object to be checked for
     * @param mContext Application Context object to access the application
     * @return <b>true</b> or <b>false</b> if the given <code>serviceClass</code> is running or not
     */
    public static boolean isMyServiceRunning(Class<?> serviceClass, Context mContext) {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines whether the color code passed as an argument in <b>color</b> parameter is dark color or not.
     * This is determined based on the illuminant factor of the color code
     *
     * @param color Parsed Color code to determine whether it's dark or not
     * @return <b>true</b> or <b>false</b> if the given <code>color</code> is dark color or not
     */
    public static boolean isColorDark(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return !(darkness < 0.5);
    }

    /**
     * Handler method to handle any piece of code after default <b>0.5 seconds</b> delay in execution.
     * For defining custom delay, use {@code attachHandler({@link Runnable} runnable, {@link Long} delayMilliseconds)}
     *
     * @param runnable Runnable object to execute after the 0.5 seconds delay
     */
    public static void attachHandler(Runnable runnable) {
        attachHandler(runnable, 500);
    }

    /**
     * Handler method to handle any piece of code after a defined delay in execution
     *
     * @param runnable Runnable object to execute after the set delay
     * @param delayMilliseconds Delay time in milliseconds
     */
    public static void attachHandler(Runnable runnable, long delayMilliseconds) {
        appHandler.postDelayed(runnable, delayMilliseconds);
    }

    /**
     * Exports your application local database and provides you with a Share option to share outside the application.
     *
     * You must specify a <code>provider</code> tag in your app manifest file with <code>android:authorities="${applicationId}.provider"</code>
     * as an attribute. Learn more about {@link FileProvider} here
     *
     * @param context Application context object to access the application
     * @param dbName Name of the db file object to export
     * @return {@code 1} if export is successful else {@code 0}
     */
    public static int exportAppDbFile(Context context, String dbName) {
        int responseCode = 0;
        File dbFile = context.getDatabasePath(dbName);
        String myFilePath = dbFile.getAbsolutePath();
        writeLog(LOG_LEVEL.INFO, myFilePath);
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        File fileWithinMyDir = new File(myFilePath);
        if (fileWithinMyDir.exists()) {
            intentShareFile.setType("application/vnd.sqlite3");
            Uri fileUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", new File(myFilePath));
            intentShareFile.putExtra(Intent.EXTRA_STREAM, fileUri);
            intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "Exporting App DB - " + dbName);
            intentShareFile.putExtra(Intent.EXTRA_TEXT, "Exporting Application DB - " + dbName);
            context.startActivity(Intent.createChooser(intentShareFile, "Save Exported DB"));
            Toast.makeText(context, "Upload this backup file to your File System/Google Drive for restoring at a later time.", Toast.LENGTH_LONG).show();
            responseCode = 1;
        }
        return responseCode;
    }

    /**
     * Invokes a REST API by the URL given in <code>REST_API_URL</code>, passing query parameters to it as given in <code>REST_API_PARAMS</code>
     * and returning response in a callback interface
     *
     * @param REST_API_URL URL of the REST API to invoke to get API token
     * @param REST_API_PARAMS URL Query Params string containing the request parameters
     * @param restApiCallback Callback interface in which {@code onSuccess()} method will be invoked after receiving the response from REST API URL
     * @return {@link REST_STATUS enum values stating status of the transaction}
     */
    public static Object invokeRESTAPI(String REST_API_URL, String REST_API_PARAMS, RestApiCallback restApiCallback) {
        try {
            URL url = new URL(REST_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            conn.connect();
            try(OutputStream os = conn.getOutputStream()) {
                byte[] input = REST_API_PARAMS.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            try(BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                Object jsonObject = null;
                if (response.toString().endsWith("]")) {
                    jsonObject = new JSONArray(response.toString());
                } else {
                    jsonObject = new JSONObject(response.toString());
                }
                if (restApiCallback != null) {
                    restApiCallback.onSuccess(jsonObject);
                } else {
                    return jsonObject;
                }
            }
            return REST_STATUS.IN_PROGRESS;
        } catch (Exception e) {
            writeLog(SPDUtilities.LOG_LEVEL.ERROR, e.getMessage());
            return REST_STATUS.FAILED;
        }
    }

    /**
     * Invokes a REST API by the URL given in <code>REST_API_URL</code>, passing query parameters to it as given in <code>REST_API_PARAMS</code>
     * and returning response as a {@link JSONObject}
     *
     * @param REST_API_URL URL of the REST API to invoke to get API token
     * @param REST_API_PARAMS URL Query Params string containing the request parameters
     * @return {@link REST_STATUS enum values stating status of the transaction}
     */
    public static Object invokeRESTAPI(String REST_API_URL, String REST_API_PARAMS) {
        return invokeRESTAPI(REST_API_URL, REST_API_PARAMS, null);
    }

    public interface RestApiCallback {
        void onSuccess(Object o);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String encodeString(String inputText) {
        try {
            return URLEncoder.encode(inputText, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }
}
