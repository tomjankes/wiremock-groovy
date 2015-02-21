package com.github.tomjankes.wiremock
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule
import spock.lang.Specification

import static com.github.tomjankes.wiremock.testutils.HttpHelper.getRequest
import static com.github.tomjankes.wiremock.testutils.HttpHelper.postRequest

/**
 * Test snippets for docs, to not to introduce errors in them (again).
 */
class ReadmeSpec extends Specification {
    @Rule
    WireMockRule wireMockRule = new WireMockRule()

    def wireMockStub = new WireMockGroovy()


    def "some integration test that tests feature using external REST resource" () {
        given:
        wireMockStub.stub {
            request {
                method "GET"
                url "/some/thing"
            }
            response {
                status 200
                body "Some body"
                headers {
                    "Content-Type" "text/plain"
                }
            }
        }

        when:
        def response = getRequest("http://localhost:8080/some/thing")

        then:
        response.status == 200
        response.contentType == "text/plain"
        response.body == "Some body"

    }

    def "example verifying test" () {
        when:
        getRequest("http://localhost:8080/some/url")

        then:
        1 == wireMockStub.count {
            method "GET"
            url "/some/url"
        }
    }

    def "test using groovy truth if you need at least one request and shows example matcher" () {
        when:
        postRequest("http://localhost:8080/some/url", "<root />", "some.xml")

        then:
        wireMockStub.count {
            method "POST"
            url "/some/url"
            headers {
                "Content-Type" {
                    matches ".*xml"
                }
            }
        }
    }
}
