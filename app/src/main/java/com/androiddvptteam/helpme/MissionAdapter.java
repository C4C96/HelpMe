package com.androiddvptteam.helpme;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zhao on 2017/4/27.
 */

public class MissionAdapter extends RecyclerView.Adapter<MissionAdapter.ViewHolder> {

    private List<Mission> myMissionList;
    private List<String> mySchoolNumList;

    public Activity activity;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View missionView;
        TextView titleText;
        TextView genderText;
        TextView attributeText;
        TextView rangeText;

        public ViewHolder(View view)
        {
            super(view);
            missionView=view;
            titleText=(TextView)view.findViewById(R.id.title_TextView);
            genderText=(TextView)view.findViewById(R.id.genderContent_TextView);
            attributeText=(TextView)view.findViewById(R.id.attributeContent_TextView);
            rangeText=(TextView)view.findViewById(R.id.rangeContent_TextView);
        }
    }

    public MissionAdapter(List<Mission> missionList,List<String> schoolNumList,Activity a)
    {
        myMissionList=missionList;
        mySchoolNumList=schoolNumList;
        activity=a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.titleText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                int position= holder.getAdapterPosition();
                Mission mission =myMissionList.get(position);

                Intent intent=new Intent(activity,MissionDetail.class);
                activity.startActivity(intent);

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Mission mission=myMissionList.get(position);
        holder.titleText.setText(mission.getTitle());
        String gender="",attribute="",range="";
        switch(mission.getGender())
        {
            case 0:gender="男";
                break;
            case 1:gender="女";
                break;
            case 2:gender="其他";
                break;
            case 3:gender="无所谓";
        }
        switch(mission.getAttribute())
        {
            case 0:attribute="送东西";
                break;
            case 1:attribute="取东西";
                break;
            case 2:attribute="代购";
                break;
            case 3:attribute="其他";
        }
        switch(mission.getRange())
        {
            case 0:range="100米以内";
                break;
            case 1:range="100-300米";
                break;
            case 2:range="300-700米";
                break;
            case 3:range="700米以上";
        }
        holder.genderText.setText(gender);
        holder.attributeText.setText(attribute);
        holder.rangeText.setText(range);
    }

    @Override
    public int getItemCount() {
        return myMissionList.size();
    }
}
