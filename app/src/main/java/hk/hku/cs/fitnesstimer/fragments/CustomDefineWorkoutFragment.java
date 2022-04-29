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

import hk.hku.cs.fitnesstimer.MainActivity;
import hk.hku.cs.fitnesstimer.R;
import hk.hku.cs.fitnesstimer.WorkoutRecViewAdapter;
import hk.hku.cs.fitnesstimer.db.RoomDB;
import hk.hku.cs.fitnesstimer.db.Workout;


public class CustomDefineWorkoutFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private Context cwContext;
    private RecyclerView cwRecView;

    public CustomDefineWorkoutFragment() {
    }

    public static CustomDefineWorkoutFragment newInstance(String param1, String param2) {
        CustomDefineWorkoutFragment fragment = new CustomDefineWorkoutFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_define_workout, container, false);

        cwContext = view.getContext();
        cwRecView = view.findViewById(R.id.cwRecView);

        RoomDB database = RoomDB.getInstance(cwContext);
        List<Workout> workouts = database.workoutDao().getAllCustomWorkouts();

        WorkoutRecViewAdapter workoutRecViewAdapter = new WorkoutRecViewAdapter(cwContext);
        workoutRecViewAdapter.setWorkouts(workouts);

        cwRecView.setAdapter(workoutRecViewAdapter);
        cwRecView.setLayoutManager(new LinearLayoutManager(cwContext));

        return view;
    }
}