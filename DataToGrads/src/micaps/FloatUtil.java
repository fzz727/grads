package micaps;

public class FloatUtil {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        float v = 2356.546f;

        byte[] b = FloatToByteArray(v);

        System.out.println(ByteArrayToFloat(b));
    }

    /**
     * 将浮点数转换为IEEE754格式保存到的字节数组，将其直接按字节写入文件中即可形成标准的IEEE754规范的数据文件
     * 
     * @param f
     * @return
     */
    public static byte[] FloatToByteArray(float f) {

        int i = Float.floatToIntBits(f);

        byte[] b = new byte[4];

        b[0] = (byte) (i & 0xff);
        b[1] = (byte) ((i >> 8) & 0xff);
        b[2] = (byte) ((i >> 16) & 0xff);
        b[3] = (byte) ((i >> 24) & 0xff);

        return b;
    }

    /**
     * 将字节数组转换为浮点数，按照IEEE754标准
     * @param b
     * @return
     */
    public static float ByteArrayToFloat(byte[] b) {

        int i = (int) ((b[3] & 0xff) << 24 | (b[2] & 0xff) << 16
                | (b[1] & 0xff) << 8 | (b[0] & 0xff));

        return Float.intBitsToFloat(i);
    }
    
    
    public static byte[] IntegerToByteArray(int i) {

        byte[] b = new byte[4];

        b[0] = (byte) (i & 0xff);
        b[1] = (byte) ((i >> 8) & 0xff);
        b[2] = (byte) ((i >> 16) & 0xff);
        b[3] = (byte) ((i >> 24) & 0xff);

        return b;
    }
}
