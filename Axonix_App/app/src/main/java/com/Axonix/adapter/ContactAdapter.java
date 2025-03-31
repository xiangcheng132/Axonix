package com.Axonix.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.Axonix.R;
import com.Axonix.model.EmergencyContact;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private final List<EmergencyContact> contacts;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(EmergencyContact contact);
    }

    public ContactAdapter(List<EmergencyContact> contacts, OnItemClickListener listener) {
        this.contacts = contacts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(contacts.get(position), listener);
    }

    @Override
    public int getItemCount() { return contacts.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName, tvPhone, tvRelationship;
        private final ImageButton btnCall;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            tvRelationship = itemView.findViewById(R.id.tv_relationship);
            btnCall = itemView.findViewById(R.id.btn_call);
        }

        void bind(final EmergencyContact contact, final OnItemClickListener listener) {
            // 显示原始号码（可带格式）
            tvName.setText(contact.getName());
            tvPhone.setText(contact.getPhone()); // 显示号码可以带短横线
            tvRelationship.setText(contact.getRelationship());

            // 整行点击
            itemView.setOnClickListener(v -> listener.onItemClick(contact));

            // 拨号按钮点击（关键修复）
            btnCall.setOnClickListener(v -> {
                // 清洗号码：移除非数字字符
                String rawNumber = contact.getPhone().replaceAll("[^0-9]", "");

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + rawNumber)); // 使用纯数字
                itemView.getContext().startActivity(intent);
            });
        }
    }
}