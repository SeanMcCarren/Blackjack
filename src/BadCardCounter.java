class BadCardCounter extends Player{
    protected int count = 0;    //holds the current count of the game
    protected double betSize = 1;

    public BadCardCounter(){
        super();
    }

    //implemented the basic strategy for blackjack
    public boolean wantsNext(int card){

        int score = getScore();

        return score <= 16;
        
    }
    
    public void pulled(int value){
        super.pulled(value);

        notice(value);

    }
   
    public void setBet(int decks){
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