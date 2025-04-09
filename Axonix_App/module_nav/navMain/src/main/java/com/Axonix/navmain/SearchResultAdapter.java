package com.Axonix.navmain;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.services.help.Tip;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private List<Tip> tips;
    private OnItemClickListener onItemClickListener;

    public SearchResultAdapter(List<Tip> tips, OnItemClickListener onItemClickListener) {
        this.tips = tips;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 通过 LayoutInflater 加载单个条目布局
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tip tip = tips.get(position);
        holder.nameTextView.setText(tip.getName());
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(tip));
    }

    @Override
    public int getItemCount() {
        return tips != null ? tips.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.result_name);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Tip tip);
    }
}
