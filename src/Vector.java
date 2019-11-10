/**
 * Class utilisé par Plane et Calculus pour construire les vecteurs avec les coordonés en 3D(x,y,z)
 * @author Oussama, achraf & louiza
 */
public class Vector {
    private double x, y, z;
    
    /**
     * Constructeur Vector, construis un vecteur
     * @param x
     * @param y
     * @param z 
     */
    public Vector(double x, double y, double z){
        double longueur = Math.sqrt(sqr(x) + sqr(y) + sqr(z));

        this.x = longueur > 0 ? x/longueur : this.x;
        this.y = longueur > 0 ? y/longueur : this.y;
        this.z = longueur > 0 ? z/longueur : this.z;
        
    }
    
    private double sqr(double nbr){
        return nbr * nbr;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
    
    /**
     * calcul de produit du vecteur perpendiculaire
     * @param vec
     * @return Vector
     */
    public Vector produitVectorielle(Vector vec){
        return new Vector(y * vec.z - z * vec.y, z * vec.x - x * vec.z, x * vec.y - y * vec.x);		
    }
}
