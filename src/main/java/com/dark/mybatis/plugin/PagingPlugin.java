package com.dark.mybatis.plugin;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

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
                args = {Connection.class, Integer.class }
        )
})
public class PagingPlugin implements Interceptor {

    private final static Logger LOG = LoggerFactory.getLogger(PagingPlugin.class);

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
        StatementHandler statementHandler=getUnProxyObject(invocation);
        MetaObject metaObject= SystemMetaObject.forObject(statementHandler);
        String sql=(String) metaObject.getValue("delegate.boundSql.sql");
        if(!checkSelect(sql)){
            return invocation.proceed();
        }
        BoundSql boundSql=(BoundSql)metaObject.getValue("delegate.boundSql");
        Object parameterObject=boundSql.getParameterObject();
        PageParams pageParams=getPageParams(parameterObject);
        if (pageParams==null){
            return invocation.proceed();
        }

        Integer pageNum=pageParams.getPage()==null?this.defaultPage:pageParams.getPage();
        Integer pageSize=pageParams.getPageSize()==null?this.defaultPageSize:pageParams.getPageSize();
        Boolean useFlag=pageParams.getUseFlag()==null?this.defaultUseFlag:pageParams.getUseFlag();
        Boolean checkFlag=pageParams.getCheckFlag()==null?this.defaultCheckFlag:pageParams.getCheckFlag();
        if(!useFlag){
            return invocation.proceed();
        }

        int total=getTotal(invocation,metaObject,boundSql);
        setTotalToPageParams(pageParams,total,pageSize);

        checkPage(checkFlag,pageNum,pageParams.getTotalPage());


        return changeSQL(invocation,metaObject,boundSql,pageNum,pageSize);
    }

    private StatementHandler getUnProxyObject(Invocation invocation){
        StatementHandler statementHandler=(StatementHandler)invocation.getTarget();
        MetaObject metaObject= SystemMetaObject.forObject(statementHandler);
        Object obj=null;
        while (metaObject.hasGetter("h")){
            obj=metaObject.getValue("h");
        }
        if (obj==null){
            return statementHandler;
        }

        return (StatementHandler)obj;

    }

    private Boolean checkSelect(String sql){
        String trimSql=sql.trim();
        int index=trimSql.toLowerCase().indexOf("select");
        return index==0;
    }

    private PageParams getPageParams(Object parameterObject){
        if (parameterObject==null){
            return null;
        }
        PageParams pageParams=null;
        if (parameterObject instanceof Map){
            Map<String,Object> paramMap=(Map<String,Object>)parameterObject;
            Set<String> keySet=paramMap.keySet();
            Iterator<String> iterator=keySet.iterator();
            while (iterator.hasNext()){
                String key=iterator.next();
                Object value=paramMap.get(key);
                if (value instanceof PageParams){
                    return (PageParams) value;
                }
            }
        }else if(parameterObject instanceof PageParams){
            pageParams=(PageParams)parameterObject;
        }
        return pageParams;
    }


    private Integer getTotal(Invocation invocation,MetaObject metaObject,BoundSql boundSql){
        MappedStatement mappedStatement=(MappedStatement)metaObject.getValue("delegate.mappedStatement");
        Configuration configuration=mappedStatement.getConfiguration();
        String sql=(String) metaObject.getValue("delegate.boundSql.sql");
        String countSql="select count(*) as total from ("+sql+") $_paging";
        LOG.debug("the countSql is :" +countSql);
        Connection connection=(Connection) invocation.getArgs()[0];
        PreparedStatement ps=null;
        int total=0;
        try {
            ps=connection.prepareStatement(countSql);
            BoundSql countBoundSql=new BoundSql(configuration,countSql,boundSql.getParameterMappings(),boundSql.getParameterObject());
            ParameterHandler parameterHandler=new DefaultParameterHandler(mappedStatement,boundSql.getParameterObject(),countBoundSql);
            parameterHandler.setParameters(ps);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                total=rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (ps!=null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }

        return total;

    }

    private void checkPage(Boolean checkFlag,Integer pageNum,Integer pageTotal) throws Exception{
        if (checkFlag){
            if (pageNum>pageTotal){
                throw new Exception("select error! pageNum>pageTotal");
            }
        }
    }

    private void setTotalToPageParams(PageParams pageParams,Integer total,Integer pageSize){
        pageParams.setTotal(total);
        Integer totalPage=total%pageSize==0?total/pageSize:total/pageSize+1;
        pageParams.setTotalPage(totalPage);
    }

    private Object changeSQL(Invocation invocation,MetaObject metaObject,BoundSql boundSql,Integer page,Integer pageSize)throws Exception{
        String sql=(String) metaObject.getValue("delegate.boundSql.sql");
        String newSql="select *  from ( "+sql+" ) $_paging_table limit ? , ? ";
        metaObject.setValue("delegate.boundSql.sql",newSql);
        PreparedStatement ps=(PreparedStatement)invocation.proceed();
        int count=ps.getParameterMetaData().getParameterCount();
        ps.setInt(count-1,(page-1)*pageSize);
        ps.setInt(count,pageSize);
        return ps;

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
