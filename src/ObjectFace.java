import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

public class ObjectFace{
    private final Polygon P;
    private final Color c;
    private boolean draw;
    private boolean visible;

    private boolean cacher;
    private double lighting = 1;
    private final GenericClass pere;
    
    //private String imageFile1 = "src/assets/binary-code.png";
    //private TexturePaint imagePaint1;
    //private Rectangle imageRect;
    //private Polygon imageTriangle;
    
    
  
    public ObjectFace(GenericClass cube, double[] x, double[] y, Color c, boolean cacher){
        this.visible = true;
        this.draw = true;
        this.P = new Polygon();
        for(int i = 0; i<x.length; i++){
            this.P.addPoint((int)x[i], (int)y[i]);
        }
        this.c = c;
        this.cacher = cacher;
        this.pere = cube;
        //imageTriangle = P;
        //TiledImages();
    }
    
    /*public void TiledImages() {
        BufferedImage image = ImageUtilities.getBufferedImage(imageFile1, Stage.jpanel);
        imageRect = new Rectangle(0, 0, 60,60);
        imagePaint1 = new TexturePaint(image, imageRect);
    }*/

    void updateFace(double[] x, double[] y){
        this.P.reset();
        for(int i = 0; i<x.length; i++){
            this.P.xpoints[i] = (int) x[i];
            this.P.ypoints[i] = (int) y[i];
            this.P.npoints = x.length;
        }
        //imageTriangle = P;
    }

    void drawFace(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        g2d.setStroke(new BasicStroke(1));
        if(draw && visible){
            if(cacher){
                if(Stage.keyBoard[5]){//F
                    g2d.setColor(new Color(255, 255, 255));
                    g2d.drawPolygon(this.P);
                    if(pere.getForm().getCube() != null){
                        if(pere.getForm().getCube().isSelected()) {
                            g2d.setColor(new Color(255, 0, 0, 100));
                            g2d.fillPolygon(this.P);
                        }
                    }else if(pere.getForm().getPyramid() != null){
                        if(pere.getForm().getPyramid().isSelected()) {
                            g2d.setColor(new Color(255, 0, 0, 100));
                            g2d.fillPolygon(this.P);
                        }
                    }else if(pere.getForm().getPrism() != null){
                        if(pere.getForm().getPrism().isSelected()) {
                            g2d.setColor(new Color(255, 0, 0, 100));
                            g2d.fillPolygon(this.P);
                        }
                    }else if(pere.getForm().getSphere() != null){
                        if(pere.getForm().getSphere().isSelected()) {
                            g2d.setColor(new Color(255, 0, 0, 100));
                            g2d.fillPolygon(this.P);
                        }
                    }
                }else this.P.reset();
                
            }else{
                if(pere.getForm().getCube() != null){
                    if(pere.getForm().getCube().isSelected()) {
                        g2d.setStroke(new BasicStroke(3));
                        g2d.setColor(new Color(255, 0, 0, 200));
                        g2d.fillPolygon(this.P);
                    }else{
                        g2d.setColor(new Color((int)(c.getRed() * this.lighting), (int)(c.getGreen() * this.lighting), (int)(c.getBlue() * this.lighting)));
                        g2d.fillPolygon(this.P);
                        //ce morceau est pour les Textures
                        //g2d.setPaint(imagePaint1);
                        //g2d.fill(imageTriangle);
                    }
                    
                }else if(pere.getForm().getPyramid() != null){
                    if(pere.getForm().getPyramid().isSelected()) {
                        g2d.setStroke(new BasicStroke(3));
                        g2d.setColor(new Color(255, 0, 0, 200));
                        g2d.fillPolygon(this.P);
                    }else{
                        g2d.setColor(new Color((int)(c.getRed() * this.lighting), (int)(c.getGreen() * this.lighting), (int)(c.getBlue() * this.lighting)));
                        g2d.fillPolygon(this.P);
                        //ce morceau est pour les Textures
                        //g2d.setPaint(imagePaint1);
                        //g2d.fill(imageTriangle);
                    }
                }else if(pere.getForm().getPrism() != null){
                    if(pere.getForm().getPrism().isSelected()) {
                        g2d.setStroke(new BasicStroke(3));
                        g2d.setColor(new Color(255, 0, 0, 200));
                        g2d.fillPolygon(this.P);
                    }else{
                        g2d.setColor(new Color((int)(c.getRed() * this.lighting), (int)(c.getGreen() * this.lighting), (int)(c.getBlue() * this.lighting)));
                        g2d.fillPolygon(this.P);
                        //ce morceau est pour les Textures
                        //g2d.setPaint(imagePaint1);
                        //g2d.fill(imageTriangle);
                    }
                }else if(pere.getForm().getSphere() != null){
                    if(pere.getForm().getSphere().isSelected()) {
                        g2d.setStroke(new BasicStroke(3));
                        g2d.setColor(new Color(255, 0, 0, 200));
                        g2d.fillPolygon(this.P);
                    }else{
                        g2d.setColor(new Color((int)(c.getRed() * this.lighting), (int)(c.getGreen() * this.lighting), (int)(c.getBlue() * this.lighting)));
                        g2d.fillPolygon(this.P);
                        //ce morceau est pour les Textures
                        //g2d.setPaint(imagePaint1);
                        //g2d.fill(imageTriangle);
                    }
                }
                if(Stage.contour) {
                    g2d.setColor(new Color(255, 255, 255));
                    g2d.drawPolygon(this.P);
                }              
            }
        }
        
    }

    boolean hover() {
        return this.P.contains(Stage.screenSize.getWidth()/2, Stage.screenSize.getHeight()/2);
    }
    
    public void setDraw(boolean draw) {
        this.draw = draw;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setLighting(double lighting) {
        this.lighting = lighting;
    }

    public boolean isDraw() {
        return draw;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setCacher(boolean cacher) {
        this.cacher = cacher;
    }

    public boolean isCacher() {
        return cacher;
    }

    public double getLighting() {
        return lighting;
    }
    
    public GenericClass getPere() {
        return pere;
    }
    
    public Polygon getP() {
        return P;
    }
}
