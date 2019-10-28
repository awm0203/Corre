package com.example.corre;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Run {
    String id;
    String date;
    double distance;
    String time;
    double rate;

    public Run(){

    }

    public Run(String id, double distance, String time, double rate) {
        Calendar c = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        this.id = id;
        this.date = df.format(c.getTime());
        this.distance = distance;
        this.time = time;
        this.rate = rate;

    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public double getRate() {
        return rate;
    }

    public double getDistance() {
        return distance;
    }

    public String doubleToStr(double d){
        return Double.toString(d);
    }

    public static double newtime(String oldTime) {
        //Hours
        String time = oldTime.substring(0,2);
        double total = Double.parseDouble(time);
        //Minutes
        time = oldTime.substring(3,5);
        double m = Double.parseDouble(time);
        total+= m/60;
        //Seconds
        time = oldTime.substring(6,8);
        double s = Double.parseDouble(time);
        total+= s / 3600;
        //Milliseconds
        time = oldTime.substring(10,12);
        double mm = Double.parseDouble(time);
        total += mm/(3600*60);
        DecimalFormat df = new DecimalFormat("#.##");
        total = Double.valueOf(df.format(total));
        return total;
    }

    public static double newtimeMinute(String oldTime) {
        //Hours
        String time = oldTime.substring(0,2);
        double total = Double.parseDouble(time)*60;
        //Minutes
        time = oldTime.substring(3,5);
        double m = Double.parseDouble(time);
        total+= m;
        //Seconds
        time = oldTime.substring(6,8);
        double s = Double.parseDouble(time);
        total+= s/60;
        //Milliseconds
        time = oldTime.substring(10,12);
        double mm = Double.parseDouble(time);
        total += mm/6000;
        DecimalFormat df = new DecimalFormat("##.##");
        total = Double.valueOf(df.format(total));
        return total;
    }

    public static double newRate(double d, double t){
        if(d==0 || t==0){
            return 0;
        }
        double r = d/t;
        if(r>30){
            r = 30.00;
        }
        BigDecimal bd = new BigDecimal(r).setScale(2, RoundingMode.HALF_UP);
        r = bd.doubleValue();
        return r;
    }

    public static Run makeRun(String d, String s, String id){

        double distanceForSpeed = Double.parseDouble(d);
        double t = Run.newtime(s);
        double rate = Run.newRate(distanceForSpeed, t);
        Run r = new Run(id, distanceForSpeed, s, rate);
        return r;
    }

    public static String runTOShare(Run r){
        String sharable = "It's so fun and easy using Corre to track my runs!\nI just ran " + r.distance + " miles in " + Run.newtimeMinute(r.time) + " minutes!";
        return sharable;
    }
}



