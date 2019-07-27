//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cc.moq.gc.utils;

import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class IOUtils extends FileUtils {
    protected static MessageDigest messagedigest = null;
    protected static char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static Logger log;

    public IOUtils() {
    }

    public static boolean copyFile(String srcFileName, String descFileName) {
        return copyFileCover(srcFileName, descFileName, false);
    }

    public static String getFileSuffix(String fileName) {
        return fileName != null && fileName.indexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase() : null;
    }

    public static String getFileNameNoSuffix(String fileName) {
        return fileName != null && fileName.indexOf(".") != -1 ? fileName.substring(0, fileName.lastIndexOf(".")) : fileName;
    }

    public static boolean copyFileCover(String srcFileName, String descFileName, boolean coverlay) {
        File srcFile = new File(srcFileName);
        if (!srcFile.exists()) {
            log.debug("复制文件失败，源文件 " + srcFileName + " 不存在!");
            return false;
        } else if (!srcFile.isFile()) {
            log.debug("复制文件失败，" + srcFileName + " 不是一个文件!");
            return false;
        } else {
            File descFile = new File(descFileName);
            if (descFile.exists()) {
                if (!coverlay) {
                    log.debug("复制文件失败，目标文件 " + descFileName + " 已存在!");
                    return false;
                }

                log.debug("目标文件已存在，准备删除!");
                if (!delFile(descFileName)) {
                    log.debug("删除目标文件 " + descFileName + " 失败!");
                    return false;
                }
            } else if (!descFile.getParentFile().exists()) {
                log.debug("目标文件所在的目录不存在，创建目录!");
                if (!descFile.getParentFile().mkdirs()) {
                    log.debug("创建目标文件所在的目录失败!");
                    return false;
                }
            }

            //int readByte = false;
            int readByte = 0;
            InputStream ins = null;
            FileOutputStream outs = null;

            boolean var9;
            try {
                ins = new FileInputStream(srcFile);
                outs = new FileOutputStream(descFile);
                byte[] buf = new byte[1024];

                //int readByte;
                while((readByte = ins.read(buf)) != -1) {
                    outs.write(buf, 0, readByte);
                }

                log.debug("复制单个文件 " + srcFileName + " 到" + descFileName + "成功!");
                var9 = true;
                return var9;
            } catch (Exception var23) {
                log.debug("复制文件失败：" + var23.getMessage());
                var9 = false;
            } finally {
                if (outs != null) {
                    try {
                        outs.close();
                    } catch (IOException var22) {
                        var22.printStackTrace();
                    }
                }

                if (ins != null) {
                    try {
                        ins.close();
                    } catch (IOException var21) {
                        var21.printStackTrace();
                    }
                }

            }

            return var9;
        }
    }

    public static boolean copyDirectory(String srcDirName, String descDirName) {
        return copyDirectoryCover(srcDirName, descDirName, false);
    }

    public static boolean copyDirectoryCover(String srcDirName, String descDirName, boolean coverlay) {
        File srcDir = new File(srcDirName);
        if (!srcDir.exists()) {
            log.debug("复制目录失败，源目录 " + srcDirName + " 不存在!");
            return false;
        } else if (!srcDir.isDirectory()) {
            log.debug("复制目录失败，" + srcDirName + " 不是一个目录!");
            return false;
        } else {
            String descDirNames = descDirName;
            if (!descDirName.endsWith(File.separator)) {
                descDirNames = descDirName + File.separator;
            }

            File descDir = new File(descDirNames);
            if (descDir.exists()) {
                if (!coverlay) {
                    log.debug("目标目录复制失败，目标目录 " + descDirNames + " 已存在!");
                    return false;
                }

                log.debug("目标目录已存在，准备删除!");
                if (!delFile(descDirNames)) {
                    log.debug("删除目录 " + descDirNames + " 失败!");
                    return false;
                }
            } else {
                log.debug("目标目录不存在，准备创建!");
                if (!descDir.mkdirs()) {
                    log.debug("创建目标目录失败!");
                    return false;
                }
            }

            boolean flag = true;
            File[] files = srcDir.listFiles();

            for(int i = 0; i < files.length; ++i) {
                if (files[i].isFile()) {
                    flag = copyFile(files[i].getAbsolutePath(), descDirName + files[i].getName());
                    if (!flag) {
                        break;
                    }
                }

                if (files[i].isDirectory()) {
                    flag = copyDirectory(files[i].getAbsolutePath(), descDirName + files[i].getName());
                    if (!flag) {
                        break;
                    }
                }
            }

            if (!flag) {
                log.debug("复制目录 " + srcDirName + " 到 " + descDirName + " 失败!");
                return false;
            } else {
                log.debug("复制目录 " + srcDirName + " 到 " + descDirName + " 成功!");
                return true;
            }
        }
    }

    public static boolean delFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            log.debug(fileName + " 文件不存在!");
            return true;
        } else {
            return file.isFile() ? deleteFile(fileName) : deleteDirectory(fileName);
        }
    }

    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                log.debug("删除单个文件 " + fileName + " 成功!");
                return true;
            } else {
                log.debug("删除单个文件 " + fileName + " 失败!");
                return false;
            }
        } else {
            log.debug(fileName + " 文件不存在!");
            return true;
        }
    }

    public static boolean deleteDirectory(String dirName) {
        String dirNames = dirName;
        if (!dirName.endsWith(File.separator)) {
            dirNames = dirName + File.separator;
        }

        File dirFile = new File(dirNames);
        if (dirFile.exists() && dirFile.isDirectory()) {
            boolean flag = true;
            File[] files = dirFile.listFiles();

            for(int i = 0; i < files.length; ++i) {
                if (files[i].isFile()) {
                    flag = deleteFile(files[i].getAbsolutePath());
                    if (!flag) {
                        break;
                    }
                } else if (files[i].isDirectory()) {
                    flag = deleteDirectory(files[i].getAbsolutePath());
                    if (!flag) {
                        break;
                    }
                }
            }

            if (!flag) {
                log.debug("删除目录失败!");
                return false;
            } else if (dirFile.delete()) {
                log.debug("删除目录 " + dirName + " 成功!");
                return true;
            } else {
                log.debug("删除目录 " + dirName + " 失败!");
                return false;
            }
        } else {
            log.debug(dirNames + " 目录不存在!");
            return true;
        }
    }

    public static boolean createFile(String descFileName) {
        File file = new File(descFileName);
        if (file.exists()) {
            log.debug("文件 " + descFileName + " 已存在!");
            return false;
        } else if (descFileName.endsWith(File.separator)) {
            log.debug(descFileName + " 为目录，不能创建目录!");
            return false;
        } else if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            log.debug("创建文件所在的目录失败!");
            return false;
        } else {
            try {
                if (file.createNewFile()) {
                    log.debug(descFileName + " 文件创建成功!");
                    return true;
                } else {
                    log.debug(descFileName + " 文件创建失败!");
                    return false;
                }
            } catch (Exception var3) {
                var3.printStackTrace();
                log.debug(descFileName + " 文件创建失败!");
                return false;
            }
        }
    }

    public static boolean createDirectory(String descDirName) {
        String descDirNames = descDirName;
        if (!descDirName.endsWith(File.separator)) {
            descDirNames = descDirName + File.separator;
        }

        File descDir = new File(descDirNames);
        if (descDir.exists()) {
            log.debug("目录 " + descDirNames + " 已存在!");
            return false;
        } else if (descDir.mkdirs()) {
            log.debug("目录 " + descDirNames + " 创建成功!");
            return true;
        } else {
            log.debug("目录 " + descDirNames + " 创建失败!");
            return false;
        }
    }

    public static void zipDownLoad(Map<File, String> downQuene, HttpServletResponse response) throws IOException {
        ServletOutputStream out = response.getOutputStream();
        ZipOutputStream zipout = new ZipOutputStream(out);
        ZipEntry entry = null;
        zipout.setLevel(1);
        if (downQuene != null && downQuene.size() > 0) {
            Iterator var5 = downQuene.entrySet().iterator();

            while(var5.hasNext()) {
                Entry<File, String> fileInfo = (Entry)var5.next();
                File file = (File)fileInfo.getKey();

                try {
                    String filename = new String(((String)fileInfo.getValue()).getBytes(), "GBK");
                    entry = new ZipEntry(filename);
                    entry.setSize(file.length());
                    zipout.putNextEntry(entry);
                } catch (IOException var11) {
                }

                BufferedInputStream fr = new BufferedInputStream(new FileInputStream((File)fileInfo.getKey()));
                byte[] buffer = new byte[1024];

                int len;
                while((len = fr.read(buffer)) != -1) {
                    zipout.write(buffer, 0, len);
                }

                fr.close();
            }
        }

        zipout.finish();
        zipout.flush();
    }

    public static void zipFiles(String srcDirName, String fileName, String descFileName) {
        if (srcDirName == null) {
            log.debug("文件压缩失败，目录 " + srcDirName + " 不存在!");
        } else {
            File fileDir = new File(srcDirName);
            if (fileDir.exists() && fileDir.isDirectory()) {
                String dirPath = fileDir.getAbsolutePath();
                File descFile = new File(descFileName);

                try {
                    ZipOutputStream zouts = new ZipOutputStream(new FileOutputStream(descFile));
                    if (!"*".equals(fileName) && !"".equals(fileName)) {
                        File file = new File(fileDir, fileName);
                        if (file.isFile()) {
                            zipFilesToZipFile(dirPath, file, zouts);
                        } else {
                            zipDirectoryToZipFile(dirPath, file, zouts);
                        }
                    } else {
                        zipDirectoryToZipFile(dirPath, fileDir, zouts);
                    }

                    zouts.close();
                    log.debug(descFileName + " 文件压缩成功!");
                } catch (Exception var8) {
                    log.debug("文件压缩失败：" + var8.getMessage());
                    var8.printStackTrace();
                }

            } else {
                log.debug("文件压缩失败，目录 " + srcDirName + " 不存在!");
            }
        }
    }

    public static boolean unZipFiles(String zipFileName, String descFileName) {
        String descFileNames = descFileName;
        if (!descFileName.endsWith(File.separator)) {
            descFileNames = descFileName + File.separator;
        }

        try {
            ZipFile zipFile = new ZipFile(zipFileName);
            ZipEntry entry = null;
            String entryName = null;
            String descFileDir = null;
            byte[] buf = new byte[4096];
            //int readByte = false;
            int readByte = 0;
            Enumeration enums = zipFile.entries();

            while(true) {
                while(enums.hasMoreElements()) {
                    entry = (ZipEntry)enums.nextElement();
                    entryName = entry.getName();
                    descFileDir = descFileNames + entryName;
                    if (entry.isDirectory()) {
                        (new File(descFileDir)).mkdirs();
                    } else {
                        (new File(descFileDir)).getParentFile().mkdirs();
                        File file = new File(descFileDir);
                        OutputStream os = new FileOutputStream(file);
                        InputStream is = zipFile.getInputStream(entry);

                        //int readByte;
                        while((readByte = is.read(buf)) != -1) {
                            os.write(buf, 0, readByte);
                        }

                        os.close();
                        is.close();
                    }
                }

                zipFile.close();
                log.debug("文件解压成功!");
                return true;
            }
        } catch (Exception var13) {
            log.debug("文件解压失败：" + var13.getMessage());
            return false;
        }
    }

    public static void zipDirectoryToZipFile(String dirPath, File fileDir, ZipOutputStream zouts) {
        if (fileDir.isDirectory()) {
            File[] files = fileDir.listFiles();
            if (files.length == 0) {
                ZipEntry entry = new ZipEntry(getEntryName(dirPath, fileDir));

                try {
                    zouts.putNextEntry(entry);
                    zouts.closeEntry();
                } catch (Exception var6) {
                    var6.printStackTrace();
                }

                return;
            }

            for(int i = 0; i < files.length; ++i) {
                if (files[i].isFile()) {
                    zipFilesToZipFile(dirPath, files[i], zouts);
                } else {
                    zipDirectoryToZipFile(dirPath, files[i], zouts);
                }
            }
        }

    }

    public static void zipFilesToZipFile(String dirPath, File file, ZipOutputStream zouts) {
        FileInputStream fin = null;
        ZipEntry entry = null;
        byte[] buf = new byte[4096];
        //int readByte = false;
        int readByte = 0;
        if (file.isFile()) {
            try {
                fin = new FileInputStream(file);
                entry = new ZipEntry(getEntryName(dirPath, file));
                zouts.putNextEntry(entry);

                //int readByte;
                while((readByte = fin.read(buf)) != -1) {
                    zouts.write(buf, 0, readByte);
                }

                zouts.closeEntry();
                fin.close();
                System.out.println("添加文件 " + file.getAbsolutePath() + " 到zip文件中!");
            } catch (Exception var8) {
                var8.printStackTrace();
            }
        }

    }

    private static String getEntryName(String dirPath, File file) {
        String dirPaths = dirPath;
        if (!dirPath.endsWith(File.separator)) {
            dirPaths = dirPath + File.separator;
        }

        String filePath = file.getAbsolutePath();
        if (file.isDirectory()) {
            filePath = filePath + "/";
        }

        int index = filePath.indexOf(dirPaths);
        return filePath.substring(index + dirPaths.length());
    }

    public static void writeFile(String content, String filePath) {
        try {
            if (createFile(filePath)) {
                FileWriter fileWriter = new FileWriter(filePath, true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(content);
                bufferedWriter.close();
                fileWriter.close();
            } else {
                log.info("生成失败，文件已存在！");
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public static void downloadFile(HttpServletResponse response, String filePath, String fileName, boolean inline) throws Exception {
        File file = new File(filePath);
        downloadFile(response, file, fileName, inline);
    }

    public static void downloadFile(HttpServletResponse response, String filePath, String fileName) throws Exception {
        File file = new File(filePath);
        downloadFile(response, file, fileName, false);
    }

    public static void downloadFile(HttpServletResponse response, File file, String fileName) throws Exception {
        downloadFile(response, file, fileName, false);
    }

    public static void downloadFile(HttpServletResponse response, File file, String fileName, boolean inline) throws Exception {
        OutputStream outp = null;
        FileInputStream br = null;
        boolean var6 = false;

        try {
            br = new FileInputStream(file);
            response.reset();
            outp = response.getOutputStream();
            outp.flush();
            response.setContentType("application/octet-stream");
            response.setContentLength((int)file.length());
            String header = (inline ? "inline" : "attachment") + ";filename=" + new String(fileName.getBytes(), "ISO8859-1");
            response.addHeader("Content-Disposition", header);
            byte[] buf = new byte[1024];

            int len;
            while((len = br.read(buf)) != -1) {
                outp.write(buf, 0, len);
            }

            outp.flush();
            outp.close();
        } finally {
            if (br != null && 0 == br.available()) {
                br.close();
            }

        }
    }

    public static void downloadFile(HttpServletResponse response, InputStream is, String realName) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            response.setContentType("text/html;charset=UTF-8");
            long fileLength = (long)is.available();
            response.setContentType("application/octet-stream");
            realName = new String(realName.getBytes("GBK"), "ISO8859-1");
            response.setHeader("Content-disposition", "attachment; filename=" + realName);
            response.setHeader("Content-Length", String.valueOf(fileLength));
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];

            int bytesRead;
            while(-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception var17) {
        } finally {
            try {
                bos.close();
                bis.close();
            } catch (IOException var16) {
                var16.printStackTrace();
            }

        }

    }

    public static InputStream uploadFile(HttpServletRequest request) {
        InputStream is = null;
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest)request;
        Map<String, MultipartFile> fileMap = mRequest.getFileMap();
        Iterator<Entry<String, MultipartFile>> it = fileMap.entrySet().iterator();
        if (it.hasNext()) {
            Entry<String, MultipartFile> entry = (Entry)it.next();
            MultipartFile mFile = (MultipartFile)entry.getValue();

            try {
                is = mFile.getInputStream();
            } catch (IOException var8) {
                var8.printStackTrace();
            }
        }

        return is;
    }

    public static String getFileMD5String(File file) {
        try {
            InputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            boolean var3 = false;

            int numRead;
            while((numRead = fis.read(buffer)) > 0) {
                messagedigest.update(buffer, 0, numRead);
            }

            fis.close();
            return bufferToHex(messagedigest.digest());
        } catch (Exception var4) {
            return UUID.randomUUID().toString().replaceAll("-", "");
        }
    }

    public static String bufferToHex(byte[] bytes) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte[] bytes, int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;

        for(int l = m; l < k; ++l) {
            appendHexPair(bytes[l], stringbuffer);
        }

        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 240) >> 4];
        char c1 = hexDigits[bt & 15];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    public static String getHumanSize(long fileSize) {
        DecimalFormat df = new DecimalFormat("#.##");
        String[] units = new String[]{"字节", "KB", "MB", "GB"};
        int i = 0;

        double size;
        for(size = (double)fileSize; size > 1024.0D; ++i) {
            size /= 1024.0D;
        }

        return df.format(size) + units[i];
    }

    public static void zipDownLoad(File file, HttpServletResponse response) throws IOException {
        ServletOutputStream out = response.getOutputStream();
        ZipOutputStream zipout = new ZipOutputStream(out);
        zipout.setLevel(1);
        File[] var4 = file.listFiles();
        int var5 = var4.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            File f = var4[var6];
            if (f.isFile()) {
                compressFile(f, f.getName(), zipout, "");
            } else {
                compressFolder(f, f.getName(), zipout, "");
            }
        }

        zipout.finish();
        zipout.flush();
    }

    private static void compressFolder(File dir, String fileName, ZipOutputStream zipout, String baseDir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();

            for(int i = 0; i < files.length; ++i) {
                if (files[i].isDirectory()) {
                    if (baseDir.equals("/")) {
                        baseDir = "";
                    }

                    compressFolder(files[i], files[i].getName(), zipout, baseDir + fileName + "/");
                } else {
                    compressFile(files[i], files[i].getName(), zipout, baseDir + fileName + "/");
                }
            }

        }
    }

    private static void compressFile(File file, String fileName, ZipOutputStream zipout, String baseDir) {
        try {
            ZipEntry entry = null;
            if (baseDir.equals("/")) {
                baseDir = "";
            }

            String filename = new String((baseDir + fileName).getBytes(), "GBK");
            entry = new ZipEntry(filename);
            entry.setSize(file.length());
            zipout.putNextEntry(entry);
            BufferedInputStream fr = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[1024];

            int len;
            while((len = fr.read(buffer)) != -1) {
                zipout.write(buffer, 0, len);
            }

            fr.close();
        } catch (IOException var9) {
        }

    }

    public static void fileWrite(String fileName, String contents) {
        Preconditions.checkNotNull(fileName, "Provided file name for writing must NOT be null.");
        Preconditions.checkNotNull(contents, "Unable to write null contents.");
        File newFile = new File(fileName);

        try {
            Files.write(contents.getBytes("utf-8"), newFile);
        } catch (IOException var4) {
            System.err.println("ERROR trying to write to file '" + fileName + "' - " + var4.toString());
        }

    }

    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var1) {
            var1.printStackTrace();
        }

        log = LoggerFactory.getLogger(IOUtils.class);
    }
}
