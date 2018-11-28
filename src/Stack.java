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
            stack[i] = decks;
        }
        stacks[9] = decks * 4; // There are four times as many tens
        this.cardsInStack = decks * 13
        this.random = new Random();
    }

    public pull() {
        if(cardsInStack == 0){
            return null;
        }
        int nextCardIndex = random.nextInt(cardsInStack);
        int i = stack[0];
        int j = 0; //index of card in stack to be determined
        while (i <= nextCardIndex) {
            i+=stack[++j];
        }
        --stack[j];
        --cardsInStack;
        return (++j);
    }
}