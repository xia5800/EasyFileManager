package com.efm.system.vo;

import java.util.Date;

/**
 * 文件列表
 * @author gcc
 * @date 2020/8/25 14:58
 */
public class FolderFileVo {

    /** 文件或目录id */
    private String id;

    /** 文件或目录名称 */
    private String name;

    /** 文件大小 */
    private String size;

    /** 文件类型 */
    private String fileType;

    /** 修改时间 */
    private Date updateTime;

    /** 当前目录相对路径 */
    private String folderPath;

    /** 上级目录id */
    private String parentId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

}
