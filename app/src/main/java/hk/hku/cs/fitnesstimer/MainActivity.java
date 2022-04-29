package hk.hku.cs.fitnesstimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import hk.hku.cs.fitnesstimer.db.RoomDB;
import hk.hku.cs.fitnesstimer.db.Workout;
import hk.hku.cs.fitnesstimer.fragments.BadgeFragment;
import hk.hku.cs.fitnesstimer.fragments.CustomDefineWorkoutFragment;
import hk.hku.cs.fitnesstimer.fragments.PredefineWorkoutFragment;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private FloatingActionButton addFab, createFab, importFab;
    private Animation fabOpen, fabClose, rotateForward, rotateBackward;
    private boolean isOpen = false;
    private RoomDB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDefaultFragment();
        toolbar = findViewById(R.id.toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Boolean returnToCW = extras.getBoolean("returnToCW");
            if (returnToCW) {
                addFragment(new CustomDefineWorkoutFragment(),false, "cw");
                toolbar.setTitle(getResources().getString(R.string.cw));
            }
        }

        // clearDB();
        // clearStat();

        database = RoomDB.getInstance(this.getApplicationContext());
        if (database.workoutDao().getAllPredefineWorkouts().size() == 0) {
            initialiseDB();
        }

        drawerLayout = findViewById(R.id.drawerLayout);

        navigationView = findViewById(R.id.navigationView);
        addFab = findViewById(R.id.addFab);


        createFab = findViewById(R.id.createFab);
        importFab = findViewById(R.id.importFab);

        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);

        setUpFragmentTravel(navigationView);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle abdToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_draw_open, R.string.navigation_draw_close);
        abdToggle.setDrawerIndicatorEnabled(true);
        abdToggle.syncState();
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(abdToggle);

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
            }
        });

        createFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
                Intent intent = new Intent(MainActivity.this, DefineWorkoutActivity.class);
                startActivity(intent);
            }
        });

        importFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
                Intent intent = new Intent(MainActivity.this, ImportActivity.class);
                startActivity(intent);
            }
        });


    }

    public void displayFAB() {
        addFab.setVisibility(View.VISIBLE);
    }

    public void hideFAB() {
        addFab.setVisibility(View.INVISIBLE);
    }

    public void collapse() {
        addFab.setImageResource(R.drawable.ic_baseline_add_24);
        createFab.startAnimation(fabClose);
        importFab.startAnimation(fabClose);
        createFab.setClickable(false);
        importFab.setClickable(false);
        isOpen = false;
    }

    public void expand() {
        addFab.setImageResource(R.drawable.ic_baseline_close_24);
        createFab.startAnimation(fabOpen);
        importFab.startAnimation(fabOpen);
        createFab.setClickable(true);
        importFab.setClickable(true);
        isOpen = true;
    }

    public void animateFab() {
        if (isOpen) {

            collapse();
        } else {

            expand();
        }
    }

    public void initialiseDB() {
        database = RoomDB.getInstance(this.getApplicationContext());
        String exercisesInfo = "[{'name': 'sprint', 'duration': 60, 'restTime': 60, 'numSet': 1, 'numRep': 1, 'timeGap': 0}, "
                             + "{'name': 'jumping jack', 'duration': 60, 'restTime': 60, 'numSet': 1, 'numRep': 1, 'timeGap': 0}, "
                             + "{'name': 'squat', 'duration': 60, 'restTime': 60, 'numSet': 1, 'numRep': 1, 'timeGap': 0}, "
                             + "{'name': 'the mummy', 'duration': 60, 'restTime': 60, 'numSet': 1, 'numRep': 1, 'timeGap': 0}, "
                             + "{'name': 'sprinter sit-up', 'duration': 60, 'restTime': 60, 'numSet': 1, 'numRep': 1, 'timeGap': 0}, "
                             + "{'name': 'cross jack', 'duration': 60, 'restTime': 60, 'numSet': 1, 'numRep': 1, 'timeGap': 0}, "
                             + "{'name': 'push up', 'duration': 60, 'restTime': 60, 'numSet': 1, 'numRep': 1, 'timeGap': 0}, "
                             + "{'name': 'pike', 'duration': 60, 'restTime': 60, 'numSet': 1, 'numRep': 1, 'timeGap': 0}]";
        database.workoutDao().insertWorkout(new Workout(true, "Beginner", "easy", 8, exercisesInfo));

        exercisesInfo = "[{'name': 'sprint', 'duration': 60, 'restTime': 30, 'numSet': 1, 'numRep': 1, 'timeGap': 0}, "
                      + "{'name': 'jumping jack', 'duration': 60, 'restTime': 30, 'numSet': 1, 'numRep': 1, 'timeGap': 0}, "
                      + "{'name': 'squat', 'duration': 60, 'restTime': 30, 'numSet': 1, 'numRep': 1, 'timeGap': 0}, "
                      + "{'name': 'the mummy', 'duration': 60, 'restTime': 30, 'numSet': 1, 'numRep': 1, 'timeGap': 0}, "
                      + "{'name': 'sprinter sit-up', 'duration': 60, 'restTime': 30, 'numSet': 1, 'numRep': 1, 'timeGap': 0}, "
                      + "{'name': 'cross jack', 'duration': 60, 'restTime': 30, 'numSet': 1, 'numRep': 1, 'timeGap': 0}, "
                      + "{'name': 'push up', 'duration': 60, 'restTime': 30, 'numSet': 1, 'numRep': 1, 'timeGap': 0}, "
                      + "{'name': 'pike', 'duration': 60, 'restTime': 30, 'numSet': 1, 'numRep': 1, 'timeGap': 0}]";
        database.workoutDao().insertWorkout(new Workout(true, "Intermediate", "normal", 8, exercisesInfo));

        exercisesInfo = "[{'name': 'sprint', 'duration': 60, 'restTime': 15, 'numSet': 1, 'numRep': 1, 'timeGap': 0}, "
                      + "{'name': 'jumping jack', 'duration': 60, 'restTime': 15, 'numSet': 1, 'numRep': 1, 'timeGap': 0}, "
                      + "{'name': 'squat', 'duration': 60, 'restTime': 15, 'numSet': 1, 'numRep': 1, 'timeGap': 0}, "
                      + "{'name': 'the mummy', 'duration': 60, 'restTime': 15, 'numSet': 1, 'numRep': 1, 'timeGap': 0}, "
                      + "{'name': 'sprinter sit-up', 'duration': 60, 'restTime': 15, 'numSet': 1, 'numRep': 1, 'timeGap': 0}, "
                      + "{'name': 'cross jack', 'duration': 60, 'restTime': 15, 'numSet': 1, 'numRep': 1, 'timeGap': 0}, "
                      + "{'name': 'push up', 'duration': 60, 'restTime': 15, 'numSet': 1, 'numRep': 1, 'timeGap': 0}, "
                      + "{'name': 'pike', 'duration': 60, 'restTime': 15, 'numSet': 1, 'numRep': 1, 'timeGap': 0}]";
        database.workoutDao().insertWorkout(new Workout(true, "Advance", "hard", 8, exercisesInfo));
    }

    public void clearDB() {
        database = RoomDB.getInstance(this.getApplicationContext());
        database.workoutDao().deleteAll();
    }

    public void clearStat() {
        SharedPreferences sharedPreferences = getSharedPreferences("Stat", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public void addFragment(Fragment fragment, boolean addToBackStack, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.replace(R.id.fragmentContainer, fragment, tag);
        ft.commitAllowingStateLoss();
    }

    public void setDefaultFragment() {
        addFragment(new PredefineWorkoutFragment(),false, "pw");
    }

    public void setUpFragmentTravel(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.nav_pw:
                        hideFAB();
                        addFragment(new PredefineWorkoutFragment(),true, "pw");
                        toolbar.setTitle(R.string.pw);

                        break;
                    case R.id.nav_cw:
                        displayFAB();
                        addFragment(new CustomDefineWorkoutFragment(),true, "cw");
                        toolbar.setTitle(R.string.cw);

                        break;
                    case R.id.nav_badge:
                        hideFAB();
                        addFragment(new BadgeFragment(), true, "badge");
                        toolbar.setTitle(R.string.statistic);

                        break;
                }
                item.setChecked(false);
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}