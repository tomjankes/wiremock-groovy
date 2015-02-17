package pl.jankes.wiremock
import groovy.json.JsonBuilder
import org.apache.http.HttpResponse
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.ResponseHandler
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients

class WireMockStub {
    JsonBuilder json
    String address

    WireMockStub(String host, Long port) {
        address = "http://$host:$port/__admin/mappings/new"
    }

    WireMockStub() {
        this("localhost", 8080)
    }

    WireMockStub(Long port) {
        this("localhost", port)
    }

    public void stub(Closure<JsonBuilder> closure) {
        json = new JsonBuilder(closure)
        sendStub(json)
    }
    //temporarily using HttpClient as Http Builder has dependency clashes
    //with wire mock
    private void sendStub(JsonBuilder json) {
        def httpClient = HttpClients.createDefault()
        def post = new HttpPost(address)
        post.setEntity(new StringEntity(json.toPrettyString()))
        httpClient.execute(post, new ResponseHandler<Void>() {
            @Override
            Void handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                def code = response.statusLine.statusCode
                if (code != 201) {
                    throw new RuntimeException(
                        "Wiremock error while stubbing: [$code] $response.statusLine.reasonPhrase")
                }
                null
            }
        })
    }
}
