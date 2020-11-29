package mn.swagger.api.controller;

import java.util.List;
import javax.validation.constraints.NotBlank;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.reactivex.Single;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import mn.swagger.api.dtos.BarDto;
import mn.swagger.api.service.FooService;

import static io.micronaut.http.MediaType.APPLICATION_JSON;

@Controller("/")
public class FooController {

    private final FooService fooService;

    public FooController(FooService fooService) {
        this.fooService = fooService;
    }

    @Post(uri="/bars", produces= APPLICATION_JSON)
    @Operation(summary = "Creates a new bar object adding a decorated id and the current time",
            description = "Showcase of the creation of a dto"
    )
    @ApiResponse(
            content = @Content(mediaType = "application/json",
                    schema = @Schema(type="object"))
    )
    @ApiResponse(responseCode = "201", description = "Bar object correctly created")
    @ApiResponse(responseCode = "400", description = "Invalid id Supplied")
    @ApiResponse(responseCode = "500", description = "Remote error, server is going nuts")
    @Tag(name = "create")
    public Single<HttpResponse<BarDto>> create(@Body @NotBlank String id) {
        return Single.just(HttpResponse.created(fooService.create(id)));
    }

    @Put(uri="/bars/{id}", produces= APPLICATION_JSON)
    @Operation(summary = "Updates an existing bar object with a new label and modifying the current time",
            description = "Showcase of the update of a dto"
    )
    @ApiResponse(
            content = @Content(mediaType = "application/json",
                    schema = @Schema(type="object"))
    )
    @ApiResponse(responseCode = "200", description = "Bar object correctly updated")
    @ApiResponse(responseCode = "404", description = "Bar not found by using the provided id")
    @ApiResponse(responseCode = "500", description = "Remote error, server is going nuts")
    @Tag(name = "update")
    public Single<HttpResponse<BarDto>> update(@Parameter(description="Id to generate a Bar object") @NotBlank String id) {
        return Single.just(fooService.update(id)
                .map(HttpResponse::ok)
                .orElseGet(HttpResponse::notFound)
        );
    }

    @Get(uri="/bars/{id}", produces= APPLICATION_JSON)
    @Operation(summary = "Find the bar object corresponding to the provided id",
            description = "Showcase of a finder method returning a dto"
    )
    @ApiResponse(
            content = @Content(mediaType = "application/json",
                    schema = @Schema(type="object"))
    )
    @ApiResponse(responseCode = "200", description = "A bar object has been successfully found and returned")
    @ApiResponse(responseCode = "404", description = "Bar not found by using the provided id")
    @ApiResponse(responseCode = "500", description = "Remote error, server is going nuts")
    @Tag(name = "findById")
    public Single<HttpResponse<BarDto>> findById(@Parameter(description="id to find a bar object") @NotBlank String id) {
        return Single.just(fooService.findById(id)
                .map(HttpResponse::ok)
                .orElseGet(HttpResponse::notFound)
        );
    }

    @Get(uri="/bars", produces= APPLICATION_JSON)
    @Operation(summary = "Find all the bar objects",
            description = "Showcase of a method returning a list of dtos")
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(type="object")))
    @ApiResponse(responseCode = "200", description = "A list of bar objects is returned")
    @ApiResponse(responseCode = "500", description = "Remote error, server is going nuts")
    @Tag(name = "findAll")
    public Single<List<BarDto>> findAll() {
        return Single.just(fooService.findAll());
    }

    @Delete(uri="/bars/{id}", produces= APPLICATION_JSON)
    @Operation(summary = "Remove the bar object corresponding to the provided id",
            description = "Showcase of a deletion method"
    )
    @ApiResponse(responseCode = "202", description = "Bar object has been correctly removed")
    @ApiResponse(responseCode = "500", description = "Remote error, server is going nuts")
    @Tag(name = "remove")
    public Single<HttpResponse> remove(@Parameter(description="Id to remove a bar object") @NotBlank String id) {
        fooService.remove(id);
        return Single.just(HttpResponse.accepted());
    }

}
