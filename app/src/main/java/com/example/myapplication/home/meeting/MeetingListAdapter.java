package com.example.myapplication.home.meeting;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class MeetingListAdapter extends RecyclerView.Adapter<MeetingListAdapter.MeetingViewHolder> {
    private final LayoutInflater mInflater;
    private List<Meeting> meetings; // Cached copy of meetings

    private OnItemClickListener mOnItemClickListener;

    MeetingListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    public void setOnItemClickListener(MeetingListAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public MeetingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.component_meeting_item, parent, false);
        return new MeetingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MeetingViewHolder holder, int position) {
        if (meetings != null) {
            Meeting current = meetings.get(position);
            holder.titleView.setText(current.getTitle());
            holder.subTitleView.setText(current.getSub_title());
            Uri uri = Uri.parse(current.getIcon_url());
            holder.draweeView.setImageURI(uri);

            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnItemClickListener.onItemClick(holder.itemView, position);
                    }
                });
            }

        } else {
            // Covers the case of data not being ready yet.
            holder.titleView.setText("no meeting");
            holder.subTitleView.setText("none");
        }
    }

    void setMeetings(List<Meeting> meetings){
        this.meetings = meetings;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (meetings != null)
            return meetings.size();
        else return 0;
    }

    class MeetingViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final TextView subTitleView;
        private final SimpleDraweeView draweeView;

        private MeetingViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.meeting_item_title);
            subTitleView = itemView.findViewById(R.id.meeting_item_description);
            draweeView = itemView.findViewById(R.id.meeting_item_image);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public Meeting getMeetingAtPosition (int position) {
        return meetings.get(position);
    }
}
