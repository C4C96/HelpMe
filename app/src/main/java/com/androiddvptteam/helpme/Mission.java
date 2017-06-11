package com.androiddvptteam.helpme;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.*;
import android.widget.Toast;

import com.androiddvptteam.helpme.Connection.AbandonConnection;
import com.androiddvptteam.helpme.Connection.CancelConnection;
import com.androiddvptteam.helpme.Connection.FinishConnection;
import com.androiddvptteam.helpme.Connection.ReceiveConnection;
import com.androiddvptteam.helpme.Connection.ReleaseConnection;
import com.androiddvptteam.helpme.MissionAttribute.MissionAttribute;

import java.net.URL;
import java.sql.Time;
import java.util.Calendar;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.io.Serializable;
import static com.androiddvptteam.helpme.Tools.netDelay;

public class Mission implements Serializable
{
    //任务的状态枚举
    public static int STATE_UNRECEIVED = 0; //未接收
    public static int STATE_DOING = 1;      //进行中
    public static int STATE_FINISHED = 2;   //已完成
    public static int STATE_CANCELED = 3;   //已取消

    private String ID;                      //任务的ID，具有唯一性

    private String title;                   //任务标题
    private String content;                 //任务内容

    private PersonalInformation publisher;  //发布者信息
    private PersonalInformation recipient;  //接收者信息，未接收则为null

    //任务的限制属性属性
    private int gender = MissionAttribute.GENDER_IDONTCARE;      //性别
    private int attribute = MissionAttribute.ATTRIBUTE_OTHER;    //标签
    private int range = MissionAttribute.RANGE0;                 //范围

	//任务的经纬度
	private double latitude;//纬度
	private double longitude;//经度

    private Calendar createTime;                //任务创建时间
    private Calendar receiveTime;               //任务被接收时间，未被接收则为null
    private Calendar finishTime;                //任务完成的时间，未完成则为null
    private Calendar cancelTime;                //任务取消的时间，未取消则为null
    private int state = STATE_UNRECEIVED;   //任务的状态

    /**
     * 新建任务时用的构造函数
     * */
    public Mission(@NonNull String              title,
                   @NonNull String              content,
				   @NonNull int                 gender,
				   @NonNull int                 attribute,
				   @NonNull int                 range,
                   @NonNull PersonalInformation publisher,
                   @NonNull Calendar            createTime,
				   @NonNull double       		latitude,
				   @NonNull double       		longitude)
    {
        this.title = title;
        this.content = content;
		this.gender = gender;
		this.attribute = attribute;
		this.range = range;
        this.publisher = publisher;
        this.createTime = createTime;
        this.ID = generateID();
		this.latitude=latitude;
		this.longitude=longitude;
    }

	/**
     * 生成一条任务（比如说服务器上拉下来的）的实例时用的构造函数
     * _(:з)∠)_长得恶心的参数列表，不过估计也就我需要用了，所以不写约束了。——CC
     * */
    public Mission(String              ID,
                   String              title,
                   String              content,
                   PersonalInformation publisher,
                   PersonalInformation recipient,
                   int                 gender,
                   int                 attribute,
                   int                 range,
                   Calendar            createTime,
                   Calendar            receiveTime,
                   Calendar            finishTime,
                   Calendar            cancelTime,
                   int                 state,
				   double 			   latitude,
				   double 			   longitude)
    {
        this.ID = ID;
        this.title = title;
        this.content = content;
        this.publisher = publisher;
        this.recipient = recipient;
        this.gender = gender;
        this.attribute = attribute;
        this.range = range;
        this.createTime = createTime;
        this.receiveTime = receiveTime;
        this.finishTime = finishTime;
        this.cancelTime = cancelTime;
        this.state = state;
		this.latitude=latitude;
		this.longitude=longitude;
    }

