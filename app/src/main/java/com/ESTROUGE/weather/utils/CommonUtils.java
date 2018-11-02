package com.ESTROUGE.weather.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.text.format.DateUtils;
import android.view.View;

import com.ESTROUGE.weather.R;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class CommonUtils {

    private static final String TAG = "CommonUtils";

    private CommonUtils() {
    }

    public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

    public static JSONObject LoadJsonFromUrl(String urlString) {
        BufferedReader reader = null;
        try {
            URLConnection urlConnection = new URL(urlString).openConnection();
            urlConnection.setConnectTimeout(8000);
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return new JSONObject(sb.toString());
        } catch (Exception e) {
            LogHelper.e(TAG, "Failed to parse the json for media list ", e);
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    public static String getDateString(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
    public static Date String2Date(String datestr){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(datestr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static int getImageBackgroundResouce(String name){
        switch (name)
        {
            case "clear":
                return R.drawable.art_clear;
            case "clouds":
                return R.drawable.art_clouds;
            case "fog":
                return R.drawable.art_fog;
            case "light_clouds":
                return R.drawable.art_light_clouds;
            case "light_rain":
                return R.drawable.art_light_rain;
            case "rain":
                return R.drawable.art_rain;
            case "snow":
                return R.drawable.art_snow;
            case "storm":
                return R.drawable.art_storm;
            default:
                return R.drawable.art_clear;
        }
    }

    public static int getIconBackgroundResouce(String name){
        switch (name)
        {
            case "clear":
                return R.drawable.ic_clear;
            case "clouds":
                return R.drawable.ic_cloudy;
            case "fog":
                return R.drawable.ic_fog;
            case "light_clouds":
                return R.drawable.ic_light_clouds;
            case "light_rain":
                return R.drawable.ic_light_rain;
            case "rain":
                return R.drawable.ic_rain;
            case "snow":
                return R.drawable.ic_snow;
            case "storm":
                return R.drawable.ic_storm;
            default:
                return R.drawable.ic_clear;
        }
    }

    public static void shareWeatherInformation(Context context, View view){
        Bitmap bm = screenShot(view);
        File file = saveBitmap(bm, "weather.png");
        LogHelper.d(TAG, "filepath: "+file.getAbsolutePath());
        Uri uri = Uri.fromFile(new File(file.getAbsolutePath()));
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out my app.");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(shareIntent, "share via"));
    }

    public static void shareWeatherInformationViaBluetooth(Context context, View view){
        Bitmap bm = screenShot(view);
        File file = saveBitmap(bm, "weather.png");
        LogHelper.d(TAG, "filepath: "+file.getAbsolutePath());
        Uri uri = Uri.fromFile(new File(file.getAbsolutePath()));
        Intent shareIntent = new Intent();
        //shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out my app.");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(shareIntent, "share via"));
    }

    public static Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public static File saveBitmap(Bitmap bm, String fileName){
        final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(path);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dir, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 90, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static boolean isYesterday(Date d) {
        return DateUtils.isToday(d.getTime() + DateUtils.DAY_IN_MILLIS);
    }

    public static boolean isToday(Date d) {
        return DateUtils.isToday(d.getTime());
    }

    public static boolean isTomorrow(Date d) {
        return DateUtils.isToday(d.getTime() - DateUtils.DAY_IN_MILLIS);
    }

    public static Float convertToFloat(Double doubleValue) {
        return doubleValue == null ? null : doubleValue.floatValue();
    }

    public static String getTwoDecimals(double value){
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(value);
    }
}
