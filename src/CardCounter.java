class CardCounter extends Player{
    protected int count = 0;    //holds the current count of the game
    protected double betSize = 1;

    public CardCounter(){
        super();
    }

    //implemented the basic strategy for blackjack
    public boolean wantsNext(int card){

        int score = getScore();

        if(this.amountOfAcesInHand == 0){
            if(score < 12){
                return true;
            }
            else if(score == 12 && (card == 4 || card == 5 || card == 6)){
                return false;
            }
            else if(score == 12){
                return true;
            }
            else if(13 <= score && score <= 16 && 2 <= card && card <= 6){
                return false;
            }
            else if(13 <= score && score <= 16){
                return true;
            }
            else{
                return false;
            }
        } else {
            if(score <= 17){
                return true;
            }
            else if (score == 18 && (card > 8 || card == 1)){
                return true;
            }
            else {
                return false;
            }
        }
    }
    
    public void pulled(int value){
        super.pulled(value);

        notice(value);

    }
   
    public void setBet(int decks){
        /*if (count > 1){
            betSize = ( count/decks - 1.0);
        }
        else if ( count <= 0){
            betSize = 1;
        }*/

        this.betSize = 1;
    }

    public void notice(int value){
        if (value == 1 || value == 10) {
            count--;
        } else if ( 1 < value && value <= 6) {
            count++;
        }
    }

    public int getCount(){
        return count;
    }

    public double getBet(){
        return betSize; 
    }

    public void resetCount(){
        this.count = 0;
    }

}