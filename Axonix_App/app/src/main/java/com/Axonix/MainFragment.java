package com.Axonix;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.Axonix.R;
import com.alibaba.android.arouter.facade.annotation.Route;
import android.util.Log;

@Route(path = "/index/main")
public class MainFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("MainFragment", "onCreateView 执行了！");
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}
