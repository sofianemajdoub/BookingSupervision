package com.sofiane.envol.ownersquarehangout.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sofiane.envol.ownersquarehangout.R;


public class ListMenuHolder extends RecyclerView.ViewHolder {

    TextView nameTxt;
    TextView descriptionTxt;
    TextView priceTxt;
    CardView card;

    public ListMenuHolder(View itemView) {
        super(itemView);

        nameTxt = (TextView) itemView.findViewById(R.id.nameTxt);
        descriptionTxt = (TextView) itemView.findViewById(R.id.descriptionTxt);
        priceTxt = (TextView) itemView.findViewById(R.id.priceTxt);
        card = (CardView) itemView.findViewById(R.id.cardRestau);

    }
}
