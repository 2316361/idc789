package com.idc789;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class Xufei {

    private static Log log = LogFactory.getLog(Xufei.class);

    @Resource
    private ClientGetter clientGetter;

    @Scheduled(cron = "0 0 0/6 * * *")
    public void operate() {
        WebClient webClient = null;
        try {
            while (true) {
                webClient = clientGetter.getWebClient();
                if (webClient == null) {
                    log.info("30秒后重试");
                    Thread.sleep(30000);
                } else {
                    break;
                }
            }
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            long renewalTime = calendar.getTimeInMillis();
            HtmlPage htmlPage = webClient.getPage("http://www.idc789.com/idc/top_login.asp");
            input(htmlPage, "username", "2316361");
            input(htmlPage,"password", "3211348");
            DomElement domElement = htmlPage.getElementById("login");
            htmlPage = domElement.click();
            if (htmlPage.getElementsByTagName("script").get(0).asXml().contains("top.location='/default.asp'")) {
                log.info("登录成功");
                htmlPage = webClient.getPage("http://www.idc789.com/user/vpsadm.asp");
                DomElement hostTable = null;
                for (DomElement temp : htmlPage.getElementsByTagName("table")) {
                    if (temp.getAttribute("bgcolor").equals("#CCCCCC")) {
                        hostTable = temp;
                        break;
                    }
                }
                List<HtmlElement> trList = hostTable.getElementsByTagName("tr");
                int length = trList.size();
                if (length == 1) {
                    log.info("该用户未开通虚拟主机");
                } else {
                    log.info("找到" + (length - 1) + "个虚拟主机");
                    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                    for (int i = 1; i < trList.size(); ++i) {
                        HtmlElement tr = trList.get(i);
                        List<HtmlElement> tdList = tr.getElementsByTagName("td");
                        String name = tdList.get(2).asText() + tdList.get(3).asText();
                        log.info("开始为" + name + "续费");
                        String endDateString = tdList.get(6).asText().replaceAll("\\s", "");
                        log.info("到期日期：" + endDateString);
                        Date comeDueTime = format.parse(endDateString);
                        if (comeDueTime.getTime() > renewalTime) {
                            log.info(name + "无需续费");
                        } else {
                            String vpsId = tdList.get(0).getElementsByTagName("input").get(0).getAttribute("value");
                            webClient.getPage("http://www.idc789.com/vpsadm/selfvpsmodifyrepay_first.asp?id=" + vpsId);
                            Thread.sleep(5000);
                            htmlPage = webClient.getPage("http://www.idc789.com/vpsadm/selfvpsmodifyrepay.asp?id=" + vpsId);
                            List<DomElement> elementList = htmlPage.getElementsByName("year");
                            for (DomElement element : elementList) {
                                if (element.getAttribute("value").equals("9001")) {
                                    element.click();
                                    break;
                                }
                            }
                            HtmlElement htmlElement = htmlPage.getElementByName("Submit");
                            htmlPage = htmlElement.click();
                            List<DomElement> domElementList = htmlPage.getElementsByTagName("b");
                            if (domElementList.size() > 0) {
                                log.info(name + ":" + domElementList.get(0).asText());
                            } else {
                                log.info(name + ":续费成功");
                            }
                        }
                    }
                }
            } else {
                log.info("登录失败，账号或密码错误");
            }
        } catch (Exception e) {
            log.error(e);
        } finally {
            if (webClient != null) {
                webClient.close();
            }
        }
    }

    private void input(HtmlPage htmlPage, String name, String value) throws IOException {
        HtmlElement htmlElement = htmlPage.getElementByName(name);
        htmlElement.focus();
        htmlElement.type(value);
    }
}
