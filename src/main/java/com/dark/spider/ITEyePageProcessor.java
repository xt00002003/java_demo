package com.dark.spider;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by tengxue on 16-4-22.
 * 爬虫程序的第一个测试用例
 */
public class ITEyePageProcessor implements PageProcessor {
    private Site site = Site.me().setUserAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.111 Safari/537.36")
            .setRetryTimes(3).setSleepTime(3000);

    @Override
    public void process(Page page) {

        page.addTargetRequests(page.getHtml().xpath("/html/body/div[1]/div[3]/div/div[1]/div[1]/*").links().regex("/yandsearch?cl4url=.*").all());
        page.putField("title", page.getHtml().xpath("/html/body/div[5]/div[1]/div[1]/article/div[2]/div[2]/header/h1/text()").toString());
        page.putField("content", page.getHtml().xpath("/html/body/div[5]/div[1]/div[1]/article/div[2]/div[2]/div/div[1]/text()").toString());

    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new ITEyePageProcessor()).addUrl("https://news.yandex.ru/politics.html")
                .addPipeline(new ConsolePipeline()).thread(5).run();
    }
}
