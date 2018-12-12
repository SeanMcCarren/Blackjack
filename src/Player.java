public class Player {
    protected int score;
    protected int betSize;
    protected double winnings;
    protected int amountOfWins;
    protected int amountOfAcesInHand;

    public Player() {
        this.score = 0;
        this.amountOfWins = 0;
        this.betSize = 1;
        this.winnings = 0.0;
        this.amountOfAcesInHand = 0;
    }

    public void pulled(int value) {
        if(value == 1){
            this.amountOfAcesInHand++;
        }
        this.score += value;
    }

    public int getScore() {
        return this.score + ((this.amountOfAcesInHand != 0 && this.score <= 11) ? 10 : 0);
    }

    //public abstract boolean wantsNext();       //removed int score inside function call    

    public double getWinnings(){
        return winnings;
    }

    public void addWinnings(double winnings){
        this.winnings += winnings;
    }

    public void resetScore(){
        this.score = 0;
    }

    public void won(){
        this.amountOfWins++;
    }

    public int getAmountOfWins(){
        return amountOfWins;
    }
}