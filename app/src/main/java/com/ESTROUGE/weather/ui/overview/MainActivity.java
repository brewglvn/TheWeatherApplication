package com.ESTROUGE.weather.ui.overview;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ESTROUGE.weather.R;
import com.ESTROUGE.weather.data.model.Weather;
import com.ESTROUGE.weather.ui.base.BaseActivity;
import com.ESTROUGE.weather.ui.details.DetailActivity;
import com.ESTROUGE.weather.utils.CommonUtils;
import com.ESTROUGE.weather.utils.CustomDialog;
import com.ESTROUGE.weather.utils.Define;
import com.ESTROUGE.weather.utils.LogHelper;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import static com.ESTROUGE.weather.utils.Define.ACCOUNTS;
import static com.ESTROUGE.weather.utils.Define.CALL;
import static com.ESTROUGE.weather.utils.Define.CAMERA;
import static com.ESTROUGE.weather.utils.Define.LOCATION;
import static com.ESTROUGE.weather.utils.Define.READ_EXST;
import static com.ESTROUGE.weather.utils.Define.WRITE_EXST;

public class MainActivity extends BaseActivity implements MainMvpView, RecyclerViewClickListener {
    private static final String TAG = "MainActivity";
    private Weather today;
    private List<Weather> theWeatherList;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;;
    private RelativeLayout rootContent;

    @Inject
    MainMvpPresenter<MainMvpView> mPresenter;

    TextView txtDate;
    TextView txtTemp;
    TextView txtTempmin;
    TextView txtDescription;
    ImageView imgIconWeather;

    ImageButton btnMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        today = new Weather();
        theWeatherList = new ArrayList<>();
        rootContent = (RelativeLayout) findViewById(R.id.rtoday);

        txtDate = (TextView) findViewById(R.id.date);
        txtTemp = (TextView) findViewById(R.id.temp);
        txtTempmin = (TextView) findViewById(R.id.tempmin);
        txtDescription = (TextView) findViewById(R.id.description);
        imgIconWeather = (ImageView) findViewById(R.id.iconWeather);
        recyclerView = (RecyclerView) findViewById(R.id.recyclers);
        btnMore = (ImageButton) findViewById(R.id.more);

        LinearLayoutManager mLayoutManagerPreparation = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManagerPreparation);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getActivityComponent().inject(this);

        mPresenter.onAttach(MainActivity.this);

        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenuWithIcon(view);
            }
        });
    }

    @SuppressLint("RestrictedApi")
    public void showPopupMenuWithIcon(final View view){
        Context wrapper = new ContextThemeWrapper(this,R.style.popupMenuStyle);
        MenuBuilder menuBuilder =new MenuBuilder(wrapper);
        MenuInflater inflater = new MenuInflater(wrapper);
        inflater.inflate(R.menu.menu_main, menuBuilder);
        MenuPopupHelper optionsMenu = new MenuPopupHelper(wrapper, menuBuilder, view);
        optionsMenu.setForceShowIcon(true);
        menuBuilder.setCallback(new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                switch (item.getItemId()) {
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
                        Toast.makeText(MainActivity.this, "Todo ...............!", Toast.LENGTH_LONG).show();
                        return true;
                    case R.id.action_menu_google:
                        LogHelper.e(TAG, "action_menu_google");
                        Toast.makeText(MainActivity.this, "Todo ...............!", Toast.LENGTH_LONG).show();
                        return true;
                    case R.id.action_menu_facebook:
                        LogHelper.e(TAG, "action_menu_facebook");
                        Toast.makeText(MainActivity.this, "Todo ...............!", Toast.LENGTH_LONG).show();
                        return true;
                    case R.id.action_menu_location:
                        LogHelper.e(TAG, "action_menu_location");
                        ShowEditLocation();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onMenuModeChange(MenuBuilder menu) {}
        });
        // Display the menu
        optionsMenu.show();
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

    private void ShowEditLocation(){
        CustomDialog customDialog = new CustomDialog(this, R.layout.location);
        customDialog.setOkClickListener(new CustomDialog.OnCustomDialogClickListener() {
            @Override
            public void onClick(CustomDialog sDialog, double la, double lo) {
                LogHelper.e(TAG, "Latitude : " + la);
                LogHelper.e(TAG, "Longitude : " + lo);
                sDialog.dismiss();
                mPresenter.refresh(la, lo);
            }
        });
        customDialog.show();
    }

    @Override
    public void update(Weather today, List<Weather> list) {
        this.today = today;
        this.theWeatherList = list;
        LogHelper.e(TAG, "weathers size : " + theWeatherList.size());
    }

    @Override
    public void error() {
        setContentView(R.layout.error);
    }

    @Override
    public void setUp() {
        Date date = CommonUtils.String2Date(today.getDt_txt());
        DateFormat formatter = new SimpleDateFormat("MMMM dd", Locale.ENGLISH);
        String strToday = formatter.format(date);
        txtDate.setText("Today, " + strToday);
        txtTemp.setText(""+today.getMain_temp());
        txtTempmin.setText(""+today.getMain_temp_min());
        txtDescription.setText(""+today.getWeather_main());
        imgIconWeather.setImageResource(CommonUtils.getImageBackgroundResouce(today.getWeather_main().toLowerCase()));

        recyclerViewAdapter = new RecyclerViewAdapter(this, theWeatherList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setRecyclerViewClickListener(this);
        recyclerViewAdapter.notifyDataSetChanged();
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

    @Override
    public void onRecyclerViewClick(View view, int position) {
        Intent intent = DetailActivity.getStartIntent(MainActivity.this);
        Weather weather = theWeatherList.get(position);

        intent.putExtra("DATE", weather.getDt_txt());
        intent.putExtra("TEMP", weather.getMain_temp());
        intent.putExtra("TEMP_MIN", weather.getMain_temp_min());
        intent.putExtra("HUMINDITY", weather.getMain_humidity());
        intent.putExtra("PRESSURE", weather.getMain_pressure());
        intent.putExtra("WIND", weather.getWind_speed());
        intent.putExtra("DESCRIPTION", weather.getWeather_main());
        startActivity(intent);
    }

    private boolean askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {
                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
            return false;
        } else {
            //Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
}
