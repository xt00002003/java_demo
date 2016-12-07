package com.dark.spider.dao;

import com.dark.spider.entity.Article;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Map;

/**
 * Created by darkxue on 23/11/16.
 */
public class ArticleDaoImpl implements Pipeline {

    @Override
    public void process(ResultItems resultItems, Task task) {
        System.out.println("get page: " + resultItems.getRequest().getUrl());
        //遍历所有结果，输出到控制台，上面例子中的"author"、"name"、"readme"都是一个key，其结果则是对应的value
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            System.out.println(entry.getKey() + ":\t" + entry.getValue());

        }
        Article article=new Article();
        Map<String,Object> result=resultItems.getAll();
        if (result.size()>0){
            if(null!=result.get("title")){
                article.setTitle(result.get("title").toString());
            }
            if (null!=result.get("content")){
                article.setContent(result.get("content").toString());
            }
            if (null!=result.get("date")){
                article.setDate(result.get("date").toString());
            }

            System.out.println("the Article is :"+article);
        }


    }
}
