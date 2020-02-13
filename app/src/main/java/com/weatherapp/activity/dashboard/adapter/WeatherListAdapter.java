package com.weatherapp.activity.dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.weatherapp.DateUtil;
import com.weatherapp.R;
import com.weatherapp.activity.dashboard.bean.DashboardBean;
import com.weatherapp.databinding.ItemWeatherBinding;
import com.weatherapp.util.server.APIConstants;

import java.util.ArrayList;

public class WeatherListAdapter extends RecyclerView.Adapter<WeatherListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<DashboardBean._List> mList;


    public WeatherListAdapter(Context context)
    {
        mContext = context;

    }


    public  void setList(ArrayList<DashboardBean._List> list)
    {
        mList = new ArrayList<>();
        mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemWeatherBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext) , R.layout.item_weather,
                parent , false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DashboardBean._List data = mList.get(position);

        holder.binding.tvTodayTemp.setText(""+String.format("%.0f", data.getMain().getTemp())+
                 " \u2103");
        String img = APIConstants.IMG_BASE_URL +data.getWeather().get(0).getIcon()
                + ".png";
        Glide.with(mContext).load(img).error(R.mipmap.ic_launcher)
                .dontAnimate().into(holder.binding.ivWeather);

        holder.binding.tvTime.setText(""+
                DateUtil.timeStamptodate(data.getDt(),
                        "dd-MM-yyyy hh:mm a"));

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
    ItemWeatherBinding binding;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }
}

}
