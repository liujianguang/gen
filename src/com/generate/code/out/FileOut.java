/**
 * 
 */
package com.generate.code.out;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.generate.code.ConfigConst;
import com.generate.code.model.TableFieldInfo;
import com.generate.code.mysql.FileDoUtil;

/**
 * @author Administrator
 *
 */
public class FileOut {
	private static String privateStr = "private ";
	private static String publicStr = "public ";
	private static String package_pojo = "pojo";
	private static String package_dao = "dao";
	private static String package_service = "service";
	private static String package_action = "http.action.mgr";

	public static void outVO(String basePackageStr, String className, String tableName, List<TableFieldInfo> tableFieldInfoList) {
		StringBuilder classStrb = new StringBuilder();
		StringBuilder importStrb = new StringBuilder();
		StringBuilder fieldStrb = new StringBuilder();
		StringBuilder functionStrb = new StringBuilder();
		String packageStr = basePackageStr + package_pojo ;
		classStrb.append("package " + packageStr  + ";\n");
		// /
		importStrb.append("import java.util.Date;\n");
		importStrb.append("import javax.persistence.Column;\n");
		importStrb.append("import javax.persistence.Entity;\n");
		importStrb.append("import javax.persistence.GeneratedValue;\n");
		importStrb.append("import javax.persistence.GenerationType;\n");
		importStrb.append("import javax.persistence.Id;\n");
		importStrb.append("import javax.persistence.Table;\n");
		importStrb.append("import javax.persistence.Temporal;\n");
		importStrb.append("import javax.persistence.TemporalType;\n");
		importStrb.append("import org.apache.commons.lang.builder.ToStringBuilder;\n");
		importStrb.append("import org.codehaus.jackson.map.annotate.JsonSerialize;\n");
		importStrb.append("import core.web.transform.json.JacksonDateTimeSerializer;\n");
		// //
		fieldStrb.append("\tprivate static final long serialVersionUID = 1L;\n");
		if (tableFieldInfoList != null) {
			for (TableFieldInfo model : tableFieldInfoList) {
				String attrType = getFieldType(model.getType().toUpperCase());
				String attrName = getFieldName(model.getName());
				// /
				fieldStrb.append("\t/**\n");
				fieldStrb.append("\t *  " + model.getNote() + "\n");
				fieldStrb.append("\t */\n");
				if ("id".equals(model.getName())) {
					fieldStrb.append("\t@Id\n");
					fieldStrb.append("\t@GeneratedValue(strategy = GenerationType.AUTO)\n");
					fieldStrb.append("\t@Column(name = \"id\", unique = true, nullable = false)\n");
				} else {
					fieldStrb.append("\t@Column(name =\"" + model.getName() + "\", length = " + model.getLength() + ")\n");
				}
				fieldStrb.append("\t" + privateStr);
				fieldStrb.append(attrType);
				fieldStrb.append(attrName + ";\n");
				// /
				if ("DATETIME".equals(model.getType())) {
					functionStrb.append("\t@JsonSerialize(using = JacksonDateTimeSerializer.class)\n\t@Temporal(TemporalType.TIMESTAMP)\n");
				}
				functionStrb.append("\t" + publicStr);
				functionStrb.append(attrType);
				functionStrb.append(getFunctionGet(attrName));
				functionStrb.append("(){\n");
				functionStrb.append("\t\treturn " + attrName + ";\n\t}\n");

				functionStrb.append("\t" + publicStr);
				functionStrb.append("void ");
				functionStrb.append(getFunctionSet(attrName));
				functionStrb.append("(" + attrType + attrName + "){\n");
				functionStrb.append("\t\tthis." + attrName + "= " + attrName + ";\n\t}\n");

			}
		}

		classStrb.append(importStrb.toString());
		classStrb.append("\n@Entity\n@Table(name = \"" + tableName + "\")\npublic class " + className + " implements java.io.Serializable{\n");
		classStrb.append(fieldStrb.toString());
		classStrb.append(functionStrb.toString());
		classStrb.append("\t@Override\n");
		classStrb.append("\tpublic String toString() {\n");
		classStrb.append("\t\treturn ToStringBuilder.reflectionToString(this);\n\t}\n");
		classStrb.append("}\n");
		String path = ConfigConst.OUT_FILE_BASE_PATH + "/" + packageStr.replace(".", "/") + "/" + className + ".java";
		System.out.println("=====" + path);
		FileDoUtil.outFile(path, classStrb.toString());
	}

	public static void outDao(String basePackageStr, String className) {
		StringBuilder classStrb = new StringBuilder();
		String packageStr = basePackageStr + package_dao;
		classStrb.append("package " + packageStr + ";\n");
		classStrb.append("import java.util.Map; \n");
		classStrb.append("import com.base.extend.ExtendHibernateDao;\n");
		classStrb.append("import " + basePackageStr + package_pojo + "." + className + ";\n");
		classStrb.append("public interface ");
		classStrb.append(className + "Dao extends ExtendHibernateDao<");
		classStrb.append(className + ">{\n\n}\n");
		String path = ConfigConst.OUT_FILE_BASE_PATH + "/" + packageStr.replace(".", "/") + "/" + className + "Dao.java";
		System.out.println("=====" + path);
		FileDoUtil.outFile(path, classStrb.toString());
	}

