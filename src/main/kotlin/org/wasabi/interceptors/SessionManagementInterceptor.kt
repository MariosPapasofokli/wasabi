package org.wasabi.interceptors

import org.wasabi.http.Request
import org.wasabi.http.Response
import org.wasabi.app.AppServer
import org.wasabi.http.Session
import org.wasabi.http.Cookie
import java.util.UUID

public class SessionManagementInterceptor(val cookieKey: String = "_sessionID", sessionStorage: SessionStorage = InMemorySessionStorage()): Interceptor, SessionStorage by sessionStorage {

    private fun generateSessionID(): String {
        // TODO: Tie this to IP/etc.
        return UUID.randomUUID().toString()
    }

    override fun intercept(request: Request, response: Response): Boolean {
        val x = request.cookies[cookieKey]
        if (x != null && x.value != "") {
            request.session = loadSession(request.session!!.id)
        } else {
            request.session = Session(generateSessionID())
            storeSession(request.session!!)
            response.cookies[cookieKey] = Cookie(cookieKey, request.session!!.id, request.path, request.host, request.isSecure)
        }
        return true
    }
}


public fun AppServer.enableSessionSupport() {
    intercept(SessionManagementInterceptor())
}