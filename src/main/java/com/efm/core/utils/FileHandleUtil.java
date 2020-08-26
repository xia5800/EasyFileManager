package com.efm.core.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import org.apache.tika.Tika;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

/**
 * 文件处理工具类
 * @author gcc
 * @date 2020/8/24 17:57
 */
public class FileHandleUtil {

    /**
     * 获取文件后缀
     * @param fileName  文件名
     * @return
     */
    public static String getFileSuffix(String fileName){
        if(fileName==null || fileName.length()==0 || !fileName.contains(".")){
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 从相对路径中获取目录名
     * @param relativePath
     * @return
     */
    public static String getFolderName(String relativePath) {
        if(relativePath == null || relativePath.length() == 0) {
            return "";
        }
        if(!relativePath.contains("/")) {
            return relativePath;
        }
        return relativePath.substring(relativePath.lastIndexOf("/"));
    }


    /**
     * 拼接路径
     * @param pathArray:  new String[]{username, uploadPath, filename}
     * @return 拼接之后的路径 例如： /admin/upload/123.txt
     */
    public static String joinPath(String[] pathArray) {
        // 存入数组并换成list
        List<String> pathList = CollUtil.toList(pathArray);
        // 移除list中的空元素
        CollUtil.removeBlank(pathList);
        // 用"/"拼接list中的元素 得到形如：/admin/upload/123.txt 这样的路徑
        return CollUtil.join(pathList, "/");
    }

    /**
     * 创建文件夹路径
     */
    public static void createDirIfNotExists(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 上传单个文件
     *
     * @param inputStream 文件流
     * @param path        文件路径，如：image/
     * @param filename    文件名，如：test.jpg
     * @return 成功：上传后的文件访问路径，失败返回：null
     */
    public static String upload(InputStream inputStream, String path, String filename) {
        //创建文件夹
        createDirIfNotExists(path);
        //存文件
        File uploadFile = new File(path, filename);
        if(!uploadFile.exists()) {
            FileUtil.touch(uploadFile);
            //文件不存在
            FileUtil.writeFromStream(inputStream, uploadFile);
        }
        return uploadFile.getPath();
    }

    /**
     * 输出文件流
     * @param file          文件路径
     * @param fileName      文件名（要改名的话）
     * @param response
     * @param isPreview     是否预览文件 true预览，false下载
     */
    public static void outputFile(String file, HttpServletResponse response, boolean isPreview) {
        // 判断文件是否存在
        File inFile = new File(file);
        if (!inFile.exists()) {
            outNotFund(response);
            return;
        }
        if (isPreview) {
            // 获取文件类型
            String contentType = getFileType(inFile);
            if (contentType != null) {
                response.setContentType(contentType);
            } else {
                setDownloadHeader(inFile.getName(), response);
            }
        } else {
            setDownloadHeader(inFile.getName(), response);
        }
        // 输出文件流
        FileInputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(inFile);
            os = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int len;
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 获取文件类型
     */
    public static String getFileType(File file) {
        String contentType = null;
        try {
            contentType = new Tika().detect(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentType;
    }

    /**
     * 设置下载文件的header
     */
    public static void setDownloadHeader(String fileName, HttpServletResponse response) {
        response.setContentType("application/force-download");
        String newName = fileName;
        try {
            newName = URLEncoder.encode(newName, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-Disposition", "attachment;fileName="+newName);
    }


    /**
     * 输出文件不存在
     */
    private static void outNotFund(HttpServletResponse response) {
        PrintWriter writer;
        try {
            response.setContentType("text/html;charset=UTF-8");
            writer = response.getWriter();
            writer.write("<!doctype html><title>404 Not Found</title><h1 style=\"text-align: center\">404 Not Found</h1><hr/><p style=\"text-align: center\">该文件好像丢失了</p>");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
