package com.efm.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.efm.system.entity.SysFolder;

/**
 * @author gcc
 * @date 2020/8/24 15:52
 */
public interface SysFolderService extends IService<SysFolder> {

    /**
     * 创建文件夹
     * @param path         目录路径
     * @param createUser   创建用户
     * @return
     */
    String createFolder(String path, String createUser);


    /**
     * 创建文件夹
     * @param sysFolder
     * @return
     */
    boolean saveFolder(SysFolder sysFolder);

    /**
     * 修改文件夹
     * @param sysFolder
     * @return
     */
    boolean editFolder(SysFolder sysFolder);


    /**
     * 删除文件夹
     * @param folderId
     * @return
     */
    boolean delFolder(String folderId);

}
