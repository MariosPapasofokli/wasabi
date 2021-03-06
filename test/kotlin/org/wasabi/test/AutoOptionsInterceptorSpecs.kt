package org.wasabi.test

import org.junit.Test as spec
import org.wasabi.interceptors.enableAutoOptions
import kotlin.test.assertEquals
import org.wasabi.http.StatusCodes

public class AutoOptionsInterceptorSpecs : TestServerContext() {

    spec fun auto_options_should_return_all_methods_available_for_a_specific_resource () {

        TestServer.reset()
        TestServer.appServer.get("/person", {})
        TestServer.appServer.post("/person", {})
        TestServer.appServer.post("/customer", {})
        TestServer.appServer.enableAutoOptions()

        val response = options("http://localhost:3000/person")

        assertEquals("GET,POST", response.headers.filter { it.getName() == "Allow"}.first().getValue())

    }

    spec fun with_auto_options_disabled_options_should_return_method_not_allowed () {
        TestServer.reset()

        TestServer.appServer.get("/person", {})

        val response = options("http://localhost:3000/person")

        assertEquals(StatusCodes.MethodNotAllowed.code, response.statusCode)
    }

}