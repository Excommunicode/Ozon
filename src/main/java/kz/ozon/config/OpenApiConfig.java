package kz.ozon.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Ozon",
                description = "Loyalty System", version = "1.0.0",
                contact = @Contact(
                        name = "Farukh Mahmuthodzhaev",
                        email = "faruh28.06mail.ru"
                )
        )
)
public class OpenApiConfig {

}
