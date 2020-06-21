package com.example.myapplication.home.paper;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import com.example.myapplication.R;

public class PaperListAdapter extends RecyclerView.Adapter<PaperListAdapter.PaperViewHolder> {
    private final LayoutInflater mInflater;
    private List<Paper> papers; // Cached copy of meetings

    private OnItemClickListener mOnItemClickListener;

    public PaperListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    public void setOnItemClickListener(PaperListAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public PaperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.component_paper_item, parent, false);
        return new PaperViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PaperViewHolder holder, int position) {
        if (papers != null) {
            Paper current = papers.get(position);
            holder.titleView.setText(current.getTitle());
            String firstAuthor = current.getAuthor() + " et al.";
            holder.writersView.setText(firstAuthor);

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
            holder.writersView.setText("none");
        }
    }

    public void setPapers(List<Paper> papers){
        this.papers = papers;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (papers != null)
            return papers.size();
        else return 0;
    }

    class PaperViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final TextView writersView;

        private PaperViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.paper_item_title);
            writersView = itemView.findViewById(R.id.paper_item_writers);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public Paper getPaperAtPosition (int position) {
        return papers.get(position);
    }
}
