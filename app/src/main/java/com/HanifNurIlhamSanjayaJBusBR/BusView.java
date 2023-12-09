package com.HanifNurIlhamSanjayaJBusBR;

public class BusView {
    private int mBusImgId;
    private String mBusName;
    private String mDepartureStation;
    private String mDestination;

    public BusView (int BusImgId, String BusName, String DepartureStation, String Destination) {
        mBusImgId = BusImgId;
        mBusName = BusName;
        mDepartureStation = DepartureStation;
        mDestination = Destination;
    }

    public int getBusImgId() {
        return mBusImgId;
    }

    public String getmBusName() {
        return mBusName;
    }

    public String getmDepartureStation() {
        return mDepartureStation;
    }

    public String getmDestination() {
        return mDestination;
    }
}
