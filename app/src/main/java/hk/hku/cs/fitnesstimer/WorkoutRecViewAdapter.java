package hk.hku.cs.fitnesstimer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

import hk.hku.cs.fitnesstimer.db.RoomDB;
import hk.hku.cs.fitnesstimer.db.Workout;

public class WorkoutRecViewAdapter extends RecyclerView.Adapter<WorkoutRecViewAdapter.ViewHolder> {
    private List<Workout> workouts;
    private Context context;
    private static final String FILE_NAME = "share.txt";

    public WorkoutRecViewAdapter(Context context) {
        this.context = context;
    }

    public void setWorkouts(List<Workout> workouts) {
        this.workouts = workouts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.workoutTitle.setText(workouts.get(position).getWorkoutName());
        holder.workoutDesc.setText(workouts.get(position).getDescription());
        holder.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                boolean wtype = workouts.get(pos).isDefineType();
                String exercisesInfo = workouts.get(pos).getExercisesInfo();
                int numExercise = workouts.get(pos).getNumExercise();

                Intent intent = new Intent(context, ExerciseActivity.class);
                intent.putExtra("workoutType", wtype);
                intent.putExtra("exercisesInfo", exercisesInfo);
                intent.putExtra("numExercise", numExercise);
                context.startActivity(intent);
            }
        });

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                int pos = holder.getAdapterPosition();
                String wjson = gson.toJson(workouts.get(pos));
                Intent intent = new Intent(context, DefineWorkoutActivity.class);
                intent.putExtra("wjson", wjson);
                context.startActivity(intent);
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                int wid = workouts.get(pos).getWid();
                new MaterialAlertDialogBuilder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete this workout?")
                        .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                RoomDB database = RoomDB.getInstance(context.getApplicationContext());
                                database.workoutDao().deleteWorkout(wid);
                                updateList();
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
            }
        });

        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                int pos = holder.getAdapterPosition();
                String wjson = gson.toJson(workouts.get(pos));
                saveTxt(wjson);
            }
        });
    }

    public void saveTxt(String wjson){
        FileOutputStream fos = null;

        try {
            fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write(wjson.getBytes());
            //Toast.makeText(context, "save to "+context.getFilesDir()+"/"+FILE_NAME, Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        String path = context.getFilesDir() + "/" + FILE_NAME;
        File shareFile = new File(path);
        shareFile.setReadable(true, false);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri uri = FileProvider.getUriForFile(context.getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", shareFile);
        intent.setType("text/*");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(intent, "share file with"));

        /*
        PackageManager pm = context.getApplicationContext().getPackageManager();
        if (intent.resolveActivity(pm) != null) {
            context.startActivity(intent);
        }
         */
    }



    @Override
    public int getItemCount() {
        return workouts.size();
    }

    public void updateList() {
        RoomDB database = RoomDB.getInstance(context.getApplicationContext());
        setWorkouts(database.workoutDao().getAllCustomWorkouts());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private MaterialCardView workoutCardView;
        private TextView workoutTitle, workoutDesc;
        private Button startBtn, editBtn;
        private ImageButton deleteBtn, shareBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            workoutCardView = itemView.findViewById(R.id.workoutCardView);
            workoutTitle = itemView.findViewById(R.id.workoutTitle);
            workoutDesc = itemView.findViewById(R.id.workoutDesc);
            startBtn = itemView.findViewById(R.id.startBtn);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            shareBtn = itemView.findViewById(R.id.shareBtn);
        }
    }
}
