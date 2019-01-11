public class Player {
    protected int score;
    protected int betSize;
    protected double winnings;
    protected int amountOfAcesInHand;

    public Player() {
        this.score = 0;
        this.betSize = 1;
        this.winnings = 0.0;
        this.amountOfAcesInHand = 0;
    }

    public void pulled(int value) {
        if(value == 1){
            this.amountOfAcesInHand++;
            this.score += 11;
        } else {
            this.score += value;
        }
        if (this.score > 21 && this.amountOfAcesInHand > 0) {
            this.amountOfAcesInHand--;
            this.score -= 10;
        }
    }

    public int getScore() {
        return this.score;
    }

    //public abstract boolean wantsNext();       //removed int score inside function call    

    public double getWinnings(){
        return winnings;
    }

    public void addWinnings(double winnings){
        this.winnings += winnings;
    }

    public void reset(){
        this.score = 0;
        this.amountOfAcesInHand = 0;
    }

}