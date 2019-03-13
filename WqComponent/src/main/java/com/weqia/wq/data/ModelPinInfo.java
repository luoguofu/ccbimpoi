package com.weqia.wq.data;

import com.weqia.data.UtilData;

/**
 * Created by berwin on 2017/9/5.
 */


public class ModelPinInfo extends UtilData{
    private String name;
    private String info;
    private String viewInfo;
    private String floorName;
    private int floorId;
    private String type; //视口0   标注1   构件2
    private String nodeId;
    private String posfile;
    private String handle;
    private String componentId;
    private String nodeType;

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public ModelPinInfo() {
    }

    public ModelPinInfo(String name, String info, String floorName, String type, String nodeId, String componentId) {
        this.name = name;
        this.info = info;
        this.floorName = floorName;
        this.type = type;
        this.nodeId = nodeId;
        this.componentId = componentId;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getViewInfo() {
        return viewInfo;
    }

    public void setViewInfo(String viewInfo) {
        this.viewInfo = viewInfo;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getPosfile() {
        return posfile;
    }

    public void setPosfile(String posfile) {
        this.posfile = posfile;
    }

    public enum ModePinType {
        SEEPOS(1, "视口"),
        MARK(2, "标注"),
        COMPONENT(3, "构件");

        private String strName;
        private int value;

        private ModePinType(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public String strName() {
            return strName;
        }

        public int value() {
            return value;
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public int getFloorId() {
        return floorId;
    }

    public void setFloorId(int floorId) {
        this.floorId = floorId;
    }
}