package pt.isel.daw.config

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpOutputMessage
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import pt.isel.daw.common.MIMEType
import pt.isel.daw.controller.representation.output.contract.IOutputModel
import pt.isel.daw.interceptors.BasicAuthInterceptor
import java.lang.reflect.Type

@Configuration
class AppConfig(val authInterceptor: BasicAuthInterceptor) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor)
    }

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        converters.add(0, SirenMessageConverter())
    }

    class SirenMessageConverter : MappingJackson2HttpMessageConverter() {
        override fun writeInternal(obj: Any, type: Type?, outputMessage: HttpOutputMessage) {
            super.writeInternal((obj as IOutputModel).toSirenOutputModel(), type, outputMessage)
        }

        override fun canWrite(mediaType: MediaType?) =
                mediaType?.isCompatibleWith(MIMEType.APPLICATION_JSON_SIREN) ?: false
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        //registry
        //        .addMapping("/**")
        //        .allowedHeaders("*")
        //        .allowedMethods("*")
        //        .allowedOrigins("*")
    }
}