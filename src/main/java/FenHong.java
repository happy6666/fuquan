import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yiwen on 4/21/16.
 */
public class FenHong
{
    private int _songgu, _zhuanzeng;
    private float _paixi;
    private String _gonggao, _chuquanchuxi, _guquandengji, _honggushangshi;

    public FenHong(int sg, int zz, float px, String gg, String cc, String gq, String hg)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        _songgu = sg;
        _zhuanzeng = zz;
        _paixi = px;
        _gonggao = toDateStr(sdf, gg);
        _chuquanchuxi = toDateStr(sdf, cc);
        _guquandengji = toDateStr(sdf, gq);
        _honggushangshi = toDateStr(sdf, hg);
    }

    private String toDateStr(SimpleDateFormat sdf, String input)
    {
        if (input == null)
        {
            return null;
        }
        try
        {
            return sdf.format(sdf.parse(input));
        } catch (Exception e)
        {
            return null;
        }
    }

    public double getSonggu()
    {
        return (_songgu+_zhuanzeng)/10d;
    }

    public double getPaixi()
    {
        return _paixi/10d;
    }
}
