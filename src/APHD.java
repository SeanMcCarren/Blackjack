import java.util.ArrayList;

public class APHD {
    public ArrayList<APHD> options = new ArrayList<>();
    public int value;
    public int latestCard;
    public Stack remainingStack; //we do not use this in strategyGenerator
    public int aces; //soft implies we may substract 10 from score
    public APHD parent;
    public double probability;
    public double deathProbability;
    public double deathProbabilityOneHit;
    public double expOutcomeBestMethod; //first index is expected outcome, second is take or not. take=1, no take=0
    public boolean bestOptionIsTake;
    public int depth;

    public void print(ArrayList<Integer> cards, String pre) {
        System.out.print(pre + "|- Value: " + value + " Hand: ");
        for (int card : cards) {
            System.out.print(card + " ");
        }
        System.out.print(" aces: " + Integer.toString(aces) + " take: " + (bestOptionIsTake?"true":"false") +
                " death/prob ");
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

    public void print() {
        System.out.print("Value: " + value);
        System.out.print(" aces: " + Integer.toString(aces) + " take: " + (bestOptionIsTake?"true":"false") +
                " deathNow ");
        System.out.printf("%f, death: %f, expected: %f, prob path: %f, optN: %d\n",deathProbabilityOneHit, deathProbability, expOutcomeBestMethod, probability, options.size());
    }
}