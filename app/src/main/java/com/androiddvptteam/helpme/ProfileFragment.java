package com.androiddvptteam.helpme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProfileFragment extends BaseFragment implements View.OnClickListener
{
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.profile_fragment, container, false);
		View options_text = view.findViewById(R.id.profile_options_text),
			 released_button = view.findViewById(R.id.profile_released_button),
			 accepted_button = view.findViewById(R.id.profile_accepted_button),
			 doing_button = view.findViewById(R.id.profile_doing_button);
		options_text.setOnClickListener(this);
		released_button.setOnClickListener(this);
		accepted_button.setOnClickListener(this);
		doing_button.setOnClickListener(this);
		return view;
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
		}
	}
}
