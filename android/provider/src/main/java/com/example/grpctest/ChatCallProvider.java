package com.example.grpctest;

import android.content.Context;
import android.util.Log;

import com.oplus.ocs.icdf.BaseAgentConsists;
import com.oplus.ocs.icdf.BaseJobAgent;
import com.oplus.ocs.icdf.RequestJobAgentCallback;
import com.oplus.ocs.icdf.model.PeerAgent;

public class ChatCallProvider extends BaseJobAgent {
    private final static String Tag = "ChatCallProvider";

    public ChatCallProvider(Context context) {
        super(context, BaseAgentConsists.CHANNEL_TYPE_GRPC);
        Log.i(Tag, "ChatCallProvider");
    }

    public static void getInstance(Context context, RequestJobAgentCallback callback) {
        BaseJobAgent.getInstance(context, ChatCallProvider.class.getName(), callback);
    }

    @Override
    public void destroy() {
        Log.i(Tag, "destroy ChatCallProvider");
        super.destroy();
    }

    @Override
    protected String onReadGrpcServiceClassName() {
        Log.i(Tag, "onReadGrpcServiceClassName");
        return ChatCallImpl.class.getName();
    }

    // 当Provider端收到Consumer端的建立请求后会回调此方法，允许连接则调用allowConnection()，拒绝连接则调用rejectConnection()
    @Override
    protected void onConnectionRequest(PeerAgent peerAgent) {
        Log.i(Tag, "onConnectionRequest, accept");
        allowConnection(peerAgent);
    }

    @Override
    protected void onPeerAgentDown(PeerAgent peerAgent) {
        Log.i(Tag, "onPeerAgentDown " + peerAgent.getAgentId());
    }
}
