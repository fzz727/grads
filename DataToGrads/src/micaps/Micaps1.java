/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package micaps;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Joeff
 */
public class Micaps1 {

    public void ConvertToGrads(String strMicapsFile, String strGradsFile) throws Exception {

        List<String> lines = FileUtils.readLines(new File(strMicapsFile));

        FileOutputStream stream = FileUtils.openOutputStream(new File(strGradsFile));
        //FileOutputStream stream2 = FileUtils.openOutputStream(new File(strGradsFile+".txt"));
//
//        stream.write("12345678".getBytes());
//        stream.write(FloatUtil.FloatToByteArray(0f));
//        stream.write(FloatUtil.FloatToByteArray(0f));
//        stream.write(FloatUtil.FloatToByteArray(0f));
//        stream.write(FloatUtil.IntegerToByteArray(0));
//        stream.write(FloatUtil.IntegerToByteArray(1));

        for (Iterator<String> it = lines.iterator(); it.hasNext();) {
            String line = it.next().trim();

            String[] data = line.split(" +");

            // 跳过非数据行
            if (data.length != 5) {
                continue;
            }

            // 跳过缺测数据
            if (Double.parseDouble(data[4]) == 9999.0D) {
                continue;
            }

            //write(30)sta(num),ylat(num),xlon(num),tim,ilev,nflag  (每个时次的结束)
            stream.write((data[0] + "       ").substring(0, 8).getBytes());
            stream.write(FloatUtil.FloatToByteArray(Float.parseFloat(data[2])));
            stream.write(FloatUtil.FloatToByteArray(Float.parseFloat(data[1])));
            stream.write(FloatUtil.FloatToByteArray(0f));
            stream.write(FloatUtil.IntegerToByteArray(1));
            stream.write(FloatUtil.IntegerToByteArray(1));

            stream.write(FloatUtil.FloatToByteArray(Float.parseFloat(data[4])));

        }

        stream.write("12345   ".getBytes());
        stream.write(FloatUtil.FloatToByteArray(0f));
        stream.write(FloatUtil.FloatToByteArray(0f));
        stream.write(FloatUtil.FloatToByteArray(0f));
        stream.write(FloatUtil.IntegerToByteArray(0));
        stream.write(FloatUtil.IntegerToByteArray(1));

        stream.close();
    }

    public void CreateMapFile(String strMapFile) throws Exception {

        FileOutputStream stream = FileUtils.openOutputStream(new File(strMapFile));

        for (int i = 0; i < 80; i++) {
            for (int j = 0; j < 60; j++) {
                stream.write(FloatUtil.FloatToByteArray(1f));
            }
        }

        stream.close();

    }
}
