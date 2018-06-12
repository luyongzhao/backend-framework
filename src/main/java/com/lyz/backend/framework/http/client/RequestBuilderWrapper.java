package com.lyz.backend.framework.http.client;

import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.HeaderGroup;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RequestBuilderWrapper {

    private String method;
    private ProtocolVersion version;
    private URI uri;
    private HeaderGroup headergroup;
    private HttpEntity entity;
    private LinkedList<NameValuePair> parameters;
    private RequestConfig config;

    RequestBuilderWrapper(final String method) {
        super();
        this.method = method;
    }

    RequestBuilderWrapper() {
        this(null);
    }

    public static RequestBuilderWrapper create(final String method) {
        Args.notBlank(method, "HTTP method");
        return new RequestBuilderWrapper(method);
    }

    public static RequestBuilderWrapper get() {
        return new RequestBuilderWrapper (HttpGet.METHOD_NAME);
    }

    public static RequestBuilderWrapper head() {
        return new RequestBuilderWrapper (HttpHead.METHOD_NAME);
    }

    public static RequestBuilderWrapper post() {
        return new RequestBuilderWrapper (HttpPost.METHOD_NAME);
    }

    public static RequestBuilderWrapper put() {
        return new RequestBuilderWrapper (HttpPut.METHOD_NAME);
    }

    public static RequestBuilderWrapper delete() {
        return new RequestBuilderWrapper (HttpDelete.METHOD_NAME);
    }

    public static RequestBuilderWrapper trace() {
        return new RequestBuilderWrapper (HttpTrace.METHOD_NAME);
    }

    public static RequestBuilderWrapper options() {
        return new RequestBuilderWrapper (HttpOptions.METHOD_NAME);
    }

    public static RequestBuilderWrapper copy(final HttpRequest request) {
        Args.notNull(request, "HTTP request");
        return new RequestBuilderWrapper ().doCopy(request);
    }

    private RequestBuilderWrapper doCopy(final HttpRequest request) {
        if (request == null) {
            return this;
        }
        method = request.getRequestLine().getMethod();
        version = request.getRequestLine().getProtocolVersion();
        if (request instanceof HttpUriRequest) {
            uri = ((HttpUriRequest) request).getURI();
        } else {
            uri = URI.create(request.getRequestLine().getUri());
        }
        if (headergroup == null) {
            headergroup = new HeaderGroup();
        }
        headergroup.clear();
        headergroup.setHeaders(request.getAllHeaders());
        if (request instanceof HttpEntityEnclosingRequest) {
            entity = ((HttpEntityEnclosingRequest) request).getEntity();
        } else {
            entity = null;
        }
        if (request instanceof Configurable) {
            this.config = ((Configurable) request).getConfig();
        } else {
            this.config = null;
        }
        this.parameters = null;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public ProtocolVersion getVersion() {
        return version;
    }

    public RequestBuilderWrapper setVersion(final ProtocolVersion version) {
        this.version = version;
        return this;
    }

    public URI getUri() {
        return uri;
    }

    public RequestBuilderWrapper setUri(final URI uri) {
        this.uri = uri;
        return this;
    }

    public RequestBuilderWrapper setUri(final String uri) {
        this.uri = uri != null ? URI.create(uri) : null;
        return this;
    }

    public Header getFirstHeader(final String name) {
        return headergroup != null ? headergroup.getFirstHeader(name) : null;
    }

    public Header getLastHeader(final String name) {
        return headergroup != null ? headergroup.getLastHeader(name) : null;
    }

    public Header[] getHeaders(final String name) {
        return headergroup != null ? headergroup.getHeaders(name) : null;
    }

    public RequestBuilderWrapper addHeader(final Header header) {
        if (headergroup == null) {
            headergroup = new HeaderGroup();
        }
        headergroup.addHeader(header);
        return this;
    }

    public RequestBuilderWrapper addHeader(final String name, final String value) {
        if (headergroup == null) {
            headergroup = new HeaderGroup();
        }
        this.headergroup.addHeader(new BasicHeader(name, value));
        return this;
    }

    public RequestBuilderWrapper removeHeader(final Header header) {
        if (headergroup == null) {
            headergroup = new HeaderGroup();
        }
        headergroup.removeHeader(header);
        return this;
    }

    public RequestBuilderWrapper removeHeaders(final String name) {
        if (name == null || headergroup == null) {
            return this;
        }
        for (final HeaderIterator i = headergroup.iterator(); i.hasNext(); ) {
            final Header header = i.nextHeader();
            if (name.equalsIgnoreCase(header.getName())) {
                i.remove();
            }
        }
        return this;
    }

    public RequestBuilderWrapper setHeader(final Header header) {
        if (headergroup == null) {
            headergroup = new HeaderGroup();
        }
        this.headergroup.updateHeader(header);
        return this;
    }

    public RequestBuilderWrapper setHeader(final String name, final String value) {
        if (headergroup == null) {
            headergroup = new HeaderGroup();
        }
        this.headergroup.updateHeader(new BasicHeader(name, value));
        return this;
    }

    public HttpEntity getEntity() {
        return entity;
    }

    public RequestBuilderWrapper setEntity(final HttpEntity entity) {
        this.entity = entity;
        return this;
    }

    public List<NameValuePair> getParameters() {
        return parameters != null ? new ArrayList<NameValuePair>(parameters) :
                new ArrayList<NameValuePair>();
    }

    public RequestBuilderWrapper addParameter(final NameValuePair nvp) {
        Args.notNull(nvp, "Name value pair");
        if (parameters == null) {
            parameters = new LinkedList<NameValuePair>();
        }
        parameters.add(nvp);
        return this;
    }

    public RequestBuilderWrapper addParameter(final String name, final String value) {
        return addParameter(new BasicNameValuePair(name, value));
    }

    public RequestBuilderWrapper addParameters(final NameValuePair... nvps) {
        for (final NameValuePair nvp: nvps) {
            addParameter(nvp);
        }
        return this;
    }

    public RequestConfig getConfig() {
        return config;
    }

    public RequestBuilderWrapper setConfig(final RequestConfig config) {
        this.config = config;
        return this;
    }

    public HttpUriRequest build() {
        final HttpRequestBase result;
        URI uri = this.uri != null ? this.uri : URI.create("/");
        HttpEntity entity = this.entity;
        if (parameters != null && !parameters.isEmpty()) {
            if (entity == null && (HttpPost.METHOD_NAME.equalsIgnoreCase(method)
                    || HttpPut.METHOD_NAME.equalsIgnoreCase(method))) {
                try {
                    entity = new UrlEncodedFormEntity(parameters, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    uri = new URIBuilder(uri).addParameters(parameters).build();
                } catch (final URISyntaxException ex) {
                    // should never happen
                }
            }
        }
        if (entity == null) {
            result = new RequestBuilderWrapper.InternalRequest(method);
        } else {
            final RequestBuilderWrapper.InternalEntityEclosingRequest request = new RequestBuilderWrapper.InternalEntityEclosingRequest(method);
            request.setEntity(entity);
            result = request;
        }
        result.setProtocolVersion(this.version);
        result.setURI(uri);
        if (this.headergroup != null) {
            result.setHeaders(this.headergroup.getAllHeaders());
        }
        result.setConfig(this.config);
        return result;
    }

    static class InternalRequest extends HttpRequestBase {

        private final String method;

        InternalRequest(final String method) {
            super();
            this.method = method;
        }

        @Override
        public String getMethod() {
            return this.method;
        }

    }

    static class InternalEntityEclosingRequest extends HttpEntityEnclosingRequestBase {

        private final String method;

        InternalEntityEclosingRequest(final String method) {
            super();
            this.method = method;
        }

        @Override
        public String getMethod() {
            return this.method;
        }

    }
}
