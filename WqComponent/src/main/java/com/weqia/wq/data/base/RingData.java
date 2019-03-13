package com.weqia.wq.data.base;

import com.weqia.wq.data.BaseData;

public class RingData extends BaseData  {

	private static final long serialVersionUID = 1L;
	private String name;
    private String uri;
    private	Boolean checked = false;

    public RingData() {
    }

    public RingData(String name, String uri) {
        this.name = name;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
