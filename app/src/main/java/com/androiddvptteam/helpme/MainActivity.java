package com.androiddvptteam.helpme;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends BaseActivity implements View.OnClickListener
{
    private MapFragment mapFragment = null;
    private ListFragment listFragment = null;
    private ReleaseFragment releaseFragment = null;
    private MessageFragment messageFragment = null;
    private ProfileFragment profileFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		//初始化
        bindView();
        mapFragment = new MapFragment();
        setFragment(mapFragment);

		ListFragment.mainActivity = this;
    }

	@Override
	protected void onResume()
	{
		super.onResume();
		final MyApplication myApplication = (MyApplication)getApplication();
		if (myApplication.getPersonalInformation() != null) return;//已登录则无需操作

		final String userId, password;
		final boolean isRemember;
		SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
		userId = preference.getString("userId", null);
		password = Tools.decrypt(preference.getString("password", null));
		isRemember = preference.getBoolean("isRemember", false);
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				if (!isRemember || !myApplication.login(userId, password))//用上次登录的用户名密码后台自动登录
					LoginActivity.actionStart(MainActivity.this, userId, isRemember);//若失败，则打开登录界面
			}
		}).start();
	}

	/**
     * 绑定监听
     * */
    private void bindView()
    {
        TextView map = (TextView)findViewById(R.id.map_text),
                list = (TextView)findViewById(R.id.list_text),
                release = (TextView)findViewById(R.id.release_text),
                message = (TextView)findViewById(R.id.message_text),
                profile = (TextView)findViewById(R.id.profile_text);
        map.setOnClickListener(this);
        list.setOnClickListener(this);
        release.setOnClickListener(this);
        message.setOnClickListener(this);
        profile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.map_text:
                if (mapFragment == null)
                    mapFragment = new MapFragment();
                setFragment(mapFragment);
                break;
            case R.id.list_text:
                if (listFragment == null)
                    listFragment = new ListFragment();
                setFragment(listFragment);
                break;
            case R.id.release_text:
                if (releaseFragment == null)
                    releaseFragment = new ReleaseFragment();
                setFragment(releaseFragment);
                break;
            case R.id.message_text:
                if (messageFragment == null)
                    messageFragment = new MessageFragment();
                setFragment(messageFragment);
                break;
            case R.id.profile_text:
                if (profileFragment == null)
                    profileFragment = new ProfileFragment();
                setFragment(profileFragment);
                break;
        }
    }


	//get方法

	public MapFragment getMapFragment()
	{
		if (mapFragment == null)
			mapFragment = new MapFragment();
		return mapFragment;
	}

	public ListFragment getListFragment()
	{
		if (listFragment == null)
			listFragment = new ListFragment();
		return listFragment;
	}

	public ReleaseFragment getReleaseFragment()
	{
		if (releaseFragment == null)
			releaseFragment = new ReleaseFragment();
		return releaseFragment;
	}

	public MessageFragment getMessageFragment()
	{
		if (messageFragment == null)
			messageFragment = new MessageFragment();
		return messageFragment;
	}

	public ProfileFragment getProfileFragment()
	{
		if (profileFragment == null)
			profileFragment = new ProfileFragment();
		return profileFragment;
	}

    /**
     * 改变当前活动的fragment
     * @param  fragment
     *                  新的活动fragment
     * */
    public void setFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.activeFragment_layout, fragment);
        transaction.commit();
    }
}
