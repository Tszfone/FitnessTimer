package hk.hku.cs.fitnesstimer.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

import hk.hku.cs.fitnesstimer.MainActivity;
import hk.hku.cs.fitnesstimer.R;
import hk.hku.cs.fitnesstimer.WorkoutRecViewAdapter;
import hk.hku.cs.fitnesstimer.db.RoomDB;
import hk.hku.cs.fitnesstimer.db.Workout;


public class PredefineWorkoutFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private Context pwContext;
    private RecyclerView pwRecView;

    public PredefineWorkoutFragment() {
    }

    public static PredefineWorkoutFragment newInstance(String param1, String param2) {
        PredefineWorkoutFragment fragment = new PredefineWorkoutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_predefine_workout, container, false);
        pwContext = view.getContext();
        pwRecView = view.findViewById(R.id.pwRecView);

        RoomDB database = RoomDB.getInstance(pwContext);
        List<Workout> workouts = database.workoutDao().getAllPredefineWorkouts();

        WorkoutRecViewAdapter workoutRecViewAdapter = new WorkoutRecViewAdapter(pwContext);
        workoutRecViewAdapter.setWorkouts(workouts);

        pwRecView.setAdapter(workoutRecViewAdapter);
        pwRecView.setLayoutManager(new LinearLayoutManager(pwContext));

        return view;
    }
}