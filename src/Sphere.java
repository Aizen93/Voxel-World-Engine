import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;


public class Sphere {
    double[] ax = new double[3];
    double[] ay = new double[3];
    double[] az = new double[3];
    double[] bx = new double[3];
    double[] by = new double[3];
    double[] bz = new double[3];
    ArrayList<Face> fac = new ArrayList<>();
    private double avrgDistance;
    private boolean selected;
    Random rd = new Random();
    
    public Sphere(double coordx, double coordy, double coordz, double latitudes, double longitudes,double rayon) {
        double alpha = Math.PI/(2*latitudes);
        double beta = 2*Math.PI/longitudes;
        
        this.selected = false;
        GenericClass gen = new GenericClass(this);
        
        for (int t=0; t<latitudes; t++) {
            double rt   = rayon*Math.cos(t    *alpha);
            double rt_1 = rayon*Math.cos((t+1)*alpha);
            double y    = rayon*Math.sin(t    *alpha);
            double y_1  = rayon*Math.sin((t+1)*alpha);
            for (int f=0; f<longitudes; f++) {
                double xrt   = rt  *Math.cos(f*beta);
                double zrt   = rt  *Math.sin(f*beta);
                double x_1rt = rt  *Math.cos((f+1)*beta);
                double z_1rt = rt  *Math.sin((f+1)*beta);
                double xrt_1 = rt_1*Math.cos(f*beta);
                double zrt_1 = rt_1*Math.sin(f*beta);
                double x_1rt_1 = rt_1*Math.cos((f+1)*beta);
                double z_1rt_1 = rt_1*Math.sin((f+1)*beta);


                fac.add(new Face(gen, 
                        new double[]{xrt+coordx, x_1rt+coordx, xrt_1+coordx, x_1rt_1+coordx},
                        new double[]{y+coordy, y+coordy, y_1+coordy, y_1+coordy},
                        new double[]{zrt+coordz, z_1rt+coordz, zrt_1+coordz, z_1rt_1+coordz},
                        Color.RED,
                        false
                ));

                fac.add(new Face(gen, 
                        new double[]{xrt+coordx, x_1rt+coordx, xrt_1+coordx, x_1rt_1+coordx},
                        new double[]{-y+coordy, -y+coordy, -y_1+coordy, -y_1+coordy},
                        new double[]{zrt+coordz, z_1rt+coordz, zrt_1+coordz, z_1rt_1+coordz},
                        Color.RED,
                        false
                ));
            }
        }
    }
    public int getSize(){
        return fac.size();
    }
    public void updateDistance(){
        double total = 0;
        for(int i=0; i<fac.size(); i++){
            total += fac.get(i).getAvrgDist();
        }
        this.avrgDistance = total / fac.size();
    }
    
    public void updateSphere(){
        for(Face face : fac) {
            face.updateFace();
        }
        updateDistance();
    }
    
    public void drawSphere(Graphics g){
        this.fac.sort(Comparator.comparing(Face::getAvrgDist));
        for(int i = fac.size()-1; i >= 0; i--) {
            fac.get(i).getFace().drawFace(g);
        }
    }
    
    public double getAvrgDistance() {
        return avrgDistance;
    }
    
    public boolean isSelected() {
        return selected;
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
