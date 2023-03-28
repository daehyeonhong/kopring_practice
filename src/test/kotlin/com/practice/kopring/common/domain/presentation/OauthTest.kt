import io.restassured.RestAssured
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = ["spring.config.location=classpath:/application-oauth.yml"])
class OauthTest {
    @BeforeEach
    fun setup() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = 8080
    }

    @Test
    fun google로그인_시도하면_OAuth인증창_등장한다() {
        RestAssured.given()
            .`when`()
            .redirects().follow(false)["/login"]
            .then()
            .statusCode(302)
            .header("Location", Matchers.containsString("https://accounts.google.com/o/oauth2/auth"))
    }
}
