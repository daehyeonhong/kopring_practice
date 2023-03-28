import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.practice.kopring.common.presentation.IndexController
import org.junit.jupiter.api.Disabled
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc


@Disabled
@WebMvcTest(IndexController::class)
abstract class ControllerTest {
    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Throws(JsonProcessingException::class)
    protected fun createJson(dto: Any?): String {
        return this.objectMapper.writeValueAsString(dto)
    }
}
