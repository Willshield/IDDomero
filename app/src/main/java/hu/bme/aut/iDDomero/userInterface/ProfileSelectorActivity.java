package hu.bme.aut.iDDomero.userInterface;


import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import hu.bme.aut.iDDomero.R;
import hu.bme.aut.iDDomero.model.ProfileData;

public class ProfileSelectorActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProfileViewAdapter adapter;

    private Button newProfile;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_selector);
        initWidgets();
        setOnClickListeners();

        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.ProfileRecyclerView);
        adapter = new ProfileViewAdapter(getApplicationContext());
        loadItemsInBackground();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<ProfileData>>() {
            @Override
            protected List<ProfileData> doInBackground(Void... voids) {
                return ProfileData.listAll(ProfileData.class);
            }

            @Override
            protected void onPostExecute(List<ProfileData> profiles) {
                super.onPostExecute(profiles);
                adapter.update(profiles);
            }
        }.execute();
    }

    private void setOnClickListeners() {
        newProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    private void initWidgets() {
        newProfile = (Button) findViewById(R.id.newProfile);
        title = (TextView) findViewById(R.id.titleText);
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/BubbleMan.ttf");
        title.setTypeface(type);
    }


    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New profile:");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ProfileData newProfileItem = new ProfileData(input.getText().toString());
                if(validProfile(newProfileItem)){
                    newProfileItem.save();
                    adapter.addItem(newProfileItem);
                };

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private boolean validProfile(ProfileData newProfileItem) {
        if(newProfileItem.name.equals("")){
            Toast.makeText(getApplicationContext(),"Username must be at least one character long", Toast.LENGTH_LONG).show();
            return false;
        }

        if(adapter.contains(newProfileItem)){
            Toast.makeText(getApplicationContext(),"Username is already taken", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

}
