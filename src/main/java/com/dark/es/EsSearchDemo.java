package com.dark.es;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.sort.SortOrder;

import java.util.HashMap;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.constantScoreQuery;

/**
 * Created by darkxue on 18/11/16.
 * this class  show  how  to use
 */
public class EsSearchDemo {

    private static Client client=EsClientFactory.getEsTransportClient();
    private static final String INDEX="p_channel";
    private static final String TYPE="devices";
    /**
     * this method  show  how  to  use  must  and range  query  data from es
     * the String value explain what the value is needed
     */
    public static void  mustQuery(){
        BoolQueryBuilder query =
                QueryBuilders.boolQuery()
                        .must(QueryBuilders.termQuery("day", "value"));

        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("exposeNum");
        rangeQuery.gte("minExpose");
        rangeQuery.lt("maxExpose");

        query.must(rangeQuery);
        SearchRequestBuilder request =
                client.prepareSearch(INDEX).setTypes(TYPE).setQuery(query).addSort("sortParam", SortOrder.DESC);

        SearchResponse response = request.execute().actionGet();
    }

    /**
     * 插入一条自带ID的数据
     *
     * @param index
     * @param type
     * @param jsonSource
     * @param id
     * @return
     */
    public boolean insertOneById(String index, String type, String jsonSource, String id) {
        IndexResponse response =
                client.prepareIndex(index, type).setSource(jsonSource).setId(id).execute().actionGet();
        if (StringUtils.isNotBlank(response.getId())) {
            return true;
        }
        return false;
    }

    /**
     * 更新文档
     *
     * @param index
     * @param type
     * @param jsonSource
     * @param id
     */
    public void updateOneById(String index, String type, String jsonSource, String id) {
        UpdateRequestBuilder indexReuqest =
                client.prepareUpdate(index, type, id).setDoc(jsonSource);

        indexReuqest.execute().actionGet();
    }

    /**
     * 删除文档
     *
     * @param index
     * @param type
     * @param id
     */
    public void deleteById(String index, String type, String id) {
        DeleteRequestBuilder deleteRequestBuilder = client.prepareDelete(index, type, id);
        DeleteResponse response = deleteRequestBuilder.execute().actionGet();

    }

    /**
     * 根据id获取数据
     * @param index
     * @param type
     * @param id
     * @return
     */
    public String getById(String index, String type, String id) {
        GetResponse reponse = client.prepareGet(
                index, type, id).execute().actionGet();
        return reponse.getSourceAsString();
    }

    /**
     * ES查询模块，termsAggration聚合之后接sumAggration 适用：表格统计：查找termsField对应的值（sumField）
     *
     * @param index
     * @param docType
     * @param query ES查询条件
     * @param termsField termsAggration聚合字段
     * @param sumField sumAggration求和字段
     * @param size size决定terms的个数
     * @param order
     */
    protected Map<String, Long> termsAggrSumAggr(final String index, final String docType,
                                                 final QueryBuilder query, final String termsField, final String sumField,
                                                 final int size, final Terms.Order order) {
        final AbstractAggregationBuilder aggr =
                AggregationBuilders.terms(termsField).field(termsField).order(order).size(size)
                        .subAggregation(AggregationBuilders.sum(sumField).field(sumField));

        SearchRequestBuilder request =client.
                prepareSearch(index).setTypes(docType).setSize(0).setQuery(constantScoreQuery(query))
                        .addAggregation(aggr);
        final SearchResponse response = request.execute().actionGet();
        final Map<String, Long> result = new HashMap<>();
        final Terms terms = response.getAggregations().get(termsField);
        for (final Terms.Bucket bucket : terms.getBuckets()) {
            final Sum sum = bucket.getAggregations().get(sumField);
            result.put(bucket.getKeyAsString(), Math.round(sum.getValue()));
        }
        return result;
    }

    public static void main(String[] args){

    }
}
