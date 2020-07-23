package pt.isel.daw

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.web.context.WebApplicationContext
import pt.isel.daw.common.Classes
import pt.isel.daw.common.Href
import pt.isel.daw.common.MIMEType


@AutoConfigureMockMvc
@SpringBootTest
//@ContextConfiguration
//@RunWith(SpringJUnit4ClassRunner::class)
class DawProjectApplicationTests {

    @Autowired
    private lateinit var context: WebApplicationContext

    @Autowired
    private lateinit var mvc: MockMvc
/*
	@Before
	fun setup() {
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build()
	}

	@Test
	fun postProjectExample_ShouldReturn201() {
		mvc.post(Href.forProjects()){
			contentType = MediaType.APPLICATION_JSON
			content = "{ " +
					"\"name\": \"ProjectExample\", " +
					"\"description\" : \"Example of Project Description\"," +
					"\"issueStates\" : [\"open\", \"closed\", \"archived\"]," +
					"\"issueLabels\" : [\"new_functionality\", \"defect\"] " +
					"}"
		}.andExpect { status { isCreated } }
	}
*/

    @Test
    fun getProjects_ShouldReturn200_AndProjectOutputInfo() {
        mvc.get(Href.forProjects()) {
            accept = MIMEType.APPLICATION_JSON_SIREN
        }.andExpect {
            status { isOk }
            content { contentType(MIMEType.APPLICATION_JSON_SIREN_VALUE) }
            jsonPath("$.clazz") { value(Classes.PROJECTS) }
            jsonPath("$.properties") { exists() }
            jsonPath("$.links") { exists() }
            jsonPath("$.actions") { exists() }
        }
    }

    @Test
    fun getProjectGrupo3_ShouldReturn200_AndProjectOutputInfo() {
        mvc.get(Href.forProjectName("Grupo 3")) {
            accept = MIMEType.APPLICATION_JSON_SIREN
        }.andExpect {
            status { isOk }
            content { contentType(MIMEType.APPLICATION_JSON_SIREN_VALUE) }
            jsonPath("$.clazz") { value(Classes.PROJECT) }
            jsonPath("$.properties") { exists() }
            jsonPath("$.links") { exists() }
            jsonPath("$.actions") { exists() }
        }
    }

}
