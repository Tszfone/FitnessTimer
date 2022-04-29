package hk.hku.cs.fitnesstimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;


public class ExerciseActivity extends AppCompatActivity {
    private ConstraintLayout timerCL;
    private Button finishBtn, skipBtn;
    private TextView exerciseNameTV, numSetTV, numRepTV, timerTV, stateTV;
    private int numExercise, counter;
    private String exercisesInfo;
    private Gson gson;
    private Exercise[] exercises;
    private CountDownTimer cdtimer = null;
    private int exerciseCounter, setCounter;
    private boolean workoutType;
    private boolean wait, finishBtnClicked;

    private long numCompletedWorkout, timeSpent;

    private SoundPool soundPool;
    private HashMap<Integer, Integer> soundPoolMap;

    private static final int PREPARE = 0;
    private static final int WORK = 1;
    private static final int REST = 2;
    private static final int GAP = 3;
    private static final int FINISH = 4;
    private static final int INTERVAL = 1000;



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        exerciseCounter = 0;
        setCounter = 0;
        timeSpent = 0;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            workoutType = extras.getBoolean("workoutType");
            exercisesInfo = extras.getString("exercisesInfo");
            numExercise = extras.getInt("numExercise");
            gson = new Gson();
            exercises = gson.fromJson(exercisesInfo, Exercise[].class);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).build();
        } else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }

        soundPoolMap = new HashMap<Integer, Integer>();
        soundPoolMap.put(1, soundPool.load(this, R.raw.countdown, 1));
        soundPoolMap.put(2, soundPool.load(this, R.raw.start, 1));

        timerCL = findViewById(R.id.timerCL);
        timerTV = findViewById(R.id.timerTV);
        exerciseNameTV = findViewById(R.id.exerciseNameTV);
        numSetTV = findViewById(R.id.numSetTV);
        numRepTV = findViewById(R.id.numRepTV);
        stateTV = findViewById(R.id.stateTV);
        finishBtn = findViewById(R.id.finishBtn);
        skipBtn = findViewById(R.id.skipBtn);

        finishBtn.setVisibility(View.GONE);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wait = false;
                finishBtnClicked = true;
            }
        });
        skipBtn.setVisibility(View.GONE);

        exerciseCounter = 0;
        setCounter = 0;
        stateTV.setText(R.string.prepare);
        numSetTV.setText(exercises[0].getNumSet() + " set");
        numRepTV.setText(exercises[0].getNumRep() + " rep");
        exerciseNameTV.setText(exercises[0].getName());
        timerCL.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow));
        cdTimer(5, PREPARE);
    }

    public void cdTimer(int cdtime, int type) {
        final Handler handler = new Handler();
        final int[] cd = {cdtime};
        handler.post(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                if (cd[0] > 0) {
                    if (!wait) {
                        String minute = "" + (cd[0] / 60) % 60;
                        if (minute.length() == 1) {
                            minute = "0" + minute;
                        }
                        String second = "" + cd[0] % 60;
                        if (second.length() == 1) {
                            second = "0" + second;
                        }
                        if (!finishBtnClicked) {
                            timerTV.setText(minute + ":" + second);

                        }

                        if (cd[0] < 5) {
                            soundPool.play(1, 1, 1, 1, 0, 1);
                        }

                        cd[0]--;
                        timeSpent++;
                    } else {
                        timerTV.setText(R.string.time);
                    }

                    if (!finishBtnClicked) {
                        handler.postDelayed(this, INTERVAL);
                    } else {
                        finishBtnClicked = false;
                        handler.post(this);
                    }


                } else {
                    if (exerciseCounter < numExercise) {
                        if (type == PREPARE) {
                            stateTV.setText(R.string.work);
                            numSetTV.setText((exercises[exerciseCounter].getNumSet() - setCounter) + " set");
                            numRepTV.setText(exercises[exerciseCounter].getNumRep() + " rep");
                            exerciseNameTV.setText(exercises[exerciseCounter].getName());
                            timerCL.setBackgroundColor(ContextCompat.getColor(ExerciseActivity.this, R.color.red));

                            if (exercises[exerciseCounter].getDuration() == -1) {
                                wait = true;
                                finishBtn.setVisibility(View.VISIBLE);
                                soundPool.play(2, 1, 1, 1, 0, 1);
                                cdTimer(1, WORK);
                            } else {
                                wait = false;
                                finishBtn.setVisibility(View.GONE);
                                soundPool.play(2, 1, 1, 0, 0, 1);
                                cdTimer(exercises[exerciseCounter].getDuration(), WORK);
                            }
                        } else if (type == WORK) {
                            setCounter++;
                            if (setCounter == exercises[exerciseCounter].getNumSet()) {
                                setCounter = 0;
                                exerciseCounter++;
                                if (exerciseCounter < numExercise - 1) {
                                    numSetTV.setText((exercises[exerciseCounter].getNumSet() - setCounter) + " set");
                                    numRepTV.setText(exercises[exerciseCounter].getNumRep() + " rep");
                                }
                            }

                            if (exerciseCounter < numExercise) {
                                finishBtn.setVisibility(View.GONE);
                                stateTV.setText(R.string.rest);
                                numSetTV.setText((exercises[exerciseCounter].getNumSet() - setCounter) + " set");
                                numRepTV.setText(exercises[exerciseCounter].getNumRep() + " rep");
                                exerciseNameTV.setText(exercises[exerciseCounter].getName());
                                timerCL.setBackgroundColor(ContextCompat.getColor(ExerciseActivity.this, R.color.green));
                                cdTimer(exercises[exerciseCounter].getRestTime(), REST);
                            }

                            if (exerciseCounter == numExercise) {
                                finishBtn.setVisibility(View.GONE);
                                stateTV.setText(R.string.finish);
                                numSetTV.setText("");
                                numRepTV.setText("");
                                exerciseNameTV.setText("Good Job");
                                timerCL.setBackgroundColor(ContextCompat.getColor(ExerciseActivity.this, R.color.green));
                                cdTimer(5, FINISH);
                            }

                        } else if (type == REST) {
                            int gap = exercises[exerciseCounter].getTimeGap();
                            if (gap == 0) {
                                stateTV.setText(R.string.work);
                                numSetTV.setText((exercises[exerciseCounter].getNumSet() - setCounter) + " set");
                                numRepTV.setText(exercises[exerciseCounter].getNumRep() + " rep");
                                exerciseNameTV.setText(exercises[exerciseCounter].getName());
                                timerCL.setBackgroundColor(ContextCompat.getColor(ExerciseActivity.this, R.color.red));
                                if (exercises[exerciseCounter].getDuration() == -1) {
                                    wait = true;
                                    finishBtn.setVisibility(View.VISIBLE);
                                    soundPool.play(2, 1, 1, 1, 0, 1);
                                    cdTimer(1, WORK);
                                } else {
                                    wait = false;
                                    finishBtn.setVisibility(View.INVISIBLE);
                                    soundPool.play(2, 1, 1, 1, 0, 1);
                                    cdTimer(exercises[exerciseCounter].getDuration(), WORK);
                                }
                            } else {
                                finishBtn.setVisibility(View.GONE);
                                stateTV.setText(R.string.prepare);
                                numSetTV.setText((exercises[exerciseCounter].getNumSet() - setCounter) + " set");
                                numRepTV.setText(exercises[exerciseCounter].getNumRep() + " rep");
                                exerciseNameTV.setText(exercises[exerciseCounter].getName());
                                timerCL.setBackgroundColor(ContextCompat.getColor(ExerciseActivity.this, R.color.yellow));
                                cdTimer(gap, GAP);
                            }

                        } else if (type == GAP){
                            setCounter = 0;
                            stateTV.setText(R.string.work);
                            numSetTV.setText((exercises[exerciseCounter].getNumSet() - setCounter) + " set");
                            numRepTV.setText(exercises[exerciseCounter].getNumRep() + " rep");
                            exerciseNameTV.setText(exercises[exerciseCounter].getName());
                            timerCL.setBackgroundColor(ContextCompat.getColor(ExerciseActivity.this, R.color.red));
                            if (exercises[exerciseCounter].getDuration() == -1) {
                                wait = true;
                                finishBtn.setVisibility(View.VISIBLE);
                                soundPool.play(2, 1, 1, 1, 0, 1);
                                cdTimer(1, WORK);
                            } else {
                                wait = false;
                                finishBtn.setVisibility(View.INVISIBLE);
                                soundPool.play(2, 1, 1, 1, 0, 1);
                                cdTimer(exercises[exerciseCounter].getDuration(), WORK);
                            }
                        }
                    }

                    if (type == FINISH) {
                        numCompletedWorkout = read("cw");
                        long tsNum = read("ts");

                        numCompletedWorkout++;
                        write("cw", numCompletedWorkout);

                        tsNum+=timeSpent;
                        write("ts", tsNum);

                        Intent intent = new Intent(ExerciseActivity.this, MainActivity.class);
                        if(!workoutType) {
                            intent.putExtra("returnToCW", true);
                        } else {
                            intent.putExtra("returnToCW", false);
                        }

                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    public void write(String tag, long value) {
        SharedPreferences sharedPreferences= getSharedPreferences("Stat", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(tag, value);
        editor.apply();
    }

    public long read(String tag) {
        SharedPreferences sharedPreferences = getSharedPreferences("Stat", Context.MODE_PRIVATE);
        return sharedPreferences.getLong(tag, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
    }
}