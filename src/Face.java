import java.awt.Color;

public class Face {
    private double[] x, y, z;
    private boolean draw;
    private double[] CalcPos, nvX, nvY;
    private final ObjectFace face;
    private double avrgDistance;

    public Face(GenericClass form, double[] x, double[] y,  double[] z, Color c, boolean cacher){
        this.draw = true;
        this.x = x;
        this.y = y;
        this.z = z;
        this.face = new ObjectFace(form, new double[x.length], new double[x.length], c, cacher);
        this.avrgDistance = GetDistance();
    }

    public void updateFace(){
        nvX = new double[x.length];
        nvY = new double[x.length];
        draw = true;
        for(int i=0; i<x.length; i++) {
            CalcPos = Stage.calculus.calculDePositionFace(Stage.viewFrom, Stage.viewTo, x[i], y[i], z[i]);
            nvX[i] = (Stage.screenWidthDiv2 - Stage.calculus.getCalcFocusPos(0)) + CalcPos[0] * Stage.zoom;
            nvY[i] = (Stage.screenHeightDiv2 - Stage.calculus.getCalcFocusPos(1)) + CalcPos[1] * Stage.zoom;			
            if(Stage.calculus.getT() < 0)
                draw = false;
        }
        if(Stage.soleil){
            calcLighting();//------------Lumiere control
        }
        face.setDraw(draw);
        face.updateFace(nvX, nvY);
        avrgDistance = GetDistance();
    }

    private void calcLighting(){//------------Lumiere control
        Plane lightingPlane = new Plane(this);
        Vector lightPlane = lightingPlane.getNV();
        double angle = Math.acos(((lightPlane.getX() * Stage.rayCasting[0]) + 
            (lightPlane.getY() * Stage.rayCasting[1]) + (lightPlane.getZ() * Stage.rayCasting[2]))
            /(Math.sqrt(Stage.rayCasting[0] * Stage.rayCasting[0] + Stage.rayCasting[1] * Stage.rayCasting[1] + Stage.rayCasting[2] * Stage.rayCasting[2])));

        face.setLighting(1.2 - Math.sqrt(Math.toDegrees(angle)/180));

        if(face.getLighting() > 1) face.setLighting(1);
        if(face.getLighting() < 0) face.setLighting(0);
    }
    
    public void updateDistance(){
        this.avrgDistance = GetDistance();
    }
    
    private double GetDistance(){
        double total = 0;
        for(int i=0; i<x.length; i++){
            total += GetDistanceToP(i);
        }
        return total / x.length;
    }

    private double GetDistanceToP(int i){
        return Math.sqrt(
            sqr(Stage.viewFrom[0]-x[i]) + sqr(Stage.viewFrom[1]-y[i]) + sqr(Stage.viewFrom[2]-z[i]));
    }
    
    private double sqr(double nbr){
        return nbr * nbr;
    }

    public double getX(int i) {
        return x[i];
    }

    public double getY(int i) {
        return y[i];
    }

    public double getZ(int i) {
        return z[i];
    }

    public double getAvrgDist() {
        return avrgDistance;
    }

    public ObjectFace getFace() {
        return face;
    }

    public void setX(double[] x) {
        this.x = x;
    }

    public void setY(double[] y) {
        this.y = y;
    }

    public void setZ(double[] z) {
        this.z = z;
    }
    
    public boolean isDraw() {
        return draw;
    }
    
   
}
