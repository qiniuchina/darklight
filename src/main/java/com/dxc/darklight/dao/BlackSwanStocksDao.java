package com.dxc.darklight.dao;

import java.util.List;
import java.util.Date;
import org.nutz.dao.Cnd;
import org.springframework.stereotype.Repository;

import com.dxc.darklight.model.BlackSwanStocks;
import com.dxc.darklight.util.DateUtils;

@Repository
public class BlackSwanStocksDao extends BaseDao{
	public List<BlackSwanStocks> getAllBlackSwanStocks()
	{
		Date dt=new Date();
		String dtString=DateUtils.formatCommonFormat(dt);
		List<BlackSwanStocks> blackSwanStocks = dao.query(BlackSwanStocks.class, Cnd.where("stage_date_str", "=", dtString));
		return blackSwanStocks;
	}

}
