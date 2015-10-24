import java.util.ArrayList;

/**
 * Created by venkata on 10/23/15.
 */
public class Ticker {

    String Name;
    int units;
    double startDividend;
    double curDividend;
    double buyPrice;

    public Ticker(String Name, int units, double dividend, double buyPrice) {
        this.Name = Name;
        this.units = units;
        this.startDividend = dividend;
        this.buyPrice = buyPrice;
    }

    public void addUnits(double price, int units) {
        this.buyPrice = (this.buyPrice*this.units + price*units)/(this.units + units);
        this.units += units;
    }

    public void removeUnits(double price, int units) {
        this.buyPrice = (this.buyPrice*this.units - price*units)/(this.units - units);
        this.units -= units;

    }

    public void addDividend(double dividend) {
        this.curDividend = dividend;
    }
}
