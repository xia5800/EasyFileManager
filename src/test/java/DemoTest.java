import cn.hutool.core.io.FileUtil;
import com.efm.EasyFileApplication;
import com.efm.core.utils.FileHandleUtil;
import com.efm.system.entity.SysFileZone;
import com.efm.system.service.SysFileZoneService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gcc
 * @date 2020/8/24 17:58
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EasyFileApplication.class)
public class DemoTest {

    @Autowired
    private SysFileZoneService sysFileZoneService;

    @Test
    public void t1() {
        String fileMd5 = "5995b6d45730fc815ec2f08008421d6a";
        // 服务器上的文件名  md5+后缀
        String serverFileName = fileMd5 + ".mp4";
        List<SysFileZone> sysFileZones = sysFileZoneService.findByFileMd5(fileMd5);
        // C:\/fileSystem/temp/admin/5995b6d45730fc815ec2f08008421d6a
        String zonePath = sysFileZones.get(0).getZonePath();
        String localPath = FileHandleUtil.joinPath(new String[]{
                File.listRoots()[0].getPath(),
                "fileSystem",
                "admin",
                ""
        });

        try {

            File parentFileDir = new File(zonePath);
            if(parentFileDir.isDirectory()) {
                File destTempFile = new File(localPath, serverFileName);
                // 创建文件及其父目录，如果这个文件存在，直接返回这个文件
                destTempFile = FileUtil.touch(destTempFile);

                List<String> ids = new ArrayList<>();
                BufferedOutputStream bos = FileUtil.getOutputStream(destTempFile);
                for (SysFileZone fileZone : sysFileZones) {
                    // C:\/fileSystem/temp/admin/5995b6d45730fc815ec2f08008421d6a/xxxx.temp
                    File partFile = new File(parentFileDir, fileZone.getZoneName());
                    BufferedInputStream bis = FileUtil.getInputStream(partFile);
                    int size = 0;
                    byte[] buffer=new byte[10240];
                    while((size = bis.read(buffer)) != -1){
                        bos.write(buffer,0, size);
                    }
                    bis.close();
                    ids.add(fileZone.getId());
                }
                bos.flush();
                bos.close();
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void t2() {
        System.out.println(handleStr(""));
        System.out.println(handleStr("/新建文件夹"));
        System.out.println(handleStr("新建文件夹/新建文件夹1/新建文件夹2/"));
        System.out.println(handleStr("/新建文件夹/新建文件夹1/新建文件夹2/新建文件夹3/"));
    }


    public String handleStr(String folderPath) {
        if(folderPath.startsWith("/")) {
            folderPath = folderPath.substring(1);
        }
        if(folderPath.endsWith("/")) {
            folderPath = folderPath.substring(0, folderPath.length() - 1);
        }
        return folderPath;
    }
}
