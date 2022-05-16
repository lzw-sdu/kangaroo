package com.example.grpctest;

import android.util.Log;

import io.grpc.stub.StreamObserver;

public class HearthCallImpl extends HearthTestGrpc.HearthTestImplBase {
    private final static String Tag = "HearthCallImpl";
    private static CallBack callBack = null;
    public static void setCallBack(CallBack cb) {
        callBack = cb;
    }

    public abstract class Invoker {
        public io.grpc.stub.StreamObserver<com.example.grpctest.Grpctest.RespHearth> mObserver;
        public Invoker(io.grpc.stub.StreamObserver<com.example.grpctest.Grpctest.RespHearth> observer) {
            mObserver = observer;
        }
        abstract boolean shutdown();
    }

    public interface CallBack {
        void onReport(int transactionId, Invoker iv);
        void onNext(int transactionId, boolean living);
        void onError(int transactionId, String err);
        void onCompleted(int transactionId);
    }

    private int totalReports = 0;
    @Override
    public io.grpc.stub.StreamObserver<com.example.grpctest.Grpctest.RequHearth> report (
            final io.grpc.stub.StreamObserver<com.example.grpctest.Grpctest.RespHearth> responseObserver) {
        Log.i(Tag, "HearthCallImpl.report() ");
        totalReports = 0;
        if (callBack != null) {
            callBack.onReport(responseObserver.hashCode(), new Invoker(responseObserver) {
                @Override
                public boolean shutdown() {
                    boolean suc = true;
                    try {
                        Log.i("HearthCallImpl", "shutdown responseObserver.hashCode() " + mObserver.hashCode());
                        Grpctest.RespHearth resp = Grpctest.RespHearth.newBuilder().setCode(totalReports).build();
                        mObserver.onNext(resp);
                        mObserver.onCompleted();
                    } catch (Exception e) {
                        suc = false;
                    }
                    return suc;
                }
            });
        }
        // 此处定义的StreamObserver用于Consumer端向本端发送Request信息
        return new StreamObserver<Grpctest.RequHearth>() {
            @Override
            public void onNext(Grpctest.RequHearth value) {
                totalReports++;
                if (callBack != null) {
                    callBack.onNext(responseObserver.hashCode(), value.getLiving());
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
        };
    }
}

