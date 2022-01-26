package com.dana.wordsearch.chooseImage;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.dana.wordsearch.Constants;
import com.dana.wordsearch.Preferences;
import com.dana.wordsearch.R;
import com.dana.wordsearch.databinding.ItemAvathatBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class ChooseAvatharAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final List<Integer> DataListMap;
    private Context activity;


    public ChooseAvatharAdapter(List<Integer> DataListMap) {

        this.DataListMap=DataListMap;


    }


    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {

        this.activity = parent.getContext();

        ItemAvathatBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_avathat, parent, false);
            return new ViewHolder(binding);

    }

    @SuppressLint({"UseCompatLoadingForDrawables", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NotNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            Integer bean= DataListMap.get(position);

            viewHolder.itemAvathatBinding.ivImage.setImageResource(bean);

            if (bean==Preferences.getInstance(activity).getIntPref(Constants.AVATHAR_ID,R.drawable.men))
            {
                viewHolder.itemAvathatBinding.checkbox.setVisibility(View.VISIBLE);
            }else
            {
                viewHolder.itemAvathatBinding.checkbox.setVisibility(View.GONE);
            }

            viewHolder.itemAvathatBinding.getRoot().setOnClickListener(view
                    -> {
                Preferences.getInstance(activity).setIntPref(Constants.AVATHAR_ID,bean);
                notifyDataSetChanged();
            });

        }
        }




    @Override
    public int getItemCount() {
        return DataListMap.size();
    }



    static class ViewHolder extends RecyclerView.ViewHolder {

        ItemAvathatBinding itemAvathatBinding;

        ViewHolder(@NonNull  ItemAvathatBinding itemAvathatBinding) {
            super(itemAvathatBinding.getRoot());

         this.itemAvathatBinding=itemAvathatBinding;

        }
    }


}
