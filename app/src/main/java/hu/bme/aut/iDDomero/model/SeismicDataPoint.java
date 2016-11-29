package hu.bme.aut.iDDomero.model;

public class SeismicDataPoint {

    private float x;
    private float y;
    private float z;
    private float all;

    public SeismicDataPoint(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        all = (float) Math.sqrt((double)(x*x + y*y + z*z));
    }

    public float getAllAccel(){
        return all;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        all = (float) Math.sqrt((double)(x*x + y*y + z*z));
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        all = (float) Math.sqrt((double)(x*x + y*y + z*z));
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
        all = (float) Math.sqrt((double)(x*x + y*y + z*z));
    }
}


