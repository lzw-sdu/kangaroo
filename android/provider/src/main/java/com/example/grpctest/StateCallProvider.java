package com.example.grpctest;

import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.oplus.ocs.icdf.BaseAgent;
import com.oplus.ocs.icdf.BaseAgentConsists;
import com.oplus.ocs.icdf.model.PeerAgent;

public class StateCallProvider extends BaseAgent {

    private final static String Tag = "StateCallProvider";
    private final IBinder mBinder = new LocalBinder();

    // 通过Binder获取Service实例
    public class LocalBinder extends Binder {
        public StateCallProvider getService(){
            Log.e(Tag, "getService");
            return StateCallProvider.this;
        }
    }

    public StateCallProvider() {
        super(BaseAgentConsists.CHANNEL_TYPE_GRPC);
        Log.i(Tag, "StateCallProvider");
    }

    @Override
    public IBinder onBind(Intent intent){
        Log.i(Tag, "onBind");
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(Tag, "onCreate");
    }

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
        return StateCallImpl.class.getName();
    }

    // 当Provider端收到Consumer端的建立请求后会回调此方法，允许连接则调用allowConnection()，拒绝连接则调用rejectConnection()
    @Override
    protected void onConnectionRequest(PeerAgent peerAgent) {
        Log.i(Tag, "onConnectionRequest, accept");
        allowConnection(peerAgent);
//        Intent intent = new Intent(StateCallProvider.this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
    }

    @Override
    protected void onPeerAgentDown(PeerAgent peerAgent) {
        Log.i(Tag, "onPeerAgentDown " + peerAgent.getAgentId());
    }

}
