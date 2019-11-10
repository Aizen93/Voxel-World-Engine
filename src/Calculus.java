/**
 * Calculatrice de Vecteur et de Projection
 * @author Oussama
 */
public class Calculus {
    private double transform;
    private final Vector vec1, vec2;
    private final Vector viewVector, rotationVector, directionVector;
    private final Vector planeVector1, planeVector2;
    private final Plane plane;
    private double[] calcFocusPos;

    public Calculus(){
        this.calcFocusPos = new double[2];
        this.transform = 0;
        this.viewVector = new Vector(Stage.viewTo[0] - Stage.viewFrom[0], Stage.viewTo[1] - Stage.viewFrom[1], Stage.viewTo[2] - Stage.viewFrom[2]);			
        this.directionVector = new Vector(1, 1, 1);				
        this.planeVector1 = viewVector.produitVectorielle(directionVector);
        this.planeVector2 = viewVector.produitVectorielle(planeVector1);
        this.plane = new Plane(planeVector1, planeVector2, Stage.viewTo);

        this.rotationVector = getVecteurDeRotation(Stage.viewFrom, Stage.viewTo);
        this.vec1 = viewVector.produitVectorielle(rotationVector);
        this.vec2 = viewVector.produitVectorielle(vec1);

        this.calcFocusPos = calculDePositionFace(Stage.viewFrom, Stage.viewTo, Stage.viewTo[0], Stage.viewTo[1], Stage.viewTo[2]);
        this.calcFocusPos[0] = Stage.zoom * calcFocusPos[0];
        this.calcFocusPos[1] = Stage.zoom * calcFocusPos[1];
    }

    public double[] calculDePositionFace(double[] viewFrom, double[] viewTo, double x, double y, double z){		
        double[] projP = getProj(viewFrom, viewTo, x, y, z, plane);
        double[] drawP = getCoordFace(projP[0], projP[1], projP[2]);
        return drawP;
    }

    private double[] getProj(double[] viewFrom, double[] viewTo, double x, double y, double z, Plane plan){
        Vector viewToPos = new Vector(x - viewFrom[0], y - viewFrom[1], z - viewFrom[2]);

        Vector planNV = plan.getNV();
        
        double planNVX = planNV.getX() * plan.getFACE(0);
        double planNVY = planNV.getY() * plan.getFACE(1);
        double planNVZ = planNV.getZ() * plan.getFACE(2);
        double planP = planNVX + planNVY +  planNVZ;

        planNVX = planNV.getX() * viewFrom[0];
        planNVY = planNV.getY() * viewFrom[1];
        planNVZ = planNV.getZ() * viewFrom[2];
        double planView = planNVX + planNVY + planNVZ;

        double planViewP = planP - planView;

        planNVX = planNV.getX() * viewToPos.getX();
        planNVY = planNV.getY() * viewToPos.getY();
        planNVZ = planNV.getZ() * viewToPos.getZ();
        double planNVPos = planNVX + planNVY + planNVZ;

        this.transform = planViewP / planNVPos;

        x = viewFrom[0] + viewToPos.getX() * transform;
        y = viewFrom[1] + viewToPos.getY() * transform;
        z = viewFrom[2] + viewToPos.getZ() * transform;

        return new double[] {x, y, z};
    }

    private double[] getCoordFace(double x, double y, double z){		
        return new double[]{vec2.getX() * x + vec2.getY() * y + vec2.getZ() * z, 
            vec1.getX() * x + vec1.getY() * y + vec1.getZ() * z};
    }

    private Vector getVecteurDeRotation(double[] depuis, double[] vers){
        double dx = Math.abs(depuis[0] - vers[0]);
        double dy = Math.abs(depuis[1] - vers[1]);
        double xRot, yRot;
        xRot = dy / (dx + dy);		
        yRot = dx / (dx + dy);
        
        xRot = depuis[1] > vers[1] ? -xRot : xRot;
        yRot = depuis[0] < vers[0] ? -yRot : yRot;
        
        Vector v = new Vector(xRot, yRot, 0);

        return v;
    }

    public double getT() {
        return transform;
    }

    public double getCalcFocusPos(int i) {
        return calcFocusPos[i];
    }

}