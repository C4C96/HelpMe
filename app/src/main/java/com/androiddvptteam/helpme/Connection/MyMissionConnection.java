package com.androiddvptteam.helpme.Connection;

import com.androiddvptteam.helpme.Mission;
import com.androiddvptteam.helpme.PersonalInformation;
import net.sf.json.JSONArray;
import java.io.*;
import java.net.*;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/*
* 读取任务
* */

public class MyMissionConnection extends URLConnection
{
    public HttpURLConnection urlConnection = null;
    public URL url = null;

    private PersonalInformation publisher;
    public List<Mission> missionsList;//接收任务的list

    public boolean listResult;
    public  boolean connectionResult;//判断连接结果是否正常

    public MyMissionConnection(URL url)
    {
        super(url);
        this.url=url;
    }

    public void setPublisher(PersonalInformation p)
    {
        this.publisher=p;
    }

    public void connect() throws IOException
    {
        try
        {
            urlConnection = (HttpURLConnection) url.openConnection();//打开http连接
            urlConnection.setConnectTimeout(5000);//连接的超时时间
            urlConnection.setUseCaches(false);//不使用缓存
            urlConnection.setInstanceFollowRedirects(true);//是成员函数，仅作用于当前函数,设置这个连接是否可以被重定向
            urlConnection.setReadTimeout(5000);//响应的超时时间
            urlConnection.setDoInput(true);//设置这个连接是否可以写入数据
            urlConnection.setDoOutput(true);//设置这个连接是否可以输出数据
            urlConnection.setRequestMethod("POST");//设置请求的方式
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");//设置消息的类型
            urlConnection.connect();// 连接，从上述至此的配置必须要在connect之前完成，实际上它只是建立了一个与服务器的TCP连接

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                connectionResult = true;
                InputStream in = urlConnection.getInputStream();//客户端接收服务端返回来的数据是urlConnection.getInputStream()输入流来读取
                BufferedReader br = new BufferedReader(new InputStreamReader(in));//高效缓冲流包装它，这里用的是字节流来读取数据的
                String str = null;
                StringBuffer buffer = new StringBuffer();//用来接收数据的StringBuffer对象

                while ((str = br.readLine()) != null)
                {
                    //BufferedReader特有功能，一次读取一行数据
                    buffer.append(str);
                }
                in.close();
                br.close();
                JSONArray arr = JSONArray.fromObject(buffer.toString());
                //missionsList = arr.getJSONArray(0);

                //从JSONArray对象中得到第一个值，里面是List<Map<String,Object>>
                List<Map<String, Object>> listForMission = (List<Map<String, Object>>) arr.getJSONArray(0);
                List<Map<String, Object>> listForPerson = (List<Map<String, Object>>) arr.getJSONArray(1);
                if (listForMission == null || listForMission.size() < 1)
                {//判断listForMission中有没有数据，如果没有则返回false
                    listResult = false;
                }
                else
                {
                    listResult = true;
                    for (int i = 0; i < listForMission.size(); i++)
                    {//对接收的数据进行遍历打印
                        PersonalInformation recipient = new PersonalInformation(
                                (String) listForPerson.get(i).get("name"),
                                (String) listForPerson.get(i).get("schoolNum"),
                                (int) listForPerson.get(i).get("gender"),
                                (String) listForPerson.get(i).get("departmentName"),
                                (String) listForPerson.get(i).get("introduction")
                        );

                        Mission mission = new Mission(
                                (String) listForMission.get(i).get("missionID"),
                                (String) listForMission.get(i).get("title"),
                                (String) listForMission.get(i).get("content"),
                                (PersonalInformation) publisher,
                                (PersonalInformation) recipient,
                                (int) listForMission.get(i).get("gender"),
                                (int) listForMission.get(i).get("attribute"),
                                (int) listForMission.get(i).get("range"),
                                (Calendar) listForMission.get(i).get("createTime"),
                                (Calendar) listForMission.get(i).get("receiveTime"),
                                (Calendar) listForMission.get(i).get("finishTime"),
                                (Calendar) listForMission.get(i).get("cancelTime"),
                                (int) listForMission.get(i).get("state")
                        );

                        missionsList.add(mission);
                    }
                }
            }
            else
                connectionResult = false;
        }
        catch(Exception e)
        {
            listResult = false;
            connectionResult = false;
            e.printStackTrace();
        }
        urlConnection.disconnect();//使用完关闭TCP连接，释放资源
    }

    public List<Mission> getList()
    {
        return this.missionsList;
    }
}