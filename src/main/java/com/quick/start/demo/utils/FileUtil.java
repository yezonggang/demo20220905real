package com.quick.start.demo.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.HashSet;
import java.util.Set;

public class FileUtil {

    //将文件转换成Byte数组
    public static byte[] getBytesByFile(String pathStr) {
        File file = new File(pathStr);
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            byte[] data = bos.toByteArray();
            bos.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //将Byte数组转换成文件
    public static void getFileByBytes(byte[] bytes, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + "\\" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static boolean copyDir(String src, String des)  {
        try {
            //初始化文件复制
            File file1=new File(src);
            if(!file1.exists()){
                return false;
            }else{
                //把文件里面内容放进数组
                File[] fs=file1.listFiles();
                //初始化文件粘贴
                File file2=new File(des);
                //判断是否有这个文件有不管没有创建
                if(!file2.exists()){
                    file2.mkdirs();
                }
                //遍历文件及文件夹
                for (File f : fs) {
                    if(f.isFile()){
                        //文件
                        fileCopy(f.getPath(),des+ File.separator+f.getName()); //调用文件拷贝的方法
                    }else if(f.isDirectory()){
                        //文件夹
                        copyDir(f.getPath(),des+File.separator+f.getName());//继续调用复制方法      递归的地方,自己调用自己的方法,就可以复制文件夹的文件夹了
                    }
                }
                return true;
            }
        }catch (Exception e){
            e.getStackTrace();
            return false;
        }
    }

    /**
     * 文件复制的具体方法
     */
    private static void fileCopy(String src, String des) throws Exception {
        //io流固定格式
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(des));
        int i = -1;//记录获取长度
        byte[] bt = new byte[2014];//缓冲区
        while ((i = bis.read(bt))!=-1) {
            bos.write(bt, 0, i);
        }
        bis.close();
        bos.close();
        //关闭流
    }

    public static String getFileName(String fName){
        String fileName = null;
        File tempFile =new File( fName.trim());
        if(tempFile.exists()){
            File[] array = tempFile.listFiles();
            for(int i = 0 ; i<array.length ; i++){
                String houzhui = array[i].toString().substring(array[i].toString().lastIndexOf(".") + 1);
                if(houzhui.equals("iw")||houzhui.equals(".zip")){
                    fileName = array[i].getName();
                }
            }
        }
        return fileName;
    }

    /**
     * 删除空目录
     * @param dir 将要删除的目录路径
     */
    private static void doDeleteEmptyDir(String dir) {
        boolean success = (new File(dir)).delete();
        if (success) {

        } else {

        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    public static Set<String> getDirName(String fName){
        Set<String> set = new HashSet<>();
        String fileName = null;
        File tempFile =new File( fName.trim());
        if(tempFile.exists()){
            File[] array = tempFile.listFiles();
            for(int i = 0 ; i<array.length ; i++){
                set.add(array[i].getName().toString());
            }
        }
        System.out.println(set);
        return set;
    }

    /*判断文件是否存在*/
    public static boolean isExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /*判断是否是文件夹*/
    public static boolean isDir(String path) {
        File file = new File(path);
        if(file.exists()){
            return file.isDirectory();
        }else{
            return false;
        }
    }

    /**
     * 文件或者目录重命名
     * @param oldFilePath 旧文件路径
     * @param newName 新的文件名,可以是单个文件名和绝对路径
     * @return
     */
    public static boolean renameTo(String oldFilePath, String newName) {
        try {
            File oldFile = new File(oldFilePath);
            //若文件存在
            if(oldFile.exists()){
                //判断是全路径还是文件名
                if (newName.indexOf("/") < 0 && newName.indexOf("\\") < 0){
                    //单文件名，判断是windows还是Linux系统
                    String absolutePath = oldFile.getAbsolutePath();
                    if(newName.indexOf("/") > 0){
                        //Linux系统
                        newName = absolutePath.substring(0, absolutePath.lastIndexOf("/") + 1)  + newName;
                    }else{
                        newName = absolutePath.substring(0, absolutePath.lastIndexOf("\\") + 1)  + newName;
                    }
                }
                File file = new File(newName);
                //判断重命名后的文件是否存在
                if(file.exists()){
                    System.out.println("该文件已存在,不能重命名");
                }else{
                    //不存在，重命名
                    return oldFile.renameTo(file);
                }
            }else {
                System.out.println("原该文件不存在,不能重命名");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /*文件拷贝操作*/
    public static void copy(String sourceFile, String targetFile) {
        File source = new File(sourceFile);
        File target = new File(targetFile);
        target.getParentFile().mkdirs();
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fis = new FileInputStream(source);
            fos = new FileOutputStream(target);
            in = fis.getChannel();//得到对应的文件通道
            out = fos.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null){
                    out.close();
                }
                if (in != null){
                    in.close();
                }
                if (fos != null){
                    fos.close();
                }
                if (fis != null){
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通过上一层目录和目录名得到最后的目录层次
     * @param previousDir 上一层目录
     * @param dirName 当前目录名
     * @return
     */
    public static String getSaveDir(String previousDir, String dirName) {
        if (StringUtils.isNotBlank(previousDir)){
            dirName = previousDir + "/" + dirName + "/";
        }else {
            dirName = dirName + "/";
        }
        return dirName;
    }

    /**
     * 如果目录不存在，就创建文件
     * @param dirPath
     * @return
     */
    public static String mkdirs(String dirPath) {
        try{
            File file = new File(dirPath);
            if(!file.exists()){
                file.mkdirs();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return dirPath;
    }

/*
    public static void main(String[] args) throws Exception {

        //一；创建根文件夹
        String path="E://OaTestFile/";
        String name="办公室";
        String endPath=path+name;

        if (isExists(endPath)){
            System.out.println("文件夹已存在");
        }else {
            mkdirs(endPath);
            System.out.println("ok");
        }



    }
*/


}
