package com.efm.system.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.efm.core.param.FileUploadParam;
import com.efm.core.utils.FileHandleUtil;
import com.efm.core.utils.JsonResult;
import com.efm.core.utils.PageParam;
import com.efm.core.utils.PageResult;
import com.efm.system.entity.SysFile;
import com.efm.system.entity.SysFileZone;
import com.efm.system.service.SysFileService;
import com.efm.system.service.SysFileZoneService;
import com.efm.system.vo.FolderFileVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 文件管理
 * @author gcc
 * @date 2020/8/24 13:02
 */
@Controller
@RequestMapping("/file")
public class FileController {
    Logger log = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private SysFileService fileService;
    @Autowired
    private SysFileZoneService sysFileZoneService;
    @Autowired
    private FileUploadParam fileUploadParam;


    /**
     * 列表数据
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/list")
    public PageResult<FolderFileVo> list(HttpServletRequest request) {
        PageParam pageParam = new PageParam(request);
        pageParam.put("createUser", fileUploadParam.getUserNameDir());
        return fileService.listPage(pageParam.setDefaultOrder(null, new String[]{"create_time"}));
    }


    /**
     * 【第一步】
     * 在文件发送之前request，此时还没有分片（如果配置了分片的话），可以用来做文件整体md5验证
     * @param fileMd5 文件md5值，用于秒传判断
     * @return
     */
    @ResponseBody
    @RequestMapping("/upload/beforeSendFile")
    public JsonResult beforeSendFile(String fileMd5) {
        log.info("【第一步】 获取到的fileMd5值为：{}", fileMd5);
        SysFile sysFile = fileService.getOne(
                new QueryWrapper<SysFile>()
                    .eq("file_md5", fileMd5)
                    .eq("create_user", fileUploadParam.getUploadDir())
        );

        if(ObjectUtil.isEmpty(sysFile)) {
            return JsonResult.error("请选择文件上传");
        }
        return JsonResult.ok("秒传");
    }

    /**
     * 【第二步】
     * 在分片发送之前request，可以用来做分片验证，判断此分片是否已经上传成功了
     * @param fileMd5   文件的md5值
     * @param zoneMd5   分片的md5值
     * @return
     */
    @ResponseBody
    @RequestMapping("/upload/beforeSend")
    public JsonResult beforeSend(String fileMd5, String zoneMd5) {
        log.info("【第二步】 校验分片 获取到的fileMd5值为：{}  获取到的zoneMd5的值为：{}", fileMd5, zoneMd5);
        // 查询分片信息
        SysFileZone sysFileZone = sysFileZoneService.findByZoneMd5AndFileMd5(zoneMd5, fileMd5);
        if(ObjectUtil.isEmpty(sysFileZone)) {
            return JsonResult.error("分块验证不通过(服务器上不存在该分块)，进行上传分块操作");
        }
        return JsonResult.ok();
    }

    /**
     * 【第三步】
     * 分片验证不通过，上传分片
     * @param file
     * @return
     */
    @ResponseBody
    @RequestMapping("/upload")
    public JsonResult zoneUpload(MultipartFile file, SysFileZone sysFileZone) {
        log.info("【第三步】 上传分片");
        if(ObjectUtil.isEmpty(file)){
            return JsonResult.error("参数错误");
        }
        sysFileZone.setCreateUser(fileUploadParam.getUserNameDir());
        return fileService.zoneUpload(file, sysFileZone);
    }


    /**
     * 【第四步】
     * 在所有分片都上传完毕后，且没有错误后request，用来做分片验证，此时如果promise被reject，当前文件上传会触发错误
     * @param fileMd5   文件的md5值，用于合并分片
     * @return
     */
    @ResponseBody
    @RequestMapping("/upload/afterSendFile/{fileMd5}")
    public JsonResult afterSendFile(@PathVariable("fileMd5")String fileMd5) {
        log.info("【第四步】 合并分片  获取到的fileMd5值为：{} ", fileMd5);
        return fileService.zoneMerge(fileMd5, fileUploadParam.getUserNameDir());
    }

    /**
     * 修改文件名
     * @param sysFile
     * @return
     */
    @ResponseBody
    @RequestMapping("/editFile")
    public JsonResult editFile(SysFile sysFile) {
        if(StrUtil.isEmpty(sysFile.getId())) {
            return JsonResult.error("参数有误");
        }
        sysFile.setUpdateTime(new Date());
        if(fileService.updateById(sysFile)) {
            return JsonResult.ok();
        }

        return JsonResult.error();
    }


    /**
     * 查看原文件
     * @param fileId
     * @param response
     */
    @GetMapping("/{fileId}")
    public void file(@PathVariable("fileId") String fileId, HttpServletResponse response) {
        SysFile file = fileService.getById(fileId);
        FileHandleUtil.outputFile(file.getServerLocalPath(), response, true);
    }

    /**
     * 下载原文件
     * @param fileId
     * @param response
     */
    @GetMapping("/download/{fileId}")
    public void download(@PathVariable("fileId") String fileId, HttpServletResponse response) {
        SysFile file = fileService.getById(fileId);
        FileHandleUtil.outputFile(file.getServerLocalPath(), response, false);
    }

    /**
     * 删除原文件
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/delFile")
    public JsonResult delFile(@RequestParam String id) {
        if(fileService.delFile(id)){
            return JsonResult.ok();
        }
        return JsonResult.error();
    }



}
