import java.util.ArrayList;
import java.util.List;

public class StrategyGenerator {

    private double[] valueProbability;
    private int dealerUpperLimit = 17;
    private List<List<Boolean>> takeListHard;
    private List<List<Boolean>> takeListSoft;
    private int[] takeHard = new int[100];
    private int[] takeSoft = new int[100];
    private int dealerFirstCard;
    

    public static void main(String[] args) {

        new StrategyGenerator().runFirstCardDependent();

    }

    public void test() {
        getValueProbabilityDealer();
        printArray(valueProbability);
        APHD node = new APHD();
        node.value = 21;
        node.depth = 2;
        //System.out.println(expectedOutcomeNoTake(node));
        //printArray(valueProbability);
    }

    public void runFirstCardDependent() {
        //of course the dealer has one card open. Say we want to generate a strategy based on that too..
        //10 width,  10 height
        for (dealerFirstCard = 2; dealerFirstCard <= 11; dealerFirstCard++) {
            APHD root = new APHD();
            root.value = dealerFirstCard;
            root.probability = 1.0;
            root.depth = 1;
            if (dealerFirstCard == 11) {
                root.aces = 1;
            } else {
                root.aces = 0;
            }
            traverse(root, dealerUpperLimit);
            valueProbability = getValueProbability(root, true);
            //System.out.println(". The probability table of dealer: ");
            //printArray(valueProbability);
            run();
        }
        System.out.println("S  2  3  4  5  6  7  8  9  10 A");
        printFakeMatrix(takeSoft);
        System.out.println("H  2  3  4  5  6  7  8  9  10 A");
        printFakeMatrix(takeHard);
        
    }

    public void printFakeMatrix(int[] matrix) {
        if (matrix.length == 100) {
            for (int i = 0; i<10; i++) {
                System.out.print(i+11);
                for (int j=0; j<10; j++) {
                    System.out.print(" "  + Integer.toString(matrix[i*10+j]) + " ");
                }
                System.out.print("\n");
            }
            System.out.println("\n");
        }
    }

    public void run() {
        if (valueProbability == null) {
            getValueProbabilityDealer();
        }
        APHD root = new APHD();
        root.probability = 1.0;
        root.depth = 0;
        traverse(root, 21); //to create all options
        bestMethodTraversal(root); //we should ignore method suggestions if depth <= 2, but i dont do that here
        //root.options.get(8).options.get(9).print(new ArrayList<Integer>() {{add(10); add(11);}}, "");

        takeListHard = new ArrayList<List<Boolean>>();
        takeListSoft = new ArrayList<List<Boolean>>();
        for (int value = 0; value < 21; value ++) { //consider having values 0..20
            takeListHard.add(new ArrayList<Boolean>());
            takeListSoft.add(new ArrayList<Boolean>());
            
        }
        traverseTake(root); //list all take booleans per value of node
        for (int i = 2; i < 21; i++) {
            //check all take values for if they are equal!
            if(!takeListSoft.get(i).isEmpty()) {
                boolean bPrev = takeListSoft.get(i).get(0);
                for (boolean b : takeListSoft.get(i)) {
                    if (bPrev != b) {
                        System.out.println("PROBLEM SOFT");
                        //for (boolean b2 : takeListSoft.get(i)) {
                        //    System.out.print(b2?"1":"0");
                        //}
                        return;
                    }
                }
            }
            if(!takeListHard.get(i).isEmpty()) {
                boolean bPrev = takeListHard.get(i).get(0);
                for (boolean b : takeListHard.get(i)) {
                    if (bPrev != b) {
                        System.out.println("PROBLEM HARD");
                        //for (boolean b2 : takeListHard.get(i)) {
                        //    System.out.print(b2?"1":"0");
                        //}
                        return;
                    }
                }
            }
        }
        for (int i = 11; i < 21; i++) {
            if(!takeListSoft.get(i).isEmpty()) {
                takeSoft[(i-11)*10+(dealerFirstCard-2)] = takeListSoft.get(i).get(0)?1:0;
            }
            if(!takeListHard.get(i).isEmpty()) {
                takeHard[(i-11)*10+(dealerFirstCard-2)] = takeListHard.get(i).get(0)?1:0;
            }
        }
        //System.out.println(root.expOutcomeBestMethod);
    }

