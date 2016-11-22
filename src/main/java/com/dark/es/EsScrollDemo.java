package com.dark.es;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by darkxue on 17/11/16.
 * this demo is show us  how  to  use  es scroll to get paged data
 * if the cms need the pagination  data , you should  use  from & size to  get data.
 */
public class EsScrollDemo {
    private static final Logger logger = LoggerFactory.getLogger(EsScrollDemo.class);

    private static final String INDEX="p_channel";
    private static final String TYPE="devices";

    private static String scrollId="";
    public static void main(String[] args){
//        testSearchScroll();
        testSearchScrollScan();
    }

    /**
     * 在生成游标的同时，不会返回数据。
     */
    public static void testSearchScrollScan(){
        System.out.println("scroll scan 模式启动！");
        Client client = EsClientFactory.getEsTransportClient();
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        SearchRequestBuilder request = client.prepareSearch(INDEX).setTypes(TYPE).setQuery(queryBuilder)
                .setSize(2).setScroll(TimeValue.timeValueMinutes(1)).setSearchType(SearchType.SCAN);

        logger.debug(request.toString());
        SearchResponse scrollResponse = request.execute().actionGet();
        long count = scrollResponse.getHits().getTotalHits();//第一次不返回数据
        for(int i=0,sum=0; sum<count; i++){
            System.out.println("foreach num is :"+i);
            scrollResponse = client.prepareSearchScroll(scrollResponse.getScrollId())
                    .setScroll(TimeValue.timeValueMinutes(8))
                    .execute().actionGet();
            sum += scrollResponse.getHits().hits().length;
            SearchHit[] result=scrollResponse.getHits().hits();
            for (SearchHit item:result){
                System.out.println("id--------->"+item.id());
                System.out.println("username--------->"+item.getSource().get("username"));
                System.out.println("password--------->"+item.getSource().get("password"));
            }
            System.out.println("总量"+count+" 已经查到"+sum);
        }
    }

    /**
     * 在生成游标的同时，会返回执行size的数据。
     */
    public static void testSearchScroll(){
        System.out.println("scroll scan 模式启动！");
        Client client = EsClientFactory.getEsTransportClient();
        SearchHit[] result;
        if (StringUtils.isEmpty(scrollId)){
            QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
            SearchRequestBuilder request = client.prepareSearch(INDEX).setTypes(TYPE).setQuery(queryBuilder)
                    .setSize(5).setScroll(TimeValue.timeValueMinutes(1));

            logger.debug(request.toString());
            SearchResponse scrollResponse = request.execute().actionGet();
            result=scrollResponse.getHits().hits();
            for (SearchHit item:result){
                System.out.println("id--------->"+item.id());
                System.out.println("username--------->"+item.getSource().get("username"));
                System.out.println("password--------->"+item.getSource().get("password"));
            }
            scrollId=scrollResponse.getScrollId();
            testSearchScroll();
        }else {
            SearchResponse scrollResponse = client.prepareSearchScroll(scrollId)
                    .setScroll(TimeValue.timeValueMinutes(8))
                    .execute().actionGet();
            result=scrollResponse.getHits().hits();
            for (SearchHit item:result){
                System.out.println("id--------->"+item.id());
                System.out.println("username--------->"+item.getSource().get("username"));
                System.out.println("password--------->"+item.getSource().get("password"));
            }
            if (result.length>0){
                scrollId=scrollResponse.getScrollId();
                testSearchScroll();
            }

        }

    }

    public boolean clearScroll(String scrollId){

        Client esClient = EsClientFactory.getEsTransportClient();
        ClearScrollRequestBuilder clearScrollRequestBuilder = esClient.prepareClearScroll();
        clearScrollRequestBuilder.addScrollId(scrollId);
        ClearScrollResponse response = clearScrollRequestBuilder.get();

        return response.isSucceeded();
    }
}
