package com.androiddvptteam.helpme;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ProfileEditActivity extends BaseActivity implements View.OnClickListener
{
	private ImageView photoImageView, genderImageView;
	private TextView nameTextView, introduceTextView, departmentTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_edit);

		//设置返回按钮
		Toolbar toolbar = (Toolbar)findViewById(R.id.options_toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		bind();
		initInfo();
	}

	/**
	 * 绑定控件
	 * */
	private void bind()
	{
		View confirmButton = findViewById(R.id.profile_edit_confirm_button);
		confirmButton.setOnClickListener(this);
		nameTextView = (TextView) findViewById(R.id.profile_edit_name_text);
		photoImageView = (ImageView) findViewById(R.id.profile_edit_photo_image);
		photoImageView.setOnClickListener(this);
		introduceTextView = (TextView) findViewById(R.id.profile_edit_introduce_editText);
		genderImageView = (ImageView) findViewById(R.id.profile_edit_gender_image);
		departmentTextView = (TextView) findViewById(R.id.profile_edit_department_text);
	}

	/**
	 * 初始化信息
	 * */
	private void initInfo()
	{
		PersonalInformation personalInformation = ((MyApplication)getApplication()).personalInformation;
		introduceTextView.setText(personalInformation.getIntroduction());
		nameTextView.setText(personalInformation.getUserName());
		departmentTextView.setText(personalInformation.getDepartmentName());
		//设置头像、性别图片
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.profile_edit_photo_image:
				//改图片
				break;
			case R.id.profile_edit_confirm_button:
				PersonalInformation personalInformation = ((MyApplication)getApplication()).personalInformation;
				personalInformation.setIntroduction(introduceTextView.getText().toString());
				break;
		}
	}

	public static void actionStart(Context context)
	{
		Intent intent = new Intent(context, ProfileEditActivity.class);
		context.startActivity(intent);
	}
}
