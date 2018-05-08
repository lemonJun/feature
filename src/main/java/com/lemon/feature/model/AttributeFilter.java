package com.lemon.feature.model;

import org.apache.log4j.Logger;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class AttributeFilter {

    private static final Logger logger = Logger.getLogger(AttributeFilter.class);

    /*
     * 数据属性清洗：调用weka自带的Filter清洗训练中不适用的属性
     * @param ins
     * @param attr
     */
    public Instances filter(Instances ins, int[] attr) {
        Instances washedIns = new Instances(ins);

        // set Filter
        Remove rmFilter = new Remove();
        rmFilter.setAttributeIndicesArray(attr);
        rmFilter.setInvertSelection(false);

        try {
            rmFilter.setInputFormat(ins);
            washedIns = Filter.useFilter(ins, rmFilter);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error : rmFilter.useFilter", e);
        }
        return washedIns;
    }
}
