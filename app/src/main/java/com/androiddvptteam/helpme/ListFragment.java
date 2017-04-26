package com.androiddvptteam.helpme;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import static java.security.AccessController.getContext;

public class ListFragment extends BaseFragment
{
	private Spinner spinnerGender;
	private Spinner spinnerAttribute;
	private Spinner spinnerRange;

	public String [] genderList=new String[5];
	public String [] attributeList=new String[5];
	public String [] rangeList=new String[5];

	private ArrayAdapter<String> arr_adapter;

	public int gender;
	public int attribute;
	public int range;

	public View view;
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.list_fragment, container, false);
		loadGenderData();
		loadAttributeData();
		loadRangeData();
		getSpinner();
		return view;
	}

	private void loadGenderData()//加载性别数据
	{
		spinnerGender = (Spinner) view.findViewById(R.id.gender_Spinner);
		//数据
		genderList[0]="性别";
		genderList[1]=this.getString(R.string.genderMale);
		genderList[2]=this.getString(R.string.genderFemale);
		genderList[3]=this.getString(R.string.genderOther);
		genderList[4]=this.getString(R.string.genderIDontCare);

		//适配器
		arr_adapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,genderList);
		//设置样式
		arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//加载适配器
		spinnerGender.setAdapter(arr_adapter);
		spinnerGender.setVisibility(View.VISIBLE);
	}

	private void loadAttributeData()//加载属性数据
	{
		spinnerAttribute = (Spinner) view.findViewById(R.id.attribute_Spinner);

		//数据
		attributeList[0]="类型";
		attributeList[1]=this.getString(R.string.attributeBring);
		attributeList[2]=this.getString(R.string.attributeTake);
		attributeList[3]=this.getString(R.string.attributeBuy);
		attributeList[4]=this.getString(R.string.attributeOther);

		//适配器
		arr_adapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,attributeList);
		//设置样式
		arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//加载适配器
		spinnerAttribute.setAdapter(arr_adapter);
	}

	private void loadRangeData()//加载范围数据
	{
		spinnerRange = (Spinner) view.findViewById(R.id.range_Spinner);

		//数据
		rangeList[0]="范围";
		rangeList[1]=this.getString(R.string.range0);
		rangeList[2]=this.getString(R.string.range1);
		rangeList[3]=this.getString(R.string.range2);
		rangeList[4]=this.getString(R.string.range3);

		//适配器
		arr_adapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,rangeList);
		//设置样式
		arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//加载适配器
		spinnerRange.setAdapter(arr_adapter);
	}



	public void getSpinner()//得到3个下拉框选择的值
	{
		spinnerGender.setOnItemSelectedListener(
				new AdapterView.OnItemSelectedListener()
				{
					public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
					{
						gender =  spinnerGender.getSelectedItemPosition();
					}
					public void onNothingSelected(AdapterView<?> parent)
					{
						// TODO Auto-generated method stub
					}
				}
		);

		spinnerAttribute.setOnItemSelectedListener(
				new AdapterView.OnItemSelectedListener()
				{
					public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
					{
						attribute =  spinnerAttribute.getSelectedItemPosition();
					}
					public void onNothingSelected(AdapterView<?> parent)
					{
						// TODO Auto-generated method stub
					}
				}
		);

		spinnerRange.setOnItemSelectedListener(
				new AdapterView.OnItemSelectedListener()
				{
					public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
					{
						range =  spinnerRange.getSelectedItemPosition();
					}
					public void onNothingSelected(AdapterView<?> parent)
					{
						// TODO Auto-generated method stub
					}
				}
		);
	}
}
