package com.github.tomjankes.http

import org.apache.http.client.ResponseHandler
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
/**
 * temporarily using HttpClient as Http Builder has dependency clashes
 * with wire mock
 */
class WireMockHttpApi {

    public Object getRequestsCount(String address, String requestBody) {
        makePostRequest(address, requestBody, new RequestCountHandler())
    }

    public void postStub(String address, String body) {
        makePostRequest(address, body, new NewMappingHandler())
    }

    private def makePostRequest(String address, String body, ResponseHandler responseHandler) {
        def post = new HttpPost(address)
        post.setEntity(new StringEntity(body))
        HttpClients.createDefault().execute(post, responseHandler)
    }

}
