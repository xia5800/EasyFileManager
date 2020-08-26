# EasyFileManager

### 一、项目介绍
整合layui、百度Web Uploader文件上传插件，实现的文件管理系统

#### 1.1 项目配置
文件上传配置
```yaml
## 文件上传配置
fileupload:
  config:
    # 上传到第几个盘符下
    upload-dis-index: 0
    # 存储文件的目录名称
    upload-dir: 'fileSystem'
    # 用户文件夹名
    user-name-dir: 'admin'
```
配置说明，在Windows系统下，会将用户上传的文件存放到 ```C:\/fileSystem/admin/``` 目录下


### 二、技术选型

#### 2.1 后端技术

| 技术                       | 名称                                                         |
| -------------------------- | ------------------------------------------------------------ |
| Spring boot                | 核心框架                                                     |
| MyBatis                    | ORM框架                                                      |
| MyBatis-plus               | [MyBatis扩展](https://mp.baomidou.com/)                      |
| Beetl                      | [超高性能的java模板引擎](http://ibeetl.com/guide/#/beetl/)   |
| hutool                     | [Java工具类大全](https://hutool.cn/docs/#/)                  |

#### 2.2 前端技术

| 技术                    | 名称                                                         |
| ----------------------- | ------------------------------------------------------------ |
| easyweb                 | [基于Layui的一套通用型后台管理模板](https://easyweb.vip/)    |
| font-awesome            | 字体图标                                                     |
| layUpload               | [带进度条 md5 验证组件](https://fly.layui.com/extend/layUploader/)  |
| WebUploader             | [百度Web Uploader文件上传插件](http://fex.baidu.com/webuploader/) |


#### 三、项目截图

- 首页  
  ![首页](https://image.bestgcc.cn/github/EasyFileManager/图1.png)
- 创建文件夹  
  ![创建文件夹](https://image.bestgcc.cn/github/EasyFileManager/图2.png)
- 文件操作  
  ![文件操作](https://image.bestgcc.cn/github/EasyFileManager/图3.png)
- 上传文件  
  ![上传文件](https://image.bestgcc.cn/github/EasyFileManager/图4.png)

