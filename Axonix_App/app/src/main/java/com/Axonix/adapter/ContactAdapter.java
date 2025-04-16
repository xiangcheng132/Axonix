package com.Axonix.adapter;

import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Axonix.R;
import com.Axonix.index.model.EmergencyContact;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private final List<EmergencyContact> contacts;
    private final OnContactClickListener listener;

    // 自定义点击接口：包含拨号和删除两个回调
    public interface OnContactClickListener {
        void onDeleteClick(EmergencyContact contact);
        void onCallClick(EmergencyContact contact);
    }

    public ContactAdapter(List<EmergencyContact> contacts, OnContactClickListener listener) {
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
    public int getItemCount() {
        return contacts.size();
    }

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

        void bind(final EmergencyContact contact, final OnContactClickListener listener) {
            tvName.setText(contact.getName());
            tvPhone.setText(contact.getPhone());
            tvRelationship.setText(contact.getRelationship());

            // 记录触摸位置，用于弹窗定位
            final int[] touchPosition = new int[2];

            itemView.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    touchPosition[0] = (int) event.getRawX();
                    touchPosition[1] = (int) event.getRawY();
                }
                return false;
            });

            // 长按显示删除弹窗
            itemView.setOnLongClickListener(v -> {
                View popupView = LayoutInflater.from(itemView.getContext()).inflate(R.layout.popup_delete, null);
                PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        true
                );

                TextView tvDelete = popupView.findViewById(R.id.tv_delete);
                tvDelete.setOnClickListener(delView -> {
                    listener.onDeleteClick(contact); // 删除回调
                    popupWindow.dismiss();
                });

                popupWindow.setBackgroundDrawable(new ColorDrawable());
                popupWindow.setOutsideTouchable(true);

                int offsetY = (int) (itemView.getContext().getResources().getDisplayMetrics().density * 10);
                popupWindow.showAtLocation(itemView, Gravity.NO_GRAVITY, touchPosition[0], touchPosition[1] + offsetY);

                return true;
            });

            // 拨号按钮点击事件
            btnCall.setOnClickListener(v -> {
                listener.onCallClick(contact); // 拨号回调
            });
        }
    }
}
