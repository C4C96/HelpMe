package com.androiddvptteam.helpme.Connection;

import com.androiddvptteam.helpme.Mission;
import org.json.JSONObject;
import java.io.*;
import java.net.*;
import java.util.Calendar;

/*
* 发布者发布任务
* */

public class LoginConnection extends URLConnection
{
    public HttpURLConnection urlConnection = null;
    public URL url = null;

    private String ID;
    private String password;
    public String result;

    public  boolean connectionResult;//判断连接结果是否正常

    public LoginConnection(URL url)
    {
        super(url);
        this.url=url;
    }

    public void setAttributes(String id, String password)
    {
        this.ID=id;
        this.password=password;
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
            json.put("ID", URLEncoder.encode(this.ID, "UTF-8"));
            json.put("password", URLEncoder.encode(this.password, "UTF-8"));

            String jsonToString = json.toString();//把JSON对象按JSON的编码格式转换为字符串

            //字符流写入数据
            OutputStream out = urlConnection.getOutputStream();//输出流，用来发送请求，http请求实际上直到这个函数里面才正式发送出去
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));//创建字符流对象并用高效缓冲流包装它，便获得最高的效率,发送的是字符串推荐用字符流，其它数据就用字节流
            bw.write(jsonToString);//把json字符串写入缓冲区中
            bw.flush();//刷新缓冲区，把数据发送出去，这步很重要
            out.close();
            bw.close();//使用完关闭

            //得到服务端的返回码是否连接成功，然后接收服务器返回的数据
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                connectionResult=true;
                //字符流读取服务端返回的数据
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

                result = rjson.getString("result");//从json对象中得到相应key的值
                setResult(result);
            }
            else
                connectionResult=false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        urlConnection.disconnect();//使用完关闭TCP连接，释放资源
    }
    private void setResult(String s)
    {
        this.result=s;
    }
    public String getResult()
    {
        return this.result;
    }
}