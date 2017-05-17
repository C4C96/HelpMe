package com.androiddvptteam.helpme;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androiddvptteam.helpme.MissionAttribute.MissionAttribute;

import org.w3c.dom.Text;

public class ProfileFragment extends BaseFragment implements View.OnClickListener
{
	private View view;
	private ImageView avatarImageView, genderImageView;
	private TextView nameTextView, introductionTextView;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.profile_fragment, container, false);
		bind();
		return view;
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
		View options_text = view.findViewById(R.id.profile_options_text),
				myTast_button = view.findViewById(R.id.profile_my_task_button),
				released_button = view.findViewById(R.id.profile_released_button),
				accepted_button = view.findViewById(R.id.profile_accepted_button),
				doing_button = view.findViewById(R.id.profile_doing_button),
				edit_button = view.findViewById(R.id.profile_edit_button);
		options_text.setOnClickListener(this);
		myTast_button.setOnClickListener(this);
		released_button.setOnClickListener(this);
		accepted_button.setOnClickListener(this);
		doing_button.setOnClickListener(this);
		edit_button.setOnClickListener(this);
		avatarImageView = (ImageView) view.findViewById(R.id.profile_avatar_image);
		genderImageView = (ImageView) view.findViewById(R.id.profile_gender_image);
		nameTextView = (TextView) view.findViewById(R.id.profile_name_text);
		introductionTextView = (TextView) view.findViewById(R.id.profile_introduction_text);
	}

	/**
	 * 刷新信息
	 * */
	private void refreshInfo()
	{
		final MyApplication myApplication = (MyApplication) getActivity().getApplication();
		PersonalInformation personalInformation = myApplication.getPersonalInformation();
		if (personalInformation == null) {
			Toast.makeText(myApplication, "null Profile", Toast.LENGTH_SHORT).show(); return;}
		nameTextView.setText(personalInformation.getUserName());
		genderImageView.setImageResource(personalInformation.getGender() == MissionAttribute.GENDER_MALE?
											R.drawable.gender_male:
											R.drawable.gender_female);
		introductionTextView.setText(personalInformation.getIntroduction());
		//先拿缓存的图片凑上去
		if (myApplication.getAvatar() != null)
			avatarImageView.setImageBitmap(myApplication.getAvatar());
		else
			avatarImageView.setImageResource(R.drawable.default_avatar);
		//再尝试联网更新用户头像
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				Bitmap avatar = Bitmap.createBitmap(1,1, Bitmap.Config.RGB_565);
				if (Tools.getUserAvatarFromWeb(myApplication.getPersonalInformation().getSchoolNumber(), avatar))
				{
					myApplication.setAvatar(avatar);
					getActivity().runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{
							if (myApplication.getAvatar() != null)
								avatarImageView.setImageBitmap(myApplication.getAvatar());
							else
								avatarImageView.setImageResource(R.drawable.default_avatar);
						}
					});
				}
			}
		}).start();
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.profile_options_text:
				OptionsActivity.actionStart(getActivity());
				break;
			case R.id.profile_my_task_button:
				MyTaskActivity.actionStart(getActivity(), MyTaskActivity.ALL_TAB);
				break;
			case R.id.profile_released_button:
				MyTaskActivity.actionStart(getActivity(), MyTaskActivity.RELEASED_TAB);
				break;
			case R.id.profile_accepted_button:
				MyTaskActivity.actionStart(getActivity(), MyTaskActivity.ACCEPTED_TAB);
				break;
			case R.id.profile_doing_button:
				MyTaskActivity.actionStart(getActivity(), MyTaskActivity.DOING_TAB);
				break;
			case R.id.profile_edit_button:
				ProfileEditActivity.actionStart(getActivity());
				break;
		}
	}
}