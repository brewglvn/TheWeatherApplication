package com.ESTROUGE.weather.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.ESTROUGE.weather.R;

import java.util.ArrayList;
import java.util.List;

public class CustomDialog extends Dialog implements View.OnClickListener {

    private Activity c;
    private Dialog d;
    private Button btnOK;
    private Button btnCurrent;
    private EditText editlatitude;
    private EditText editlongitude;
    private int layoutResID;
    private OnCustomDialogClickListener mOKClickListener;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<City> myDataset;

    public static interface OnCustomDialogClickListener {
        public void onClick(CustomDialog customDialogClass, double latitude, double longitude);
    }
    public CustomDialog(Activity a, int layoutResID) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.layoutResID = layoutResID;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(layoutResID);
        btnOK = (Button) findViewById(R.id.btnOK);
        btnCurrent = (Button) findViewById(R.id.current);
        editlatitude = (EditText) findViewById(R.id.editlatitude);
        editlongitude = (EditText) findViewById(R.id.editlongitude);
        btnOK.setOnClickListener(this);
        btnCurrent.setOnClickListener(this);
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(false);

        myDataset = new ArrayList<>();
        myDataset.add(new City("London",37.129, -84.0833));
        myDataset.add(new City("Tokyo", 35.6828, 139.759));
        myDataset.add(new City("New York", 40.7306, -73.9867));
        myDataset.add(new City("Berlin", 52.517, 13.3889));
        myDataset.add(new City("Paris", 48.8566, 2.3515));
        myDataset.add(new City("Ha Noi", 21.0245, 105.8412));

        mRecyclerView = (RecyclerView) findViewById(R.id.cities);

        LinearLayoutManager mLayoutManagerPreparation = new LinearLayoutManager(c, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManagerPreparation);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new LocationAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setCancelable(Boolean cancelable)
    {
        this.setCancelable(cancelable);
    }

    public CustomDialog setOkClickListener(OnCustomDialogClickListener listener) {
        mOKClickListener = listener;
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOK:
                if (mOKClickListener != null) {
                    double la = editlatitude.getText().toString().trim().isEmpty() ? 0 : Float.parseFloat(editlatitude.getText().toString().trim());
                    double lo = editlongitude.getText().toString().trim().isEmpty() ? 0 : Float.parseFloat(editlongitude.getText().toString().trim());
                    mOKClickListener.onClick(CustomDialog.this, la, lo);
                }
                break;
            case R.id.current:
                City dn = new City("DaNang", 16.068,108.2119);
                editlatitude.setText(""+dn.getLatitude());
                editlongitude.setText(""+dn.getLongitude());
                break;
            default:
                break;
        }
    }

    public class City{
        private String name;
        private double latitude;
        private double longitude;

        public City(){}

        public City(String name, double latitude, double longitude){
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }

    public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.MyViewHolder> {
        private List<City> mDataset;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView cityName;
            public MyViewHolder(View view) {
                super(view);
                this.cityName = (TextView) view.findViewById(R.id.city);
            }
        }

        public LocationAdapter(List<City> myDataset) {
            mDataset = myDataset;
        }

        @Override
        public LocationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_location_item, null);
            LocationAdapter.MyViewHolder mh = new LocationAdapter.MyViewHolder(v);
            return mh;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.cityName.setText(mDataset.get(position).getName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    City city = myDataset.get(position);
                    editlatitude.setText(""+city.getLatitude());
                    editlongitude.setText(""+city.getLongitude());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }
}