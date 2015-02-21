package com.github.tomjankes.wiremock
import com.github.tomjankes.wiremock.features.JsonBodyFeature
import com.github.tomjankes.wiremock.http.WireMockHttpApi
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
/**
 * Main WireMockGroovy entry point.
 *
 * Allows to interact with WireMock server in unit test in more concise groovy-ish way.
 */
class WireMockGroovy {
    String address
    WireMockHttpApi api = new WireMockHttpApi()
    List<StubbingFeature> stubbingFeatures = new LinkedList<>()

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
        def map = applyStubbingFeatures(closure)
        api.postStub(address + "/mappings/new", new JsonBuilder(map).toPrettyString())
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


    private def applyStubbingFeatures(closure) {
        if (!stubbingFeatures.empty) {
            def map = new JsonSlurper().parse(new StringReader(new JsonBuilder(closure).toString()))
            def result = map instanceof Cloneable ? map.clone() : map
            stubbingFeatures.each { result = it.apply(result) }
            return result
        } else {
            return closure
        }
    }

    /**
     * Add a feature that will transform stubbing json sent to WireMock
     *
     * NOTE that sequence of features may matter since they operate on json map
     * transformed by previous features. Features are executed in the order they
     * are added.
     *
     * @see StubbingFeature for more explanation
     * @param feature
     * @return WireMockGroovy for fluent interface
     */
    def addStubbingFeature(StubbingFeature feature) {
        stubbingFeatures.add(feature)
        this
    }

    static def builder() {
        new Builder()
    }

    public static class Builder {
        Integer port
        String host
        List<StubbingFeature> stubbingFeatures = new LinkedList<>()

        Builder() {
        }

        Builder withPort(Integer port) {
            this.port = port
            this
        }

        Builder withHost(String host) {
            this.host = host
            this
        }

        Builder enableJsonBodyFeature() {
            stubbingFeatures.add(new JsonBodyFeature())
            this
        }

        Builder withFeature(StubbingFeature feature) {
            stubbingFeatures.add(feature)
            this
        }

        WireMockGroovy build() {
            WireMockGroovy wireMock
            if (host && port) {
                wireMock = new WireMockGroovy(host, (long)port)
            } else if (host) {
                wireMock = new WireMockGroovy(port)
            } else {
                wireMock = new WireMockGroovy()
            }
            stubbingFeatures.each { wireMock.addStubbingFeature(it) }
            wireMock
        }
    }
}
