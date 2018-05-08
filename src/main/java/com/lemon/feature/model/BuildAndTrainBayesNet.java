package com.lemon.feature.model;

import org.apache.log4j.Logger;

import weka.classifiers.bayes.BayesNet;
import weka.core.Instances;

public class BuildAndTrainBayesNet {

    private final Logger logger = Logger.getLogger(BuildAndTrainBayesNet.class);

    /**
     * 创建并且训练贝叶斯网络
     * @param impBayesNet
     * @param queryType
     * @return impBayesNet
     */
    public BayesNet buildAndTrain(BayesNet impBayesNet, Instances organIns) {

        logger.info("Log : Train BayesNet start.");
        try {
            impBayesNet.buildClassifier(organIns);
            impBayesNet.initStructure();
            impBayesNet.buildStructure();
            impBayesNet.estimateCPTs();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Exception : build and train BayesNet error.", e);
        }
        logger.info("Log : Train BayesNet over.");
        return impBayesNet;
    }
}
