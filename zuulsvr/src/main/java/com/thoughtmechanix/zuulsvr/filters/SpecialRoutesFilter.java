package com.thoughtmechanix.zuulsvr.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.thoughtmechanix.zuulsvr.model.AbTestingRoute;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

@Component
public class SpecialRoutesFilter extends ZuulFilter {
    private static final int FILTER_ORDER =  1;
    private static final boolean SHOULD_FILTER =true;
    private static final Logger logger = LoggerFactory.getLogger(SpecialRoutesFilter.class);

    @Autowired
    private FilterUtils filterUtils;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String filterType() {
        return FilterUtils.ROUTE_FILTER_TYPE;
    }

    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return SHOULD_FILTER;
    }

    private ProxyRequestHelper helper = new ProxyRequestHelper();

    private AbTestingRoute getAbRoutingInfo(String serviceName) {
        ResponseEntity<AbTestingRoute> restExchange = null;
        try {
            restExchange = restTemplate.exchange(
                    "http://specialroutesservice/v1/route/abtesting/{serviceName}",
                    HttpMethod.GET,
                    null, AbTestingRoute.class, serviceName);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                return null;
            }
            throw ex;
        }

        return restExchange.getBody();
    }

    private String buildRouteString(String oldEndpoint, String newEndpoint, String serviceName) {
        int index = oldEndpoint.indexOf(serviceName);

        String strippedRoute = oldEndpoint.substring(index + serviceName.length());
        logger.error("======> Target route: {}", String.format("%s/%s", newEndpoint, strippedRoute));
//        System.out.println("Target route: " + String.format("%s/%s", newEndpoint, strippedRoute));

        return String.format("%s/%s", newEndpoint, strippedRoute);
    }

    private String getVerb(HttpServletRequest request) {
        String sMethod = request.getMethod();
        return sMethod.toUpperCase();
    }

    private HttpHost getHttpHost(URL host) {
        HttpHost httpHost = new HttpHost(host.getHost(), host.getPort(), host.getProtocol());

        return httpHost;
    }

    private Header[] convertHeaders(MultiValueMap<String, String> headers) {
        List<Header> list = new ArrayList<>();
        for (String name : headers.keySet()) {
            for (String value : headers.get(name)) {
                list.add(new BasicHeader(name, value));
            }
        }
        return list.toArray(new Header[0]);
    }

    private HttpResponse forwardRequest(HttpClient httpClient, HttpHost httpHost, HttpRequest httpRequest) throws IOException {
        HttpResponse httpResponse = httpClient.execute(httpHost, httpRequest);

        logger.error("======> HttpClient response status {}", httpResponse.getStatusLine());
        Scanner sc = new Scanner(httpResponse.getEntity().getContent());
        while (sc.hasNext()) {
            logger.error("======> HttpClient response content {}", sc.nextLine());
        }

        return httpResponse;
    }

    private MultiValueMap<String, String> reverHeaders(Header[] headers) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        for (Header header : headers) {
            String name = header.getName();
            if (!map.containsKey(name)) {
                map.put(name, new ArrayList<String>());
            }
            map.get(name).add(header.getValue());
        }
        return map;
    }

    private InputStream getRequestBody(HttpServletRequest request) {
        InputStream requestEntity = null;
        try {
            requestEntity = request.getInputStream();
        } catch (IOException ex) {
            // no requestBody is ok.
        }
        return requestEntity;
    }

    private void setResponse(HttpResponse response) throws IOException {
        this.helper.setResponse(response.getStatusLine().getStatusCode(),
                response.getEntity() == null ? null : response.getEntity().getContent(),
                reverHeaders(response.getAllHeaders()));

        Scanner sc = new Scanner(response.getEntity().getContent());
        while (sc.hasNext()) {
            logger.error("======> setResponse response content {}", sc.nextLine());
        }

        /*RequestContext ctx = RequestContext.getCurrentContext();
        InputStream entity = response.getEntity().getContent();
        if (entity != null) {
            ctx.setResponseDataStream(entity);
        }

        logger.error("======> setResponse ===> context response body {}", ctx.getResponseBody());
        logger.error("======> setResponse ===> context response data stream {}", ctx.getResponseDataStream());*/
    }

    private HttpResponse forward(HttpClient httpClient, String verb, String uri, HttpServletRequest request, MultiValueMap<String, String> headers,
                                 MultiValueMap<String, String> params, InputStream requestEntity) throws Exception {
        Map<String, Object> info = this.helper.debug(verb, uri, headers, params, requestEntity);
        URL host = new URL(uri);
        HttpHost httpHost = getHttpHost(host);

        HttpRequest httpRequest;
        int contentLength = request.getContentLength();
        InputStreamEntity entity = new InputStreamEntity(requestEntity, contentLength, request.getContentType() != null
                ? ContentType.create(request.getContentType()) : null);

        switch (verb.toUpperCase()) {
            case "POST":
                HttpPost httpPost = new HttpPost(uri);
                httpRequest = httpPost;
                httpPost.setEntity(entity);
                break;
            case "PUT":
                HttpPut httpPut = new HttpPut(uri);
                httpRequest = httpPut;
                httpPut.setEntity(entity);
                break;
            case "PATCH":
                HttpPatch httpPatch = new HttpPatch(uri );
                httpRequest = httpPatch;
                httpPatch.setEntity(entity);
                break;
            default:
                httpRequest = new BasicHttpRequest(verb, uri);
        }

        httpRequest.setHeaders(convertHeaders(headers));
        return forwardRequest(httpClient, httpHost, httpRequest);
    }

    private boolean useSpecialRoute(AbTestingRoute testingRoute) {
        Random random = new Random();

        if (testingRoute.getActive().equals("N")) {
            return false;
        }

        int value = random.nextInt((10 - 1) + 1) + 1;

        return testingRoute.getWeight() < value;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();

        AbTestingRoute abTestingRoute = getAbRoutingInfo(filterUtils.getServiceId());

        if (abTestingRoute != null && useSpecialRoute(abTestingRoute)) {
            logger.error("======> Here, hahahahhhhh");

            String route = buildRouteString(ctx.getRequest().getRequestURI(),
                    abTestingRoute.getEndpoint(),
                    ctx.get("serviceId").toString());
            forwardToSpecialRoute(route);
        }
        return null;
    }

    private void forwardToSpecialRoute(String route) {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        MultiValueMap<String, String> headers = this.helper.buildZuulRequestHeaders(request);
        MultiValueMap<String, String> params = this.helper.buildZuulRequestQueryParams(request);

        String verb = getVerb(request);
        InputStream requestEntity = getRequestBody(request);
        if (request.getContentLength() < 0) {
            ctx.setChunkedRequestBody();
        }

        this.helper.addIgnoredHeaders();
        CloseableHttpClient httpClient = null;
        HttpResponse response = null;

        try {
            httpClient = HttpClients.createDefault();
            response = forward(httpClient, verb, route, request, headers, params, requestEntity);
            setResponse(response);

            logger.error("======> forwardToSpecialRoute context response body ===> {}", ctx.getResponseBody());
            logger.error("======> forwardToSpecialRoute context response data stream ===> {}", ctx.getResponseDataStream());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException ex) {

            }
        }
    }
}
