package com.project.base.app.tz.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.project.base.app.tz.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SecondListItemAdapter extends RecyclerView.Adapter<SecondListItemAdapter.SecondListViewHolder> {

    private Context context;

    public void setmList(ArrayList<String> mList) {
        this.mList = mList;
    }

    ArrayList<String> mList;


    public SecondListItemAdapter(Context context, ArrayList<String> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public SecondListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SecondListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_second_adapter, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SecondListViewHolder holder, int position) {
        holder.itemSecondTv.setText(position+"");
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class SecondListViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item_second_tv)
        TextView itemSecondTv;

        public SecondListViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
