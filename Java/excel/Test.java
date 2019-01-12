package com.self.excel;

import java.util.List;
import java.util.Map;

/**
 * 测试操作Excel的类
 */
public class Test {
	
	public static void main(String[] args) {

		//传入的文件名
		String fileName = "2007.xlsx";
		
		//判断Excel文件是那种类型
		if(POI.isHSSForXSSF(fileName)){
			dealWithHSSF(fileName);
		}else{
			dealWithXSSF(fileName);
		}

	}
	
	/**
	 * HSSF版Excel操作 
	 */
	public static void dealWithHSSF(String fileName){
		//从HSSF版的Excel中获取数据
		List<List<String>> listHSSF = POI.getExcelByHSSF("E:\\excel\\" + fileName);
		
		//将HSSF版的Excel数据组装成合适的格式
		List<Map<String,Object>> listMapHSSF = POI.dataConvert(listHSSF);
		
		//将数据写入到_new文件中去
		POI.setExcelByHSSF(listMapHSSF, "E:\\excel\\" + POI.getNewFileName(fileName));
	}
	
	/**
	 * XSSF版Excel操作
	 */
	public static void dealWithXSSF(String fileName){
		//从XSSF版的Excel中获取数据
		List<List<String>> listXSSF = POI.getExcelByXSSF("E:\\excel\\" + fileName);
		
		//将XSSF版的Excel数据组装成合适的格式
		List<Map<String,Object>> listMapXSSF = POI.dataConvert(listXSSF);
		
		//将数据写入到_new文件中去
		POI.setExcelByXSSF(listMapXSSF, "E:\\excel\\" + POI.getNewFileName(fileName));
	}
	
}
