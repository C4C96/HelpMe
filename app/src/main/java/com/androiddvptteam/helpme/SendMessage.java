package com.androiddvptteam.helpme;

import com.androiddvptteam.helpme.Connection.SendMessageConnection;
import java.net.URL;
import java.util.Calendar;

/**
 * 发送消息
* */
public class SendMessage
{
    public SendMessage(String ID,String pn,String rn,String state,String t)
    {
        final String id=ID;//任务ID
        final String publisherNumber=pn;//发布者的学号
        final String recipientNumber=rn;//接收者的学号
        final String st=state;//任务状态
        final String title=t;//标题
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Calendar c=Calendar.getInstance();
                SendMessageConnection connection;
                String year=String.valueOf(c.get(Calendar.YEAR));
                String month=String.valueOf(c.get(Calendar.MONTH)+1);
                if(c.get(Calendar.MONTH)+1<10)
                    month="0"+String.valueOf(c.get(Calendar.MONTH)+1);
                String day=String.valueOf(c.get(Calendar.DATE));
                if(c.get(Calendar.DATE)<10)
                    day="0"+String.valueOf(c.get(Calendar.DATE));
                String hour=String.valueOf(c.get(Calendar.HOUR_OF_DAY));
                if(c.get(Calendar.HOUR_OF_DAY)<10)
                    hour="0"+String.valueOf(c.get(Calendar.HOUR_OF_DAY));
                String minute=String.valueOf(c.get(Calendar.MINUTE));
                if(c.get(Calendar.MINUTE)<10)
                    minute="0"+String.valueOf(c.get(Calendar.MINUTE));
                String second=String.valueOf(c.get(Calendar.SECOND));
                if(c.get(Calendar.SECOND)<10)
                    second="0"+String.valueOf(c.get(Calendar.SECOND));
                try
                {
                    connection = new SendMessageConnection(new URL("http://192.168.0.3:8080/AndroidServlet/SendMessageServlet"));
//                    connection=new SendMessageConnection(new URL("http://123.206.125.166:8080/AndroidServlet/SendMessageServlet"));
                    connection.setAttributes(id,publisherNumber,recipientNumber,st,title,year,month,day,hour,minute,second);
                    connection.connect();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        ).start();
    }
}
