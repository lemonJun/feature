package com.lemon.feature.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

public class CsvFileFilter {

    private final Logger logger = Logger.getLogger(CsvFileFilter.class);
    private final double INS_PER_KB = 17.6289;

    /*
     * 超大CSV文件过滤器
     * @param file
     */
    public File filter(File file) {
        double MaxSize = 200 * 1024 * INS_PER_KB;
        long size = file.length();
        int rate = (int) (size / MaxSize);
        try {
            CSVReader reader = new CSVReader(new FileReader(file));
            try {
                //                while(reader)
                //                {
                String[] strs = reader.readNext();
                //                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.info("Error : Read File Exception.");
        }

        return file;
    }
}
