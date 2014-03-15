/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package micaps;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;

/**
 *MICAPS文件转换基类，把文件内容读入一个数组中
 * @author 张洲峰
 */
public class MicapsBase {

    protected String micapsFile;
    protected String diamond;
    protected int index;
    protected String title;
    protected Date date;
    protected boolean loaded = false;

    public String getMicapsFile() {
        return micapsFile;
    }

    public Date getDate() {
        return date;
    }

    public String getDiamond() {
        return diamond;
    }

    public int getIndex() {
        return index;
    }

    public String getTitle() {
        return title;
    }

    public boolean isLoaded() {
        return loaded;
    }

    /***
     * 把文件读入一个数据组，并获取公用的参数
     * @param strMicapsFile
     * @return
     */
    protected String[] OpenAsArray(String strMicapsFile) {
        this.micapsFile = strMicapsFile;

        // 先把文件读入到系统中，写入到一个字符串数组中，后面根据文件格式说明对文件内容进行解析处理
        List<String> strFileContent = null;
        try {
            strFileContent = FileUtils.readLines(new File(this.micapsFile));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        StringBuilder strContent = new StringBuilder();
        for (int i = 0; i < strFileContent.size(); i++) {
            String str = strFileContent.get(i);
            if (i > 0 && str.trim().toLowerCase().startsWith("diamond")) {
                continue;
            }
            if (!str.isEmpty()) {
                if (i > 0) {
                    if  ((str.indexOf( "E-" ) == -1) && (str.indexOf( "e-" ) == -1)&&(str.indexOf( "E+" ) == -1) && (str.indexOf( "e+" ) == -1)){
                        str = str.replaceAll("-", " -"); //处理如下情况：“-1234.35-3456.34”
                        //处理如下情况：“123ABC”
                        String reg = "([\\d|\\.|\\-]+)([A-Za-z]+)";
                        Matcher m = Pattern.compile(reg).matcher(str);
                        if (m.find()) {
                            str = m.replaceAll("$1 $2");
                        }
                    }
                }
                //str=Pattern.compile("(\\d)(-)(\\d)").matcher(str).replaceAll("$1 -$3");
                strContent.append(str.trim() + " ");
            }
        }


        return strContent.toString().trim().split("\\s+");
    }
}
