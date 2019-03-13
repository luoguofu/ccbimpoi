package com.weqia.wq.component.qr;

import com.weqia.wq.data.BaseData;

/**
 * <pre>
 *     author: MLLWF
 *     time  : 2017/12/5
 *     desc  :扫描模型二维码返回info里所包含的数据
 * </pre>
 */
public class QRModeData extends BaseData {
    public enum QRModeEnum {
        YES(1, "显示"),
        NO(2, "不显示"),
        //
        ;
        private int value;
        private String strName;

        QRModeEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

        public static QRModeEnum valueOf(int value) {
            for (QRModeEnum requestType : QRModeEnum.values()) {
                if (requestType.getValue() == value) {
                    return requestType;
                }
            }
            return NO;
        }
    }

    private String qrCodeId;
    private int needPassword;  //1 true  2  false

    public String getQrCodeId() {
        return qrCodeId;
    }

    public void setQrCodeId(String qrCodeId) {
        this.qrCodeId = qrCodeId;
    }

    public int getNeedPassword() {
        return needPassword;
    }

    public void setNeedPassword(int needPassword) {
        this.needPassword = needPassword;
    }
}
