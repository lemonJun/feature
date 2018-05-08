package com.lemon.feature.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.lemon.feature.util.ConstantsUtil;

import weka.classifiers.bayes.BayesNet;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.CSVLoader;

public class TrainModelBootStrap {

    private static final Logger logger = Logger.getLogger(TrainModelBootStrap.class);

    private static AtomicInteger success = new AtomicInteger(0);

    private static List<String> fail = new ArrayList<String>();

    private static final int[] PROBLEM_ATTRIBUTE = new int[] { 0, 1, 2, 13 };

    public static void main(String[] args) {
        TrainModelBootStrap train = new TrainModelBootStrap();
        train.trainAll(ConstantsUtil.RAW_PATH);
    }

    // 训练所有文件
    public void trainAll(String dirpath) {
        File dir = new File(dirpath);
        File[] all = dir.listFiles();
        List<File> rawFiles = new ArrayList<File>();
        for (File f : all) {
            if (f.isFile() && f.getName().endsWith("csv")) {
                if (f.length() > 1024 * 1024 * 300) {
                    logger.info(f.getName() + " is too big --- " + f.length());
                    String fileName = f.getName();
                    if (f.renameTo(new File(dirpath + "old_" + fileName))) {
                        File oldFile = new File(dirpath + "old_" + fileName);
                        File nf = null;
                        try {
                            nf = GrepFile.rewriteFile(oldFile, dirpath, fileName);
                        } catch (IOException e) {
                            logger.error("Exception : rewriteFile " + fileName + " Error.");
                        }
                        rawFiles.add(nf);
                        logger.info("add newFile " + fileName + " --- size : " + nf.length());
                    } else {
                        logger.error("Exception : rename file " + fileName + " error.");
                    }
                } else {
                    rawFiles.add(f);
                }
            }
        }

        for (File f : rawFiles) {
            trainOne(f);
            System.gc();

            if (success.get() % 20 == 0) {
                logger.info("-----------------------------------------------");
                logger.info("calculate cnt = " + (success.get() + fail.size()));
                logger.info("success   cnt = " + success.get());
                logger.info("fail      cnt = " + fail.size());
                logger.info("-----------------------------------------------");
            }
        }

        logger.info("-------  Program is over  -------");
        logger.info("success cnt = " + success.get());
        logger.info("fail cnt    = " + fail.size());
    }

