package com.atguigu.imperial.court.entity;

public class Memorials {

    private Integer memorialsId;
    private String memorialsTitle;
    private String memorialsContent;

    // 奏折摘要数据库没有，这里是为了配合页面显示
    private String memorialsContentDigest;
    private Integer memorialsEmp;

    // 员工姓名数据库没有，这里是为了配合页面显示
    private String memorialsEmpName;
    private String memorialsCreateTime;
    private String feedbackTime;
    private String feedbackContent;
    private Integer memorialsStatus;

    public Integer getMemorialsId() {
        return memorialsId;
    }

    public void setMemorialsId(Integer memorialsId) {
        this.memorialsId = memorialsId;
    }

    public String getMemorialsTitle() {
        return memorialsTitle;
    }

    public void setMemorialsTitle(String memorialsTitle) {
        this.memorialsTitle = memorialsTitle;
    }

    public String getMemorialsContent() {
        return memorialsContent;
    }

    public void setMemorialsContent(String memorialsContent) {
        this.memorialsContent = memorialsContent;
    }

    public String getMemorialsContentDigest() {
        return memorialsContentDigest;
    }

    public void setMemorialsContentDigest(String memorialsContentDigest) {
        this.memorialsContentDigest = memorialsContentDigest;
    }

    public Integer getMemorialsEmp() {
        return memorialsEmp;
    }

    public void setMemorialsEmp(Integer memorialsEmp) {
        this.memorialsEmp = memorialsEmp;
    }

    public String getMemorialsEmpName() {
        return memorialsEmpName;
    }

    public void setMemorialsEmpName(String memorialsEmpName) {
        this.memorialsEmpName = memorialsEmpName;
    }

    public String getMemorialsCreateTime() {
        return memorialsCreateTime;
    }

    public void setMemorialsCreateTime(String memorialsCreateTime) {
        this.memorialsCreateTime = memorialsCreateTime;
    }

    public String getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(String feedbackTime) {
        this.feedbackTime = feedbackTime;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }

    public Integer getMemorialsStatus() {
        return memorialsStatus;
    }

    public void setMemorialsStatus(Integer memorialsStatus) {
        this.memorialsStatus = memorialsStatus;
    }

    @Override
    public String toString() {
        return "Memorials{" +
                "memorialsId=" + memorialsId +
                ", memorialsTitle='" + memorialsTitle + '\'' +
                ", memorialsContent='" + memorialsContent + '\'' +
                ", memorialsContentDigest='" + memorialsContentDigest + '\'' +
                ", memorialsEmp=" + memorialsEmp +
                ", memorialsEmpName='" + memorialsEmpName + '\'' +
                ", memorialsCreateTime='" + memorialsCreateTime + '\'' +
                ", feedbackTime='" + feedbackTime + '\'' +
                ", feedbackContent='" + feedbackContent + '\'' +
                ", memorialsStatus=" + memorialsStatus +
                '}';
    }

    public Memorials(Integer memorialsId, String memorialsTitle, String memorialsContent, String memorialsContentDigest, Integer memorialsEmp, String memorialsEmpName, String memorialsCreateTime, String feedbackTime, String feedbackContent, Integer memorialsStatus) {
        this.memorialsId = memorialsId;
        this.memorialsTitle = memorialsTitle;
        this.memorialsContent = memorialsContent;
        this.memorialsContentDigest = memorialsContentDigest;
        this.memorialsEmp = memorialsEmp;
        this.memorialsEmpName = memorialsEmpName;
        this.memorialsCreateTime = memorialsCreateTime;
        this.feedbackTime = feedbackTime;
        this.feedbackContent = feedbackContent;
        this.memorialsStatus = memorialsStatus;
    }

    public Memorials() {
    }
}
