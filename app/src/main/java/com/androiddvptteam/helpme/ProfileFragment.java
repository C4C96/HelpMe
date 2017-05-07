package com.androiddvptteam.helpme;

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

import com.androiddvptteam.helpme.MissionAttribute.MissionAttribute;

import org.w3c.dom.Text;

public class ProfileFragment extends BaseFragment implements View.OnClickListener
{
	private View view;
	private ImageView avatarImageView;
	private TextView nameTextView, introductionTextView;

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.profile_fragment, container, false);
		bind();
		//refreshInfo();
		return view;
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
				released_button = view.findViewById(R.id.profile_released_button),
				accepted_button = view.findViewById(R.id.profile_accepted_button),
				doing_button = view.findViewById(R.id.profile_doing_button),
				edit_button = view.findViewById(R.id.profile_edit_button);
		options_text.setOnClickListener(this);
		released_button.setOnClickListener(this);
		accepted_button.setOnClickListener(this);
		doing_button.setOnClickListener(this);
		edit_button.setOnClickListener(this);
		avatarImageView = (ImageView) view.findViewById(R.id.profile_avatar_image);
		nameTextView = (TextView) view.findViewById(R.id.profile_name_text);
		introductionTextView = (TextView) view.findViewById(R.id.profile_introduction_text);
	}

	/**
	 * 刷新信息
	 * */
	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	private void refreshInfo()
	{
		MyApplication myApplication = (MyApplication) getActivity().getApplication();
		PersonalInformation personalInformation = myApplication.getPersonalInformation();
		nameTextView.setText(personalInformation.getUserName());
		nameTextView.setCompoundDrawablesWithIntrinsicBounds(null,
				getResources().getDrawable(personalInformation.getGender() == MissionAttribute.GENDER_MALE?
						R.drawable.gender_male:
						R.drawable.gender_female, null),
				null, null);
		introductionTextView.setText(personalInformation.getIntroduction());
		if (myApplication.getAvatar() != null)
			avatarImageView.setImageBitmap(myApplication.getAvatar());
		else
			avatarImageView.setImageResource(R.drawable.default_avatar);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.profile_options_text:
				OptionsActivity.actionStart(getActivity());
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
