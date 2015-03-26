package com.github.tomjankes.wiremock.features
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomjankes.wiremock.WireMockGroovy
import org.junit.Rule
import spock.lang.Specification
import spock.lang.Subject

import static com.github.tomjankes.wiremock.testutils.HttpHelper.getJson

class JsonBodyFeatureWithVariablesInsideSpec extends Specification {
    @Rule
    WireMockRule wireMockRule = new WireMockRule(8080)
    @Subject
    def wireMock = WireMockGroovy.builder().withFeature(new JsonBodyFeature()).withPort(8080).build()


    def "should handle variables in jsonBody"() {
        when:
        def theValue = "some value"
        wireMock.stub {
            request {
                method "GET"
                url "/some/thing"
            }
            response {
                status 200
                jsonBody {
                    theKey theValue
                }
            }
        }

        and:
        def json = getJson("http://localhost:8080/some/thing")

        then:
        json.theKey == "some value"
    }
}
