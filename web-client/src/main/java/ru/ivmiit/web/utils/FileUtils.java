package ru.ivmiit.web.utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class FileUtils {

    public static void writeToFile(String filePath, String text, boolean create) throws IOException {

        File commandFile = new File(filePath);
        if(create){
            commandFile.setWritable(true, false);
            commandFile.setReadable(true, false);
            commandFile.setExecutable(true, false);
            boolean resultJavaFile = commandFile.createNewFile();
            commandFile.setWritable(true, false);
            commandFile.setReadable(true, false);
            commandFile.setExecutable(true, false);
        }

        PrintWriter out = new PrintWriter(commandFile.getAbsolutePath(), "UTF-8");
        out.print(text);
        out.close();
    }
}
