package com.Axonix.navmain;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.services.help.Tip;

import java.util.List;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SearchResultViewHolder> {

    // 新的接口，包含两个点击方法
    public interface OnItemClickListener {
        void onItemClick(Tip tip);     // 整行点击
        void onDepartClick(Tip tip);   // “出发”按钮点击
    }

    private List<Tip> mTips;
    private OnItemClickListener mListener;

    public SearchResultsAdapter(List<Tip> tips, OnItemClickListener listener) {
        this.mTips = tips;
        this.mListener = listener;
    }

    public void updateData(List<Tip> tips) {
        this.mTips = tips;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 使用已定义的单项布局文件（例如 item_search_result.xml）
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        Tip tip = mTips.get(position);
        holder.bind(tip, mListener);
    }

    @Override
    public int getItemCount() {
        return mTips != null ? mTips.size() : 0;
    }

    static class SearchResultViewHolder extends RecyclerView.ViewHolder {
        TextView resultName;
        Button btnDepart;

        public SearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            resultName = itemView.findViewById(R.id.result_name);
            btnDepart = itemView.findViewById(R.id.btn_depart);
        }

        public void bind(final Tip tip, final OnItemClickListener listener) {
            resultName.setText(tip.getName());
            itemView.setOnClickListener(v -> listener.onItemClick(tip));
            btnDepart.setOnClickListener(v -> listener.onDepartClick(tip));
        }
    }
}
