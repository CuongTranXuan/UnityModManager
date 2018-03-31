package io.github.xausky.unitymodmanager;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.flurry.android.FlurryAgent;
import com.lody.virtual.client.core.VirtualCore;

import org.json.JSONObject;

import java.io.InputStream;

import io.github.xausky.unitymodmanager.adapter.VisibilityAdapter;
import io.github.xausky.unitymodmanager.fragment.BaseFragment;
import io.github.xausky.unitymodmanager.utils.ModUtils;

/**
 * Created by xausky on 2018/2/1.
 */

public class MainApplication extends Application {
    public static final String LOG_TAG = "UnityModManager";
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            VirtualCore.get().startup(base);
            SharedPreferences preferences = base.getSharedPreferences("default", MODE_PRIVATE);
            SharedPreferences visibilityPreferences = base.getSharedPreferences(VisibilityAdapter.VISIBILITY_SHARED_PREFERENCES_KEY, MODE_PRIVATE);
            if(preferences.getBoolean("first", true)){
                //微信和支付宝默认可见（用于氪金）
                visibilityPreferences.edit()
                        .putBoolean("com.eg.android.AlipayGphone", true)
                        .putBoolean("com.tencent.mm", true).apply();
                preferences.edit().putBoolean("first", false).apply();
            }
            BaseFragment.initialize(base);
            InputStream mapInputStream = base.getAssets().open("map.json");
            byte[] bytes = new byte[mapInputStream.available()];
            if(mapInputStream.read(bytes) == -1){
                throw new Exception("map.json read failed.");
            }
            String json = new String(bytes);
            ModUtils.map = new JSONObject(json);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(this, "YS94RJ9WV9NGSFHT8ZJM");
        //这个是我的Flurry的移动分析API Key，如果你fork了我的项目并且准备自己发布请务必修改这个。
    }
}
