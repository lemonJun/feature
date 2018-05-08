package com.lemon.feature.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.lemon.feature.util.ConstantsUtil;
import com.lemon.feature.util.PathUtil;

public class PickOutCPT {

    private static final Logger logger = Logger.getLogger(PickOutCPT.class);

    /*
     * 解析CPT
     * @param graphInfo
     */
    public Map<String, String[]> getCPT(String graphInfo) {

        try {
            //            FileWriter fr = new FileWriter(new File(PathUtil.getCurrentPath() + "/" + ConstantsUtil.GRAPH_FILE));
            FileWriter fr = new FileWriter(new File(ConstantsUtil.GRAPH_FILE));
            BufferedWriter br = new BufferedWriter(fr);
            br.write(graphInfo);
            //            logger.info(graphInfo);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        Map<String, String[]> result = null;
        //	     try {
        //	      parse(graphInfo);
        //	     } catch (Exception e) {
        //	      e.printStackTrace();
        //	     }

        InputStream in;
        try {
            String word = "";
            StringBuffer sb = new StringBuffer();
            //            FileReader f = new FileReader(PathUtil.getCurrentPath() + "/" + ConstantsUtil.GRAPH_FILE);
            FileReader f = new FileReader(ConstantsUtil.GRAPH_FILE);
            BufferedReader br = new BufferedReader(f);
            try {
                while ((word = br.readLine()) != null) {
                    sb.append(word);
                }
                // content用来模拟传入的字符串
                String content = sb.toString();

                // 解析xml
                //	      result = parse(content);
                result = parse(graphInfo);

                String[] outcomes = result.get("outcomes");
                String[] tables = result.get("table");

                for (int i = 0; i < tables.length; i++) {
                    logger.equals(outcomes[i]);
                    logger.equals(tables[i]);
                    logger.equals("------------------");
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("Error : FileNotFound.");
        }
        return result;
    }

    public static Map<String, String[]> parse(String graphInfo) throws Exception {
        SAXReader saxReader = null;
        Document document = null;
        List<String> outcomes = new ArrayList<String>();
        List<String> tables = new ArrayList<String>();
        try {
            saxReader = new SAXReader();
            document = saxReader.read(new ByteArrayInputStream(graphInfo.getBytes("UTF-8")));
            List<Element> variables = document.selectNodes("/BIF/NETWORK/VARIABLE");
            List<Element> definitions = document.selectNodes("/BIF/NETWORK/DEFINITION");
            for (int i = 0; i < variables.size(); i++) {
                // 解析VARIABLE
                List<Element> variable = variables.get(i).elements();
                StringBuffer sb = new StringBuffer();
                for (int j = 0; j < variable.size(); j++) {
                    Element var = variable.get(j);
                    if (var.getName().equals("OUTCOME")) {
                        sb.append(var.getTextTrim()).append(" ");
                    }
                }
                outcomes.add(sb.toString());

                // 解析DEFINITION
                List<Element> definition = definitions.get(i).elements();
                Map<String, String> defMap = new HashMap<String, String>();
                for (int j = 0; j < definition.size(); j++) {
                    Element elem = definition.get(j);
                    if (elem.getName().equals("TABLE")) {
                        tables.add(elem.getTextTrim());
                    }
                }
            }

            String[] outcomeArray = new String[outcomes.size()];
            outcomes.toArray(outcomeArray);
            String[] tableArray = new String[tables.size()];
            tables.toArray(tableArray);
            Map<String, String[]> result = new HashMap<String, String[]>();
            result.put("outcomes", outcomeArray);
            result.put("table", tableArray);
            return result;
        } catch (Throwable e) {
            logger.info("PickOutCPT error!");
            throw new RuntimeException(e);
        } finally {
            if (document != null) {
                document.clearContent();
            }
        }
    }
}
