package com.lemon.feature.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.lemon.feature.util.ConstantsUtil;
import com.lemon.feature.util.PathUtil;

public class CateFeatureCache {

    private static final Logger logger = Logger.getLogger(CateFeatureCache.class);

    public static ConcurrentHashMap<String, List<FeatureEntity>> infoFeatureCache = new ConcurrentHashMap<String, List<FeatureEntity>>();
    public static ConcurrentHashMap<String, List<FeatureEntity>> resumeFeatureCache = new ConcurrentHashMap<String, List<FeatureEntity>>();

    /**
     * 为职位获取它所感兴趣的简历的特征数组  如果缓存中没有值  则从文件中读取
     * @param cate
     * @param field
     * @param boost
     */
    public static List<FeatureEntity> forInfo(int cate) {
        List<FeatureEntity> entitys = infoFeatureCache.get(String.valueOf(cate));
        if (entitys == null) {
            entitys = new ArrayList<FeatureEntity>();
            try {
                List<String> lines = Files.readLines(new File(PathUtil.getCurrentPath() + File.separator + ConstantsUtil.MODEL_PATH + cate + ".csv"), Charsets.UTF_8);
                for (String line : lines) {
                    if (line == null || line.trim().length() < 1) {
                        continue;
                    }

                    String[] arrays = line.split(",");
                    if (arrays == null || arrays.length < 3) {
                        continue;
                    }
                    String field = quatoClear(arrays[0]);
                    String value = quatoClear(arrays[1]);
                    String boost = quatoClear(arrays[2]);
                    logger.info(field + "-" + value + "-" + boost);
                    if (field.startsWith("u")) {
                        continue;
                    }
                    FeatureEntity entity = new FeatureEntity();
                    entity.setField(field);
                    entity.setValue(value);
                    entity.setBoost(boost);
                    entitys.add(entity);
                }
                if (entitys.size() > 0) {
                    infoFeatureCache.put(String.valueOf(cate), entitys);
                }
                return entitys;
            } catch (IOException e) {
                logger.error(cate, e);
            }
        }
        return entitys;
    }

    public static List<FeatureEntity> forInfo(int city, int cate) {
        String key = String.format("%d_%d", city, cate);
        List<FeatureEntity> entitys = infoFeatureCache.get(key);
        if (entitys == null) {
            entitys = new ArrayList<FeatureEntity>();
            try {
                List<String> lines = Files.readLines(new File(PathUtil.getCurrentPath() + File.separator + ConstantsUtil.MODEL_PATH + key + ".csv"), Charsets.UTF_8);
                for (String line : lines) {
                    if (line == null || line.trim().length() < 1) {
                        continue;
                    }

                    String[] arrays = line.split(",");
                    if (arrays == null || arrays.length < 3) {
                        continue;
                    }
                    String field = quatoClear(arrays[0]);
                    String value = quatoClear(arrays[1]);
                    String boost = quatoClear(arrays[2]);
                    logger.info(field + "-" + value + "-" + boost);
                    if (field.startsWith("u")) {
                        continue;
                    }
                    FeatureEntity entity = new FeatureEntity();
                    entity.setField(field);
                    entity.setValue(value);
                    entity.setBoost(boost);
                    entitys.add(entity);
                }
                if (entitys.size() > 0) {
                    infoFeatureCache.put(key, entitys);
                }
                return entitys;
            } catch (IOException e) {
                logger.error(cate, e);
            }
        }
        return entitys;
    }

    /**
     * 为简历获取它所感兴趣的职位的特征数组   如果缓存中没有 则从文件中读取
     * @param cate
     * @return
     */
    public static List<FeatureEntity> forResume(int cate) {
        List<FeatureEntity> entitys = resumeFeatureCache.get(String.valueOf(cate));
        if (entitys == null) {
            entitys = new ArrayList<FeatureEntity>();
            try {
                List<String> lines = Files.readLines(new File(PathUtil.getCurrentPath() + File.separator + ConstantsUtil.MODEL_PATH + cate + ".csv"), Charsets.UTF_8);
                for (String line : lines) {
                    if (line == null || line.trim().length() < 1) {
                        continue;
                    }
                    String[] arrays = line.split(",");
                    if (arrays == null || arrays.length < 3) {
                        continue;
                    }
                    String field = quatoClear(arrays[0]);
                    String value = quatoClear(arrays[1]);
                    String boost = quatoClear(arrays[2]);

                    logger.info(field + "-" + value + "-" + boost);

                    if (field.startsWith("i")) {
                        continue;
                    }
                    FeatureEntity entity = new FeatureEntity();
                    entity.setField(field);
                    entity.setValue(value);
                    entity.setBoost(boost);
                    entitys.add(entity);
                }
                if (entitys.size() > 0) {
                    resumeFeatureCache.put(String.valueOf(cate), entitys);
                }
                return entitys;
            } catch (IOException e) {
                logger.error(cate, e);
            }
        }
        return entitys;
    }

    public static List<FeatureEntity> forResume(int city, int cate) {
        String key = String.format("%d_%d", city, cate);
        List<FeatureEntity> entitys = resumeFeatureCache.get(key);
        if (entitys == null) {
            entitys = new ArrayList<FeatureEntity>();
            try {
                List<String> lines = Files.readLines(new File(PathUtil.getCurrentPath() + File.separator + ConstantsUtil.MODEL_PATH + key + ".csv"), Charsets.UTF_8);
                for (String line : lines) {
                    if (line == null || line.trim().length() < 1) {
                        continue;
                    }
                    String[] arrays = line.split(",");
                    if (arrays == null || arrays.length < 3) {
                        continue;
                    }
                    String field = quatoClear(arrays[0]);
                    String value = quatoClear(arrays[1]);
                    String boost = quatoClear(arrays[2]);

                    logger.info(field + "-" + value + "-" + boost);

                    if (field.startsWith("i")) {
                        continue;
                    }
                    FeatureEntity entity = new FeatureEntity();
                    entity.setField(field);
                    entity.setValue(value);
                    entity.setBoost(boost);
                    entitys.add(entity);
                }
                if (entitys.size() > 0) {
                    resumeFeatureCache.put(key, entitys);
                }
                return entitys;
            } catch (IOException e) {
                logger.error(cate, e);
            }
        }
        return entitys;
    }

    //去除csv文件中 每个字段的""
    private static String quatoClear(String content) {
        if (content.startsWith("\"")) {
            content = content.substring(1, content.length());
        }
        if (content.endsWith("\"")) {
            content = content.substring(0, content.length() - 1);
        }
        return content;
    }
}
