package com.example.grpctest;

import android.util.Log;

public class SimpleCallImpl extends EchoTestGrpc.EchoTestImplBase{
    private final static String Tag = "SimpleCallImpl";
    private static CallBack callBack = null;
    public static void setCallBack(CallBack cb){
        callBack = cb;
    }
    public interface CallBack {
        void onEcho(String value);
    }
    @Override
    public void echo(com.example.grpctest.Grpctest.RequEcho request,
                     io.grpc.stub.StreamObserver<com.example.grpctest.Grpctest.RespEcho> responseObserver) {
        Log.i(Tag, "SimpleCallImpl.echo() ");
        if(callBack != null){
            callBack.onEcho(request.getContent());
        }
        Grpctest.RespEcho resp = Grpctest.RespEcho
                                 .newBuilder()
                                 .setContent(request.getContent())
                                 .build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }
}