package cn.sinobest.ypgj.util;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import cn.sinobest.jzpt.framework.utils.JdbcService;



/**
 * 
 * @author: lihaoquan
 * @Description: 调用Oracle数据库中的通用函数方法
 */
public class SqlCallUtil  extends StoredProcedure {

	public SqlCallUtil(DataSource dataSource,String f_sql,Map<String,String> paramMap) {
		super(dataSource,f_sql);
		try{
		setFunction(true);
		declareParameter(new SqlOutParameter("RETURNDATA", Types.VARCHAR));
		Iterator<Entry<String, String>> iter = paramMap.entrySet().iterator();
		while(iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			 declareParameter(new SqlParameter(entry.getKey(), Types.VARCHAR));
		}
		compile();
		}catch (Exception e) {
			System.out.println("缺少返回数据信息 !!!");
		}
	}
}
