/**
 * Class Plane, construis et calcul les vecteurs necessaire au calculus
 * @author Oussama, achraf, louiza
 */
public class Plane {
    
    private final Vector vec1, vec2, vecN;
    private double[] FACE = new double[3];
    
    /**
     * Constructeur Plane, construis 2 vecteurs et leur produit, d'une face
     * @param face 
     */
    public Plane(Face face){
        this.FACE[0] = face.getX(0); 
        this.FACE[1] = face.getY(0); 
        this.FACE[2] = face.getZ(0); 

        this.vec1 = new Vector(face.getX(1) - face.getX(0), face.getY(1) - face.getY(0), face.getZ(1) - face.getZ(0));
        this.vec2 = new Vector(face.getX(2) - face.getX(0), face.getY(2) - face.getY(0), face.getZ(2) - face.getZ(0));
        this.vecN = this.vec1.produitVectorielle(this.vec2);
    }
    
    /**
     * Constructeur Plane, construis 2 vecteurs et leur produit et initialise les points du polygon P
     * @param vec1
     * @param vec2
     * @param z 
     */
    public Plane(Vector vec1, Vector vec2, double[] z){
        this.FACE = z;
        this.vec1 = vec1;
        this.vec2 = vec2;
        this.vecN = this.vec1.produitVectorielle(vec2);
    }

    public Vector getV1() {
        return vec1;
    }

    public Vector getV2() {
        return vec2;
    }

    public Vector getNV() {
        return vecN;
    }
    
    public double getFACE(int i) {
        return FACE[i];
    }
}
