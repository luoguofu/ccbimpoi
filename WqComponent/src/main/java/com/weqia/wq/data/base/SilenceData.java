package com.weqia.wq.data.base;

import com.weqia.utils.annotation.sqlite.Id;
import com.weqia.utils.annotation.sqlite.Table;
import com.weqia.wq.data.BaseData;

@Table(name = "silence_data")
public class SilenceData extends BaseData {
    private static final long serialVersionUID = 1L;
    private
    @Id
    String business_id;//唯一性,type_id
    private Integer voiceType; //声音类型  1有声音 2无声音


    public SilenceData() {
    }

    public SilenceData(String business_id,  Integer voiceType) {
        this.business_id = business_id;
        this.voiceType = voiceType;
    }

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }


    public Integer getVoiceType() {
        return voiceType;
    }

    public void setVoiceType(Integer voiceType) {
        this.voiceType = voiceType;
    }
}
