package com.efm.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 目录表
 * @author gcc
 * @date 2020/8/24 15:11
 */
@TableName("sys_folder")
public class SysFolder implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 目录id */
    @TableId(value = "folder_id", type = IdType.ASSIGN_UUID)
    private String folderId;

    /** 目录名称 */
    @TableField("folder_name")
    private String folderName;

    /** 目录路径 */
    @TableField("folder_path")
    private String folderPath;

    /** 上级目录id */
    @TableField("folder_parent_id")
    private String folderParentId;

    /** 创建时间 */
    @TableField("create_time")
    private Date createTime;

    /** 修改时间 */
    @TableField("update_time")
    private Date updateTime;

    /** 创建人 */
    @TableField("create_user")
    private String createUser;

    /** 层级 */
    @TableField("level")
    private Integer level;

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public String getFolderParentId() {
        return folderParentId;
    }

    public void setFolderParentId(String folderParentId) {
        this.folderParentId = folderParentId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
