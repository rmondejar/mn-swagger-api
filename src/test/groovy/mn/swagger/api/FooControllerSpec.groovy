package mn.swagger.api

import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.runtime.server.EmbeddedServer
import mn.swagger.api.dtos.BarDto
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Instant

class FooControllerSpec extends Specification {

    @Shared
    @AutoCleanup
    EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)

    @Shared
    @AutoCleanup
    RxHttpClient client = embeddedServer.applicationContext.createBean(RxHttpClient, embeddedServer.getURL())

    @Shared
    BarDto foundBar

    @Unroll
    def "Create a new bar object with #text as an id"() {

        when: 'Creates a bar object'
        HttpRequest request = HttpRequest.POST("/bars", text)
        HttpResponse<BarDto> response = client.toBlocking().exchange(request, BarDto)
        BarDto bar = response.body()

        then: 'Correct bar object is returned'
        response.status == HttpStatus.CREATED
        bar
        bar.id == text
        bar.label.contains(text)
        bar.label.size() == size
        bar.seconds <= Instant.now().getEpochSecond()

        where:
        text    | size
        "Hello" | 5+8
        "Foo"   | 3+8
        "World" | 5+8
        "Bar"   | 3+8
    }

    def "Retrieve a previously created bar object"() {

        given:
        String text = 'Bar'

        when: 'Find the bar remotely'
        HttpRequest request = HttpRequest.GET("/bars/$text")
        HttpResponse<BarDto> response = client.toBlocking().exchange(request, BarDto)
        foundBar = response.body()

        then:
        noExceptionThrown()
        response.status == HttpStatus.OK
        foundBar
        foundBar.id == text
        foundBar.label
        foundBar.label.contains(text)
        foundBar.seconds
        foundBar.seconds <= Instant.now().getEpochSecond()
    }

    def "Update the previously found bar object"() {

        given:
        String text = foundBar.id

        when: 'Update the bar remotely'
        HttpRequest request = HttpRequest.PUT("/bars/$text", void)
        HttpResponse<BarDto> response = client.toBlocking().exchange(request, BarDto)
        BarDto updatedBar = response.body()

        then:
        noExceptionThrown()
        response.status == HttpStatus.OK
        updatedBar
        updatedBar.id == foundBar.id
        updatedBar.label
        updatedBar.label.contains(foundBar.id)
        updatedBar.seconds
        updatedBar.seconds >= foundBar.seconds
    }

    def "Retrieve all the created bars until now"() {

        when: 'Find all the bars remotely'
        HttpRequest request = HttpRequest.GET("/bars")
        HttpResponse<List<BarDto>> response = client.toBlocking().exchange(request, List)
        List<BarDto> bars = response.body()

        then:
        noExceptionThrown()
        response.status == HttpStatus.OK
        bars
        bars?.size() == 4

        bars.first()
        bars.first().id
        bars.first().label
        bars.first().seconds
        bars.first().seconds <= Instant.now().getEpochSecond()
    }

    def "Remove the previously found bar"() {

        given: 'The same id as the preivously found bar'
        String text = foundBar.id

        when: 'Delete the bar remotely'
        HttpRequest deleteReq = HttpRequest.DELETE("/bars/$text")
        HttpResponse removeResp = client.toBlocking().exchange(deleteReq, void)

        and: 'Find again all the remaining bar objects'
        HttpRequest findReq = HttpRequest.GET("/bars")
        HttpResponse<List<BarDto>> findResp = client.toBlocking().exchange(findReq, List)
        List<BarDto> bars = findResp.body()

        then:
        removeResp
        removeResp.status == HttpStatus.ACCEPTED

        findResp
        findResp.status == HttpStatus.OK
        bars.size() == 3
    }


}
