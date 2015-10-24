import sun.security.krb5.internal.Ticket;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by siddardha on 10/23/2015.
 */
public class BackgroundDataPopulator implements Runnable {

    ConcurrentHashMap<String , Ticker> allTickers;
    ExchangeClient ec;
    public BackgroundDataPopulator() throws IOException {
        allTickers = new ConcurrentHashMap<String, Ticker>();
        ec = TradingSystem.getExchangeClient("BackgroundDataPopulator");
        initialize();
    }

    private void initialize() throws IOException {
        for(SecurityDTO security : ec.getSecurities()){
            if(!allTickers.containsKey(security.tickerName)){
                allTickers.put(security.tickerName , new Ticker(security.tickerName) );
            }
            Ticker ticker = allTickers.get(security.tickerName);
            ticker.startDividend = security.dividend;
            ticker.volatility = security.volatality;
            ticker.netWorth = security.netWorth;
        }

        for(ConcurrentHashMap.Entry<String, Ticker> entry : allTickers.entrySet()){
            entry.getValue().curOrders = ec.getOrders(entry.getKey());
        }
    }

    @Override
    public void run() {

        while(true){
            try {
                Thread.sleep(1000);
                HashMap<String,SecurityDTO> latestDividendValues = ec.getMySecurities();
                for( HashMap.Entry<String, SecurityDTO> entry : latestDividendValues.entrySet() ){

                    Ticker ticker = allTickers.get(entry.getKey());
                    ticker.updateCurDividend(entry.getValue().dividend);
                    ticker.units = entry.getValue().units;
                    ticker.buyPrice = ticker.bidPrice;
                    ticker.bidPrice = 0;
                    ticker.bidUnits = 0;
                }
                for(ConcurrentHashMap.Entry<String, Ticker> entry : allTickers.entrySet()){
                    entry.getValue().curOrders = ec.getOrders(entry.getKey());
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
