package com.androiddvptteam.helpme.MissionAttribute;

/**
 * Created by 13759 on 2017/4/23.
 */

//各类属性均用int表示，获取下拉框内容时只用获取用户选中的那一栏的标号，传到mission类里再写入数据库
//就比如某用户选了男性，则下拉框获取到的数字是0，然后传到mission里面的gender里。
// 接收者选择任务的时候，如果筛选男性，就把gender==GENDER_MALE的人筛选出来
public class MissionAttribute
{
    public static final int GENDER_MALE = 0;
    public static final int GENDER_FEMALE = 1;
    public static final int GENDER_OTHER = 2;
    public static final int GENDER_IDONTCARE = 3;

    public static final int ATTRIBUTE_BRING = 0;
    public static final int ATTRIBUTE_TAKE = 1;
    public static final int ATTRIBUTE_BUY = 2;
    public static final int ATTRIBUTE_OTHER = 3;

    public static final int RANGE0 = 0;
    public static final int RANGE1 = 1;
    public static final int RANGE2 = 2;
    public static final int RANGE3 = 3;
}
