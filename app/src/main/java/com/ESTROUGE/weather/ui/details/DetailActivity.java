package com.ESTROUGE.weather.ui.details;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ESTROUGE.weather.R;
import com.ESTROUGE.weather.ui.base.BaseActivity;
import com.ESTROUGE.weather.ui.overview.MainActivity;
import com.ESTROUGE.weather.utils.CommonUtils;
import com.ESTROUGE.weather.utils.LogHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import static com.ESTROUGE.weather.utils.Define.ACCOUNTS;
import static com.ESTROUGE.weather.utils.Define.CALL;
import static com.ESTROUGE.weather.utils.Define.CAMERA;
import static com.ESTROUGE.weather.utils.Define.LOCATION;
import static com.ESTROUGE.weather.utils.Define.READ_EXST;
import static com.ESTROUGE.weather.utils.Define.WRITE_EXST;

public class DetailActivity extends BaseActivity implements DetailMvpView{
    private static final String TAG = "DetailActivity";

    @Inject
    DetailMvpPresenter<DetailMvpView> mPresenter;

    TextView txtToday;
    TextView txtDate;
    TextView txtTemp;
    TextView txtTempmin;
    TextView txtDescription;
    TextView txtHumidity;
    TextView txtPressure;
    TextView txtWind;
    ImageView imgIconWeather;
    String date, description;
    float temp, tempmin, humidity, pressure, wind;
    private RelativeLayout rootContent;
    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, DetailActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Details");
        }
        txtToday = (TextView) findViewById(R.id.today);
        txtDate = (TextView) findViewById(R.id.date);
        txtTemp = (TextView) findViewById(R.id.temp);
        txtTempmin = (TextView) findViewById(R.id.tempmin);
        txtDescription = (TextView) findViewById(R.id.description);
        txtHumidity = (TextView) findViewById(R.id.humidity);
        txtPressure = (TextView) findViewById(R.id.pressure);
        txtWind = (TextView) findViewById(R.id.wind);
        imgIconWeather = (ImageView) findViewById(R.id.iconWeather);
        rootContent = (RelativeLayout) findViewById(R.id.root);

        Intent intent = getIntent();
        date = intent.getStringExtra("DATE");
        description = intent.getStringExtra("DESCRIPTION");
        temp = intent.getFloatExtra("TEMP", 0);
        tempmin = intent.getFloatExtra("TEMP_MIN", 0);
        humidity = intent.getFloatExtra("HUMINDITY", 0);
        pressure = intent.getFloatExtra("PRESSURE", 0);
        wind = intent.getFloatExtra("WIND", 0);

        getActivityComponent().inject(this);

        mPresenter.onAttach(this);
        setUp();
    }

    @Override
    public void error() {
    }

    @Override
    public void setUp() {
        Date d = CommonUtils.String2Date(date);
        if(CommonUtils.isToday(d))
            txtToday.setText(getString(R.string.s_today));
        else if(CommonUtils.isTomorrow(d))
            txtToday.setText(getString(R.string.s_tomorrow));
        else {
            DateFormat todayFormatter = new SimpleDateFormat("EEEE", Locale.ENGLISH);
            String strToday = todayFormatter.format(d);
            txtToday.setText(strToday);
        }
        DateFormat dateFormatter = new SimpleDateFormat("MMMM dd", Locale.ENGLISH);
        String strDate = dateFormatter.format(d);
        txtDate.setText(strDate);
        txtDescription.setText(description);
        txtTemp.setText(""+temp);
        txtTempmin.setText(""+tempmin);
        txtHumidity.setText("Humidity: "+humidity + " %");
        txtPressure.setText("Pressure: "+pressure + " hPa");
        txtWind.setText("Wind: "+ CommonUtils.getTwoDecimals(wind*3.6) +" km/h W");
        imgIconWeather.setImageResource(CommonUtils.getImageBackgroundResouce(description.toLowerCase()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                finish();
                break;
            case R.id.action_menu_share:
                LogHelper.e(TAG, "action_menu_share");
                //Toast.makeText(MainActivity.this, "You must grant storage permission! (Todo....)", Toast.LENGTH_LONG).show();

                if(askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXST)){
                    CommonUtils.shareWeatherInformation(getApplicationContext(), rootContent);
                }
                return true;
            case R.id.action_menu_bluetooth:
                LogHelper.e(TAG, "action_menu_bluetooth");
                //CommonUtils.shareWeatherInformation(getApplicationContext(), rootContent);
                Toast.makeText(DetailActivity.this, "Todo ...............!", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_menu_google:
                LogHelper.e(TAG, "action_menu_google");
                Toast.makeText(DetailActivity.this, "Todo ...............!", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_menu_facebook:
                LogHelper.e(TAG, "action_menu_facebook");
                Toast.makeText(DetailActivity.this, "Todo ...............!", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
            switch (requestCode) {
                case LOCATION:
                    break;
                case CALL:
                    break;
                case WRITE_EXST:
                case READ_EXST:
                    CommonUtils.shareWeatherInformation(getApplicationContext(), rootContent);
                    break;
                case CAMERA:
                    break;
                case ACCOUNTS:
                    break;
            }
            //Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private boolean askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(DetailActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(DetailActivity.this, permission)) {
                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(DetailActivity.this, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(DetailActivity.this, new String[]{permission}, requestCode);
            }
            return false;
        } else {
            //Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
}
