/**
 * Created by yiwen on 4/20/16.
 */
public class DayPrice
{
	private String _date;
	private double _value;
	private double _realPrice;
	private DayPrice _preDayPrice;
	private double _ratio;

	public DayPrice(double val, String date)
	{
		this._date = date;
		this._value = val;
		_preDayPrice = null;
		_ratio = -1;
	}

	public void setPreDayPrice(DayPrice val)
	{
		this._preDayPrice = val;
		this._ratio = (_value - val._value) / val._value;
	}

	public boolean equals(Object dp)
	{
		if (dp instanceof DayPrice)
		{
			return ((DayPrice) dp)._date.equals(this._date);
		} else
		{
			return false;
		}
	}

	public double getValue()
	{
		return _value;
	}

	public String getDate()
	{
		return _date;
	}

	public void calculateRealPrice(FenHong fenHong )
	{
		if (fenHong==null)
		{
			if(_preDayPrice!=null)
			{
				_realPrice=_preDayPrice._realPrice*(1+_ratio);
			}else
			{
				_realPrice=_value;
			}
		}else{
			(fenHong.getSonggu()+1)
		}
	}

}
