package com.weqia.wq.data;


public class UnitData extends BaseData {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private String unitName;
    private String unitPeople;
    private String unitPhone;
    public String getUnitName() {
        return unitName;
    }
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
    public String getUnitPeople() {
        return unitPeople;
    }
    public void setUnitPeople(String unitPeople) {
        this.unitPeople = unitPeople;
    }
    public String getUnitPhone() {
        return unitPhone;
    }
    public void setUnitPhone(String unitPhone) {
        this.unitPhone = unitPhone;
    }
    public UnitData(String unitName, String unitPeople, String unitPhone) {
        super();
        if (unitName == null) {
            unitName = "";
        }
        if (unitPeople == null) {
            unitPeople = "";
        }
        if (unitPhone == null) {
            unitPhone = "";
        }
        this.unitName = unitName;
        this.unitPeople = unitPeople;
        this.unitPhone = unitPhone;
    }
    
    public UnitData() {
    }
}
