package hu.bme.aut.iDDomero.userInterface;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import hu.bme.aut.iDDomero.R;
import hu.bme.aut.iDDomero.model.TimesData;

public class HighScoresActivity extends AppCompatActivity {

    private Button clearButton;
    private TextView title;
    private ListView listView;

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
                return TimesData.listAll(TimesData.class);
            }

            @Override
            protected void onPostExecute(List<TimesData> times) {
                super.onPostExecute(times);
                addToListView(times);
            }
        }.execute();
    }

    private void addToListView(List<TimesData> times) {
        //todo: format, sort
        String[] values = new String[times.size()];
        for (int i = 0; i < times.size(); i++){
            values[i] = times.get(i).profile + " " + times.get(i).time;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listView.setAdapter(adapter);

    }

    private void setOnClickListeners() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;
                String itemValue = (String) listView.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimesData.deleteAll(TimesData.class);
                listView.removeAllViewsInLayout();
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
