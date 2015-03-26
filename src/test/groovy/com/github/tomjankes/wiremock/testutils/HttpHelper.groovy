package com.github.tomjankes.wiremock.testutils

import com.google.common.base.Charsets
import groovy.json.JsonSlurper
import org.apache.http.HttpResponse
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.ResponseHandler
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils


class HttpHelper {
    static def getRequest(String url) {
        def get = new HttpGet(url)
        def response
        HttpClients.createDefault().execute(get, new ResponseHandler() {
            @Override
            Object handleResponse(HttpResponse res) throws ClientProtocolException, IOException {
                response = [
                        status: res.statusLine.statusCode,
                        contentType: res.getFirstHeader("Content-Type").value,
                        body: EntityUtils.toString(res.entity, Charsets.UTF_8)
                ]
            }
        })
        response
    }

    static def postRequest(String url, body, contentType) {
        def post = new HttpPost(url)
        post.setEntity(new StringEntity(body))
        post.addHeader("Content-Type", contentType)

        HttpClients.createDefault().execute(post)
    }

    static def getJson(String url) {
        new JsonSlurper().parse(new URL(url))
    }
}
