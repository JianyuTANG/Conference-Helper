package com.example.myapplication.detail.paper_comment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;


public class CommentListAdapter
        extends RecyclerView.Adapter<CommentListAdapter.CommentViewHolder>{
    private final LayoutInflater mInflater;
    private List<Comment> comments; // Cached copy of meetings

    public CommentListAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    public Comment getCommentAtPosition (int position) {
        return comments.get(position);
    }

    @NonNull
    @Override
    public CommentListAdapter.CommentViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.component_comment_item,
                parent, false);
        return new CommentListAdapter.CommentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(
            @NonNull CommentListAdapter.CommentViewHolder holder, int position) {
        if (comments != null && comments.size() > 0) {
            Comment current = comments.get(position);
            holder.nameView.setText(current.getName());
            holder.contentView.setText(current.getContent());
        } else {
            // Covers the case of data not being ready yet.
            holder.nameView.setText("no meeting");
            holder.contentView.setText("None");
        }
    }

    @Override
    public int getItemCount() {
        if (comments != null)
            return comments.size();
        else return 0;
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameView;
        private final TextView contentView;

        private CommentViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.comment_item_name);
            contentView = itemView.findViewById(R.id.comment_item_content);
        }
    }
}
