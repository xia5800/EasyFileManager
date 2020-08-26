package com.efm.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.efm.system.entity.SysFileZone;

import java.util.List;

/**
 * @author gcc
 * @date 2020/8/24 15:52
 */
public interface SysFileZoneService extends IService<SysFileZone> {

    /**
     * 通过分片md5以及文件总md5查询该分片的信息
     * @param zoneMd5   分片的md5值
     * @param fileMd5   文件的md5值
     * @return
     */
    SysFileZone findByZoneMd5AndFileMd5(String zoneMd5, String fileMd5);


    /**
     * 通过文件的md5值查询该文件的所有分片信息
     * @param fileMd5   文件的md5值
     * @return
     */
    List<SysFileZone> findByFileMd5(String fileMd5);
}
