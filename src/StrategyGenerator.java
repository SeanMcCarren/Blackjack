public class StrategyGenerator {
    public static void main(String[] args) {
        
        APHD root = new APHD();
        root.probability = 1.0;
        traverse(root, 17); // as root we can pass our first card

        /* Nitty gritty manual indexing for checking data structure
        APHD obj = root.options.get(9).options.get(8);
        System.out.println(obj.value);
        System.out.println(obj.probability);
        */

        /* This should equal 1, it is sum over all odds
        System.out.println(sum(getValueProbability(root)) + root.deathProbability);
        */
    }

    private static double sum(double[] arr) {
        double result = 0;
        for (double val : arr) {
            result += val;
        }
        return result;
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

    // firstCard = true implies the root is already a chosen card
    public static double[] getValueProbability(APHD root, boolean firstCard) {
        //result[0] is odd of blackjack
        //result[i] is odd of ending with i (of course you dont end with ...)
        double[] result = new double[22];
        getVPHelper(root, firstCard ? 1:0, result);
        return result;
    }

    public static double[] getValueProbability(APHD root) {
        return getValueProbability(root, false);
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