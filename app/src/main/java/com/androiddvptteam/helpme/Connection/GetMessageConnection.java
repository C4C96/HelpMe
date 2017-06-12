package com.androiddvptteam.helpme.Connection;

import com.androiddvptteam.helpme.Message;
import com.androiddvptteam.helpme.Mission;
import com.androiddvptteam.helpme.PersonalInformation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static android.R.drawable.ic_menu_info_details;

public class GetMessageConnection extends URLConnection
{
    public HttpURLConnection urlConnection = null;
    public URL url = null;

    public String myNum="";
    public List<Message> message=new LinkedList<>();
    public boolean listResult=false;

    public GetMessageConnection(URL url)
    {
        super(url);
        this.url=url;
    }

    public void setMyNum(String s)
    {
        this.myNum=s;
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

            //得到服务端的返回码是否连接成功，然后接收服务器返回的数据
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStream in = urlConnection.getInputStream();//客户端接收服务端返回来的数据是urlConnection.getInputStream()输入流来读取
                BufferedReader br = new BufferedReader(new InputStreamReader(in));//高效缓冲流包装它，这里用的是字节流来读取数据的
                String str = null;
                StringBuffer buffer = new StringBuffer();//用来接收数据的StringBuffer对象

                while ((str = br.readLine()) != null)
                    buffer.append(str);
                in.close();
                br.close();

                if(buffer.toString()!=null&&buffer.toString().length()!=0&&!"".equals(buffer.toString()))
                {
                    JSONObject rjson = new JSONObject(buffer.toString());
                    String s1=new String(rjson.getString("list").getBytes(),"UTF-8");

                    Gson gson = new Gson();

                    //得到List<Map<String,Object>>
                    List<Map<String,Object>> list = gson.fromJson( s1, new TypeToken<List<Map<String,Object>>>(){}.getType());//返回任务的信息
                    System.out.println("消息列表："+list);

                    if (list == null || list.size() < 1)
                    {//判断listForMission中有没有数据，如果没有则返回false
                        listResult = false;
                    }
                    else
                    {
                        listResult = true;
                        for (int i = 0; i < list.size(); i++)
                        {//对接收的数据进行遍历打印
                            if(list.get(i).get("publisherNum").equals(myNum)||list.get(i).get("recipientNum").equals(myNum))
                            {
                                Message m=new Message();
                                String M="";
                                if(list.get(i).get("state").equals("确认完成"))
                                {
                                    if(list.get(i).get("publisherNum").equals(myNum))//当前用户是发布者
                                        M="您已确认“"+(String) list.get(i).get("title")+"”的任务被完成了";
                                    if(list.get(i).get("recipientNum").equals(myNum))//当前用户是接收者
                                        M="学号为"+(String) list.get(i).get("publisherNum")+"的同学已确认任务“"+(String) list.get(i).get("title")+"”完成";
                                }
                                else if(list.get(i).get("state").equals("接收"))
                                {
                                    if(list.get(i).get("publisherNum").equals(myNum))//当前用户是发布者
                                        M="您的“"+(String) list.get(i).get("title")+"”任务已经被学号为"+(String) list.get(i).get("recipienNum")+"的同学接收了";
                                    if(list.get(i).get("recipientNum").equals(myNum))//当前用户是接收者
                                        M="您已接受了“"+(String) list.get(i).get("title")+"”这个任务";
                                }
                                else if(list.get(i).get("state").equals("放弃"))
                                {
                                    if(list.get(i).get("publisherNum").equals(myNum))//当前用户是发布者
                                        M="您的“"+(String) list.get(i).get("title")+"”任务已经被学号为"+(String) list.get(i).get("recipienNum")+"的同学放弃了";
                                    if(list.get(i).get("recipientNum").equals(myNum))//当前用户是接收者
                                        M="您已放弃了"+(String) list.get(i).get("title")+"”这个任务";
                                }
                                else if(list.get(i).get("state").equals("取消"))
                                    M="您的“"+(String) list.get(i).get("title")+"”任务已经被您自己取消了";
                                else if(list.get(i).get("state").equals("发送成功"))
                                    M="您的“"+(String) list.get(i).get("title")+"”任务已经成功发送";
                                m=new Message(ic_menu_info_details,
                                        "新消息",
                                        M,
                                        (String) list.get(i).get("year"),
                                        (String) list.get(i).get("month"),
                                        (String) list.get(i).get("day"),
                                        (String) list.get(i).get("hour"),
                                        (String) list.get(i).get("minute"),
                                        (String) list.get(i).get("second"));
                                message.add(m);
                            }
                        }
                    }
                    if(listResult)
                        setMessage(message);
                    System.out.println("get!"+message.get(0).getMessageTitle());
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        urlConnection.disconnect();//使用完关闭TCP连接，释放资源
    }
    public List<Message> getMessage()
    {
        return message;
    }

    public void setMessage(List<Message> message)
    {
        this.message = message;
    }
}
