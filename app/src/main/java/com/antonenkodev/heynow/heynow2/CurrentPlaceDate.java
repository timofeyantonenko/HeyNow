package com.antonenkodev.heynow.heynow2;

import android.app.Application;

/**
 * Created by root on 21.04.15.
 */
public class CurrentPlaceDate extends Application{
    //Insta_access_token
    private String insta_token;
    private boolean firstReg=true;

    public boolean isFirstReg() {
        return firstReg;
    }

    public void setFirstReg(boolean firstReg) {
        this.firstReg = firstReg;
    }

    private int maxRadius=1000;

    public int getMaxRadius() {
        return maxRadius;
    }

    public void setMaxRadius(int maxRadius) {
        this.maxRadius = maxRadius;
    }

    public String getInsta_token() {
        return insta_token;
    }

    public void setInsta_token(String insta_token) {
        this.insta_token = insta_token;
    }

    private boolean insta;
    private boolean vk;

    public boolean isInsta() {
        return insta;
    }

    public void setInsta(boolean insta) {
        this.insta = insta;
    }

    public boolean isVk() {
        return vk;
    }

    public void setVk(boolean vk) {
        this.vk = vk;
    }

    //текущая выбранная долгота
    private double coordX;
    //текущая выбранная широта
    private double coordY;
    //моя текущая долгота
    private double myCoordX;
    //моя текущая широта
    private double myCoordY;

    private boolean firstGallery = true;

    public boolean isFirstGallery() {
        return firstGallery;
    }

    public void setFirstGallery(boolean firstGallery) {
        this.firstGallery = firstGallery;
    }

    public void setCoordX(double coordX) {
        this.coordX = coordX;
    }

    public void setCoordY(double coordY) {
        this.coordY = coordY;
    }

    public void setMyCoordX(double myCoordX) {
        this.myCoordX = myCoordX;
    }

    public void setMyCoordY(double myCoordY) {
        this.myCoordY = myCoordY;
    }

    public void setMax_time(long max_time) {
        this.max_time = max_time;
    }

    public void setMin_time(long min_time) {
        this.min_time = min_time;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    //время окончания отсчета
    private long Imax_time;
    //время начала отсчета
    private long Imin_time;
    //время окончания отсчета
    private long Vmax_time;
    //время начала отсчета
    private long Vmin_time;
    //время окончания отсчета
    private long max_time;
    //время начала отсчета
    private long min_time;
    //время окончания отсчета
    private long ILmax_time;
    //время начала отсчета
    private long ILmin_time;
    //время окончания отсчета
    private long VLmax_time;
    //время начала отсчета
    private long VLmin_time;
    //время окончания отсчета
    private long Lmax_time;
    //время начала отсчета
    private long Lmin_time;

    private long V_date_incr=432000;
    private long I_date_incr=259200;

    public long getImax_time() {
        return Imax_time;
    }

    public void setImax_time(long imax_time) {
        Imax_time = imax_time;
    }

    public long getImin_time() {
        return Imin_time;
    }

    public void setImin_time(long imin_time) {
        Imin_time = imin_time;
    }

    public long getVmax_time() {
        return Vmax_time;
    }

    public void setVmax_time(long vmax_time) {
        Vmax_time = vmax_time;
    }

    public long getVmin_time() {
        return Vmin_time;
    }

    public void setVmin_time(long vmin_time) {
        Vmin_time = vmin_time;
    }

    public long getILmax_time() {
        return ILmax_time;
    }

    public void setILmax_time(long ILmax_time) {
        this.ILmax_time = ILmax_time;
    }

    public long getILmin_time() {
        return ILmin_time;
    }

    public void setILmin_time(long ILmin_time) {
        this.ILmin_time = ILmin_time;
    }

    public long getVLmax_time() {
        return VLmax_time;
    }

    public void setVLmax_time(long VLmax_time) {
        this.VLmax_time = VLmax_time;
    }

    public long getVLmin_time() {
        return VLmin_time;
    }

    public void setVLmin_time(long VLmin_time) {
        this.VLmin_time = VLmin_time;
    }

    public long getLmax_time() {
        return Lmax_time;
    }

    public void setLmax_time(long lmax_time) {
        Lmax_time = lmax_time;
    }

    public long getLmin_time() {
        return Lmin_time;
    }

    public void setLmin_time(long lmin_time) {
        Lmin_time = lmin_time;
    }

    public long getV_date_incr() {
        return V_date_incr;
    }

    public void setV_date_incr(long v_date_incr) {
        V_date_incr = v_date_incr;
    }

    public long getI_date_incr() {
        return I_date_incr;
    }

    public void setI_date_incr(long i_date_incr) {
        I_date_incr = i_date_incr;
    }

    //текущий радиус поиска фотографий
    private int radius=1000;

    public double getCoordX() {
        return coordX;
    }

    public double getMyCoordX() {
        return myCoordX;
    }

    public double getMyCoordY() {
        return myCoordY;
    }

    public long getMax_time() {
        return max_time;
    }

    public long getMin_time() {
        return min_time;
    }

    public int getRadius() {
        return radius;
    }

    public double getCoordY() {
        return coordY;
    }
}
