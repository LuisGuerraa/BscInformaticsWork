package pt.isel.daw.controller.representation.output.representation

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import org.springframework.http.HttpMethod
import java.net.URI

/**
 * For details regarding the Siren media type, see <a href="https://github.com/kevinswiber/siren">Siren</a>
 */
@JsonPropertyOrder("class", "properties", "links", "actions", "entities")
@JsonInclude(JsonInclude.Include.NON_NULL)
abstract class Siren(
        val clazz: List<String> = listOf(),
        open val properties: Map<String, Any?>,
        open val links: List<SirenLink> = listOf(),
        open val actions: List<SirenAction> = listOf(),
        open val entities: List<SirenEntity> = listOf()
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SirenLink(
        val rel: List<String>,
        val href: URI,
        val title: String? = null
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SirenAction(
        val name: String,
        val href: URI,
        val title: String?,
        @JsonProperty("class")
        val clazz: List<String>? = null,
        val method: HttpMethod? = null,
        @JsonSerialize(using = ToStringSerializer::class)
        val type: String? = null,
        val fields: List<SirenField>? = null
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SirenField(
        val name: String,
        val type: String? = null,
        val value: Any? = null,
        val title: String? = null
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SirenEntity(
        @JsonProperty("class") val clazz: List<String>? = null,
        val properties: Any? = null,
        val links: List<SirenLink>? = null,
        val actions: List<SirenAction>? = null,
        val title: String? = null
)