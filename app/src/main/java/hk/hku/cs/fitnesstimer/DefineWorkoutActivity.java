package hk.hku.cs.fitnesstimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import hk.hku.cs.fitnesstimer.db.Workout;

public class DefineWorkoutActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextInputLayout workoutNameTIL, descTIL, numExerciseTIL;
    private Button nextBtn;
    private String wjson;
    private Workout workout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_define_workout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.workout_info);

        workoutNameTIL = findViewById(R.id.workoutNameTIL);
        descTIL = findViewById(R.id.descTIL);
        numExerciseTIL = findViewById(R.id.numExerciseTIL);

        workout = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Gson gson = new Gson();
            wjson = extras.getString("wjson");
            workout =  gson.fromJson(wjson, Workout.class);

            workoutNameTIL.getEditText().setText(workout.getWorkoutName());
            descTIL.getEditText().setText(workout.getDescription());
            numExerciseTIL.getEditText().setText(""+workout.getNumExercise());
        }

        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = true;
                String workoutName = workoutNameTIL.getEditText().getText().toString();
                String desc = descTIL.getEditText().getText().toString();
                String numExercise = numExerciseTIL.getEditText().getText().toString();

                if (workoutName.isEmpty()) {
                    isValid = false;
                    workoutNameTIL.setError(getResources().getString(R.string.empty_error));
                } else {
                    workoutNameTIL.setError(null);
                }

                if (desc.isEmpty()) {
                    isValid = false;
                    descTIL.setError(getResources().getString(R.string.empty_error));
                } else {
                    descTIL.setError(null);
                }

                if (numExercise.isEmpty()) {
                    isValid = false;
                    numExerciseTIL.setError(getResources().getString(R.string.empty_error));
                } else {
                    numExerciseTIL.setError(null);
                }

                if (isValid) {
                    Intent intent = new Intent(DefineWorkoutActivity.this, DefineExerciseActivity.class);
                    intent.putExtra("workoutName", workoutName);
                    intent.putExtra("desc", desc);
                    intent.putExtra("numExercise", Integer.parseInt(numExercise));
                    if (workout != null) {
                        intent.putExtra("wjson", wjson);
                    }
                    startActivity(intent);
                }
            }
        });
    }


}