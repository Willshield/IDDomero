package hu.bme.aut.iDDomero.userInterface;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import hu.bme.aut.iDDomero.R;
import hu.bme.aut.iDDomero.model.TimesData;

public class HighScoresActivity extends AppCompatActivity {

    private Button clearButton;
    private TextView title;
    private ListView listView;
    private List<TimesData> timeList;
    private boolean onePlayerDataShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);

        initWidgets();
        setOnClickListeners();
        loadItemsInBackground();

    }

    private void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<TimesData>>() {
            @Override
            protected List<TimesData> doInBackground(Void... voids) {
                return TimesData.getHighscores();
            }

            @Override
            protected void onPostExecute(List<TimesData> times) {
                super.onPostExecute(times);
                timeList = times;
                addToListView(times);
            }
        }.execute();
    }

    private void addToListView(final List<TimesData> times) {

        ArrayAdapter<TimesData> adapter = new ArrayAdapter<TimesData> (this, R.layout.item_highscore, android.R.id.text1, times) {
            @Override
            public View getView(int position,
                                View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(times.get(position).profile);
                text2.setText(times.get(position).time);

                return view;
            }

        };
        listView.setAdapter(adapter);

    }

    private void setOnClickListeners() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!onePlayerDataShowing){
                    TimesData itemValue = timeList.get(position);
                    List playersHighScores = TimesData.getHighscoresOf(itemValue.profile);
                    listView.removeAllViewsInLayout();
                    addToListView(playersHighScores);
                    clearButton.setText(R.string.back);
                    onePlayerDataShowing = true;
                }

            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onePlayerDataShowing){
                    loadItemsInBackground();
                    clearButton.setText(R.string.clear_highScores);
                    onePlayerDataShowing = false;
                } else {
                    TimesData.deleteAll(TimesData.class);
                    listView.removeAllViewsInLayout();
                }
            }
        });
    }

    private void initWidgets() {
        title = (TextView) findViewById(R.id.titleText);
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/BubbleMan.ttf");
        title.setTypeface(type);
        clearButton = (Button) findViewById(R.id.clearHighScores);
        listView = (ListView) findViewById(R.id.listOfHighScores);
    }


}
