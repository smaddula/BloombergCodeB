import jdk.internal.util.xml.impl.Pair;

import java.util.ArrayList;

/**
 * Created by venkata on 10/23/15.
 */
public class Ticker {

    String Name;
    int units = 0;
    double netWorth = 0;
    double startDividend = 0;
    double curDividend = 0;
    double buyPrice = 0;
    double volatility = 0;
    Orders curOrders = null;
    int bidUnits = 0;
    double bidPrice = 0;
    int askUnits = 0;
    double askPrice = 0;
    ArrayList<Double> history = new ArrayList<Double>();

    public Ticker(String Name) {
        this.Name = Name;
    }

    public void addUnits(double price, int units) {
        this.buyPrice = (this.buyPrice*this.units + price*units)/(this.units + units);
        this.units += units;
    }

    public void removeUnits(double price, int units) {
        this.buyPrice = (this.buyPrice*this.units - price*units)/(this.units - units);
        this.units -= units;

    }

    public void updateCurDividend(double dividend) {
        this.curDividend = dividend;
    }

    public boolean isHistoricPositive(){
        int count = 0;
        int j=0;
        for(int i=history.size()-1;i>=1&&j<10;j++,i--){
            if(history.get(i)>history.get(i-1))
                count++;
            else
                count--;
        }
        return count>0;
    }
}
