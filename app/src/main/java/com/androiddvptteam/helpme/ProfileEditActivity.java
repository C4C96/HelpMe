package com.androiddvptteam.helpme;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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
import java.io.FileNotFoundException;
import java.io.IOException;

public class ProfileEditActivity extends BaseActivity implements View.OnClickListener
{
	private static final int CHOOSE_PICTURE = 0;
	private static final int TAKE_PICTURE = 1;
	private static final int CROP_SMALL_PICTURE = 2;

	private Uri tempUri;//修改头像选择拍照时临时图片的UI

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
						/* Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
						openAlbumIntent.setType("image/*");
						startActivityForResult(openAlbumIntent, CHOOSE_PICTURE); */
						if (ContextCompat.checkSelfPermission(ProfileEditActivity.this,
									Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
									PackageManager.PERMISSION_GRANTED)
							ActivityCompat.requestPermissions(ProfileEditActivity.this, 
									new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
						else
							openAlbum();									
						break;
					case TAKE_PICTURE: // 拍照
					/* 	if (ContextCompat.checkSelfPermission(ProfileEditActivity.this, Manifest.permission.CAMERA)
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
						} */
						File outputImage = new File(getExternalCacheDir(), "image.jpg");
						if (outputImage.exists())
							outputImage.delete();
						if (Build.VERSION.SDK_INT >= 24)
							tempUri = FileProvider.getUriForFile(ProfileEditActivity.this,
											"com.androiddvptteam.helpme", outputImage);
						else
							tempUri = Uri.fromFile(outputImage);
						Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
						intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
						startActivityForResult(intent, TAKE_PICTURE);
						break;
				}
			}
		});
		builder.create().show();
	}

	/**
	 * 打开相册
	 * */
	private void openAlbum()
	{
		Intent intent = new Intent("android.intent.action.GET_CONTENT");
		intent.setType("image/*");
		startActivityForResult(intent, CHOOSE_PICTURE);
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
					openAlbum();
				else
					Toast.makeText(this, "使用相册的请求遭到了拒绝", Toast.LENGTH_SHORT).show();
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
					//startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
					try
					{
						Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(tempUri));
						avatarImageView.setImageBitmap(bitmap);
					}
					catch (FileNotFoundException e)
					{
						e.printStackTrace();
					}
					break;
				case CHOOSE_PICTURE:
					startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
					/*if (Build.VERSION.SDK_INT >= 19)
						handleImageOnKitKat(data);
					else
						handleImageBeforeKitKat(data);*/
					break;
				case CROP_SMALL_PICTURE:
					if (data != null)
						setImage(data); //让刚才选择裁剪得到的图片显示在界面上
					break;
			}
		}
	}

	/**
	 * 4.4及以上系统使用这个方法处理图片
	 * */
	@TargetApi(19)
	private void handleImageOnKitKat(Intent data)
	{
		String imagePath = null;
		Uri uri = data.getData();
		if (DocumentsContract.isDocumentUri(this, uri))
		{
			//如果是document类型的uri，则通过document id处理
			String docId = DocumentsContract.getDocumentId(uri);
			if ("com.android.providers.media.documents".equals(uri.getAuthority()))
			{
				String id = docId.split(":")[1];//解析出数字格式的id
				String selection = MediaStore.Images.Media._ID + "=" + id;
				imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
			}
			else if ("com.android.providers.downloads.documents".equals(uri.getAuthority()))
			{
				Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
				imagePath = getImagePath(contentUri, null);
			}
		}
		else if ("content".equalsIgnoreCase(uri.getScheme()))
			//如果是content类型的Uri，则使用普通方式处理
			imagePath = getImagePath(uri, null);
		else if ("file".equalsIgnoreCase(uri.getScheme()))
			//如果是file类型的Uri，直接获取图片路径即可
			imagePath = uri.getPath();
		displayImage(imagePath);
	}
	
	/**
	 * 4.4以下系统使用这个方法处理图片
	 * */
	private void handleImageBeforeKitKat(Intent data)
	{
		Uri uri = data.getData();
		String imagePath = getImagePath(uri, null);
		displayImage(imagePath);
	}
	
	private String getImagePath(Uri uri, String selection)
	{
		String path = null;
		//通过Uri和selection来获取真实的图片路径
		Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
		if (cursor != null)
		{
			if (cursor.moveToFirst())
				path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
			cursor.close();
		}
		return path;
	}
	
	private void displayImage(String imagePath)
	{
		if (imagePath != null)
		{
			Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
			avatarImageView.setImageBitmap(bitmap);
		}
		else
			Toast.makeText(this, "Fail to get image", Toast.LENGTH_SHORT).show();
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