    // 训练一个文件
    public void trainOne(File f) {
        String filename = f.getName();
        int cateModel = 0;
        String[] result = null;
        Map<String, String[]> mapCPT = null;
        try {
            String city = filename.substring(0, 1);
            String cate = filename.substring(2, filename.length() - 4);

            logger.info("-----------------------------------");
            logger.info("city = " + city);
            logger.info("cate = " + cate);
            logger.info("-----------------------------------");

            if (cate.matches("\\d+")) {
                cateModel = Integer.parseInt(cate);
            }
            if (cateModel < 1) {
                fail.add(filename);
                return;
            }

            CSVLoader csvLoader = new CSVLoader();
            Instances organIns = null;
            csvLoader.setSource(f);
            organIns = csvLoader.getDataSet();

            // CSV文件名称及大小的日志，用户监控异常文件
            logger.info("File     = " + city + "_" + cateModel + ".csv");
            if ((f.length() / 1024 / 1024 / 1024) != 0)
                logger.info("Memory   = " + (f.length() / 1024 / 1024 / 1024) + " GB");
            else if ((f.length() / 1024 / 1024) != 0)
                logger.info("Memory   = " + (f.length() / 1024 / 1024) + " MB");
            else
                logger.info("Memeory  = " + (f.length() / 1024) + " KB");

            logger.info("numInstances = " + organIns.numInstances());

            // 检测是否所有的均为FALSE，即是否存在TURE
            boolean checkExistTrue = false;
            String[] strTrue = new String[] { "True", "TRUE", "true" };
            for (int ins = 0; ins < organIns.numInstances(); ins++) {
                if (checkExistTrue)
                    break;
                String strIns = organIns.instance(ins).toString();
                for (int i = 0; i < strTrue.length; i++) {
                    if (strIns.substring(strIns.length() - 4, strIns.length()).equals(strTrue[i])) {
                        checkExistTrue = true;
                        break;
                    }
                }
            }
            if (!checkExistTrue) {
                logger.error("There are all FALSE in this CateID : " + city + "_" + cate);
                fail.add(filename);
                return;
            }
            logger.info("Pass ExistTrueCheck");

            // 检测是否所有的均为TRUE，即是否存在FALSE
            boolean checkExistFalse = false;
            String[] strFalse = new String[] { "False", "FALSE", "false" };
            for (int ins = 0; ins < organIns.numInstances(); ins++) {
                if (checkExistFalse)
                    break;
                String strIns = organIns.instance(ins).toString();
                for (int i = 0; i < strFalse.length; i++) {
                    if (strIns.substring(strIns.length() - 5, strIns.length()).equals(strFalse[i])) {
                        checkExistFalse = true;
                        break;
                    }
                }
            }
            if (!checkExistFalse) {
                logger.error("There are all TRUE in this CateID : " + city + "_" + cate);
                fail.add(filename);
                return;
            }
            logger.info("Pass ExistFalseCheck");

            AttributeFilter impAttrFilter = new AttributeFilter();
            organIns = impAttrFilter.filter(organIns, PROBLEM_ATTRIBUTE);

            BayesNet impBayesNet = new BayesNet();
            organIns.setClassIndex(organIns.numAttributes() - 1);

            BuildAndTrainBayesNet batBayesNet = new BuildAndTrainBayesNet();
            impBayesNet = batBayesNet.buildAndTrain(impBayesNet, organIns);

            /**
             * 这里想要用write和read记录网络模型的中间结果
             * 最后决定在最后所有功能模块都顺利跑完后再写下网络模型更加合理
             */
            //			SerializationHelper.write(ConstantsUtil.BAYESNET_PATH + city + "_" + cateModel, impBayesNet);
            //			BayesNet iimpBayesNet = (BayesNet) weka.core.SerializationHelper.read(ConstantsUtil.BAYESNET_PATH + city + "_" + cateModel);

            CheckBayesNetInfo checkBNInfo = new CheckBayesNetInfo();
            String impGraph = checkBNInfo.checkInfo(impBayesNet);

            //			GetPredictDist getPDist = new GetPredictDist();
            //			double[] dist = getPDist.get(impBayesNet, organIns.instance(0));

            //            WriteMessage wtPredict = new WriteMessage();
            //            wtPredict.write(cateModel, dist[0], dist[1]);

            CalRightPercent calRP = new CalRightPercent();
            calRP.calculate(impBayesNet, organIns);

            try {
                // PickOutCPT
                PickOutCPT pickOutCPT = new PickOutCPT();
                mapCPT = pickOutCPT.getCPT(impGraph);
            } catch (Exception e) {
                logger.error("PickOutCPT error.");
            }

            try {
                // CalParam
                CalParam calParam = new CalParam();
                result = calParam.calculate(mapCPT);
            } catch (Exception e) {
                logger.error("CalParam error.");
            }

            try {
                // WriteMessage
                WriteMessage wtResult = new WriteMessage();
                wtResult.write(cateModel, result, city);
            } catch (Exception e) {
                logger.error("WriteMessage error.");
            }

            FileOutputStream fos = new FileOutputStream(new File(ConstantsUtil.BAYESNET_PATH + city + "_" + cateModel));
            SerializationHelper.write(fos, impBayesNet);
            //			SerializationHelper.write(ConstantsUtil.BAYESNET_PATH + city + "_" + cateModel, impBayesNet);

            success.getAndIncrement();
        } catch (Exception e) {
            fail.add(filename);
            logger.error(e);
        }
    }
}
