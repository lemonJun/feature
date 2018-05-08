package com.lemon.feature.model;

public class ModUpdate {

    private final String strLInf = "0";
    private final String strRInf = "999";

    /*
     * 增量迭代训练模型
     * @param strOld
     * @param strNew
     */
    public String update(String strOld, String strNew) {
        String[] strResult = new String[2];
        if (strOld.equals("All") || strOld.equals("."))
            return strNew;
        if (strNew.equals("All") || strNew.equals("."))
            return strOld;

        String[] strsOld = strOld.split("-");
        String[] strsNew = strNew.split("-");

        strResult[0] = (Double.parseDouble(strsOld[0]) > Double.parseDouble(strsNew[0])) ? strsOld[0] : strsNew[0];
        strResult[1] = (Double.parseDouble(strsOld[1]) > Double.parseDouble(strsNew[1])) ? strsOld[1] : strsNew[1];

        if (Double.parseDouble(strResult[0]) > Double.parseDouble(strResult[1]))
            return strNew;

        //        return "(" + strResult[0] + "-" + strResult[1] + ")";
        return (strResult[0] + "-" + strResult[1]);

        //        String[] strsOld = strOld.substring(1, strOld.length()-1).split("-");
        //        String[] strsNew = strNew.substring(1, strNew.length()-1).split("-");
        //        if(strsOld.length == 3 && strsNew.length == 3)
        //        {
        //            strResult[0] = strLInf;
        //            if(strsOld[2].equals(strRInf) && strsNew[2].equals(strRInf))
        //                strResult[1] = strRInf;
        //            else if(strsOld[2].equals(strRInf))
        //                strResult[1] = strsNew[2];
        //            else if(strsNew[2].equals(strRInf))
        //                strResult[1] = strsOld[2];
        //            else {
        //                strResult[1] = (Double.parseDouble(strsNew[2]) < Double.parseDouble(strsOld[2])) ? strsNew[2] : strsOld[2];
        //            }
        //        }
        //        else if(strsOld.length == 3)
        //        {
        //            strResult[0] = strsNew[0];
        //            if(strsOld[2].equals(strRInf) && strsNew[1].equals(strRInf))
        //                strResult[1] = strRInf;
        //            else if(strsOld[2].equals(strRInf))
        //                strResult[1] = strsNew[1];
        //            else if(strsNew[1].equals(strRInf))
        //                strResult[1] = strsOld[2];
        //            else {
        //                strResult[1] = (Double.parseDouble(strsNew[1]) < Double.parseDouble(strsOld[2])) ? strsNew[1] : strsOld[2];
        //            }
        //        }
        //        else if(strsNew.length == 3)
        //        {
        //            strResult[0] = strsOld[0];
        //            if(strsOld[1].equals(strRInf) && strsNew[2].equals(strRInf))
        //                strResult[1] = strRInf;
        //            else if(strsOld[1].equals(strRInf))
        //                strResult[1] = strsNew[2];
        //            else if(strsNew[2].equals(strRInf))
        //                strResult[1] = strsOld[1];
        //            else {
        //                strResult[1] = (Double.parseDouble(strsNew[2]) < Double.parseDouble(strsOld[1])) ? strsNew[2] : strsOld[1];
        //            }
        //        }
        //        else
        //        {
        //            strResult[0] = (Double.parseDouble(strsNew[0]) > Double.parseDouble(strsOld[0])) ? strsNew[0] : strsOld[0];
        //            if(strsOld[1].equals(strRInf) && strsNew[1].equals(strRInf))
        //                strResult[1] = strRInf;
        //            else if(strsOld[1].equals(strRInf))
        //                strResult[1] = strsNew[1];
        //            else if(strsNew[1].equals(strRInf))
        //                strResult[1] = strsOld[1];
        //            else {
        //                strResult[1] = (Double.parseDouble(strsNew[1]) < Double.parseDouble(strsOld[1])) ? strsNew[1] : strsOld[1];
        //            }
        //        }
        //        return ("(" + strResult[0] + "-" + strResult[1] + ")");

    }
}
