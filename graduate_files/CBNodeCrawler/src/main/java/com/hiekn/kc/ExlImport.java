package com.hiekn.kc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.alibaba.fastjson.JSONObject;
import com.hiekn.util.BufferedReaderUtil;
import com.hiekn.util.StringTimeUtils;

public class ExlImport {
	
	private static Map<String,Long> comIdMap = null;
	
	public static void main(String[] args) {
		comIdMap = getComIdMap();
		universityImport();
		hospitalImport();
	}
	
	public static void universityImport() {
		try {
			FileWriter fw = new FileWriter("data/kc/kc_result_uni.txt");
			Sheet uniSheet = getSheet("data/kc/uni.xls", "Sheet1");
			for (Row row : uniSheet) {
				if (row.getCell(0)!=null) {
					String uniName = row.getCell(0).toString().trim();
					if (comIdMap.containsKey(uniName)) {
						long comId = comIdMap.get(uniName);
						if (row.getCell(1)!=null) {
							JSONObject obj = new JSONObject();
							if (!row.getCell(1).toString().trim().equals("")) {
								obj.put("entity_id", comId);
								obj.put("attr_id", 1201);
								obj.put("attr_value", row.getCell(1).toString().trim());
								fw.write(obj.toJSONString() + "\r\n"); 
								fw.flush();
							}
						}
						if (row.getCell(2)!=null) {
							if (!row.getCell(2).toString().trim().equals("")) {
								JSONObject obj = new JSONObject();
								obj.put("entity_id", comId);
								obj.put("attr_id", 1202);
								obj.put("attr_value", row.getCell(2).toString().trim());
								fw.write(obj.toJSONString() + "\r\n"); 
								fw.flush();
							}
						}
						if (row.getCell(3)!=null) {
							if (!row.getCell(3).toString().trim().equals("")) {
								JSONObject obj = new JSONObject();
								obj.put("entity_id", comId);
								obj.put("attr_id", 1220);
								obj.put("attr_value", row.getCell(3).toString().trim());
								fw.write(obj.toJSONString() + "\r\n"); 
								fw.flush();
							}
						}
						if (row.getCell(5)!=null) {
							try {
								if (!row.getCell(5).toString().trim().equals("")) {
									JSONObject obj = new JSONObject();
									obj.put("entity_id", comId);
									obj.put("attr_id", 1203);
									obj.put("attr_value", StringTimeUtils.formatStringTime(row.getCell(5).toString().trim()).substring(0,4));
									fw.write(obj.toJSONString() + "\r\n"); 
									fw.flush();
								}
							} catch (Exception e) {
								System.out.println(row.getCell(5).toString().trim());
								e.printStackTrace();
							}
						}
						if (row.getCell(6)!=null) {
							if (!row.getCell(6).toString().trim().equals("")) {
								JSONObject obj = new JSONObject();
								obj.put("entity_id", comId);
								obj.put("attr_id", 1204);
								obj.put("attr_value", row.getCell(6).toString().trim());
								fw.write(obj.toJSONString() + "\r\n"); 
								fw.flush();
							}
						}
						if (row.getCell(7)!=null) {
							if (!row.getCell(7).toString().trim().equals("")) {
								JSONObject obj = new JSONObject();
								obj.put("entity_id", comId);
								obj.put("attr_id", 1205);
								obj.put("attr_value", row.getCell(7).toString().trim());
								fw.write(obj.toJSONString() + "\r\n"); 
								fw.flush();
							}
						}
						if (row.getCell(8)!=null) {
							if (!row.getCell(8).toString().trim().equals("")) {
								JSONObject obj = new JSONObject();
								obj.put("entity_id", comId);
								obj.put("attr_id", 1206);
								obj.put("attr_value", row.getCell(8).toString().trim());
								fw.write(obj.toJSONString() + "\r\n"); 
								fw.flush();
							}
						}
						if (row.getCell(9)!=null) {
							if (!row.getCell(9).toString().trim().equals("")) {
								JSONObject obj = new JSONObject();
								obj.put("entity_id", comId);
								obj.put("attr_id", 1207);
								obj.put("attr_value", row.getCell(9).toString().trim());
								fw.write(obj.toJSONString() + "\r\n"); 
								fw.flush();
							}
						}
						if (row.getCell(10)!=null) {
							if (!row.getCell(10).toString().trim().equals("")) {
								JSONObject obj = new JSONObject();
								obj.put("entity_id", comId);
								obj.put("attr_id", 1208);
								obj.put("attr_value", row.getCell(10).toString().trim());
								fw.write(obj.toJSONString() + "\r\n"); 
								fw.flush();
							}
						}
						if (row.getCell(11)!=null) {
							if (!row.getCell(11).toString().trim().equals("")) {
								JSONObject obj = new JSONObject();
								obj.put("entity_id", comId);
								obj.put("attr_id", 1209);
								obj.put("attr_value", row.getCell(11).toString().trim());
								fw.write(obj.toJSONString() + "\r\n"); 
								fw.flush();
							}
						}
						if (row.getCell(12)!=null) {
							if (!row.getCell(12).toString().trim().equals("")) {
								JSONObject obj = new JSONObject();
								obj.put("entity_id", comId);
								obj.put("attr_id", 1210);
								obj.put("attr_value", row.getCell(12).toString().trim().replace(".0", ""));
								fw.write(obj.toJSONString() + "\r\n"); 
								fw.flush();
							}
						}
						if (row.getCell(13)!=null) {
							if (!row.getCell(13).toString().trim().equals("")) {
								JSONObject obj = new JSONObject();
								obj.put("entity_id", comId);
								obj.put("attr_id", 1211);
								obj.put("attr_value", row.getCell(13).toString().trim().replace(".0", ""));
								fw.write(obj.toJSONString() + "\r\n"); 
								fw.flush();
							}
						}
						if (row.getCell(14)!=null) {
							if (!row.getCell(14).toString().trim().equals("")) {
								JSONObject obj = new JSONObject();
								obj.put("entity_id", comId);
								obj.put("attr_id", 1212);
								obj.put("attr_value", row.getCell(14).toString().trim().replace(".0", ""));
								fw.write(obj.toJSONString() + "\r\n"); 
								fw.flush();
							}
						}
						if (row.getCell(15)!=null) {
							if (!row.getCell(15).toString().trim().equals("")) {
								JSONObject obj = new JSONObject();
								obj.put("entity_id", comId);
								obj.put("attr_id", 1213);
								obj.put("attr_value", row.getCell(15).toString().trim().replace(".0", ""));
								fw.write(obj.toJSONString() + "\r\n"); 
								fw.flush();
							}
						}
						if (row.getCell(16)!=null) {
							if (!row.getCell(16).toString().trim().equals("")) {
								JSONObject obj = new JSONObject();
								obj.put("entity_id", comId);
								obj.put("attr_id", 1214);
								obj.put("attr_value", row.getCell(16).toString().trim().replace(".0", ""));
								fw.write(obj.toJSONString() + "\r\n"); 
								fw.flush();
							}
						}
						if (row.getCell(17)!=null) {
							if (!row.getCell(17).toString().trim().equals("")) {
								JSONObject obj = new JSONObject();
								obj.put("entity_id", comId);
								obj.put("attr_id", 1215);
								obj.put("attr_value", row.getCell(17).toString().trim().replace(".0", ""));
								fw.write(obj.toJSONString() + "\r\n"); 
								fw.flush();
							}
						}
						if (row.getCell(18)!=null) {
							if (!row.getCell(18).toString().trim().equals("")) {
								JSONObject obj = new JSONObject();
								obj.put("entity_id", comId);
								obj.put("attr_id", 1216);
								obj.put("attr_value", row.getCell(18).toString().trim());
								fw.write(obj.toJSONString() + "\r\n"); 
								fw.flush();
							}
						}
						if (row.getCell(19)!=null) {
							if (!row.getCell(19).toString().trim().equals("")) {
								JSONObject obj = new JSONObject();
								obj.put("entity_id", comId);
								obj.put("attr_id", 1217);
								obj.put("attr_value", row.getCell(19).toString().trim().replace(".0", ""));
								fw.write(obj.toJSONString() + "\r\n"); 
								fw.flush();
							}
						}
						if (row.getCell(20)!=null) {
							if (!row.getCell(20).toString().trim().equals("")) {
								JSONObject obj = new JSONObject();
								obj.put("entity_id", comId);
								obj.put("attr_id", 1218);
								obj.put("attr_value", row.getCell(20).toString().trim());
								fw.write(obj.toJSONString() + "\r\n"); 
								fw.flush();
							}
						}
					}
				}
			}
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void hospitalImport() {
		try {
			FileWriter fw = new FileWriter("data/kc/kc_result_hos.txt");
			Sheet hosSheet = getSheet("data/kc/hos.xls", "Sheet1");
			for (Row row : hosSheet) {
				if (row.getCell(0)!=null) {
					String hosName = row.getCell(0).toString().trim();
					if (comIdMap.containsKey(hosName)) {
						long comId = comIdMap.get(hosName);
						if (row.getCell(1)!=null) {
							if (!row.getCell(1).toString().trim().equals("")) {
								JSONObject obj = new JSONObject();
								obj.put("entity_id", comId);
								obj.put("attr_id", 110);
								obj.put("attr_value", row.getCell(1).toString().trim());
								fw.write(obj.toJSONString() + "\r\n"); 
								fw.flush();
							}
						}
						if (row.getCell(2)!=null) {
							if (!row.getCell(2).toString().trim().equals("")) {
								Cell cell = row.getCell(2);
								JSONObject obj = new JSONObject();
								obj.put("entity_id", comId);
								obj.put("attr_id", 111);
								if (row.getCell(2).toString().contains(".")) {
									System.out.println(cell);
								}
								obj.put("attr_value", row.getCell(2).toString().trim());
								fw.write(obj.toJSONString() + "\r\n"); 
								fw.flush();
							}
						}
						if (row.getCell(3)!=null) {
							if (!row.getCell(3).toString().trim().equals("")) {
								JSONObject obj = new JSONObject();
								obj.put("entity_id", comId);
								obj.put("attr_id", 131);
								obj.put("attr_value", row.getCell(3).toString().trim());
								fw.write(obj.toJSONString() + "\r\n"); 
								fw.flush();
							}
						}
						if (row.getCell(4)!=null) {
							if (!row.getCell(4).toString().trim().equals("")) {
								JSONObject obj = new JSONObject();
								obj.put("entity_id", comId);
								obj.put("attr_id", 104);
								obj.put("attr_value", row.getCell(4).toString().trim());
								fw.write(obj.toJSONString() + "\r\n"); 
								fw.flush();
							}
						}
						if (row.getCell(5)!=null) {
							if (!row.getCell(5).toString().trim().equals("")) {
								try {
									JSONObject obj = new JSONObject();
									obj.put("entity_id", comId); 
									obj.put("attr_id", 106);
									obj.put("attr_value", StringTimeUtils.formatStringTime(row.getCell(5).toString().trim()).substring(0,4));
									fw.write(obj.toJSONString() + "\r\n"); 
									fw.flush();
								} catch (Exception e) {
									System.out.println(row.getCell(5).toString().trim());
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Sheet getSheet(String path,String sheetName) {
		Workbook wb = null;
		try {
			wb = new HSSFWorkbook(new FileInputStream(new File(path)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Sheet sheet = wb.getSheet(sheetName);
		return sheet;
	}
	
	public static Map<String,Long> getComIdMap() {
		Map<String,Long> comIdMap = new HashMap<String,Long>();
		try {
			BufferedReader br = BufferedReaderUtil.getBuffer("data/kc/ent_all_id.txt");
			String input = "";
			while ((input = br.readLine())!=null) {
				String[] split = input.split("\t");
				String comName = split[0];
				Long id = Long.valueOf(split[1]);
				comIdMap.put(comName, id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return comIdMap;
	}
}
