public class GameMusic extends Music {

    public GameMusic(String fileName, boolean loops) {
        super(fileName, loops);
    }

    // You can override methods if you want custom behavior
    @Override
    public void run() {
        System.out.println("Custom behavior for background music");
        super.run();
    }

    // Add more custom logic if needed
}
