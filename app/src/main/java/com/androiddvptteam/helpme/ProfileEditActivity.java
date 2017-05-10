package com.androiddvptteam.helpme;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androiddvptteam.helpme.MissionAttribute.MissionAttribute;

import org.w3c.dom.Text;

import java.io.File;

public class ProfileEditActivity extends BaseActivity implements View.OnClickListener
{
	private static final int CHOOSE_PICTURE = 0;
	private static final int TAKE_PICTURE = 1;
	private static final int CROP_SMALL_PICTURE = 2;

	private static Uri tempUri;//修改头像选择拍照时临时图片的UI

	private ImageView avatarImageView, genderImageView;
	private TextView nameTextView, schoolNumView, introduceTextView, departmentTextView;

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
	}

	@Override
	public void onResume()
	{
		super.onResume();
		refreshInfo();
	}

	/**
	 * 绑定控件
	 * */
	private void bind()
	{
		View confirmButton = findViewById(R.id.profile_edit_confirm_button);
		confirmButton.setOnClickListener(this);
		nameTextView = (TextView) findViewById(R.id.profile_edit_name_text);
		avatarImageView = (ImageView) findViewById(R.id.profile_edit_avatar_image);
		avatarImageView.setOnClickListener(this);
		genderImageView = (ImageView) findViewById(R.id.profile_edit_gender_image);
		schoolNumView = (TextView) findViewById(R.id.profile_edit_schoolNum_text);
		introduceTextView = (TextView) findViewById(R.id.profile_edit_introduce_editText);
		departmentTextView = (TextView) findViewById(R.id.profile_edit_department_text);
	}

	/**
	 * 刷新信息
	 * */
	private void refreshInfo()
	{
		MyApplication myApplication = (MyApplication) getApplication();
		PersonalInformation personalInformation = myApplication.getPersonalInformation();
		Bitmap avatar = myApplication.getAvatar();
		if (personalInformation == null) return;
		introduceTextView.setText(personalInformation.getIntroduction());
		nameTextView.setText(personalInformation.getUserName());
		genderImageView.setImageResource(personalInformation.getGender() == MissionAttribute.GENDER_MALE?
											R.drawable.gender_male:
											R.drawable.gender_female);
		schoolNumView.setText(personalInformation.getSchoolNumber());
		departmentTextView.setText(personalInformation.getDepartmentName());
		if (avatar != null)
			avatarImageView.setImageBitmap(avatar);
		else
			avatarImageView.setImageResource(R.drawable.default_avatar);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.profile_edit_avatar_image:
				showChoosePicDialog();
				break;
			case R.id.profile_edit_confirm_button:
				MyApplication myApplication = (MyApplication) getApplication();
				PersonalInformation personalInformation = myApplication.getPersonalInformation();
				personalInformation.setIntroduction(introduceTextView.getText().toString());
				break;
		}
	}

	/**
	 * 显示设置头像的对话框
	 * */
	protected void showChoosePicDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("设置头像");
		String[] items = { "选择本地照片", "拍照" };
		builder.setNegativeButton("取消", null);
		builder.setItems(items, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				switch (which)
				{
					case CHOOSE_PICTURE: // 选择本地照片
						Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
						openAlbumIntent.setType("image/*");
						startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
						break;
					case TAKE_PICTURE: // 拍照
						if (ContextCompat.checkSelfPermission(ProfileEditActivity.this, Manifest.permission.CAMERA)
								!= PackageManager.PERMISSION_GRANTED)
						{
							    //请求权限
							    ActivityCompat.requestPermissions(ProfileEditActivity.this,
								            new String[]{Manifest.permission.CAMERA}, 1);
						}
						else
						{
							try
							{
								Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
								tempUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.jpg"));
								// 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
								openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
								startActivityForResult(openCameraIntent, TAKE_PICTURE);
							}
							catch (SecurityException e)
							{
								Log.e(TAG, "Camera Security Exception.");
							}
						}
						break;
				}
			}
		});
		builder.create().show();
	}

	/**
	 * 申请权限的回调函数
	 * */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode)
		{
			case 1:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
					Log.d(TAG, "Camera request permission has been granted.");
				else
					Toast.makeText(this, "使用相机的请求遭到了拒绝", Toast.LENGTH_SHORT).show();
				break;
		}
	}

	/**
	 * 活动返回信息处理
	 * */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK)
		{
			switch (requestCode) {
				case TAKE_PICTURE:
					startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
					break;
				case CHOOSE_PICTURE:
					startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
					break;
				case CROP_SMALL_PICTURE:
					if (data != null)
						setImage(data); //让刚才选择裁剪得到的图片显示在界面上
					//getContentResolver().delete(tempUri, null, null); //将临时图片删除
					break;
			}
		}
	}

	/**
	 * 裁剪图片
	 * */
	protected void startPhotoZoom(Uri uri)
	{
		if (uri == null)
			Log.e(TAG, "The uri is not exist.");
		tempUri = uri;
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, CROP_SMALL_PICTURE);
	}

	//设置图片
	protected void setImage(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null)
		{
			Bitmap photo = extras.getParcelable("data");
			((MyApplication)getApplication()).setAvatar(photo);
		}
	}

	public static void actionStart(Context context)
	{
		Intent intent = new Intent(context, ProfileEditActivity.class);
		context.startActivity(intent);
	}
}
