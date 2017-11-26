package com.repolho.natour.model;

/**
 * Created by repolho on 03/10/16.
 */
public class Detail {

    private Integer dangerousness;
    private String walkingAverage;
    private Integer accessDifficulty;
    private Integer wheelchairDifficulty;

    public Detail() {}

    public Detail(Integer dangerousness, String walkingAverage, Integer accessDifficulty, Integer wheelchairDifficulty) {
        this.dangerousness = dangerousness;
        this.walkingAverage = walkingAverage;
        this.accessDifficulty = accessDifficulty;
        this.wheelchairDifficulty = wheelchairDifficulty;
    }

    public Integer getDangerousness() {
        return dangerousness;
    }

    public void setDangerousness(Integer dangerousness) {
        this.dangerousness = dangerousness;
    }

    public String getWalkingAverage() {
        return walkingAverage;
    }

    public void setWalkingAverage(String walkingAverage) {
        this.walkingAverage = walkingAverage;
    }

    public Integer getAccessDifficulty() {
        return accessDifficulty;
    }

    public void setAccessDifficulty(Integer accessDifficulty) {
        this.accessDifficulty = accessDifficulty;
    }

    public Integer getWheelchairDifficulty() {
        return wheelchairDifficulty;
    }

    public void setWheelchairDifficulty(Integer wheelchairDifficulty) {
        this.wheelchairDifficulty = wheelchairDifficulty;
    }
}
