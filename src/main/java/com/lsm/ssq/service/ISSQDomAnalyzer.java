package com.lsm.ssq.service;

import org.jsoup.nodes.Document;

import java.util.Map;

public interface ISSQDomAnalyzer {

    Map<String, Object> forListMap(Document document);
}
