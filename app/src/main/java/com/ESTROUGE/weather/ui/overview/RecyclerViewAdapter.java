package com.ESTROUGE.weather.ui.overview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ESTROUGE.weather.R;
import com.ESTROUGE.weather.data.model.Weather;
import com.ESTROUGE.weather.utils.CommonUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ItemRowHolder>{

    private Context mContext;
    private RecyclerViewClickListener recyclerViewClickListener;
    private List<Weather> weathers;

    public RecyclerViewAdapter(Context context, List<Weather> weathers) {
        this.mContext = context;
        this.weathers = weathers;
    }

    public void setRecyclerViewClickListener(RecyclerViewClickListener recyclerViewClickListener)
    {
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycleview_item, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder,final int i) {
        Weather weather = weathers.get(i);
        itemRowHolder.item_icon.setImageResource(CommonUtils.getIconBackgroundResouce(weather.getWeather_main().toLowerCase()));

        Date date = CommonUtils.String2Date(weather.getDt_txt());
        if(CommonUtils.isToday(date))
            itemRowHolder.item_date.setText(mContext.getResources().getString(R.string.s_today));
        else if(CommonUtils.isTomorrow(date))
            itemRowHolder.item_date.setText(mContext.getResources().getString(R.string.s_tomorrow));
        else {
            DateFormat formatter = new SimpleDateFormat("EEEE", Locale.ENGLISH);
            String strToday = formatter.format(date);
            itemRowHolder.item_date.setText(strToday);
        }
        itemRowHolder.item_description.setText(weather.getWeather_main());
        itemRowHolder.item_temp.setText(""+weather.getMain_temp());
        itemRowHolder.item_temp_min.setText(""+weather.getMain_temp_min());

        itemRowHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewClickListener.onRecyclerViewClick(view, i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != weathers ? weathers.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        protected ImageView item_icon;
        protected TextView item_date;
        protected TextView item_description;
        protected TextView item_temp;
        protected TextView item_temp_min;
        protected ImageView item_degrees;
        protected ImageView item_degrees_min;

        public ItemRowHolder(View view) {
            super(view);
            this.item_icon = (ImageView) view.findViewById(R.id.item_icon);
            this.item_date = (TextView) view.findViewById(R.id.item_date);
            this.item_description = (TextView) view.findViewById(R.id.item_description);
            this.item_temp = (TextView) view.findViewById(R.id.item_temp);
            this.item_temp_min = (TextView) view.findViewById(R.id.item_temp_min);
            this.item_degrees = (ImageView) view.findViewById(R.id.item_icon);
            this.item_degrees_min = (ImageView) view.findViewById(R.id.item_degrees_min);
        }

    }

}
