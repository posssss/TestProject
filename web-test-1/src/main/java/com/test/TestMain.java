package com.test;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import static java.lang.Math.PI;

public class TestMain {

    public static void main(String[] args) {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(new Date());
//        int month = cal.get(Calendar.MONTH); //注意月份是从0开始的,比如当前7月，获得的month为6
//
//        System.out.println(month);

//        TestMain main = new TestMain();
//        Date date = new Date(System.currentTimeMillis());
//        System.out.println(main.getDaysOfMonth(date));
//        System.out.println(main.getDaysByYearMonth(2));

//        List<Date> list = getMonthBeginAndEndTime(new Date(System.currentTimeMillis()));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println(sdf.format(list.get(0)));
//        System.out.println(sdf.format(list.get(1)));

//        Calendar a =Calendar.getInstance();
//        a.setTime(new Date(System.currentTimeMillis()));
//        System.out.println(a.get(Calendar.YEAR) + "-" + 1 + "-1");

        System.out.println(getDay(new Date(System.currentTimeMillis()), 7));

    }

    /**
     * 得到某一日期前几天的日期，返回list集合
     */
    public static List<String> getDay(Date date, int days){
        List<String> list = new ArrayList<>(days);
        SimpleDateFormat sdfMonth = new SimpleDateFormat("MM-dd");
        for (int i=0; i<days; i++) {
            list.add(0, sdfMonth.format(dayAddNum(date, -i)));
        }
        return list;
    }

    /**
     * 日期：年数加减
     */
    public static Date yearAddNum(Date time, Integer num) {
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Date date = format.parse(time);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.add(Calendar.YEAR, num);
        Date newTime = calendar.getTime();
        return newTime;
    }

    /**
     * 日期：月数加减
     */
    public static Date monthAddNum(Date time, Integer num){
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Date date = format.parse(time);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.add(Calendar.MONTH, num);
        Date newTime = calendar.getTime();
        return newTime;
    }

    /**
     * 日期：天数加减
     */
    public static Date dayAddNum(Date time, Integer num){
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Date date = format.parse(time);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.add(Calendar.DAY_OF_MONTH, num);
        Date newTime = calendar.getTime();
        return newTime;
    }

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static long oneDay = 60 * 60 * 24 * 1000;

    public static List<Date> getMonthBeginAndEndTime(Date date) throws ParseException {
        List<Date> list = new ArrayList<>(2);
        Date currentDate = sdf.parse(new java.sql.Date(date.getTime()).toString());

        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH) + 1;

        int monthDays = getDaysOfMonth(currentDate);
        String currentMonthString  = currentYear + "-" + currentMonth + "-1";
        Date beginDate = sdf.parse(currentMonthString);
        Date endDate = new Date(beginDate.getTime() + (oneDay * monthDays));
        list.add(beginDate);
        list.add(endDate);
        return list;
    }
    // ------------------------------------------------------------------------------

    public static int getDaysOfMonth(Date date) {
         Calendar calendar =Calendar.getInstance();
         calendar.setTime(date);
         return calendar.getActualMaximum(Calendar.DAY_OF_MONTH) ;
    }
    public int getDaysByYearMonth(int month) {
        Calendar a =Calendar.getInstance();
        //a.set(Calendar.YEAR,year); 
        a.set(Calendar.MONTH, month- 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE,-1);
        return a.get(Calendar.DATE);
    }

    // ------------------------------------------------------------------------------


    public   void main1(String[] args) {

        // 所有的城市存在一个list中
        List<City> cityList = new ArrayList();

        String strJson = readToString("D://city.json");
        JSONArray jsonArray = JSONObject.parseArray(strJson);
        Map<String,City> map = new HashMap<>();
        int a = 1;
        for (int i=0;i<jsonArray.size();i++){
            JSONArray jso = JSONObject.parseArray(jsonArray.get(i).toString());
            if (map.get(jso.get(1)) == null) {
                City city = new City();
                city.setId(a++);
                city.setCityName(jso.get(1).toString());
                city.setCityCountry(jso.get(2).toString());
                city.setLongitude(Double.valueOf(jso.get(3).toString()));
                city.setLatitude(Double.valueOf(jso.get(4).toString()));
                map.put(jso.get(1).toString(),city);
                a++;
                cityList.add(city);
            }
        }

        /*

        现在得到了所有的城市、经纬度
        下一步是绘制出来
        绘制首先要讲数将数据做成json
        将list 转换成 json

         */

        String cityJson = JSONObject.toJSONString(cityList);
        System.out.println(cityJson);



    }














    /**
     * 读取filePath的文件，将文件中的数据按照行读取到String数组中
     * @param filePath    文件的路径
     * @return            文件中一行一行的数据
     */
    public static String readToString(String filePath)
    {
        File file = new File(filePath);
        Long filelength = file.length(); // 获取文件长度
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
            return new String(filecontent,"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
class City{
    private Integer id;
    private String cityName;
    private String cityCountry;
    private Double longitude;   // 经度
    private Double latitude;    // 纬度

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    public String getCityCountry() {
        return cityCountry;
    }
    public void setCityCountry(String cityCountry) {
        this.cityCountry = cityCountry;
    }
    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}