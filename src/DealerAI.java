public class DealerAI extends Player {

    public DealerAI() {
        super();
    }

    public boolean wantsNext() {
        return getScore() <= 16 ? true: false;
    }

}