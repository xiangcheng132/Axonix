package com.Axonix.socialmain;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.Axonix.socialmain.model.Post;
import com.Axonix.socialmain.model.Comment;
import java.util.List;
import java.util.ArrayList;

public class SocialEventHandler {
    private Context context;
    private FragmentManager fragmentManager;

    public SocialEventHandler(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    public void handleCreatePost() {
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

        Post post = new Post("user","我是标题","我是内容","已发布","2025-4-9 0:39",200,0);
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("user1","我是评论1","已发布","2025-4-9 0:40",10,0));
        comments.add(new Comment("user2","我是评论2","已发布","2025-4-9 0:50",10,1));

        Fragment detailFragment = new PostDetailFragment(post, comments);

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
