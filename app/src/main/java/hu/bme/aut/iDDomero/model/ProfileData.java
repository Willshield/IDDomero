package hu.bme.aut.iDDomero.model;


import com.orm.SugarRecord;

import java.util.List;

public class ProfileData extends SugarRecord {
    public String name;

    public ProfileData(){}

    public ProfileData(String name){
        this.name = name;
    }

    List<TimesData> getTimes(){
        return TimesData.find(TimesData.class, "profile = ?", name);
    }
}
