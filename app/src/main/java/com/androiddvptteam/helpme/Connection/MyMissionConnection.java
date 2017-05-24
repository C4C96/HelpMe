package com.androiddvptteam.helpme.Connection;

import com.androiddvptteam.helpme.Mission;
import com.androiddvptteam.helpme.PersonalInformation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sf.json.JSONArray;

import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/*
* 读取任务
* 只用于下载
* */

public class MyMissionConnection extends URLConnection
{
    public HttpURLConnection urlConnection = null;
    public URL url = null;

    private PersonalInformation publisher;
    private int state;
    public List<Mission> missionsList;//接收任务的list

    public boolean listResult;
    public  boolean connectionResult;//判断连接结果是否正常

    public MyMissionConnection(URL url)
    {
        super(url);
        this.url=url;
    }

    public void setAttributes(PersonalInformation p,int state)
    {
        this.publisher=p;
        this.state=state;
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

            JSONObject json = new JSONObject();//创建json对象
            //使用URLEncoder.encode对特殊和不可见字符进行编码
            // 把数据put进json对象中
            json.put("schoolNumber", this.publisher.getSchoolNumber());
            json.put("state", this.state);

            String jsonToString = json.toString();//把JSON对象按JSON的编码格式转换为字符串

            //字符流写入数据
            OutputStream out = urlConnection.getOutputStream();//输出流，用来发送请求，http请求实际上直到这个函数里面才正式发送出去
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));//创建字符流对象并用高效缓冲流包装它，便获得最高的效率,发送的是字符串推荐用字符流，其它数据就用字节流
            bw.write(jsonToString);//把json字符串写入缓冲区中
            bw.flush();//刷新缓冲区，把数据发送出去，这步很重要
            out.close();
            bw.close();//使用完关闭

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

                    JSONObject rjson = new JSONObject(buffer.toString());
                    String s1=new String(rjson.getString("listForMission").getBytes(),"UTF-8");
                    String s2=new String(rjson.getString("listForPerson").getBytes(),"UTF-8");

                    Gson gson = new Gson();

                    //得到List<Map<String,Object>>
                    List<Map<String, Object>> listForMission = gson.fromJson(s1,
                            new TypeToken<List<Map<String, Object>>>()
                            {
                            }.getType());//返回接收者的信息
                    List<Map<String, Object>> listForPerson = gson.fromJson(s2,
                            new TypeToken<List<Map<String, Object>>>()
                            {
                            }.getType());//返回任务信息

                if (listForMission == null || listForMission.size() < 1)
                {//判断listForMission中有没有数据，如果没有则返回false
                    listResult = false;
                }
                else
                {
                    listResult = true;
                    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    for (int i = 0; i < listForMission.size(); i++)
                    {//对接收的数据进行遍历打印
                        PersonalInformation recipient = new PersonalInformation(
                                (String) listForPerson.get(i).get("name"),
                                (String) listForPerson.get(i).get("schoolNum"),
                                (int) listForPerson.get(i).get("gender"),
                                (String) listForPerson.get(i).get("departmentName"),
                                (String) listForPerson.get(i).get("introduction")
                        );

                        java.util.Date dateCreate=sdf.parse(listForMission.get(i).get("createTime").toString());
                        java.util.Date dateReceive=sdf.parse(listForMission.get(i).get("receiveTime").toString());
                        java.util.Date dateFinish=sdf.parse(listForMission.get(i).get("finishTime").toString());
                        java.util.Date dateCancel=sdf.parse(listForMission.get(i).get("cancelTime").toString());
                        Calendar cCreate=Calendar.getInstance();
                        Calendar cReceive=Calendar.getInstance();
                        Calendar cFinish=Calendar.getInstance();
                        Calendar cCancel=Calendar.getInstance();
                        cCreate.setTime(dateCreate);
                        cReceive.setTime(dateReceive);
                        cFinish.setTime(dateFinish);
                        cCancel.setTime(dateCancel);
                        Mission mission = new Mission(
                                (String) listForMission.get(i).get("missionID"),
                                (String) listForMission.get(i).get("title"),
                                (String) listForMission.get(i).get("content"),
                                (PersonalInformation) publisher,
                                (PersonalInformation) recipient,
                                (int) listForMission.get(i).get("gender"),
                                (int) listForMission.get(i).get("attribute"),
                                (int) listForMission.get(i).get("range"),
                                cCreate,
                                cReceive,
                                cFinish,
                                cCancel,
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