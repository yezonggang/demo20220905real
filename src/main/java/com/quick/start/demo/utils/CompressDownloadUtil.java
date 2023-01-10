package com.quick.start.demo.utils;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Component
public class CompressDownloadUtil {


    /**
     * 设置下载响应头
     *
     * @param response
     * @return
     * @author hongwei.lian
     * @date 2018年9月7日 下午3:01:59
     */
    public HttpServletResponse setDownloadResponse(HttpServletResponse response, String downloadName) {
        response.reset();
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;fileName*=UTF-8''"+ downloadName);
        return response;
    }

    /**
     * 字符串转换为整型数组
     *
     * @param param
     * @return
     * @author hongwei.lian
     * @date 2018年9月6日 下午6:38:39
     */
    public  Integer[] toIntegerArray(String param) {
        return Arrays.stream(param.split(","))
                .map(Integer::valueOf)
                .toArray(Integer[]::new);
    }

    /**
     * 将多个文件压缩到指定输出流中
     *
     * @param files 需要压缩的文件列表
     * @param outputStream  压缩到指定的输出流
     * @author hongwei.lian
     * @date 2018年9月7日 下午3:11:59
     */
    public  void compressZip(List<File> files, OutputStream outputStream) {
        ZipOutputStream zipOutStream = null;
        try {
            //-- 包装成ZIP格式输出流
            zipOutStream = new ZipOutputStream(new BufferedOutputStream(outputStream));
            // -- 设置压缩方法
            zipOutStream.setMethod(ZipOutputStream.DEFLATED);
            //-- 将多文件循环写入压缩包
            for (int i = 0; i < files.size(); i++) {
                File file = files.get(i);
                FileInputStream filenputStream = new FileInputStream(file);
                byte[] data = new byte[(int) file.length()];
                filenputStream.read(data);
                //-- 添加ZipEntry，并ZipEntry中写入文件流，这里，加上i是防止要下载的文件有重名的导致下载失败
                zipOutStream.putNextEntry(new ZipEntry(i + file.getName()));
                zipOutStream.write(data);
                filenputStream.close();
                zipOutStream.closeEntry();
            }
        } catch (IOException e) {
            log.error("输入异常"+e.getMessage());
        }  finally {
            try {
                if (Objects.nonNull(zipOutStream)) {
                    zipOutStream.flush();
                    zipOutStream.close();
                }
                if (Objects.nonNull(outputStream)) {
                    outputStream.close();
                }
            } catch (IOException e) {
                log.error("输入异常"+e.getMessage());
            }
        }
    }

    /**
     * 下载文件
     *
     * @param zipFilepath 需要下载文件的路径
     */
    public  HttpServletResponse downloadFile(String zipFilepath,HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径。
            File file = new File(zipFilepath);
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(zipFilepath));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename="  + URLEncoder.encode(filename, "UTF-8"));
            response.addHeader("Content-Length", "" + file.length());
            response.addHeader("Access-Control-Allow-Origin","*");
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
            return null;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return response;
    }

    /**
     * 删除指定路径的文件
     *
     * @param filepath
     * @author hongwei.lian
     * @date 2018年9月7日 下午3:44:53
     */
    public  void deleteFile(String filepath) {
        File file = new File(filepath);
        deleteFile(file);
    }

    /**
     * 删除指定文件
     *
     * @param file
     * @author hongwei.lian
     * @date 2018年9月7日 下午3:45:58
     */
    public  void deleteFile(File file) {
        //-- 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }


}