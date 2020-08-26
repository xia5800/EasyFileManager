package com.efm.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.efm.system.entity.SysFolder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author gcc
 * @date 2020/8/24 15:50
 */
public interface SysFolderMapper extends BaseMapper<SysFolder> {

    /**
     * 查询指定目录下所有的目录（包括指定目录）
     * @param folderId
     * @return
     */
    List<SysFolder> listChildren(@Param("folderId")String folderId);

}
