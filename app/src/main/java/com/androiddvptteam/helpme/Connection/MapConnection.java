package com.androiddvptteam.helpme.Connection;

import org.json.JSONObject;
import java.io.*;
import java.net.*;

public class MapConnection extends URLConnection
{
    public HttpURLConnection urlConnection = null;
    public URL url = null;

    public MapConnection(URL url)
    {
        super(url);
    }

    public void connect() throws IOException
    {
        try
        {
            url = new URL("...");//每个servlet的地址，暂时还没改
            urlConnection = (HttpURLConnection) url.openConnection();//打开http连接
            urlConnection.setConnectTimeout(3000);//连接的超时时间
            urlConnection.setUseCaches(false);//不使用缓存
            urlConnection.setInstanceFollowRedirects(true);//是成员函数，仅作用于当前函数,设置这个连接是否可以被重定向
            urlConnection.setReadTimeout(3000);//响应的超时时间
            urlConnection.setDoInput(true);//设置这个连接是否可以写入数据
            urlConnection.setDoOutput(true);//设置这个连接是否可以输出数据
            urlConnection.setRequestMethod("POST");//设置请求的方式
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");//设置消息的类型
            urlConnection.connect();// 连接，从上述至此的配置必须要在connect之前完成，实际上它只是建立了一个与服务器的TCP连接

            JSONObject json = new JSONObject();//创建json对象

            //写需要提交到服务器的数据
            //json.put("name", URLEncoder.encode("name", "UTF-8"));//使用URLEncoder.encode对特殊和不可见字符进行编码
            //json.put("password", URLEncoder.encode("password", "UTF-8"));//把数据put进json对象中

            String jsonstr = json.toString();//把JSON对象按JSON的编码格式转换为字符串

            //------------字符流写入数据------------
            OutputStream out = urlConnection.getOutputStream();//输出流，用来发送请求，http请求实际上直到这个函数里面才正式发送出去
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));//创建字符流对象并用高效缓冲流包装它，便获得最高的效率,发送的是字符串推荐用字符流，其它数据就用字节流
            bw.write(jsonstr);//把json字符串写入缓冲区中
            bw.flush();//刷新缓冲区，把数据发送出去，这步很重要
            out.close();
            bw.close();//使用完关闭
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            urlConnection.disconnect();//使用完关闭TCP连接，释放资源
        }
    }
}
