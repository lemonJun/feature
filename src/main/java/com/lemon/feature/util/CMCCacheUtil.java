package com.lemon.feature.util;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

public class CMCCacheUtil {

    private static final Logger logger = Logger.getLogger(CMCCacheUtil.class);

    public static final int TYPE_INFO = 1; //职位常量
    public static final int TYPE_RESUME = 2;//简历常量

    //薪资的统一标准
    public static final ConcurrentHashMap<String, Integer> salCache = new ConcurrentHashMap<String, Integer>();
    //学历的统一标准
    public static final ConcurrentHashMap<String, Integer> eduCache = new ConcurrentHashMap<String, Integer>();
    //1职位 2简历
    public static final ConcurrentHashMap<String, Integer> expCache = new ConcurrentHashMap<String, Integer>();

    static {
        intEdu();
        initSal();
        initExp();
    }

    /**
     * 把简历/职位的 教育程序 进行规一化处理
     * @param sal  当前数值
     * @param type 1职位  2简历
     * @return
     */
    public static int uniqueEdu(int edu, int type, long id) {
        if (TYPE_INFO != type && TYPE_RESUME != type) {
            return -1;
        }
        Integer result = 0;
        try {
            result = eduCache.get(String.format("%s_%s", type, edu));
        } catch (Exception e) {
            logger.error(String.format("edu: %s_%s_%s cant be unique", type, edu, id), e);
        }
        if (result == null) {
            logger.info(String.format("edu: %s_%s_%s cant be unique", type, edu, id));
            return 0;
        }
        return result;
    }

    /**
     * 把简历/职位的 工作经验 进行规一化处理
     * @param sal  当前数值
     * @param type 1职位  2简历
     * @return
     */
    public static int uniqueExp(int exp, int type, long id) {
        if (TYPE_INFO != type && TYPE_RESUME != type) {
            return -1;
        }
        Integer result = 0;
        try {
            result = expCache.get(String.format("%s_%s", type, exp));
        } catch (Exception e) {
            logger.error(String.format("exp: %s_%s_%s cant be unique", type, exp, id), e);
        }
        if (result == null) {
            logger.info(String.format("exp: %s_%s_%s cant be unique", type, exp, id));
            return 0;
        }
        return result;
    }

    /**
     * 把简历/职位的 薪水 进行规一化处理
     * @param salary
     * @param type 1职位  2简历
     * @return
     */
    public static int uniqueSalary(String salary, int type, long id) {
        Integer result = 0;
        try {
            if (TYPE_RESUME == type) {
                result = salCache.get(String.format("%s_%s", type, salary));
            } else {
                result = salCache.get(salary);
            }
        } catch (Exception e) {
            logger.error(String.format("sal: %s_%s_%s cant be unique", type, salary, id), e);
        }
        if (result == null) {
            logger.info(String.format("sal: %s_%s_%s cant be unique", type, salary, id));
            return 0;
        }
        return result;
    }

    /**
     * 把 年龄 进行规一化
     * @param date
     * @return
     */
    public static int uniqueAge(Date date) {
        int age = 0;
        if (date != null) {
            long d = (new Date().getTime() - date.getTime()) / (1000 * 60 * 60 * 24);
            age = (int) d / 365;
        }
        return age > 0 ? age : 1;
    }

    /**
    * 性别
    * @param gender
    * @return
    */
    public static int uniqueGender(boolean gender) {
        if (false == gender) {
            return 1;
        } else {
            return 0;
        }
    }

    //性别
    //男1   女0

    //
    //职位学历   5356
    //{ 1, 2, 3, 4, 5, 6, 7, 8 };
    //{ "不限", "高中", "技校", "中专", "大专", "本科", "硕士", "博士" };
    //简历学历
    //0:不限;1:高中以下;2:高中;3:中专/技校；4：大专;5:本科;6:硕士;7:博士;8:MBA/EMBA
    public static void intEdu() {

        eduCache.put("", 0);//不限
        eduCache.put("1_0", 0);//不限
        eduCache.put("1_1", 0);//不限
        eduCache.put("1_", 0);//不限
        eduCache.put("1_-1", 0);//不限

        eduCache.put("1_2", 2);//高中
        eduCache.put("1_3", 3);//技校
        eduCache.put("1_4", 3);//中专
        eduCache.put("1_5", 4);//大专
        eduCache.put("1_6", 5);//本科
        eduCache.put("1_7", 6);//硕士
        eduCache.put("1_8", 7);//博士

        eduCache.put("2_0", 0);//不限
        eduCache.put("2_", 0);//不限
        eduCache.put("2_-1", 0);//不限
        eduCache.put("2_1", 1);//高中以下
        eduCache.put("2_2", 2);//高中
        eduCache.put("2_3", 3);//中专
        eduCache.put("2_4", 4);//大专
        eduCache.put("2_5", 5);//本科
        eduCache.put("2_6", 6);//硕士
        eduCache.put("2_7", 7);//博士
        eduCache.put("2_8", 8);//MBA
        eduCache.put("2_9", 9);//这是啥啊 

    }

