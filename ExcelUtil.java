package cn.sinobest.ypgj.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import jxl.Cell;
import jxl.Sheet;
/**
 * Excel工具类
 * @author chenjianhua
 *
 */
public class ExcelUtil {
	/**
	 * 把Excel表中的工作表转为Json格式的字符串
	 * @return
	 */
	public  static String parseSheet2JSON(Sheet sheet){
		String result = "";
		if(sheet!=null&&sheet.getRows()>0){
		
			List<List<String>> resultList = new ArrayList<List<String>>();
			for(int i=0,len=sheet.getRows();i<len;i++){
				List<String> list = new ArrayList<String>();
				Cell[] rows = sheet.getRow(i);
				for(int j=0,len1=rows.length;j<len1;j++){
                    if(rows[j].getContents()!=null&&!"".equals(rows[j].getContents()))
					    list.add(rows[j].getContents());
				}
				resultList.add(list);
			}

			result = JSONArray.fromObject(resultList).toString();
			//System.out.println("json:"+result);
		}
		return result;
	}
}
