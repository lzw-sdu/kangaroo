package com.sdu.kangaroo;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.32.1)",
    comments = "Source: grpctest.proto")
public final class HearthTestGrpc {

  private HearthTestGrpc() {}

  public static final String SERVICE_NAME = "HearthTest";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.sdu.kangaroo.Grpctest.RequHearth,
      com.sdu.kangaroo.Grpctest.RespHearth> getReportMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "report",
      requestType = com.sdu.kangaroo.Grpctest.RequHearth.class,
      responseType = com.sdu.kangaroo.Grpctest.RespHearth.class,
      methodType = io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
  public static io.grpc.MethodDescriptor<com.sdu.kangaroo.Grpctest.RequHearth,
      com.sdu.kangaroo.Grpctest.RespHearth> getReportMethod() {
    io.grpc.MethodDescriptor<com.sdu.kangaroo.Grpctest.RequHearth, com.sdu.kangaroo.Grpctest.RespHearth> getReportMethod;
    if ((getReportMethod = HearthTestGrpc.getReportMethod) == null) {
      synchronized (HearthTestGrpc.class) {
        if ((getReportMethod = HearthTestGrpc.getReportMethod) == null) {
          HearthTestGrpc.getReportMethod = getReportMethod =
              io.grpc.MethodDescriptor.<com.sdu.kangaroo.Grpctest.RequHearth, com.sdu.kangaroo.Grpctest.RespHearth>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "report"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.sdu.kangaroo.Grpctest.RequHearth.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.sdu.kangaroo.Grpctest.RespHearth.getDefaultInstance()))
              .setSchemaDescriptor(new HearthTestMethodDescriptorSupplier("report"))
              .build();
        }
      }
    }
    return getReportMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static HearthTestStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<HearthTestStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<HearthTestStub>() {
        @java.lang.Override
        public HearthTestStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new HearthTestStub(channel, callOptions);
        }
      };
    return HearthTestStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static HearthTestBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<HearthTestBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<HearthTestBlockingStub>() {
        @java.lang.Override
        public HearthTestBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new HearthTestBlockingStub(channel, callOptions);
        }
      };
    return HearthTestBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static HearthTestFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<HearthTestFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<HearthTestFutureStub>() {
        @java.lang.Override
        public HearthTestFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new HearthTestFutureStub(channel, callOptions);
        }
      };
    return HearthTestFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class HearthTestImplBase implements io.grpc.BindableService {

    /**
     */
    public io.grpc.stub.StreamObserver<com.sdu.kangaroo.Grpctest.RequHearth> report(
        io.grpc.stub.StreamObserver<com.sdu.kangaroo.Grpctest.RespHearth> responseObserver) {
      return asyncUnimplementedStreamingCall(getReportMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getReportMethod(),
            asyncClientStreamingCall(
              new MethodHandlers<
                com.sdu.kangaroo.Grpctest.RequHearth,
                com.sdu.kangaroo.Grpctest.RespHearth>(
                  this, METHODID_REPORT)))
          .build();
    }
  }

  /**
   */
  public static final class HearthTestStub extends io.grpc.stub.AbstractAsyncStub<HearthTestStub> {
    private HearthTestStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HearthTestStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new HearthTestStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.sdu.kangaroo.Grpctest.RequHearth> report(
        io.grpc.stub.StreamObserver<com.sdu.kangaroo.Grpctest.RespHearth> responseObserver) {
      return asyncClientStreamingCall(
          getChannel().newCall(getReportMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   */
  public static final class HearthTestBlockingStub extends io.grpc.stub.AbstractBlockingStub<HearthTestBlockingStub> {
    private HearthTestBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HearthTestBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new HearthTestBlockingStub(channel, callOptions);
    }
  }

  /**
   */
  public static final class HearthTestFutureStub extends io.grpc.stub.AbstractFutureStub<HearthTestFutureStub> {
    private HearthTestFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HearthTestFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new HearthTestFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_REPORT = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final HearthTestImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(HearthTestImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_REPORT:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.report(
              (io.grpc.stub.StreamObserver<com.sdu.kangaroo.Grpctest.RespHearth>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class HearthTestBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    HearthTestBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.sdu.kangaroo.Grpctest.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("HearthTest");
    }
  }

  private static final class HearthTestFileDescriptorSupplier
      extends HearthTestBaseDescriptorSupplier {
    HearthTestFileDescriptorSupplier() {}
  }

  private static final class HearthTestMethodDescriptorSupplier
      extends HearthTestBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    HearthTestMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (HearthTestGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new HearthTestFileDescriptorSupplier())
              .addMethod(getReportMethod())
              .build();
        }
      }
    }
    return result;
  }
}
