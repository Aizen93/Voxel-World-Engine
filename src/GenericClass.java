/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Oussama
 */
public class GenericClass {
    private H form;
    
    class H<E>{
        Cube cube;
        Pyramid pyramid;
        Prism prism;
        Sphere sphere;
        public H(E e){
            if(e.getClass().equals(Cube.class)){
                cube = (Cube)e;
            }else if(e.getClass().equals(Pyramid.class)){
                pyramid = (Pyramid)e;
            }else if(e.getClass().equals(Prism.class)){
                prism = (Prism)e;
            }else if(e.getClass().equals(Sphere.class)){
                sphere = (Sphere)e;
            }
        }
        public Cube getCube(){
            return cube;
        }
        public Pyramid getPyramid(){
            return pyramid;
        }
        public Prism getPrism(){
            return prism;
        }
        public Sphere getSphere(){
            return sphere;
        }
    }
    
    
    public GenericClass(Cube cube){
        this.form = new H(cube);
    }
    
    public GenericClass(Pyramid pyramid){
        this.form = new H(pyramid);
    }
    
    public GenericClass(Prism prism){
        this.form = new H(prism);
    }
    
    public GenericClass(Sphere sphere){
        this.form = new H(sphere);
    }
    
    public H getForm(){
        return this.form;
    }
    
}
