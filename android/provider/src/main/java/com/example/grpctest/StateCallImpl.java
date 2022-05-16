package com.example.grpctest;

import android.util.Log;
import io.grpc.stub.ServerCallStreamObserver;

import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

public class StateCallImpl extends StateTestGrpc.StateTestImplBase {
    private final static String Tag = "StateCallImpl";
    private static CallBack callBack = null;
    public static void setCallBack(CallBack cb){
        callBack = cb;
    }
    public abstract class Invoker {
        public io.grpc.stub.StreamObserver<com.example.grpctest.Grpctest.RespState> mObserver;
        public Invoker(io.grpc.stub.StreamObserver<com.example.grpctest.Grpctest.RespState> observer) {
            mObserver = observer;
        }
        abstract boolean notify(int stat);
        abstract boolean shutdown();
    }
    public interface CallBack {
        void onListen(int transactionId, Invoker iv);
    }
    @Override
    public void listen(com.example.grpctest.Grpctest.RequState request,
                       final io.grpc.stub.StreamObserver<com.example.grpctest.Grpctest.RespState> responseObserver) {
        Log.i(Tag, "StateCallImpl.listen() ");
        ((ServerCallStreamObserver) responseObserver).setOnCancelHandler(new Runnable() {
            @Override
            public void run() {
                Log.i(Tag, "#### onCancelHandler, client disconnected");
            }
        });

        if (callBack != null) {
            callBack.onListen(responseObserver.hashCode(), new Invoker(responseObserver) {
                @Override
                public boolean notify(int stat) {
                    boolean suc = true;
                    try {
                        Grpctest.RespState resp = Grpctest.RespState.newBuilder().setStat(stat).build();
                        Log.i(Tag, "#### onNext stat: " + stat);
                        mObserver.onNext(resp);
                    } catch (Exception e) {
                        Log.i(Tag, "#### onNext exception: " + e);
                        suc = false;
                    }
                    return suc;
                }
                @Override
                public boolean shutdown() {
                    boolean suc = true;
                    try {
                        Log.i("StateCallImpl", "shutdown responseObserver.hashCode() " + mObserver.hashCode());
                        mObserver.onCompleted();
                        Log.i(Tag, "#### onCompleted");
                    } catch (Exception e) {
                        Log.i(Tag, "#### onCompleted exception: " + e);
                        suc = false;
                    }
                    return suc;
                }
            });
        }
    }
}
