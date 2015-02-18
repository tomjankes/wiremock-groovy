package com.github.tomjankes.wiremock.http

import groovy.json.JsonSlurper
import org.apache.http.HttpResponse
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.ResponseHandler


class RequestCountHandler implements ResponseHandler<Object> {
    @Override
    Object handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
        def code = response.statusLine.statusCode;
        if (code != 200) {
            throw new RuntimeException(
                    "Unexpected response from WireMock: [$code] $response.statusLine.reasonPhrase")
        }
        new JsonSlurper().parse(response.entity.content)
    }
}

