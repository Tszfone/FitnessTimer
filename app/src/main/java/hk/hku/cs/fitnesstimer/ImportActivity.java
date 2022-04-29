package hk.hku.cs.fitnesstimer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import hk.hku.cs.fitnesstimer.db.RoomDB;
import hk.hku.cs.fitnesstimer.db.Workout;

public class ImportActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button importBtn;
    private final int CHOOSE_TXT_FROM_DEVICE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.importBtn);

        importBtn = findViewById(R.id.importBtn);
        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseTxtFromDevice();
            }
        });
    }


    public void chooseTxtFromDevice() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        startActivityForResult(intent, CHOOSE_TXT_FROM_DEVICE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_TXT_FROM_DEVICE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    InputStream in = getContentResolver().openInputStream(uri);
                    BufferedReader r = new BufferedReader(new InputStreamReader(in));
                    StringBuilder total = new StringBuilder();
                    for (String line; (line = r.readLine()) != null; ) {
                        total.append(line).append('\n');
                    }
                    String wjson = total.toString();

                    Gson gson = new Gson();
                    Workout w = gson.fromJson(wjson, Workout.class);
                    Workout nw = new Workout(false, w.getWorkoutName(), w.getDescription(), w.getNumExercise(), w.getExercisesInfo());
                    RoomDB database = RoomDB.getInstance(ImportActivity.this.getApplicationContext());
                    database.workoutDao().insertWorkout(nw);

                    Intent intent = new Intent(ImportActivity.this, MainActivity.class);
                    intent.putExtra("returnToCW", true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }catch (Exception e) {

                }

            }
        }
    }
}