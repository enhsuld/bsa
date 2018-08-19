package com.nbb.controllers;

import com.nbb.models.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.DocumentException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.nbb.dao.UserDao;
import com.nbb.services.FileUploadService;
import com.nbb.services.UserService;
import com.nbb.storage.StorageService;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@RestController
public class FileController {

	@Autowired
	StorageService storageService;
	
	@Autowired
	FileUploadService fileUploadService;
	
	@Autowired
	private UserDao dao;
	
	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	/*@GetMapping("/api/excel/delete/attach/{id}")
	@ResponseBody
	public boolean deleteFormFile(@PathVariable long id) {
		LnkAuditFormFile main = (LnkAuditFormFile) dao.getHQLResult("from LnkAuditFormFile t where t.id='"+id+"'", "current");
		Path currentRelativePath = Paths.get("");
		String realpath = currentRelativePath.toAbsolutePath().toString();
		File file = new File(realpath+File.separator+main.getFileurl());
		if(file.exists()){
			file.delete();
			dao.PeaceCrud(main, "LnkAuditFormFile", "delete", (long) id, 0, 0, null);
			return true;
		}
		else{
			return false;
		}
	}*/
	
	@PostMapping("/api/file/user/import")
	@ResponseBody
	public Boolean getFileUser(@RequestParam("file") MultipartFile file, Principal pr,HttpServletRequest req,HttpServletResponse response) throws EncryptedDocumentException, InvalidFormatException, IOException {
		//Path currentRelativePath = Paths.get("");
		//String realpath = currentRelativePath.toAbsolutePath().toString();
		
		InputStream str= file.getInputStream();
		Workbook workbook = WorkbookFactory.create(str); 
		Sheet sht=workbook.getSheet("Sheet1");
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		List<LutRole> rl= (List<LutRole>) dao.getHQLResult("from LutRole t where t.roleauth='nyabo'", "list");
		if(sht!=null){
			for(int y=0;y<sht.getLastRowNum();y++){
				Row row = sht.getRow(y);
				
				LutUser us =null;
				String username="";
				switch (evaluator.evaluateInCell(row.getCell(3)).getCellType()) 
				{
				case Cell.CELL_TYPE_STRING:
					username=row.getCell(3).getStringCellValue(); 
					break;
				case Cell.CELL_TYPE_NUMERIC:
					username=String.valueOf((int) row.getCell(3).getNumericCellValue());  
					break;
				}
				
				List<LutUser> usiz= (List<LutUser>) dao.getHQLResult("from LutUser t where t.username='"+username+"'", "list");
				if(usiz.size()==0){
					 us =new LutUser();
				}
				else{
					 us =usiz.get(0);
				}
				if(row.getCell(0)!=null){
					switch (evaluator.evaluateInCell(row.getCell(0)).getCellType()) 
					{
					case Cell.CELL_TYPE_STRING:
						us.setOrgName(row.getCell(0).getStringCellValue());   											
						break;
					case Cell.CELL_TYPE_NUMERIC:
						us.setOrgName(String.valueOf(row.getCell(0).getNumericCellValue()));   											
						break;
					}
				}
				
				if(row.getCell(1)!=null){
					switch (evaluator.evaluateInCell(row.getCell(1)).getCellType()) 
					{
					case Cell.CELL_TYPE_STRING:
						us.setOrgCode(row.getCell(1).getStringCellValue());   											
						break;
					case Cell.CELL_TYPE_NUMERIC:
						us.setOrgCode(String.valueOf(row.getCell(1).getNumericCellValue()));   											
						break;
					}
				}
				
				if(row.getCell(2)!=null){
					switch (evaluator.evaluateInCell(row.getCell(2)).getCellType()) 
					{
					case Cell.CELL_TYPE_STRING:
						us.setMobile(row.getCell(2).getStringCellValue());   											
						break;
					case Cell.CELL_TYPE_NUMERIC:
						us.setMobile(String.valueOf(row.getCell(2).getNumericCellValue()));   											
						break;
					}
				}
				if(row.getCell(3)!=null){
					switch (evaluator.evaluateInCell(row.getCell(3)).getCellType()) 
					{
					case Cell.CELL_TYPE_STRING:
						username=row.getCell(3).getStringCellValue();
						us.setUsername(row.getCell(3).getStringCellValue());   
						us.setPassword(passwordEncoder.encode(row.getCell(3).getStringCellValue()));   
						break;
					case Cell.CELL_TYPE_NUMERIC:
						username=String.valueOf((int) row.getCell(3).getNumericCellValue());
						us.setUsername(String.valueOf((int) row.getCell(3).getNumericCellValue()));  
						us.setPassword(passwordEncoder.encode(String.valueOf((int) row.getCell(3).getNumericCellValue())));   
						break;
					}
				}
				us.setIsactive(true);
				
				
				
				if(usiz.size()==0){
					dao.PeaceCrud(us, "LutUser", "save", (long) 0, 0, 0, null);
					LnkUserrole lnk=new LnkUserrole();
					lnk.setRoleid(rl.get(0).getId());
					lnk.setUserid(us.getId());
					dao.PeaceCrud(lnk, "LnkUserrole", "save", (long) 0, 0, 0, null);
				}
				else{
					dao.PeaceCrud(us, "LutUser", "update", (long) us.getId(), 0, 0, null);
					
					dao.PeaceCrud(null, "LnkUserrole", "delete", (long) us.getId(), 0, 0, "userid");	
					
					LnkUserrole lnk=new LnkUserrole();
					lnk.setRoleid(rl.get(0).getId());
					lnk.setUserid(us.getId());
					dao.PeaceCrud(lnk, "LnkUserrole", "save", (long) 0, 0, 0, null);
				}
			}
			return true;
		}
		return false;
		
	}
	
	@PostMapping("/api/checker")
	public String checker(@RequestParam("file") MultipartFile file,Principal pr,HttpServletRequest req) throws IllegalStateException, IOException,RuntimeException, NumberFormatException,ParseException, InvalidFormatException, JSONException {
				
			String SAVE_DIR = "upload-dir";
			String furl = File.separator + SAVE_DIR ;
			String filename = file.getOriginalFilename();
    		String newfilename = file.getOriginalFilename();
    		int newindex=newfilename.lastIndexOf('.');
    		String newlastOne=(newfilename.substring(newindex +1));
    	    String newuuid = UUID.randomUUID().toString()+"."+newlastOne;	
    	    //storageService.store(file,pr.getName(),newuuid);
			LutUser us=(LutUser) dao.getHQLResult("from LutUser t where t.username='"+pr.getName()+"'","current");
			Date d1 = new Date();
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/YYYY HH:mm a");
			String formattedDate = df.format(d1);

    	    
    	    if(FilenameUtils.getExtension(file.getOriginalFilename()).equalsIgnoreCase("xlsx") || FilenameUtils.getExtension(file.getOriginalFilename()).equalsIgnoreCase("xls")){

				InputStream stt= file.getInputStream();
				Workbook workbook = WorkbookFactory.create(stt);
				String incuid = UUID.randomUUID().toString()+".xlsx";

				File folder = new File("upload-dir"+File.separator+pr.getName());
				if(!folder.exists()){
					folder.mkdirs();
				}

				FileOutputStream incfout = new FileOutputStream("upload-dir"+File.separator+pr.getName()+ File.separator+incuid);
				workbook.write(incfout);
				incfout.close();

				FileConverted newFile = new FileConverted();
				newFile.setName(newfilename);
				newFile.setFsize(file.getSize()/1024);
				newFile.setFdate(formattedDate);
				newFile.setUserid(us.getId());
				newFile.setFlurl(incuid);
				dao.PeaceCrud(newFile, "FileConverted", "save", (long) 0, 0, 0, null);


            	
    			FileInputStream zagwar = null;
    			File files = null;
    			Path currentRelativePath = Paths.get("");
    			String realpath = currentRelativePath.toAbsolutePath().toString();
				FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

				List<LutValidation> tu=(List<LutValidation>) dao.getHQLResult("from LutValidation t where t.userid="+us.getId()+" order by t.id desc","list");

				List<StsCheckVariable> datas = new ArrayList<StsCheckVariable>();
				String str="";
				for(int k=0; k<tu.size();k++){
					StsCheckVariable sts=new StsCheckVariable();
					long a=0;
					long b=0;
					Sheet sheet = workbook.getSheetAt(0);
					Row row = sheet.createRow(k);
					if (tu.get(k).getIsformula1() == 1 && tu.get(k).getIsformula2() == 1) {
						try {
							Cell cell1 = row.createCell(0);
							cell1.setCellFormula(tu.get(k).getPosition1());
							CellValue cellValue1 = evaluator.evaluate(cell1);

							a = (long) cellValue1.getNumberValue();
							sts.setData6(a);

							Cell cell2 = row.createCell(1);
							cell2.setCellFormula(tu.get(k).getPosition2());
							CellValue cellValue2 = evaluator.evaluate(cell2);
							b = (long) cellValue2.getNumberValue();
							sts.setData9(b);


							sts.setData1(tu.get(k).getTitle1());
							sts.setData2(tu.get(k).getTitle2());
							sts.setData3(tu.get(k).getBalanceid());
							sts.setData4(tu.get(k).getCode1());
							sts.setData5(tu.get(k).getPosition1());
							sts.setData7(tu.get(k).getCode2());
							sts.setData8(tu.get(k).getPosition2());
							sts.setPlanid(newFile.getId());
							sts.setData10(sts.getData6() - sts.getData9());
							sts.setValid(tu.get(k).getValid());
							datas.add(sts);
						}
						catch (FormulaParseException e) {
							JSONObject errObject = new JSONObject();
							errObject.put("sheet", sheet.getSheetName());
							errObject.put("error", e.getMessage());
						}
						catch (RuntimeException e) {
							JSONObject errObject = new JSONObject();
							errObject.put("sheet", sheet.getSheetName());
							errObject.put("error", e.getMessage());
						}
					}

    			}
    			dao.inserBatch(datas,"tulgalt");
				return "true";
    		}


		return "false";
	}
	
