package pl.jankes.wiremock.http

import org.apache.http.HttpResponse
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.ResponseHandler


class NewMappingHandler implements ResponseHandler<Void> {
    @Override
    Void handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
        def code = response.statusLine.statusCode
        if (code != 201) {
            throw new RuntimeException(
                    "Wiremock error while stubbing: [$code] $response.statusLine.reasonPhrase")
        }
        null
    }
}
