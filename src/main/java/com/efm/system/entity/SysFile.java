package com.efm.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统文件表
 * @author gcc
 * @date 2020/8/24 14:46
 */
@TableName("sys_file")
public class SysFile implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 文件id */
    @TableId(value="id",type= IdType.ASSIGN_UUID)
    private String id;

    /** 源文件名 */
    @TableField("file_name")
    private String fileName;

    /** 服务器生成的文件名 */
    @TableField("server_local_name")
    private String serverLocalName;

    /** 服务器储存路径 */
    @TableField("server_local_path")
    private String serverLocalPath;

    /** 所在目录 */
    @TableField("folder_id")
    private String folderId;

    /** 文件md5值 */
    @TableField("file_md5")
    private String fileMd5;

    /** 文件大小 */
    @TableField("file_size")
    private Long fileSize;

    /** 文件后缀 */
    @TableField("file_suffix")
    private String fileSuffix;

    /** 文件类型 */
    @TableField("file_type")
    private String fileType;

    /** 是否合并 0否 1是 */
    @TableField("is_merge")
    private Integer isMerge;

    /** 是否分片 0否 1是 */
    @TableField("is_zone")
    private Integer isZone;

    /** 分片总数 */
    @TableField("zone_total")
    private Integer zoneTotal;


    /** 创建时间 */
    @TableField("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 修改时间 */
    @TableField("update_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 创建人 */
    @TableField("create_user")
    private String createUser;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getServerLocalName() {
        return serverLocalName;
    }

    public void setServerLocalName(String serverLocalName) {
        this.serverLocalName = serverLocalName;
    }

    public String getServerLocalPath() {
        return serverLocalPath;
    }

    public void setServerLocalPath(String serverLocalPath) {
        this.serverLocalPath = serverLocalPath;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Integer getIsMerge() {
        return isMerge;
    }

    public void setIsMerge(Integer isMerge) {
        this.isMerge = isMerge;
    }

    public Integer getIsZone() {
        return isZone;
    }

    public void setIsZone(Integer isZone) {
        this.isZone = isZone;
    }

    public Integer getZoneTotal() {
        return zoneTotal;
    }

    public void setZoneTotal(Integer zoneTotal) {
        this.zoneTotal = zoneTotal;
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
}