	@PostMapping("/api/nyabo")
	public String nyaboFormUpload(@RequestParam("file") MultipartFile file,Principal pr,HttpServletRequest req) throws IllegalStateException, IOException, NumberFormatException,ParseException, InvalidFormatException, JSONException {
				
			String SAVE_DIR = "upload-dir";
			String furl = File.separator + SAVE_DIR ;
			String filename = file.getOriginalFilename();
    		String newfilename = file.getOriginalFilename();
    		int newindex=newfilename.lastIndexOf('.');
    		String newlastOne=(newfilename.substring(newindex +1));
    	    String newuuid = UUID.randomUUID().toString()+"."+newlastOne;	
    	    //storageService.store(file,pr.getName(),newuuid);
    	    
    	    if(FilenameUtils.getExtension(file.getOriginalFilename()).equalsIgnoreCase("xlsx") || FilenameUtils.getExtension(file.getOriginalFilename()).equalsIgnoreCase("xls")){

            	InputStream stt= file.getInputStream();
            	Workbook workbook = WorkbookFactory.create(stt); 
            	
    			FileInputStream zagwar = null;
    			File files = null;
    			Path currentRelativePath = Paths.get("");
    			String realpath = currentRelativePath.toAbsolutePath().toString();
    			
    			List<FileUpload> fl=(List<FileUpload>) dao.getHQLResult("from FileUpload t where t.autype=1 and t.aan=1 order by t.id desc", "list");
    		
    			
    			JSONObject arr= new JSONObject();
    			JSONObject err= new JSONObject();
    			int count=0;
    			if(fl.size()>0){
    				files = new File(realpath+fl.get(0).getFileurlAdmin());				
    				if(files.exists()){
    					zagwar = new FileInputStream(files);
    				}
    				else{
    					JSONObject robj=new JSONObject();
    					robj.put("support", true);
    		    		robj.put("excel", false);
    		    		robj.put("error", arr);
    		    		robj.put("file", false);
    		    		return robj.toString(); 
    				}
    			}
    			else{
    				JSONObject robj=new JSONObject();
    				robj.put("support", true);
    	    		robj.put("excel", false);
    	    		robj.put("error", arr);
    	    		robj.put("file", false);
    	    		return robj.toString(); 
    			}
    			
    			
    			Workbook zbook = WorkbookFactory.create(zagwar); 
    			JSONArray errList= new JSONArray();
    			JSONArray sheetList= new JSONArray();
    			for(int i=0;i<workbook.getNumberOfSheets()-6;i++){
    				Sheet sht=zbook.getSheet(workbook.getSheetName(i));
    				if(sht!=null){
    					Row drow = workbook.getSheetAt(i).getRow(6);
    					Row zrow = sht.getRow(6);
    					if(workbook.getSheetName(i).equalsIgnoreCase("15.Journal") || workbook.getSheetName(i).equalsIgnoreCase("Journal")){
    						drow = workbook.getSheetAt(i).getRow(3);
    						zrow = sht.getRow(3);
    					}
    					if(workbook.getSheetName(i).equalsIgnoreCase("16.Assets") || workbook.getSheetName(i).equalsIgnoreCase("Assets") 
    							|| workbook.getSheetName(i).equalsIgnoreCase("17.Inventory") || workbook.getSheetName(i).equalsIgnoreCase("19.Budget")){
    						drow = workbook.getSheetAt(i).getRow(4);
    						zrow = sht.getRow(4);
    					}
    					if(workbook.getSheetName(i).equalsIgnoreCase("18.Payroll") || workbook.getSheetName(i).equalsIgnoreCase("Payroll")){
    						drow = workbook.getSheetAt(i).getRow(1);
    						zrow = sht.getRow(1);						 
    					}
    					if(workbook.getSheetName(i).equalsIgnoreCase("12.CTT7") || workbook.getSheetName(i).equalsIgnoreCase("12.CTT7")){
    						drow = workbook.getSheetAt(i).getRow(7);
    						zrow = sht.getRow(7);
    					}
    					if(drow!=null){
    						for(int y=0;y<drow.getLastCellNum();y++){
    							Cell cl = drow.getCell(y);
    							if(zrow!=null){
    								Cell zcl = zrow.getCell(y);
    								if(cl!=null && zcl!=null){		
    									JSONObject errObj= new JSONObject();
    									if(workbook.getSheetName(i).equalsIgnoreCase("2.CT1A") || workbook.getSheetName(i).equalsIgnoreCase("CT1A") ||
    									   workbook.getSheetName(i).equalsIgnoreCase("3.CT2A") || workbook.getSheetName(i).equalsIgnoreCase("CT2A") ||
    									   workbook.getSheetName(i).equalsIgnoreCase("4.CT3A") || workbook.getSheetName(i).equalsIgnoreCase("CT3A") ||
    									   workbook.getSheetName(i).equalsIgnoreCase("5.CT4A") || workbook.getSheetName(i).equalsIgnoreCase("CT4A") ||
    									   workbook.getSheetName(i).equalsIgnoreCase("6.CTT1") || workbook.getSheetName(i).equalsIgnoreCase("7.CTT2") ||
    									   workbook.getSheetName(i).equalsIgnoreCase("8.CTT3") || workbook.getSheetName(i).equalsIgnoreCase("9.CTT4") ||
    									   workbook.getSheetName(i).equalsIgnoreCase("10.CTT5") || workbook.getSheetName(i).equalsIgnoreCase("11.CTT6") ||
    									   workbook.getSheetName(i).equalsIgnoreCase("15.Journal") || workbook.getSheetName(i).equalsIgnoreCase("Journal")){
    										if(!String.valueOf(cl.getRichStringCellValue().getString().trim()).equalsIgnoreCase(String.valueOf(zcl.getRichStringCellValue().getString().trim()))){									
    											errObj.put("sheetname", cl.getSheet().getSheetName());
    											errObj.put("bagana", cl.getRichStringCellValue().getString());
    											errObj.put("bagana2", zcl.getRichStringCellValue().getString());
    											errList.put(errObj);
    										}								
    									}
    								}
    							}												
    						}
    					}
    					else{
    						JSONObject errObj= new JSONObject();
    						errObj.put("sheetname", workbook.getSheetName(i));
    						sheetList.put(errObj);
    						
    					}
    				}
    				else{
    					JSONObject errObj= new JSONObject();
    					errObj.put("sheetname", workbook.getSheetName(i));
    					sheetList.put(errObj);
    				}
    			}
    			
    			
    			JSONArray arr1= new JSONArray();
    			FormulaEvaluator evaluator = zbook.getCreationHelper().createFormulaEvaluator();
    			
    			FormulaEvaluator wevaluator = workbook.getCreationHelper().createFormulaEvaluator();
    		
    	        JSONArray errMsg= new JSONArray();	
    			
    			if(sheetList.length()>0){
    				err.put("additionalSheet", sheetList);
    				err.put("excel", false);
    				err.put("support", false);				
    				return  err.toString();
    			}
    			else{
    				Sheet hch=zbook.getSheet("ЧХ");
    				if(hch!=null){
    					Row row4 = hch.getRow(4);
    					Row row5 = hch.getRow(5);
    					Row row6 = hch.getRow(6);
    					Row row7 = hch.getRow(7);
    					Row row8 = hch.getRow(8);
    					Row row12 = hch.getRow(12);
    					Row row13 = hch.getRow(13);
    					Row row14 = hch.getRow(14);
    					Row row15 = hch.getRow(15);
    					
    					Cell cell41 = row4.getCell(1);
    					Cell cell4 = row4.getCell(2);
    					Cell cell5 = row5.getCell(2);
    					Cell cell6 = row6.getCell(2);
    					Cell cell7 = row7.getCell(2);
    					Cell cell8 = row8.getCell(2);
    					Cell cell12 = row12.getCell(2);
    					Cell cell13 = row13.getCell(2);
    					Cell cell14 = row14.getCell(2);
    					Cell cell15 = row15.getCell(2);
    					
    				}
    				here: for(int i=0;i<workbook.getNumberOfSheets();i++){
    					//FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
    					Sheet sheet = zbook.getSheet(workbook.getSheetAt(i).getSheetName().trim());				
    					FormulaEvaluator evaluatorZbook = zbook.getCreationHelper().createFormulaEvaluator();
    					FormulaEvaluator eval= workbook.getCreationHelper().createFormulaEvaluator();
    					Sheet dataSheet = workbook.getSheetAt(i);
    					if(sheet!=null){
    						System.out.println("sheetname"+sheet.getSheetName());
    						
    						if(sheet.getSheetName().equalsIgnoreCase("23.TRIAL BALANCE")){    									
    							for(int k=5; k <= sheet.getLastRowNum();k++){
    								Row row = dataSheet.getRow(k);
    								Row crow = sheet.getRow(k);
    								if(row!=null){
    									Cell codeCell = dataSheet.getRow(k).getCell(0);
    									try {
    										if(row.getCell(0)!=null){
            									String str="";
        										switch (eval.evaluateInCell(row.getCell(0)).getCellType()) 
        										{
        										case Cell.CELL_TYPE_STRING:
        											str=row.getCell(0).getStringCellValue();
        											break;
        										case Cell.CELL_TYPE_NUMERIC:
        											str=String.valueOf(row.getCell(0).getNumericCellValue());
        											break;
        										}
        										if(str.length()>0){
        											String formula="value("+str+")";
        											codeCell.setCellFormula(formula);  
        											CellValue cellValue = eval.evaluate(codeCell);    	
        										}											
        									}
    									}
    									catch (FormulaParseException e) {
    										JSONObject errObject = new JSONObject();
    										errObject.put("sheet", sheet.getSheetName());
    										errObject.put("error", e.getMessage());
									    }
    									for(int y=0;y<10;y++){		
    										Cell ss = dataSheet.getRow(0).createCell(100+y);
    										int cc=y+3;
    										
    										String formula="VLOOKUP("+crow.getCell(0).getNumericCellValue()+",A6:L600,"+cc+",FALSE)";
    										ss.setCellFormula(formula);    										
    										Cell zcell =crow.getCell(cc-1);
    										CellValue cellValue = eval.evaluate(ss);    	
    										if(cellValue.getNumberValue()!=0){
    											zcell.setCellValue(cellValue.getNumberValue());    	 
    										}    										
    									}
    								}    								
    							}
    						}
    						if(sheet.getSheetName().equalsIgnoreCase("2.CT1A")){    									
    							for(int k=7; k <= sheet.getLastRowNum();k++){
    								Row row = dataSheet.getRow(k);
    								Row crow = sheet.getRow(k);
    								
    								
    								if(row!=null){
    									if(row.getCell(0)!=null){
        									Cell codeCell = dataSheet.getRow(k).getCell(0);
        									try {
        										if(row.getCell(0)!=null){
                									String str="";
            										switch (eval.evaluateInCell(row.getCell(0)).getCellType()) 
            										{
            										case Cell.CELL_TYPE_STRING:
            											str=row.getCell(0).getStringCellValue();
            											break;
            										case Cell.CELL_TYPE_NUMERIC:
            											str=String.valueOf(row.getCell(0).getNumericCellValue());
            											break;
            										}
            										if(str.length()>0){
            											String formula="value("+str+")";
            											codeCell.setCellFormula(formula);  
            											CellValue cellValue = eval.evaluate(codeCell);    	
            										}											
            									}
        									}
        									catch (FormulaParseException e) {
        										JSONObject errObject = new JSONObject();
        										errObject.put("sheet", sheet.getSheetName());
        										errObject.put("error", e.getMessage());
        										errMsg.put(errObject);
    									    }											
    									}
    								}
    								
    								if(row!=null){
    									for(int y=0;y<3;y++){		
    										Cell ss = dataSheet.getRow(0).createCell(100+y);
    										int cc=y+3;
    										String str="";
    										if(row.getCell(0)!=null){
    											switch (evaluator.evaluateInCell(crow.getCell(0)).getCellType()) 
    											{
    											case Cell.CELL_TYPE_STRING:
    												str=crow.getCell(0).getStringCellValue();
    												break;
    											case Cell.CELL_TYPE_NUMERIC:
    												str=String.valueOf(crow.getCell(0).getNumericCellValue());
    												break;
    											}
    											if(str.length()>0){
    												String formula="VLOOKUP("+str+",A8:D300,"+cc+",FALSE)";
    												ss.setCellFormula(formula);    										
    												Cell zcell =crow.getCell(cc-1);
    												CellValue cellValue = eval.evaluate(ss);    	
    												if(cellValue.getNumberValue()!=0){
    													zcell.setCellValue(cellValue.getNumberValue());    	 
    												} 
    											}											
    										}										   										
    									}
    								}									
    							}
    						}
    						if(sheet.getSheetName().equalsIgnoreCase("3.CT2A")){    									
    							for(int k=7; k <= sheet.getLastRowNum();k++){
    								Row row = dataSheet.getRow(k);
    								Row crow = sheet.getRow(k);
    								if(row!=null){
    									Cell codeCell = dataSheet.getRow(k).getCell(0);
    									try {
    										if(row.getCell(0)!=null){
            									String str="";
        										switch (eval.evaluateInCell(row.getCell(0)).getCellType()) 
        										{
        										case Cell.CELL_TYPE_STRING:
        											str=row.getCell(0).getStringCellValue();
        											break;
        										case Cell.CELL_TYPE_NUMERIC:
        											str=String.valueOf(row.getCell(0).getNumericCellValue());
        											break;
        										}
        										if(str.length()>0){
        											String formula="value("+str+")";
        											codeCell.setCellFormula(formula);  
        											CellValue cellValue = eval.evaluate(codeCell);    	
        										}											
        									}
    									}
    									catch (FormulaParseException e) {
    										JSONObject errObject = new JSONObject();
    										errObject.put("sheet", sheet.getSheetName());
    										errObject.put("error", e.getMessage());
    										errMsg.put(errObject);
									    }
    									for(int y=0;y<3;y++){		
    										Cell ss = dataSheet.getRow(0).createCell(100+y);
    										int cc=y+3;
    										String str="";
    										if(row.getCell(0)!=null){
    											switch (evaluator.evaluateInCell(crow.getCell(0)).getCellType()) 
    											{
    											case Cell.CELL_TYPE_STRING:
    												str=crow.getCell(0).getStringCellValue();
    												break;
    											case Cell.CELL_TYPE_NUMERIC:
    												str=String.valueOf(crow.getCell(0).getNumericCellValue());
    												break;
    											}
    											if(str.length()>0){
    												String formula="VLOOKUP("+str+",A8:D300,"+cc+",FALSE)";
    												ss.setCellFormula(formula);    										
    												Cell zcell =crow.getCell(cc-1);
    												CellValue cellValue = eval.evaluate(ss);    	
    												if(cellValue.getNumberValue()!=0){
    													zcell.setCellValue(cellValue.getNumberValue());    	 
    												} 
    											}											
    										}										   										
    									}
    								}    								
    							}
    						}
    						if(sheet.getSheetName().equalsIgnoreCase("4.CT3A")){    									
    							for(int k=7; k <= sheet.getLastRowNum();k++){
    								Row row = dataSheet.getRow(k);
    								Row crow = sheet.getRow(k);
    								if(row!=null){
    									Cell codeCell = dataSheet.getRow(k).getCell(0);
    									try {
    										if(row.getCell(0)!=null){
            									String str="";
        										switch (eval.evaluateInCell(row.getCell(0)).getCellType()) 
        										{
        										case Cell.CELL_TYPE_STRING:
        											str=row.getCell(0).getStringCellValue();
        											break;
        										case Cell.CELL_TYPE_NUMERIC:
        											str=String.valueOf(row.getCell(0).getNumericCellValue());
        											break;
        										}
        										if(str.length()>0){
        											String formula="value("+str+")";
        											codeCell.setCellFormula(formula);  
        											CellValue cellValue = eval.evaluate(codeCell);    	
        										}											
        									}
    									}
    									catch (FormulaParseException e) {
    										JSONObject errObject = new JSONObject();
    										errObject.put("sheet", sheet.getSheetName());
    										errObject.put("error", e.getMessage());
    										errMsg.put(errObject);
									    }
    									for(int y=0;y<3;y++){		
    										Cell ss = dataSheet.getRow(0).createCell(100+y);
    										int cc=y+3;
    										String str="";
    										if(row.getCell(0)!=null){
    											switch (evaluator.evaluateInCell(crow.getCell(0)).getCellType()) 
    											{
    											case Cell.CELL_TYPE_STRING:
    												str=crow.getCell(0).getStringCellValue();
    												break;
    											case Cell.CELL_TYPE_NUMERIC:
    												str=String.valueOf(crow.getCell(0).getNumericCellValue());
    												break;
    											}
    											if(str.length()>0){
    												String formula="VLOOKUP("+str+",A8:D350,"+cc+",FALSE)";
    												ss.setCellFormula(formula);    										
    												Cell zcell =crow.getCell(cc-1);
    												CellValue cellValue = eval.evaluate(ss);    	
    												if(cellValue.getNumberValue()!=0){
    													zcell.setCellValue(cellValue.getNumberValue());    	 
    												} 
    											}											
    										}										   										
    									}
    								}    								
    							}
    						}
    						if(sheet.getSheetName().equalsIgnoreCase("5.CT4A")){    									
    							for(int k=7; k <= sheet.getLastRowNum();k++){
    								Row row = dataSheet.getRow(k);
    								Row crow= sheet.getRow(k);
    								if(row!=null){
    									Cell codeCell = dataSheet.getRow(k).getCell(0);
    									if(codeCell!=null){
    										switch (evaluator.evaluateInCell(codeCell).getCellType()) 
											{
											case Cell.CELL_TYPE_STRING:
												if(codeCell.getStringCellValue().equalsIgnoreCase("C01")){
	        										crow= sheet.getRow(7);
	        									}
	        									if(codeCell.getStringCellValue().equalsIgnoreCase("C02")){
	        										crow= sheet.getRow(8);
	        									}
	        									if(codeCell.getStringCellValue().equalsIgnoreCase("C03")){
	        										crow= sheet.getRow(9);
	        									}
	        									if(codeCell.getStringCellValue().equalsIgnoreCase("C04")){
	        										crow= sheet.getRow(10);
	        									}
	        									if(codeCell.getStringCellValue().equalsIgnoreCase("C05")){
	        										crow= sheet.getRow(11);
	        									}
	        									if(codeCell.getStringCellValue().equalsIgnoreCase("C06")){
	        										crow= sheet.getRow(12);
	        									}
	        									if(codeCell.getStringCellValue().equalsIgnoreCase("C07")){
	        										crow= sheet.getRow(13);
	        									}
	        									if(codeCell.getStringCellValue().equalsIgnoreCase("C08")){
	        										crow= sheet.getRow(14);
	        									}
	        									if(codeCell.getStringCellValue().equalsIgnoreCase("D01")){
	        										crow= sheet.getRow(15);
	        									}
	        									if(codeCell.getStringCellValue().equalsIgnoreCase("D02")){
	        										crow= sheet.getRow(16);
	        									}
	        									if(codeCell.getStringCellValue().equalsIgnoreCase("D03")){
	        										crow= sheet.getRow(17);
	        									}
	        									if(codeCell.getStringCellValue().equalsIgnoreCase("D04")){
	        										crow= sheet.getRow(18);
	        									}
	        									if(codeCell.getStringCellValue().equalsIgnoreCase("D05")){
	        										crow= sheet.getRow(19);
	        									}
	        									if(codeCell.getStringCellValue().equalsIgnoreCase("D06")){
	        										crow= sheet.getRow(20);
	        									}
	        									if(codeCell.getStringCellValue().equalsIgnoreCase("D07")){
	        										crow= sheet.getRow(21);
	        									}
	        									if(codeCell.getStringCellValue().equalsIgnoreCase("D08")){
	        										crow= sheet.getRow(22);
	        									}
	        									if(codeCell.getStringCellValue().equalsIgnoreCase("D09")){
	        										crow= sheet.getRow(23);
	        									}
												break;        									
											}
    									}
        								/*if(row.getCell(0)!=null){
        									String str="";
    										switch (eval.evaluateInCell(row.getCell(0)).getCellType()) 
    										{
    										case Cell.CELL_TYPE_STRING:
    											str=row.getCell(0).getStringCellValue();
    											break;
    										case Cell.CELL_TYPE_NUMERIC:
    											str=String.valueOf(row.getCell(0).getNumericCellValue());
    											break;
    										}
    										if(str.length()>0){
    											String formula="value("+str+")";
    											codeCell.setCellFormula(formula);  
    											CellValue cellValue = eval.evaluate(codeCell);    	
    										}											
    									}*/
    									for(int y=0;y<4;y++){		
    										Cell ss = dataSheet.getRow(0).createCell(100+y);
    										int cc=y+3;
    										String str="";
    										if(row.getCell(0)!=null){
    											switch (evaluator.evaluateInCell(crow.getCell(0)).getCellType()) 
    											{
    											case Cell.CELL_TYPE_STRING:
    												str=crow.getCell(0).getStringCellValue();
    												break;
    											case Cell.CELL_TYPE_NUMERIC:
    												str=String.valueOf(crow.getCell(0).getNumericCellValue());
    												break;
    											}
    											System.out.println("@ : "+str);
    											int r=k+1;
    											if(str.length()>0){
    												String formula="VLOOKUP(A"+r+",A8:G30,"+cc+",FALSE)";
    												ss.setCellFormula(formula);    	
    												System.out.println("#"+formula);
    												Cell zcell =crow.getCell(cc-1);
    												CellValue cellValue = eval.evaluate(ss);    	
    												if(cellValue.getNumberValue()!=0){
    													zcell.setCellValue(cellValue.getNumberValue());    	 
    												} 
    											}											
    										}										   										
    									}
    								}    								
    							}
    						}
    						if(sheet.getSheetName().equalsIgnoreCase("6.CTT1")){    									
    							for(int k=7; k <= sheet.getLastRowNum();k++){
    								Row row = dataSheet.getRow(k);
    								Row crow = sheet.getRow(k);
    								if(row!=null){
    									if(row.getCell(0)!=null){
        									Cell codeCell = dataSheet.getRow(k).getCell(0);
        									try {
        										if(row.getCell(0)!=null){
                									String str="";
            										switch (eval.evaluateInCell(row.getCell(0)).getCellType()) 
            										{
            										case Cell.CELL_TYPE_STRING:
            											str=row.getCell(0).getStringCellValue();
            											break;
            										case Cell.CELL_TYPE_NUMERIC:
            											str=String.valueOf(row.getCell(0).getNumericCellValue());
            											break;
            										}
            										if(str.length()>0){
            											String formula="value("+str+")";
            											codeCell.setCellFormula(formula);  
            											CellValue cellValue = eval.evaluate(codeCell);    	
            										}											
            									}
        									}
        									catch (FormulaParseException e) {
        										JSONObject errObject = new JSONObject();
        										errObject.put("sheet", sheet.getSheetName());
        										errObject.put("error", e.getMessage());
        										errMsg.put(errObject);
    									    }										
    									}
    									if(crow!=null){
    										for(int y=0;y<6;y++){		
        										Cell ss = dataSheet.getRow(0).createCell(100+y);
        										int cc=y+3;
        										String str="";
        										if(row.getCell(0)!=null && crow.getCell(0)!=null){
        											switch (evaluator.evaluateInCell(crow.getCell(0)).getCellType()) 
        											{
        											case Cell.CELL_TYPE_STRING:
        												str=crow.getCell(0).getStringCellValue();
        												break;
        											case Cell.CELL_TYPE_NUMERIC:
        												str=String.valueOf(crow.getCell(0).getNumericCellValue());
        												break;
        											}
        											if(str.length()>0){
        												String formula="VLOOKUP("+str+",A8:F30,"+cc+",FALSE)";
        												ss.setCellFormula(formula);    										
        												Cell zcell =crow.getCell(cc-1);
        												CellValue cellValue = eval.evaluate(ss);    	
        												if(cellValue.getNumberValue()!=0){
        													zcell.setCellValue(cellValue.getNumberValue());    	 
        												} 
        											}											
        										}										   										
        									}
    									}    								
    								}									
    							}
    						}
    						if(sheet.getSheetName().equalsIgnoreCase("7.CTT2")){    									
    							for(int k=7; k <= sheet.getLastRowNum();k++){
    								Row row = dataSheet.getRow(k);
    								Row crow = sheet.getRow(k);    								
    								if(row!=null){
    									Cell codeCell = dataSheet.getRow(k).getCell(0);
    									try {
    										if(row.getCell(0)!=null){
            									String str="";
        										switch (eval.evaluateInCell(row.getCell(0)).getCellType()) 
        										{
        										case Cell.CELL_TYPE_STRING:
        											str=row.getCell(0).getStringCellValue();
        											break;
        										case Cell.CELL_TYPE_NUMERIC:
        											str=String.valueOf(row.getCell(0).getNumericCellValue());
        											break;
        										}
        										if(str.length()>0){
        											String formula="value("+str+")";
        											codeCell.setCellFormula(formula);  
        											CellValue cellValue = eval.evaluate(codeCell);    	
        										}											
        									}
    									}
    									catch (FormulaParseException e) {
    										JSONObject errObject = new JSONObject();
    										errObject.put("sheet", sheet.getSheetName());
    										errObject.put("error", e.getMessage());
    										errMsg.put(errObject);
									    }
    									for(int y=0;y<6;y++){		
    										Cell ss = dataSheet.getRow(0).createCell(100+y);
    										int cc=y+3;
    										String str="";
    										if(row.getCell(0)!=null){
    											switch (evaluator.evaluateInCell(crow.getCell(0)).getCellType()) 
    											{
    											case Cell.CELL_TYPE_STRING:
    												str=crow.getCell(0).getStringCellValue();
    												break;
    											case Cell.CELL_TYPE_NUMERIC:
    												str=String.valueOf(crow.getCell(0).getNumericCellValue());
    												break;
    											}
    											if(str.length()>0){
    												String formula="VLOOKUP("+str+",A8:F12,"+cc+",FALSE)";
    												ss.setCellFormula(formula);    										
    												Cell zcell =crow.getCell(cc-1);
    												CellValue cellValue = eval.evaluate(ss);    	
    												if(cellValue.getNumberValue()!=0){
    													zcell.setCellValue(cellValue.getNumberValue());    	 
    												} 
    											}											
    										}										   										
    									}
    								}									
    							}
    						}
    						if(sheet.getSheetName().equalsIgnoreCase("8.CTT3")){    									
    							for(int k=7; k <= sheet.getLastRowNum();k++){
    								Row row = dataSheet.getRow(k);
    								Row crow = sheet.getRow(k);    								
    								if(row!=null){
    									Cell codeCell = dataSheet.getRow(k).getCell(0);
    									try {
    										if(row.getCell(0)!=null){
            									String str="";
        										switch (eval.evaluateInCell(row.getCell(0)).getCellType()) 
        										{
        										case Cell.CELL_TYPE_STRING:
        											str=row.getCell(0).getStringCellValue();
        											break;
        										case Cell.CELL_TYPE_NUMERIC:
        											str=String.valueOf(row.getCell(0).getNumericCellValue());
        											break;
        										}
        										if(str.length()>0){
        											String formula="value("+str+")";
        											codeCell.setCellFormula(formula);  
        											CellValue cellValue = eval.evaluate(codeCell);    	
        										}											
        									}
    									}
    									catch (FormulaParseException e) {
    										JSONObject errObject = new JSONObject();
    										errObject.put("sheet", sheet.getSheetName());
    										errObject.put("error", e.getMessage());
    										errMsg.put(errObject);
									    }
        								if(crow!=null){
        									for(int y=0;y<6;y++){		
        										Cell ss = dataSheet.getRow(0).createCell(100+y);
        										int cc=y+3;
        										String str="";
        										if(row.getCell(0)!=null && crow.getCell(0)!=null){
        											switch (evaluator.evaluateInCell(crow.getCell(0)).getCellType()) 
        											{
        											case Cell.CELL_TYPE_STRING:
        												str=crow.getCell(0).getStringCellValue();
        												break;
        											case Cell.CELL_TYPE_NUMERIC:
        												str=String.valueOf(crow.getCell(0).getNumericCellValue());
        												break;
        											}
        											if(str.length()>0){
        												String formula="VLOOKUP("+str+",A8:F45,"+cc+",FALSE)";
        												ss.setCellFormula(formula);    										
        												Cell zcell =crow.getCell(cc-1);
        												CellValue cellValue = eval.evaluate(ss);    	
        												if(cellValue.getNumberValue()!=0){
        													zcell.setCellValue(cellValue.getNumberValue());    	 
        												} 
        											}											
        										}										   										
        									}
        								}
    									
    								}									
    							}
    						}
    						if(sheet.getSheetName().equalsIgnoreCase("9.CTT4")){    									
    							for(int k=7; k <= sheet.getLastRowNum();k++){
    								Row row = dataSheet.getRow(k);
    								Row crow = sheet.getRow(k);
    							
    								if(row!=null){
    									Cell codeCell = dataSheet.getRow(k).getCell(0);
    									try {
    										if(row.getCell(0)!=null){
            									String str="";
        										switch (eval.evaluateInCell(row.getCell(0)).getCellType()) 
        										{
        										case Cell.CELL_TYPE_STRING:
        											str=row.getCell(0).getStringCellValue();
        											break;
        										case Cell.CELL_TYPE_NUMERIC:
        											str=String.valueOf(row.getCell(0).getNumericCellValue());
        											break;
        										}
        										if(str.length()>0){
        											String formula="value("+str+")";
        											codeCell.setCellFormula(formula);  
        											CellValue cellValue = eval.evaluate(codeCell);    	
        										}											
        									}
    									}
    									catch (FormulaParseException e) {
    										JSONObject errObject = new JSONObject();
    										errObject.put("sheet", sheet.getSheetName());
    										errObject.put("error", e.getMessage());
    										errMsg.put(errObject);
									    }
    									for(int y=0;y<6;y++){		
    										Cell ss = dataSheet.getRow(0).createCell(100+y);
    										int cc=y+3;
    										String str="";
    										if(row.getCell(0)!=null){
    											switch (evaluator.evaluateInCell(crow.getCell(0)).getCellType()) 
    											{
    											case Cell.CELL_TYPE_STRING:
    												str=crow.getCell(0).getStringCellValue();
    												break;
    											case Cell.CELL_TYPE_NUMERIC:
    												str=String.valueOf(crow.getCell(0).getNumericCellValue());
    												break;
    											}
    											if(str.length()>0){
    												String formula="VLOOKUP("+str+",A8:F20,"+cc+",FALSE)";
    												ss.setCellFormula(formula);    										
    												Cell zcell =crow.getCell(cc-1);
    												CellValue cellValue = eval.evaluate(ss);    	
    												if(cellValue.getNumberValue()!=0){
    													zcell.setCellValue(cellValue.getNumberValue());    	 
    												} 
    											}											
    										}										   										
    									}
    								}									
    							}
    						}
    						if(sheet.getSheetName().equalsIgnoreCase("10.CTT5")){    									
    							for(int k=7; k <= sheet.getLastRowNum();k++){
    								Row row = dataSheet.getRow(k);
    								Row crow = sheet.getRow(k);
    							
    								if(row!=null){
    									Cell codeCell = dataSheet.getRow(k).getCell(0);
    									try {
    										if(row.getCell(0)!=null){
            									String str="";
        										switch (eval.evaluateInCell(row.getCell(0)).getCellType()) 
        										{
        										case Cell.CELL_TYPE_STRING:
        											str=row.getCell(0).getStringCellValue();
        											break;
        										case Cell.CELL_TYPE_NUMERIC:
        											str=String.valueOf(row.getCell(0).getNumericCellValue());
        											break;
        										}
        										if(str.length()>0){
        											String formula="value("+str+")";
        											codeCell.setCellFormula(formula);  
        											CellValue cellValue = eval.evaluate(codeCell);    	
        										}											
        									}
    									}
    									catch (FormulaParseException e) {
    										JSONObject errObject = new JSONObject();
    										errObject.put("sheet", sheet.getSheetName());
    										errObject.put("error", e.getMessage());
    										errMsg.put(errObject);
									    }
    									for(int y=0;y<14;y++){		
    										Cell ss = dataSheet.getRow(0).createCell(100+y);
    										int cc=y+3;
    										String str="";
    										if(row.getCell(0)!=null){
    											switch (evaluator.evaluateInCell(crow.getCell(0)).getCellType()) 
    											{
    											case Cell.CELL_TYPE_STRING:
    												str=crow.getCell(0).getStringCellValue();
    												break;
    											case Cell.CELL_TYPE_NUMERIC:
    												str=String.valueOf(crow.getCell(0).getNumericCellValue());
    												break;
    											}
    											if(str.length()>0){
    												String formula="VLOOKUP("+str+",A8:Q25,"+cc+",FALSE)";
    												ss.setCellFormula(formula);    										
    												Cell zcell =crow.getCell(cc-1);
    												CellValue cellValue = eval.evaluate(ss);    	
    												if(cellValue.getNumberValue()!=0){
    													zcell.setCellValue(cellValue.getNumberValue());    	 
    												} 
    											}											
    										}										   										
    									}
    								}									
    							}
    						}
    						if(sheet.getSheetName().equalsIgnoreCase("11.CTT6")){    									
    							for(int k=7; k <= sheet.getLastRowNum();k++){
    								Row row = dataSheet.getRow(k);
    								Row crow = sheet.getRow(k);
    								
    								if(row!=null){
    									Cell codeCell = dataSheet.getRow(k).getCell(0);
    									try {
    										if(row.getCell(0)!=null){
            									String str="";
        										switch (eval.evaluateInCell(row.getCell(0)).getCellType()) 
        										{
        										case Cell.CELL_TYPE_STRING:
        											str=row.getCell(0).getStringCellValue();
        											break;
        										case Cell.CELL_TYPE_NUMERIC:
        											str=String.valueOf(row.getCell(0).getNumericCellValue());
        											break;
        										}
        										if(str.length()>0){
        											String formula="value("+str+")";
        											codeCell.setCellFormula(formula);  
        											CellValue cellValue = eval.evaluate(codeCell);    	
        										}											
        									}
    									}
    									catch (FormulaParseException e) {
    										JSONObject errObject = new JSONObject();
    										errObject.put("sheet", sheet.getSheetName());
    										errObject.put("error", e.getMessage());
    										errMsg.put(errObject);
									    }
    									for(int y=0;y<6;y++){		
    										Cell ss = dataSheet.getRow(0).createCell(100+y);
    										int cc=y+3;
    										String str="";
    										if(row.getCell(0)!=null){
    											switch (evaluator.evaluateInCell(crow.getCell(0)).getCellType()) 
    											{
    											case Cell.CELL_TYPE_STRING:
    												str=crow.getCell(0).getStringCellValue();
    												break;
    											case Cell.CELL_TYPE_NUMERIC:
    												str=String.valueOf(crow.getCell(0).getNumericCellValue());
    												break;
    											}
    											if(str.length()>0){
    												String formula="VLOOKUP("+str+",A8:F30,"+cc+",FALSE)";
    												ss.setCellFormula(formula);    										
    												Cell zcell =crow.getCell(cc-1);
    												CellValue cellValue = eval.evaluate(ss);    	
    												if(cellValue.getNumberValue()!=0){
    													zcell.setCellValue(cellValue.getNumberValue());    	 
    												} 
    											}											
    										}										   										
    									}
    								}									
    							}
    						}
    						if(sheet.getSheetName().equalsIgnoreCase("12.CTT7")){    									
    							for(int k=9; k <= sheet.getLastRowNum();k++){
    								Row row = dataSheet.getRow(k);
    								Row crow = sheet.getRow(k);    							
    								if(row!=null){
    									
    									Cell codeCell = dataSheet.getRow(k).getCell(0);
    									try {
    										if(row.getCell(0)!=null){
            									String str="";
        										switch (eval.evaluateInCell(row.getCell(0)).getCellType()) 
        										{
        										case Cell.CELL_TYPE_STRING:
        											str=row.getCell(0).getStringCellValue();
        											break;
        										case Cell.CELL_TYPE_NUMERIC:
        											str=String.valueOf(row.getCell(0).getNumericCellValue());
        											break;
        										}
        										if(str.length()>0){
        											String formula="value("+str+")";
        											codeCell.setCellFormula(formula);  
        											CellValue cellValue = eval.evaluate(codeCell);    	
        										}											
        									}
    									}
    									catch (FormulaParseException e) {
    										JSONObject errObject = new JSONObject();
    										errObject.put("sheet", sheet.getSheetName());
    										errObject.put("error", e.getMessage());
    										errMsg.put(errObject);
									    }
										for(int y=0;y<13;y++){		
    										Cell ss = dataSheet.getRow(k).createCell(100+y);
    										int cc=y+3;
    										String str="";
    										if(row.getCell(0)!=null){
    											switch (evaluator.evaluateInCell(crow.getCell(0)).getCellType()) 
    											{
    											case Cell.CELL_TYPE_STRING:
    												str=crow.getCell(0).getStringCellValue();
    												break;
    											case Cell.CELL_TYPE_NUMERIC:
    												str=String.valueOf(crow.getCell(0).getNumericCellValue());
    												break;
    											}
    											if(str.length()>0){
    												String formula="VLOOKUP("+str+",A8:P40,"+cc+",FALSE)";
    												ss.setCellFormula(formula);    										
    												Cell zcell =crow.getCell(cc-1);
    												CellValue cellValue = eval.evaluate(ss);    	
    												if(cellValue.getNumberValue()!=0){
    													zcell.setCellValue(cellValue.getNumberValue());    	 
    												} 
    											}											
    										}										   										
    									}   									
    								}									
    							}
    						}
    						if(sheet.getSheetName().equalsIgnoreCase("13.CTT8")){    									
    							for(int k=7; k <= sheet.getLastRowNum();k++){
    								Row row = dataSheet.getRow(k);
    								Row crow = sheet.getRow(k);    							
    								if(row!=null && crow!=null){
    									Cell codeCell = dataSheet.getRow(k).getCell(0);
    									try {
    										if(row.getCell(0)!=null){
            									String str="";
        										switch (eval.evaluateInCell(row.getCell(0)).getCellType()) 
        										{
        										case Cell.CELL_TYPE_STRING:
        											str=row.getCell(0).getStringCellValue();
        											break;
        										case Cell.CELL_TYPE_NUMERIC:
        											str=String.valueOf(row.getCell(0).getNumericCellValue());
        											break;
        										}
        										if(str.length()>0){
        											String formula="value("+str+")";
        											codeCell.setCellFormula(formula);  
        											CellValue cellValue = eval.evaluate(codeCell);    	
        										}											
        									}
    									}
    									catch (FormulaParseException e) {
    										JSONObject errObject = new JSONObject();
    										errObject.put("sheet", sheet.getSheetName());
    										errObject.put("error", e.getMessage());
    										errMsg.put(errObject);
									    }
        								
    									for(int y=0;y<6;y++){		
    										Cell ss = dataSheet.getRow(0).createCell(100+y);
    										int cc=y+3;
    										String str="";
    										if(row.getCell(0)!=null && crow.getCell(0)!=null){
    											switch (evaluator.evaluateInCell(crow.getCell(0)).getCellType()) 
    											{
    											case Cell.CELL_TYPE_STRING:
    												str=crow.getCell(0).getStringCellValue();
    												break;
    											case Cell.CELL_TYPE_NUMERIC:
    												str=String.valueOf(crow.getCell(0).getNumericCellValue());
    												break;
    											}
    											if(str.length()>0){
    												String formula="VLOOKUP("+str+",A8:F70,"+cc+",FALSE)";
    												ss.setCellFormula(formula);    										
    												Cell zcell =crow.getCell(cc-1);
    												CellValue cellValue = eval.evaluate(ss);    	
    												if(cellValue.getNumberValue()!=0){
    													zcell.setCellValue(cellValue.getNumberValue());    	 
    												} 
    											}											
    										}										   										
    									}
    								}									
    							}
    						}
    						if(sheet.getSheetName().equalsIgnoreCase("14.CTT9")){    									
    							for(int k=7; k <= sheet.getLastRowNum();k++){
    								Row row = dataSheet.getRow(k);
    								Row crow = sheet.getRow(k);    							
    								if(row!=null){
    									Cell codeCell = dataSheet.getRow(k).getCell(0);
    									try {
    										if(row.getCell(0)!=null){
            									String str="";
        										switch (eval.evaluateInCell(row.getCell(0)).getCellType()) 
        										{
        										case Cell.CELL_TYPE_STRING:
        											str=row.getCell(0).getStringCellValue();
        											break;
        										case Cell.CELL_TYPE_NUMERIC:
        											str=String.valueOf(row.getCell(0).getNumericCellValue());
        											break;
        										}
        										if(str.length()>0){
        											String formula="value("+str+")";
        											codeCell.setCellFormula(formula);  
        											CellValue cellValue = eval.evaluate(codeCell);    	
        										}											
        									}
    									}
    									catch (FormulaParseException e) {
    										JSONObject errObject = new JSONObject();
    										errObject.put("sheet", sheet.getSheetName());
    										errObject.put("error", e.getMessage());
    										errMsg.put(errObject);
									    }
    									for(int y=0;y<6;y++){		
    										Cell ss = dataSheet.getRow(0).createCell(100+y);
    										int cc=y+3;
    										String str="";
    										if(row.getCell(0)!=null){
    											switch (evaluator.evaluateInCell(crow.getCell(0)).getCellType()) 
    											{
    											case Cell.CELL_TYPE_STRING:
    												str=crow.getCell(0).getStringCellValue();
    												break;
    											case Cell.CELL_TYPE_NUMERIC:
    												str=String.valueOf(crow.getCell(0).getNumericCellValue());
    												break;
    											}
    											if(str.length()>0){
    												String formula="VLOOKUP("+str+",A8:F40,"+cc+",FALSE)";
    												ss.setCellFormula(formula);    										
    												Cell zcell =crow.getCell(cc-1);
    												CellValue cellValue = eval.evaluate(ss);    	
    												if(cellValue.getNumberValue()!=0){
    													zcell.setCellValue(cellValue.getNumberValue());    	 
    												} 
    											}											
    										}										   										
    									}
    								}									
    							}
    						}
    						if(sheet.getSheetName().equalsIgnoreCase("19.Budget")){    									
    							for(int k=7; k <= sheet.getLastRowNum();k++){
    								Row row = dataSheet.getRow(k);
    								Row crow = sheet.getRow(k);    							
    								if(row!=null){
    									Cell codeCell = dataSheet.getRow(k).getCell(2);
        								/*if(row.getCell(2)!=null){
        									int str=0;
    										switch (evaluator.evaluateInCell(row.getCell(2)).getCellType()) 
    										{
    										case Cell.CELL_TYPE_STRING:
    											str=Integer.parseInt(row.getCell(2).getStringCellValue());
    											break;
    										case Cell.CELL_TYPE_NUMERIC:
    											str=(int) row.getCell(2).getNumericCellValue();
    											break;
    										}
    										if(str>0){
    											String formula="value("+str+")";
    											codeCell.setCellFormula(formula);  
    											CellValue cellValue = eval.evaluate(codeCell);    	
    										}											
    									}*/
    									/*for(int y=0;y<14;y++){		
    										Cell ss = dataSheet.getRow(0).createCell(100+y);
    										int cc=y+3;
    										String str="";
    										if(row.getCell(2)!=null){
    											switch (evaluator.evaluateInCell(crow.getCell(2)).getCellType()) 
    											{
    											case Cell.CELL_TYPE_STRING:
    												str=crow.getCell(2).getStringCellValue();
    												break;
    											case Cell.CELL_TYPE_NUMERIC:
    												str=String.valueOf(crow.getCell(2).getNumericCellValue());
    												break;
    											}
    											if(str.length()>0){
    												String formula="VLOOKUP("+str+",A8:Q120,"+cc+",FALSE)";
    												ss.setCellFormula(formula);    										
    												Cell zcell =crow.getCell(cc-1);
    												CellValue cellValue = eval.evaluate(ss);    	
    												if(cellValue.getNumberValue()!=0){
    													zcell.setCellValue(cellValue.getNumberValue());    	 
    												} 
    											}											
    										}										   										
    									}*/
    								}									
    							}
    						}
    						if(sheet.getSheetName().equalsIgnoreCase("20.TGT1")){    									
    							for(int k=7; k <= sheet.getLastRowNum();k++){
    								Row row = dataSheet.getRow(k);
    								Row crow = sheet.getRow(k);    								
    								if(row!=null){
    									Cell codeCell = dataSheet.getRow(k).getCell(2);
        								if(row.getCell(2)!=null){
        									String str="";
    										switch (evaluator.evaluateInCell(row.getCell(2)).getCellType()) 
    										{
    										case Cell.CELL_TYPE_STRING:
    											str=row.getCell(2).getStringCellValue();
    											break;
    										case Cell.CELL_TYPE_NUMERIC:
    											str=String.valueOf(row.getCell(2).getNumericCellValue());
    											break;
    										}
    										if(str.length()>0){
    											String formula="value("+str+")";
    											codeCell.setCellFormula(formula);  
    											CellValue cellValue = eval.evaluate(codeCell);    	
    										}											
    									}
    									for(int y=0;y<6;y++){		
    										Cell ss = dataSheet.getRow(0).createCell(100+y);
    										int cc=y+3;
    										String str="";
    										if(row.getCell(2)!=null){
    											switch (evaluator.evaluateInCell(crow.getCell(2)).getCellType()) 
    											{
    											case Cell.CELL_TYPE_STRING:
    												str=crow.getCell(2).getStringCellValue();
    												break;
    											case Cell.CELL_TYPE_NUMERIC:
    												str=String.valueOf(crow.getCell(2).getNumericCellValue());
    												break;
    											}
    											if(str.length()>0){
    												String formula="VLOOKUP("+str+",C8:H200,"+cc+",FALSE)";
    												ss.setCellFormula(formula);    										
    												Cell zcell =crow.getCell(cc+1);
    												CellValue cellValue = eval.evaluate(ss);    	
    												if(cellValue.getNumberValue()!=0){
    													zcell.setCellValue(cellValue.getNumberValue());    	 
    												} 
    											}											
    										}										   										
    									}
    								}									
    							}
    						}    						
    						
    						if(sheet!=null && dataSheet.getSheetName().trim().equals("1.Info") || sheet!=null && dataSheet.getSheetName().trim().equals("16.Assets") 
    								|| sheet!=null && dataSheet.getSheetName().trim().equals("15.Journal") || sheet!=null && dataSheet.getSheetName().trim().equals("Journal")
    								|| sheet!=null && dataSheet.getSheetName().trim().equals("17.Inventory") || sheet!=null && dataSheet.getSheetName().trim().equals("18.Payroll")
    								|| sheet!=null && dataSheet.getSheetName().trim().equals("19.Budget") 
    								|| sheet!=null && dataSheet.getSheetName().trim().equals("21.TGT1A") || sheet!=null && dataSheet.getSheetName().trim().equals("22.NT2")
    								|| sheet!=null && dataSheet.getSheetName().trim().equals("23.TRIAL BALANCE") || sheet!=null && dataSheet.getSheetName().trim().equals("24.ABWS")
    								|| sheet!=null && dataSheet.getSheetName().trim().equals("25.CBWS")){

								for(int kk=2;kk<dataSheet.getLastRowNum();kk++){
									Row currentRow= dataSheet.getRow(kk);	
									if(currentRow!=null){
										if(currentRow.getCell(0)!=null){											
											Row r = dataSheet.getRow(kk);	
											Row dr=null;
											if(sheet.getRow(kk)!=null){
												dr=sheet.getRow(kk);
											}
											else{
												dr=sheet.createRow(kk);
											}
												for (int p = 0; p < 60; p++) {
								                    Cell columnHeaderCell =null;
								                    if(sheet.getRow(kk).getCell(p)!=null){
								                    	columnHeaderCell = sheet.getRow(kk).getCell(p);
								                    }
								                    else{
								                    	columnHeaderCell = sheet.getRow(kk).createCell(p);
								                    }
								                    if(currentRow.getCell(p)!=null){
							                    	    switch (wevaluator.evaluateInCell(currentRow.getCell(p)).getCellType()) 
														{
														case Cell.CELL_TYPE_STRING:
															columnHeaderCell.setCellValue(currentRow.getCell(p).getStringCellValue());
															break;
														case Cell.CELL_TYPE_NUMERIC:
															columnHeaderCell.setCellValue(currentRow.getCell(p).getNumericCellValue());
															break;
									                    case Cell.CELL_TYPE_FORMULA:
									                    	final CellValue cellValue = evaluator.evaluate(currentRow.getCell(p));
									                    	columnHeaderCell.setCellValue(String.valueOf(cellValue.getNumberValue()));
												            break;
														}
								                    }					                   
								                }
											}										    
										
									}	
								}							
							}
    					}
    					else{
    						JSONObject robj=new JSONObject();
    						robj.put("excel", false);
    						robj.put("support", false);
    						robj.put("sheetname", workbook.getSheetAt(i).getSheetName().trim());
    			    		return robj.toString();
    					}
    								
    				
    				}
    				
    				JSONArray arr3 = new JSONArray();
    				
    				Sheet sh = zbook.getSheet("3"); 
			        if(sh.getSheetName().equalsIgnoreCase("3")){
			        	 for(Row r : sh) { 
				        	if(r!=null && r.getRowNum()>0){
				        		for(Cell c : r) {
				        			if(c.getColumnIndex()==7){
				        				final CellValue cell3Value = evaluator.evaluate(r.getCell(7));
				        				if(cell3Value!=null){
				        					switch (cell3Value.getCellType()) {
				                    	    case Cell.CELL_TYPE_STRING:
				                    	        break;
				                    	    case Cell.CELL_TYPE_NUMERIC:
				                    	    	if(cell3Value.getNumberValue()!=0){
				    		        					JSONObject obj= new JSONObject();
				    		        					if(r.getCell(0)!=null){
				    		        						switch (r.getCell(0).getCellType()) {
				    				                    	    case Cell.CELL_TYPE_STRING:
				    				                    	    	obj.put("sheet", r.getCell(0).getStringCellValue());
				    				                    	        break;
				    				                    	    case Cell.CELL_TYPE_NUMERIC:
				    				                    	    	obj.put("sheet", r.getCell(0).getNumericCellValue());
				    				                    	        break;
				    		        						}
				    		        						switch (r.getCell(1).getCellType()) {
					    			                    	    case Cell.CELL_TYPE_STRING:
					    			                    	    	obj.put("code", r.getCell(1).getStringCellValue());
					    			                    	        break;
					    			                    	    case Cell.CELL_TYPE_NUMERIC:
					    			                    	    	obj.put("code", r.getCell(1).getNumericCellValue());
					    			                    	        break;
					    			                    	    case Cell.CELL_TYPE_FORMULA:
				    			                    	    	final CellValue cellValue = evaluator.evaluate(r.getCell(1));
				    			                    	    	switch (cellValue.getCellType()) {
				    					                    	    case Cell.CELL_TYPE_STRING:
				    					                    	    	if(cellValue.getStringValue()!=null){
				    					                    	    		obj.put("code", cellValue.getStringValue());
				    					                    	    	}
				    					                    	    	else{
				    					                    	    		obj.put("code", "null");
				    					                    	    	}
				    					                    	        break;
				    					                    	    case Cell.CELL_TYPE_NUMERIC:
				    					                    	    	if(cellValue.getNumberValue()!=0){
				    					                    	    		obj.put("code", cellValue.getNumberValue());
				    					                    	    	}
				    					                    	    	else{
				    					                    	    		obj.put("code", "null");
				    					                    	    	}
				    					                    	        break;
				    			        						}
				    			                    	        break;
				    		        						}
				    		        						switch (r.getCell(2).getCellType()) {
					    			                    	    case Cell.CELL_TYPE_STRING:
					    			                    	    	obj.put("dans", r.getCell(2).getStringCellValue());
					    			                    	        break;
					    			                    	    case Cell.CELL_TYPE_NUMERIC:
					    			                    	    	obj.put("dans", r.getCell(2).getNumericCellValue());
					    			                    	        break;
				    		        						}
				    		        						switch (r.getCell(3).getCellType()) {
					    			                    	    case Cell.CELL_TYPE_STRING:
					    			                    	    	obj.put("uld", r.getCell(3).getStringCellValue());
					    			                    	        break;
					    			                    	    case Cell.CELL_TYPE_NUMERIC:
					    			                    	    	obj.put("uld", r.getCell(3).getNumericCellValue());
					    			                    	        break;
				    		        						}
				    		        						switch (r.getCell(4).getCellType()) {
					    			                    	    case Cell.CELL_TYPE_STRING:
					    			                    	    	obj.put("uldDun", r.getCell(4).getStringCellValue());
					    			                    	        break;
					    			                    	    case Cell.CELL_TYPE_NUMERIC:
					    			                    	    	obj.put("uldDun", r.getCell(4).getNumericCellValue());
					    			                    	        break;
					    			                    	    case Cell.CELL_TYPE_BLANK:
					    			                    	    	obj.put("uldDun", "0");
					    			                    	        break;
					    			                    	    case Cell.CELL_TYPE_FORMULA:
				    			                    	    	final CellValue cellValue = evaluator.evaluate(r.getCell(4));
				    			                    	    	switch (cellValue.getCellType()) {
				    					                    	    case Cell.CELL_TYPE_STRING:
				    					                    	    	if(cellValue.getStringValue()!=null){
				    					                    	    		obj.put("uldDun", cellValue.getStringValue());
				    					                    	    	}
				    					                    	    	else{
				    					                    	    		obj.put("uldDun", "0");
				    					                    	    	}
				    					                    	        break;
				    					                    	    case Cell.CELL_TYPE_NUMERIC:
				    					                    	    	if(cellValue.getNumberValue()!=0){
				    					                    	    		obj.put("uldDun", cellValue.getNumberValue());
				    					                    	    	}
				    					                    	    	else{
				    					                    	    		obj.put("uldDun", "0");
				    					                    	    	}
				    					                    	        break;
				    			        						}
				    			                    	        break;
				    		        						}
				    		        						switch (r.getCell(5).getCellType()) {
					    			                    	    case Cell.CELL_TYPE_STRING:
					    			                    	    	obj.put("tulgalt", r.getCell(5).getStringCellValue());
					    			                    	        break;
					    			                    	    case Cell.CELL_TYPE_NUMERIC:
					    			                    	    	obj.put("tulgalt", r.getCell(5).getNumericCellValue());
					    			                    	        break;
				    		        						}
				    		        						if(r.getCell(6)!=null){
				    		        							switch (r.getCell(6).getCellType()) {
						    			                    	    case Cell.CELL_TYPE_STRING:
						    			                    	    	obj.put("uldDun2", r.getCell(6).getStringCellValue());
						    			                    	        break;
						    			                    	    case Cell.CELL_TYPE_NUMERIC:
						    			                    	    	obj.put("uldDun2", r.getCell(6).getNumericCellValue());
						    			                    	        break;
						    			                    	    case Cell.CELL_TYPE_BLANK:
						    			                    	    	obj.put("uldDun2", "0");
						    			                    	        break;
						    			                    	    case Cell.CELL_TYPE_FORMULA:
					    			                    	    	final CellValue cellValue = evaluator.evaluate(r.getCell(6));
					    			                    	    	switch (cellValue.getCellType()) {
					    					                    	    case Cell.CELL_TYPE_STRING:
					    					                    	    	if(cellValue.getStringValue()!=null){
					    					                    	    		obj.put("uldDun2", cellValue.getStringValue());
					    					                    	    	}
					    					                    	    	else{
					    					                    	    		obj.put("uldDun2", "0");
					    					                    	    	}
					    					                    	        break;
					    					                    	    case Cell.CELL_TYPE_NUMERIC:
					    					                    	    	if(cellValue.getNumberValue()!=0){
					    					                    	    		obj.put("uldDun2", cellValue.getNumberValue());
					    					                    	    	}
					    					                    	    	else{
					    					                    	    		obj.put("uldDun2", "0");
					    					                    	    	}
					    					                    	        break;
					    			        						}
					    			                    	        break;
					    		        						}
				    		        						}
				    		        						if(r.getCell(7)!=null){
				    		        							switch (r.getCell(7).getCellType()) {
						    			                    	    case Cell.CELL_TYPE_STRING:
						    			                    	    	obj.put("zuruu", r.getCell(7).getStringCellValue());
						    			                    	        break;
						    			                    	    case Cell.CELL_TYPE_NUMERIC:
						    			                    	    	obj.put("zuruu", r.getCell(7).getNumericCellValue());
						    			                    	        break;
						    			                    	    case Cell.CELL_TYPE_BLANK:
						    			                    	    	obj.put("zuruu", "0");
						    			                    	        break;
						    			                    	    case Cell.CELL_TYPE_FORMULA:
					    			                    	    	final CellValue cellValue = evaluator.evaluate(r.getCell(7));
					    			                    	    	switch (cellValue.getCellType()) {
					    					                    	    case Cell.CELL_TYPE_STRING:
					    					                    	    	if(cellValue.getStringValue()!=null){
					    					                    	    		obj.put("zuruu", cellValue.getStringValue());
					    					                    	    	}
					    					                    	    	else{
					    					                    	    		obj.put("zuruu", "0");
					    					                    	    	}
					    					                    	        break;
					    					                    	    case Cell.CELL_TYPE_NUMERIC:
					    					                    	    	if(cellValue.getNumberValue()>1 ||cellValue.getNumberValue()<-1){
					    					                    	    		obj.put("zuruu", cellValue.getNumberValue());
					    					                    	    		arr3.put(obj);
					    					                    	    	}
					    					                    	        break;
					    			        						}
					    			                    	        break;
					    		        						}
				    		        						}				    		        						
				    		        					}
				    		        				}
				                    	        break;
				        					}
				        				}
				        			}
					            } 
				        	}
				        } 
			        }
    		
    		        
    				JSONObject robj=new JSONObject();
    				
    				if(errList.length()>0 || sheetList.length()>0){
    					err.put("prefilter", errList);
    					err.put("additionalSheet", sheetList);
    					err.put("excel", false);
    					err.put("support", false);				
    					return  err.toString();
    				}
    				
    				if(arr1.length()>0){
    					robj.put("support", false);
    		    		robj.put("excel", false);
    		    		robj.put("error", arr1);
    		    		return robj.toString();
    				}
    				
    				if(errMsg.length()>0){
    					robj.put("support", false);
    		    		robj.put("excel", false);
    		    		robj.put("formula", errMsg);
    		    		return robj.toString();
    				}
    				
    				if(err.length()==0){
    					
    					String uuid = UUID.randomUUID().toString()+".xlsx";

    					File directory = new File("upload-dir"+File.separator+pr.getName());
    					if (! directory.exists()){
    					        directory.mkdir();
					    }
					    LutUser loguser= (LutUser) dao.getHQLResult("from LutUser t where t.username='"+pr.getName()+"'", "current");
    		            FileOutputStream fout = new FileOutputStream("upload-dir"+File.separator+pr.getName()+ File.separator+uuid);
    		            File oldFile=new File(realpath+File.separator+loguser.getFlurl());
    		            if(oldFile.exists()){    		            	
    		            	oldFile.delete();
    		            	System.out.println("file deleted");
    		            }
    		          
    		            String incuid = UUID.randomUUID().toString()+".xlsx";    		            
    		            
    		    	    furl = furl+File.separator+pr.getName()+ File.separator+uuid ;		
    		    		Date d1 = new Date();
    		    		SimpleDateFormat df = new SimpleDateFormat("MM/dd/YYYY HH:mm a");
    		            String formattedDate = df.format(d1);
    		     	    FileConverted newFile = new FileConverted();
    		    		newFile.setName(file.getOriginalFilename());
    		    		newFile.setFsize(file.getSize()/1024);    		    	
    		    		newFile.setFdate(formattedDate);
    		    		newFile.setUserid(loguser.getId());
    		    		newFile.setFlurl(furl);
    		    		
    		    		loguser.setFlurl(furl);
    		    		loguser.setFlname(file.getOriginalFilename());
    		    		dao.PeaceCrud(loguser, "LutUser", "update", (long) loguser.getId(), 0, 0, null);
    		            
    		          //  FileOutputStream incfout = new FileOutputStream("upload-dir"+File.separator+pr.getName()+ File.separator+incuid);
    		    		robj.put("support", true);
    		    		robj.put("excel", true);
    		    		robj.put("diff", arr3);

    		            if(zbook.getSheet("15.Journal")!=null){
    		            	for(int i=0;i<zbook.getSheet("15.Journal").getLastRowNum()+1;i++){
    							Row currentRow = zbook.getSheet("15.Journal").getRow(i);
    							if(currentRow!=null){
    								if(currentRow.getCell(0)==null){
    									zbook.getSheet("15.Journal").removeRow(currentRow);
    								}
    							}						
    						}
    		            }
    		            if(zbook.getSheet("Journal")!=null){
    		            	for(int i=0;i<zbook.getSheet("Journal").getLastRowNum()+1;i++){
    							Row currentRow = zbook.getSheet("Journal").getRow(i);
    							if(currentRow!=null){
    								if(currentRow.getCell(0)==null){
    									zbook.getSheet("Journal").removeRow(currentRow);
    								}
    							}						
    						}
    		            }
    		            zbook.write(fout);
    		            fout.close();
    		            
    				}
    				else{
    					robj.put("support", true);
    		    		robj.put("excel", false);
    		    		robj.put("error", arr);
    				}
    	    		return robj.toString();
    			}
    		
    		}
    	    
    	    
    	    furl = furl+File.separator+pr.getName()+ File.separator+newuuid ;		
    		Date d1 = new Date();
    		SimpleDateFormat df = new SimpleDateFormat("MM/dd/YYYY HH:mm a");
            String formattedDate = df.format(d1);
    /*	    LnkAuditFormFile newFile = new LnkAuditFormFile();
    		newFile.setName(newuuid);
    		newFile.setSize(file.getSize()/1024);
    		newFile.setFilename(filename);
    		newFile.setCreateDate(formattedDate);
    		newFile.setFormid(formid);
    		newFile.setFileurl(furl);
    		dao.PeaceCrud(newFile, "LnkAuditFile", "save", (long) 0, 0, 0, null);*/
    	    
			return "true";
	}
	
