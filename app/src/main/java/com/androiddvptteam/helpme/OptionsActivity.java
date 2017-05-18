package com.androiddvptteam.helpme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class OptionsActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener
{
	private Switch notificationSwitch, soundSwitch, vibrationSwitch;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options);

		//设置返回按钮
		Toolbar toolbar = (Toolbar)findViewById(R.id.options_toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		bindView();
		init();
	}

	/**
	 * 初始化信息
	 * */
	private void init()
	{
		notificationSwitch.setChecked(Config.getNotificationConfig());
		soundSwitch.setChecked(Config.getSoundConfig());
		vibrationSwitch.setChecked(Config.getVibrationConfig());
	}

	/**
	 * 绑定监听
	 * */
	private void bindView()
	{
		View aboutButton = findViewById(R.id.options_about_button),
			 logoutButton = findViewById(R.id.options_logout_button),
			 checkUpdateButton = findViewById(R.id.options_check_update_button);
		notificationSwitch = (Switch) findViewById(R.id.options_notification_switch);
	    soundSwitch = (Switch) findViewById(R.id.options_sound_switch);
	    vibrationSwitch = (Switch) findViewById(R.id.options_vibration_switch);
		aboutButton.setOnClickListener(this);
		checkUpdateButton.setOnClickListener(this);
		logoutButton.setOnClickListener(this);
		notificationSwitch.setOnCheckedChangeListener(this);
		soundSwitch.setOnCheckedChangeListener(this);
		vibrationSwitch.setOnCheckedChangeListener(this);
	}

	public static void actionStart(Context context)
	{
		Intent intent = new Intent(context, OptionsActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v)
	{
		MyApplication myApplication = (MyApplication) getApplication();
		switch (v.getId())
		{
			case R.id.options_about_button:
				Toast.makeText(this, "关于", Toast.LENGTH_SHORT).show();
				break;
			case R.id.options_check_update_button:
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							//延迟个500毫秒，假装在联网
							Thread.sleep(500);
						} catch (InterruptedException e)
						{
							e.printStackTrace();
						}
						runOnUiThread(new Runnable()
						{
							@Override
							public void run()
							{
								//反正这破玩意也不会更新的，直接说最新就行了
								Toast.makeText(OptionsActivity.this, "当前已是最新版本", Toast.LENGTH_SHORT).show();
							}
						});
					}
				}).start();
				break;
			case R.id.options_logout_button:
				SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
				preference.edit().putString("password", null).apply();//退出的时候只将首选项中的密码置空，保留用户名
				myApplication.logout();
				break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	{
		switch(buttonView.getId())
		{
			case R.id.options_notification_switch:
				Config.setNotificationConfig(isChecked);
				break;
			case R.id.options_sound_switch:
				Config.setSoundConfig(isChecked);
				break;
			case R.id.options_vibration_switch:
				Config.setVibrationConfig(isChecked);
				break;
		}
	}
}
