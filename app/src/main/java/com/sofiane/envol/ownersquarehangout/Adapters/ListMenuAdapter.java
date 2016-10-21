package com.sofiane.envol.ownersquarehangout.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sofiane.envol.ownersquarehangout.Activities.UpdateMenuActivity;
import com.sofiane.envol.ownersquarehangout.Entities.Menus;
import com.sofiane.envol.ownersquarehangout.R;

import java.util.ArrayList;

public class ListMenuAdapter extends RecyclerView.Adapter<ListMenuHolder> {

    private Context context;
    private ArrayList<Menus> menus;

    public ListMenuAdapter(Context context, ArrayList<Menus> menues) {
        this.context = context;
        this.menus = menues;
    }

    @Override
    public ListMenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_card, parent, false);
        ListMenuHolder holder = new ListMenuHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ListMenuHolder holder, final int position) {

        holder.nameTxt.setText(menus.get(position).getMenu_name());
        holder.descriptionTxt.setText(menus.get(position).getMenu_description());
        holder.priceTxt.setText(menus.get(position).getMenu_price() + ".");


        holder.card.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UpdateMenuActivity.class);
                intent.putExtra("position", menus.get(position).getMenu_id());
                intent.putExtra("name_menu", menus.get(position).getMenu_name());
                intent.putExtra("description_menu", menus.get(position).getMenu_description());
                intent.putExtra("price_menu", menus.get(position).getMenu_price());

                v.getContext().startActivity(intent);
            }
        });
    }

    public void refresh() {
        menus.clear();
    }


    @Override
    public int getItemCount() {
        return menus.size();
    }


}
