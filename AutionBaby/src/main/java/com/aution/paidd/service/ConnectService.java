package com.aution.paidd.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.framework.core.utils.LogUtils;
import com.framework.core.utils.ParseUtils;
import com.aution.paidd.bean.ServiceMessage;
import com.aution.paidd.common.CacheData;
import com.aution.paidd.common.Constant;
import com.aution.paidd.common.HttpMethod;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by quxiang on 2017/9/5.
 */

public class ConnectService extends Service {

	private WebSocketClient mWebSocketClient;

	private String address;

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		if (CacheData.getInstance().getUserData() != null)
			address = HttpMethod.Socket_URL + CacheData.getInstance().getUserData().getUid();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LogUtils.e("服务开启啦");
		//开启socket 服务
		initSocketClient();
		connect();
		return START_STICKY;
	}


	/**
	 * @throws URISyntaxException
	 */
	private void initSocketClient() {

		try {
			mWebSocketClient = new WebSocketClient(new URI(address)) {
				@Override
				public void onOpen(ServerHandshake serverHandshake) {
					//连接成功
					Log.e("连接成功", "opened connection");
				}


				@Override
				public void onMessage(String s) {
//					LogUtils.e("接收到服务器的时间："+s);
					//服务端消息
					ServiceMessage sm = ParseUtils.parseJson(s, ServiceMessage.class);
					if (sm != null) {
						//可以发送了

						if (sm.getCmd() == 1002) {
							LogUtils.e("有人中奖了====" + sm.getObj().getLastbid() + " cid=" + sm.getObj().getCid() + " status=" + sm.getObj().getStatus());
							//有人中奖了
							Intent intent = new Intent("com.qxhd.toast");
							intent.putExtra("uid", sm.getObj().getLastuid());
							sendBroadcast(intent);
						}
						Intent intent = new Intent(Constant.BroadcastAction);
						intent.putExtra("model", sm.getObj());
						sendBroadcast(intent);

					}
				}


				@Override
				public void onClose(int i, String s, boolean remote) {
					//连接断开，remote判定是客户端断开还是服务端断开
					Log.e("连接断开", "Connection closed by " + (remote ? "remote peer" : "us") + ", info=" + s);
					//
					closeConnect();
				}


				@Override
				public void onError(Exception e) {
					Log.e("连接错误", "error:" + e);
				}
			};

		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}

	//连接
	private void connect() {
		new Thread() {
			@Override
			public void run() {
				if (!mWebSocketClient.isConnecting())
				mWebSocketClient.connect();
			}
		}.start();
	}


	//断开连接
	private void closeConnect() {
		try {
			mWebSocketClient.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mWebSocketClient = null;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
