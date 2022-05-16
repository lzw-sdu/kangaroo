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
public final class StateTestGrpc {

  private StateTestGrpc() {}

  public static final String SERVICE_NAME = "StateTest";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.sdu.kangaroo.Grpctest.RequState,
      com.sdu.kangaroo.Grpctest.RespState> getListenMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "listen",
      requestType = com.sdu.kangaroo.Grpctest.RequState.class,
      responseType = com.sdu.kangaroo.Grpctest.RespState.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<com.sdu.kangaroo.Grpctest.RequState,
      com.sdu.kangaroo.Grpctest.RespState> getListenMethod() {
    io.grpc.MethodDescriptor<com.sdu.kangaroo.Grpctest.RequState, com.sdu.kangaroo.Grpctest.RespState> getListenMethod;
    if ((getListenMethod = StateTestGrpc.getListenMethod) == null) {
      synchronized (StateTestGrpc.class) {
        if ((getListenMethod = StateTestGrpc.getListenMethod) == null) {
          StateTestGrpc.getListenMethod = getListenMethod =
              io.grpc.MethodDescriptor.<com.sdu.kangaroo.Grpctest.RequState, com.sdu.kangaroo.Grpctest.RespState>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "listen"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.sdu.kangaroo.Grpctest.RequState.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.sdu.kangaroo.Grpctest.RespState.getDefaultInstance()))
              .setSchemaDescriptor(new StateTestMethodDescriptorSupplier("listen"))
              .build();
        }
      }
    }
    return getListenMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static StateTestStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<StateTestStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<StateTestStub>() {
        @java.lang.Override
        public StateTestStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new StateTestStub(channel, callOptions);
        }
      };
    return StateTestStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static StateTestBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<StateTestBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<StateTestBlockingStub>() {
        @java.lang.Override
        public StateTestBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new StateTestBlockingStub(channel, callOptions);
        }
      };
    return StateTestBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static StateTestFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<StateTestFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<StateTestFutureStub>() {
        @java.lang.Override
        public StateTestFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new StateTestFutureStub(channel, callOptions);
        }
      };
    return StateTestFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class StateTestImplBase implements io.grpc.BindableService {

    /**
     */
    public void listen(com.sdu.kangaroo.Grpctest.RequState request,
        io.grpc.stub.StreamObserver<com.sdu.kangaroo.Grpctest.RespState> responseObserver) {
      asyncUnimplementedUnaryCall(getListenMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getListenMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                com.sdu.kangaroo.Grpctest.RequState,
                com.sdu.kangaroo.Grpctest.RespState>(
                  this, METHODID_LISTEN)))
          .build();
    }
  }

  /**
   */
  public static final class StateTestStub extends io.grpc.stub.AbstractAsyncStub<StateTestStub> {
    private StateTestStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StateTestStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new StateTestStub(channel, callOptions);
    }

    /**
     */
    public void listen(com.sdu.kangaroo.Grpctest.RequState request,
        io.grpc.stub.StreamObserver<com.sdu.kangaroo.Grpctest.RespState> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getListenMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class StateTestBlockingStub extends io.grpc.stub.AbstractBlockingStub<StateTestBlockingStub> {
    private StateTestBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StateTestBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new StateTestBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<com.sdu.kangaroo.Grpctest.RespState> listen(
        com.sdu.kangaroo.Grpctest.RequState request) {
      return blockingServerStreamingCall(
          getChannel(), getListenMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class StateTestFutureStub extends io.grpc.stub.AbstractFutureStub<StateTestFutureStub> {
    private StateTestFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StateTestFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new StateTestFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_LISTEN = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final StateTestImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(StateTestImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_LISTEN:
          serviceImpl.listen((com.sdu.kangaroo.Grpctest.RequState) request,
              (io.grpc.stub.StreamObserver<com.sdu.kangaroo.Grpctest.RespState>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class StateTestBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    StateTestBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.sdu.kangaroo.Grpctest.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("StateTest");
    }
  }

  private static final class StateTestFileDescriptorSupplier
      extends StateTestBaseDescriptorSupplier {
    StateTestFileDescriptorSupplier() {}
  }

  private static final class StateTestMethodDescriptorSupplier
      extends StateTestBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    StateTestMethodDescriptorSupplier(String methodName) {
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
      synchronized (StateTestGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new StateTestFileDescriptorSupplier())
              .addMethod(getListenMethod())
              .build();
        }
      }
    }
    return result;
  }
}
