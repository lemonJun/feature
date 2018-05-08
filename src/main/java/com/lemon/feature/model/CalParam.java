package com.lemon.feature.model;

import java.util.Map;

import org.apache.log4j.Logger;

public class CalParam {

    private final Logger logger = Logger.getLogger(CalParam.class);
    private final String[] dims = new String[] { "iExp", "iLvl", "iPrice", "iAge", "iGender", "uLvl", "uExp", "ulat", "ulng", "uPrice", "isBuy" };

    /**
     * 提取贝叶斯网络的边缘分布概率并保存
     * @param mapCPT
     */
    public String[] calculate(Map<String, String[]> mapCPT) {
        String[] outcomes = mapCPT.get("outcomes");
        String[] table = mapCPT.get("table");
        String[] result = new String[10];

        // init 
        for (int i = 0; i < 10; i++)
            result[i] = "";

        // check mapCPT
        //        for (int dimi = 0; dimi < 10; dimi++) {
        //            logger.equals("  ---  Dim" + (dimi + 1) + " : " + dims[dimi] + "  ---  ");
        //            logger.equals(outcomes[dimi]);
        //            logger.equals(table[dimi]);
        //        }

        // change style & calculate
        for (int dimi = 0; dimi < 10; dimi++) {
            result[dimi] += dims[dimi] + " ";

            String[] range = outcomes[dimi].split(" ");
            String[] strScore = table[dimi].split(" ");

            int size = range.length;
            int indexMax = -1;
            double valueMax = -1;
            Double[] score = new Double[size];

            double weight = 0;
            for (int j = 0; j < size; j++) {
                range[j] = range[j].substring(1, range[j].length() - 1);
                score[j] = Double.parseDouble(strScore[j + size]);

                try {
                    if (range[j].equals("."))
                        range[j] = "All";
                    if (!range[j].equals("All")) {
                        range[j] = range[j].substring(1, range[j].length() - 1);
                        if (range[j].split("-").length == 3)
                            range[j] = range[j].substring(1, range[j].length());
                        if (range[j].substring(0, range[j].indexOf("-")).equals("inf"))
                            range[j] = "0" + range[j].substring(3, range[j].length());
                        if (range[j].substring(range[j].indexOf("-") + 1, range[j].length()).equals("inf"))
                            range[j] = range[j].substring(0, range[j].length() - 3) + "999";
                    }
                } catch (Exception e) {
                    logger.info(e);
                    logger.info(range[j]);
                    logger.info("wash inf Error.");
                }

                if (score[j] > valueMax) {
                    valueMax = score[j];
                    indexMax = j;
                }
                weight += score[j] * score[j];
            }
            weight = Math.sqrt(weight);
            if (weight == 1)
                weight = 0.5;

            // calculate : Range Suggestion
            result[dimi] += range[indexMax] + " ";

            // calculate : Weight Suggestion
            result[dimi] += weight;
        }

        return result;
    }
}
