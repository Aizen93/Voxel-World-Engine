import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
/**
 * Objet cube avec 6 faces
 * @author Oussama, achraf & louiza
 */
public class Cube {
    private double x;
    private double y;
    private double z;
    private double width;
    private Color c;
    private double avrgDistance;
    private long code;
    private int texture;
    private boolean selected;
    private int id;
    List<Face> f = new ArrayList<>();
    
    /**
     * Constructeur Cube utilisé par l'octree
     * @param code 
     */
    public Cube(long code){
        this.code = code;
    }
    
    /**
     * Constructeur Cube utilisé par nos algorithmes
     * @param x
     * @param y
     * @param z
     * @param width
     * @param c
     * @param cacher
     * @param texture 
     */
    public Cube(double x, double y, double z, double width, Color c, boolean cacher, int texture, int id){
        double xWidth = x + width;
        double yLength = y + width;
        double zHeight = z + width;
        GenericClass gen = new GenericClass(this);
        this.f.add(new Face(gen, new double[]{x, xWidth, xWidth, x}, new double[]{y, y, yLength, yLength},  new double[]{z, z, z, z}, c, cacher));
        
        this.f.add(new Face(gen, new double[]{x, xWidth, xWidth, x}, new double[]{y, y, yLength, yLength},  new double[]{zHeight, zHeight, zHeight, zHeight}, c, cacher));
        
        this.f.add(new Face(gen, new double[]{x, x, xWidth, xWidth}, new double[]{y, y, y, y},  new double[]{z, zHeight, zHeight, z}, c, cacher));
        
        this.f.add(new Face(gen, new double[]{xWidth, xWidth, xWidth, xWidth}, new double[]{y, y, yLength, yLength},  new double[]{z, zHeight, zHeight, z}, c, cacher));
        
        this.f.add(new Face(gen, new double[]{x, x, xWidth, xWidth}, new double[]{yLength, yLength, yLength, yLength},  new double[]{z, zHeight, zHeight, z}, c, cacher));
        
        this.f.add(new Face(gen, new double[]{x, x, x, x}, new double[]{y, y, yLength, yLength},  new double[]{z, zHeight, zHeight, z}, c, cacher));
        
        updateDistance();
        this.c = c;
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.selected = false;
        this.texture = texture;
        this.id = id;
    }
    
    /**
     * Utilisé par l'octree pour initialiser le cube génèré
     * @param x
     * @param y
     * @param z
     * @param width
     * @param c
     * @param cacher
     * @param texture 
     */
    public void setCube(double x, double y, double z, double width, Color c, boolean cacher, int texture){
        double xWidth = x + width;
        double yLength = y + width;
        double zHeight = z + width;
        GenericClass gen = new GenericClass(this);
        
        this.f.add(new Face(gen, new double[]{x, xWidth, xWidth, x}, new double[]{y, y, yLength, yLength},  new double[]{z, z, z, z}, c, cacher));
        
        this.f.add(new Face(gen, new double[]{x, xWidth, xWidth, x}, new double[]{y, y, yLength, yLength},  new double[]{zHeight, zHeight, zHeight, zHeight}, c, cacher));
        
        this.f.add(new Face(gen, new double[]{x, x, xWidth, xWidth}, new double[]{y, y, y, y},  new double[]{z, zHeight, zHeight, z}, c, cacher));
        
        this.f.add(new Face(gen, new double[]{xWidth, xWidth, xWidth, xWidth}, new double[]{y, y, yLength, yLength},  new double[]{z, zHeight, zHeight, z}, c, cacher));
        
        this.f.add(new Face(gen, new double[]{x, x, xWidth, xWidth}, new double[]{yLength, yLength, yLength, yLength},  new double[]{z, zHeight, zHeight, z}, c, cacher));
        
        this.f.add(new Face(gen, new double[]{x, x, x, x}, new double[]{y, y, yLength, yLength},  new double[]{z, zHeight, zHeight, z}, c, cacher));
        
        updateDistance();
        this.c = c;
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.selected = false;
        this.texture = texture;
    }
    
    public void updateDistance(){
        double total = 0;
        for(int i=0; i<f.size(); i++){
            total += f.get(i).getAvrgDist();
        }
        this.avrgDistance = total / f.size();
    }
    
    public void updateCube(){
        for(Face face : f) {
            face.updateFace();
        }
        updateDistance();
    }
    
    public void drawCube(Graphics g){
        this.f.sort(Comparator.comparing(Face::getAvrgDist));
        for(int i = f.size()-1; i >= 0; i--) {
            f.get(i).getFace().drawFace(g);
        }
    }
    
    public ObjectFace faceHover(){
        for(Face face : f) {
            if(face.getFace().hover())
                return face.getFace();
        }
        return null;
    }
    
    public double getAvrgDistance() {
        return avrgDistance;
    }
    
    private double sqr(double nbr){
        return nbr * nbr;
    }
    
    public Face getFace(int i) {
        return f.get(i);
    }
    
    public void hideCube(){
        for(Face face : f) {
            face.getFace().setCacher(true);
        }
    }
    
    public void showCube(){
        for(Face face : f) {
            face.getFace().setCacher(false);
        }
    }

    public Color getC() {
        return c;
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
    
    public void print(){
        System.out.println("cube x:"+x+" y:"+y+" z:"+z+" width:"+width);
    }
    
    public void setCode(long code){
        this.code = code;
    }
    
    public long getCode(){
        return this.code;
    }

    public void setX(int x){
        this.x = (double)x;
    }
    
    public void setY(int y){
        this.y = (double)y;
    }
    
    public void setZ(int z){
        this.z = (double)z;
    }
    
    public void setTexture(int texture){
        this.texture = texture;
    }
    
    public void setWidth(double width) {
        this.width = width;
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }
    
    public int getId() {
        return id;
    }
}