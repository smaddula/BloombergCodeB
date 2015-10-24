import java.io.IOException;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
            while (true) {

                if (cash > 30) {
                    Ticker besTicker = null;
                    for (ConcurrentHashMap.Entry<String, Ticker> entry : backgroundDataPopulator.allTickers.entrySet()) {
                        Ticker ticker = entry.getValue();
                        if (!ticker.curOrders.isMinAskAvailable() || ticker.units != 0 || ticker.askUnits > 0)
                            continue;
                        if (besTicker == null) {
                            besTicker = ticker;
                        } else {
                            if (besTicker.curOrders != null && ticker.curOrders != null && ((int) Math.floor(cash / (3 * ticker.curOrders.getMinAskPrice())) != 0)) {
                                if (besTicker.netWorth / besTicker.curOrders.getMinAskPrice() < ticker.netWorth / ticker.curOrders.getMinAskPrice()) {
                                    besTicker = ticker;
                                }
                            }
                        }
                    }
                    if (besTicker != null ) {
                        double minPrice = besTicker.curOrders.getMinAskPrice();
                        double newBidValue = minPrice * 1.001;
                        if(besTicker.bidPrice <newBidValue || besTicker.bidUnits == 0 ){
                           if(besTicker.bidUnits != 0) ec.clearBid(besTicker.Name);
                            ec.placeBid(besTicker.Name, minPrice * 1.001, (int) Math.floor(cash / (3 * minPrice)));
                            besTicker.bidPrice = minPrice * 1.001;
                            besTicker.bidUnits = (int) Math.floor(cash / (3 * minPrice));
                        }
                    }

                }

                cash = ec.getCash();

                for (ConcurrentHashMap.Entry<String, Ticker> entry : backgroundDataPopulator.allTickers.entrySet()) {
                    Ticker ticker = entry.getValue();
                    if (!ticker.curOrders.isMaxBidAvailable() || ticker.units == 0 ||ticker.bidUnits > 0)
                        continue;
                    if ((ticker.curDividend < ticker.startDividend / 2  && (ticker.curOrders.getMaxBidPrice() - ticker.buyPrice > 0) )
                            || ((ticker.curOrders.getMaxBidPrice() - ticker.buyPrice) / ticker.buyPrice < -0.2 ) ||
                            ((ticker.curOrders.getMaxBidPrice() - ticker.buyPrice) / ticker.buyPrice > 0.05 )) {
                        double tmp = ticker.askPrice;
                        ticker.askPrice = ticker.curOrders.getMaxBidPrice() * 0.999;
                        ticker.askUnits = ticker.units;
                        if(ticker.askUnits > 0 && ticker.askPrice > tmp) {
                            ec.clearAsk(ticker.Name);
                            ec.placeAsk(ticker.Name,ticker.askPrice,ticker.askUnits);
                        } else if (ticker.askUnits > 0 && ticker.askPrice < tmp) continue;
                        else ec.placeAsk(ticker.Name, ticker.curOrders.getMaxBidPrice() * 0.999, ticker.units);
                        continue;
                    }
                    /*if ((ticker.curOrders.getMaxBidPrice() - ticker.buyPrice) / ticker.buyPrice > 0.05 ) {
                        ticker.askPrice = ticker.curOrders.getMaxBidPrice() * 0.999;
                        ticker.askUnits = ticker.units;
                        ec.placeAsk(ticker.Name, ticker.curOrders.getMaxBidPrice() * 0.999, ticker.units);
                        continue;
                    }*/
                    if (ticker.curOrders.getMaxBidPrice() - ticker.buyPrice < 0 )
                        continue;

                    /*if (ticker.curDividend < ticker.startDividend / 2 ) {
                        ticker.askPrice = ticker.curOrders.getMaxBidPrice() * 0.999;
                        ticker.askUnits = ticker.units;
                        ec.placeAsk(ticker.Name, ticker.curOrders.getMaxBidPrice() * 0.99, ticker.units);
                        continue;
                    }*/
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

