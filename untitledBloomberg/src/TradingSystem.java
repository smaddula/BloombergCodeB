import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by venkata on 10/23/15.
 */
public class TradingSystem {
    private ExchangeClient ec;
    public TradingSystem() throws IOException {
        this.ec = getExchangeClient("TradingSystem");
    }

    static HashMap<String, ExchangeClient> exchangeClients = new HashMap<String, ExchangeClient>();
    static ExchangeClient getExchangeClient(String whoIsThis) throws IOException {
        if(exchangeClients.containsKey(whoIsThis.toLowerCase())){
            return exchangeClients.get(whoIsThis.toLowerCase());
        }
        ExchangeClient ec= new ExchangeClient();
        exchangeClients.put(whoIsThis.toLowerCase(),ec);
        return ec;
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        final TradingSystem ts = new TradingSystem();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                for(String key : exchangeClients.keySet()){
                    ExchangeClient ec = exchangeClients.get(key);

                    ec.pout.println("CLOSE_CONNECTION");
                    ec.pout.close();
                    try {
                        ec.bin.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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

