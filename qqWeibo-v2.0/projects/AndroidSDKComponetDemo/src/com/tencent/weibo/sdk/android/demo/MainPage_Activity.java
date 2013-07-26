package com.tencent.weibo.sdk.android.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.tencent.weibo.sdk.android.api.util.Util;
import com.tencent.weibo.sdk.android.component.Authorize;
import com.tencent.weibo.sdk.android.component.GeneralInterfaceActivity;
import com.tencent.weibo.sdk.android.component.PublishActivity;
import com.tencent.weibo.sdk.android.component.ReAddActivity;
import com.tencent.weibo.sdk.android.component.sso.AuthHelper;
import com.tencent.weibo.sdk.android.component.sso.OnAuthListener;
import com.tencent.weibo.sdk.android.component.sso.WeiboToken;
public class MainPage_Activity extends Activity {
	private Button authorize =null;
	private Button add =null;
	private Button readd =null;
	private Context context = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.main_layout);
		context = this.getApplicationContext();
		this.init();
	}

	public void init(){
		authorize = (Button)findViewById(R.id.authorize);
		authorize.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				/**
				 * 跳转到授权组件
				 */
				long appid = Long.valueOf(Util.getConfig().getProperty("APP_KEY"));
				String app_secket = Util.getConfig().getProperty("APP_KEY_SEC");
				auth(appid, app_secket);
			}
		});
		
		add = (Button)findViewById(R.id.add);
		add.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//startReq();
				
				/**
				 * 跳转到一键转播组件
				 */
				Intent i = new Intent(MainPage_Activity.this,PublishActivity.class);
				startActivity(i);
			}
		});
		
		readd = (Button)findViewById(R.id.readd);
		readd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//startReq();
				/**
				 * 跳转到一键转播组件
				 * 可以传递一些参数
				 * 如content为转播内容
				 * video_url为转播视频URL
				 * pic_url为转播图片URL
				 */
				Intent i = new Intent(MainPage_Activity.this,ReAddActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("content", "Make U happy");
				bundle.putString("video_url", "http://www.tudou.com/programs/view/b-4VQLxwoX4/");
				bundle.putString("pic_url", "http://t2.qpic.cn/mblogpic/9c7e34358608bb61a696/2000");
				i.putExtras(bundle);
				startActivity(i);
			}
		});
		Button delAuthorize = (Button)findViewById(R.id.exit);
		delAuthorize.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Util.clearSharePersistent(context);
				Toast.makeText(MainPage_Activity.this,"注销成功", Toast.LENGTH_SHORT)
	            .show();
			}
		});
		
		Button comInterface = (Button)findViewById(R.id.commoninterface);
		comInterface.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainPage_Activity.this,GeneralInterfaceActivity.class);
				startActivity(i);
			}
		});
		 
	}
	
	private void auth(long appid, String app_secket) {
		final Context context = this.getApplicationContext();
		
		AuthHelper.register(this, appid, app_secket, new OnAuthListener() {

			@Override
			public void onWeiBoNotInstalled() {
				Toast.makeText(MainPage_Activity.this, "onWeiBoNotInstalled", 1000)
						.show();
				Intent i = new Intent(MainPage_Activity.this,Authorize.class);
				startActivity(i);
			}

			@Override
			public void onWeiboVersionMisMatch() {
				Toast.makeText(MainPage_Activity.this, "onWeiboVersionMisMatch",
						1000).show();
				Intent i = new Intent(MainPage_Activity.this,Authorize.class);
				startActivity(i);
			}

			@Override
			public void onAuthFail(int result, String err) {
				Toast.makeText(MainPage_Activity.this, "result : " + result, 1000)
						.show();
			}

			@Override
			public void onAuthPassed(String name, WeiboToken token) {
				Toast.makeText(MainPage_Activity.this, "passed", 1000).show();
				//
				Util.saveSharePersistent(context, "ACCESS_TOKEN", token.accessToken);
				Util.saveSharePersistent(context, "EXPIRES_IN", String.valueOf(token.expiresIn));
				Util.saveSharePersistent(context, "OPEN_ID", token.openID);
//				Util.saveSharePersistent(context, "OPEN_KEY", token.omasKey);
				Util.saveSharePersistent(context, "REFRESH_TOKEN", "");
//				Util.saveSharePersistent(context, "NAME", name);
//				Util.saveSharePersistent(context, "NICK", name);
				Util.saveSharePersistent(context, "CLIENT_ID", Util.getConfig().getProperty("APP_KEY"));
				Util.saveSharePersistent(context, "AUTHORIZETIME",
						String.valueOf(System.currentTimeMillis() / 1000l));
				//
			}
		});

		AuthHelper.auth(this, "");
	}
}
