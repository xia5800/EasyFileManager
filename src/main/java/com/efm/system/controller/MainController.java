package com.efm.system.controller;

import com.efm.system.entity.SysFolder;
import com.efm.system.service.SysFolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author gcc
 * @date 2020/8/24 13:02
 */
@Controller
public class MainController {
    @Autowired
    private SysFolderService folderService;

    /**
     * 文件管理首页
     * @return
     */
    @RequestMapping({"/", "/index"})
    public String index(Model model) {
        SysFolder folder = folderService.getById("1");
        model.addAttribute("folderId", folder.getFolderId());
        model.addAttribute("folderPath", folder.getFolderPath());
        return "index";
    }


}
