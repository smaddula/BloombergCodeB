import java.io.IOException;

/**
 * Created by venkata on 10/23/15.
 */
public class TradingSystem {
    ExchangeClient ec;
    public TradingSystem() throws IOException {
        this.ec = new ExchangeClient();
    }
    public static void main(String[] args) throws IOException, InterruptedException {

        final TradingSystem ts = new TradingSystem();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                ts.ec.pout.println("CLOSE_CONNECTION");
                ts.ec.pout.close();
                try {
                    ts.ec.bin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Shutdown hook ran!");
            }
        });

        while (true) {
            System.out.println(ts.ec.getCash());
            Thread.sleep(1000);

        }
    }

}

