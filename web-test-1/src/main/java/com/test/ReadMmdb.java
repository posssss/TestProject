package com.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.maxmind.db.Reader;
import com.test.pojo.IpLocation;

import java.io.File;
import java.net.InetAddress;
import java.util.Arrays;

/**
 *
 **/
public class ReadMmdb {

    private static Reader getDatabase(String location) throws Exception{
        File database = new File(location);
        return new Reader(database);
    }

    private static IpLocation getIpLocation(Reader reader, String ip) throws Exception {
        InetAddress address = InetAddress.getByName(ip);
        JsonNode response = reader.get(address);
        IpLocation ipLocation = new IpLocation();
        String continent = null;
        if (response.get("continent") != null && response.get("continent").get("names") != null &&
                response.get("continent").get("names").get("zh-CN") != null) {
            continent = response.get("continent").get("names").get("zh-CN").toString();
        }
        ipLocation.setContinent(continent == null?null:continent.substring(1,continent.length()-1));
        String country = null;
        if (response.get("country") != null && response.get("country").get("names") != null &&
                response.get("country").get("names").get("zh-CN") != null) {
            country = response.get("country").get("names").get("zh-CN").toString();
            country = country == null?null:country.substring(1,country.length()-1);
        }
        ipLocation.setCountry(country);
        String city;
        if (response.get("city") != null && response.get("city").get("names") != null &&
                response.get("city").get("names").get("zh-CN") != null) {
            city = response.get("city").get("names").get("zh-CN").toString();
        } else {
            city = " " + country + "某城市 ";
        }
        ipLocation.setCity(city == null?null:city.substring(1,city.length()-1));
        ipLocation.setLatitude(Double.valueOf(response.get("location").get("latitude").toString()));
        ipLocation.setLongitude(Double.valueOf(response.get("location").get("longitude").toString()));
        ipLocation.setRadius(Integer.valueOf(response.get("location").get("accuracy_radius").toString()));
        return ipLocation;
    }

    public static void main(String[] args) throws Exception {
        try (Reader reader = getDatabase("D://GeoLite2-City.mmdb")){
//            IpLocation ipLocation = getIpLocation(reader,"123.207.162.60");
//            IpLocation ipLocation = getIpLocation(reader,"52.69.166.231");
//            IpLocation ipLocation2 = getIpLocation(reader,"220.194.224.145");
//            IpLocation ipLocation2 = getIpLocation(reader,"216.58.200.238");
//            IpLocation ipLocation2 = getIpLocation(reader,"108.59.2.24");
            IpLocation ipLocation2 = getIpLocation(reader,"47.94.234.157");
//            System.out.println(ipLocation);
            System.out.println(ipLocation2);
//            System.out.println(ipLocation.equals(ipLocation2));
//            System.out.println(Arrays.toString(IpLocation.getAround(ipLocation.getLatitude(), ipLocation.getLongitude(), ipLocation.getRadius())));
        }
    }


}
