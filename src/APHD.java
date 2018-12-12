import java.util.ArrayList;

public class APHD {
    public ArrayList<APHD> options = new ArrayList<>();
    public int value;
    public boolean soft; //soft implies we may substract 10 from score
    public APHD parent;
    public double probability;
    public double deathProbability;
}