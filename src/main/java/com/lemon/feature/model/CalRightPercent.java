package com.lemon.feature.model;

import org.apache.log4j.Logger;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.core.Instance;
import weka.core.Instances;

public class CalRightPercent {

    private final Logger logger = Logger.getLogger(CalRightPercent.class);

    /**
     * 计算训练后的贝叶斯网络的预测正确率
     * @param impBayesNet
     * @param organIns
     * @return null
     */
    public void calculate(BayesNet impBayesNet, Instances organIns) {
        Instance testIns;
        Evaluation testingEvaluation = null;
        try {
            testingEvaluation = new Evaluation(organIns);
            int length = organIns.numInstances();
            for (int i = 0; i < length; i++) {
                testIns = organIns.instance(i);
                testingEvaluation.evaluateModelOnceAndRecordPrediction(impBayesNet, testIns);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.info("————————————————   Right Rate    ————————————————");
        logger.info("Rate : " + (1 - testingEvaluation.errorRate()));
    }
}
