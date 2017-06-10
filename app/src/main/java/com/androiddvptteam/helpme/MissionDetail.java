package com.androiddvptteam.helpme;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

/**
 * 差取消任务
 */
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
            name=(TextView)findViewById(R.id.name_text);
            gender=(TextView)findViewById(R.id.gender_text);
            introduction=(TextView)findViewById(R.id.intro_text);
            content=(TextView)findViewById(R.id.content_text);

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


        final MyApplication myApplication = (MyApplication)getApplication();
        final Mission.MissionManager missionManager=new Mission.MissionManager();

        if(mission.getPublisher().getSchoolNumber().equals(myApplication.getPersonalInformation().getSchoolNumber()))
        {//如果当前用户是改任务的发布者
            switch(mission.getState())
            {
                case 0://该任务还未被接受
                    confirmButton.setText("取消任务");
                    confirmButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v) {
                            new Thread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    missionManager.cancel(MissionDetail.this,mission, Calendar.getInstance());
                                    new SendMessage(mission.getID(),mission.getPublisher().getSchoolNumber(),"","取消",mission.getTitle());
                                }
                            }).start();

                            finish();
                        }
                    });
                    break;
                case 1://该任务正在被做
                    confirmButton.setText("确认完成");
                    confirmButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v) {
                            new Thread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    missionManager.finish(MissionDetail.this,mission, Calendar.getInstance());
                                    new SendMessage(mission.getID(),mission.getPublisher().getSchoolNumber(),mission.getRecipient().getSchoolNumber(),"确认完成",mission.getTitle());
                                }
                            }).start();

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
        else if(mission.getRecipient()==null || mission.getRecipient().getSchoolNumber().equals(myApplication.getPersonalInformation().getSchoolNumber()))
        {//如果当前用户是该任务的接收者
            switch(mission.getState())
            {
                case 0://该任务未被接受
                    confirmButton.setText("接收");
                    confirmButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v) {
                            new Thread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    missionManager.receive(MissionDetail.this,mission,myApplication.getPersonalInformation(),Calendar.getInstance());
                                    new SendMessage(mission.getID(),mission.getPublisher().getSchoolNumber(),mission.getRecipient().getSchoolNumber(),"接收",mission.getTitle());
                                }
                            }).start();


                            finish();
                        }
                    });

                    break;
                case 1://该任务正在被做
                    confirmButton.setText("放弃任务");
                    confirmButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v) {
                            new Thread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    missionManager.abandon(MissionDetail.this,mission/*,Calendar.getInstance()*/);
                                    new SendMessage(mission.getID(),mission.getPublisher().getSchoolNumber(),mission.getRecipient().getSchoolNumber(),"放弃",mission.getTitle());
                                }
                            }).start();

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
                public void onClick(View v) {
                    new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            missionManager.receive(MissionDetail.this,mission,myApplication.getPersonalInformation(),Calendar.getInstance());
                            new SendMessage(mission.getID(),mission.getPublisher().getSchoolNumber(),mission.getRecipient().getSchoolNumber(),"接收",mission.getTitle());
                        }
                    }).start();

                    finish();
                }
            });

        }

    }

    public static void actionStart(Context context, Mission mission)
    {
        Intent intent=new Intent(context,MissionDetail.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("thisMission", mission);
        intent.putExtras(bundle);
        context.startActivity(intent);
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
