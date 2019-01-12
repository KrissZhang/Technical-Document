package com.self.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class POI {
	
	/**
	 * 通过HSSF获取HSSF版本的Excel数据
	 * @param path 文件路径
	 */
	public static List<List<String>> getExcelByHSSF(String path){
		
		//存储Excel中返回的数据
		List<List<String>> list = new ArrayList<List<String>>();
		
		try {
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(path));
			HSSFWorkbook wb = new HSSFWorkbook(fs);//工作簿
			HSSFSheet sheet = wb.getSheetAt(0);//第一页
			int rowNum = sheet.getLastRowNum();//总行数
			HSSFRow firstRow = sheet.getRow(0);//第一行
			int cellNum = firstRow.getLastCellNum();//总列数
			for(int i=1;i<rowNum+1;i++){
				List<String> listRow = new ArrayList<String>();
				HSSFRow row = sheet.getRow(i);//当前行
				for(int j=0;j<cellNum;j++){
					HSSFCell cell = row.getCell(j);//当前列
					
					//判断当前列的数据类型并获取当前列的值
					listRow.add(getCellByHSSF(cell));
				}
				list.add(listRow);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 * 获取HSSF版当前列的值
	 * @param cell 当前列
	 */
	private static String getCellByHSSF(HSSFCell cell) {

		//列值
		String cellValue = "";
		
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_NUMERIC://数字
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				//如果为时间格式的内容(这里需要对不同的时间格式进行自定义)
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				cellValue = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
				break;
			} else {
				cellValue = new DecimalFormat("0").format(cell.getNumericCellValue());
				break;
			}
		case HSSFCell.CELL_TYPE_STRING://字符串
			cellValue = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN://布尔值
			cellValue = cell.getBooleanCellValue() + "";
			break;
		case HSSFCell.CELL_TYPE_FORMULA://公式
			cellValue = cell.getCellFormula() + "";
			break;
		case HSSFCell.CELL_TYPE_BLANK://空值
			cellValue = "";
			break;
		case HSSFCell.CELL_TYPE_ERROR://故障
			cellValue = "非法字符";
			break;
		default:
			cellValue = "未知类型";
			break;
		}

		return cellValue;
	}
	
	/**
	 * 获取XSSF版当前列的值
	 * @param cell 当前列
	 */
	private static String getCellByXSSF(XSSFCell cell) {

		//列值
		String cellValue = "";
		
		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_NUMERIC://数字
			if (DateUtil.isCellDateFormatted(cell)) {
				//如果为时间格式的内容(这里需要对不同的时间格式进行自定义)
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				cellValue = sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue())).toString();
				break;
			} else {
				cellValue = new DecimalFormat("0").format(cell.getNumericCellValue());
				break;
			}
		case XSSFCell.CELL_TYPE_STRING://字符串
			cellValue = cell.getStringCellValue();
			break;
		case XSSFCell.CELL_TYPE_BOOLEAN://布尔值
			cellValue = cell.getBooleanCellValue() + "";
			break;
		case XSSFCell.CELL_TYPE_FORMULA://公式
			cellValue = cell.getCellFormula() + "";
			break;
		case XSSFCell.CELL_TYPE_BLANK://空值
			cellValue = "";
			break;
		case XSSFCell.CELL_TYPE_ERROR://故障
			cellValue = "非法字符";
			break;
		default:
			cellValue = "未知类型";
			break;
		}

		return cellValue;
	}
	
	/**
	 * 数据格式转换
	 * @param list
	 * @return
	 */
	public static List<Map<String,Object>> dataConvert(List<List<String>> list){
		
		//组装后的数据
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		
		for(int i=0;i<list.size();i++){
			Map<String,Object> map = new HashMap<String,Object>();
			List<String> listCell = list.get(i);
			for(int j=0;j<listCell.size();j++){
				if(j==0){
					//Index
					map.put("Index", listCell.get(j));
				}else if(j==1){
					//UName
					map.put("UName", listCell.get(j));
				}else if(j==2){
					//Pwd
					map.put("Pwd", listCell.get(j));
				}else if(j==3){
					//Birth
					map.put("Birth", listCell.get(j));
				}
			}
			listMap.add(map);
		}
		
		return listMap;
	}
	
	/**
	 * 将HSSF数据写入到_new中去
	 * @param list 数据集
	 */
	public static void setExcelByHSSF(List<Map<String,Object>> list,String path){
		HSSFWorkbook wb = new HSSFWorkbook();//工作簿
		HSSFSheet sheet = wb.createSheet("Sheet1");//页
		
		//创建首行
		HSSFRow firstRow = sheet.createRow(0);
		HSSFCellStyle firstStyle = wb.createCellStyle();
		firstStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//创建居中格式
		
		HSSFCell firstCell = firstRow.createCell(0);
		firstCell.setCellValue("Index");
		firstCell.setCellStyle(firstStyle);
		
		firstCell = firstRow.createCell(1);
		firstCell.setCellValue("UName");
		firstCell.setCellStyle(firstStyle);
		
		firstCell = firstRow.createCell(2);
		firstCell.setCellValue("Pwd");
		firstCell.setCellStyle(firstStyle);
		
		firstCell = firstRow.createCell(3);
		firstCell.setCellValue("Birth");
		firstCell.setCellStyle(firstStyle);
		
		//添加数据
		for(int i=0;i<list.size();i++){
			Map<String,Object> map = list.get(i);
			HSSFRow row = sheet.createRow(i+1);
			
			HSSFCell cell = null;
			
			//第一列
			cell = row.createCell(0);
			cell.setCellValue(map.get("Index").toString());
			cell.setCellStyle(firstStyle);
			
			//第二列
			cell = row.createCell(1);
			cell.setCellValue(map.get("UName").toString());
			cell.setCellStyle(firstStyle);
			
			//第三列
			cell = row.createCell(2);
			cell.setCellValue(map.get("Pwd").toString());
			cell.setCellStyle(firstStyle);
			
			//第四列
			cell = row.createCell(3);
			cell.setCellValue(map.get("Birth").toString());
			cell.setCellStyle(firstStyle);

		}
		
		//将工作簿写入文件
		FileOutputStream fps = null;
		try {
			fps = new FileOutputStream(path);
			wb.write(fps);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(fps != null){
				try {
					fps.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	/**
	 * 通过XSSF获取XSSF版本的Excel数据
	 * @param path 文件路径
	 */
	public static List<List<String>> getExcelByXSSF(String path){
		
		//存储Excel中返回的数据
		List<List<String>> list = new ArrayList<List<String>>();
		
		InputStream is = null;
		Workbook wb = null;
		try {
			is = new FileInputStream(new File(path));
			wb = WorkbookFactory.create(is);//工作簿
			is.close();
			
			Sheet sheet = wb.getSheetAt(0);//第一页
			int rowNum = sheet.getLastRowNum();//总行数
			Row firstRow = sheet.getRow(0);//第一行
			int cellNum = firstRow.getLastCellNum();//总列数
			for(int i=1;i<rowNum+1;i++){
				List<String> listRow = new ArrayList<String>();
				Row row = sheet.getRow(i);//当前行
				for(int j=0;j<cellNum;j++){
					Cell cell = row.getCell(j);//当前列
					XSSFCell xCell = null;
					if(cell instanceof XSSFCell){
						xCell = (XSSFCell)cell;
					}
					listRow.add(getCellByXSSF(xCell));
				}
				list.add(listRow);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 * 将XSSF数据写入到_new中去
	 * @param list 数据集
	 */
	public static void setExcelByXSSF(List<Map<String,Object>> list,String path){
		XSSFWorkbook wb = new XSSFWorkbook();//工作簿
		XSSFSheet Sheet = wb.createSheet("Sheet1");//页
		
		//创建首行
		XSSFRow firstRow = Sheet.createRow(0);//第一行表头
		XSSFCellStyle firstStyle = wb.createCellStyle();//列样式
		firstStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);//创建居中格式
		
		XSSFCell Cell = firstRow.createCell(0);
		Cell.setCellValue("Index");
		Cell.setCellStyle(firstStyle);
		
		Cell = firstRow.createCell(1);
		Cell.setCellValue("UName");
		Cell.setCellStyle(firstStyle);
		
		Cell = firstRow.createCell(2);
		Cell.setCellValue("Pwd");
		Cell.setCellStyle(firstStyle);
		
		Cell = firstRow.createCell(3);
		Cell.setCellValue("Birth");
		Cell.setCellStyle(firstStyle);
		
		//添加数据
		for(int i=0;i<list.size();i++){
			Map<String,Object> map = list.get(i);
			XSSFRow row = Sheet.createRow(i+1);//当前行
			
			XSSFCell cell = null;
			
			//第一列
			cell = row.createCell(0);
			cell.setCellValue(map.get("Index").toString());
			cell.setCellStyle(firstStyle);
			
			//第二列
			cell = row.createCell(1);
			cell.setCellValue(map.get("UName").toString());
			cell.setCellStyle(firstStyle);
			
			//第三列
			cell = row.createCell(2);
			cell.setCellValue(map.get("Pwd").toString());
			cell.setCellStyle(firstStyle);
			
			//第四列
			cell = row.createCell(3);
			cell.setCellValue(map.get("Birth").toString());
			cell.setCellStyle(firstStyle);
		}
		
		//将工作簿写入文件
		FileOutputStream fps = null;
		try {
			fps = new FileOutputStream(path);
			wb.write(fps);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(fps != null){
				try {
					fps.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	/**
	 * 根据原始的文件名生成新的文件名
	 * @param fileName 原始文件名
	 */
	public static String getNewFileName(String fileName){
		StringBuilder builder = new StringBuilder();
		List<String> list = new ArrayList<String>();
		StringTokenizer token = new StringTokenizer(fileName, ".");
		while(token.hasMoreTokens()){
			list.add(token.nextToken().toString());
		}
		builder.append(list.get(0));
		builder.append("_new.");
		builder.append(list.get(1));
		return builder.toString();
	}
	
	/**
	 * 根据文件名后缀判断文件的类型
	 * @param fileName 文件名
	 * @return true-HSSF,false-XSSF
	 */
	public static boolean isHSSForXSSF(String fileName){
		List<String> list = new ArrayList<String>();
		StringTokenizer token = new StringTokenizer(fileName, ".");
		while(token.hasMoreTokens()){
			list.add(token.nextToken().toString());
		}
		String suffix = list.get(1);//后缀名
		if("xls".equals(suffix)){
			return true;
		}else{
			return false;
		}
		
	}
	
}
