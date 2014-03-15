/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package micaps;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;
import ucar.ma2.ArrayFloat;
import ucar.ma2.DataType;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFileWriteable;

/**
 *MICAPS类型dimond 4的转换类
 * @author 张洲峰
 */
public class Micaps4 extends MicapsBase implements IMicaps {

    private int timeLimit;  // 时效
    private String level;   // 时次
    private double deltLon; // 经度格距
    private double deltLat; // 纬度格距
    private double startLon;// 起始经度
    private double endLon;  // 终止经度
    private double startLat;// 起始纬度
    private double endLat;  // 终止纬度
    private int latCount;   // 经线格点数量
    private int lonCount;   // 纬线格点数量
    private double courInteval;// 等值线间隔
    private double courStart;  // 等值线起点
    private double courEnd;    // 等值线终点
    private double smooth;      // 平滑系数
    private double blod;        // 加粗线
    //
    private double[] gridData;

    public double getBlod() {
        return blod;
    }

    public double getCourEnd() {
        return courEnd;
    }

    public double getCourInteval() {
        return courInteval;
    }

    public double getCourStart() {
        return courStart;
    }

    public double getDeltLat() {
        return deltLat;
    }

    public double getDeltLon() {
        return deltLon;
    }

    public double getEndLat() {
        return endLat;
    }

    public double getEndLon() {
        return endLon;
    }

    public double[] getGridData() {
        return gridData;
    }

    public int getLatCount() {
        return latCount;
    }

    public String getLevel() {
        return level;
    }

    public int getLonCount() {
        return lonCount;
    }

    public double getSmooth() {
        return smooth;
    }

    public double getStartLat() {
        return startLat;
    }

