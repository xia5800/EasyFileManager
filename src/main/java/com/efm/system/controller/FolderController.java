package com.efm.system.controller;

import cn.hutool.core.util.StrUtil;
import com.efm.core.utils.JsonResult;
import com.efm.system.entity.SysFolder;
import com.efm.system.service.SysFolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author gcc
 * @date 2020/8/26 0:01
 */
@Controller
@RequestMapping("/folder")
public class FolderController {

    @Autowired
    private SysFolderService sysFolderService;


    /**
     * 新建文件夹
     * @param sysFolder
     * @return
     */
    @ResponseBody
    @RequestMapping("/createFolder")
    public JsonResult createFolder(SysFolder sysFolder) {
        if(sysFolderService.saveFolder(sysFolder)){
            return JsonResult.ok();
        }
        return JsonResult.error();
    }

    /**
     * 编辑文件夹
     * @param sysFolder
     * @return
     */
    @ResponseBody
    @RequestMapping("/editFolder")
    public JsonResult editFolder(SysFolder sysFolder) {
        if(StrUtil.isEmpty(sysFolder.getFolderId())) {
            return JsonResult.error("参数有误");
        }

        if(sysFolderService.editFolder(sysFolder)){
            return JsonResult.ok();
        }
        return JsonResult.error();
    }

    /**
     * 删除文件夹
     * @param folderId
     * @return
     */
    @ResponseBody
    @RequestMapping("/delFolder")
    public JsonResult delFolder(@RequestParam String folderId) {
        if(sysFolderService.delFolder(folderId)){
            return JsonResult.ok();
        }
        return JsonResult.error();
    }

}
