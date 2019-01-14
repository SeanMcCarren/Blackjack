public class Main {
    public static void main(String[] args) {
        for(int i = 0; i < 10; i++){
            playFullGames();
        }
    }

    public static void playFullGames() {
        CardCounter player = new CardCounter();
        DealerAI dealer = new DealerAI();

        int amountOfGames = 10000000;
        int finalScore = 21; //vary the end score to win something
        int amountOfDecks = 6;
        int rounds = 0;

        int progressBarLength = 20;
        int progressIncrement = 0;
        int progress = 0;

        for(int i = 0; i < amountOfGames; i++){
            rounds += playGameFull(amountOfDecks, player, dealer, 60, finalScore);
            player.resetCount();

            if (i == progressIncrement) {
                progressIncrement = ++progress * amountOfGames / progressBarLength;
                System.out.print("|" + repeat("=", progress-1) + repeat(" ", progressBarLength-progress) + "|\r");
            }
        }

        double result1 = player.getWinnings()/(100.0  * amountOfGames);
        double result2 = player.getWinnings()/(100.0 * rounds);
        //double result2 = player.getAmountOfWins()/ (double) amountOfGames; 
        System.out.println("Money won, in earnings / total games played: " + result1 + ".");
        System.out.println("Money won, in earnings / total rounds played: " + result2 + ".");
        System.out.println("Amount of rounds played: " + rounds + ".");
        System.out.println("Amount of games played: " + amountOfGames + ".");

    }

    public static String repeat(String a, int n) {
        String res = "";
        for (int i = 0; i < n; i++) {
            res += a;
        }
        return res;
    }



    public static int playGame(int decks, CardCounter player, DealerAI dealer, int maxDepth, int finalScore) {
        Stack stack = new Stack(decks);
        int numberOfRounds = 0;

        while(stack.getCards() > maxDepth){
            //new game, so reset scores
            player.reset();
            dealer.reset();
            numberOfRounds++;

            //if count of player is positive, we bet more
            //if the count is negative, we bet less
            int decksRemaining = (int) Math.floor( stack.getCards()/(52.0));
            player.setBet(decksRemaining);
            double betSize = player.getBet();

            //player gets two cards in the beginning
            player.pulled(stack.pull(), false);
            player.pulled(stack.pull(), false);

            //dealer gets one open card and one closed card
            int card1 = stack.pull();
            player.notice(card1);
            dealer.pulled(card1);
            
            int card2 = stack.pull();
            dealer.pulled(card2);

            //actual game starts now
            //System.out.println("Game started!");

            //If player has a blackjack, 21, then the game is already over
            if(player.getScore() == finalScore) {
                if(dealer.getScore() == finalScore) {
                    //System.out.println("Player and dealer have blackjack");
                    continue;
                }
                else { 
                    //System.out.println("Player has blackjack");
                    player.addWinnings(1.5 * betSize);
                    continue;
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

            if(playerScore > finalScore){ //even if dealer loses too: house's edge
                player.addWinnings(-1.0 * betSize);
                continue;
            } else if(dealerScore > finalScore){ //player didnt bust, dealer did.
                player.addWinnings(1.0 * betSize);
                continue;
            } else if(playerScore < dealerScore){ //no busts, but player < dealer
                player.addWinnings(-1.0 * betSize);
                continue;
            } else if(playerScore > dealerScore){ //no busts, but player > dealer
                player.addWinnings(1.0 * betSize);
            }

        }
        return numberOfRounds;
        //System.out.println(player.getWinnings());
    }



    public static int playGameSplit(int decks, CardCounter player, DealerAI dealer, int maxDepth, int finalScore) {
        Stack stack = new Stack(decks);
        int numberOfRounds = 0;

        while(stack.getCards() > maxDepth){
            //new game, so reset scores
            player.reset();
            dealer.reset();
            numberOfRounds++;

            //if count of player is positive, we bet more
            //if the count is negative, we bet less
            int decksRemaining = (int) Math.floor( stack.getCards()/(52.0));
            player.setBet(decksRemaining);
            double betSize = player.getBet();

            //player gets two cards in the beginning
            int playerCard1 = stack.pull();
            int playerCard2 = stack.pull();

            int dealerCard1 = stack.pull();

            //if we can split and want to split
            if (playerCard1 == playerCard2 && player.wantsSplit(playerCard1, dealerCard1)){

                boolean gotBlackjack1 = false;
                boolean gotTied1 = false;
                boolean gotBlackjack2 = false;
                boolean gotTied2 = false;

                player.notice(playerCard2);
                player.notice(dealerCard1);

                int dealerTempCardSplit = stack.pull();
                dealer.pulled(dealerTempCardSplit);
                dealer.pulled(dealerCard1);

                player.pulled(playerCard1, false);
                player.pulled(stack.pull(), false);

                if(player.getScore() == 21){
                    if(dealer.getScore() == 21){
                        gotTied1 = true;
                    } else{
                        gotBlackjack1 = true;
                    }
                }

                while(player.wantsNext(dealerCard1)){
                    player.pulled(stack.pull(), false);
                }

                int playerScore1 = player.getScore();
                player.reset();

                player.pulled(playerCard2, true);
                player.pulled(stack.pull(), false);

                if(player.getScore() == 21){
                    if(dealer.getScore() == 21){
                        gotTied2 = true;
                    } else{
                        gotBlackjack2 = true;
                    }
                }

                while(player.wantsNext(dealerCard1)){
                    player.pulled(stack.pull(), false);
                }

                int playerScore2 = player.getScore();

                player.notice(dealerTempCardSplit);

                while(dealer.wantsNext()){
                    int temp = stack.pull();
                    player.notice(temp);
                    dealer.pulled(temp);
                }

                int dealerScoreSplit = dealer.getScore();

                if (gotBlackjack1){
                    player.addWinnings(1.5 * betSize);
                } else if (gotTied1){
                    //nothing
                } else{
                    player.addWinnings(playerWon(dealerScoreSplit, playerScore1, betSize));
                }

                if (gotBlackjack2){
                    player.addWinnings(1.5 * betSize);
                } else if (gotTied2){
                    //nothing
                } else{
                    player.addWinnings(playerWon(dealerScoreSplit, playerScore2, betSize));
                }

                continue;
            }

            player.pulled(playerCard1, false);
            player.pulled(playerCard2, false);

            //dealer gets one open card and one closed card
            player.notice(dealerCard1);
            dealer.pulled(dealerCard1);
            
            int dealerCard2 = stack.pull();
            dealer.pulled(dealerCard2);

            //actual game starts now
            //System.out.println("Game started!");

            //If player has a blackjack, 21, then the game is already over
            if(player.getScore() == finalScore) {
                if(dealer.getScore() == finalScore) {
                    //System.out.println("Player and dealer have blackjack");
                    continue;
                }
                else { 
                    //System.out.println("Player has blackjack");
                    player.addWinnings(1.5 * betSize);
                    continue;
                }
            }

             //Player's turn
            //System.out.println("Player starts");

            while(player.wantsNext(dealerCard1)){
                //int tempCard1 = stack.pull();
                //System.out.println("pulled a " + tempCard1 + ".");

                player.pulled(stack.pull(), false);
            }

            //the closed card is open to the player
            //System.out.println("Player is done, card is flipped");
            player.notice(dealerCard2);

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

            if(playerScore > finalScore){
                player.addWinnings(-1.0 * betSize);
                continue;
            } else if(dealerScore > finalScore){
                player.addWinnings(1.0 * betSize);
                continue;
            } else if(playerScore < dealerScore){
                player.addWinnings(-1.0 * betSize);
                continue;
            } else if(playerScore > dealerScore){
                player.addWinnings(1.0 * betSize);
            }

        }
        return numberOfRounds;
        //System.out.println(player.getWinnings());
    }

    public static int playGameDouble(int decks, CardCounter player, DealerAI dealer, int maxDepth, int finalScore) {
        Stack stack = new Stack(decks);
        int numberOfRounds = 0;

        while(stack.getCards() > maxDepth){
            //new game, so reset scores
            player.reset();
            dealer.reset();
            numberOfRounds++;

            //if count of player is positive, we bet more
            //if the count is negative, we bet less
            int decksRemaining = (int) Math.floor( stack.getCards()/(52.0));
            player.setBet(decksRemaining);
            double betSize = player.getBet();


            //player gets two cards in the beginning
            player.pulled(stack.pull(), false);
            player.pulled(stack.pull(), false);

            int dealerCard1 = stack.pull();
            player.notice(dealerCard1);
            dealer.pulled(dealerCard1);
            
            int card2 = stack.pull();
            dealer.pulled(card2);


            if (player.wantsDouble(player.getScore(), dealerCard1)){
                
                //one additional card, rule of doubling down
                player.pulled(stack.pull(), false);

                //betsize doubled
                betSize = betSize * 2;

                player.notice(card2);

                //dealer takes his cards
                while (dealer.wantsNext()){
                    int temporary = stack.pull();
                    player.notice(temporary);
                    dealer.pulled(temporary);
                }

                int dealerScore = dealer.getScore();
                int playerScore = player.getScore();

                
                player.addWinnings(playerWon(dealerScore, playerScore, betSize));

                continue;

            }

            //dealer gets one open card and one closed card

            //actual game starts now
            //System.out.println("Game started!");

            //If player has a blackjack, 21, then the game is already over
            if(player.getScore() == finalScore) {
                if(dealer.getScore() == finalScore) {
                    //System.out.println("Player and dealer have blackjack");
                    continue;
                }
                else { 
                    //System.out.println("Player has blackjack");
                    player.addWinnings(1.5 * betSize);
                    continue;
                }
            }

             //Player's turn
            //System.out.println("Player starts");

            while(player.wantsNext(dealerCard1)){
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

            if(playerScore > finalScore){ //even if dealer loses too: house's edge
                player.addWinnings(-1.0 * betSize);
                continue;
            } else if(dealerScore > finalScore){ //player didnt bust, dealer did.
                player.addWinnings(1.0 * betSize);
                continue;
            } else if(playerScore < dealerScore){ //no busts, but player < dealer
                player.addWinnings(-1.0 * betSize);
                continue;
            } else if(playerScore > dealerScore){ //no busts, but player > dealer
                player.addWinnings(1.0 * betSize);
            }
            //if equal nothing happens and we just go on
        }
        return numberOfRounds;
        //System.out.println(player.getWinnings());
    }

    public static int playGameFull(int decks, CardCounter player, DealerAI dealer, int maxDepth, int finalScore) {
        Stack stack = new Stack(decks);
        int numberOfRounds = 0;

        while(stack.getCards() > maxDepth){
            //new game, so reset scores
            player.reset();
            dealer.reset();
            numberOfRounds++;



            //if count of player is positive, we bet more
            //if the count is negative, we bet less
            int decksRemaining = (int) Math.floor( stack.getCards()/(52.0));
            player.setBet(decksRemaining);
            double betSize = player.getBet();

            //player gets two cards in the beginning
            int playerCard1 = stack.pull();
            int playerCard2 = stack.pull();
            player.pulled(playerCard1, false);
            player.pulled(playerCard2, false);

            //dealer gets one open card and one closed card
            int dealerCard1 = stack.pull();
            player.notice(dealerCard1);
            dealer.pulled(dealerCard1);
            
            int dealerCard2 = stack.pull();
            dealer.pulled(dealerCard2);

            if (player.wantsDouble(player.getScore(), dealerCard1)){
                
                //one additional card, rule of doubling down
                player.pulled(stack.pull(), false);

                //betsize doubled
                betSize = betSize * 2;

                player.notice(dealerCard2);

                //dealer takes his cards
                while (dealer.wantsNext()){
                    int temporary = stack.pull();
                    player.notice(temporary);
                    dealer.pulled(temporary);
                }

                int dealerScore = dealer.getScore();
                int playerScore = player.getScore();

                
                player.addWinnings(playerWon(dealerScore, playerScore, betSize));

                continue;

            } else if (playerCard1 == playerCard2 && player.wantsSplit(playerCard1, dealerCard1)){

                boolean gotBlackjack1 = false;
                boolean gotTied1 = false;
                boolean gotBlackjack2 = false;
                boolean gotTied2 = false;

                player.reset();

                //player.notice(playerCard2);
                player.notice(dealerCard1);

                //int dealerTempCardSplit = stack.pull();
                //dealer.pulled(dealerTempCardSplit);
                //dealer.pulled(dealerCard1);

                player.pulled(playerCard1, true);
                player.pulled(stack.pull(), false);

                if(player.getScore() == 21){
                    if(dealer.getScore() == 21){
                        gotTied1 = true;
                    } else{
                        gotBlackjack1 = true;
                    }
                }

                while(player.wantsNext(dealerCard1)){
                    player.pulled(stack.pull(), false);
                }

                int playerScore1 = player.getScore();
                player.reset();

                player.pulled(playerCard2, true);
                player.pulled(stack.pull(), false);

                if(player.getScore() == 21){
                    if(dealer.getScore() == 21){
                        gotTied2 = true;
                    } else{
                        gotBlackjack2 = true;
                    }
                }

                while(player.wantsNext(dealerCard1)){
                    player.pulled(stack.pull(), false);
                }

                int playerScore2 = player.getScore();

                player.notice(dealerCard2);

                while(dealer.wantsNext()){
                    int temp = stack.pull();
                    player.notice(temp);
                    dealer.pulled(temp);
                }

                int dealerScoreSplit = dealer.getScore();

                if (gotBlackjack1){
                    player.addWinnings(1.5 * betSize);
                } else if (gotTied1){
                    //nothing
                } else{
                    player.addWinnings(playerWon(dealerScoreSplit, playerScore1, betSize));
                }

                if (gotBlackjack2){
                    player.addWinnings(1.5 * betSize);
                } else if (gotTied2){
                    //nothing
                } else{
                    player.addWinnings(playerWon(dealerScoreSplit, playerScore2, betSize));
                }

                continue;
            }

            //actual game starts now
            //System.out.println("Game started!");

            //If player has a blackjack, 21, then the game is already over
            if(player.getScore() == finalScore) {
                if(dealer.getScore() == finalScore) {
                    //System.out.println("Player and dealer have blackjack");
                    continue;
                }
                else { 
                    //System.out.println("Player has blackjack");
                    player.addWinnings(1.5 * betSize);
                    continue;
                }
            }

             //Player's turn
            //System.out.println("Player starts");

            while(player.wantsNext(dealerCard1)){
                //int tempCard1 = stack.pull();
                //System.out.println("pulled a " + tempCard1 + ".");

                player.pulled(stack.pull(), false);
            }

            //the closed card is open to the player
            //System.out.println("Player is done, card is flipped");
            player.notice(dealerCard2);

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

            if(playerScore > finalScore){ //even if dealer loses too: house's edge
                player.addWinnings(-1.0 * betSize);
                continue;
            } else if(dealerScore > finalScore){ //player didnt bust, dealer did.
                player.addWinnings(1.0 * betSize);
                continue;
            } else if(playerScore < dealerScore){ //no busts, but player < dealer
                player.addWinnings(-1.0 * betSize);
                continue;
            } else if(playerScore > dealerScore){ //no busts, but player > dealer
                player.addWinnings(1.0 * betSize);
            }

        }
        return numberOfRounds;
        //System.out.println(player.getWinnings());
    }

    public static int playGameInfinite(int decks, CardCounter player, DealerAI dealer, int maxDepth, int finalScore) {
        Stack stack = new Stack(decks);
        int numberOfRounds = 0;

        int cardsDrawn = decks * 13 * 4;

        while(cardsDrawn > maxDepth){
            player.reset();
            dealer.reset();
            numberOfRounds++;

            int decksRemaining = (int) Math.floor( stack.getCards()/(52.0));
            player.setBet(decksRemaining);
            double betSize = player.getBet();

            player.pulled(stack.pullInfinite(), false);
            cardsDrawn--;
            player.pulled(stack.pullInfinite(), false);
            cardsDrawn--;

            int card1 = stack.pullInfinite();
            player.notice(card1);
            dealer.pulled(card1);
            cardsDrawn--;
            
            int card2 = stack.pullInfinite();
            dealer.pulled(card2);
            cardsDrawn--;


            if(player.getScore() == finalScore) {
                if(dealer.getScore() == finalScore) {
                    continue;
                }
                else { 
                    player.addWinnings(1.5 * betSize);
                    continue;
                }
            }

            while(player.wantsNext(card1)){

                player.pulled(stack.pullInfinite(), false);
                cardsDrawn--;

            }

            player.notice(card2);

            while(dealer.wantsNext()){
                int tempCard2 = stack.pullInfinite();

                player.notice(tempCard2);
                dealer.pulled(tempCard2);
                cardsDrawn--;
            }

            int playerScore = player.getScore();
            int dealerScore = dealer.getScore();

            if(playerScore > finalScore){ 
                player.addWinnings(-1.0 * betSize);
                continue;
            } else if(dealerScore > finalScore){ 
                player.addWinnings(1.0 * betSize);
                continue;
            } else if(playerScore < dealerScore){ 
                player.addWinnings(-1.0 * betSize);
                continue;
            } else if(playerScore > dealerScore){ 
                player.addWinnings(1.0 * betSize);
            }

        }
        return numberOfRounds;
    }

    public static double playerWon(int dealerScore, int playerScore, double betSize){
        if (playerScore > 21){
            return -1.0 * betSize;
        } else if (dealerScore > 21){
            return 1.0 * betSize;
        } else if (playerScore < dealerScore){
            return -1.0 * betSize;
        } else if (playerScore > dealerScore){
            return 1.0 * betSize;
        }
        return 0;
    }
}