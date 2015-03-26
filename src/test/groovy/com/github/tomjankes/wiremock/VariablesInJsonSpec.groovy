package com.github.tomjankes.wiremock
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static com.github.tomjankes.wiremock.testutils.HttpHelper.getJson

class VariablesInJsonSpec extends Specification {
    @Rule
    WireMockRule wireMockRule = new WireMockRule(8080)
    @Subject
    def stubber = new WireMockGroovy(8080)

    @Unroll
    def "should allow for variables in builder json with variables '#someValue', '#anotherValue'"() {
        when:
        stubber.stub {
            request {
                method "GET"
                url "/some/thing"
            }
            response {
                status 200
                body "{\"some_key\": \"$someValue\", \"second_key\": $anotherValue }"
                headers {
                    "Content-Type" "text/plain"
                }
            }
        }
        and:
        def json = getJson("http://localhost:8080/some/thing")

        then:
        json.some_key == someValue
        json.second_key == anotherValue

        where:
        someValue << ["great value 1", "great value 2"]
        anotherValue << [12.4, 43.4]
    }
}