    public double getStartLon() {
        return startLon;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    /***
     *加载数据文件
     * @param strMicapsFile 数据文件路径
     * @return
     */
    public boolean load(String strMicapsFile) {

        // diamond  4  屏幕上需显示的内容  年  月  日  时次  时效  层次
        // 经度格距  纬度格距  起始经度  终止经度  起始纬度  终止纬度
        // X-DIM  Y-DIM  等值线间隔  等值线起始值  终止值  平滑系数
        // 加粗线值

        // 先把文件读入到系统中，写入到一个字符串数组中，后面根据文件格式说明对文件内容进行解析处理
        String[] strArray = OpenAsArray(strMicapsFile);
        if (strArray == null) {
            return false;
        }

        // 计数器
        int n = 0;
        // 第四类数据格式：用于修改后结果
        // diamond  4  屏幕上需显示的内容  年  月  日  时次  时效  层次
        this.diamond = strArray[0];
        this.index = Integer.parseInt(strArray[1]);
        this.title = strArray[2];
        //this.date = MicapsUtil.ConvertToDate(strArray[3], strArray[4], strArray[5], strArray[6]);
        this.timeLimit = Integer.parseInt(strArray[7]);
        this.level = strArray[8];

        n = 9;
        // 经度格距  纬度格距  起始经度  终止经度  起始纬度  终止纬度
        this.deltLon = Double.parseDouble(strArray[n++]);
        this.deltLat = Double.parseDouble(strArray[n++]);
        this.startLon = Double.parseDouble(strArray[n++]);
        this.endLon = Double.parseDouble(strArray[n++]);
        this.startLat = Double.parseDouble(strArray[n++]);
        this.endLat = Double.parseDouble(strArray[n++]);

        this.lonCount = Integer.parseInt(strArray[n++]);
        this.latCount = Integer.parseInt(strArray[n++]);

        // 等值线间隔  等值线起始值  终止值  平滑系数 加粗线值
        this.courInteval = Double.parseDouble(strArray[n++]);
        this.courStart = Double.parseDouble(strArray[n++]);
        this.courEnd = Double.parseDouble(strArray[n++]);
        this.smooth = Double.parseDouble(strArray[n++]);
        this.blod = Double.parseDouble(strArray[n++]);

        //this.latCount = (int) Math.abs((this.startLat - this.endLat) / this.deltLat) + 1;
        //this.lonCount = (int) Math.abs((this.startLon - this.endLon) / this.deltLon) + 1;

        // 处理完边界数据后，开始进行数据的赋值
        // 根据起始和结束数值大小，来选择循环的方式
        // 记录总经纬数，外循环经度变更，内循环纬度变更，记录步长

        int slon, slat, elon, elat; // 循环的范围
        int oStep, aStep;           // 循环的步长

        // 计算纬度步长
        if (this.startLat < this.endLat) {
            slat = 0;
            elat = this.latCount;
            aStep = 1;
        } else {
            slat = this.latCount - 1;
            elat = -1;
            aStep = -1;
        }
        // 计算经度步长
        if (this.startLon < this.endLon) {
            slon = 0;
            elon = this.lonCount;
            oStep = 1;
        } else {
            slon = this.lonCount - 1;
            elon = -1;
            oStep = -1;
        }

        // 将数据部分载入。
        // 这个载入的顺序是固定的，通过上面的处理，
        // 所有的数据载入后的组织方式都是从低经度到高经度，从低纬度到高纬度  
        this.gridData = new double[this.latCount * this.lonCount];
        int i, j;                   // 循环的记录变量
        i = slat;
        try {
            while (i != elat) {
                j = slon;
                while (j != elon) {
                    //
                    this.gridData[i * this.lonCount + j] = Double.parseDouble(strArray[n++]);
                    j += oStep;
                }
                i += aStep;
            }
        } catch (Exception e) {
            String temp = e.getMessage();
            e.printStackTrace();
        }

        // 数据载入完成后，对高低经纬度信息进行交换处理，保证数据的组织顺序
        if (this.startLat > this.endLat) {
            double tmp = this.startLat;
            this.startLat = this.endLat;
            this.endLat = tmp;
        }
        if (this.startLon > this.endLon) {
            double tmp = this.startLon;
            this.startLon = this.endLon;
            this.endLon = tmp;
        }
        // 保证步长是正数
        this.deltLat = Math.abs(this.deltLat);
        this.deltLon = Math.abs(this.deltLon);

        this.loaded = true;
        return this.loaded;
    }

    /***
     *处理后保存数据转换后的数据文件
     * @param strNcFile 保存文件的路径
     * @return
     */
    public boolean SaveAsNC(String strNcFile) {
        // 固定的文件处理
        if (!this.loaded) {
            return false;
        }

        NetcdfFileWriteable ncFile = null;
        try {
            ncFile = NetcdfFileWriteable.createNew(strNcFile, false);
            ncFile.create();
            ncFile.setRedefineMode(true);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        // 添加全局属性，记录各类型现行的数量
        //    protected String diamond;
        //    protected int index;
        ncFile.addGlobalAttribute("ID", this.diamond + " " + String.valueOf(this.getIndex()));
        //    protected String title;
        ncFile.addGlobalAttribute("Title", this.getTitle());
        //    protected Date date;
        ncFile.addGlobalAttribute("Date", this.getDate().toString());
        //    private int timeLimit;  // 时效
        ncFile.addGlobalAttribute("TimeLimit", this.getTimeLimit());
        //    private String level;   // 时次
        ncFile.addGlobalAttribute("Level", this.getLevel());
        //    private double deltLon; // 经度格距
        ncFile.addGlobalAttribute("DeltLon", this.getDeltLon());
        //    private double deltLat; // 纬度格距
        ncFile.addGlobalAttribute("DeltLat", this.getDeltLat());
        //    private double startLon;// 起始经度
        ncFile.addGlobalAttribute("sLon", this.getStartLon());
        //    private double endLon;  // 终止经度
        ncFile.addGlobalAttribute("eLon", this.getEndLon());
        //    private double startLat;// 起始纬度
        ncFile.addGlobalAttribute("sLat", this.getStartLat());
        //    private double endLat;  // 终止纬度
        ncFile.addGlobalAttribute("eLon", this.getEndLon());
        //    private int latCount;   // 经线格点数量
        ncFile.addGlobalAttribute("LonCount", this.getLonCount());
        //    private int lonCount;   // 纬线格点数量
        ncFile.addGlobalAttribute("LatCount", this.getLatCount());
        //    private double courInteval;// 等值线间隔
        ncFile.addGlobalAttribute("Inteval", this.getCourInteval());
        //    private double courStart;  // 等值线起点
        ncFile.addGlobalAttribute("CourStart", this.getCourStart());
        //    private double courEnd;    // 等值线终点
        ncFile.addGlobalAttribute("CourEnd", this.getCourEnd());
        //    private double smooth;      // 平滑系数
        ncFile.addGlobalAttribute("Smooth", this.getSmooth());
        //    private double blod;        // 加粗线
        ncFile.addGlobalAttribute("Blod", this.getBlod());

        // 添加经纬度 维度坐标
        Dimension latDim = ncFile.addDimension("latitude", this.getLatCount());
        Dimension lonDim = ncFile.addDimension("longitude", this.getLonCount());

        ncFile.addVariable("latitude", DataType.FLOAT, new Dimension[]{latDim});
        ncFile.addVariable("longitude", DataType.FLOAT, new Dimension[]{lonDim});
        ncFile.addVariableAttribute("latitude", "units", "degrees_north");
        ncFile.addVariableAttribute("longitude", "units", "degrees_east");

        ArrayList dims = new ArrayList();

        dims.add(latDim);
        dims.add(lonDim);

        ncFile.addVariable("GridData", DataType.FLOAT, dims);
        ncFile.addVariableAttribute("GridData", "units", "hPa");

        ArrayFloat.D1 dataLat = new ArrayFloat.D1(latDim.getLength());
        ArrayFloat.D1 dataLon = new ArrayFloat.D1(lonDim.getLength());

        int lon, lat;
        for (lat = 0; lat < this.getLatCount(); lat++) {
            dataLat.set(lat, new Float(this.getStartLat() + lat * this.getDeltLat()));
        }

        for (lon = 0; lon < this.getLonCount(); lon++) {
            dataLon.set(lon, new Float(this.getStartLon() + lon * this.getDeltLon()));
        }

        ArrayFloat.D2 data = new ArrayFloat.D2(this.getLatCount(), this.getLonCount());
        for (lat = 0; lat < this.getLatCount(); lat++) {
            for (lon = 0; lon < this.getLonCount(); lon++) {
                data.set(lat, lon, new Float(this.gridData[lat * this.lonCount + lon]));
            }
        }

        // 写入变量
        // 关闭文件
        try {
            ncFile.setRedefineMode(false);

            ncFile.write("longitude", dataLon);
            ncFile.write("latitude", dataLat);
            ncFile.write("GridData", data);

            ncFile.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 
     * @param strGradsFile
     * @return 
     */
    public boolean SaveAsGrads(String strGradsDataFile) {

        File grdFile = new File(strGradsDataFile);
        FileOutputStream stream = null;
        try {
            stream = FileUtils.openOutputStream(grdFile);

            for (int i = 0; i < gridData.length; i++) {
                byte[] b = FloatUtil.FloatToByteArray((float) gridData[i]);
                stream.write(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {

            try {
                stream.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }

        StringBuilder strContent = new StringBuilder();


        return true;
    }
}