	@GetMapping("/api/file/download/excel")
	@ResponseBody
	public void getFile(Principal pr,HttpServletRequest req,HttpServletResponse response) throws EncryptedDocumentException, InvalidFormatException, IOException {
		LutUser loguser= (LutUser) dao.getHQLResult("from LutUser t where t.username='"+pr.getName()+"'", "current");
		Path currentRelativePath = Paths.get("");
		String realpath = currentRelativePath.toAbsolutePath().toString();
		if(loguser.getFlurl()!=null){
			File con=new File(realpath+loguser.getFlurl());
			FileInputStream str= new FileInputStream(con);
			Workbook workbook = WorkbookFactory.create(str); 
			
			List<String> shArr= Arrays.asList("1.Info", "2.CT1A", "3.CT2A", "4.CT3A", "5.CT4A", "6.CTT1", "7.CTT2", "8.CTT3", "9.CTT4", "10.CTT5", "11.CTT6", "12.CTT7","13.CTT8","14.CTT9","15.Journal","16.Assets","17.Inventory","18.Payroll","19.Budget","20.TGT1","21.TGT1A","22.NT2","23.TRIAL BALANCE","24.ABWS","25.CBWS");
			FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
			for(String item:shArr){
				Sheet sh=workbook.getSheet(item);
				 for(Row r : sh) { 
			        	if(r!=null){
			        		 for(Cell c : r) { 
				                if (c != null && c.getCellTypeEnum()== CellType.FORMULA) { 
				                    String formula = c.getCellFormula(); 
				                    if (formula != null) { 
				                    	c.setCellFormula(formula);	    				                    	
				                    	CellValue cellValue = evaluator.evaluate(c);	    				                    	 
				                    	switch (cellValue.getCellTypeEnum()) {
				                    	    case STRING:	    				                    	    	
				                    	    	c.setCellType(CellType.STRING);
				                    	    	c.setCellValue(cellValue.getStringValue());
				                    	        break;
				                    	    case BOOLEAN:
				                    	        System.out.print(cellValue.getBooleanValue());
				                    	        break;
				                    	    case NUMERIC:
				                    	    	c.setCellType(CellType.NUMERIC);
				                    	    	c.setCellValue(cellValue.getNumberValue());
				                    	        break;
				                    	}
				                        evaluator.clearAllCachedResultValues();                       
				                    } 
				                } 
				            } 
			        	}
			        }
			}
			//evaluator.evaluateAll();		
			for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
				Sheet st=workbook.getSheetAt(i);
				boolean sheet=false;
				for(String nm:shArr){
					if(nm.toUpperCase().equalsIgnoreCase(st.getSheetName().trim().toUpperCase())){
						 sheet=true;
					}
				}
				if(!sheet){
					workbook.removeSheetAt(i);
				}
			}

	        try (ServletOutputStream outputStream = response.getOutputStream()) {
				response.setContentType("application/ms-excel; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
	            response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+"Audit-it-"+loguser.getFlname()+".xlsx");
	            workbook.write(outputStream);
	            outputStream.close();
	        }
		}		
	}
	
	@GetMapping("/api/file/verify/excel")
	@ResponseBody
	public String getFileVerify(Principal pr,HttpServletRequest req,HttpServletResponse response) throws EncryptedDocumentException, InvalidFormatException, IOException {
		LutUser loguser= (LutUser) dao.getHQLResult("from LutUser t where t.username='"+pr.getName()+"'", "current");
		Path currentRelativePath = Paths.get("");
		String realpath = currentRelativePath.toAbsolutePath().toString();
		JSONObject obj=new JSONObject();
		if(loguser.getFlurl()!=null){
			File fl=new File(realpath+File.separator+loguser.getFlurl());
			if(fl.exists()){
				if(loguser.getBalance()>0){
					loguser.setBalance(loguser.getBalance()-1);
					obj.put("excel", true);
					obj.put("balance", loguser.getBalance());
					dao.PeaceCrud(loguser, "LutUser", "update", (long) loguser.getId(), 0, 0, null);	
				}
				else{
					obj.put("balance", false);
				}
			}else{
				obj.put("file", false);
			}
		}
		return obj.toString();
	}
	
	@GetMapping("/api/file/download/{id}")
	@ResponseBody
	public void getFileConv(@PathVariable long id, Principal pr,HttpServletRequest req,HttpServletResponse response) throws EncryptedDocumentException, InvalidFormatException, IOException {
		FileConverted fl=  (FileConverted) dao.getHQLResult("from FileConverted t where t.id='"+id+"'", "current");
		Path currentRelativePath = Paths.get("");
		String realpath = currentRelativePath.toAbsolutePath().toString();
		
		File con=new File(realpath+fl.getFlurl());
		FileInputStream str= new FileInputStream(con);
		Workbook workbook = WorkbookFactory.create(str); 
		
		List<String> shArr= Arrays.asList("3","1.Info", "2.CT1A", "3.CT2A", "4.CT3A", "5.CT4A", "6.CTT1", "7.CTT2", "8.CTT3", "9.CTT4", "10.CTT5", "11.CTT6", "12.CTT7","13.CTT8","14.CTT9","15.Journal","16.Assets","17.Inventory","18.Payroll","19.Budget","20.TGT1","21.TGT1A","22.NT2","23.TRIAL BALANCE","24.ABWS","25.CBWS");
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		
		for(String item:shArr){
			Sheet sh=workbook.getSheet(item);
			 for(Row r : sh) { 
		        	if(r!=null){
		        		 for(Cell c : r) { 
			                if (c != null && c.getCellTypeEnum()== CellType.FORMULA) { 
			                    String formula = c.getCellFormula(); 
			                    if (formula != null) { 
			                    	c.setCellFormula(formula);	    				                    	
			                    	CellValue cellValue = evaluator.evaluate(c);	    				                    	 
			                    	switch (cellValue.getCellTypeEnum()) {
			                    	    case STRING:	    				                    	    	
			                    	    	c.setCellType(CellType.STRING);
			                    	    	c.setCellValue(cellValue.getStringValue());
			                    	        break;
			                    	    case BOOLEAN:
			                    	        System.out.print(cellValue.getBooleanValue());
			                    	        break;
			                    	    case NUMERIC:
			                    	    	c.setCellType(CellType.NUMERIC);
			                    	    	c.setCellValue(cellValue.getNumberValue());
			                    	        break;
			                    	}
			                        evaluator.clearAllCachedResultValues();                       
			                    } 
			                } 
			            } 
		        	}
		        }
		}
		//evaluator.evaluateAll();		
		for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
			Sheet st=workbook.getSheetAt(i);	
		
			boolean sheet=false;
			for(String nm:shArr){
				if(nm.toUpperCase().equalsIgnoreCase(st.getSheetName().trim().toUpperCase())){
					 sheet=true;
				}
			}
			if(!sheet){
				workbook.removeSheetAt(i);
			}
		}

        try (ServletOutputStream outputStream = response.getOutputStream()) {
			response.setContentType("application/ms-excel; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+"Audit-it-"+fl.getName()+".xlsx");
            workbook.write(outputStream);
            outputStream.close();
        }
	}

	@GetMapping("/api/file/sheet/excel")
	@ResponseBody
	public void getFileSheet(Principal pr,HttpServletRequest req,HttpServletResponse response) throws EncryptedDocumentException, InvalidFormatException, IOException {
		LutUser loguser= (LutUser) dao.getHQLResult("from LutUser t where t.username='"+pr.getName()+"'", "current");
		//FileConverted fl=  (FileConverted) dao.getHQLResult("from FileConverted t where t.id='"+id+"'", "current");
		Path currentRelativePath = Paths.get("");
		String realpath = currentRelativePath.toAbsolutePath().toString();
		
		File con=new File(realpath+loguser.getFlurl());
		if(con.exists()){
			FileInputStream str= new FileInputStream(con);
			Workbook workbook = WorkbookFactory.create(str); 
			
			List<String> shArr= Arrays.asList("А-6.1","А-6.2", "А-6.3", "СТХ");
			FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
			
			for(String item:shArr){
				Sheet sh=workbook.getSheet(item);
				 for(Row r : sh) { 
			        	if(r!=null){
			        		 for(Cell c : r) { 
				                if (c != null && c.getCellTypeEnum()== CellType.FORMULA) { 
				                    String formula = c.getCellFormula(); 
				                    if (formula != null) { 
				                    	c.setCellFormula(formula);	    				                    	
				                    	CellValue cellValue = evaluator.evaluate(c);	    				                    	 
				                    	switch (cellValue.getCellTypeEnum()) {
				                    	    case STRING:	    				                    	    	
				                    	    	c.setCellType(CellType.STRING);
				                    	    	c.setCellValue(cellValue.getStringValue());
				                    	        break;
				                    	    case BOOLEAN:
				                    	        System.out.print(cellValue.getBooleanValue());
				                    	        break;
				                    	    case NUMERIC:
				                    	    	c.setCellType(CellType.NUMERIC);
				                    	    	c.setCellValue(cellValue.getNumberValue());
				                    	        break;
				                    	}
				                        evaluator.clearAllCachedResultValues();                       
				                    } 
				                } 
				            } 
			        	}
			        }
			}
			
			
		
			for(int i=workbook.getNumberOfSheets()-1;i>=0;i--){
				Sheet st=workbook.getSheetAt(i);	
			
				boolean sheet=false;
				for(String nm:shArr){
					if(nm.toUpperCase().equalsIgnoreCase(st.getSheetName().trim().toUpperCase())){
						 sheet=true;
					}
				}
				if(!sheet){
					workbook.removeSheetAt(i);
				}
			}

	        try (ServletOutputStream outputStream = response.getOutputStream()) {
				response.setContentType("application/ms-excel; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
	            response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+"Audit-it-"+loguser.getFlname()+".xlsx");
	            workbook.write(outputStream);
	            outputStream.close();
	        }
		}		
	}
	
	
	/*@RequestMapping(value="/api/excel/verify/report/{mid}/{id}",method=RequestMethod.GET)
	public boolean verify(@PathVariable long id,@PathVariable long mid,HttpServletRequest req,HttpServletResponse response) throws JSONException, DocumentException, Exception {
		JsonObject obj= new JsonObject();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			FileInputStream fis = null;
			LnkAuditReport main = (LnkAuditReport) dao.getHQLResult("from LnkAuditReport t where t.id='"+id+"'", "current");
			Path currentRelativePath = Paths.get("");
			String realpath = currentRelativePath.toAbsolutePath().toString();
			if(main!=null){
				File file = new File(realpath+File.separator+main.getFileurl());
				if(main.getFileurl()==null){
					return false;
				}
				else if(!file.exists()){
					return false;
				}
				else{
					return true;
				}
			}			
    		
		}
		return false;
	}*/
	
	/*@GetMapping("formfile/{id}")
	@ResponseBody
	public ResponseEntity<Resource> getFileName(@PathVariable int id) {
		LnkAuditFormFile fl=  (LnkAuditFormFile) dao.getHQLResult("from LnkAuditFormFile t where t.id='"+id+"'", "current");
		Resource file = storageService.loadAsResource(fl.getFileurl());
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fl.getName() + "\"")
				.body(file);
	}
	
	@GetMapping("/api/excel/export/report/{appid}/{id}")
	@ResponseBody
	public ResponseEntity<Resource> getFileNotloh(@PathVariable long id,@PathVariable long appid) {
		LnkAuditReport fl=  (LnkAuditReport) dao.getHQLResult("from LnkAuditReport t where t.id='"+id+"'", "current");
		Resource file = storageService.loadAsResource(fl.getFileurl());
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fl.getFilename() + "\"")
				.body(file);
	}
	
	@GetMapping("/api/excel/delete/report/{appid}/{id}")
	@ResponseBody
	public boolean deleteReport(@PathVariable long id,@PathVariable long appid) {
		LnkAuditReport main = (LnkAuditReport) dao.getHQLResult("from LnkAuditReport t where t.id='"+id+"'", "current");
		Path currentRelativePath = Paths.get("");
		String realpath = currentRelativePath.toAbsolutePath().toString();
		File file = new File(realpath+File.separator+main.getFileurl());
		if(file.exists()){
			file.delete();
			dao.PeaceCrud(main, "LnkAuditReport", "delete", (long) id, 0, 0, null);
			return true;
		}
		else{
			return false;
		}
	}*/

}
