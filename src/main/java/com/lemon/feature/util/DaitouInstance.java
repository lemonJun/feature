//package com.lemon.feature.util;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.WriteAbortedException;
//import java.io.Writer;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.log4j.Logger;
//
//
////import java_cup.internal_error;
//import au.com.bytecode.opencsv.CSVWriter;
//
//import com.bj58.sfft.imc.entity.Info;
//import com.bj58.sfft.imc.entity.ParaClass;
//import com.bj58.zhaopin.service.zp.fundation.contract.entitys.ResumeDetail;
//import com.bj58.zhaopin.service.zp.fundation.contract.entitys.ResumeParam;
//import com.lemon.feature.model.AttributeFilter;
//import com.lemon.feature.model.TrainModelBootStrap;
//import com.lemon.feature.util.ConstantsUtil;
//
//import weka.core.Attribute;
//import weka.core.DenseInstance;
//import weka.core.Instance;
//import weka.core.Instances;
//import weka.core.SparseInstance;
//import weka.core.converters.CSVLoader;
//
//public class DaitouInstance{
//	
//	private static final Logger logger = Logger.getLogger(TrainModelBootStrap.class);
//	private static final int[] PROBLEM_ATTRIBUTE = new int[] { 0, 1, 2, 13 };
//	
//	// iLat,iLng,iType,iExp,iLvl,iPrice,iAge,iGender
//	protected String iLat;
//	protected String iLng;
//	protected String iType;
//	protected String iExp;
//	protected String iLvl;
//	protected String iPrice;
//	protected String iAge;
//	protected String iGender;
//	// uLvl,uExp,uLat,uLng,uPrice
//	protected String uLvl;
//	protected String uExp;
//	protected String uLat;
//	protected String uLng;
//	protected String uPrice;
//	// LocalId,isBuy
//	protected String LocalId;
//	protected String isBuy;
//	
//	public DaitouInstance(Info info, ResumeDetail resume) {
//		// collect resume information
//		Map<String, String> iPosition = getResumePosition(resume.getParaList());
//		this.iLat   = iPosition.get("lat") == null ? "-1" :iPosition.get("lat");
//		this.iLng   = iPosition.get("lng") == null ? "-1" : iPosition.get("lng");
//		this.iType  = resume.getTargetCateID().split("\\,")[0] == null ? "-1" : resume.getTargetCateID().split("\\,")[0];
//		this.iExp   = "" + resume.getWorkedYears();
//		this.iLvl   = "" + resume.getEducation();
//		this.iPrice = "" + resume.getSalary();
//		this.iAge   = "" + resume.getAge();
//		if(resume.getGender() == false)
//			this.iGender = "1";   // iGender=1, male
//		else
//			this.iGender = "0";   // iGender=0, female
//		
//		// collect info information
//		List<ParaClass> paras = info.getPara();
//		for (ParaClass para : paras) {
//            int paramid = para.getValue().getParameterID();
//            String paramvalue = para.getValue().getParameterValue();
//            if (paramid == 6691) {
//                this.uLat = paramvalue == null ? "-1" : paramvalue;
//            } else if (paramid == 6692) {
//                this.uLng = paramvalue == null ? "-1" : paramvalue;
//            } else if (paramid == 5354) {
//            	String tempUPrice = String.valueOf(CMCCacheUtil.uniqueSalary(paramvalue,1,info.getInfoID()));
//                this.uPrice = tempUPrice == null ? "-1" : tempUPrice;
//            } else if (paramid == 5356) {
//            	String tempULvl = String.valueOf(CMCCacheUtil.uniqueEdu(Integer.parseInt(paramvalue), 1,info.getInfoID()));
//                this.uLvl = tempULvl == null ? "-1" : tempULvl;
//            } else if (paramid == 5357) {
//            	String tempUExp = String.valueOf(CMCCacheUtil.uniqueExp(Integer.parseInt(paramvalue), 1,info.getInfoID()));
//            	this.uExp = tempUExp == null ? "-1" : tempUExp;
//
//            }
//        }
//		
//		// collect other information
//		this.LocalId = "000";
//		this.isBuy = "ture";
//	}
//	
//	public Map<String, String> getResumePosition(List<ResumeParam> params) {
//        ResumeParam lonlatparam = null;
//        Map<String, String> result = new HashMap<String, String>();
//        if (params == null) {
//            result.put("long", "-1");
//            result.put("lat", "-1");
//            return result;
//        }
//        for (ResumeParam param : params) {
//            if (100 == param.getParamid()) {
//                lonlatparam = param;
//                break;
//            }
//        }
//        try {
//            String lngstr = lonlatparam.getParamValue().split(",")[0];
//            String latstr = lonlatparam.getParamValue().split(",")[1];
//            result.put("long", lngstr);
//            result.put("lat", latstr);
//            return result;
//        } catch (Exception e) {
//            result.put("long", "-1");
//            result.put("lat", "-1");
//            return result;
//        }
//    }
//	
//	public void showInstance(){
//		logger.info("\n\n");
//		logger.info("iLat    = " + iLat);
//		logger.info("iLng    = " + iLng);
//		logger.info("iType   = " + iType);
//		logger.info("iExp    = " + iExp);
//		logger.info("iLvl    = " + iLvl);
//		logger.info("iPrice  = " + iPrice);
//		logger.info("iAge    = " + iAge);
//		logger.info("iGender = " + iGender);
//		logger.info("uLvl    = " + uLvl);
//		logger.info("uExp    = " + uExp);
//		logger.info("uLat    = " + uLat);
//		logger.info("uLng    = " + uLng);
//		logger.info("uPrice  = " + uPrice);
//		logger.info("LocalId = " + LocalId);
//		logger.info("isBuy   = " + isBuy);
//		logger.info("\n\n");
//	}
//	
//	public Instance makeInstanceByBuild(){
//		Instance testIns = null;
//		Instances setIns = null;
//		Instance oneIns = null;
//		try{
//			// 构建纬度属性
//			// iLat,iLng,iType,iExp,iLvl,iPrice,iAge,iGender
//			Attribute iLat    = new Attribute("iLat");
//			Attribute iLng    = new Attribute("iLng");
//			Attribute iType   = new Attribute("iType");
//			Attribute iExp    = new Attribute("iExp");
//			Attribute iLvl    = new Attribute("iLvl");
//			Attribute iPrice  = new Attribute("iPrice");
//			Attribute iAge    = new Attribute("iAge");
//			Attribute iGender = new Attribute("iGender");
//			// uLvl,uExp,uLat,uLng,uPrice
//			Attribute uLvl    = new Attribute("uLvl");
//			Attribute uExp    = new Attribute("uExp");
//			Attribute uLat    = new Attribute("uLat");
//			Attribute uLng    = new Attribute("uLng");
//			Attribute uPrice  = new Attribute("uPrice");
//			// LocalId,isBuy
//			Attribute LocalId = new Attribute("LocalId");
//			ArrayList<String> labels = new ArrayList<String>();
//			labels.add("true");
//			labels.add("false");
//			Attribute isBuy = new Attribute("isBuy", labels);
//			
//			// 添加属性到属性集attrs
//			ArrayList<Attribute> attrs = new ArrayList<Attribute>();
//			// iLat,iLng,iType,iExp,iLvl,iPrice,iAge,iGender
//			attrs.add(iLat);
//			attrs.add(iLng);
//			attrs.add(iType);
//			attrs.add(iExp);
//			attrs.add(iLvl);
//			attrs.add(iPrice);
//			attrs.add(iAge);
//			attrs.add(iGender);
//			// uLvl,uExp,uLat,uLng,uPrice
//			attrs.add(uLvl);
//			attrs.add(uExp);
//			attrs.add(uLat);
//			attrs.add(uLng);
//			attrs.add(uPrice);
//			// LocalId,isBuy
//			attrs.add(LocalId);
//			attrs.add(isBuy);
//			
//			// 构建以attrs属性的Instances集合setIns
//			setIns = new Instances("ScoreIns", attrs, 1);
////			logger.info("\n\n init setIns attribute:");
////			logger.info(setIns);
//		}catch(Exception e){
//			logger.error("\n\n make attributes(setIns) error.");
//		}
//		
//		try{
//			// 构建Instance
//			double[] values = new double[setIns.numAttributes()];
//			// iLat,iLng,iType,iExp,iLvl,iPrice,iAge,iGender
//			values[0]  = Double.parseDouble(this.iLat);
//			values[1]  = Double.parseDouble(this.iLng);
//			values[2]  = Double.parseDouble(this.iType);
//			values[3]  = Double.parseDouble(this.iExp);
//			values[4]  = Double.parseDouble(this.iLvl);
//			values[5]  = Double.parseDouble(this.iPrice);
//			values[6]  = Double.parseDouble(this.iAge);
//			values[7]  = Double.parseDouble(this.iGender);
//			// uLvl,uExp,uLat,uLng,uPrice
//			values[8]  = Double.parseDouble(this.uLvl);
//			values[9]  = Double.parseDouble(this.uExp);
//			values[10] = Double.parseDouble(this.uLat);
//			values[11] = Double.parseDouble(this.uLng);
//			values[12] = Double.parseDouble(this.uPrice);
//			// LocalId,isBuy
//			values[13] = Double.parseDouble(this.LocalId);
//			values[14] = setIns.attribute(setIns.numAttributes() -1).indexOfValue("true");
//			oneIns = new DenseInstance(1.0, values);
//			logger.info("\n\n oneIns : " + oneIns.toString());
//		}catch(Exception e){
//			logger.error("\n\n make Instance(oneIns) Error.");
//		}
//		
//		try{
//			// 将构建的ins添加到用例集合setIns
//			setIns.add(oneIns);
////			logger.info("\n\n setIns before washed:");
////			logger.info(setIns);
//		}catch(Exception e){
//			logger.error("\n\n put Instance(oneIns) into Set(setIns) error.");
//		}
//		
//		try{
//			AttributeFilter impAttrFilter = new AttributeFilter();
//			setIns = impAttrFilter.filter(setIns, PROBLEM_ATTRIBUTE);
//			setIns.setClassIndex(setIns.numAttributes() - 1);
////			logger.info("\n\n setIns after washed:");
////			logger.info(setIns);
//		}catch(Exception e){
//			logger.error("\n\n filter wash attribute error.");
//		}
//			
//		try{
//			testIns = setIns.instance(setIns.numInstances() - 1);
//			logger.info("\n\n testIns = " + testIns.toString());
//		}catch(Exception e){
//			logger.error("\n\n pick Instance(testIns) from Set(setIns) error.");
//		}
//		return testIns;
//	}
//	
//	public Instance makeInstanceByFile(){
//		Instance testIns = null;
//		
//		File f = new File(ConstantsUtil.TEST_INS_PATH);
//		CSVWriter writer = null;
//		// write testIns
//		try{
//			writer = new CSVWriter(new FileWriter(f),',');
//		}catch(Exception e){
//			logger.info("Open writer Exception.");
//			return testIns;
//		}
//		List<String[]> allList=new ArrayList<String[]>();
//		List<String> list = null;
//		list = new ArrayList<String>();
//		// iLat,iLng,iType,iExp,iLvl,iPrice,iAge,iGender
//		list.add("iLat");
//		list.add("iLng");
//		list.add("iType");
//		list.add("iExp");
//		list.add("iLvl");
//		list.add("iPrice");
//		list.add("iAge");
//		list.add("iGender");
//		// uLvl,uExp,uLat,uLng,uPrice
//		list.add("uLvl");
//		list.add("uExp");
//		list.add("uLat");
//		list.add("uLng");
//		list.add("uPrice");
//		// other
//		list.add("LocalId");
//		list.add("isBuy");
//		allList.add(list.toArray(new String[list.size()]));
//		list = new ArrayList<String>();
//		// iLat,iLng,iType,iExp,iLvl,iPrice,iAge,iGender
//		list.add(this.iLat);
//		list.add(this.iLng);
//		list.add(this.iType);
//		list.add(this.iExp);
//		list.add(this.iLvl);
//		list.add(this.iPrice);
//		list.add(this.iAge);
//		list.add(this.iGender);
//		// uLvl,uExp,uLat,uLng,uPrice
//		list.add(this.uLvl);
//		list.add(this.uExp);
//		list.add(this.uLat);
//		list.add(this.uLng);
//		list.add(this.uPrice);
//		// other
//		list.add(this.LocalId);
//		list.add(this.isBuy);
//		allList.add(list.toArray(new String[list.size()]));
//		writer.writeAll(allList);
//		try{
//			writer.close();
//		}catch(Exception e){
//			logger.info("writer close Exception.");
//			return testIns;
//		}
//
//		// load
//		CSVLoader csvLoader = new CSVLoader();
//		Instances organIns = null;
//		try{
//			csvLoader.setSource(f);
//		}catch(Exception e){
//			logger.info("Open loader Exception.");
//			return testIns;
//		}
//		try {
//	        organIns = csvLoader.getDataSet();
//	        AttributeFilter impAttrFilter = new AttributeFilter();
//			organIns = impAttrFilter.filter(organIns, PROBLEM_ATTRIBUTE);
//			organIns.setClassIndex(organIns.numAttributes() - 1);
//        } catch (IOException e) {
//        	logger.info("Put csvFile into organIns Exception");
//        	return testIns;
//        }
//		try{
//			testIns = organIns.instance(organIns.numInstances() - 1);
//			logger.info("testIns = " + testIns.toString());
//		}catch(Exception e){
//			logger.info("get testIns from organIns Exception.");
//			return testIns;
//		}
//		return testIns;
//	}
//}
