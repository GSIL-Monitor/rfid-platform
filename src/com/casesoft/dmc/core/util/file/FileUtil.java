package com.casesoft.dmc.core.util.file;

import com.casesoft.dmc.core.Constant;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class FileUtil {

  public final static String File_Dir = "";

  public static void init() {
    System.out.println(Constant.rootPath);
    initFolder(Constant.Folder.Epc_Init_Zip_File_Folder);
    initFolder(Constant.Folder.Epc_Init_File_Folder);
    initFolder(Constant.Folder.Report_File_Folder);

    try {
      initFolder(Constant.rootPath+File.separator+PropertyUtil.getValue("MilanUpload"));
      initFolder(Constant.rootPath+File.separator+PropertyUtil.getValue("MilanUploadHistory"));
      //增加云上传目录
      initFolder(Constant.rootPath+File.separator+PropertyUtil.getValue("MilanUpload")+File.separator+"Cloud");
      initFolder(Constant.rootPath+File.separator+PropertyUtil.getValue("MilanUploadHistory")+File.separator+"Cloud");
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private static boolean initFolder(String path) {
    File file = new File(path);
    if (!file.exists())
      return file.mkdirs();
    return true;
  }

  public static StringBuffer readFileToStr(String fileName) throws Exception {
    return readFileToStr(new File(fileName));
  }

  public static StringBuffer readFileToStr(File f) throws Exception {
    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
    return readInputStreamToStr(bis);
  }

  /**
   * 读取BufferedInputStream到String
   * 
   * @param bis
   * @return
   * @throws Exception
   */
  public static StringBuffer readInputStreamToStr(BufferedInputStream bis) throws Exception {
    StringBuffer sb = new StringBuffer();
    try {
      byte[] buffer = new byte[1024];
      int bytesRead = 0;
      // 从文件中按字节读取内容，到文件尾部时read方法将返回-1
      while ((bytesRead = bis.read(buffer)) != -1) {
        // 将读取的字节转为字符串对象
        String chunk = new String(buffer, 0, bytesRead);
        sb.append(chunk);
      }
    } catch (IOException ex) {
      ex.printStackTrace();
      throw ex;
    } finally {
      if (bis != null) {
        try {
          bis.close();
        } catch (IOException ex) {
          ex.printStackTrace();
          throw ex;
        }
      }
    }
    return sb;
  }

  /**
   * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
   * 
   * @param fileName
   *          文件的名
   */
  public static void readFileByBytes(String fileName) {
    File file = new File(fileName);
    InputStream in = null;
    try {
      System.out.println("以字节为单位读取文件内容，一次读一个字节：");
      // 一次读一个字节
      in = new FileInputStream(file);
      int tempbyte;
      while ((tempbyte = in.read()) != -1) {
        System.out.write(tempbyte);
      }
      in.close();
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }
    try {
      System.out.println("以字节为单位读取文件内容，一次读多个字节：");
      // 一次读多个字节
      byte[] tempbytes = new byte[100];
      int byteread = 0;
      in = new FileInputStream(fileName);
      FileUtil.showAvailableBytes(in);
      // 读入多个字节到字节数组中，byteread为一次读入的字节数
      while ((byteread = in.read(tempbytes)) != -1) {
        System.out.write(tempbytes, 0, byteread);
      }
    } catch (Exception e1) {
      e1.printStackTrace();
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e1) {
        }
      }
    }
  }

  /**
   * 以字符为单位读取文件，常用于读文本，数字等类型的文件
   * 
   * @param fileName
   *          文件名
   */
  public static void readFileByChars(String fileName) {
    File file = new File(fileName);
    Reader reader = null;
    try {
      System.out.println("以字符为单位读取文件内容，一次读一个字节：");
      // 一次读一个字符
      reader = new InputStreamReader(new FileInputStream(file));
      int tempchar;
      while ((tempchar = reader.read()) != -1) {
        // 对于windows下，rn这两个字符在一起时，表示一个换行。
        // 但如果这两个字符分开显示时，会换两次行。
        // 因此，屏蔽掉r，或者屏蔽n。否则，将会多出很多空行。
        if (((char) tempchar) != 'r') {
          System.out.print((char) tempchar);
        }
      }
      reader.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      System.out.println("以字符为单位读取文件内容，一次读多个字节：");
      // 一次读多个字符
      char[] tempchars = new char[30];
      int charread = 0;
      reader = new InputStreamReader(new FileInputStream(fileName));
      // 读入多个字符到字符数组中，charread为一次读取字符数
      while ((charread = reader.read(tempchars)) != -1) {
        // 同样屏蔽掉r不显示
        if ((charread == tempchars.length) && (tempchars[tempchars.length - 1] != 'r')) {
          System.out.print(tempchars);
        } else {
          for (int i = 0; i < charread; i++) {
            if (tempchars[i] == 'r') {
              continue;
            } else {
              System.out.print(tempchars[i]);
            }
          }
        }
      }
    } catch (Exception e1) {
      e1.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e1) {
        }
      }
    }
  }

  /**
   * 以行为单位读取文件，常用于读面向行的格式化文件
   * 
   * @param fileName
   *          文件名
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static List readFileByLines(String fileName) {
    List stringList = new ArrayList();

    File file = new File(fileName);
    BufferedReader reader = null;
    try {
      InputStreamReader read = new InputStreamReader(new FileInputStream(file), "GB2312");
      reader = new BufferedReader(read);
      String tempString = null;
      int line = 1;
      // 一次读入一行，直到读入null为文件结束
      while ((tempString = reader.readLine()) != null) {
        // 显示行号
        System.out.println("line " + line + ": " + tempString);
        stringList.add(tempString);
        line++;
      }
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e1) {
        }
      }
    }

    return stringList;
  }

  /**
   * 随机读取文件内容
   * 
   * @param fileName
   *          文件名
   */
  public static void readFileByRandomAccess(String fileName) {
    RandomAccessFile randomFile = null;
    try {
      System.out.println("随机读取一段文件内容：");
      // 打开一个随机访问文件流，按只读方式
      randomFile = new RandomAccessFile(fileName, "r");
      // 文件长度，字节数
      long fileLength = randomFile.length();
      // 读文件的起始位置
      int beginIndex = (fileLength > 4) ? 4 : 0;
      // 将读文件的开始位置移到beginIndex位置。
      randomFile.seek(beginIndex);
      byte[] bytes = new byte[10];
      int byteread = 0;
      // 一次读10个字节，如果文件内容不足10个字节，则读剩下的字节。
      // 将一次读取的字节数赋给byteread
      while ((byteread = randomFile.read(bytes)) != -1) {
        System.out.write(bytes, 0, byteread);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (randomFile != null) {
        try {
          randomFile.close();
        } catch (IOException e1) {
        }
      }
    }
  }

  /**
   * 显示输入流中还剩的字节数
   * 
   * @param in
   */
  private static void showAvailableBytes(InputStream in) {
    try {
      System.out.println("当前字节输入流中的字节数为:" + in.available());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 将字符串写入文件 FileWriter在写出文件时是以系统默认的编码格式输出的，因此对于一些不同编码格式容易产生乱码， 尤其对于中文字符，
   * 在使用时应格外留意。为了解决这个问题，可以使用OutputStreamWriter， 示例如下： 这段代码就是指定输出文件filename，使用UTF-8的编码格式以append
   * 的形式输出字符串 toStr
   * 
   * @param
   */
  public static File writeStringToFile(String str, String fileName) {
    // System.out.println(fileName);
    // FileWriter fw = null;
    OutputStreamWriter osw = null;
    File file = null;
    try {
      file = new File(fileName);
      if (!file.exists())
        file.createNewFile();
      osw = new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8");
      osw.write(str);
      // fw = new FileWriter(file);
      long begin3 = System.currentTimeMillis();
      // fw.write(str);
      // fw.close();
      osw.close();
      long end3 = System.currentTimeMillis();
      System.out.println("FileWriter执行耗时:" + (end3 - begin3) + " 毫秒");
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        // fw.close();
        osw.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return file;
  }

  /**
   * 压缩文件夹souceFileName到文件destFileName
   * 
   * @param souceFileName
   * @param destFileName
   */
  public static void zip(String souceFileName, String destFileName) {
    File file = new File(souceFileName);
    try {
      zip(file, destFileName);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void zip(File souceFile, String destFileName) throws IOException {
    FileOutputStream fileOut = null;
    try {
      fileOut = new FileOutputStream(destFileName);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    ZipOutputStream out = new ZipOutputStream(fileOut);
    zip(souceFile, out, "");
    out.close();
  }

  private static void zip(File souceFile, ZipOutputStream out, String base) throws IOException {

    if (souceFile.isDirectory()) {
      File[] files = souceFile.listFiles();
      out.putNextEntry(new ZipEntry(base + "/"));
      base = base.length() == 0 ? "" : base + "/";
      for (File file : files) {
        zip(file, out, base + file.getName());
      }
    } else {
      if (base.length() > 0) {
        out.putNextEntry(new ZipEntry(base));
      } else {
        out.putNextEntry(new ZipEntry(souceFile.getName()));// 把待压缩文件放入out流
      }
      // System.out.println("filepath=" + souceFile.getPath());
      FileInputStream in = new FileInputStream(souceFile);

      int b;
      byte[] by = new byte[1024];
      while ((b = in.read(by)) != -1) {
        out.write(by, 0, b);
      }
      in.close();
    }
  }

  public static void unzip(File srcFile, String destPath) throws IOException {
    ZipFile zipFile = null;

    try {
      zipFile = new ZipFile(srcFile);
      Enumeration<?> enu = zipFile.entries();
      while (enu.hasMoreElements()) {
        ZipEntry entry = (ZipEntry) enu.nextElement();
        if (entry.isDirectory()) {
          new File(destPath + entry.getName()).mkdir();
          continue;
        }
        BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
        File file = new File(destPath + entry.getName());
        // 加入这个的原因是zipfile读取文件是随机读取的，这就造成可能先读取一个文件
        // 而这个文件所在的目录还没有出现过，所以要建出目录来。
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
          parent.mkdirs();
        }
        final int BUFFER = 2048;
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER);

        int count;
        byte data[] = new byte[BUFFER];
        while ((count = bis.read(data, 0, BUFFER)) != -1) {
          bos.write(data, 0, count);
        }
        bos.flush();
        bos.close();
        bis.close();

      }
    } catch (IOException e) {

      e.printStackTrace();
    } finally {
      if (zipFile != null)
        zipFile.close();
    }
  }

  /**
   * 解压缩文件 upzip
   * 
   * @throws IOException
   */
  public static void unzip(String srcFile, String destPath) throws IOException {
    unzip(new File(srcFile), destPath);
  }

  /**
   * * 递归删除目录下的所有文件及子目录下所有文件 * @param dir 将要删除的文件目录 * @return boolean Returns "true" if all
   * deletions were successful. * If a deletion fails, the method stops attempting to * delete and
   * returns "false".
   * 
   * */
  public static boolean deleteDir(File dir) {
    if (dir.isDirectory()) {
      String[] children = dir.list();// 递归删除目录中的子目录下
      for (int i = 0; i < children.length; i++) {
        boolean success = deleteDir(new File(dir, children[i]));
        if (!success) {
          return false;
        }
      }
    }// 目录此时为空，可以删除
    return dir.delete();

  }

  /**
   * 移动文件目录，移动成功，返回true，否则返回false
   * 
   * @param srcDir
   * @param destDir
   * @return
   */
  public static boolean rename(File srcDir, File destDir) {
    // if(srcDir.isDirectory()) {
    // String[] children = srcDir.list();
    // for(int i=0;i<children.length;i++) {
    // boolean success = moveDir(new File(children[i]),destDir);
    // if(! success) {
    // return false;//应该增加回滚操作
    // }
    // }
    // }

    return srcDir.renameTo(destDir);

  }

  public static boolean moveFile(File srcFile, String destPath) {
    OutputStream os = null;
    boolean success = true;
    try {
      InputStream is = new FileInputStream(srcFile);
      File toFile = new File(destPath); // 设置目标文件
      os = new FileOutputStream(toFile);
      // 设置缓存
      byte[] buffer = new byte[1024];
      int length = 0;
      // 读取myFile文件输出到toFile文件中
      while ((length = is.read(buffer)) > 0) {
        os.write(buffer, 0, length);
      }
      // 关闭输入流
      is.close();
      // 关闭输出流
      os.close();
    } catch (Exception ex) {
      success = false;
      ex.printStackTrace();
    } finally {
      try {
        if(os!=null) {
          os.close();
        }
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    return success;
  }

  public static File[] filterFile(File filePath, final String flag) {
    File[] files = filePath.listFiles(new FileFilter() {

      @Override
      public boolean accept(File pathname) {
        String fileName = pathname.getAbsolutePath();
        return fileName.toLowerCase().contains(flag.toLowerCase());
      }

    });
    return files;
  }

}
