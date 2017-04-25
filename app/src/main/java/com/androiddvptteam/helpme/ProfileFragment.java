package com.androiddvptteam.helpme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ProfileFragment extends BaseFragment implements View.OnClickListener
{
	private View view;
	private ImageView photoImageView, genderImageView;
	private TextView nameTextView, introductionTextView;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.profile_fragment, container, false);
		bind();
		initInfo();
		return view;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		initInfo();
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
		photoImageView = (ImageView) view.findViewById(R.id.profile_photo_image);
		genderImageView = (ImageView) view.findViewById(R.id.profile_gender_image);
		nameTextView = (TextView) view.findViewById(R.id.profile_name_text);
		introductionTextView = (TextView) view.findViewById(R.id.profile_introduction_text);
	}

	/**
	 * 初始化信息
	 * */
	private void initInfo()
	{
		MyApplication myApplication = (MyApplication) getActivity().getApplication();
		nameTextView.setText(myApplication.getUserName());
		introductionTextView.setText(myApplication.getUserIntroduction());
		//初始化头像、性别图片
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
