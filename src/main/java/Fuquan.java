import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiwen on 4/20/16.
 */
public class Fuquan
{
	private static final String _url = "jdbc:mysql://127.0.0.1/quantum?&useUnicode=true&characterEncoding=utf-8";
	private static final String _name = "com.mysql.jdbc.Driver";
	private static final String _user = "root";
	private static final String _password = "root";
	private static final String _queryDayPrice =
			"select code,curr_price,p_date from price where code='%s' order by p_date";
	private static final String _queryName = "select name from price where code='%s' limit 1";
	private static final String _queryFenHong =
			"select songgu,zhuanzeng,paixi,gonggao_date,chuquanchuxi_date,guquandengji_date,honggushangshi_date from" +
					" fenhong where code='%s' and process='实施' order by chuquanchuxi_date";
	private static final String _updateFuquanPrice1 = "truncate table fuquanprice";
	private static final String _updateFuquanPrice2 = "insert ignore into fuquanprice values ";

	private static Connection _conn = null;
	private static PreparedStatement _pst = null;

	static
	{
		try
		{
			Class.forName(_name);
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	private static void DBHelper(String sql)
	{
		try
		{
			if (_conn == null || _conn.isClosed())
			{
				_conn = DriverManager.getConnection(_url, _user, _password);
			}
			_pst = _conn.prepareStatement(sql);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static void close()
	{
		try
		{
			_conn.close();
			_pst.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	private static String getName(String code)
	{
		DBHelper(String.format(_queryName, code));
		String name = null;
		try
		{
			ResultSet ret = _pst.executeQuery();
			while (ret.next())
			{
				name = ret.getString("name");
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		} finally
		{
			assert name != null : "Error when try to get name of Code:" + code;
			return name;
		}
	}

	private static List<DayPrice> getDayPrice(String code)
	{
		DBHelper(String.format(_queryDayPrice, code));
		DayPriceList result = new DayPriceList();
		try
		{
			ResultSet ret = _pst.executeQuery();
			while (ret.next())
			{
				DayPrice dp = new DayPrice(ret.getFloat("curr_price"), ret.getString("p_date"));
				result.add(dp);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		} finally
		{
			return result;
		}
	}

	private static List<FenHong> getFenHongOnDate(String code)
	{
		DBHelper(String.format(_queryFenHong, code));
		List<FenHong> result = new ArrayList<FenHong>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try
		{
			ResultSet ret = _pst.executeQuery();
			while (ret.next())
			{
				FenHong fh = new FenHong(ret.getInt("songgu"), ret.getInt("zhuanzeng"), ret.getFloat("paixi"),
						sdf.format(ret.getDate("gonggao_date")), ret.getString("chuquanchuxi_date"),
						ret.getString("guquandengji_date"), ret.getString("honggushangshi_date"));
				result.add(fh);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		assert result.size() <= 1 : "Error when get FenHong for Code:" + code +
				". Result size is larger than 1.";
		return result;
	}

	private static void updateFuquanPrice(List<DayPrice> dayprices, List<FenHong> fenhongs)
	{
		int i = 0, j = 0;
		for (; i < dayprices.size(); i++)
		{
			for (; j < fenhongs.size(); )
			{
				if (i == 0)
				{
					break;
				}
			}
		}
	}

	public static void main(String[] args)
	{
		try
		{
			DBHelper(_updateFuquanPrice1);
			_pst.executeUpdate();
			StringBuilder finalQuery = new StringBuilder(_updateFuquanPrice2);
			BufferedReader br = new BufferedReader(new FileReader(args[0]));
			String line;
			while ((line = br.readLine()) != null)
			{
				String code = line.trim();
				String name = getName(code);
				List<DayPrice> dayPrices = getDayPrice(code);
				List<FenHong> fenHongs = getFenHongOnDate(code);
				updateFuquanPrice(dayPrices, fenHongs);
				StringBuilder sb = new StringBuilder();
				for (DayPrice dp : dayPrices)
				{
					sb.append("('").append(code).append('\'').append(",'").append(name).append('\'').append(",'")
							.append(dp.getValue()).append("','").append(dp.getDate()).append("'),");
				}
				finalQuery.append(sb.toString());
				DBHelper(finalQuery.toString().substring(0, finalQuery.length() - 1));
				_pst.executeUpdate();
				System.out.println(sb.toString());
				finalQuery = new StringBuilder(_updateFuquanPrice2);
			}
			//DBHelper(finalQuery.toString().substring(0, finalQuery.length() - 1));
			//_pst.executeUpdate();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
