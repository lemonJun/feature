package com.lemon.feature.model;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

import weka.classifiers.bayes.BayesNet;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

public class GetFeatureByInfoCate {
    // JOB TYPE RANGE : 2076~3231
    private final int RANGE_FRONT = 2076;
    private final int RANGE_REAR = 3231;
    // JOB TYPE
    private static final int KIND = 2164;
    // Absolute ListData Address
    private static final String ABSOLUTE_LISTDATA_ADDR = "E:/58/Project.tuijian/listData/logData/";
    // Problem Attribute need washed
    private static final int[] PROBLEM_ATTRIBUTE = new int[] { 0, 1, 2 };
    private static final Logger logger = Logger.getLogger(GetFeatureByInfoCate.class);

    /*
     * 测试类
     */
    public void get() {
        // input csvData
        CSVLoader csvLoader = new CSVLoader();
        Instances organIns = null;
        try {
            csvLoader.setSource(new File(ABSOLUTE_LISTDATA_ADDR + KIND + ".csv"));
            organIns = csvLoader.getDataSet();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // partIns split into different job type
        //     TypeFilter impTypeFilter = new TypeFilter();
        //     Instances[] partIns = impTypeFilter.filter(organIns, 2);

        // organIns without useless Attribute
        AttributeFilter impAttrFilter = new AttributeFilter();
        organIns = impAttrFilter.filter(organIns, PROBLEM_ATTRIBUTE);

        // create BayesNet and index
        BayesNet impBayesNet = new BayesNet();
        organIns.setClassIndex(organIns.numAttributes() - 1);

        // build and train network
        BuildAndTrainBayesNet batBayesNet = new BuildAndTrainBayesNet();
        batBayesNet.buildAndTrain(impBayesNet, organIns);

        // check BayesNet information
        CheckBayesNetInfo checkBNInfo = new CheckBayesNetInfo();
        String impGraph = checkBNInfo.checkInfo(impBayesNet);

        // calculate predict
        GetPredictDist getPDist = new GetPredictDist();
        double[] dist = getPDist.get(impBayesNet, organIns.instance(0));

        // write predict and judge
        WritePredict wtPredict = new WritePredict();
        wtPredict.write(KIND, dist[0], dist[1]);

        // calculate & print right percent
        CalRightPercent calRP = new CalRightPercent();
        calRP.calculate(impBayesNet, organIns);

        // PickOutCPT
        PickOutCPT pickOutCPT = new PickOutCPT();
        Map<String, String[]> mapCPT = pickOutCPT.getCPT(impGraph);

        // CalParam
        CalParam calParam = new CalParam();
        String[] result = calParam.calculate(mapCPT);

        logger.info("____________________________");
        logger.info("___________Result___________");
        logger.info("____________________________");
        for (int i = 0; i < result.length; i++)
            logger.info(result[i]);

        wtPredict.write(KIND, result);

        logger.info("Process is over.");
    }
}
