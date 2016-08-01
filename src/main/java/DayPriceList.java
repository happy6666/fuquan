import java.util.ArrayList;

/**
 * Created by yiwen on 6/23/16.
 */
public class DayPriceList extends ArrayList<DayPrice> {
	@Override
	public boolean add(DayPrice dayPrice) {
		if (super.size() > 0) {
			dayPrice.setPreDayPrice(super.get(super.size() - 1));
		}
		super.add(dayPrice);
		return true;
	}
}
