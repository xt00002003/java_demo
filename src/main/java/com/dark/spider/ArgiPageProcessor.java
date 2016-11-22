package com.dark.spider;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

/**
 * Created by tengxue on 16-4-22.
 * 爬虫程序的第一个测试用例
 */
public class ArgiPageProcessor implements PageProcessor {
    private Site site = Site.me().setUserAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.111 Safari/537.36")
            .setRetryTimes(3).setSleepTime(3000);

    @Override
    public void process(Page page) {
        //获取服务平台链接
        List<String> urls=page.getHtml().xpath("/html/body/table[3]/tbody/tr/td[1]/table[2]/tbody/tr/td[2]/table[2]/tbody/*").css("a.link03").links().all();
        System.out.println(urls);
        //获取服务平台名称
        List<String> names=page.getHtml().xpath("//html/body/table[3]/tbody/tr/td[1]/table[2]/tbody/tr/td[2]/table[2]/tbody/*").css("a.link03","text").all();
        System.out.println(names);
        //http://www.agri.cn/qxny/nqyw/201611/t20161122_5370988.htm
        urls=page.getHtml().xpath("//*[@id=\"TagContent8_1\"]/table/tbody/*").css("a.link03").links().regex("http://www.agri.cn/qxny/nqyw/.+").all();
        System.out.println(urls);
        page.addTargetRequests(urls);
//        names=page.getHtml().xpath("//*[@id=\"TagContent8_1\"]/table/tbody/*").css("a.link03","text").all();
//        System.out.println(names);

        if (page.getUrl().regex("http://www.agri.cn/qxny/nqyw/.+").match()){
            System.out.println("抓取页面的url--------------->"+page.getHtml().links().all());
            page.putField("title",page.getHtml().xpath("/html/body/table[3]/tbody/tr/td[1]/table[2]/tbody/tr/td/table[4]/tbody/tr[1]/td"));
//            page.putField("content",page.getHtml().xpath("//*[@id=\"TRS_AUTOADD\"]"));
            page.putField("date",page.getHtml().xpath("/html/body/table[3]/tbody/tr/td[1]/table[2]/tbody/tr/td/table[4]/tbody/tr[2]/td"));
        }

    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new ArgiPageProcessor()).addUrl("http://www.agri.cn/")
                .addPipeline(new ConsolePipeline()).thread(5).run();
    }
}
