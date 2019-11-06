package com.thomas.demo.glide;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.allen.library.SuperTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_cache_title)
    SuperTextView tvCacheTitle;
    @BindView(R.id.tv_callback_title)
    SuperTextView tvCallbackTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvCallbackTitle.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), CallbackActivity.class);
                startActivity(intent);
            }
        });



    }

}
