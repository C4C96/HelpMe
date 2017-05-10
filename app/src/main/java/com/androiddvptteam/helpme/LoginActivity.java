package com.androiddvptteam.helpme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Process;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{
	EditText userIdEditText, passwordEditText;
	CheckBox rememberCheckBox;
	ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		bind();

		Intent intent = getIntent();
		userIdEditText.setText(intent.getStringExtra("lastUserId"));
		rememberCheckBox.setChecked(intent.getBooleanExtra("lastRememberSetting", false));
	}

	/**
	 * 绑定控件
	 * */
	private void bind()
	{
		Button loginButton = (Button) findViewById(R.id.login_login_button);
		userIdEditText = (EditText) findViewById(R.id.login_user_id_text);
		passwordEditText = (EditText) findViewById(R.id.login_password_text);
		rememberCheckBox = (CheckBox) findViewById(R.id.login_remember_checkBox);
		progressBar = (ProgressBar) findViewById(R.id.login_processBar);
		loginButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.login_login_button:
				final MyApplication myApplication = (MyApplication) getApplication();
				final String userId = userIdEditText.getText().toString(),
					   password = passwordEditText.getText().toString();
				final Boolean isRemember = rememberCheckBox.isChecked();
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						progressBar.setVisibility(ProgressBar.VISIBLE);
						if (myApplication.login(userId, password))
						{//登录成功
							SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
							SharedPreferences.Editor editor = preference.edit();
							editor.putString("userId", userId);
							editor.putString("password", Tools.encrypt(password));
							editor.putBoolean("isRemember", isRemember);
							editor.apply();
						}
						else
						{//登陆失败
							Toast.makeText(LoginActivity.this, "网络/学号/密码/手机/项目负责人出错（大概", Toast.LENGTH_SHORT).show();
						}
						progressBar.setVisibility(ProgressBar.INVISIBLE);
					}
				});

				break;
		}
	}

	public static void actionStart(Context context, String lastUserId, boolean lastRememberSetting)
	{
		Intent intent = new Intent(context, LoginActivity.class);
		intent.putExtra("lastUserId", lastUserId);
		intent.putExtra("lastRememberSetting", lastRememberSetting);
		context.startActivity(intent);
	}
}
