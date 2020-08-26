package com.efm.core.param;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author gcc
 * @date 2020/8/24 17:20
 */
@Component
@ConfigurationProperties(prefix = "fileupload.config")
public class FileUploadParam {

    /** 上传到第几个盘符下 */
    private Integer uploadDisIndex;

    /** 存储文件的目录名称 */
    private String uploadDir;

    /** 存放用户数据的文件夹名称 */
    private String userNameDir;

    public Integer getUploadDisIndex() {
        return uploadDisIndex;
    }

    public void setUploadDisIndex(Integer uploadDisIndex) {
        this.uploadDisIndex = uploadDisIndex;
    }

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public String getUserNameDir() {
        return userNameDir;
    }

    public void setUserNameDir(String userNameDir) {
        this.userNameDir = userNameDir;
    }

}
