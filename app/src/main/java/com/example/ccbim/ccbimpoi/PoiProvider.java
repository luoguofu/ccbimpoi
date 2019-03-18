package com.example.ccbim.ccbimpoi;

import com.linked.annotion.Provider;
import com.spinytech.macore.MaProvider;

/**
 * Created by wanglei on 2016/12/28.
 */
@Provider(processName = "com.example.ccbim.ccbimpoi")
public class PoiProvider extends MaProvider {
    @Override
    protected String getName() {
        return "pvpoi";
    }
}
