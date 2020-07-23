package pt.isel.daw.interceptors

import org.apache.tomcat.util.codec.binary.Base64
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import pt.isel.daw.exceptions.AuthenticationException
import pt.isel.daw.model.User
import pt.isel.daw.service.contract.IUserService
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private val logger = LoggerFactory.getLogger(BasicAuthInterceptor::class.java)

@Component
class BasicAuthInterceptor(private val userService: IUserService) : HandlerInterceptor {

    private val headerAuthorizationRegex = """Basic (.+)""".toRegex()
    private val basicAuthorizationRegex = """(\w+):([^:\s]+)""".toRegex()
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        logger.info(request.method + " | " + request.requestURI + "\n")

        if (request.method == "OPTIONS") {
            response.status = 200
            return true
        }

        if (request.requestURI.startsWith("/projects") && !isGetMethod(request)) {

            val auth = request.getHeader(HttpHeaders.AUTHORIZATION)
                    ?: throw AuthenticationException("Authorization header required.")
            if (headerAuthorizationRegex.matches(auth)) {
                val (base64) = headerAuthorizationRegex.find(auth)!!.destructured
                val login = String(Base64.decodeBase64(base64))
                if (basicAuthorizationRegex.matches(login)) {
                    val (username, password) = basicAuthorizationRegex.find(login)!!.destructured
                    val userId = userService.login(User(username = username, password = password))
                    request.setAttribute("uid", userId)
                    return true
                }
                throw AuthenticationException("User does not exist.")
            }
            throw AuthenticationException("Invalid authorization header.")
        }
        return true
    }

    fun isGetMethod(request: HttpServletRequest): Boolean = request.method == HttpMethod.GET.name

}

