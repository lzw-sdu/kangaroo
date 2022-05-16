package com.example.grpctest;

import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.oplus.ocs.icdf.BaseAgent;
import com.oplus.ocs.icdf.BaseAgentConsists;
import com.oplus.ocs.icdf.model.PeerAgent;

public class HearthCallProvider extends BaseAgent {

    private final static String Tag = "HearthCallProvider";
    private final IBinder mBinder = new LocalBinder();

    // 通过Binder获取Service实例
    public class LocalBinder extends Binder {
        public HearthCallProvider getService(){
            Log.i(Tag, "getService");
            return HearthCallProvider.this;
        }
    }

    public HearthCallProvider() {
        super(BaseAgentConsists.CHANNEL_TYPE_GRPC);
        Log.i(Tag, "HearthCallProvider");
    }

    @Override
    public IBinder onBind(Intent intent){
        Log.e(Tag, "onBind");
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(Tag, "onCreate");
    }

    // 服务生命周期结束后自动调用此方法释放资源
    @Override
    public void onDestroy() {
        Log.e(Tag, "onDestroy");
        if (Build.VERSION.SDK_INT >= 26) {
            stopForeground(true);
        }
        super.onDestroy();
    }

    @Override
    protected String onReadGrpcServiceClassName() {
        Log.i(Tag, "onReadGrpcServiceClassName");
        return HearthCallImpl.class.getName();
    }

    // 当Provider端收到Consumer端的建立请求后会回调此方法，允许连接则调用allowConnection()，拒绝连接则调用rejectConnection()
    @Override
    protected void onConnectionRequest(PeerAgent peerAgent) {
        Log.i(Tag, "onConnectionRequest, accept");
        allowConnection(peerAgent);
//        Intent intent = new Intent(HearthCallProvider.this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
    }

    @Override
    protected void onPeerAgentDown(PeerAgent peerAgent) {
        Log.i(Tag, "onPeerAgentDown " + peerAgent.getAgentId());
    }

}
