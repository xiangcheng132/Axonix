package com.Axonix;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.Axonix.adapter.ContactAdapter;
import com.Axonix.model.EmergencyContact;
import com.Axonix.socialmain.PostDetailFragment;
import com.alibaba.android.arouter.launcher.ARouter;
import java.util.Arrays;
import java.util.List;
import com.alibaba.android.arouter.facade.annotation.Route;


@Route(path = "/index/main")
public class MainFragment extends Fragment {

    private RecyclerView rvContacts;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);

        view.findViewById(R.id.btn_ai).setOnClickListener(v -> {
            Fragment aiFragment = new AIAssistantFragment();
            navigateToFragment(aiFragment, "ai");
        });

        view.findViewById(R.id.btn_traffic).setOnClickListener(v -> {
            Fragment trafficFragment = new TrafficRecognitionFragment();
            navigateToFragment(trafficFragment, "traffic");
        });


        // 初始化联系人列表
        rvContacts = view.findViewById(R.id.rv_contacts);
        setupContacts();
        return view;
    }

    private void setupContacts() {
        List<EmergencyContact> contacts = Arrays.asList(
                new EmergencyContact("紧急联系人A", "13800138000", "家人"),
                new EmergencyContact("紧急联系人B", "13900139000", "同事")
        );

        rvContacts.setLayoutManager(new LinearLayoutManager(getContext()));
        ContactAdapter adapter = new ContactAdapter(contacts, contact -> {
            Intent intent = new Intent(getActivity(), ContactDetailActivity.class);
            startActivity(intent);
        });
        rvContacts.setAdapter(adapter);
    }

    private void navigateToFragment(Fragment targetFragment, String tag) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
        );
        transaction.replace(com.Axonix.index.R.id.content_frame, targetFragment);
        transaction.addToBackStack(tag);
        transaction.commit();
    }
}