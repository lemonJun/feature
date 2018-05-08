package com.lemon.feature.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

public class GrepFile {

    private static final Logger logger = Logger.getLogger(TrainModelBootStrap.class);

    private static final int lineMAX = 4890000;

    public static File rewriteFile(File f, String dirpath, String fileName) throws IOException {

        File nf = null;

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(f));
        } catch (Exception e) {
            logger.info("Exception : FileReader open error.");
        }
        if (reader == null) {
            logger.error("reader is null");
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(new File(dirpath + fileName)));
        } catch (Exception e) {
            logger.info("Exception : FileWriter open error.");
        }
        if (writer == null) {
            logger.error("writer is null");
        }
        String readLine = null;
        int line = 0;
        while ((readLine = reader.readLine()) != null) {
            if (line >= lineMAX) {
                break;
            }
            try {
                writer.write(readLine);
                writer.newLine();
            } catch (IOException e) {
                logger.info("Exception : writeLine error.");
            }
            //            logger.info("line " + line + " -> " + fileName + " write " + readLine);
            line++;
        }

        try {
            reader.close();
        } catch (IOException e) {
            logger.info("Exception : reader close error.");
        }
        try {
            writer.close();
        } catch (IOException e) {
            logger.info("Exception : writer close error.");
        }
        nf = new File(dirpath + fileName);
        return nf;
    }
}
