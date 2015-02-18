package com.github.tomjankes.wiremock

import groovy.json.JsonBuilder
import com.github.tomjankes.wiremock.http.WireMockHttpApi

/**
 * Main WireMockGroovy entry point.
 *
 * Allows to interact with WireMock server in unit test in more concise groovy-ish way.
 */
class WireMockGroovy {
    String address
    WireMockHttpApi api = new WireMockHttpApi()

    /**
     * Constructor allows creating instance with custom WireMock host and port
     *
     * @param host hostname where WireMock server can be found
     * @param port WireMock server listening port
     */
    WireMockGroovy(String host, Long port) {
        address = "http://$host:$port/__admin"
    }

    /**
     * Create instance with default WireMock server location: http://localhost:8080
     */
    WireMockGroovy() {
        this("localhost", 8080)
    }

    /**
     * Create instance that will connect to WireMock server on given port on localhost
     * @param port WireMock server listening port
     */
    WireMockGroovy(Long port) {
        this("localhost", port)
    }

    /**
     * Creates stub of response for given request on WireMock server.
     *
     * Basic usage
     * <pre>
     * <code>
     * stub {
     *     request {
     *         method "GET"
     *         url "/some/thing"
     *    }
     *    response {
     *        status 200
     *        body "Some body"
     *        headers {
     *           "Content-Type" "text/plain"
     *        }
     *    }
     * }
     * </code>
     * </pre>
     * where request part means the request that WireMock will receive and response
     * part is the response with which WireMock will respond.
     * In this case wire mock will respond with status 200 and response body "Some body"
     * on all requests of type "GET" with path "/some/thing"
     *
     * @param closure JsonBuilder closure that will create request for WireMock
     */
    public void stub(Closure<JsonBuilder> closure) {
        api.postStub(address + "/mappings/new", new JsonBuilder(closure).toPrettyString())
    }

    /**
     * Fetches count of matching requests made to the WireMock server
     *
     * Basic usage
     * <pre>
     * <code>
     * count {
     *    method "GET"
     *    url "/theaddress"
     * }
     * </code>
     * </pre>
     * will return count of all get requests made in this session to for path "/theaddress"
     *
     * @param closure JsonBuilder closure that will create request for WireMock
     * @return count of matching requests
     */
    public long count(Closure<JsonBuilder> closure) {
        def json = new JsonBuilder(closure)
        def requestsCountResponse = api.getRequestsCount(address + "/requests/count", json.toPrettyString())
        requestsCountResponse.count
    }
}
