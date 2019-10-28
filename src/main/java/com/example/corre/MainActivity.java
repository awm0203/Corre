package com.example.corre;

import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public void toReport(){
        BottomNavigationView bottomNav =  (BottomNavigationView)findViewById(R.id.bottom_menu);
        bottomNav.getMenu().getItem(2).setChecked(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_menu);
        bottomNav.setOnNavigationItemSelectedListener(nabListener);


        /**
         * Code Below decides first screen, there should be an if(new) { acct activation} else { the code below }
         */
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new RunFragment()).commit();
        bottomNav.getMenu().getItem(1).setChecked(true);


    }



    private BottomNavigationView.OnNavigationItemSelectedListener nabListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()){
                        case R.id.nav_profile:
                            selectedFragment = new ProfileFragment();
                            break;
                        case R.id.nav_run:
                            selectedFragment = new RunFragment();
                            break;
                        case R.id.nav_report:
                            selectedFragment = new ReportFragment();
                            break;
                        case R.id.nav_chat:
                            selectedFragment = new ChatFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, selectedFragment).commit();

                    return true;
                }
            };



}
