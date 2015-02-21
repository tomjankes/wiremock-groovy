package com.github.tomjankes.wiremock.features

import spock.lang.Specification
import spock.lang.Subject

class JsonBodyFeatureSpec extends Specification {
    @Subject
    def feature = new JsonBodyFeature()

    def "should modify json body, set the json string as a body parameter" () {
        given:
        def map = ['request': ['jsonBody': ['some': 1, 'other': ['x': 'y']]]]

        when:
        def result = feature.apply(map)

        then:
        result['request']['body'] == '{"some":1,"other":{"x":"y"}}'
    }
}
