package ru.geekbrains.android.listDayOfWeek;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.geekbrains.android.R;


public class ListDayOfWeekAdapter extends RecyclerView.Adapter<ListDayOfWeekAdapter.WeekViewHolder>{
    private  List<DayOfWeek> listDayOfWeek;
    @NonNull
    @Override
    public WeekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new WeekViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_week, parent, false)
        );

    }

    @Override
    public void onBindViewHolder(@NonNull WeekViewHolder holder, int position) {
        holder.bind(listDayOfWeek.get(position));
    }

    @Override
    public int getItemCount() {
        if (listDayOfWeek == null) return 0;

        return listDayOfWeek.size();
    }

    public void update(List<DayOfWeek> listDayOfWeek) {
        this.listDayOfWeek = listDayOfWeek;
        notifyDataSetChanged();
    }

    static class WeekViewHolder extends RecyclerView.ViewHolder {

        private final TextView textNameDay;
        private final ImageView image;
        private final TextView humidity;
        private final TextView temperature;


        public WeekViewHolder(@NonNull View itemView) {
            super(itemView);
            textNameDay = itemView.findViewById(R.id.item_week_day_of_week);
            image = itemView.findViewById(R.id.item_week_image);
            humidity = itemView.findViewById(R.id.item_week_humidity);
            temperature = itemView.findViewById(R.id.item_week_temperature);

        }

        public void bind(DayOfWeek dayOfWeek) {
            textNameDay.setText(dayOfWeek.getNameDayOfWeek());
            image.setImageResource(dayOfWeek.getImageId());
            humidity.setText(dayOfWeek.getHumidity());
            temperature.setText(dayOfWeek.getTemperature());
        }
    }
}

