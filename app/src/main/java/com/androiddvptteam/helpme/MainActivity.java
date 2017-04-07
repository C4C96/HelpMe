package com.androiddvptteam.helpme;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
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
        bindView();
        mapFragment = new MapFragment();
        setFragment(mapFragment);
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

    /**
     * 改变当前活动的fragment
     * @param  fragment
     *                  新的活动fragment
     * */
    private void setFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.activeFragment_layout, fragment);
        transaction.commit();
    }
}