    //工作年限
    //职位 5357 
    //{1:不限,4:1年以下,5:1-2年,6:3-5年,7:6-7年,8:8-10年,9:10年以上}
    //简历
    //0:不限;1:1-3年,2:3-5年,3:5-10年,4:10年以上,5:应届生,6:1年以下

    public static void initExp() {
        expCache.put("1_1", 0);//不限
        expCache.put("1_0", 0);//不限
        expCache.put("1_-1", 0);//不限
        expCache.put("1_", 0);//不限
        expCache.put("1_4", 1);//1年以下
        expCache.put("1_5", 2);//1-3年
        expCache.put("1_6", 3);//3-5年
        expCache.put("1_7", 4);//5-10年
        expCache.put("1_8", 4);//5-10年
        expCache.put("1_9", 5);//10年以上

        expCache.put("2_0", 0);//不限
        expCache.put("2_-1", 0);//不限
        expCache.put("2_", 0);//不限
        expCache.put("2_6", 1);//1年以下
        expCache.put("2_7", 1);//1年以下
        expCache.put("2_5", 1);//1年以下
        expCache.put("2_1", 2);//1-3年
        expCache.put("2_2", 3);//3-5年
        expCache.put("2_3", 4);//5-10年
        expCache.put("2_4", 5);//10年以上

        expCache.put("", 0);//1年以下
    }

    //职位工资   5354
    // { "面议-面议", "1-999", "1000-2000", "2000-3000", "3000-5000", "5000-8000", "8000-12000", "12000-20000", "25000-25000" };
    // { "面议", "1000元以下", "1000-2000元", "2000-3000元", "3000-5000元", "5000-8000元", "8000-12000元", "12000-20000元", "20000-25000元", "25000元以上" };
    //{0_1000:1000元以下,1000_2000:1000-2000元,2000_3000:2000-3000元,3000_5000:3000-5000元,5000_7999:5000-8000元,8000_12000:8000-12000元,12000_20000:12000-20000元,20000_25000:20000-25000元,25000_999999:25000元以上}
    //简历工资
    //0:面议,1:1000以下,2:1000-2000,3:2000-3000,4:3000-5000,5:5000-8000,6:8000-12000,7:12000-20000,8:20000-25000,9:25000以上
    public static void initSal() {
        salCache.put("2_0", 0);
        salCache.put("2_1", 1);
        salCache.put("2_2", 2);
        salCache.put("2_3", 3);
        salCache.put("2_4", 4);
        salCache.put("2_5", 5);
        salCache.put("2_6", 6);
        salCache.put("2_7", 7);
        salCache.put("2_8", 8);
        salCache.put("2_9", 9);

        salCache.put("面议", 0);
        salCache.put("-", 0);
        salCache.put("_", 0);

        salCache.put("面议-面议", 0);
        salCache.put("面议_面议", 0);
        salCache.put("1000以下", 1);
        salCache.put("1-999", 1);
        salCache.put("1_999", 1);
        salCache.put("0_1000", 1);
        salCache.put("1000-2000", 2);
        salCache.put("1000_2000", 2);
        salCache.put("2000-3000", 3);
        salCache.put("2200-3000", 3);
        salCache.put("2200_3000", 3);
        salCache.put("2000_3000", 3);
        salCache.put("3000-5000", 4);
        salCache.put("3000_5000", 4);
        salCache.put("5000-8000", 5);
        salCache.put("5000_8000", 5);
        salCache.put("8000-12000", 6);
        salCache.put("8000_12000", 6);
        salCache.put("12000-20000", 7);
        salCache.put("12000_20000", 7);
        salCache.put("20000-25000", 8);
        salCache.put("20000_25000", 8);
        salCache.put("25000以上", 9);
        salCache.put("20000_30000", 9);
        salCache.put("25000",9);
        salCache.put("25000-25000", 9);
        salCache.put("25000_25000", 9);
        salCache.put("NaN", 0);
        salCache.put("", 0);
    }

}
