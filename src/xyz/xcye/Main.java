package xyz.xcye;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ：qsyyke
 * @description：TODO
 * @date ：2022/3/17 22:19
 */


public class Main {
    private static int count;
    private static String mdDirPath = "/Users/aurora/Documents/笔记/study-note/docs";
    private static String regexPath;
    private static String replacePath = "https://picture.xcye.xyz";

    public static void getParam() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入笔记路径: ");
        mdDirPath = scanner.next();

        System.out.print("\n请输入正则匹配的路径: ");
        regexPath = scanner.next();

        System.out.print("\n请输入替换的路径: ");
        replacePath = scanner.next();
    }

    public static void main(String[] args) throws Exception {
        //getParam();
        parseMdDir(mdDirPath);
    }

    public static void parseMdDir(String path) throws Exception {
        File file = new File(path);
        //列出所有的文件
        File[] files = file.listFiles();
        for (File fileSingle : files) {
            //判断是否是文件夹
            String singleAbsolutePath = fileSingle.getAbsolutePath();
            if (fileSingle.isDirectory()) {
                getFileLists(singleAbsolutePath);
            }else {
                //是文件，解析文件
                parseContent(singleAbsolutePath);
            }

        }
    }

    public static void getFileLists(String path) throws Exception {
        File file = new File(path);
        File[] files = file.listFiles();
        for (File fileSingle : files) {
            boolean directory = fileSingle.isDirectory();
            String fileSingleAbsolutePath = fileSingle.getAbsolutePath();
            if (directory) {
                //是文件夹
                getFileLists(fileSingleAbsolutePath);
            }else {
                //是文件，解析文件
                parseContent(fileSingleAbsolutePath);
            }
        }
    }

    public static void parseContent(String mdFilePath) throws Exception {
        count++;

        if (!mdFilePath.contains(".md")) {
            return;
        }

        FileReader fr = new FileReader(mdFilePath);
        BufferedReader br = new BufferedReader(fr);
        String line;
        String content = "";
        while((line = br.readLine()) != null){
            content = content + line + "\n";
        }

        String regex = "!\\[[0-9a-zA-Z-~!@#$%^&*()._+]*]\\(((https|http)://ooszy\\.cco\\.vin/img/blog-note)";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(content);

        String aurora = matcher.replaceAll("![](" + replacePath);

        //删除原来的文件
        boolean delete = new File(mdFilePath).delete();
        if (delete) {
            byte[] bytes = aurora.getBytes(StandardCharsets.UTF_8);

            FileOutputStream outputStream = new FileOutputStream(mdFilePath);
            outputStream.write(bytes,0,bytes.length);
            outputStream.close();
            System.out.println(mdFilePath + "执行完成");
        }else {
            System.err.println(mdFilePath + "删除失败");
        }
    }
}
