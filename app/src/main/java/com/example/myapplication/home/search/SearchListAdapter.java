package com.example.myapplication.home.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.SearchItemViewHolder>{
    private final LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private List<SearchResult> results;


    public SearchListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    public void setOnItemClickListener(SearchListAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setResults(List<SearchResult> results) {
        this.results = results;
        notifyDataSetChanged();
    }

    public SearchResult getResByPos(int position) { return results.get(position); }

    @NonNull
    @Override
    public SearchItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.listview_item, parent, false);
        return new SearchItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchItemViewHolder holder, int position) {
        if (results != null) {
            holder.titleView.setText(results.get(position).name);
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(holder.itemView, position);
                    }
                });
            }
        } else {
            holder.titleView.setText("none");
        }
    }

    @Override
    public int getItemCount() {
        if (results != null)
            return results.size();
        return 0;
    }

    class SearchItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;

        private SearchItemViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.search_item_textview);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
