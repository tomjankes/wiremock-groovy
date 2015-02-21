# Wiremock groovy
[![Build Status](https://travis-ci.org/tomjankes/wiremock-groovy.svg?branch=master)](https://travis-ci.org/tomjankes/wiremock-groovy)
## What it is

I love [WireMock](https://github.com/tomakehurst/wiremock) and use it extensively in integration tests. I also love how [Spock](https://github.com/spockframework/spock) allows to write very readable and clean tests.
This project aims to create more suitable groovy API, that will allow more concise stubbing and verifying syntax in integration tests written in groovy/spock using WireMock, without using WireMock's default static imports API.

## What it does

Currently there is only extremely thin wrapper for Wire Mock REST API, that is exposed as raw json builder.
Examples of what can be currently achieved:

### Stubbing

```groovy
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
    ...
}
```

### Verifying

Verifying can be achieved by querying for count of matching requests that have been sent to WireMock server.

```groovy
@Rule
WireMockRule wireMockRule = new WireMockRule()

def wireMockStub = new WireMockGroovy()

def "example verifying test" () {
    ...
    then:
    1 == wireMockStub.count {
        method "GET"
        url "/some/url"
    }
}

def "test using groovy truth if you need at least one request and shows example matcher" () {
    ...
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
```

##Contribution

Project is in very early stage, pull requests and ideas in form of feature requests are welcome.

## Building

* Clone the repository
* Run `./gradlew clean build` (on Linux/Mac) or `gradlew.bat build` (on Windows)

## Licence

Apache Licence v2.0 (see LICENCE.txt)

