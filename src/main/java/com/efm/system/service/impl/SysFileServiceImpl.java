package com.efm.system.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.efm.core.constant.GlobalConstants;
import com.efm.core.param.FileUploadParam;
import com.efm.core.utils.FileHandleUtil;
import com.efm.core.utils.JsonResult;
import com.efm.core.utils.PageParam;
import com.efm.core.utils.PageResult;
import com.efm.system.entity.SysFile;
import com.efm.system.entity.SysFileZone;
import com.efm.system.entity.SysFolder;
import com.efm.system.mapper.SysFileMapper;
import com.efm.system.service.SysFileService;
import com.efm.system.service.SysFileZoneService;
import com.efm.system.service.SysFolderService;
import com.efm.system.vo.FolderFileVo;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author gcc
 * @date 2020/8/24 15:53
 */
@Service
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile> implements SysFileService {
    Logger log = LoggerFactory.getLogger(SysFileServiceImpl.class);


    @Autowired
    private FileUploadParam fileUploadParam;
    @Autowired
    private SysFileZoneService sysFileZoneService;
    @Autowired
    private SysFolderService sysFolderService;


    /**
     * 关联分页查询
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<FolderFileVo> listPage(PageParam pageParam) {
        String name = pageParam.getString("name");
        String createUser = pageParam.getString("createUser");
        String folderId = pageParam.getString("folderId");
        if(StrUtil.isEmpty(folderId)) {
            folderId = "1";
        }
        // 查询文件夹
        QueryWrapper<SysFolder> sysFolderQueryWrapper = new QueryWrapper<SysFolder>();
        sysFolderQueryWrapper.eq("create_user", createUser);
        if(StrUtil.isNotEmpty(name)){
            sysFolderQueryWrapper.like("folder_name", name);
        }else{
            sysFolderQueryWrapper.eq("folder_parent_id", folderId);
        }
        sysFolderQueryWrapper.orderByAsc("folder_name");
        List<SysFolder> folderList = sysFolderService.list(sysFolderQueryWrapper);

        // 查询文件
        QueryWrapper<SysFile> sysFileQueryWrapper = new QueryWrapper<SysFile>();
        sysFileQueryWrapper.eq("create_user", createUser);
        if(StrUtil.isNotEmpty(name)){
            sysFileQueryWrapper.like("file_name", name);
        }else{
            sysFileQueryWrapper.eq("folder_id", folderId);
        }
        sysFileQueryWrapper.orderByAsc("file_size");
        List<SysFile> fileList = this.list(sysFileQueryWrapper);

        // 处理结果
        List<FolderFileVo> folderFileVoList = new ArrayList<>();
        folderList.forEach(folder -> {
            FolderFileVo folderFileVo = new FolderFileVo();
            folderFileVo.setFileType("dir");
            folderFileVo.setId(folder.getFolderId());
            folderFileVo.setName(folder.getFolderName());
            folderFileVo.setSize("");
            folderFileVo.setFolderPath(folder.getFolderPath());
            folderFileVo.setUpdateTime(folder.getUpdateTime());
            folderFileVo.setParentId(folder.getFolderParentId());
            folderFileVoList.add(folderFileVo);
        });
        fileList.forEach(file -> {
            FileTypeUtil.getType(file.getFileName());
            FolderFileVo folderFileVo = new FolderFileVo();
            folderFileVo.setFileType(file.getFileSuffix().replace(".",""));
            folderFileVo.setId(file.getId());
            folderFileVo.setName(file.getFileName());
            folderFileVo.setSize(file.getFileSize().toString());
            folderFileVo.setUpdateTime(file.getUpdateTime());
            folderFileVoList.add(folderFileVo);
        });
        return new PageResult<>(folderFileVoList, folderFileVoList.size());
    }

    /**
     * 上传分片
     * <pre>
     * ├─如果分片不存在, 判断文件是否存在
     * │  ├─如果文件不存在
     * │  │  └─保存文件信息，上传分片
     * │  └─如果文件存在
     * │      └─上传分片
     * └─如果分片存在
     *     └─不上传该分片
     * </pre>
     * @param file
     * @param sysFileZone
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public JsonResult zoneUpload(MultipartFile file, SysFileZone sysFileZone) {
        // 分片存放目录
        String localPath = "";
        // 分片文件MD5
        String zoneMd5 = "";
        // 文件类型
        String fileType = "";

        // 获取文件名
        String fileName = file.getOriginalFilename();
        if(GlobalConstants.BLOB.equals(fileName)) {
            fileName = sysFileZone.getZoneName();
        }
        // 获取文件后缀
        String fileSuffix = FileHandleUtil.getFileSuffix(fileName);
        // 获取文件相对路径
        String relativepath = sysFileZone.getRelativePath();
        if(StrUtil.isNotEmpty(relativepath)) {
            relativepath = sysFileZone.getPath() + relativepath.substring(0, relativepath.lastIndexOf("/"));
        }else {
            relativepath = sysFileZone.getPath();
        }

        try {
            // 获取分片md5
            if (StrUtil.isEmpty(sysFileZone.getZoneMd5())) {
                zoneMd5 = DigestUtils.md5DigestAsHex(file.getInputStream());
                sysFileZone.setZoneMd5(zoneMd5);
            } else {
                zoneMd5 = sysFileZone.getZoneMd5();
            }
            // 获取文件类型
            fileType = new Tika().detect(file.getInputStream());
        }catch (IOException e) {
            e.printStackTrace();
            log.error("文件不存在");
            return JsonResult.error("文件上传错误");
        }
        // 处理分片名 = 分片的MD5值 + 原文件后缀 + ”.temp“
        String zoneName = zoneMd5 + fileSuffix + GlobalConstants.TEMP_SUFFIX;

        // 查询分片信息
        SysFileZone zoneInfo = sysFileZoneService.findByZoneMd5AndFileMd5(sysFileZone.getZoneMd5(), sysFileZone.getFileMd5());
        // 1. 如果分片不存在, 判断文件是否存在
        if(ObjectUtil.isEmpty(zoneInfo)) {
            String fileId = "";
            // 通过文件md5和创建人查询文件信息
            SysFile sysFile = this.findByFileMd5AndCreateUser(sysFileZone.getFileMd5(), sysFileZone.getCreateUser());
            // 1.1 如果文件不存在
            if(ObjectUtil.isEmpty(sysFile)) {
                localPath = FileHandleUtil.joinPath(
                        new String[]{
                                File.listRoots()[fileUploadParam.getUploadDisIndex()].getPath(),
                                fileUploadParam.getUploadDir(),
                                GlobalConstants.TEMP_DIR_NAME,
                                sysFileZone.getCreateUser(),
                                sysFileZone.getFileMd5()});
                // 1.1.1 保存文件信息
                SysFile sFile = new SysFile();
                sFile.setFileSize(sysFileZone.getFileSize());
                sFile.setFileMd5(sysFileZone.getFileMd5());
                sFile.setServerLocalName(zoneName);
                sFile.setServerLocalPath(localPath);
                sFile.setFileName(fileName);
                sFile.setFileSuffix(fileSuffix);
                sFile.setFileType(fileType);
                sFile.setIsMerge(0);
                sFile.setIsZone(0);
                sFile.setFolderId("1");
                sFile.setZoneTotal(sysFileZone.getZoneTotalCount());
                sFile.setCreateTime(new Date());
                sFile.setCreateUser(sysFileZone.getCreateUser());
                this.save(sFile);
                fileId = sFile.getId();
            }else{// 如果文件存在
                // 是否合并过了？
                if(sysFile.getIsZone() == 1 && sysFile.getIsMerge() == 1) {
                    return JsonResult.error("文件已上传");
                }
                fileId = sysFile.getId();
                localPath = sysFile.getServerLocalPath();
            }

            try {
                // 将文件写入目录
                FileHandleUtil.upload(file.getInputStream(), localPath, zoneName);
            }catch (IllegalStateException | IOException e) {
                e.printStackTrace();
                throw new RuntimeException("文件上传错误");
            }


            //记录分片文件
            sysFileZone.setId(null);
            sysFileZone.setRelativePath(relativepath);
            sysFileZone.setZoneMd5(zoneMd5);
            sysFileZone.setFileId(fileId);
            sysFileZone.setZoneName(zoneName);
            sysFileZone.setZonePath(localPath);
            sysFileZone.setCreateTime(new Date());
            sysFileZoneService.save(sysFileZone);
        }

        return JsonResult.ok();
    }

    /**
     * 合并用户上传的分片数据
     * 通过“文件的md5值”和“创建人”得到分片的存放路径，然后将分片路径下的所有分片合并成一个文件
     * @param fileMd5    文件的md5值
     * @param createUser 创建人
     * @return
     */
    @Override
    public JsonResult zoneMerge(String fileMd5, String createUser) {
        SysFile sysFile = this.findByFileMd5AndCreateUser(fileMd5, createUser);
        if(ObjectUtil.isEmpty(sysFile)) {
            log.info("错误，未获取到文件信息");
            return JsonResult.error("错误，未获取到文件信息");
        }
        // 如果分片已合并
        if(sysFile.getIsZone() == 1 && sysFile.getIsMerge() == 1) {
            log.info("错误，分片已合并");
            return JsonResult.ok();
        }

        // 查询该文件的分片数据
        List<SysFileZone> sysFileZones = sysFileZoneService.findByFileMd5(fileMd5);
        if(ObjectUtil.isNotEmpty(sysFileZones)) {
            TimeInterval timer = DateUtil.timer();
            String folderId = sysFolderService.createFolder(sysFileZones.get(0).getRelativePath(), createUser);
            log.info("============== 处理/创建路径耗时：{}s", timer.intervalRestart()/1000.0);
            // 获取文件相对路径。 即在哪个文件夹下创建的
            SysFolder sysFolder = sysFolderService.getById(folderId);
            String folderPath = sysFolder.getFolderPath();
            if(folderPath.startsWith("/")) {
                folderPath = folderPath.substring(1);
            }
            if(folderPath.endsWith("/")) {
                folderPath = folderPath.substring(0, folderPath.length() - 1);
            }


            String localPath = FileHandleUtil.joinPath(new String[]{
                    File.listRoots()[fileUploadParam.getUploadDisIndex()].getPath(),
                    fileUploadParam.getUploadDir(),
                    sysFile.getCreateUser(),
                    folderPath
            });

            // 文件后缀
            String fileSuffix = sysFile.getFileSuffix();
            // 服务器上的文件名  md5+后缀
            String serverFileName = sysFile.getFileMd5() + fileSuffix;
            // 服务器上文件存储路径
            String serverLocalPath = FileHandleUtil.joinPath(new String[]{ localPath, serverFileName });
            // 分片存放路径
            String zonePath = sysFileZones.get(0).getZonePath();
            // 存放分片id
            List<String> ids = new ArrayList<>();

            // 将多个分片文件拷贝到一个完整的文件中去
            File parentFileDir = new File(zonePath);
            if (parentFileDir.isDirectory()) {
                FileHandleUtil.createDirIfNotExists(localPath);
                File destTempFile = new File(localPath, serverFileName);
                // 创建文件及其父目录，如果这个文件存在，直接返回这个文件
                destTempFile = FileUtil.touch(destTempFile);
                try {
                    // 创建一个文件输出流
                    BufferedOutputStream bos = FileUtil.getOutputStream(destTempFile);

                    for (SysFileZone fileZone : sysFileZones) {
                        File partFile = new File(parentFileDir, fileZone.getZoneName());
                        // 创建一个文件输入流
                        BufferedInputStream bis = FileUtil.getInputStream(partFile);
                        int size = 0;
                        byte[] buffer = new byte[10240];
                        while ((size = bis.read(buffer)) != -1) {
                            bos.write(buffer, 0, size);
                        }
                        bis.close();
                        // 将id记录下来，一会要删
                        ids.add(fileZone.getId());
                    }
                    // 刷新此缓冲的输出流，保证数据全部都能写出
                    bos.flush();
                    bos.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // 删除分片信息,(也可以在此处删除分片信息)
            sysFileZoneService.removeByIds(ids);
            // 删除临时目录中的分片文件
            // C:\/fileSystem/temp/admin/fileMd5
            String zoneDir = FileHandleUtil.joinPath(new String[]{
                    File.listRoots()[fileUploadParam.getUploadDisIndex()].getPath(),
                    fileUploadParam.getUploadDir(),
                    GlobalConstants.TEMP_DIR_NAME,
                    createUser,
                    fileMd5
            });
            if(!FileUtil.del(zoneDir)){
                return JsonResult.error("分片文件删除失败");
            }
            // 修改文件上传记录
            sysFile.setServerLocalName(serverFileName);
            sysFile.setServerLocalPath(serverLocalPath);
            sysFile.setIsMerge(1);
            sysFile.setFolderId(folderId);
            sysFile.setUpdateTime(new Date());
            this.updateById(sysFile);
            return JsonResult.ok();
        }
        return JsonResult.error("合并错误");
    }

    /**
     * 通过“文件md5值”和“创建人”查询文件信息
     * @param fileMd5    文件的Md5值
     * @param createUser 创建人
     * @return
     */
    @Override
    public SysFile findByFileMd5AndCreateUser(String fileMd5, String createUser) {
        return this.getOne(
                new QueryWrapper<SysFile>()
                    .eq("file_md5", fileMd5)
                    .eq("create_user", createUser)
        );
    }


    /**
     * 删除文件
     * @param id
     * @return
     */
    @Override
    public boolean delFile(String id) {
        // 获取要删除文件的数据
        SysFile file = this.getById(id);
        if(ObjectUtil.isEmpty(file)){
            log.error("未获取到要删除文件的数据");
            return false;
        }
        // 获取文件在服务器储的存路径
        String serverFilePath = file.getServerLocalPath();

        // 删除文件
        if(!this.removeById(id)){
            throw new RuntimeException("删除文件数据失败");
        }

        return FileUtil.del(serverFilePath);
    }


}
