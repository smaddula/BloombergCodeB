import sun.security.krb5.internal.Ticket;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by siddardha on 10/23/2015.
 */
public class BackgroundDataPopulator implements Runnable {

    ConcurrentHashMap<String, Ticker> allTickers;
    ExchangeClient ec;

    public BackgroundDataPopulator() throws IOException {
        allTickers = new ConcurrentHashMap<String, Ticker>();
        ec = TradingSystem.getExchangeClient("TradingSystem");
        initialize();
    }

    private void initialize() throws IOException {
        for (SecurityDTO security : ec.getSecurities()) {
            if (!allTickers.containsKey(security.tickerName)) {
                allTickers.put(security.tickerName, new Ticker(security.tickerName));
            }
            Ticker ticker = allTickers.get(security.tickerName);
            ticker.startDividend = security.dividend;
            ticker.volatility = security.volatality;
            ticker.netWorth = security.netWorth;
        }

        for (ConcurrentHashMap.Entry<String, Ticker> entry : allTickers.entrySet()) {
            entry.getValue().curOrders = ec.getOrders(entry.getKey());
        }
    }

    @Override
    public void run() {

        //while(true){
        try {
            //Thread.sleep(20);
            HashMap<String, SecurityDTO> latestDividendValues = ec.getMySecurities();
            for (HashMap.Entry<String, SecurityDTO> entry : latestDividendValues.entrySet()) {

                Ticker ticker = allTickers.get(entry.getKey());
                ticker.updateCurDividend(entry.getValue().dividend);
                int tmp = ticker.units;
                ticker.units = entry.getValue().units;
                if (ticker.units > tmp) ticker.bidUnits = ticker.bidUnits - tmp;
                else if (ticker.units < tmp) ticker.askUnits = ticker.askUnits - tmp;
                ticker.buyPrice = ticker.bidPrice;

            }

            for (SecurityDTO security : ec.getSecurities()) {
                Ticker ticker = allTickers.get(security.tickerName);
                ticker.history.add( security.netWorth);
            }

            for (ConcurrentHashMap.Entry<String, Ticker> entry : allTickers.entrySet()) {
                entry.getValue().curOrders = ec.getOrders(entry.getKey());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        //}
    }

}
