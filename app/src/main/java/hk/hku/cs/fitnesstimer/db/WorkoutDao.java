package hk.hku.cs.fitnesstimer.db;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WorkoutDao {
    @Query("SELECT * FROM workout_table WHERE define_type = 1")
    List<Workout> getAllPredefineWorkouts();

    @Query("SELECT * FROM workout_table WHERE define_type = 0")
    List<Workout> getAllCustomWorkouts();

    @Query("UPDATE workout_table SET workout_name = :name, description = :desc, number_of_exercise = :numExe, exercises_information = :info WHERE wid = :wid")
    void updateWorkout(int wid, String name, String desc, int numExe, String info);

    @Query("DELETE FROM workout_table")
    void deleteAll();

    @Query("DELETE FROM workout_table WHERE wid = :wid")
    void deleteWorkout(int wid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWorkout(Workout workout);

    @Update
    void updateWorkout(Workout workout);

    @Delete
    void deleteWorkout(Workout workout);

}