    private void traverseTake(APHD root) {
        if (root.value < 21) {
            if (root.value > 1) {
                //System.out.println(Integer.toString(root.value) + " " + Integer.toString(root.aces) + " " + (root.bestOptionIsTake ? "take":"dont"));
                if (root.aces == 0) {
                    takeListHard.get(root.value).add(root.bestOptionIsTake);
                } else {
                    takeListSoft.get(root.value).add(root.bestOptionIsTake);
                }
            }
            for (APHD child : root.options) {
                traverseTake(child);
            }
        }
    }


    private void printArray(double[] arr) {
        for ( int i = 0; i < arr.length; i++ ) {
            System.out.println(/*i + ":\t" + */arr[i]);
        }
    }

    private double probabilityDeathGivenPathTakeOne(APHD obj) {
        double prob = obj.deathProbability;
        for (APHD child : obj.options) {
            prob -= child.deathProbability;
        }
        return prob/obj.probability;
    }

    private double sum(double[] arr) {
        double result = 0;
        for (double val : arr) {
            result += val;
        }
        return result;
    }

    private void bestMethodTraversal(APHD node) {
        double expOutcomeNoTake = expectedOutcomeNoTake(node);
        if (node.options != null && !node.options.isEmpty()) {
            double expOutcomeTake = 0.0;
            for (APHD child : node.options) {
                //assume we recursively take the best option.
                bestMethodTraversal(child);
                expOutcomeTake += child.expOutcomeBestMethod * (child.value == 10 ? 4.0 : 1.0) / 13.0;
            }
            expOutcomeTake -= probabilityDeathGivenPathTakeOne(node);
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
        if (node.depth == 2 && node.value == 21) {
            return 1.5 * (1-valueProbability[0]);
        }
        return (valueProbability[22] + dealerOddsCumulative(node.value - 1)
                - (dealerOddsCumulative(21) - dealerOddsCumulative(node.value + 1)));
    }

    private void getVPHelper(APHD root, int depth, double[] storage) {
        if (root.options.size() > 0) {
            for (APHD child : root.options) {
                getVPHelper(child, depth+1, storage);
            }
        } else {
            // Then it is a leaf, and we should consider its probability
            if (depth == 2 && root.value == 21) {
                storage[0] += root.probability;
            } else {
                storage[root.value] += root.probability;
            }
        }
    }

    private double dealerOddsCumulative(int x) {
        if (valueProbability == null) {
            getValueProbabilityDealer();
        }
        double cumulative = 0.0;
        for (int i = dealerUpperLimit; i <= (x > 21 ? 21 : x); i++) {
            cumulative += valueProbability[i];
        }
        if (x >= 21) {
            cumulative += valueProbability[0];
        }
        return cumulative;
    }

    // firstCard = true implies the root is already a chosen card
    public double[] getValueProbability(APHD root, boolean firstCard) {
        //result[0] is odd of blackjack
        //result[i] is odd of ending with i (of course you dont end with ...)
        //result[22] is death probability
        double[] result = new double[23];
        getVPHelper(root, firstCard ? 1:0, result);
        result[22] = root.deathProbability;
        return result;
    }

    public void getValueProbabilityDealer() {
        APHD root = new APHD();
        root.probability = 1.0;
        root.depth = 0;
        traverse(root, dealerUpperLimit);
        valueProbability = getValueProbability(root, false);
    }
  
    // passing limit = 17 yields dealer strat. limit = 21 yields always play strat
    public void traverse(APHD par, int lim) {
        if (par.value >= lim) {
            return;
        }
        for (int i = 2; i <= 11; i++) {
            // aka: not: go over && hard hand && i != 11
            if (par.value + i <= 21 || par.aces > 0 || i == 11) {
                
                APHD opt = new APHD();
                opt.latestCard = i;
                opt.parent = par;
                opt.depth = par.depth +1;
                opt.probability = par.probability * (i==10?4.0:1.0) / 13.0;
                opt.value = par.value;
                opt.aces = par.aces;

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
                par.deathProbability += par.probability * (i==10?4.0:1.0) / 13.0;
            }
        }
    }
}
