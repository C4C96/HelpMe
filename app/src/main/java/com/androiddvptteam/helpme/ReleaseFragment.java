package com.androiddvptteam.helpme;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.androiddvptteam.helpme.Connection.ReleaseConnection;
import java.net.URL;
import java.util.Calendar;

public class ReleaseFragment extends BaseFragment
{
	private Spinner spinnerGender;
	private Spinner spinnerAttribute;
	private Spinner spinnerRange;

	public String [] genderList=new String[4];
	public String [] attributeList=new String[4];
	public String [] rangeList=new String[4];

	public String title;
	public String content;
	public int gender;
	public int attribute;
	public int range;

	private ArrayAdapter<String> arr_adapter;
	private View view;
	private PersonalInformation publisher=(new MyApplication()).getPersonalInformation();
	private Calendar createTime;

	private EditText titleEditText;
	private EditText contentEditText;

	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.release_fragment, container, false);
		loadEverything();
		buttonListener();
		return view;
	}

	private void loadGenderData()//加载性别数据
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

	private void loadAttributeData()//加载属性数据
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

	private void loadRangeData()//加载范围数据
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

	private void loadEverything()//把三个下拉列表的值都加载进来
	{
		loadGenderData();
		loadAttributeData();
		loadRangeData();
	}

	public void getEverything()//得到输入的标题、内容、三个下拉框的值
	{
		titleEditText =(EditText)view.findViewById(R.id.titleEditText);
		contentEditText =(EditText)view.findViewById(R.id.contentEditText);
		title=titleEditText.getText().toString();//获取输入的标题
		content=contentEditText.getText().toString();//获取输入的内容

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

	private void buttonListener()//处理两个按钮的事件
	{
		//确认按钮
		Button releaseButton=(Button) view.findViewById(R.id.releaseButton);
		releaseButton.setOnClickListener(
				new View.OnClickListener()
				{
					public void onClick(View v)
					{
						getEverything();
						Toast.makeText(getContext(),title,Toast.LENGTH_SHORT).show();
						createTime= Calendar.getInstance();
						releaseDialog();
					}
				}
		);

		//取消按钮
		Button cancelButton=(Button) view.findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(
				new View.OnClickListener()
				{
					public void onClick(View v)
					{
						title=titleEditText.getText().toString();//获取输入的标题
						content=contentEditText.getText().toString();//获取输入的内容

						cancelDialog();
					}
				}
		);
	}

	private void releaseDialog()//点击发布后的提示框
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());//通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
		builder.setTitle("系统提示");//设置Title的内容
		builder.setMessage("发布成功！");//设置Content来显示一个信息

		//设置一个Button
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				titleEditText.setText("");
				contentEditText.setText("");

				Mission newMission=new Mission(title,content,gender,attribute,range,publisher,createTime);
				(new Mission.MissionManager()).releaseMission(getContext(),newMission);

				MainActivity mainActivity = (MainActivity)getActivity();
				mainActivity.setFragment(mainActivity.getMapFragment());//控制mainactivity当前显示的碎片
				MyTaskActivity.actionStart(mainActivity, MyTaskActivity.RELEASED_TAB);//启动一个新的活动，跳转到已发布过的界面
			}
		});
		builder.create().show();
	}

	private void cancelDialog()//点击取消后的提示框
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());//通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
		builder.setTitle("系统提示");//设置Title的内容
		builder.setMessage("真的要退出么…？");//设置Content来显示一个信息

		//设置两个Button
		builder.setPositiveButton("是", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				MainActivity mainActivity = (MainActivity)getActivity();
				mainActivity.setFragment(mainActivity.getMapFragment());//控制mainactivity当前显示的碎片
			}
		});
		builder.setNegativeButton("否", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which) {}
		});
		builder.create().show();
	}
}
