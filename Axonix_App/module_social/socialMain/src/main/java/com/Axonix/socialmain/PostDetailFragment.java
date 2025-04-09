package com.Axonix.socialmain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.Axonix.socialmain.R;
import com.Axonix.socialmain.model.Comment;
import com.Axonix.socialmain.model.Post;
import com.google.android.material.appbar.MaterialToolbar;


import java.util.List;

public class PostDetailFragment extends Fragment {

    private Post post;
    private List<Comment> comments;
    private MaterialToolbar btnBack;

    public PostDetailFragment(Post post, List<Comment> comments) {
        this.post = post;
        this.comments = comments;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);

        bindPostData(view);
        addComments(view, comments);
        FragmentManager fragmentManager = getParentFragmentManager();
        btnBack = view.findViewById(R.id.btn_back);
        btnBack.setNavigationOnClickListener(v -> {
            fragmentManager.popBackStack();
        });
        return view;
    }

    private void bindPostData(View view) {
        ((TextView) view.findViewById(R.id.tv_username)).setText(post.getUsername());
        ((TextView) view.findViewById(R.id.tv_title)).setText(post.getTitle());
        ((TextView) view.findViewById(R.id.tv_content)).setText(post.getContent());
        ((TextView) view.findViewById(R.id.tv_status)).setText("状态：" + post.getStatus());
        ((TextView) view.findViewById(R.id.tv_publish_time)).setText("发布时间：" + post.getPublishTime());
        ((TextView) view.findViewById(R.id.tv_likes)).setText("👍 " + post.getLikes());
        ((TextView) view.findViewById(R.id.tv_dislikes)).setText("👎 " + post.getDislikes());
    }

    private void addComments(View view, List<Comment> commentList) {
        LinearLayout commentContainer = view.findViewById(R.id.comment_container);
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        commentContainer.removeAllViews();

        for (Comment comment : commentList) {
            View itemView = inflater.inflate(R.layout.item_comment, commentContainer, false);

            ((TextView) itemView.findViewById(R.id.tv_comment_username)).setText(comment.getUsername());
            ((TextView) itemView.findViewById(R.id.tv_comment_time)).setText(comment.getTime());
            ((TextView) itemView.findViewById(R.id.tv_comment_status)).setText("状态：" + comment.getStatus());
            ((TextView) itemView.findViewById(R.id.tv_comment_content)).setText(comment.getContent());
            ((TextView) itemView.findViewById(R.id.tv_comment_likes)).setText("👍 " + comment.getLikes());
            ((TextView) itemView.findViewById(R.id.tv_comment_dislikes)).setText("👎 " + comment.getDislikes());

            commentContainer.addView(itemView);
        }
    }
}
