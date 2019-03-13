package com.weqia.wq.service;

import java.util.List;

import com.weqia.wq.data.BaseData;

public interface CallSaveInterface {

    void callSaveData(List<? extends BaseData> tmpContacts, float percent, int overper, boolean wantCheck);
}
