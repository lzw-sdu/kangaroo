package com.sdu.kangaroo;

import android.content.Context;
import android.util.Log;

import com.oplus.ocs.icdf.BaseAgentConsists;
import com.oplus.ocs.icdf.BaseJobAgent;
import com.oplus.ocs.icdf.RequestJobAgentCallback;
import com.oplus.ocs.icdf.model.PeerAgent;

import io.grpc.Channel;

public class ChatCallConsumer extends BaseJobAgent {

    private Channel mChannel = null;
    private PeerAgent mPeerAgent = null;
    private final static String Tag = "ChatCallConsumer";

    public ChatCallConsumer(Context context) {
        super(context, BaseAgentConsists.CHANNEL_TYPE_GRPC);
        Log.d(Tag, "ChatCallConsumer");
        // 配置RPC的属性（可选），只需要在Consumer端配置
        configRpcMethod("ChatTest/chat", BaseAgentConsists.ICDF_CHANNEL_BYTES, 104, false);
        // 查找可用的远端服务
        findPeerAgent();
    }

    public static void getInstance(Context context, RequestJobAgentCallback callback) {
        BaseJobAgent.getInstance(context, ChatCallConsumer.class.getName(), callback);
    }

    @Override
    public void destroy() {
        Log.d(Tag, "destroy ChatCallConsumer");
        super.destroy();
    }

    public Channel getChannel() {
        return mChannel;
    }

    @Override
    protected void onFindPeerAgentResponse(int result, PeerAgent[] peerAgents) {
        Log.i(Tag, "onFindPeerAgentResponse, result = " + result);
        if (peerAgents == null || peerAgents.length < 1) {
            Log.e(Tag, "peerAgents == null || peerAgents.length < 1");
            return;
        }
        if (result != BaseAgentConsists.FIND_PEERAGENT_SUCESS) {
            Log.e(Tag, "onFindPeerAgentsResponse result == " + result);
            return;
        }
        // 查找成功，需要从PeerAgent列表中过滤出想要连接的对端，此处是要求Provider端应用包名与本应用包名一致
        for (PeerAgent peerAgent : peerAgents) {
            if (peerAgent.getAppName().equals(getApplicationContext().getPackageName())) {
                Log.i(Tag, "peerAgents == " + peerAgent.toString());
                createGrpcChannel(peerAgent, BaseAgentConsists.PROTOCOL_DEFAULT);
                break;
            }
        }
    }

    // 用于获取createGrpcChannel的连接结果，建立成功后返回的channel用于后续通信
    @Override
    protected void onCreateGrpcChannelResponse(int ret, PeerAgent peerAgent, Channel channel) {
        Log.i(Tag, "onCreateGrpcChannelResponse, result = " + ret);
        if (ret != BaseAgentConsists.CREATE_CHANNEL_SUCCESS) {
            Log.e(Tag, "onCreateGrpcChannelResponse result == " + ret);
            return;
        }
        mChannel = channel;
        mPeerAgent = peerAgent;
    }

    @Override
    protected void onPeerAgentDown(PeerAgent peerAgent) {
        Log.i(Tag, "onPeerAgentDown " + peerAgent.getAgentId());
    }

    public void disconnect() {
        if (mChannel == null) {
            Log.e(Tag, "disconnect failed, Channel is null");
            return;
        }
        destroyGrpcChannel(mPeerAgent, mChannel);
        mChannel = null;
        mPeerAgent = null;
    }

}