    /**
     * 生成唯一的ID（正常情况下不会冲突）
     * 由发布者学号+发布时间(年月日时分秒)组成
     *     (11位)     +     (4+2+2+2+2+2位) = 25位
     * （你同一秒发两个任务算我输，好吧，嗯……还是有可能的，但算发布失败吧）
     * */
    private String generateID()
    {
        StringBuilder sb = new StringBuilder();
		int 	year = createTime.get(Calendar.YEAR),
				month = createTime.get(Calendar.MONTH) + 1,//Calendar表示月份从0开始
				day = createTime.get(Calendar.DATE),
				hour = createTime.get(Calendar.HOUR),
				minute = createTime.get(Calendar.MINUTE),
				second = createTime.get(Calendar.SECOND);
		android.util.Log.d("sss",publisher.getSchoolNumber());
        sb.append(publisher.getSchoolNumber());
        sb.append(year);
        if (month < 10) sb.append(0);
		sb.append(month);
		if (day < 10) sb.append(0);
		sb.append(day);
		if (hour < 10) sb.append(0);
		sb.append(hour);
		if (minute < 10) sb.append(10);
		sb.append(minute);
		if (second < 10) sb.append(second);
		sb.append(second);
        return sb.toString();
    }


    //get方法

    public String getID() { return this.ID; }

    public String getTitle() { return this.title; }

    public String getContent() { return this.content; }

    public PersonalInformation getPublisher() { return this.publisher; }

    public PersonalInformation getRecipient() { return this.recipient; }

    public int getGender() { return this.gender; }

    public int getAttribute() { return this.attribute; }

    public int getRange() { return this.range; }

    public int getState() { return this.state; }

    public Calendar getCreateTime() { return this.createTime; }

    public Calendar getReceivedTime() { return this.receiveTime; }

	public double getLatitude(){return latitude;}

	public double getLongitude(){return longitude;}


    //set方法


    //设置任务的三个属性
    public void setMissionAttribute(int gender,int attribute,int range)
    {
        this.gender=gender;
        this.attribute=attribute;
        this.range=range;
    }

