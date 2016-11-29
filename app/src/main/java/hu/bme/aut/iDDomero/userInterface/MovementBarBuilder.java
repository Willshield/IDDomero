package hu.bme.aut.iDDomero.userInterface;


public class MovementBarBuilder {

    private String[] bars;

    public MovementBarBuilder(int maxBars){
        if (maxBars <= 1)
            throw new IllegalArgumentException("maxBars are at least 1");
        bars = new String[maxBars];
        bars[0] = "|";
        for (int i = 1; i < maxBars; i++){
            bars[i] = (bars[i-1] + "|");
        }
    }

    public String getBars(int numOfBars){
        if (numOfBars >= bars.length){
            numOfBars = bars.length -1;
        }
        return bars[numOfBars];
    }
}
