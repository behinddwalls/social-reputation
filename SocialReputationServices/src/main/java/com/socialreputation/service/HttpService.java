package com.socialreputation.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Service
public class HttpService {

	private final static CloseableHttpClient httpclient = HttpClients.createDefault();

	static {

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					httpclient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private HttpClient getClient() {
		return httpclient;
	}

	@Builder
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class HttpRequest {

		/**
		 * url with query params expected
		 */
		private String httpUrl;
		private HttpParams queryParams;
		private HttpParams headerParams;
		private HttpEntity entity;

		public static class HttpParams {
			@Getter
			private Map<String, String> params = new HashMap<String, String>();

			public HttpParams(Map<String, String> paramMap) {
				this.params = paramMap;
			}

			public static HttpParamsBuilder builder() {
				return new HttpParamsBuilder();
			}

			public static class HttpParamsBuilder {
				protected Map<String, String> paramMap = new HashMap<String, String>();

				public HttpParamsBuilder with(final String name, final String value) {
					paramMap.put(name, value);
					return this;
				}

				public HttpParams build() {
					return new HttpParams(paramMap);
				}
			}
		}

	}

	public HttpResponse executeGet(final HttpRequest request) throws ClientProtocolException, IOException {
		// add params
		URIBuilder builder = new URIBuilder();
		Optional.ofNullable(request.getQueryParams()).ifPresent(qps -> {
			qps.getParams().forEach((k, v) -> {
				builder.setParameter(k, v);
			});
		});

		System.out.println(builder.toString());
		final HttpGet get = new HttpGet(request.getHttpUrl() + builder.toString());

		Optional.ofNullable(request.getQueryParams()).ifPresent(qps -> {
			qps.getParams().forEach((k, v) -> {
				get.addHeader(k, v);
			});
		});
		return getClient().execute(get);
	}
}
