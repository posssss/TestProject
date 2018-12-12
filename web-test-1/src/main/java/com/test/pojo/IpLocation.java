package com.test.pojo;

/**
 *
 *  IP 地址信息只需要拿对自己有用的那部分
 *
 *  1. 所属大陆
 *  2. 所属国家
 *  3. 所属城市
 *  4. 经纬度
 *  5. 经纬度 半径
 *
 *  还应该具备查询
 *      1. 如何确定两个IP位置冲突？
 *          以第一个为准，查看是否在最小到最大的经纬度之间
 *
 *
 **/
public class IpLocation {
    private String continent;
    private String country;
    private String city;
    private Double latitude;    // 纬度
    private Double longitude;   // 经度
    private Integer radius;     // 精度半径

    public String getContinent() {
        return continent;
    }
    public void setContinent(String continent) {
        this.continent = continent;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public Integer getRadius() {
        return radius;
    }
    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    /**
     * 是否相同
     *  在这里的意思也就是说当前这个 ip 的地理位置是否与其他的重叠
     *  所以这里取精度应该是取 obj 为主的？
     *
     *  ！！！！谁发起的判断就应该以谁为主！！！！
     *
     */
    @Override
    public boolean equals(Object obj) {
        /*
         * 进行判断之前首先确保几件事情，1. 双方的经纬度都有，
         */
        if (!(obj instanceof IpLocation)) {
            return false;
        }
        IpLocation toBeConfirmed = (IpLocation)obj;
        if (latitude == null || longitude == null || radius ==null ||
                toBeConfirmed.getLatitude() == null || toBeConfirmed.getLongitude() == null) {
            return false;
        }
        double[] iplocations = getAround(latitude,longitude,radius);
        /*
         * 如果
         *  {minLat,minLng,maxLat,maxLng}
         */
        return (toBeConfirmed.getLatitude() >= iplocations[0] && toBeConfirmed.getLatitude() <= iplocations[2]) &&
                (toBeConfirmed.getLongitude() >= iplocations[1] && toBeConfirmed.getLongitude() <= iplocations[3]);
    }

    // ===========================================================================================


    @Override
    public String toString() {
        return "IpLocation{" +
                "continent='" + continent + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", radius=" + radius +
                '}';
    }

    private static final double PI = 3.14159265;
    private static final double EARTH_RADIUS = 6378137;
    private static final double RAD = Math.PI / 180.0;

    //@see http://snipperize.todayclose.com/snippet/php/SQL-Query-to-Find-All-Retailers-Within-a-Given-Radius-of-a-Latitude-and-Longitude--65095/
    //The circumference of the earth is 24,901 miles.
    //24,901/360 = 69.17 miles / degree
    /**
     * @param raidus 单位米
     * return minLat,minLng,maxLat,maxLng
     */
    public static double[] getAround(double lat,double lon,int raidus){

        Double latitude = lat;
        Double longitude = lon;

        Double degree = (24901*1609)/360.0;
        double raidusMile = raidus;

        Double dpmLat = 1/degree;
        Double radiusLat = dpmLat*raidusMile;
        Double minLat = latitude - radiusLat;
        Double maxLat = latitude + radiusLat;

        Double mpdLng = degree*Math.cos(latitude * (PI/180));
        Double dpmLng = 1 / mpdLng;
        Double radiusLng = dpmLng*raidusMile;
        Double minLng = longitude - radiusLng;
        Double maxLng = longitude + radiusLng;
        //System.out.println("["+minLat+","+minLng+","+maxLat+","+maxLng+"]");
        return new double[]{minLat,minLng,maxLat,maxLng};
    }

    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
     */
    public static double getDistance(double lng1, double lat1, double lng2, double lat2)
    {
        double radLat1 = lat1*RAD;
        double radLat2 = lat2*RAD;
        double a = radLat1 - radLat2;
        double b = (lng1 - lng2)*RAD;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    public static void main(String[] args){
        Double lat1 = -34.1551;
        Double lon1 = 138.7482;

        int radius = 1000;

        double [] d = getAround(lat1,lon1,radius);

        System.out.println("最小纬度 min latitude : " + d[0]);
        System.out.println("最大纬度 max latitude : " + d[2]);

        System.out.println("最小经度 min longitude : " + d[1]);
        System.out.println("最大经度 max longitude : " + d[3]);

        //911717.0   34.264648,108.952736,39.904549,116.407288
        double dis = getDistance(108.952736,34.264648,116.407288,39.904549);
        System.out.println(dis);
    }

}
