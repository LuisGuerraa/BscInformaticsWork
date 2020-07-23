package pt.isel.daw.interceptors

import org.slf4j.LoggerFactory
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.HandlerInterceptor
import pt.isel.daw.controller.representation.input.IntrospectInputModel
import pt.isel.daw.exceptions.AuthenticationException
import pt.isel.daw.service.contract.IUserService
import java.net.URI
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private val logger = LoggerFactory.getLogger(BearerAuthInterceptor::class.java)

@Component
class BearerAuthInterceptor(private val userService: IUserService) : HandlerInterceptor {

    private val HOST_OIDC = "http://x.x.x.x"
    private val bearerAuthorizationRegex = """Bearer (.+)""".toRegex()
    private val TOKEN_INTROSPECTION_ENDPOINT = "$HOST_OIDC/openid-connect-server-webapp/introspect"
    private val CLIENT_ID = "projectX"
    private val CLIENT_SECRET = "isel"

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        logger.info(request.method + " | " + request.requestURI + "\n")

        if (request.method == HttpMethod.OPTIONS.name) {
            response.status = 200
            return true
        }

        if (request.requestURI.startsWith("/projects") && !isGetMethod(request)) {
            val auth = request.getHeader(HttpHeaders.AUTHORIZATION)
                    ?: throw AuthenticationException("Authorization header required.")
            if (bearerAuthorizationRegex.matches(auth)) {
                val (accessToken) = bearerAuthorizationRegex.find(auth)!!.destructured
                val encodedAuth = java.util.Base64.getEncoder().encodeToString("${CLIENT_ID}:${CLIENT_SECRET}".toByteArray())
                val req = RequestEntity.post(URI(TOKEN_INTROSPECTION_ENDPOINT))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .header("Authorization", "Basic $encodedAuth")
                        .body("token=$accessToken")
                val resp = RestTemplate().exchange(req, IntrospectInputModel::class.java)
                val introspect = resp.body!!.toIntrospect()
                if (resp.statusCode != HttpStatus.CREATED || !introspect.active.toBoolean())
                    throw AuthenticationException("Invalid Authorization header.")
                request.setAttribute("uid", userService.getUser(introspect.username))
                return true
            }
            throw AuthenticationException("Invalid Authorization header.")
        }
        return true
    }

    fun isGetMethod(request: HttpServletRequest): Boolean = request.method == HttpMethod.GET.name

}