package com.github.tomjankes.wiremock

/**
 * Allows modification of stub json before it is sent to WireMock
 */
interface StubbingFeature {
    /**
     * This method takes map as an input and returns modified version
     * of the map. The modified version will be used for constructing
     * json sent to WireMock while stubbing.
     *
     * It can be used to add/remove headers, body and any of the elements
     * that get sent to WireMock.
     *
     * @see com.github.tomjankes.wiremock.features.JsonBodyFeature for example
     *
     * @param input map of parameters provided by test writer
     * @return modified map that will be sent to WireMock
     */
    def apply(input)
}