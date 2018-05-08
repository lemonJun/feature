package com.lemon.feature.model;

import org.apache.log4j.Logger;

import weka.classifiers.bayes.BayesNet;
import weka.core.Instance;

public class GetPredictDist {

    private static final Logger logger = Logger.getLogger(GetPredictDist.class);

    /**
     * 计算一个案例的正误判断的概率分布
     * @param impBayesNet
     * @param tempIns
     * @return dist
     */
    public double[] get(BayesNet impBayesNet, Instance tempIns) {
        double[] dist = null;
        try {
            dist = impBayesNet.distributionForInstance(tempIns);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Exception : disreibutionForInstance error.", e);
        }
        //        for (int i = 0; i < dist.length; i++)
        //            logger.equals("dist[" + i + "] = " + dist[i]);

        return dist;
    }
}
