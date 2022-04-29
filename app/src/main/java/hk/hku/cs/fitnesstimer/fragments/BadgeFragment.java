package hk.hku.cs.fitnesstimer.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import hk.hku.cs.fitnesstimer.MainActivity;
import hk.hku.cs.fitnesstimer.R;
import hk.hku.cs.fitnesstimer.WorkoutRecViewAdapter;
import hk.hku.cs.fitnesstimer.db.RoomDB;
import hk.hku.cs.fitnesstimer.db.Workout;


public class BadgeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private TextView cwTV, tsTV;


    public BadgeFragment() {
    }

    public static BadgeFragment newInstance(String param1, String param2) {
        BadgeFragment fragment = new BadgeFragment();
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

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_badge, container, false);
        cwTV = view.findViewById(R.id.cwTV);
        tsTV = view.findViewById(R.id.tsTV);

        cwTV.setText(""+read("cw"));

        long ts = read("ts");
        long hour = ts / 3600;
        long minute = (ts % 3600) / 60;
        long second = ts % 60;
        @SuppressLint("DefaultLocale") String timeString = String.format("%02d:%02d:%02d", hour, minute, second);
        tsTV.setText(timeString);

        return view;
    }

    public long read(String tag) {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("Stat", Context.MODE_PRIVATE);
        return sharedPreferences.getLong(tag, 0);
    }
}