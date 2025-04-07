package com.Axonix.socialmain;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class SocialEventHandler {
    private Context context;
    private FragmentManager fragmentManager;

    public SocialEventHandler(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    public void handleCreatePost() {
        // 添加Fragment前先清除当前容器中的Fragment
        Fragment current = fragmentManager.findFragmentById(R.id.container);
        if (current != null) {
            fragmentManager.beginTransaction().remove(current).commitNow();
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(
                R.anim.slide_in_right,   // 进入动画
                R.anim.slide_out_left,   // 退出动画
                R.anim.slide_in_left,    // 返回进入动画
                R.anim.slide_out_right   // 返回退出动画
        );
        transaction.replace(R.id.container, new CreatePostFragment());
        transaction.addToBackStack("create_post");
        transaction.commit();
    }

    public void handlePostClick(String postId) {
        Fragment current = fragmentManager.findFragmentById(R.id.container);
        if (current != null) {
            fragmentManager.beginTransaction().remove(current).commitNow();
        }

        Fragment detailFragment = PostDetailFragment.newInstance(postId);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
        );
        transaction.replace(R.id.container, detailFragment);
        transaction.addToBackStack("post_detail");
        transaction.commit();
    }
}
