package com.efm.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.efm.system.entity.SysFileZone;
import com.efm.system.mapper.SysFileZoneMapper;
import com.efm.system.service.SysFileZoneService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author gcc
 * @date 2020/8/24 15:53
 */
@Service
public class SysFileZoneServiceImpl extends ServiceImpl<SysFileZoneMapper, SysFileZone> implements SysFileZoneService {

    /**
     * 通过分片md5以及文件总md5查询该分片的信息
     * @param zoneMd5 分片的md5值
     * @param fileMd5 文件的md5值
     * @return
     */
    @Override
    public SysFileZone findByZoneMd5AndFileMd5(String zoneMd5, String fileMd5) {
        return this.getOne(
                new QueryWrapper<SysFileZone>()
                    .eq("file_md5", fileMd5)
                    .eq("zone_md5", zoneMd5)
        );
    }

    /**
     * 通过文件的md5值查询该文件的所有分片信息
     * @param fileMd5 文件的md5值
     * @return
     */
    @Override
    public List<SysFileZone> findByFileMd5(String fileMd5) {
        return this.list(
                new QueryWrapper<SysFileZone>()
                    .eq("file_md5", fileMd5)
                    .orderByAsc("zone_index")
        );
    }

}