	/**
	 * 一个用于Mission发布，接收等操作的内部静态类
	 * */
	public static class MissionManager
	{
		/**
		 * 发布者发布任务
		 * @param 	context		调用该函数的context，用于Toast
		 * @param 	mission		发布的任务
		 * @return				是否成功
		 * */
		public static boolean releaseMission(@NonNull Context context,
											 @NonNull Mission mission)
		{
			ReleaseConnection connection;
			boolean result=true;
			try
			{
				connection=new ReleaseConnection(new URL("http://123.206.125.166:8080/AndroidServlet/ReleaseServlet"));
//				connection = new ReleaseConnection(new URL("http://192.168.0.3:8080/AndroidServlet/ReleaseServlet"));
				connection.setAttributes(mission);
				connection.connect();
				if (connection.connectionResult)
					result = true;
				else
				{
					result = false;
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}

//			//如果ID撞了（按发布按钮了会关掉窗口的吧，一秒发出两个也蛮厉害的），如下操作：
//			//Toast.makeText(context, "发布失败", Toast.LENGTH_SHORT).show();
//			//return false;
//			//如果因为网络原因连不到后端，如上操作，后面几个函数同理
			return result;
		}

		/**
		 * 接收者接收任务
		 * @param context		调用该函数的context，用于Toast
		 * @param mission		被接收的任务
		 * @param recipient     接收者的信息
		 * @param receivedTime  接收的时间
		 * @return              是否成功,若失败，则表明该任务已被接收或已完成或已取消
		 * */
		public boolean receive(@NonNull Context 			context,
							   @NonNull Mission 			mission,
							   @NonNull PersonalInformation recipient,
							   @NonNull Calendar            receivedTime)
		{
			//只有未被接收的任务才能被接收
					if (mission.state == STATE_UNRECEIVED)
					{
						mission.state = STATE_DOING;
						mission.recipient = recipient;
						mission.receiveTime = receivedTime;

						ReceiveConnection connection;
						boolean result=true;
						try
						{
//							connection = new ReceiveConnection(new URL("http://192.168.0.3:8080/AndroidServlet/ReceiveServlet"));
							connection=new ReceiveConnection(new URL("http://123.206.125.166:8080/AndroidServlet/ReceiveServlet"));
							connection.setAttributes(mission,recipient,receivedTime);

						connection.connect();

						if(connection.connectionResult)
							result=true;
						else
						{
							result = false;
						}
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
						if (!result)
						{
							//对本地信息回滚
							mission.state = STATE_UNRECEIVED;
							mission.recipient = null;
							mission.receiveTime = null;
							//Toast.***
							return false;
						}
						else
							return true;
			}
			else
			{
				Toast.makeText(context, "该任务已被接收", Toast.LENGTH_SHORT).show();
				return false;
			}
		}

		/**
		 * 发布者完成任务
		 * @param context		调用该函数的context，用于Toast
		 * @param mission		被完成的任务
		 * @param finishTime    完成的时间
		 * @return              是否成功,若失败，则表明该任务未被接受或已完成或已取消
		 * */
		public boolean finish(@NonNull Context context,
							  @NonNull Mission mission,
							  @NonNull Calendar finishTime)
		{
			if (mission.state == STATE_DOING)
			{
				mission.state = STATE_FINISHED;
				mission.finishTime = finishTime;

				FinishConnection connection;
				boolean result=true;
				try
				{
//					connection = new FinishConnection(new URL("http://192.168.0.3:8080/AndroidServlet/FinishServlet"));
					connection=new FinishConnection(new URL("http://123.206.125.166:8080/AndroidServlet/FinishServlet"));
					connection.setAttributes(mission,finishTime);
					connection.connect();
					if(connection.connectionResult)
						result=true;
					else
					{
						result = false;
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				if (!result)
				{
					//对本地信息回滚
					mission.state = STATE_DOING;
					mission.finishTime = null;
					//Toast.***
					return false;
				}
				else
					return true;
			}
			else
			{
				Toast.makeText(context, "该任务尚未被接受或已完成或已取消", Toast.LENGTH_SHORT).show();
				return false;
			}
		}

		/**
		 * 发布者取消任务
		 * @param context		调用该函数的context，用于Toast
		 * @param mission		被取消的任务
		 * @param cancelTime    取消的时间
		 * @return              是否成功，若失败则表明该任务已完成或已取消
		 * */
		public boolean cancel(@NonNull Context  context,
							  @NonNull Mission  mission,
							  @NonNull Calendar cancelTime)
		{
			int ori_state = mission.state;
			if (mission.state == STATE_UNRECEIVED || mission.state == STATE_DOING )
			{
				mission.state = STATE_CANCELED;
				mission.cancelTime = cancelTime;

				CancelConnection connection;
				boolean result=true;
				try
				{
//					connection = new CancelConnection(new URL("http://192.168.0.3:8080/AndroidServlet/CancelServlet"));
					connection=new CancelConnection(new URL("http://123.206.125.166:8080/AndroidServlet/CancelServlet"));
					connection.setAttributes(mission,cancelTime);
					connection.connect();
					if(connection.connectionResult)
						result=true;
					else
					{
						result = false;
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				if (!result)
				{
					//对本地信息回滚
					mission.state = ori_state;
					mission.cancelTime = null;
					//Toast.***
					return false;
				}
				else
					return true;
			}
			else
			{
				Toast.makeText(context, "任务已完成或已取消", Toast.LENGTH_SHORT).show();
				return false;
			}
		}

		/**
		 * 接收者放弃接收任务
		 * @param context		调用该函数的context，用于Toast
		 * @param mission		被放弃的任务
		 * @return      是否成功，若失败则表明该任务未被接收或已完成或已取消
		 * */
		public boolean abandon(@NonNull Context context,
							   @NonNull Mission mission
							   /*@NonNull Calendar abandonTime*/)
		{
			PersonalInformation ori_recipient = mission.recipient;
			Calendar ori_receiveTime = mission.receiveTime;
			if (mission.state == STATE_DOING)
			{
				mission.state = STATE_UNRECEIVED;
				mission.recipient = null;
				mission.receiveTime = null;

				AbandonConnection connection;
				boolean result=true;
				try
				{
//					connection=new AbandonConnection(new URL("http://192.168.0.3:8080/AndroidServlet/AbandonServlet"));
					connection=new AbandonConnection(new URL("http://123.206.125.166:8080/AndroidServlet/AbandonServlet"));
					connection.setAttributes(mission/*,abandonTime*/);
					connection.connect();
					if(connection.connectionResult)
						result=true;
					else
					{
						result = false;
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				if (!result)
				{
					//对本地信息回滚
					mission.state = STATE_DOING;
					mission.recipient = ori_recipient;
					mission.receiveTime = ori_receiveTime;
					//Toast.***
					return false;
				}
				else
					return true;
			}
			else
			{
				Toast.makeText(context, "放弃该任务失败", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
	}
}
