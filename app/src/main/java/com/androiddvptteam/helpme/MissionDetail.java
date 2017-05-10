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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_detail);

        //确认和取消按钮
        Button cancelButton=(Button)findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button confirmButton=(Button)findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {//对数据库进行修改
                finish();
            }
        });

        //获得mission信息
        Intent intent = this.getIntent();
        mission=(Mission)intent.getSerializableExtra("thisMission");

        //修改textview的值
        TextView name=(TextView)findViewById(R.id.name_text);
        TextView gender=(TextView)findViewById(R.id.gender_text);
        TextView introduction=(TextView)findViewById(R.id.intro_text);
        TextView content=(TextView)findViewById(R.id.content_text);

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
