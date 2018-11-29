public class DealerAI extends Player {

    public DealerAI() {
        super();
    }

    public boolean wantsNext() {
        return this.score <= 16 ? true: false;
    }

}