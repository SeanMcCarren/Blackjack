abstract class Player {
    protected int score;
    protected int betSize;
    protected int winnings;

    public Player() {
        this.score = 0;
    }

    public void pulled(int value) {
        this.score += value;
    }

    public int getScore() {
        return this.score;
    }

    public abstract boolean wantsNext(int score){

    }

    public abstract int getBet(){
    }

    public int getWinnings(){
        return winnings;
    }


}