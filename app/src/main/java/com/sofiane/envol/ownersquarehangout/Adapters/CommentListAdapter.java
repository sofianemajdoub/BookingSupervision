package com.sofiane.envol.ownersquarehangout.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Query;
import com.sofiane.envol.ownersquarehangout.Entities.Comments;
import com.sofiane.envol.ownersquarehangout.R;
import com.squareup.picasso.Picasso;

public class CommentListAdapter extends FirebaseListAdapter<Comments> {

    private Context mContext;

    public CommentListAdapter(Query ref, Activity activity, int layout, Context context) {
        super(ref, Comments.class, layout, activity);
        mContext = context;
    }

    @Override
    protected void populateView(View view, final Comments m) {
        String img_user = m.getImg_user();
        ImageView img_profil = (ImageView) view.findViewById(R.id.pic_comment);
        Picasso.with(mContext).load(img_user).into(img_profil);

        final String author = m.getName_user();
        TextView authorText = (TextView) view.findViewById(R.id.name_user);
        authorText.setText(author);

        String descrption = m.getDescription();
        TextView descText = (TextView) view.findViewById(R.id.description);
        descText.setText(descrption);

        String date = m.getDate();
        TextView dateText = (TextView) view.findViewById(R.id.date);
        dateText.setText(date);

        String like = m.getLike();
        TextView likeText = (TextView) view.findViewById(R.id.nbr_like);
        likeText.setText(like);

    }

}
