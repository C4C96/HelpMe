package com.androiddvptteam.helpme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReleaseFragment extends Fragment
{
	private Spinner spinnerGender;
	private Spinner spinnerAttribute;
	private Spinner spinnerRange;
	public String [] genderList=new String[4];
	public String [] attributeList=new String[4];
	public String [] rangeList=new String[4];
	public String title="";
	public String content="";
	private ArrayAdapter<String> arr_adapter;
	private View view;

	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.release_fragment, container, false);
		loadGenderData();
		loadAttributeData();
		loadRangeData();
		getTitleAndContent();
		return view;
	}

	private void loadGenderData()
	{
		spinnerGender = (Spinner) view.findViewById(R.id.genderSpinner);
		//数据
		genderList[0]=this.getString(R.string.genderMale);
		genderList[1]=this.getString(R.string.genderFemale);
		genderList[2]=this.getString(R.string.genderOther);
		genderList[3]=this.getString(R.string.genderIDontCare);

		//适配器
		arr_adapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,genderList);
		//设置样式
		arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//加载适配器
		spinnerGender.setAdapter(arr_adapter);
	}

	private void loadAttributeData()
	{
		spinnerAttribute = (Spinner) view.findViewById(R.id.attributeSpinner);

		//数据
		attributeList[0]=this.getString(R.string.attributeBring);
		attributeList[1]=this.getString(R.string.attributeTake);
		attributeList[2]=this.getString(R.string.attributeBuy);
		attributeList[3]=this.getString(R.string.attributeOther);

		//适配器
		arr_adapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,attributeList);
		//设置样式
		arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//加载适配器
		spinnerAttribute.setAdapter(arr_adapter);
	}

	private void loadRangeData()
	{
		spinnerRange = (Spinner) view.findViewById(R.id.rangeSpinner);

		//数据
		rangeList[0]=this.getString(R.string.range0);
		rangeList[1]=this.getString(R.string.range1);
		rangeList[2]=this.getString(R.string.range2);
		rangeList[3]=this.getString(R.string.range3);

		//适配器
		arr_adapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,rangeList);
		//设置样式
		arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//加载适配器
		spinnerRange.setAdapter(arr_adapter);
	}

	public void getTitleAndContent()
	{
		Button releaseButton=(Button) view.findViewById(R.id.releaseButton);
		releaseButton.setOnClickListener(
				new View.OnClickListener()
				{
					public void onClick(View v)
					{
						//获取输入的标题
						EditText titleEditText =(EditText)view.findViewById(R.id.titleEditText);
						title=titleEditText.getText().toString();

						//获取输入的内容
						EditText contentEditText =(EditText)view.findViewById(R.id.contentEditText);
						content=contentEditText.getText().toString();

						Toast.makeText(getContext(),title,Toast.LENGTH_SHORT).show();
					}
				}
		);
	}
}
