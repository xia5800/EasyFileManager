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
 * 文件分片表
 * @author gcc
 * @date 2020/8/24 15:11
 */
@TableName("sys_file_zone")
public class SysFileZone implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 分片ID */
    @TableId(value="id", type= IdType.ASSIGN_UUID)
    private String id;

    /** 文件id */
    @TableField("file_id")
    private String fileId;

    /** 分片名称 */
    @TableField("zone_name")
    private String zoneName;

    /** 分片路径 */
    @TableField("zone_path")
    private String zonePath;

    /** 分片md5值 */
    @TableField("zone_md5")
    private String zoneMd5;

    /** 分片总数 */
    @TableField("zone_total_count")
    private Integer zoneTotalCount;

    /** 分片前文件的md5值 */
    @TableField("file_md5")
    private String fileMd5;

    /** 文件大小 */
    @TableField("file_size")
    private Long fileSize;

    /** 当前分片索引 */
    @TableField("zone_index")
    private Integer zoneIndex;

    /** 分片开始位置 */
    @TableField("zone_start_size")
    private Long zoneStartSize;

    /** 分片结束位置 */
    @TableField("zone_end_size")
    private Long zoneEndSize;

    /** 文件相对路径 */
    @TableField("relative_path")
    private String relativePath;

    /** 创建时间 */
    @TableField("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 创建人 */
    @TableField("create_user")
    private String createUser;

    /** 当前所在文件夹 */
    @TableField(exist = false)
    private String path;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getZonePath() {
        return zonePath;
    }

    public void setZonePath(String zonePath) {
        this.zonePath = zonePath;
    }

    public String getZoneMd5() {
        return zoneMd5;
    }

    public void setZoneMd5(String zoneMd5) {
        this.zoneMd5 = zoneMd5;
    }

    public Integer getZoneTotalCount() {
        return zoneTotalCount;
    }

    public void setZoneTotalCount(Integer zoneTotalCount) {
        this.zoneTotalCount = zoneTotalCount;
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

    public Integer getZoneIndex() {
        return zoneIndex;
    }

    public void setZoneIndex(Integer zoneIndex) {
        this.zoneIndex = zoneIndex;
    }

    public Long getZoneStartSize() {
        return zoneStartSize;
    }

    public void setZoneStartSize(Long zoneStartSize) {
        this.zoneStartSize = zoneStartSize;
    }

    public Long getZoneEndSize() {
        return zoneEndSize;
    }

    public void setZoneEndSize(Long zoneEndSize) {
        this.zoneEndSize = zoneEndSize;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
