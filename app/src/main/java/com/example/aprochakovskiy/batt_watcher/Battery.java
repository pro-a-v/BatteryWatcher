package com.example.aprochakovskiy.batt_watcher;

import java.util.Date;

/**
 * Created by a.prochakovskiy on 28.08.2015.
 */
public class Battery {

    //private variables
    int _id;
    int _vol_value;
    Date _vol_datetime;
    int _min_value;
    int _max_value;

    // Empty constructor
    public Battery(){

    }

    // constructor
    public Battery(int id, int vol_value){
        this._id = id;
        this._vol_value = vol_value;
        this._vol_datetime = new Date();
    }


    public int getID(){
        return this._id;
    }

    public void setID(int id){
        this._id = id;
    }

    public int getVoltage(){
        return this._vol_value;
    }

    public void setVoltage(int vol_value){
        this._vol_value = vol_value;
    }


    public Long getVoltageDatetime(){
        return this._vol_datetime.getTime();
    }

    public void setVoltageDatetime(Long bd_date_time){
        this._vol_datetime = new Date(bd_date_time);
    }

    public void setVoltageDatetime(Date bd_date_time){
        this._vol_datetime = bd_date_time;
    }

    public int get_min_value(){
        return this._min_value;
    }

    public void set_min_value(int min_value){
        this._min_value = min_value;
    }

    public int get_max_value(){
        return this._max_value;
    }


    public void set_max_value(int max_value){
        this._max_value = max_value;
    }




}
