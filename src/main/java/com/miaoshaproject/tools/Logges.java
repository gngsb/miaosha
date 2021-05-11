package com.miaoshaproject.tools;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class Logges {
    public static String dir = System.getProperty("user.dir")+"/logs/";

    public static void WriteErrorLog(String msg) throws IOException {

        File file = new File(dir+DateTime.now().toString("yyyy-MM-dd")+".txt");
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdir();
        }
        FileWriter fw = new FileWriter(file,true);
        fw.write("运行时错误出现时间：" + DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        fw.write("错误原因：" + msg);
        fw.write("\n");
        fw.close();

    }
}
