package com.example.maximum.cppai.Frontend;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.maximum.cppai.Backend.API;
import com.example.maximum.cppai.Backend.Query;
import com.example.maximum.cppai.Backend.Response;
import com.example.maximum.cppai.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public static final String CELSIUS_KEY = "celsius", NICKNAME_KEY = "nickname";
    private ImageButton mic;
    private EditText input;
    private RecyclerView history;
    private List<Response> responses;
    private ResponseViewAdapter adapter;



    private boolean menuOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar)findViewById(R.id.my_toolbar));
        mic = (ImageButton) findViewById(R.id.mic);
        input=(EditText)findViewById(R.id.input);

        responses = new ArrayList<>();

        adapter = new ResponseViewAdapter(responses);
        history = (RecyclerView)findViewById(R.id.requests);
        history.setAdapter(adapter);
        history.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        history.setLayoutManager(layoutManager);

        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Response response = new Query().execute(input.getText().toString()).get();
//                    Log.d("Response", response.toString());
                    responses.add(response);
                    adapter.notifyDataSetChanged();
                    //Toast.makeText(MainActivity.this, response.getDetailedDescription(), Toast.LENGTH_SHORT).show();

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear:
                responses.clear();
                adapter.notifyDataSetChanged();
               // settings.edit().
                Toast.makeText(this, "Cleared history", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.settings:
                showSettings();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    public void showSettings(){
        final AlertDialog.Builder settingsBuilder = new AlertDialog.Builder(MainActivity.this);
        View sView = getLayoutInflater().inflate(R.layout.settings_dialog,null);

        final SharedPreferences settings = getSharedPreferences("Settings",MODE_PRIVATE);


        final EditText nickname = (EditText)sView.findViewById(R.id.nickname);
        nickname.setText(settings.getString(NICKNAME_KEY,""));

        final RadioButton celsius = (RadioButton)sView.findViewById(R.id.celsius);
        RadioButton fahrenheit = (RadioButton)sView.findViewById(R.id.fahrenheit);
        if(settings.getBoolean(CELSIUS_KEY,true))
            celsius.setChecked(true);
        else
            fahrenheit.setChecked(true);


        settingsBuilder.setTitle("Settings");
        settingsBuilder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        settingsBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                settings.edit().putBoolean(CELSIUS_KEY,celsius.isChecked()).putString(NICKNAME_KEY,nickname.getText().toString().trim()).apply();
                Toast.makeText(MainActivity.this, "name: "+nickname.getText().toString()+"\ntemp: "+(celsius.isChecked()?"Celsius":"fahrenheit"), Toast.LENGTH_SHORT).show();
            }
        });

        settingsBuilder.setView(sView);
        settingsBuilder.create().show();
    }
}
