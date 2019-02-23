package com.androiddvptteam.helpme.Connection;

import com.androiddvptteam.helpme.Mission;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

/**
 * Created by 13759 on 2017/6/9.
 */

public class SendMessageConnection extends URLConnection
{
    public HttpURLConnection urlConnection = null;
    public URL url = null;

    private String ID;//任务ID
    private String publisherNumber;//发布者的学号
    private String recipientNumber;//接收者的学号
    private String state;//任务状态
    private String title;//任务的标题
    private String year;
    private String month;
    private String day;
    private String hour;
    private String minute;
    private String second;

    public boolean connectionResult;//判断连接结果是否正常

    public SendMessageConnection(URL url)
    {
        super(url);
        this.url = url;
    }

    public void setAttributes(String ID,
                              String pn,
                              String rn,
                              String state,
                              String title,
                              String year,
                              String month,
                              String day,
                              String hour,
                              String minute,
                              String second)
    {
        this.ID = ID;
        this.publisherNumber = pn;
        this.recipientNumber = rn;
        this.state = state;
        this.title = title;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
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
            json.put("ID", this.ID);
            json.put("publisherNumber", this.publisherNumber);
            json.put("recipientNumber", this.recipientNumber);
            json.put("state", this.state);
            json.put("title", this.title);
            json.put("year", this.year);
            json.put("month", this.month);
            json.put("day", this.day);
            json.put("hour", this.hour);
            json.put("minute", this.minute);
            json.put("second", this.second);

            System.out.println("send!" + this.title);

            String jsonToString = json.toString();//把JSON对象按JSON的编码格式转换为字符串

            //字符流写入数据
            OutputStream out = urlConnection.getOutputStream();//输出流，用来发送请求，http请求实际上直到这个函数里面才正式发送出去
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));//创建字符流对象并用高效缓冲流包装它，便获得最高的效率,发送的是字符串推荐用字符流，其它数据就用字节流
            bw.write(jsonToString);//把json字符串写入缓冲区中
            bw.flush();//刷新缓冲区，把数据发送出去，这步很重要
            out.close();
            bw.close();//使用完关闭

            //得到服务端的返回码是否连接成功，然后接收服务器返回的数据
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                connectionResult = true;
            else
                connectionResult = false;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        urlConnection.disconnect();//使用完关闭TCP连接，释放资源
    }
}
