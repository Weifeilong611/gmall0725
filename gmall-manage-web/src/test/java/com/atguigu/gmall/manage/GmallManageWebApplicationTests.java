package com.atguigu.gmall.manage;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class GmallManageWebApplicationTests {

    @Test
    public void contextLoads() throws IOException, MyException {
        String path = GmallManageWebApplicationTests.class.getClassLoader().getResource("tracker.conf").getPath();

        ClientGlobal.init(path);

        TrackerClient trackerClient = new TrackerClient();
        TrackerServer connection = trackerClient.getConnection();

        StorageClient storageClient = new StorageClient(connection,null);
        String[] pngs = storageClient.upload_file("d:/QQ截图20180624223004.png", "png", null);

        String url = "http://192.168.206.135";
        for (int i = 0; i < pngs.length; i++) {
            url = url +"/" +pngs[i];
        }
        System.out.println(url);
    }

}
