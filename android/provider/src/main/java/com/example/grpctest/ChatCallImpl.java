package com.example.grpctest;

import android.util.Log;

import io.grpc.stub.StreamObserver;

public class ChatCallImpl extends ChatTestGrpc.ChatTestImplBase {
    private final static String Tag = "ChatCallImpl";
    private static CallBack callBack = null;

    public static void setCallBack(CallBack cb) {
        callBack = cb;
    }

    public abstract class Invoker {
        public io.grpc.stub.StreamObserver<com.example.grpctest.Grpctest.RespChat> mObserver;

        public Invoker(io.grpc.stub.StreamObserver<com.example.grpctest.Grpctest.RespChat> observer) {
            mObserver = observer;
        }

        abstract boolean notify(String msg);

        abstract boolean shutdown();
    }

    public interface CallBack {
        void onChat(int transactionId, Invoker iv);

        void onNext(int transactionId, String msg);

        void onError(int transactionId, String err);

        void onCompleted(int transactionId);
    }

    @Override
    public io.grpc.stub.StreamObserver<com.example.grpctest.Grpctest.RequChat> chat(
            final io.grpc.stub.StreamObserver<com.example.grpctest.Grpctest.RespChat> responseObserver) {
        Log.i(Tag, "ChatCallImpl.chat() ");
        if (callBack != null) {
            callBack.onChat(responseObserver.hashCode(), new Invoker(responseObserver) {
                @Override
                public boolean notify(String msg) {
                    boolean suc = true;
                    try {
                        Grpctest.RespChat resp = Grpctest.RespChat.newBuilder().setMessage(msg).build();
                        mObserver.onNext(resp);
                    } catch (Exception e) {
                        suc = false;
                    }
                    return suc;
                }

                @Override
                public boolean shutdown() {
                    boolean suc = true;
                    try {
                        Log.i("ChatCallImpl", "shutdown responseObserver.hashCode() " + mObserver.hashCode());
                        mObserver.onCompleted();
                    } catch (Exception e) {
                        suc = false;
                    }
                    return suc;
                }
            });
        }
        // 此处定义的StreamObserver用于Consumer端向本端发送Request信息
        return new StreamObserver<Grpctest.RequChat>() {
            @Override
            public void onNext(Grpctest.RequChat value) {
                if (callBack != null) {
                    callBack.onNext(responseObserver.hashCode(), value.getMessage());
                }
            }

            @Override
            public void onError(Throwable t) {
                if (callBack != null) {
                    callBack.onError(responseObserver.hashCode(), "throw error");
                }
            }

            @Override
            public void onCompleted() {
                if (callBack != null) {
                    callBack.onCompleted(responseObserver.hashCode());
                }
            }
        }; //new StreamObserver<Grpctest.RequChat>
    }
}
