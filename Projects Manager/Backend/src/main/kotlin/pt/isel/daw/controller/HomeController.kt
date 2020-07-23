package pt.isel.daw.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import pt.isel.daw.common.Href
import pt.isel.daw.common.MIMEType as MediaType

@Controller
class HomeController {
    @RequestMapping(path = [Href.Path.HOME], method = [RequestMethod.GET],
            produces = [MediaType.APPLICATION_JSON_HOME_VALUE])
    fun getHomeAPI() = "home_api.json"
}