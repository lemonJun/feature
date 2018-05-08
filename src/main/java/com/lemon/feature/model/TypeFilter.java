package com.lemon.feature.model;

import org.apache.log4j.Logger;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.RemoveWithValues;

/**
 * @param organIns
 * @param keyAttr
 * @return partIns
 */

public class TypeFilter {

    // JOB TYPE RANGE : 2076~3231
    private final int RANGE_FRONT = 2076;
    private final int RANGE_REAR = 3231;
    // neighbourhood theta
    private final double THETA = 0.001;
    private static final Logger logger = Logger.getLogger(TypeFilter.class);

    /*
     * 调用weka中的Instance过滤器，去除指定keyAttr的Instance
     * @param organIns
     * @param keyAttr
     */
    public Instances[] filter(Instances organIns, int keyAttr) {
        // create partIns
        Instances[] partIns = new Instances[RANGE_REAR + 10];
        for (int i = RANGE_FRONT; i <= RANGE_REAR; i++)
            partIns[i] = new Instances(organIns);

        Instances washedIns = null;
        Instances halfWashedIns = null;

        // set Filter
        RemoveWithValues rwvLowFilter = new RemoveWithValues();
        //        rwvLowFilter.setAttributeIndex(JOB_TYPE_INDEX);
        rwvLowFilter.setAttributeIndex("" + keyAttr);
        rwvLowFilter.setDontFilterAfterFirstBatch(false);
        rwvLowFilter.setInvertSelection(false);
        rwvLowFilter.setNominalIndices("first-last");
        rwvLowFilter.setMatchMissingValues(false);
        rwvLowFilter.setModifyHeader(false);

        RemoveWithValues rwvHighFilter = new RemoveWithValues();
        //        rwvHighFilter.setAttributeIndex(JOB_TYPE_INDEX);
        rwvHighFilter.setAttributeIndex("" + keyAttr);
        rwvHighFilter.setDontFilterAfterFirstBatch(false);
        rwvHighFilter.setInvertSelection(true);
        rwvHighFilter.setNominalIndices("first-last");
        rwvHighFilter.setMatchMissingValues(false);
        rwvHighFilter.setModifyHeader(false);

        for (int splitNum = RANGE_FRONT; splitNum <= RANGE_REAR; splitNum++) {
            rwvLowFilter.setSplitPoint(splitNum - THETA);
            try {
                rwvLowFilter.setInputFormat(organIns);
                halfWashedIns = Filter.useFilter(organIns, rwvLowFilter);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Error : rwvLowFilter.useFilter", e);
            }

            rwvHighFilter.setSplitPoint(splitNum + THETA);
            try {
                rwvHighFilter.setInputFormat(halfWashedIns);
                washedIns = Filter.useFilter(halfWashedIns, rwvHighFilter);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Error : rwvHighFilter.useFilter", e);
            }
            partIns[splitNum] = washedIns;
        }
        return partIns;
    }
}
