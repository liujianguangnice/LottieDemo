package com.ed.lottiedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.OnCompositionLoadedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    private final String TAG = this.getClass().getSimpleName();
    private Button button1,button2;
    private TextView tv_seek;
    LottieAnimationView animation_view_assets,animation_view_assetsTest,animation_view_assetsTestTest;
    LottieAnimationView animation_view_net_get;
    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();


    }

    private void initView() {
        animation_view_assetsTestTest =(LottieAnimationView)findViewById(R.id.animation_view_asset_getTestTest);
        animation_view_assetsTestTest.playAnimation();

        animation_view_assets =(LottieAnimationView)findViewById(R.id.animation_view_asset_get);
        animation_view_assets.setAnimation("imagess/LottieLogo1.json");
        animation_view_assets.loop(true);
        animation_view_assets.buildDrawingCache();          //强制缓存绘制数据

        animation_view_assets.playAnimation();
        animation_view_assets.addAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                tv_seek.setText(" 动画进度" +(int) (animation.getAnimatedFraction()*100) +"%");

                if((int) (animation.getAnimatedFraction()*100)==80){
                    image = animation_view_assets.getDrawingCache(); //获取当前绘制数据
                    if (image != null) {
                    }
                }
            }
        });


        tv_seek=(TextView)findViewById(R.id.tv_seek);

        button1=(Button)findViewById(R.id.button1);
        button2=(Button)findViewById(R.id.button2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startAnima();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stopAnima();
            }
        });


        //网络图加载
        animation_view_net_get=(LottieAnimationView)findViewById(R.id.animation_view_net_get);
        loadUrl("https://assets6.lottiefiles.com/packages/lf20_IJ8lYj.json");


    }



    /*
     * 开始动画
     */
    private  void startAnima(){

        boolean inPlaying = animation_view_assets.isAnimating();
        if (!inPlaying) {
            animation_view_assets.setProgress(0);
            animation_view_assets.playAnimation();
        }
    }
    /*
     * 停止动画
     */
    private  void stopAnima(){
        boolean inPlaying = animation_view_assets.isAnimating();
        if (inPlaying) {
            animation_view_assets.pauseAnimation();
        }
    }




    private void loadUrl(String url) {
        Request request = new Request.Builder().url(url).build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: ");
            }
            @Override public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject json = new JSONObject(response.body().string());
                    LottieComposition.Factory
                            .fromJsonString(json.toString(), new OnCompositionLoadedListener() {
                                @Override
                                public void onCompositionLoaded(LottieComposition composition) {
                                    animation_view_net_get.setComposition(composition);
                                    animation_view_net_get.playAnimation();
                                    animation_view_net_get.loop(true);
                                }
                            });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        animation_view_assets.cancelAnimation();
    }
}
