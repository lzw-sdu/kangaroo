package com.faceunity.app_ptag;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import com.faceunity.app_ptag.util.ToastUtils;
import com.faceunity.editor_ptag.data_center.FuDevDataCenter;
import com.faceunity.editor_ptag.util.KotlinExpandKt;
import com.faceunity.fupta.cloud_download.entity.CloudConfig;

public class FuEditActivity extends AppCompatActivity {

    /**
     * 打开 FuEditActivity
     *
     * @param context Activity context or Fragment context
     */
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, FuEditActivity.class);
        context.startActivity(intent);
    }

    /**
     * 打开 FuEditActivity
     *
     * @param context    Activity context or Fragment context
     * @param fragmentId 仅支持 R.navigation.fu_nav_graph 里的 fragment id
     */
    public static void startActivity(Context context, @IdRes int fragmentId) {
        Intent intent = new Intent(context, FuEditActivity.class);
        intent.putExtra("fragmentId", fragmentId);
        context.startActivity(intent);
    }

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fu_edit_activity);
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        KotlinExpandKt.tintBarColor(this, KotlinExpandKt.getColorCompat(this, R.color.scene_bg));

        Intent intent = getIntent();
        if (intent != null) {
            int id = intent.getIntExtra("fragmentId", -1);
            if (id != -1) {
                getNavController().navigate(id);
            }
        }
        checkInitStatus();
    }

    private void checkInitStatus() {
        CloudConfig cloudConfig = FuDevDataCenter.repository.getCloudConfig();
        if (FuDevInitializeWrapper.INSTANCE.getAuthPack().equals("联系相芯相关人员获取".getBytes())
                || cloudConfig.getApiKey().equals("联系相芯相关人员获取")
                || cloudConfig.getApiSecret().equals("联系相芯相关人员获取")
                || cloudConfig.getProjectId().equals("联系相芯相关人员获取")
        ) {
            ToastUtils.INSTANCE.showFailureToast(this, "请联系相芯相关人员获取测试证书");
        }
    }

    public NavController getNavController() {
        return navController;
    }
}