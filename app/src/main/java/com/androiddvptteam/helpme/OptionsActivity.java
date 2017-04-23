package com.androiddvptteam.helpme;

import android.content.Context;
import android.content.Intent;
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
	}

	/**
	 * 绑定监听
	 * */
	private void bindView()
	{
		View aboutButton = findViewById(R.id.options_about_button),
			 logoutButton = findViewById(R.id.options_logout_button),
			 checkUpdateButton = findViewById(R.id.options_check_update_button);
		Switch messageRemindSwitch = (Switch) findViewById(R.id.options_message_remind_switch);
		aboutButton.setOnClickListener(this);
		checkUpdateButton.setOnClickListener(this);
		logoutButton.setOnClickListener(this);
		messageRemindSwitch.setOnCheckedChangeListener(this);
	}

	public static void actionStart(Context context)
	{
		Intent intent = new Intent(context, OptionsActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.options_about_button:
				Toast.makeText(this, "关于", Toast.LENGTH_SHORT).show();
				break;
			case R.id.options_check_update_button:
				Toast.makeText(this, "检查更新", Toast.LENGTH_SHORT).show();
				break;
			case R.id.options_logout_button:
				Toast.makeText(this, "登出", Toast.LENGTH_SHORT).show();
				break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	{
		switch(buttonView.getId())
		{
			case R.id.options_message_remind_switch:
				if(isChecked)
					Toast.makeText(this, "新消息提醒开启", Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(this, "新消息提醒关闭", Toast.LENGTH_SHORT).show();
				break;
		}
	}
}
