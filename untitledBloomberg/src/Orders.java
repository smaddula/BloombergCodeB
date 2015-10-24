import java.util.ArrayList;
import java.util.List;

/**
 * Created by venkata on 10/24/15.
 */
public class Orders {


    public List<Trade> getAskList() {
        return askList;
    }

    public void setAskList(List<Trade> askList) {
        this.askList = askList;
    }

    public List<Trade> getBidList() {
        return bidList;
    }

    public void setBidList(List<Trade> bidList) {
        this.bidList = bidList;
    }

    List<Trade> bidList;
    List<Trade> askList;

    public Orders() {
        this.bidList = new ArrayList<Trade>();
        this.askList= new ArrayList<Trade>();
    }

    public void addToBidList(Trade myTrade) {
        this.bidList.add(myTrade);
    }

    public void addToAskList(Trade myTrade) {
        this.askList.add(myTrade);
    }


}