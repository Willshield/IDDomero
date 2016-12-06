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
        //todo: convert to string, format, add listview

    }

    private void initListView() {
        listView = (ListView) findViewById(R.id.listOfHighScores);
        String[] values = new String[]{"Android List View",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android Example List View",
                "Android Example List View",
                "Android Example List View"
        };
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
                //todo
            }
        });
    }

    private void initWidgets() {
        title = (TextView) findViewById(R.id.titleText);
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/BubbleMan.ttf");
        title.setTypeface(type);
        clearButton = (Button) findViewById(R.id.clearHighScores);

        initListView();
    }


}