	public static void outDaoImpl(String basePackageStr, String className) {
		StringBuilder classStrb = new StringBuilder();
		String packageStr = basePackageStr + package_dao + ".impl";
		classStrb.append("package " + packageStr + ";\n");
		classStrb.append("import com.base.extend.ExtendHibernateDaoImpl;\n");
		classStrb.append("import " + basePackageStr + package_dao + "." + className + "Dao;\n");
		classStrb.append("import " + basePackageStr + package_pojo + "." + className + ";\n");
		classStrb.append("public class ");
		classStrb.append(className + "DaoImpl extends ExtendHibernateDaoImpl<");
		classStrb.append(className + "> implements ");
		classStrb.append(className + "Dao{\n\n}\n");
		String path = ConfigConst.OUT_FILE_BASE_PATH + "/" + packageStr.replace(".", "/") + "/" + className + "DaoImpl.java";
		System.out.println("=====" + path);
		FileDoUtil.outFile(path, classStrb.toString());
	}
	
	public static void outServiceByTemplate(String basePackageStr, String pojo) {
		String packageStr = basePackageStr + package_service;
		String import_pojo = basePackageStr + package_pojo + "." + pojo ;
		String className = pojo + "Service";
		File templateFile = FileDoUtil.findFile("templates/service.txt");
		if(templateFile != null){
			try {
				String contentStr = FileUtils.readFileToString(templateFile,"UTF-8");
				contentStr = contentStr.replace("#package#", packageStr);
				contentStr = contentStr.replace("#import_pojo#", import_pojo);
				contentStr = contentStr.replace("#className#", className);
				contentStr = contentStr.replace("#create_date#", getFormateDate());
				contentStr = contentStr.replace("#pojo#", pojo);
				String path = ConfigConst.OUT_FILE_BASE_PATH + "/" + packageStr.replace(".", "/") + "/" + className + ".java";
				System.out.println("=====" + path);
				FileDoUtil.outFile(path, contentStr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void outServiceImplByTemplate(String basePackageStr, String pojo) {
		String packageStr = basePackageStr + package_service + ".impl";
		String import_pojo = basePackageStr + package_pojo + "." + pojo ;
		String import_pojo_dao = basePackageStr + package_dao + "." + pojo + "Dao";
		String import_pojo_service = basePackageStr + package_service + "." + pojo + "Service";
		String result = (new String(pojo.substring(0, 1))).toLowerCase()+new String(pojo.substring(1));
		String pojoDaoVar =  result + "Dao";
		String className = pojo + "ServiceImpl";
		File templateFile = FileDoUtil.findFile("templates/serviceimpl.txt");
		if(templateFile != null){
			try {
				String contentStr = FileUtils.readFileToString(templateFile,"UTF-8");
				contentStr = contentStr.replace("#package#", packageStr);
				contentStr = contentStr.replace("#import_pojo#", import_pojo);
				contentStr = contentStr.replace("#import_pojoDao#", import_pojo_dao);
				contentStr = contentStr.replace("#import_pojoService#", import_pojo_service);
				contentStr = contentStr.replace("#create_date#", getFormateDate());
				contentStr = contentStr.replace("#pojo#", pojo);
				contentStr = contentStr.replace("#pojoDaoVar#", pojoDaoVar);
				String path = ConfigConst.OUT_FILE_BASE_PATH + "/" + packageStr.replace(".", "/") + "/" + className + ".java";
				System.out.println("=====" + path);
				FileDoUtil.outFile(path, contentStr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void outService(String basePackageStr, String className) {
		StringBuilder classStrb = new StringBuilder();
		String packageStr = basePackageStr + package_service;
		classStrb.append("package " + packageStr + ";\n");
		classStrb.append("import java.util.Map; \n");
		classStrb.append("import core.util.Pagination; \n");
		classStrb.append("import com.base.extend.SearchParamValue; \n");
		classStrb.append("import " + basePackageStr + package_pojo + "." + className + ";\n");
		classStrb.append("public interface ");
		classStrb.append(className + "Service{\n");
		classStrb.append("public Pagination<" + className + "> findListUp(Integer offSet, Integer pageSize, Map<String, SearchParamValue> whereParamMap);\n");
		classStrb.append("\n}\n");
		String path = ConfigConst.OUT_FILE_BASE_PATH + "/" + packageStr.replace(".", "/") + "/" + className + "Service.java";
		System.out.println("=====" + path);
		FileDoUtil.outFile(path, classStrb.toString());
	}

	public static void outServiceImpl(String basePackageStr, String className) {
		StringBuilder classStrb = new StringBuilder();
		String packageStr = basePackageStr + package_service + ".impl";
		classStrb.append("package " + packageStr + ";\n");
		classStrb.append("import java.util.Map; \n");
		classStrb.append("import core.util.Pagination; \n");
		classStrb.append("import com.base.extend.SearchParamValue; \n");
		classStrb.append("import " + basePackageStr + package_pojo + "." + className + ";\n");
		classStrb.append("import " + basePackageStr + package_dao + "." + className + "Dao;\n");
		classStrb.append("import " + basePackageStr + package_service+ "." + className + "Service;\n");
		classStrb.append("public class ");
		classStrb.append(className + "ServiceImpl implements " + className + "Service{\n");
		String result = "";
		result += (new String(className.substring(0, 1))).toLowerCase();
		result += new String(className.substring(1)) + "Dao";
		String daoVar = className + "Dao " + result;
		classStrb.append(privateStr + daoVar + ";\n");
		classStrb.append(publicStr + "void " + getFunctionSet(result) + "(" + daoVar + "){\n");
		classStrb.append("this." + result + " = " + result + ";\n}\n");
		classStrb.append("public Pagination<" + className + "> findListUp(Integer offSet, Integer pageSize, Map<String, SearchParamValue> whereParamMap){\n");
		classStrb.append("return " + result + ".findListUp(offSet, pageSize, whereParamMap);\n");
		classStrb.append("\n}\n}\n");
		String path = ConfigConst.OUT_FILE_BASE_PATH + "/" + packageStr.replace(".", "/") + "/" + className + "ServiceImpl.java";
		System.out.println("=====" + path);
		FileDoUtil.outFile(path, classStrb.toString());
	}

	public static void outAction(String basePackageStr, String className) {
		StringBuilder classStrb = new StringBuilder();
		String packageStr = basePackageStr + package_action;
		classStrb.append("package " + packageStr + ";\n");
		classStrb.append("import java.util.Map; \n");
		classStrb.append("import javax.annotation.Resource;\n");
		classStrb.append("import core.util.Pagination; \n");
		classStrb.append("import com.base.extend.SearchParamValue; \n");
		classStrb.append("import javax.servlet.http.HttpServletRequest;\n");
		classStrb.append("import javax.servlet.http.HttpServletResponse;\n");
		classStrb.append("import org.apache.commons.logging.Log;\n");
		classStrb.append("import org.apache.commons.logging.LogFactory;\n");
		classStrb.append("import org.springframework.stereotype.Controller;\n");
		classStrb.append("import org.springframework.web.bind.ServletRequestUtils;\n");
		classStrb.append("import org.springframework.web.bind.annotation.RequestMapping;\n");
		classStrb.append("import org.springframework.web.bind.annotation.RequestMethod;\n");
		classStrb.append("import org.springframework.web.bind.annotation.ResponseBody;\n");
		classStrb.append("import core.bean.ResultBean;\n");
		classStrb.append("import com.music.http.aop.ClassSession;\n");
		classStrb.append("import " + basePackageStr + package_pojo + "." + className + ";\n");
		classStrb.append("import " + basePackageStr + package_service + "." + className + "Service;\n");
		classStrb.append("@Controller\n");
		classStrb.append("@RequestMapping(\"/" + className.toLowerCase() + "\")\n");
		classStrb.append("@ClassSession\n");
		classStrb.append("public class ");
		classStrb.append(className + "Action {\n");
		classStrb.append("private Log logger = LogFactory.getLog(" + className + "Action.class) ;\n");
		String result = "";
		result += (new String(className.substring(0, 1))).toLowerCase();
		result += new String(className.substring(1)) + "Service";
		String daoVar = className + "Service " + result;
		classStrb.append("@Resource\n");
		classStrb.append(privateStr + daoVar + ";\n");
		classStrb.append("\n}\n");
		String path = ConfigConst.OUT_FILE_BASE_PATH + "/" + packageStr.replace(".", "/") + "/" + className + "Action.java";
		System.out.println("=====" + path);
		FileDoUtil.outFile(path, classStrb.toString());
	}

	private static final Map<String, String> typeMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 935135465949526890L;
		{
			put("INT", "int ");
			put("VARCHAR", "String ");
			put("DATETIME", "Date ");
			put("TINYINT", "int ");
			put("BIT", "Boolean ");
			put("MEDIUMBLOB", "byte[] ");

		}
	};

	public static String getFieldType(String type) {
		return typeMap.get(type);
	}

	public static String getFieldName(String name) {

		String strs[] = name.split("_");
		String result = strs[0];
		for (int i = 1; i < strs.length; i++) {
			String s = strs[i];
			result += (new String(s.substring(0, 1))).toUpperCase();
			result += new String(s.substring(1));
		}
		return result;
	}

	public static String getFunctionGet(String s) {
		String result = "get";
		result += (new String(s.substring(0, 1))).toUpperCase();
		result += new String(s.substring(1));
		return result;
	}

	public static String getFunctionSet(String s) {
		String result = "set";
		result += (new String(s.substring(0, 1))).toUpperCase();
		result += new String(s.substring(1));
		return result;
	}
	
	public static String getFormateDate(){
		Date date = new Date();
		SimpleDateFormat formate = new SimpleDateFormat("yyyy年MM月dd日  hh:mm:ss");
		String str = formate.format(date);
		return str;
	}

}
