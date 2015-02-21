package com.github.tomjankes.wiremock.features
import com.github.tomjankes.wiremock.StubbingFeature
import groovy.json.JsonBuilder

class JsonBodyFeature implements StubbingFeature {

    /**
     * Replace jsonBody key in request and response with body
     * that is generated json string
     *
     * @param input json map
     * @return void
     */
    @Override
    def apply(input) {
        ['request', 'response'].each {
            if (input.containsKey(it) && input[it].containsKey('jsonBody')) {
                input[it]['body'] = new JsonBuilder(input[it]['jsonBody']).toString()
                input[it].remove('jsonBody')
            }
        }
        input
    }
}
