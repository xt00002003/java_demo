package com.dark.mybatis.plugin;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;

import java.sql.Connection;
import java.util.Properties;

/**
 * java_demo
 * User: dark xue
 * Date: 2017/5/6
 * Time: 14:28
 * description:
 */
@Intercepts({
        @Signature(
                type = StatementHandler.class,
                method = "prepare",
                args = {Connection.class}
        )
})
public class PagingPlugin implements Interceptor {

    private Integer defaultPage;
    private Integer defaultPageSize;
    private Boolean defaultUseFlag;
    private Boolean defaultCheckFlag;

    private static final String DEFAULT_PAGE="default.page";
    private static final String DEFAULT_PAGE_SIZE="default.page.size";
    private static final String DEFAULT_USE_FLAG="default.use.flag";
    private static final String DEFAULT_CHECK_FLAG="default.check.flag";


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        return null;
    }

    @Override
    public Object plugin(Object statementHandler) {
        return Plugin.wrap(statementHandler,this);
    }

    @Override
    public void setProperties(Properties properties) {
        String strDefaultPage=properties.getProperty(DEFAULT_PAGE,"1");
        String strDefaultPageSize=properties.getProperty(DEFAULT_PAGE_SIZE,"50");
        String strDefaultUseFlag=properties.getProperty(DEFAULT_USE_FLAG,"false");
        String strDefaultCheckFlag=properties.getProperty(DEFAULT_CHECK_FLAG,"false");
        this.defaultPage=Integer.parseInt(strDefaultPage);
        this.defaultPageSize=Integer.parseInt(strDefaultPageSize);
        this.defaultUseFlag=Boolean.parseBoolean(strDefaultUseFlag);
        this.defaultCheckFlag=Boolean.parseBoolean(strDefaultCheckFlag);


    }
}
