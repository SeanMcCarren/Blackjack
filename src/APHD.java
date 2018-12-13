import java.util.ArrayList;

public class APHD {
    public ArrayList<APHD> options = new ArrayList<>();
    public int value;
    public int latestCard;
    public boolean soft; //soft implies we may substract 10 from score
    public APHD parent;
    public double probability;
    public double deathProbability;
    public double expOutcomeBestMethod; //first index is expected outcome, second is take or not. take=1, no take=0
    public boolean bestOptionIsTake;

    public void print(ArrayList<Integer> cards, String pre) {
        System.out.print(pre + "|- Value: " + value + "\tCards in hand: ");
        for (int card : cards) {
            System.out.print(card + " ");
        }
        System.out.print(/*"\tsoft: " + (soft?"true":"false") +*/ "\ttake: " + (bestOptionIsTake?"true":"false") +
                "\tdeath/prob ");
        double prob = deathProbability;
        for (APHD child : options) {
            prob -= child.deathProbability;
        }
        System.out.printf("%f, expected: %f\n",prob/probability, expOutcomeBestMethod);
        for (int i = 2; i < options.size() + 2; i++){
            cards.add(options.get(i-2).latestCard);
            options.get(i-2).print(cards, " " + pre);
            cards.remove(cards.size() - 1);
        }
    }
}