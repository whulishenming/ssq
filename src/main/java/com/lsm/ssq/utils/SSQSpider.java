package com.lsm.ssq.utils;

import com.lsm.ssq.service.ISSQDomAnalyzer;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;

import java.util.Map;

@Slf4j
public class SSQSpider {

    public static  <T> T forEntityList(String url, Class<T> type) throws Exception {

        ISSQDomAnalyzer ssqDomAnalyzerImpl = SpringContextUtil.getBean(ISSQDomAnalyzer.class);

        log.info("开始抓取文章："+url);

        Map<String, Object> map = ssqDomAnalyzerImpl.forListMap(Jsoup.connect(url).timeout(50000).get());

        return MapObjectUtil.mapToObject(map, type);
    }
}
