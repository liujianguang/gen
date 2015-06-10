/**
 * 
 */
package com.generate.code;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import com.generate.code.mysql.FileDoUtil;

/**
 * @author Administrator
 *
 */
public class ConfigConst {
	public static void init() {
		File file = FileDoUtil.findFile("config/config.properties");
		if (file != null) {
			try {
				FileInputStream fIn = new FileInputStream(file);
				Properties p = new Properties();
				p.load(fIn);
				// /
				OUT_FILE_BASE_PATH = p.getProperty("file.out.base.path");
				// //
				fIn.close();
				fIn = null;
			} catch (Exception e) {
				System.out.println("[error]load and read db properties error.............");
				e.printStackTrace();
			}

		} else {
			System.out.println("[error]the file not found......");
		}
	}

	public static String OUT_FILE_BASE_PATH = "";
}
