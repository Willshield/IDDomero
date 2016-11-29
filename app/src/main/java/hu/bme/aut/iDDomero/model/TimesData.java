package hu.bme.aut.iDDomero.model;

/*
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;*/

import com.orm.SugarRecord;
import com.orm.query.Select;

import java.util.List;

public class TimesData extends SugarRecord {

    public String profile;
    public String time;

    public TimesData(){}

    public TimesData(String p, String t){
        profile = p;
        time = t;
    }

    public List<TimesData> getHighscores(){
        return Select.from(TimesData.class).orderBy("time").list();
    }


    /*
    public void addUser(String user){
        profiles.add(user);
        times.add(new ArrayList<String>());
    }

    public void addTime(String user, String timeStamp){
        if( !profiles.contains(user))
            throw new IllegalArgumentException("not exsisting user");

        int index = -1;
        for (int i = 0; i < profiles.size(); i++) {
            String profile = profiles.get(i);
            if (profile.equals(user)){
                index = i;
                break;
            }
        }

        times.get(index).add(timeStamp);
        sortTimesAt(index);



    }

    private void sortTimesAt(int index) {
        Comparator<String> cmp = new Comparator<String>() {
            public int compare(String o1, String o2) {
                return Integer.valueOf(o1).compareTo(Integer.valueOf(o2));
            }
        };
        Collections.sort(times.get(index), cmp);
    }*/

}
