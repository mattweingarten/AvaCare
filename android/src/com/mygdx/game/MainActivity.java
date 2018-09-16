package com.mygdx.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AndroidFragmentApplication.Callbacks{

    public static final String URL="https://c3151d22.ngrok.io";

    int id;
    int ko_type;
    @Override
    public void exit(){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO change color of main drawer
        //TODO change background color of all @+colors:colorBackgroundMain
        setTitle("AvaCare - new health issue");

        Intent i = getIntent();
        id = i.getIntExtra("id",0);

        Log.d("##id",""+id);
        ko_type = i.getIntExtra("ko_type",0);

        //TODO set background as avatar

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO send medical data to backend, receive treatment

                //TODO send via json to backend
                //--> send fever (true, false) and accident (true, false)


                //TODO get location

                //TODO get fever, get comment
                //receive answer
                String advice = "do this";
                Intent i = new Intent(MainActivity.this, ReceiveTreatmentActivity.class);
                i.putExtra("advice", advice);
                startActivity(i);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // TextView headerTV = (TextView) findViewById(R.id.nav_header_title);

        if(ko_type == 1){
            //TODO goto accident input
        }else if(ko_type == 2){
            //TODO goto illness input
        }else if(ko_type == 0){

        }
        GameFragment libgdcfragment = new GameFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.contentFramelayout, libgdcfragment).commit();
        /*AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        CallbackListener callbackListener = new CallbackListener() {
            @Override
            public void sendData(float x, float y, float z) {
                Log.d("##", "x: " + x + ", y: " + y + ", z: " + z);
            }
        };
        AndroidLauncher aa = new AndroidLauncher();
        View v = aa.initializeForView(new MyGdxGame(callbackListener), config);
        ViewGroup layout = (ViewGroup) findViewById(R.id.mainView);
        TextView tv = new TextView(this);
        tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        tv.setText("Added tv");
        layout.addView(tv);*/
        drawer.openDrawer(Gravity.LEFT);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here
        Intent i = null;
        int item_id = item.getItemId();

        Log.d("##idMAIN", ""+id);
        if (item_id == R.id.nav_accidnet) {
            i = new Intent(MainActivity.this, ReceiveTreatmentActivity.class);
            i.putExtra("ko_type", 1);    //code for accident
            i.putExtra("id", id);
        } else if (item_id == R.id.nav_sickness) {
            i = new Intent(MainActivity.this, ReceiveTreatmentActivity.class);
            i.putExtra("ko_type", 2);    //code for sickness
            i.putExtra("id", id);
        } else if (item_id == R.id.nav_history) {
            i = new Intent(MainActivity.this, HistoryActivity.class);
            i.putExtra("id",id);
        } else if (item_id == R.id.nav_skins) {
            i = new Intent(MainActivity.this, SkinsActivity.class);
        } else if (item_id == R.id.nav_settings) {
            i = new Intent(MainActivity.this, SettingsActivity.class);
        } else if (item_id == R.id.nav_logOut) {
            i = new Intent(MainActivity.this, SignInActivity.class);
            //TODO prevent user from pressing back
        }
        startActivity(i);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
