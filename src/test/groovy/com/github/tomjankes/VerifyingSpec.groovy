package com.github.tomjankes
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.apache.http.client.methods.HttpDelete
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.junit.Rule
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class VerifyingSpec extends Specification {
    @Rule
    WireMockRule wireMockRule = new WireMockRule()
    @Subject
    WireMockGroovy verifier = new WireMockGroovy()

    @Unroll
    def "should return count of matching requests for request count equal to #requestsCount"() {
        given:
        requestsCount.times {
            HttpClients.createDefault().execute(new HttpGet("http://localhost:8080/theaddress"))
        }

        when:
        def returnedCount = verifier.count {
            method "GET"
            url "/theaddress"
        }

        then:
        returnedCount == requestsCount

        where:
        requestsCount << [0, 1, 3]
    }


    def "should only count matching requests" () {
        given:
        HttpClients.createDefault().execute(new HttpGet("http://localhost:8080/theaddress"))
        HttpClients.createDefault().execute(new HttpGet("http://localhost:8080/anotheraddress"))
        HttpClients.createDefault().execute(new HttpDelete("http://localhost:8080/theaddress"))

        when:
        def returnedCount = verifier.count {
            method "GET"
            url "/theaddress"
        }

        then:
        returnedCount == 1
    }
}
