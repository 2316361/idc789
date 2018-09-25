package com.idc789;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ClientGetter {
    private static Log log = LogFactory.getLog(ClientGetter.class);

    WebClient getWebClient() throws Exception {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        boolean flag = false;
        log.info("开始获取代理");
        HtmlPage htmlPage = webClient.getPage("http://www.xicidaili.com/nn");
        DomElement table = htmlPage.getElementById("ip_list");
        List<HtmlElement> trList = table.getElementsByTagName("tr");
        List<Proxy> proxyList = new ArrayList<>();
        for (int i = 1; i < trList.size(); ++i) {
            HtmlElement tr = trList.get(i);
            List<HtmlElement> tdList = tr.getElementsByTagName("td");
            Proxy proxy = new Proxy();
            proxy.setIp(tdList.get(1).asText());
            proxy.setPort(Integer.parseInt(tdList.get(2).asText()));
            proxyList.add(proxy);
        }
        log.info("找到" + proxyList.size() + "个代理");
        Collections.shuffle(proxyList);
        for (Proxy temp : proxyList) {
            String ip = temp.getIp();
            int port = temp.getPort();
            log.info("验证代理" + ip + ":" + port);
            ProxyConfig proxyConfig = new ProxyConfig(ip, port);
            webClient.getOptions().setProxyConfig(proxyConfig);
            try {
                htmlPage = webClient.getPage("http://www.idc789.com/idc/top_login.asp");
                if (htmlPage.getElementByName("username") == null || htmlPage.getElementByName("password") == null) {
                    log.info("连接失败，代理" + ip + ":" + port + "不可用");
                    continue;
                }
            } catch (Exception e) {
                log.info("连接失败，代理" + ip + ":" + port + "不可用");
                continue;
            }
            log.info("连接成功，代理" + ip + ":" + port + "可用");
            flag = true;
            break;
        }
        if (!flag) {
            log.info("没有找到可用的代理");
            return null;
        }
        return webClient;
    }
}
