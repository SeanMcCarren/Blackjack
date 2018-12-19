import java.util.ArrayList;

public class StrategyGenerator {

    private static double[] valueProbability;
    private static int dealerUpperLimit = 17;

    public static void main(String[] args) {

        getValueProbabilityDealer();

        printArray(valueProbability);

        APHD root = new APHD();
        root.probability = 1.0;
        traverse(root, 21); //to create all options
        bestMethodTraversal(root); //we should ignore method suggestions if depth <= 2, but i dont do that here

        //root.options.get(4).options.get(6).print(new ArrayList<Integer>() {{add(6); add(8);}}, "");

        System.out.println(root.expOutcomeBestMethod);
    }

    private static void printArray(double[] arr) {
        for ( int i = 0; i < arr.length; i++ ) {
            System.out.println(i + ":\t" + arr[i]);
        }
    }

    private static double probabilityDeathGivenPathTakeOne(APHD obj) {
        double prob = obj.deathProbability;
        for (APHD child : obj.options) {
            prob -= child.deathProbability;
        }
        return prob/obj.probability;
    }

    private static double sum(double[] arr) {
        double result = 0;
        for (double val : arr) {
            result += val;
        }
        return result;
    }

    private static void bestMethodTraversal(APHD node) {
        double expOutcomeNoTake = expectedOutcomeNoTake(node);
        if (node.options != null) {
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

    private static double expectedOutcomeNoTake(APHD node) {
        return valueProbability[22] + dealerOddsCumulative(node.value - 1)
                - (dealerOddsCumulative(21) - dealerOddsCumulative(node.value + 1));
    }

    private static void getVPHelper(APHD root, int depth, double[] storage) {
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

    private static double dealerOddsCumulative(int x) {
        if (valueProbability == null) {
            getValueProbabilityDealer();
        }
        double cumulative = 0.0;
        for (int i = dealerUpperLimit; i <= (x > 21 ? 21 : x); i++) {
            cumulative += valueProbability[i];
        }
        if (x == 21) {
            cumulative += valueProbability[0];
        }
        return cumulative;
    }

    // firstCard = true implies the root is already a chosen card
    public static double[] getValueProbability(APHD root, boolean firstCard) {
        //result[0] is odd of blackjack
        //result[i] is odd of ending with i (of course you dont end with ...)
        //result[22] is death probability
        double[] result = new double[23];
        getVPHelper(root, firstCard ? 1:0, result);
        result[22] = root.deathProbability;
        return result;
    }

    public static void getValueProbabilityDealer() {
        APHD root = new APHD();
        root.probability = 1.0;
        traverse(root, dealerUpperLimit);
        valueProbability = getValueProbability(root, false);
    }
  
    // passing limit = 17 yields dealer strat. limit = 21 yields always play strat
    public static void traverse(APHD par, int lim) {
        if (par.value >= lim) {
            return;
        }
        for (int i = 2; i <= 11; i++) {
            // aka: not: go over && hard hand && i != 11
            if (par.value + i <= 21 || par.soft || i == 11) {
                
                APHD opt = new APHD();
                opt.latestCard = i;
                opt.parent = par;
                opt.probability = par.probability * (i==10?4.0:1.0) / 13.0;
                opt.value = par.value;

                // dealing with aces can be tricky

                // stay under 21, no ace
                if (opt.value + i <= 21 && i != 11) {
                    opt.value += i;
                    opt.soft = par.soft;
                //go over 21 but you do have a soft hand
                } else if (opt.value + i > 21 && par.soft) {
                    opt.value -= 10;
                    opt.soft = false;
                    //go over with another ace
                    if (i == 11 && opt.value + 11 > 21) {
                        opt.value += 1;
                    } else if (i == 11) {
                        opt.value += 11;
                        opt.soft = true;
                    //go over with something else
                    } else {
                        opt.value += i;
                    }
                //card was ace
                } else if (i == 11) {
                    //went under
                    if (opt.value + 11 <= 21) {
                        opt.value += 11;
                        opt.soft = true;
                    //go over. Notice the hand must be hard here
                    } else if (opt.value + 11 > 21) {
                        opt.value += 1;
                        opt.soft = false;
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
