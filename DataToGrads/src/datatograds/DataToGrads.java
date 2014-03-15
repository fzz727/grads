/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datatograds;

import micaps.Micaps1;

/**
 *
 * @author Joeff
 */
public class DataToGrads {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

//        Micaps4 mic = new Micaps4();
//        if (mic.load("E:\\KuaiPan\\Grads\\simple\\micaps4\\07071020.000")) {
//            mic.SaveAsGrads("F:\\ruc\\grads\\data\\height.grd");
//        }
        
        

        Micaps1 mic = new Micaps1();
        try {
            mic.ConvertToGrads("E:\\KuaiPan\\Grads\\simple\\D1\\11071907.000", "F:\\ruc\\grads\\data\\temp.dat");
            mic.CreateMapFile("F:\\ruc\\grads\\data\\grid.bin");
        } catch (Exception e) {
        }
        
        
        
    }
}
