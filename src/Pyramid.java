import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Pyramid {
	double x, y, z, width, length, height;
	Color c;
        private double avrgDistance;
        private boolean selected;
        List<Face> f = new ArrayList<>();
	
	public Pyramid(double x, double y, double z, double width, double length, double height, Color c){
            GenericClass gen = new GenericClass(this);
            this.f.add(new Face(gen, new double[]{x, x+width, x+width, x}, new double[]{y, y, y+length, y+length},  new double[]{z, z, z, z}, c, false));

            this.f.add(new Face(gen, new double[]{x, x, x+width}, new double[]{y, y, y, y},  new double[]{z, z+height, z+height}, c, false));

            this.f.add(new Face(gen, new double[]{x+width, x+width, x+width}, new double[]{y, y, y+length},  new double[]{z, z+height, z+height}, c, false));

            this.f.add(new Face(gen, new double[]{x, x, x+width}, new double[]{y+length, y+length, y+length},  new double[]{z, z+height, z+height}, c, false));

            this.f.add(new Face(gen, new double[]{x, x, x}, new double[]{y, y, y+length},  new double[]{z, z+height, z+height}, c, false));


            this.c = c;
            this.x = x;
            this.y = y;
            this.z = z;
            this.width = width;
            this.length = length;
            this.height = height;
            this.selected = false;
		
	}
        
    public void updateDistance(){
        double total = 0;
        for(int i=0; i<f.size(); i++){
            total += f.get(i).getAvrgDist();
        }
        this.avrgDistance = total / f.size();
    }
    
    public void updatePyramid(){
        for(Face face : f) {
            face.updateFace();
        }
        updateDistance();
    }
    
    public void drawPyramid(Graphics g){
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
    
    public Face getFace(int i) {
        return f.get(i);
    }
    
    public void hidePyramid(){
        for(Face face : f) {
            face.getFace().setCacher(true);
        }
    }
    
    public void showPyramid(){
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
    
    public boolean isSelected() {
        return selected;
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
        
}
