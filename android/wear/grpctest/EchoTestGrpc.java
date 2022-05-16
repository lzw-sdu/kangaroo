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
public final class EchoTestGrpc {

  private EchoTestGrpc() {}

  public static final String SERVICE_NAME = "EchoTest";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.sdu.kangaroo.Grpctest.RequEcho,
      com.sdu.kangaroo.Grpctest.RespEcho> getEchoMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "echo",
      requestType = com.sdu.kangaroo.Grpctest.RequEcho.class,
      responseType = com.sdu.kangaroo.Grpctest.RespEcho.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.sdu.kangaroo.Grpctest.RequEcho,
      com.sdu.kangaroo.Grpctest.RespEcho> getEchoMethod() {
    io.grpc.MethodDescriptor<com.sdu.kangaroo.Grpctest.RequEcho, com.sdu.kangaroo.Grpctest.RespEcho> getEchoMethod;
    if ((getEchoMethod = EchoTestGrpc.getEchoMethod) == null) {
      synchronized (EchoTestGrpc.class) {
        if ((getEchoMethod = EchoTestGrpc.getEchoMethod) == null) {
          EchoTestGrpc.getEchoMethod = getEchoMethod =
              io.grpc.MethodDescriptor.<com.sdu.kangaroo.Grpctest.RequEcho, com.sdu.kangaroo.Grpctest.RespEcho>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "echo"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.sdu.kangaroo.Grpctest.RequEcho.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.sdu.kangaroo.Grpctest.RespEcho.getDefaultInstance()))
              .setSchemaDescriptor(new EchoTestMethodDescriptorSupplier("echo"))
              .build();
        }
      }
    }
    return getEchoMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static EchoTestStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<EchoTestStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<EchoTestStub>() {
        @java.lang.Override
        public EchoTestStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new EchoTestStub(channel, callOptions);
        }
      };
    return EchoTestStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static EchoTestBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<EchoTestBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<EchoTestBlockingStub>() {
        @java.lang.Override
        public EchoTestBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new EchoTestBlockingStub(channel, callOptions);
        }
      };
    return EchoTestBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static EchoTestFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<EchoTestFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<EchoTestFutureStub>() {
        @java.lang.Override
        public EchoTestFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new EchoTestFutureStub(channel, callOptions);
        }
      };
    return EchoTestFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class EchoTestImplBase implements io.grpc.BindableService {

    /**
     */
    public void echo(com.sdu.kangaroo.Grpctest.RequEcho request,
        io.grpc.stub.StreamObserver<com.sdu.kangaroo.Grpctest.RespEcho> responseObserver) {
      asyncUnimplementedUnaryCall(getEchoMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getEchoMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.sdu.kangaroo.Grpctest.RequEcho,
                com.sdu.kangaroo.Grpctest.RespEcho>(
                  this, METHODID_ECHO)))
          .build();
    }
  }

  /**
   */
  public static final class EchoTestStub extends io.grpc.stub.AbstractAsyncStub<EchoTestStub> {
    private EchoTestStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EchoTestStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new EchoTestStub(channel, callOptions);
    }

    /**
     */
    public void echo(com.sdu.kangaroo.Grpctest.RequEcho request,
        io.grpc.stub.StreamObserver<com.sdu.kangaroo.Grpctest.RespEcho> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getEchoMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class EchoTestBlockingStub extends io.grpc.stub.AbstractBlockingStub<EchoTestBlockingStub> {
    private EchoTestBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EchoTestBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new EchoTestBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.sdu.kangaroo.Grpctest.RespEcho echo(com.sdu.kangaroo.Grpctest.RequEcho request) {
      return blockingUnaryCall(
          getChannel(), getEchoMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class EchoTestFutureStub extends io.grpc.stub.AbstractFutureStub<EchoTestFutureStub> {
    private EchoTestFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EchoTestFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new EchoTestFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.sdu.kangaroo.Grpctest.RespEcho> echo(
        com.sdu.kangaroo.Grpctest.RequEcho request) {
      return futureUnaryCall(
          getChannel().newCall(getEchoMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_ECHO = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final EchoTestImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(EchoTestImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_ECHO:
          serviceImpl.echo((com.sdu.kangaroo.Grpctest.RequEcho) request,
              (io.grpc.stub.StreamObserver<com.sdu.kangaroo.Grpctest.RespEcho>) responseObserver);
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

  private static abstract class EchoTestBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    EchoTestBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.sdu.kangaroo.Grpctest.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("EchoTest");
    }
  }

  private static final class EchoTestFileDescriptorSupplier
      extends EchoTestBaseDescriptorSupplier {
    EchoTestFileDescriptorSupplier() {}
  }

  private static final class EchoTestMethodDescriptorSupplier
      extends EchoTestBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    EchoTestMethodDescriptorSupplier(String methodName) {
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
      synchronized (EchoTestGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new EchoTestFileDescriptorSupplier())
              .addMethod(getEchoMethod())
              .build();
        }
      }
    }
    return result;
  }
}
