abstract class Player {
    protected int score;

    public Player() {
        this.score = 0;
    }

    public void pulled(int value) {
        this.score += value;
    }

    public int getScore() {
        return this.score;
    }


}