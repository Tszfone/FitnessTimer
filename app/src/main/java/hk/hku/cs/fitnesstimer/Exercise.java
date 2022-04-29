package hk.hku.cs.fitnesstimer;

public class Exercise {
    private String name;
    private int duration, restTime, numSet, numRep, timeGap;

    public Exercise(String name, int duration, int restTime, int numSet, int numRep, int timeGap) {
        this.name = name;
        this.duration = duration;
        this.restTime = restTime;
        this.numSet = numSet;
        this.numRep = numRep;
        this.timeGap = timeGap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getRestTime() {
        return restTime;
    }

    public void setRestTime(int restTime) {
        this.restTime = restTime;
    }

    public int getTimeGap() {
        return timeGap;
    }

    public void setTimeGap(int timeGap) {
        this.timeGap = timeGap;
    }

    public int getNumSet() {
        return numSet;
    }

    public void setNumSet(int numSet) {
        this.numSet = numSet;
    }

    public int getNumRep() {
        return numRep;
    }

    public void setNumRep(int numRep) {
        this.numRep = numRep;
    }
}
