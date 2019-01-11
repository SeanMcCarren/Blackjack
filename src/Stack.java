import java.util.Random;

public class Stack {

    private int decks; //The number of decks of cards
    private int[] stack; //In this array of length 10 we will store the card with value i+1 at the i-th index
    private int cardsInStack; //The number of cards left in the stack
    private Random random;

    public Stack(int decks) {
        this.decks = decks;
        this.stack = new int[10];
        for (int i = 0; i < 9; ++i) {
            this.stack[i] = decks * 4;
        }
        this.stack[9] = decks * 4 * 4; // There are four times as many tens
        this.cardsInStack = decks * 13 * 4;
        this.random = new Random();
    }

    public int pull() {
        if (cardsInStack == 0) {
            return -1; //need something better for this
        }
        int nextCardIndex = random.nextInt(cardsInStack);
        int i = stack[0];
        int j = 0; //index of card in stack to be determined
        while (i <= nextCardIndex) {
            j++;
            i+=stack[j];
        }
        --stack[j];
        --cardsInStack;
        //System.out.println("pulled " + Integer.toString(j+1) + " where index was " + Integer.toString(nextCardIndex) + " out of " + Integer.toString(cardsInStack)
        //        + " resulting to arr " + Arrays.toString(stack));
        return (++j);
    }

    public int pullInfinite() {
        if (this.cardsInStack != 4 * 13 * this.decks){
            System.out.println("something wrong");
        }

        int nextCardIndex = random.nextInt(cardsInStack);
        int i = stack[0];
        int j = 0; //index of card in stack to be determined
        while (i <= nextCardIndex) {
            j++;
            i+=stack[j];
        }

        return (++j);
    }

    public void test() {
        int pulled = pull();
        while (pulled != -1) {
            pulled = pull();
        }
    }

    public boolean isNext() {
        return cardsInStack > 0;
    }

    public int getCards(){
        return cardsInStack;
    }
}