package com.efm.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.efm.core.utils.JsonResult;
import com.efm.core.utils.PageParam;
import com.efm.core.utils.PageResult;
import com.efm.system.entity.SysFile;
import com.efm.system.entity.SysFileZone;
import com.efm.system.vo.FolderFileVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author gcc
 * @date 2020/8/24 15:52
 */
public interface SysFileService extends IService<SysFile> {

    /**
     * 关联分页查询
     * @param pageParam
     * @return
     */
    PageResult<FolderFileVo> listPage(PageParam pageParam);

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
    JsonResult zoneUpload(MultipartFile file, SysFileZone sysFileZone);

    /**
     * 合并用户上传的分片数据
     * 通过“文件的md5值”和“创建人”得到分片的存放路径，然后将分片路径下的所有分片合并成一个文件
     * @param fileMd5      文件的md5值
     * @param createUser   创建人
     * @return
     */
    JsonResult zoneMerge(String fileMd5, String createUser);

    /**
     * 通过“文件md5值”和“创建人”查询文件信息
     * @param fileMd5       文件的Md5值
     * @param createUser    创建人
     * @return
     */
    SysFile findByFileMd5AndCreateUser(String fileMd5, String createUser);


    /**
     * 删除文件
     * @param id
     * @return
     */
    boolean delFile(String id);
}
