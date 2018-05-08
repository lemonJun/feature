package com.lemon.feature.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.lemon.feature.util.ConstantsUtil;

import au.com.bytecode.opencsv.CSVWriter;

public class WritePredict {

    //    private final String predictAddr = "E:/58/Project.tuijian/listData/modData/mod";
    private static final Logger logger = Logger.getLogger(WritePredict.class);

    public void write(int KIND, double pF, double pT) {
        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new FileWriter(new File(ConstantsUtil.MODEL_PATH + KIND + ".csv")), ',');
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error : FileWriter open error", e);
        }
        List<String[]> alList = new ArrayList<String[]>();
        List<String> list = new ArrayList<String>();
        list.add("" + KIND);
        list.add((pF > pT) ? "FALSE" : "TRUE");
        list.add("" + pF);
        list.add("" + pT);
        alList.add(list.toArray(new String[list.size()]));

        writer.writeAll(alList);
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error : writer close error.", e);
        }
        logger.equals(writer.toString());
    }

    public void write(int KIND, String[] result) {
        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new FileWriter(new File(ConstantsUtil.MODEL_PATH + KIND + ".csv")), ',');
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error : FileWriter open error", e);
        }
        List<String[]> alList = new ArrayList<String[]>();

        for (int i = 0; i < result.length; i++) {
            String[] str = result[i].split(" ");
            List<String> list = new ArrayList<String>();
            list.add(str[0]);
            list.add(str[1]);
            list.add(str[2]);
            alList.add(list.toArray(new String[list.size()]));
        }
        writer.writeAll(alList);
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error : writer close error.", e);
        }
        logger.equals(writer.toString());
    }
}
