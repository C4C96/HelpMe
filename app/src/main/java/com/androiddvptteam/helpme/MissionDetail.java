package com.androiddvptteam.helpme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MissionDetail extends AppCompatActivity {

    private Mission mission;

    private TextView name,gender,introduction,content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_detail);


        //获得mission信息
        Intent intent = this.getIntent();
        mission=(Mission)intent.getSerializableExtra("thisMission");

        //修改textview的值
        TextView name=(TextView)findViewById(R.id.name_text);
        TextView gender=(TextView)findViewById(R.id.gender_text);
        TextView introduction=(TextView)findViewById(R.id.intro_text);
        TextView content=(TextView)findViewById(R.id.content_text);

        changeMissionInfo();

        //确认和取消按钮
        Button cancelButton=(Button)findViewById(R.id.cancel_button);
        Button confirmButton=(Button)findViewById(R.id.confirm_button);

        confirmButton.setText("返回");
        confirmButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        MyApplication myApplication = (MyApplication)getApplication();

        if(mission.getPublisher().getSchoolNumber()==myApplication.getPersonalInformation().getSchoolNumber())
        {//如果当前用户是改任务的发布者
            switch(mission.getState())
            {
                case 1://该任务正在被做
                    confirmButton.setText("确认完成");
                    confirmButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v) {//需要对数据库进行修改
                            finish();
                        }
                    });
                    break;
                case 2://该任务已经被完成
                    cancelButton.setVisibility(View.GONE);
                    break;
                case 3://该任务已经被取消
                    cancelButton.setVisibility(View.GONE);
                    break;
            }
        }
        else if(mission.getRecipient().getSchoolNumber()==myApplication.getPersonalInformation().getSchoolNumber())
        {//如果当前用户是该任务的接收者
            switch(mission.getState())
            {
                case 0://该任务未被接受
                    confirmButton.setText("接收");
                    confirmButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v) {//对数据库进行修改
                            finish();
                        }
                    });
                    break;
                case 1://该任务正在被做
                    confirmButton.setText("放弃任务");
                    confirmButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v) {//需要对数据库进行修改
                            finish();
                        }
                    });
                    break;
                case 2://该任务已经被完成
                    cancelButton.setVisibility(View.GONE);
                    break;
                case 3://该任务已经被取消
                    cancelButton.setVisibility(View.GONE);
                    break;
            }
        }
        else
        {//既不是发布者也不是接收者
            confirmButton.setText("接收");
            confirmButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {//对数据库进行修改
                    finish();
                }
            });
        }

    }

    private void changeMissionInfo()
    {
        String g="";
        switch(mission.getGender())
        {
            case 0:g="男";
                break;
            case 1:g="女";
                break;
            case 2:g="其他";
                break;
            case 3:g="无所谓";
        }

        name.setText(mission.getPublisher().getUserName());
        gender.setText(g);
        introduction.setText(mission.getPublisher().getIntroduction());
        content.setText(mission.getContent());

    }
}
