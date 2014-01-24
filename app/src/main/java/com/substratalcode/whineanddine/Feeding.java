package com.substratalcode.whineanddine;

import java.util.Date;

public class Feeding {
    public Date startTime;
    public long leftTime = 0l;
    public long rightTime = 0l;

    public Feeding() {
        startTime = new Date();
    }
}
