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
public final class ChatTestGrpc {

  private ChatTestGrpc() {}

  public static final String SERVICE_NAME = "ChatTest";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.sdu.kangaroo.Grpctest.RequChat,
      com.sdu.kangaroo.Grpctest.RespChat> getChatMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "chat",
      requestType = com.sdu.kangaroo.Grpctest.RequChat.class,
      responseType = com.sdu.kangaroo.Grpctest.RespChat.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<com.sdu.kangaroo.Grpctest.RequChat,
      com.sdu.kangaroo.Grpctest.RespChat> getChatMethod() {
    io.grpc.MethodDescriptor<com.sdu.kangaroo.Grpctest.RequChat, com.sdu.kangaroo.Grpctest.RespChat> getChatMethod;
    if ((getChatMethod = ChatTestGrpc.getChatMethod) == null) {
      synchronized (ChatTestGrpc.class) {
        if ((getChatMethod = ChatTestGrpc.getChatMethod) == null) {
          ChatTestGrpc.getChatMethod = getChatMethod =
              io.grpc.MethodDescriptor.<com.sdu.kangaroo.Grpctest.RequChat, com.sdu.kangaroo.Grpctest.RespChat>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "chat"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.sdu.kangaroo.Grpctest.RequChat.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.sdu.kangaroo.Grpctest.RespChat.getDefaultInstance()))
              .setSchemaDescriptor(new ChatTestMethodDescriptorSupplier("chat"))
              .build();
        }
      }
    }
    return getChatMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ChatTestStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ChatTestStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ChatTestStub>() {
        @java.lang.Override
        public ChatTestStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ChatTestStub(channel, callOptions);
        }
      };
    return ChatTestStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ChatTestBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ChatTestBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ChatTestBlockingStub>() {
        @java.lang.Override
        public ChatTestBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ChatTestBlockingStub(channel, callOptions);
        }
      };
    return ChatTestBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ChatTestFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ChatTestFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ChatTestFutureStub>() {
        @java.lang.Override
        public ChatTestFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ChatTestFutureStub(channel, callOptions);
        }
      };
    return ChatTestFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class ChatTestImplBase implements io.grpc.BindableService {

    /**
     */
    public io.grpc.stub.StreamObserver<com.sdu.kangaroo.Grpctest.RequChat> chat(
        io.grpc.stub.StreamObserver<com.sdu.kangaroo.Grpctest.RespChat> responseObserver) {
      return asyncUnimplementedStreamingCall(getChatMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getChatMethod(),
            asyncBidiStreamingCall(
              new MethodHandlers<
                com.sdu.kangaroo.Grpctest.RequChat,
                com.sdu.kangaroo.Grpctest.RespChat>(
                  this, METHODID_CHAT)))
          .build();
    }
  }

  /**
   */
  public static final class ChatTestStub extends io.grpc.stub.AbstractAsyncStub<ChatTestStub> {
    private ChatTestStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ChatTestStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ChatTestStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.sdu.kangaroo.Grpctest.RequChat> chat(
        io.grpc.stub.StreamObserver<com.sdu.kangaroo.Grpctest.RespChat> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(getChatMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   */
  public static final class ChatTestBlockingStub extends io.grpc.stub.AbstractBlockingStub<ChatTestBlockingStub> {
    private ChatTestBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ChatTestBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ChatTestBlockingStub(channel, callOptions);
    }
  }

  /**
   */
  public static final class ChatTestFutureStub extends io.grpc.stub.AbstractFutureStub<ChatTestFutureStub> {
    private ChatTestFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ChatTestFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ChatTestFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_CHAT = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ChatTestImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ChatTestImplBase serviceImpl, int methodId) {
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
        case METHODID_CHAT:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.chat(
              (io.grpc.stub.StreamObserver<com.sdu.kangaroo.Grpctest.RespChat>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class ChatTestBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ChatTestBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.sdu.kangaroo.Grpctest.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ChatTest");
    }
  }

  private static final class ChatTestFileDescriptorSupplier
      extends ChatTestBaseDescriptorSupplier {
    ChatTestFileDescriptorSupplier() {}
  }

  private static final class ChatTestMethodDescriptorSupplier
      extends ChatTestBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ChatTestMethodDescriptorSupplier(String methodName) {
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
      synchronized (ChatTestGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ChatTestFileDescriptorSupplier())
              .addMethod(getChatMethod())
              .build();
        }
      }
    }
    return result;
  }
}
