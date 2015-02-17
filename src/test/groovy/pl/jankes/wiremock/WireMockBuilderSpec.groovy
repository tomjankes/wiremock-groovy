package pl.jankes.wiremock
import com.github.tomakehurst.wiremock.junit.WireMockRule
import groovy.json.JsonSlurper
import org.junit.Rule
import spock.lang.Ignore
import spock.lang.Specification

class WireMockBuilderSpec extends Specification {

    public static final int PORT = 8080

    @Rule
    WireMockRule wireMockRule = new WireMockRule(PORT)

    def "should correctly stub wire mock get request" () {
        given:
        def builder = new WireMockStub(PORT)

        when:
        builder.stub {
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

        then:
        new URL("http://localhost:8080/some/thing").getText() == "Some body"
    }

    @Ignore("That would be awesome")
    def "should automagically handle json" () {
        given:
        def builder = new WireMockStub(PORT)

        when:
        builder.stub {
            request {
                method "GET"
                url "/some/thing"
            }
            response {
                status 200
                body {
                    element "elementValue"
                }
                headers {
                    "Content-Type" "text/plain"
                }
            }
        }

        then:
        def reader = new StringReader(new URL("http://localhost:8080/some/thing").getText())
        new JsonSlurper().parse(reader).element == "elementValue"
    }
}
