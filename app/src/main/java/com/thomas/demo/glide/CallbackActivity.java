package com.thomas.demo.glide;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.palette.graphics.Palette;

import com.allen.library.SuperTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CallbackActivity extends AppCompatActivity {

    @BindView(R.id.tv_simple_target_title)
    SuperTextView tvSimpleTargetTitle;
    @BindView(R.id.iv_simple_target)
    AppCompatImageView ivSimpleTarget;
    @BindView(R.id.tv_my_target_title)
    SuperTextView tvMyTargetTitle;
    @BindView(R.id.iv_my_target)
    AppCompatImageView ivMyTarget;
    @BindView(R.id.tv_preload_title)
    SuperTextView tvPreloadTitle;
    @BindView(R.id.iv_preload)
    AppCompatImageView ivPreload;
    @BindView(R.id.tv_download_only_title)
    SuperTextView tvDownloadOnlyTitle;
    @BindView(R.id.iv_download_only)
    AppCompatImageView ivDownloadOnly;
    @BindView(R.id.tv_listener_title)
    SuperTextView tvListenerTitle;
    @BindView(R.id.iv_listener)
    AppCompatImageView ivListener;

    private String imagePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Glide.with(this).load(API.SIMPLE_PRELOAD).preload();


        tvSimpleTargetTitle.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
                showSimpleTarget(API.SIMPLE_TARGET);
            }
        });


        tvMyTargetTitle.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
                showMyTarget(API.SIMPLE_TARGET);
            }
        });

        tvPreloadTitle.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
                showPreload();
            }
        });
        tvDownloadOnlyTitle.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
                showDownloadOnly();
            }
        });
        tvListenerTitle.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
                showListener();
            }
        });
        ivMyTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = new Random().nextInt(5);
                switch (index) {
                    case 0:
                        showMyTarget(API.IMG_1);
                        showSimpleTarget(API.IMG_1);
                        break;
                    case 1:
                        showMyTarget(API.IMG_2);
                        showSimpleTarget(API.IMG_2);
                        break;
                    case 2:
                        showMyTarget(API.IMG_3);
                        showSimpleTarget(API.IMG_3);
                        break;
                    case 3:
                        showMyTarget(API.IMG_4);
                        showSimpleTarget(API.IMG_4);
                        break;
                    case 4:
                        showMyTarget(API.IMG_5);
                        showSimpleTarget(API.IMG_5);
                        break;
                    default:
                        showMyTarget(API.SIMPLE_TARGET);
                        showSimpleTarget(API.SIMPLE_TARGET);

                }
            }
        });


        ivDownloadOnly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(imagePath)) {
                    Toast.makeText(getApplicationContext(), "图片还没有下载完", Toast.LENGTH_LONG).show();
                } else {
                    Glide.with(CallbackActivity.this).load(new File(imagePath)).into(ivDownloadOnly);
                }
            }
        });
    }

    /**
     *返回false表示事件没有被处理，继续往下传递，返回true表示事件被处理，不再往下传递
     */
    private void showListener() {

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_image_black_24dp)
                .error(R.drawable.ic_error_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(this).load(API.SIMPLE_LISTENER).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Toast.makeText(getApplicationContext(), "图片加载失败", Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                Toast.makeText(getApplicationContext(), "图片加载成功", Toast.LENGTH_LONG).show();
                return false;
            }
        }).apply(options).into(ivListener);
    }

    private void showDownloadOnly() {
        new Thread(() -> {
            FutureTarget<File> target = Glide.with(getApplicationContext()).asFile().load(API.SIMPLE_SUBMIT).submit();
            try {
                File imageFile = target.get();
                runOnUiThread(() -> {
                    imagePath = imageFile.getPath();
                    Toast.makeText(getApplicationContext(), imageFile.getPath(), Toast.LENGTH_LONG).show();
                });
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }

    private void showPreload() {
        Glide.with(this).load(API.SIMPLE_PRELOAD).into(ivPreload);
    }


    /**
     * 展示自定义Target
     */
    private void showMyTarget(String url) {
        CustomTarget target = new CustomTarget<BitmapDrawable>() {
            @Override
            public void onResourceReady(@NonNull BitmapDrawable resource, @Nullable Transition transition) {
                Palette.from(resource.getBitmap()).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(@Nullable Palette palette) {
                        int rgb = palette.getVibrantColor(0);
                        ivMyTarget.setBackgroundColor(rgb);

                    }
                });
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        };
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_image_black_24dp)
                .error(R.drawable.ic_error_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(this).load(url).apply(options).into(target);
    }

    /**
     * 展示默认的回调
     */
    private void showSimpleTarget(String url) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_image_black_24dp)
                .error(R.drawable.ic_error_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(this).load(url).apply(options).into(ivSimpleTarget);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
