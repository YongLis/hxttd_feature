package com.ly.ttd.connector.http;

import com.alibaba.fastjson.JSON;
import com.ly.ttd.connector.ConnectorException;
import com.ly.ttd.connector.api.AbstractConnector;
import com.ly.ttd.connector.api.ConnectorResponse;
import com.ly.ttd.connector.api.spi.ConnectorObserver;
import com.ly.ttd.consts.enums.ConnectorEnum;
import com.ly.ttd.consts.enums.ExecuteState;
import com.ly.ttd.feature.cfg.FeatureConfiguration;
import com.ly.ttd.feature.cfg.FeatureConfigurationAware;
import com.ly.ttd.feature.cfg.config.HttpConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.util.Map;

/**
 * @author yong.li
 * @since 2026/4/14 09:55
 */
@Slf4j
public class HttpConnector extends AbstractConnector<HttpConnectorRequest, ConnectorResponse> implements FeatureConfigurationAware {

    private FeatureConfiguration featureConfiguration;

    public HttpConnector(String connectorId) {
        super(connectorId);
    }

    private BasicHeader[] buildHeader(HttpConfig config) {
        Map<String, String> headers = config.getHeaders();

        if (MapUtils.isEmpty(headers)) {
            BasicHeader header = new BasicHeader("Content-Type", "application/json");
            BasicHeader[] tmp = new BasicHeader[1];
            tmp[0] = header;
            return tmp;
        } else {
            BasicHeader[] tmp = new BasicHeader[headers.size()];
            for (int i = 0; i < headers.size(); i++) {
                tmp[i] = new BasicHeader(headers.get(i), headers.get(i));
            }
            return tmp;
        }
    }

    @Override
    public String getConnectorType() {
        return ConnectorEnum.HTTP.getCode();
    }

    @Override
    public HttpConnectorRequest createRequest() {
        return new HttpConnectorRequest(this);
    }

    @Override
    public ConnectorResponse execute(HttpConnectorRequest req) throws ConnectorException {
        StopWatch stopWatch = StopWatch.createStarted();
        ConnectorResponse response = new ConnectorResponse();
        response.setReq(req);
        Object res = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            if ("get".equalsIgnoreCase(req.getMethod())) {
                HttpGet httpGet = new HttpGet(req.getUrl());
                if (req.getHeader() != null) {
                    for (String headerKey : req.getHeader().keySet()) {
                        Header header = new BasicHeader(headerKey, req.getHeader().get(headerKey));
                        httpGet.addHeader(header);
                    }
                }

                CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
                // 5. 获取响应内容
                String result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                int statusCode = httpResponse.getStatusLine().getStatusCode();

                // 可以在这里检查状态码，对非 200 情况进行处理
                if (statusCode != 200) {
                    throw new RuntimeException("请求失败，状态码: " + statusCode);
                }
                if (StringUtils.isNoneEmpty(result)) {
                    res = result;
                }

            } else if ("post".equalsIgnoreCase(req.getMethod())) {
                HttpPost post = new HttpPost(req.getUrl());
                if (req.getHeader() != null) {
                    for (String headerKey : req.getHeader().keySet()) {
                        Header header = new BasicHeader(headerKey, req.getHeader().get(headerKey));
                        post.addHeader(header);
                    }
                }
                post.setEntity(new StringEntity(req.getBody(), "UTF-8"));

                CloseableHttpResponse httpResponse = httpClient.execute(post);
                // 5. 获取响应内容
                String result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                int statusCode = httpResponse.getStatusLine().getStatusCode();

                // 可以在这里检查状态码，对非 200 情况进行处理
                if (statusCode != 200) {
                    throw new RuntimeException("请求失败，状态码: " + statusCode);
                }
                if (StringUtils.isNoneEmpty(result)) {
                    res = result;
                }
            }

            response.setRes(res);
            response.setState(ExecuteState.SUCCESS);
        } catch (Exception e) {
            log.error("jdbc connector execute error, txnId={}, req={}", req.getTxnId(),
                    JSON.toJSONString(req), e);
            response.setState(ExecuteState.FAIL);
            response.setErrorMsg(e.getMessage());
        }
        stopWatch.stop();
        response.setCost(stopWatch.getTime());
        return response;
    }

    @Override
    public ConnectorResponse execute(HttpConnectorRequest req, ConnectorObserver<HttpConnectorRequest, ConnectorResponse> observer) throws ConnectorException {
        StopWatch stopWatch = StopWatch.createStarted();
        ConnectorResponse response = new ConnectorResponse();
        response.setReq(req);
        Object res = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            if ("get".equalsIgnoreCase(req.getMethod())) {
                HttpGet httpGet = new HttpGet(req.getUrl());
                if (req.getHeader() != null) {
                    for (String headerKey : req.getHeader().keySet()) {
                        Header header = new BasicHeader(headerKey, req.getHeader().get(headerKey));
                        httpGet.addHeader(header);
                    }
                }

                CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
                // 5. 获取响应内容
                String result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                int statusCode = httpResponse.getStatusLine().getStatusCode();

                // 可以在这里检查状态码，对非 200 情况进行处理
                if (statusCode != 200) {
                    throw new RuntimeException("请求失败，状态码: " + statusCode);
                }
                if (StringUtils.isNoneEmpty(result)) {
                    res = result;
                }

            } else if ("post".equalsIgnoreCase(req.getMethod())) {
                HttpPost post = new HttpPost(req.getUrl());
                if (req.getHeader() != null) {
                    for (String headerKey : req.getHeader().keySet()) {
                        Header header = new BasicHeader(headerKey, req.getHeader().get(headerKey));
                        post.addHeader(header);
                    }
                }
                post.setEntity(new StringEntity(req.getBody(), "UTF-8"));

                CloseableHttpResponse httpResponse = httpClient.execute(post);
                // 5. 获取响应内容
                String result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                int statusCode = httpResponse.getStatusLine().getStatusCode();

                // 可以在这里检查状态码，对非 200 情况进行处理
                if (statusCode != 200) {
                    throw new RuntimeException("请求失败，状态码: " + statusCode);
                }
                if (StringUtils.isNoneEmpty(result)) {
                    res = result;
                }
            }

            response.setRes(res);
            response.setState(ExecuteState.SUCCESS);

            if (null != observer) {
                observer.onComplete(req, response);
            }

        } catch (Exception e) {
            log.error("jdbc connector execute error, txnId={}, req={}", req.getTxnId(),
                    JSON.toJSONString(req), e);
            response.setState(ExecuteState.FAIL);
            response.setErrorMsg(e.getMessage());
            if (null != observer) {
                observer.onException(req, response);
            }
        }
        stopWatch.stop();
        response.setCost(stopWatch.getTime());
        return response;
    }

    @Override
    public void setFeatureConfiguration(FeatureConfiguration featureConfiguration) {
        this.featureConfiguration = featureConfiguration;
    }
}
