package com.lsm.ssq.service.impl;

import com.lsm.ssq.service.ISSQDomAnalyzer;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SSQDomAnalyzerImpl implements ISSQDomAnalyzer{

    @Override
    public Map<String, Object> forListMap(Document document) {
        Map<String, Object> result = new HashMap<>();
        if (ObjectUtils.isEmpty(document))
            return null;

        List<String> ballparams = new ArrayList<>();
        ballparams.add("firstRedBall");
        ballparams.add("secondRedBall");
        ballparams.add("thirdRedBall");
        ballparams.add("fourthRedBall");
        ballparams.add("fifthRedBall");
        ballparams.add("sixthRedBall");
        ballparams.add("firstBlueBall");
        Elements elements = document.body().getElementsByClass("ball_box01").get(0).child(0).children();
        for (int i = 0; i < elements.size(); i++) {
            result.put(ballparams.get(i), Integer.parseInt(elements.get(i).getElementsByTag("li").text()));
        }

        Element element = document.body().getElementsByClass("cfont2").get(0).child(0);
        result.put("periods",Integer.parseInt(element.text()));

        Element yuceLink = document.select("a[href$=yuce.shtml]").get(0);

        int indexOf = yuceLink.text().indexOf("双色球");


        Element element1 = document.body().getElementsByClass("span_right").get(0);
        String datestr = element1.text().substring(5,16);
        System.out.println(element1.text().substring(5,16));
        String year = datestr.substring(0,datestr.indexOf("年"));

        String month = datestr.substring(datestr.indexOf("年")+1,datestr.indexOf("月"));
        if(month.length()==1){
            month =  "0"+month;
        }
        String day = datestr.substring(datestr.indexOf("月")+1,datestr.indexOf("日"));

        datestr = year+"-"+month+"-"+day;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = simpleDateFormat.parse(datestr);
            result.put("lotteryDate",date);
        }catch (ParseException e){
            e.printStackTrace();
        }

        return result;
    }
}
