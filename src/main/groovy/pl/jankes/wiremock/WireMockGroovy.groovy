package pl.jankes.wiremock

import groovy.json.JsonBuilder
import pl.jankes.wiremock.http.WireMockHttpApi

class WireMockGroovy {
    String address
    WireMockHttpApi api = new WireMockHttpApi()

    WireMockGroovy(String host, Long port) {
        address = "http://$host:$port/__admin"
    }

    WireMockGroovy() {
        this("localhost", 8080)
    }

    WireMockGroovy(Long port) {
        this("localhost", port)
    }

    public void stub(Closure<JsonBuilder> closure) {
        api.postStub(address + "/mappings/new", new JsonBuilder(closure).toPrettyString())
    }

    public long count(Closure<JsonBuilder> closure) {
        def json = new JsonBuilder(closure)
        def requestsCountResponse = api.getRequestsCount(address + "/requests/count", json.toPrettyString())
        requestsCountResponse.count
    }
}
