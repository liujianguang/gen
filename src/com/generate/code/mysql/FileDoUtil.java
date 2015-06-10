package com.generate.code.mysql;
/**
 * 
 */


import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * @author Administrator
 *
 */
public class FileDoUtil {

	/**
	 * 查找文件
	 * @param fileRelativePath
	 * @return File
	 */
	public static File findFile(String fileRelativePath) {
		String path = getBasePath();
		path += fileRelativePath;
		System.out.println("[FILE_PATH]:::"+path);
		// /
		File file = new File(path);
		if (file.exists()) {
			return file;
		} else {
			return null;
		}
	}

	/**
	 * 获取文件的基路径
	 * 
	 * @return
	 */
	public static String getBasePath() {
		String path = FileDoUtil.class.getResource("/").toString();
		path = path.replace("file:/", "");
		return path;
	}

	public static void outFile(String path,String content){
		File file = new File(path);
		try {
			if(!file.exists()){
				int index = path.lastIndexOf("/");
				String dirsPath = new String(path.substring(0,index));
				File filedirs = new File(dirsPath);
				if(!filedirs.exists()){
					filedirs.mkdirs();
				}
				file.createNewFile();
			}
			FileUtils.writeStringToFile(file, content, "UTF-8");
		} catch (IOException e) {
			System.out.println("输出文件："+path+ "  发生异常..........");
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {

	}

}
