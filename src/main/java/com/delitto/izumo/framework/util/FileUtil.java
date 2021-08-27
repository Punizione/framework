package com.delitto.izumo.framework.util;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.delitto.izumo.framework.base.PluginClassBean;
import lombok.extern.log4j.Log4j2;
import okhttp3.ResponseBody;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import retrofit2.Response;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Log4j2
@Component
public class FileUtil {
    /**
     *
     * @param body
     * @param filePath
     * @param fileName
     * @return
     */
    public static boolean write2File(ResponseBody body, String filePath, String fileName) {
        try {
            //判断文件夹是否存在
            File files = new File(filePath);//跟目录一个文件夹
            if (!files.exists()) {
                //不存在就创建出来
                files.mkdirs();
            }
            //创建一个文件
            File futureStudioIconFile = new File(filePath + File.separator + fileName);

            if(futureStudioIconFile.exists()) { //如果文件已存在
                return true;
            }
            //初始化输入流
            InputStream inputStream = null;
            //初始化输出流
            OutputStream outputStream = null;
            try {
                //设置每次读写的字节
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                //请求返回的字节流
                inputStream = body.byteStream();
                //创建输出流
                outputStream = new FileOutputStream(futureStudioIconFile);
                //进行读取操作
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    //进行写入操作
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                }

                //刷新
                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    //关闭输入流
                    inputStream.close();
                }
                if (outputStream != null) {
                    //关闭输出流
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }


    public static String copyFile2Mirai(String from, String fileName, SendType type, String to) {
        String retName = type.name() + fileName;
        try {
            FileUtils.copyFile(new File(from + File.separator + fileName), new File(to + File.separator + retName));
        } catch (IOException ioe) {
            return null;
        }
        return to + File.separator + retName;
    }

    /**
     * @TODO something wrong
     * @param fileName
     * @return
     */
//    public static String getMiraiImagePath() {
//        Environment enviroment = ApplicationContextUtil.get(Environment.class);
//        String miraiPath = enviroment.getProperty("bot-config.mirai-dir");
//        return miraiPath;
//    }


    public static JSONObject loadJson(String fileName, String filePath) {
        try {
            File pathRoot = new File(ResourceUtils.getURL("classpath:").getPath());
            if(!pathRoot.exists()) {
                pathRoot = new File("");
            }

            File f = new File(filePath);
            if(!f.exists()) {
                f.mkdirs();
            }
            InputStream inputStream = new FileInputStream(new File(filePath + File.separator + fileName));
            String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            JSONObject jsonObject = JSON.parseObject(content);
            return jsonObject;
        } catch (Exception e) {
            log.error("获取类配置文件失败:", e);
        }
        return null;
    }

    public static void writeJson(JSONObject jsonObject, String fileName, String filePath) {
        try{
            File pathRoot = new File(ResourceUtils.getURL("classpath:").getPath());
            if(!pathRoot.exists()) {
                pathRoot = new File("");
            }
            File f = new File(filePath);
            if(!f.exists()) {
                f.mkdirs();
            }
            OutputStream outputStream = new FileOutputStream(new File(filePath + fileName + ".json"));
            IOUtils.write(jsonObject.toJSONString(), outputStream, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("获取类配置文件失败:", e);
        }
    }


    public static String saveImage(Response<ResponseBody> response, SendType sendType, String tempDirPath, String saveDirPath) {
        return saveImage(response, sendType, tempDirPath, null, saveDirPath);
    }



    public static String saveImage(Response<ResponseBody> response, SendType sendType, String tempDirPath, String fileName, String saveDirPath) {
        String copyName = null;
        String randomName = "";
        if(StringUtils.isNotBlank(fileName)) {
            randomName = fileName + checkFileExtension(response);
        } else {
            randomName = UUID.randomUUID().toString().replaceAll("-", "") + checkFileExtension(response);
        }
        try {
            /*
            File pathRoot = new File(ResourceUtils.getURL("classpath:").getPath());
            if(!pathRoot.exists()) {
                pathRoot = new File("");
            }*/
            File f = new File(saveDirPath);
            if(!f.exists()) {
                f.mkdirs();
            }
            boolean toDisk = FileUtil.write2File(response.body(), saveDirPath, randomName);
            if (toDisk) {
                copyName = FileUtil.copyFile2Mirai(saveDirPath, randomName, sendType, tempDirPath);
                if (StringUtils.isNotBlank(copyName)) {
                    log.debug("文件保存成功");
                } else {
                    log.error("文件复制到临时目录失败");
                }
            } else {
                log.error("文件保存失败");
            }
        } catch (Exception fnfe) {
            log.trace(fnfe);
        }
        return copyName;
    }


    public static String checkFileExtension(Response<ResponseBody> response) {
        String cType = response.headers().get("content-type");
        String retType = Constants.extensionMap.getString(cType);
        if(StringUtils.isNotBlank(retType)) {
            return retType;
        } else {
            return "";
        }
    }

    public static String getFileNameNoExtensionByUrl(String url) {
        try {
            return url.substring(url.lastIndexOf('/') +1 ).split("\\.")[0];
        } catch (IndexOutOfBoundsException iobe) {
            log.error("获取文件名失败", iobe);
        }
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * @todo
     */
//    public static String getPathRoot(String upRoot, String downRoot) {
//        String retPathRoot = null;
//        try {
//            File pathRoot = new File(ResourceUtils.getURL("classpath:").getPath());
//            if (!pathRoot.exists()) {
//                pathRoot = new File("");
//            }
//            String filePath = pathRoot.getAbsolutePath().replace("%20", " ").replace('/', '\\')
//                    + "\\"
//                    + Constants.STATIC_DIR
//                    + File.separator
//                    + upRoot + File.separator + downRoot + File.separator;
//            return filePath;
//        } catch (FileNotFoundException fnfe) {
//            log.error(fnfe);
//            fnfe.printStackTrace();
//        }
//        return retPathRoot;
//    }


    public static List<PluginClassBean> loadJars(String pluginJarsPath) {
        List<PluginClassBean> beans = new ArrayList<>();
        Collection<File> listJars = FileUtils.listFiles(new File(pluginJarsPath), FileFilterUtils.suffixFileFilter("jar"), null);
        for (File jarFile: listJars) {
            PluginClassBean bean = new PluginClassBean();
            bean.setPath(jarFile.getPath());
            String[] splitName = jarFile.getName().split("-");
            bean.setNeedPermission(Integer.parseInt(splitName[0]));
            bean.setResponsePrefix(splitName[1]);
            bean.setMainClass(splitName[2]);
            beans.add(bean);
        }
        return beans;
    }

}
