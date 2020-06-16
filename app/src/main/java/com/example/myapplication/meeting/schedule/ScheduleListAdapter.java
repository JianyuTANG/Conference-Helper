package com.example.myapplication.meeting.schedule;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;


public class ScheduleListAdapter
        extends RecyclerView.Adapter<ScheduleListAdapter.ScheduleViewHolder> {
    private final LayoutInflater mInflater;
    private List<Schedule> schedules; // Cached copy of meetings

    public ScheduleListAdapter(Context context) { this.mInflater = LayoutInflater.from(context); }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
        notifyDataSetChanged();
    }

    public Schedule getScheduleAtPosition (int position) {
        return schedules.get(position);
    }

    @Override
    public ScheduleListAdapter.ScheduleViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.component_schedule_item,
                parent, false);
        return new ScheduleListAdapter.ScheduleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ScheduleListAdapter.ScheduleViewHolder holder, int position) {
        if (schedules != null) {
            Schedule current = schedules.get(position);
            holder.titleView.setText(current.getTitle());
            holder.lecturerView.setText(current.getLecturer());
            holder.timeView.setText(current.getTime());
            holder.placeView.setText(current.getPlace());
        } else {
            // Covers the case of data not being ready yet.
            holder.titleView.setText("no meeting");
            holder.lecturerView.setText("None");
            holder.timeView.setText("None");
            holder.placeView.setText("None");
        }
    }

    @Override
    public int getItemCount() {
        if (schedules != null)
            return schedules.size();
        else return 0;
    }

    class ScheduleViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final TextView lecturerView;
        private final TextView timeView;
        private final TextView placeView;

        private ScheduleViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.schedule_item_title);
            lecturerView = itemView.findViewById(R.id.schedule_item_lecturer);
            timeView = itemView.findViewById(R.id.schedule_item_time);
            placeView = itemView.findViewById(R.id.schedule_item_place);
        }
    }
}
