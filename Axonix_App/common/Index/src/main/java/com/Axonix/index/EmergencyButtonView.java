package com.Axonix.index;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class EmergencyButtonView extends FrameLayout {

    public EmergencyButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmergencyButtonView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.emergency_button, this, true);

        Button emergencyButton = findViewById(R.id.btn_emergency);
        emergencyButton.setOnClickListener(v -> {
            Toast.makeText(context, "一键求助触发！", Toast.LENGTH_SHORT).show();

        });
    }
}