abstract class Player {
    protected int score;
    protected int betSize;
    protected double winnings;

    public Player() {
        this.score = 0;
    }

    public void pulled(int value) {
        if(value == 1){
            if(score + 11 <= 21){
                this.score += 11;
            }
        } else{
            this.score += value;
        }
    }

    public int getScore() {
        return this.score;
    }

    //public abstract boolean wantsNext();       //removed int score inside function call


    //public abstract int getBet();
    

    public double getWinnings(){
        return winnings;
    }

    public void addWinnings(double winnings){
        this.winnings += winnings;
    }

    public void resetScore(){
        this.score = 0;
    }


}