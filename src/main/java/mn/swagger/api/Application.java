package mn.swagger.api;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.info.*;

@OpenAPIDefinition(
        info = @Info(
                title = "foo-service",
                version = "1.0",
                description = "Foo API",
                contact = @Contact(url = "https://rmondejar.github.io", name = "Ruben Mondejar")))
public class Application {
    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}
