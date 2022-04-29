package hk.hku.cs.fitnesstimer.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "workout_table")
public class Workout {
    public Workout(boolean defineType, String workoutName, String description, int numExercise, String exercisesInfo) {
        this.defineType = defineType;
        this.workoutName = workoutName;
        this.description = description;
        this.numExercise = numExercise;
        this.exercisesInfo = exercisesInfo;
    }

    @PrimaryKey(autoGenerate = true)
    private int wid;

    @ColumnInfo(name = "define_type")
    private boolean defineType;

    @ColumnInfo(name = "workout_name")
    private String workoutName;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "number_of_exercise")
    private int numExercise;

    @ColumnInfo(name = "exercises_information")
    private String exercisesInfo;


    public boolean isDefineType() {
        return defineType;
    }

    public void setDefineType(boolean defineType) {
        this.defineType = defineType;
    }

    public int getWid() {
        return wid;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumExercise() {
        return numExercise;
    }

    public void setNumExercise(int numExercise) {
        this.numExercise = numExercise;
    }

    public String getExercisesInfo() {
        return exercisesInfo;
    }

    public void setExercisesInfo(String exercisesInfo) {
        this.exercisesInfo = exercisesInfo;
    }
}
