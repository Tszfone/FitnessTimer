package hk.hku.cs.fitnesstimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import hk.hku.cs.fitnesstimer.db.RoomDB;
import hk.hku.cs.fitnesstimer.db.Workout;

public class DefineExerciseActivity extends AppCompatActivity {
    private Integer counter;
    private int numExercise, id;
    private String duration, restTime, numSet, numRep, timeGap;
    private String workoutName, desc, exerciseName, wjson;
    private Toolbar toolbar;
    private ScrollView scrollView;
    private TextInputLayout exerciseNameTIL, durationTIL, restTimeTIL, numSetTIL, numRepTIL, timeGapTIL;
    private Button nextExeBtn;
    private Exercise[] exercises, origin;
    private boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_define_exercise);

        isEdit = false;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            workoutName = extras.getString("workoutName");
            desc = extras.getString("desc");
            numExercise = extras.getInt("numExercise");
            exercises = new Exercise[numExercise];
            wjson = extras.getString("wjson");
            if (wjson != null) {
                isEdit = true;
            }
        }

        counter = 1;
        nextExeBtn = findViewById(R.id.nextExeBtn);
        toolbar = findViewById(R.id.toolbar);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Exercise " + counter.toString() + " Info");
        scrollView = findViewById(R.id.scrollView);

        exerciseNameTIL = findViewById(R.id.exerciseNameTIL);
        durationTIL = findViewById(R.id.durationTIL);
        restTimeTIL = findViewById(R.id.restTimeTIL);
        numSetTIL = findViewById(R.id.numSetTIL);
        numRepTIL = findViewById(R.id.numRepTIL);
        timeGapTIL = findViewById(R.id.timeGapTIL);

        if (isEdit) {
            Gson gson = new Gson();
            Workout workout = gson.fromJson(wjson, Workout.class);
            id = workout.getWid();
            origin = gson.fromJson(workout.getExercisesInfo(), Exercise[].class);

            exerciseNameTIL.getEditText().setText(origin[0].getName());
            durationTIL.getEditText().setText("" + origin[0].getDuration());
            restTimeTIL.getEditText().setText("" + origin[0].getRestTime());
            numSetTIL.getEditText().setText("" + origin[0].getNumSet());
            numRepTIL.getEditText().setText("" + origin[0].getNumRep());
            timeGapTIL.getEditText().setText("" + origin[0].getTimeGap());
        }

        nextExeBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                boolean isValid = true;

                exerciseName =  exerciseNameTIL.getEditText().getText().toString();
                duration = durationTIL.getEditText().getText().toString();
                restTime = restTimeTIL.getEditText().getText().toString();
                numSet = numSetTIL.getEditText().getText().toString();
                numRep = numRepTIL.getEditText().getText().toString();
                timeGap = timeGapTIL.getEditText().getText().toString();

                if (exerciseName.isEmpty()) {
                    exerciseNameTIL.setError(getResources().getString(R.string.empty_error));
                    isValid = false;
                } else {
                    exerciseNameTIL.setError(null);
                }

                if (duration.isEmpty()) {
                    durationTIL.setError(getResources().getString(R.string.empty_error));
                    isValid = false;
                } else {
                    durationTIL.setError(null);
                }

                if (restTime.isEmpty()) {
                    restTimeTIL.setError(getResources().getString(R.string.empty_error));
                    isValid = false;
                } else {
                    restTimeTIL.setError(null);
                }

                if (numSet.isEmpty()) {
                    numSetTIL.setError(getResources().getString(R.string.empty_error));
                    isValid = false;
                } else {
                    numSetTIL.setError(null);
                }

                if (numRep.isEmpty()) {
                    numRepTIL.setError(getResources().getString(R.string.empty_error));
                    isValid = false;
                } else {
                    numRepTIL.setError(null);
                }

                if (timeGap.isEmpty()) {
                    timeGapTIL.setError(getResources().getString(R.string.empty_error));
                    isValid = false;
                } else {
                    timeGapTIL.setError(null);
                }

                if (isValid) {
                    exerciseNameTIL.getEditText().getText().clear();
                    durationTIL.getEditText().getText().clear();
                    restTimeTIL.getEditText().getText().clear();
                    numSetTIL.getEditText().getText().clear();
                    numRepTIL.getEditText().getText().clear();
                    timeGapTIL.getEditText().getText().clear();

                    int dur = Integer.parseInt(duration);
                    if (dur == 0) {
                        dur = -1;
                    }

                    exercises[counter-1] = new Exercise(exerciseName,
                            dur,
                            Integer.parseInt(restTime),
                            Integer.parseInt(numSet),
                            Integer.parseInt(numRep),
                            Integer.parseInt(timeGap));
                    counter+=1;
                    toolbar.setTitle("Exercise " + counter.toString() + " Info");
                    scrollView.scrollTo(0, 0);

                    if (counter == numExercise) {
                        nextExeBtn.setText(R.string.finish);
                    }
                    if (counter > numExercise) {
                        counter = 0;
                        Gson gson = new Gson();
                        String info = gson.toJson(exercises);

                        Workout workout = new Workout(false, workoutName, desc, numExercise, info);
                        RoomDB database = RoomDB.getInstance(DefineExerciseActivity.this.getApplicationContext());
                        if (!isEdit) {
                            database.workoutDao().insertWorkout(workout);
                        } else {
                            database.workoutDao().updateWorkout(id, workoutName, desc, numExercise, info);
                        }

                        Intent intent = new Intent(DefineExerciseActivity.this, MainActivity.class);
                        intent.putExtra("returnToCW", true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        if (isEdit) {
                            exerciseNameTIL.getEditText().setText("" + origin[counter - 1].getName());
                            durationTIL.getEditText().setText("" + origin[counter - 1].getDuration());
                            restTimeTIL.getEditText().setText("" + origin[counter - 1].getRestTime());
                            numSetTIL.getEditText().setText("" + origin[counter - 1].getNumSet());
                            numRepTIL.getEditText().setText("" + origin[counter - 1].getNumRep());
                            timeGapTIL.getEditText().setText("" + origin[counter - 1].getTimeGap());
                        }
                    }
                }
            }
        });
    }
}