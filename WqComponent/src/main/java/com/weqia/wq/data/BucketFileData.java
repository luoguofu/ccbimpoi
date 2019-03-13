package com.weqia.wq.data;

import com.weqia.utils.annotation.sqlite.Id;
import com.weqia.utils.annotation.sqlite.Table;

import java.util.List;

/**
 * 转换地址
 */
@Table(name = "bucket_file_data")
public class BucketFileData extends BaseData {

    public enum BucketFileType {
        NO(0, "未转化"), //
        DOING(1, "转换中"), //
        SUCCESS(2, "成功"),
        FAIL(3, "失败");

        private String strName;
        private int value;

        BucketFileType(int value, String strName) {
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

    @Id
    private int id;
    private static final long serialVersionUID = 1L;
    private Integer accountType;

    /**
     * 存在NULL就未转换好
     */
    private String fileBucket;
    private String fileKey;

    private List<BucketFileData> fileConvertResults;  //模型转化不必须的集合字段
    private List<BucketFileData> fileConvertResultsSenior;  //模型的必需集合字段
    private String fileConvertResultsString;
    private String fileConvertResultsSeniorString;
    private String fileSize;  //文件大小


    /**
     * 唯一性约束
     */
    private String versionId;
    private String convertTime;       //转化结果版本号时间戳
    private int nodeType;

    private Integer convertStatus; //转换状态    0 未转化 1 转换中  2 成功  3  失败

    private String convertResult; //转换结果

    public BucketFileData() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public String getFileBucket() {
        return fileBucket;
    }

    public void setFileBucket(String fileBucket) {
        this.fileBucket = fileBucket;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public int getNodeType() {
        return nodeType;
    }

    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }

    public Integer getConvertStatus() {
        return convertStatus;
    }

    public void setConvertStatus(Integer convertStatus) {
        this.convertStatus = convertStatus;
    }

    public String getConvertResult() {
        return convertResult;
    }

    public void setConvertResult(String convertResult) {
        this.convertResult = convertResult;
    }

    public List<BucketFileData> getFileConvertResults() {
        return fileConvertResults;
    }

    public void setFileConvertResults(List<BucketFileData> fileConvertResults) {
        this.fileConvertResults = fileConvertResults;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public List<BucketFileData> getFileConvertResultsSenior() {
        return fileConvertResultsSenior;
    }

    public void setFileConvertResultsSenior(List<BucketFileData> fileConvertResultsSenior) {
        this.fileConvertResultsSenior = fileConvertResultsSenior;
    }

    public String getConvertTime() {
        return convertTime;
    }

    public void setConvertTime(String convertTime) {
        this.convertTime = convertTime;
    }

    public String getFileConvertResultsString() {
        return fileConvertResultsString;
    }

    public void setFileConvertResultsString(String fileConvertResultsString) {
        this.fileConvertResultsString = fileConvertResultsString;
    }

    public String getFileConvertResultsSeniorString() {
        return fileConvertResultsSeniorString;
    }

    public void setFileConvertResultsSeniorString(String fileConvertResultsSeniorString) {
        this.fileConvertResultsSeniorString = fileConvertResultsSeniorString;
    }
}
