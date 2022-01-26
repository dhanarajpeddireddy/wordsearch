package com.dana.wordsearch.chooseImage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.dana.wordsearch.R;
import com.dana.wordsearch.databinding.ActivityChooseAvatharBinding;

import java.util.ArrayList;
import java.util.List;

public class ChooseAvatharActivity extends AppCompatActivity {
    ActivityChooseAvatharBinding binding;
    private final List<Integer> datalist=new ArrayList<>();
    ChooseAvatharAdapter avatharAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_choose_avathar);

        datalist.add(R.drawable.men);
        datalist.add(R.drawable.girl);
        datalist.add(R.drawable.girl_2);
        datalist.add(R.drawable.men_3);
        datalist.add(R.drawable.men_2);

        avatharAdapter=new ChooseAvatharAdapter(datalist);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(this, 2);

        binding.rvContent.setLayoutManager(gridLayoutManager);
        binding.rvContent.setAdapter(avatharAdapter);

        binding.ivBack.setOnClickListener(view -> finish());


    }
}