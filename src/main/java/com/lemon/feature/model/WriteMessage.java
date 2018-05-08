package com.lemon.feature.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.lemon.feature.util.ConstantsUtil;
import com.lemon.feature.util.PathUtil;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * @param csvData
 * @return csvData with Title
 */

public class WriteMessage {

    //    private final String predictAddr = "E:/58/Project.tuijian/listData/modData/mod";
    private static final Logger logger = Logger.getLogger(WriteMessage.class);

    /*
     * 职位类型以及正误概率的写方式
     * @param KIND
     * @param pF
     * @param pT
     */
    public void write(int KIND, double pF, double pT) {
        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new FileWriter(new File(PathUtil.getCurrentPath() + ConstantsUtil.MODEL_PATH + KIND + ".csv")), ',');
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
        logger.info(writer.toString());
    }

    /*
     * 维度、推荐范围、权重的写方式
     * @param KIND
     * @param result
     */
    public void write(int KIND, String[] result, String city) {
        // 模型历史数据读取

        File dir = new File(PathUtil.getCurrentPath() + File.separator + ConstantsUtil.MODEL_PATH);
        File[] modFile = dir.listFiles();
        String[] value = new String[15];
        for (int i = 0; i < 14; i++) {
            value[i] = "";
        }
        boolean fileExist = false;
        for (File f : modFile) {
            if (f.getName().equals(city + "_" + KIND + ".csv")) {
                fileExist = true;
                try {
                    int countLine = 0;
                    List<String> modLines = Files.readLines(f, Charsets.UTF_8);
                    for (String line : modLines) {
                        if (line == null || line.trim().length() < 1) {
                            logger.info("continue");
                            continue;
                        }

                        String[] arrays = line.split(",");
                        if (arrays == null || arrays.length < 3) {
                            continue;
                        }
                        value[countLine] = arrays[1].substring(1, arrays[1].length() - 1);
                        //						logger,info(value[countLine]);
                        countLine++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error("Error : descript modFile.", e);
                }
                break;
            }
        }

        // 写入更新模型
        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new FileWriter(new File(PathUtil.getCurrentPath() + File.separator + ConstantsUtil.MODEL_PATH + city + "_" + KIND + ".csv")), ',');
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error : FileWriter open error", e);
        }
        List<String[]> alList = new ArrayList<String[]>();

        ModUpdate modUpdate = new ModUpdate();

        for (int i = 0; i < result.length; i++) {
            String[] str = result[i].split(" ");
            List<String> list = new ArrayList<String>();
            list.add(str[0]);

            logger.info("Log -> Name   = " + str[0]);

            try {
                if (fileExist) {
                    list.add(modUpdate.update(value[i], str[1]));
                    logger.info("Log -> Range  = " + modUpdate.update(value[i], str[1]));
                } else {
                    list.add(str[1]);
                    logger.info("Log -> Range  = " + str[1]);
                }
            } catch (Exception e) {
                logger.info("modUpdate Error.");
                logger.info(e);
            }
            list.add(str[2]);
            logger.info("Log -> Weight = " + str[2]);

            alList.add(list.toArray(new String[list.size()]));
        }
        writer.writeAll(alList);
        logger.info(writer.toString());
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error : writer close error.", e);
        }
    }
}
