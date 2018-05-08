package com.lemon.feature.model;

import org.apache.log4j.Logger;

import weka.classifiers.bayes.BayesNet;
import weka.estimators.Estimator;

public class CheckBayesNetInfo {

    private static final Logger logger = Logger.getLogger(CheckBayesNetInfo.class);

    /**
     * 监测贝叶斯网络的基本信息，确保网络正常
     * @param impBayesNet
     * @return impGraph
     */
    public String checkInfo(BayesNet impBayesNet) {
        String impGraph = null;

        // BayesNet description
        //        logger.info("******************************");
        //        logger.info("estimatorTipText :");
        //        logger.info("******************************");
        //        logger.info(impBayesNet.estimatorTipText());
        //        logger.info();
        //        logger.info("******************************");
        //        logger.info("graph :");
        //        logger.info("******************************");
        try {
            //            logger.info(impBayesNet.graph());
            impGraph = impBayesNet.graph();
            //            logger.info();
        } catch (Exception e) {
            logger.error("CheckBayesNetInfo failed ", e);
        }

        //        logger.info("******************************");
        //        logger.info("getDistributions :");
        //        logger.info("******************************");
        //        logger.info(impBayesNet.getDistributions());
        Estimator[][] impDist = impBayesNet.getDistributions();
        //        for(int i=0; i<impDist.length; i++)
        //        {
        //            for(int j=0; j<impDist[i].length; j++)
        //                System.out.print("   " + impDist[i][j]);
        //            logger.info();
        //        }
        //        logger.info();
        return impGraph;
    }
}
