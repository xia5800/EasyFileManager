package com.efm.system.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.efm.core.param.FileUploadParam;
import com.efm.core.utils.FileHandleUtil;
import com.efm.system.entity.SysFolder;
import com.efm.system.mapper.SysFolderMapper;
import com.efm.system.service.SysFolderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author gcc
 * @date 2020/8/24 15:53
 */
@Service
public class SysFolderServiceImpl extends ServiceImpl<SysFolderMapper, SysFolder> implements SysFolderService {
    Logger log = LoggerFactory.getLogger(SysFolderServiceImpl.class);

    @Autowired
    private FileUploadParam fileUploadParam;


    /**
     * 创建文件夹
     * @param path
     * @param createUser
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String createFolder(String path, String createUser) {
        if(StrUtil.isEmpty(path)) {
            // 1代表用户文件夹根目录
            return "1";
        }

        // 是否只有一级目录
        if(!path.contains("/")) {
            // 查询该用户创建的文件夹
            List<SysFolder> sysFolder = this.list(
                    new QueryWrapper<SysFolder>()
                            .eq("folder_name", path)
                            .eq("create_user", createUser)
            );
            // 如果文件夹存在，返回文件夹id
            if(ObjectUtil.isNotEmpty(sysFolder)){
                return sysFolder.get(0).getFolderId();
            }

            path = path.replace("/", "");
            // 否则创建文件夹
            String joinPath = FileHandleUtil.joinPath(new String[]{
                    File.listRoots()[fileUploadParam.getUploadDisIndex()].getPath(),
                    fileUploadParam.getUploadDir(),
                    createUser,
                    path
            });

            // 创建文件夹路径
            FileHandleUtil.createDirIfNotExists(joinPath);
            // 保存文件夹数据, 并返回文件夹id
            SysFolder folder = new SysFolder();
            folder.setFolderName(path);
            folder.setFolderPath(path);
            folder.setFolderParentId("1");
            folder.setLevel(1);
            folder.setCreateTime(new Date());
            folder.setUpdateTime(new Date());
            folder.setCreateUser(createUser);
            this.saveOrUpdate(folder);
            return folder.getFolderId();
        }
        // 如果有多级目录，创建多级文件夹
        String[] pathArray = path.split("/");
        return createFolders(pathArray, createUser);
    }

    /**
     * 创建文件夹
     * @param sysFolder
     * @return
     */
    @Override
    public boolean saveFolder(SysFolder sysFolder) {
        // 获取父目录的层级数
        SysFolder parentFolder = this.getById(sysFolder.getFolderParentId());
        if (ObjectUtil.isEmpty(parentFolder)) {
            log.error("父目录不存在");
            return false;
        }


        sysFolder.setCreateTime(new Date());
        sysFolder.setUpdateTime(new Date());
        sysFolder.setLevel(parentFolder.getLevel() + 1);
        sysFolder.setCreateUser(fileUploadParam.getUserNameDir());

        // 否则创建文件夹
        String joinPath = FileHandleUtil.joinPath(new String[]{
                File.listRoots()[fileUploadParam.getUploadDisIndex()].getPath(),
                fileUploadParam.getUploadDir(),
                fileUploadParam.getUserNameDir(),
                sysFolder.getFolderPath()
        });
        // 创建文件夹路径
        FileHandleUtil.createDirIfNotExists(joinPath);
        return this.save(sysFolder);
    }

    /**
     * 创建多级文件夹
     * @param pathArray
     * @param createUser
     * @return
     */
    public String createFolders(String[] pathArray, String createUser) {
        // 根目录
        String folderParentId = "1";
        String temp = "";
        for(int i=0; i< pathArray.length; i++) {
            String path = pathArray[i];
            // 查询该用户创建的文件夹
            List<SysFolder> sysFolder = this.list(
                    new QueryWrapper<SysFolder>()
                            .eq("folder_name", path)
                            .eq("create_user", createUser)
            );
            // 如果文件夹存在，返回文件夹id
            if(ObjectUtil.isNotEmpty(sysFolder)){
                folderParentId = sysFolder.get(0).getFolderId();
                temp = temp + path + "/";
                continue;
            }
            temp = temp + path + "/";

            // 否则创建文件夹
            String joinPath = FileHandleUtil.joinPath(new String[]{
                    File.listRoots()[fileUploadParam.getUploadDisIndex()].getPath(),
                    fileUploadParam.getUploadDir(),
                    createUser,
                    temp
            });
            // 创建文件夹路径
            FileHandleUtil.createDirIfNotExists(joinPath);
            // 保存文件夹数据, 并返回文件夹id
            SysFolder folder = new SysFolder();
            folder.setFolderName(path);
            folder.setFolderPath(temp);
            folder.setFolderParentId(folderParentId);
            folder.setLevel(i+1);
            folder.setCreateTime(new Date());
            folder.setUpdateTime(new Date());
            folder.setCreateUser(createUser);
            this.save(folder);
            folderParentId = folder.getFolderId();
        }
        return folderParentId;
    }

    /**
     * 修改文件夹
     * @param sysFolder
     * @return
     */
    @Override
    public boolean editFolder(SysFolder sysFolder) {
//        SysFolder folder = this.getById(sysFolder.getFolderId());
//
//        // 获取原来文件夹路径
//        String folderPath = folder.getFolderPath();
//        String serverDir = FileHandleUtil.joinPath(new String [] {
//                File.listRoots()[fileUploadParam.getUploadDisIndex()].getPath(),
//                fileUploadParam.getUploadDir(),
//                fileUploadParam.getUserNameDir(),
//                folderPath
//        });
//
//        File file = new File(serverDir);
//        // 把原 文件夹名 修改成 现文件夹名
//        FileUtil.rename(file, sysFolder.getFolderName(), true, false);

        sysFolder.setUpdateTime(new Date());
        return this.updateById(sysFolder);
    }

    /**
     * 删除文件夹
     * @param folderId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delFolder(String folderId) {
        SysFolder sysFolder = this.getById(folderId);
        if(ObjectUtil.isEmpty(sysFolder)){
            log.error("未获取到要删除文件夹的数据");
            return false;
        }
        // 获取文件夹完整路径
        String serverDir = FileHandleUtil.joinPath(new String[] {
                File.listRoots()[fileUploadParam.getUploadDisIndex()].getPath(),
                fileUploadParam.getUploadDir(),
                fileUploadParam.getUserNameDir(),
                sysFolder.getFolderPath()
        });

        List<String> childrenFolderIds = new ArrayList<>();
        // 查询当前目录以及子目录
        List<SysFolder> folderList = baseMapper.listChildren(folderId);
        folderList.forEach(folder ->{
            childrenFolderIds.add(folder.getFolderId());
        });

        // 删除子目录和当前目录
        if(!this.removeByIds(childrenFolderIds)) {
            throw new RuntimeException("删除当前目录以及子目录失败");
        }

        // 删除文件夹
        return FileUtil.del(serverDir);
    }


}
