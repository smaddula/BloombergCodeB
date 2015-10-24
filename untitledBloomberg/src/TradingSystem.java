import java.io.IOException;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by venkata on 10/23/15.
 */
public class TradingSystem implements Runnable {
    private ExchangeClient ec;

    BackgroundDataPopulator backgroundDataPopulator;

    public TradingSystem() throws IOException {
        backgroundDataPopulator = new BackgroundDataPopulator();
        Thread bakgroundThread = new Thread(backgroundDataPopulator);
        bakgroundThread.start();
        this.ec = getExchangeClient("TradingSystem");
    }

    static ConcurrentHashMap<String, ExchangeClient> exchangeClients = new ConcurrentHashMap<String, ExchangeClient>();

    static ExchangeClient getExchangeClient(String whoIsThis) throws IOException {
        if (exchangeClients.containsKey(whoIsThis.toLowerCase())) {
            return exchangeClients.get(whoIsThis.toLowerCase());
        }
        ExchangeClient ec = new ExchangeClient();
        exchangeClients.put(whoIsThis.toLowerCase(), ec);
        return ec;
    }


    @Override
    public void run() {

        try {
            Double cash = ec.getCash();
            while (cash > 30) {

                Ticker besTicker=null;
                for (ConcurrentHashMap.Entry<String, Ticker> entry : backgroundDataPopulator.allTickers.entrySet()) {
                    Ticker ticker = entry.getValue();
                    if(!ticker.curOrders.isMinAskAvailable())
                        continue;
                    if(besTicker == null ){
                        besTicker = ticker;
                    }else{
                        if(besTicker.curOrders != null && ticker.curOrders !=null) {
                            if (besTicker.netWorth / besTicker.curOrders.getMinAskPrice() < ticker.netWorth / ticker.curOrders.getMinAskPrice()) {
                                besTicker = ticker;
                            }
                        }
                    }
                }
                if(besTicker!=null) {
                    double minPrice = besTicker.curOrders.getMinAskPrice();
                    ec.placeBid(besTicker.Name, minPrice * 1.001, (int) Math.floor(cash / (3 * minPrice)));
                }

                cash = ec.getCash();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*while(true){

        }*/
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        final TradingSystem ts = new TradingSystem();
        Thread algorithmThread = new Thread(ts);
        algorithmThread.start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                for (String key : exchangeClients.keySet()) {
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



        /*while (true) {
            System.out.println(ts.ec.getCash());
            Thread.sleep(1000);
        }*/
    }

}

