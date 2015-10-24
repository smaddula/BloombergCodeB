import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by siddardha on 10/23/2015.
 */
public class BackgroundDataPopulator implements Runnable {

    ArrayList<Ticker> allTickers;
    ExchangeClient ec;
    public BackgroundDataPopulator(ArrayList<Ticker> array) throws IOException {
        allTickers = array;
        ec = TradingSystem.getExchangeClient("BackgroundDataPopulator");
    }
    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(1000);
                for(Ticker ticker : allTickers ){

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
