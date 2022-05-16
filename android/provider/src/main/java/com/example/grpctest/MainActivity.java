package com.example.grpctest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.oplus.ocs.icdf.BaseJobAgent;
import com.oplus.ocs.icdf.RequestJobAgentCallback;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public class MainActivity extends Activity {
    private final static String Tag = "MainActivity";

    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainHandler = new Handler(Looper.getMainLooper());

        bindForEcho();
        bindForState();
        bindForHearth();
        bindForChat();
    }

    private SimpleCallProvider echoProvider = null;
    private final ServiceConnection echoConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service){
            Log.i(Tag, "onServiceConnected : " + className);
            SimpleCallProvider.LocalBinder binder = (SimpleCallProvider.LocalBinder)service;
            if (null != binder) {
                echoProvider = binder.getService();
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName className){
            Log.i(Tag, "onServiceDisconnected : " + className);
        }
    };
    private void bindForEcho(){
        SimpleCallImpl.setCallBack(new SimpleCallImpl.CallBack(){
            @Override
            public void onEcho(final String value){
                if(mainHandler != null){
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            final TextView showView = (TextView) MainActivity.this.findViewById(R.id.text_show_echo);
                            showView.setText("request : " + value);
                        }
                    });
                }
            }
        });
        findViewById(R.id.btn_provider_simple).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (null == echoProvider) {
                    Intent service = new Intent(getApplicationContext(), SimpleCallProvider.class);
                    MainActivity.this.bindService(service, echoConnection, Context.BIND_AUTO_CREATE);
                    final TextView listView = (TextView) MainActivity.this.findViewById(R.id.contentList);
                    listView.setText("SimpleCallProvider start...!\n");
                } else {
                    echoProvider = null;
                    MainActivity.this.unbindService(echoConnection);
                    final TextView listView = (TextView) MainActivity.this.findViewById(R.id.contentList);
                    listView.setText("SimpleCallProvider close...!\n");
                }
            }
        });
    }

    private StateCallProvider stateProvider = null;
    private final ServiceConnection stateConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service){
            Log.i(Tag, "onServiceConnected : " + className);
            StateCallProvider.LocalBinder binder = (StateCallProvider.LocalBinder)service;
            if (null != binder) {
                stateProvider = binder.getService();
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName className){
            Log.i(Tag, "onServiceDisconnected : " + className);
        }
    };
    private Map<Integer, StateCallImpl.Invoker> stateInvokerMap = new ConcurrentHashMap<>();
    int statValue = -1;
    private void bindForState(){
        final Button notifyBtn = (Button) MainActivity.this.findViewById(R.id.btn_notify_state);
        StateCallImpl.setCallBack(new StateCallImpl.CallBack() {
            @Override
            public void onListen(final int transactionId, final StateCallImpl.Invoker iv) {
                if(mainHandler != null){
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            stateInvokerMap.put(transactionId, iv);
                            statValue = 0;
                            notifyBtn.setText("state = " + statValue);
                            iv.notify(statValue);
                        }
                    });
                }
            } //onListen
        });
        findViewById(R.id.btn_provider_server_stream).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (null == stateProvider) {
                    Intent service = new Intent(getApplicationContext(), StateCallProvider.class);
                    MainActivity.this.bindService(service, stateConnection, Context.BIND_AUTO_CREATE);
                    final TextView listView = (TextView) MainActivity.this.findViewById(R.id.contentList);
                    listView.setText("StateCallProvider start...!\n");
                } else {
                    stateProvider = null;
                    MainActivity.this.unbindService(stateConnection);
                    final TextView listView = (TextView) MainActivity.this.findViewById(R.id.contentList);
                    listView.setText("StateCallProvider close...!\n");
                }
            }
        });
        findViewById(R.id.btn_notify_state).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if ((statValue >= 0) && (stateInvokerMap.size() > 0)) {
                    statValue++;
                    stateInvokerMap.forEach(new BiConsumer<Integer, StateCallImpl.Invoker>() {
                        @Override
                        public void accept(Integer integer, StateCallImpl.Invoker invoker) {
                            invoker.notify(statValue);
                        }
                    });
                }
                notifyBtn.setText("state = " + statValue);
            }
        });
        findViewById(R.id.btn_shutdown_state).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                stateInvokerMap.forEach(new BiConsumer<Integer, StateCallImpl.Invoker>() {
                    @Override
                    public void accept(Integer integer, StateCallImpl.Invoker invoker) {
                        invoker.shutdown();
                    }
                });
                stateInvokerMap.clear();
                notifyBtn.setText("未连接：本端关闭");
            }
        });
    }

    private HearthCallProvider hearthProvider = null;
    private final ServiceConnection hearthConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service){
            Log.i(Tag, "onServiceConnected : " + className);
            HearthCallProvider.LocalBinder binder = (HearthCallProvider.LocalBinder)service;
            if (null != binder) {
                hearthProvider = binder.getService();
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName className){
            Log.i(Tag, "onServiceDisconnected : " + className);
        }
    };
    private Map<Integer, HearthCallImpl.Invoker> hearthInvokerMap = new ConcurrentHashMap<>();
    private void bindForHearth() {
        final TextView showView = (TextView) MainActivity.this.findViewById(R.id.text_show_hearth);
        HearthCallImpl.setCallBack(new HearthCallImpl.CallBack() {
            @Override
            public void onReport(final int transactionId, final HearthCallImpl.Invoker iv) {
                if (mainHandler != null) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            hearthInvokerMap.put(transactionId, iv);
                            showView.setText("未请求：已接入");
                        }
                    });
                }
            }

            @Override
            public void onNext(int transactionId, final boolean living) {
                if (mainHandler != null) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (living) {
                                showView.setText("已活");
                            } else {
                                showView.setText("已死");
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(final int transactionId, String err) {
                if(mainHandler != null){
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            hearthInvokerMap.remove(transactionId);
                            showView.setText("未请求：对端异常");
                        }
                    });
                }
            }

            @Override
            public void onCompleted(final int transactionId) {
                if(mainHandler != null){
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            hearthInvokerMap.get(transactionId).shutdown();
                            hearthInvokerMap.remove(transactionId);
                            showView.setText("未请求：对端关闭");
                        }
                    });
                }
            }
        });
        findViewById(R.id.btn_provider_client_stream).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (null == hearthProvider) {
                    Intent service = new Intent(getApplicationContext(), HearthCallProvider.class);
                    MainActivity.this.bindService(service, hearthConnection, Context.BIND_AUTO_CREATE);
                    final TextView listView = (TextView) MainActivity.this.findViewById(R.id.contentList);
                    listView.setText("HearthCallProvider start...!\n");
                } else {
                    hearthProvider = null;
                    MainActivity.this.unbindService(hearthConnection);
                    final TextView listView = (TextView) MainActivity.this.findViewById(R.id.contentList);
                    listView.setText("HearthCallProvider close...!\n");
                }
            }
        });
        findViewById(R.id.btn_shutdown_hearth).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                hearthInvokerMap.forEach(new BiConsumer<Integer, HearthCallImpl.Invoker>() {
                    @Override
                    public void accept(Integer integer, HearthCallImpl.Invoker invoker) {
                        invoker.shutdown();
                    }
                });
                hearthInvokerMap.clear();
                showView.setText("未请求：本端关闭");
            }
        });
    }

    private ChatCallProvider chatProvider = null;
    private RequestJobAgentCallback chatConnection = new RequestJobAgentCallback() {
        @Override
        public void onJobAgentAvailable(BaseJobAgent baseJobAgent) {
            Log.i(Tag, "onJobAgentAvailable: ChatCallProvider");
            chatProvider = (ChatCallProvider) baseJobAgent;
        }

        @Override
        public void onError(String error) {
            Log.e(Tag, "onJobAgentError: ChatCallProvider, errorCode = " + error);
        }
    };
    private Map<Integer, ChatCallImpl.Invoker> chatInvokerMap = new ConcurrentHashMap<>();
    private void bindForChat(){
        final TextView chatView = (TextView) MainActivity.this.findViewById(R.id.contentList);
        ChatCallImpl.setCallBack(new ChatCallImpl.CallBack() {
            @Override
            public void onChat(final int transactionId, final ChatCallImpl.Invoker iv) {
                if(mainHandler != null){
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            chatInvokerMap.put(transactionId, iv);
                            chatView.setText("对端接入!!!\n");
                        }
                    });
                }
            }

            @Override
            public void onNext(int transactionId, final String msg) {
                if(mainHandler != null){
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            chatView.setText("对端发送：" + msg + "\n");
                        }
                    });
                }
            }

            @Override
            public void onError(final int transactionId, String err) {
                if(mainHandler != null){
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            chatInvokerMap.remove(transactionId);
                            chatView.append("对端异常!!!\n");
                        }
                    });
                }
            }

            @Override
            public void onCompleted(final int transactionId) {
                if(mainHandler != null){
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            chatInvokerMap.get(transactionId).shutdown();
                            chatInvokerMap.remove(transactionId);
                            chatView.append("对端关闭!!!\n");
                        }
                    });
                }
            }
        });
        findViewById(R.id.btn_provider_double_stream).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (null == chatProvider) {
                    ChatCallProvider.getInstance(getApplicationContext(), chatConnection);
                    final TextView listView = (TextView) MainActivity.this.findViewById(R.id.contentList);
                    listView.setText("ChatCallProvider start...!\n");
                } else {
                    chatProvider.destroy();
                    chatProvider = null;
                    final TextView listView = (TextView) MainActivity.this.findViewById(R.id.contentList);
                    listView.setText("ChatCallProvider close...!\n");
                }
            }
        });
        findViewById(R.id.btn_resp_chat).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final EditText editView = (EditText) MainActivity.this.findViewById(R.id.edit_chat);
                final String msg = editView.getText().toString();
                chatInvokerMap.forEach(new BiConsumer<Integer, ChatCallImpl.Invoker>() {
                    @Override
                    public void accept(Integer integer, ChatCallImpl.Invoker invoker) {
                        invoker.notify(msg);
                    }
                });
                chatView.setText("本端发送：" + msg + "\n");
            }
        });
        findViewById(R.id.btn_shutdown_chat).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                chatInvokerMap.forEach(new BiConsumer<Integer, ChatCallImpl.Invoker>() {
                    @Override
                    public void accept(Integer integer, ChatCallImpl.Invoker invoker) {
                        invoker.shutdown();
                    }
                });
                chatInvokerMap.clear();
                chatView.append("本端关闭!!!\n");
            }
        });
    }
}
