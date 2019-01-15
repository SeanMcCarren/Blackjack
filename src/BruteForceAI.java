import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class BruteForceAI extends CardCounter {

    private Stack stack;
    private int amountOfDecks;
    private int numCardsInHand;
    private List<Map<String, double[]>> storageValueProbs;
    private Map<String, double[]> valueProbDealer;
    private int dealerFirstCard;

    public BruteForceAI(int decks) {
        super();
        this.amountOfDecks = decks;
        this.resetCount();
        this.reset();
        storageValueProbs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            valueProbDealer = new HashMap<String, double[]>();
            storageValueProbs.add(valueProbDealer);
        }
    }

    public static void main(String[] args) {
        new BruteForceAI(6).bestOptionFixedExample();
    }

    // This function is for demonstration purposes only
    public void bestOptionFixedExample() {
        BruteForceAI player = new BruteForceAI(6);
        DealerAI dealer = new DealerAI();
        Stack stack = new Stack(6); //new stack with 6 decks (6 * 4 * 13 cards)
        //remove half of the stack randomly
        for (int i = 0; i < 6 * 2 * 13; i++) {
            player.notice(stack.pull()); 
        }
        //player gets two cards in the beginning
        player.pulled(stack.pull(), false);
        player.pulled(stack.pull(), false);

        //dealer gets one open card and one closed card
        int card1 = stack.pull();
        player.notice(card1);
        dealer.pulled(card1);
        int card2 = stack.pull();
        dealer.pulled(card2);

        //If player has a blackjack, 21, then the game is already over
        if(player.getScore() == 21) {
            if(dealer.getScore() == 21) {
                System.out.println("Player and dealer have blackjack");
                return;
            }
            else { 
                System.out.println("Player has blackjack");
                player.addWinnings(1.5);
                return;
            }
        }

         //Player's turn
        //System.out.println("Player starts");

        while(player.wantsNext(card1)){
            //int tempCard1 = stack.pull();
            //System.out.println("pulled a " + tempCard1 + ".");

            player.pulled(stack.pull(), false);
        }

        //the closed card is open to the player
        //System.out.println("Player is done, card is flipped");
        player.notice(card2);

        //Dealer's turn
        //System.out.println("Dealer starts");
    
        while(dealer.wantsNext()){
            int tempCard2 = stack.pull();
            //System.out.println("pulled a " + tempCard2 + ".");

            player.notice(tempCard2);
            dealer.pulled(tempCard2);
        }

        //System.out.print("Decide who wins, current score is: ");
        //System.out.println("Player has: " + player.getScore() + ", Dealer has: " + dealer.getScore() + ".");

        //the game has been played, checking who wins
        int playerScore = player.getScore();
        int dealerScore = dealer.getScore();

        if(playerScore > 21){ //even if dealer loses too: house's edge
            player.addWinnings(-1.0);
            return;
        } else if(dealerScore > 21){ //player didnt bust, dealer did.
            player.addWinnings(1.0);
            return;
        } else if(playerScore < 17){ //no busts, but player < dealer
            player.addWinnings(-1.0);
            return;
        } else if(playerScore > 17){ //no busts, but player > dealer
            player.addWinnings(1.0);
        }
    }

    @Override
    public void pulled(int card) {
        numCardsInHand++;
        super.pulled(card);
    }

    @Override
    public void resetCount() {
        this.stack = new Stack(this.amountOfDecks);
    }

    public void reset() {
        numCardsInHand = 0;
        super.reset();
    }

    @Override
    public boolean wantsNext(int card) {
        if (getScore() <= 11) {
            return true; //imperative, worst option is standing
        }
        if (this.dealerFirstCard != card) {
            this.dealerFirstCard = card;
            valueProbDealer = storageValueProbs.get(card==11?0:card-1);
        }
        APHD root = new APHD();
        root.probability = 1.0;
        root.value = getScore();
        root.depth = numCardsInHand;
        root.remainingStack = stack.copy();
        traverse(root, 21); //to create all options
        bestMethodTraversal(root);
        return root.bestOptionIsTake;
    }

    @Override
    public void notice(int card) {
        this.stack.pulled(card);
    }

    private void printArray(double[] arr) {
        for ( int i = 0; i < arr.length; i++ ) {
            System.out.print(i + " " + arr[i] + "   ");
        }
        System.out.println("");
    }

    private void bestMethodTraversal(APHD node) {
        double expOutcomeNoTake = expectedOutcomeNoTake(node);
        if (node.options != null && !node.options.isEmpty()) {
            double expOutcomeTake = 0.0;
            for (APHD child : node.options) {
                //assume we recursively take the best option.
                bestMethodTraversal(child);
                expOutcomeTake += child.expOutcomeBestMethod * node.remainingStack.probability(child.latestCard==11?1:child.latestCard);
            }
            expOutcomeTake -= node.deathProbabilityOneHit;
            if(expOutcomeNoTake >= expOutcomeTake) {
                node.expOutcomeBestMethod = expOutcomeNoTake;
                node.bestOptionIsTake = false;
            } else {
                node.expOutcomeBestMethod = expOutcomeTake;
                node.bestOptionIsTake = true;
            }
        } else {
            node.expOutcomeBestMethod = expOutcomeNoTake;
            node.bestOptionIsTake = false;
        }
    }

    private double expectedOutcomeNoTake(APHD node) {
        double[] valueProbability = getValueProbability(node);
        if (node.depth == 2 && node.value == 21) {
            return 1.5 * (1-valueProbability[0]);
        }
        return (valueProbability[22] + dealerOddsCumulative(node.value - 1, valueProbability)
                - (dealerOddsCumulative(21, valueProbability) - dealerOddsCumulative(node.value + 1, valueProbability)));
    }

    private void getVPHelper(APHD root, double[] storage) {
        if (root.options.size() > 0) {
            for (APHD child : root.options) {
                getVPHelper(child, storage);
            }
        } else {
            // Then it is a leaf, and we should consider its probability
            if (root.depth == 2 && root.value == 21) {
                storage[0] += root.probability;
            } else {
                storage[root.value] += root.probability;
            }
        }
    }

    private double dealerOddsCumulative(int x, double[] valueProbability) {
        double cumulative = 0.0;
        for (int i = 17; i <= (x > 21 ? 21 : x); i++) {
            cumulative += valueProbability[i];
        }
        if (x >= 21) {
            cumulative += valueProbability[0];
        }
        return cumulative;
    }

    public double[] getValueProbability(APHD node) {
        //result[0] is odd of blackjack
        //result[i] is odd of ending with i (of course you dont end with ...)
        //result[22] is death probability
        if (valueProbDealer.get(node.remainingStack.toString()) != null) {
            //System.out.println("Already exists");
            //System.out.println("VP stack " + node.remainingStack.toString() + " gives ");
            //printArray(valueProbDealer.get(node.remainingStack.toString()));
            return valueProbDealer.get(node.remainingStack.toString());
        }

        APHD rootDealer = new APHD();
        rootDealer.probability = 1;
        rootDealer.depth = 1;
        rootDealer.value = dealerFirstCard;
        rootDealer.latestCard = dealerFirstCard;
        rootDealer.remainingStack = node.remainingStack.copy();
        rootDealer.aces = dealerFirstCard == 11 ? 1 : 0;

        traverse(rootDealer, 17);

        //gather result
        double[] result = new double[23];
        getVPHelper(rootDealer, result);
        result[22] = rootDealer.deathProbability;

        //log and return result
        //System.out.println("VP stack " + rootDealer.remainingStack.toString() + " gives ");
        //printArray(result);
        valueProbDealer.put(rootDealer.remainingStack.toString(), result);
        return result;
    }
  
    // passing limit = 17 yields dealer strat. limit = 21 yields always play strat
    public void traverse(APHD par, int lim) {
        if (par.value >= lim) {
            return;
        }
        for (int i = 2; i <= 11; i++) {
            //System.out.println(par.remainingStack);
            //System.out.println("prob of "+ i +"is: " + par.remainingStack.probability(i==11?1:i));
            if (par.remainingStack.probability((i==11?1:i))==0.0) {
                //System.out.println(par.remainingStack.toString());
                //System.out.println("Dont have to consider " + i);
                continue;
            }
            //System.out.println("Will consider " + i);
            // aka: not: go over && hard hand && i != 11
            if (par.value + i <= 21 || par.aces > 0 || i == 11) {
                
                APHD opt = new APHD();
                opt.latestCard = i;
                opt.parent = par;
                opt.depth = par.depth +1;
                opt.probability = par.probability * par.remainingStack.probability(i==11?1:i);
                opt.value = par.value;
                opt.aces = par.aces;
                opt.remainingStack = par.remainingStack.copy();
                if (!opt.remainingStack.pulled((i==11?1:i))) { System.out.println("Error cannot have pulled " + i); }

                // dealing with aces can be tricky

                // stay under 21, no ace
                if (opt.value + i <= 21 && i != 11) {
                    opt.value += i;
                //go over 21 but you do have a soft hand
                } else if (opt.value + i > 21 && par.aces>0) {
                    opt.value -= 10;
                    opt.aces -= 1;
                    //go over with another ace
                    if (i == 11 && opt.value + 11 > 21) {
                        opt.value += 1;
                    } else if (i == 11) {
                        opt.value += 11;
                        opt.aces += 1;
                    //go over with something else
                    } else {
                        opt.value += i;
                    }
                //card was ace
                } else if (i == 11) {
                    //went under
                    if (opt.value + 11 <= 21) {
                        opt.value += 11;
                        opt.aces += 1;
                    //go over. Notice the hand must be hard here
                    } else if (opt.value + 11 > 21) {
                        opt.value += 1;
                    }
                }
                
                par.options.add(opt);
                traverse(opt, lim);

                //will be computed after the traversal
                par.deathProbability += opt.deathProbability;
            }
            //aka: go over && hard hand && i != 11. No more children
            else {
                par.deathProbabilityOneHit += par.remainingStack.probability(i==11?1:i);
            }
        }
        par.deathProbability += par.deathProbabilityOneHit * par.probability;
    }
}
