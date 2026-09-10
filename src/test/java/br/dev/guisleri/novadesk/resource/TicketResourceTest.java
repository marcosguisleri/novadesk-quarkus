package br.dev.guisleri.novadesk.resource;

import br.dev.guisleri.novadesk.repository.TicketRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
class TicketResourceTest {

    @Inject
    TicketRepository ticketRepository;

    @BeforeEach
    @Transactional
    void clearDatabase() {
        ticketRepository.deleteAll();
    }

    @Test
    void shouldCreateTicket() {
        given()
                .contentType(ContentType.JSON)
                .body(createRequest("Erro ao acessar o sistema", "HIGH"))
                .when()
                .post("/tickets")
                .then()
                .statusCode(201)
                .body("id", greaterThan(0))
                .body("title", equalTo("Erro ao acessar o sistema"))
                .body("description", equalTo("Descrição detalhada do problema"))
                .body("requester", equalTo("maria@empresa.com"))
                .body("status", equalTo("OPEN"))
                .body("priority", equalTo("HIGH"))
                .body("createdAt", notNullValue());
    }

    @Test
    void shouldListTickets() {
        createTicket("Erro ao acessar o sistema", "HIGH");
        createTicket("Impressora está offline", "LOW");

        given()
                .when()
                .get("/tickets")
                .then()
                .statusCode(200)
                .body("", hasSize(2))
                .body("title", containsInAnyOrder("Erro ao acessar o sistema", "Impressora está offline"));
    }

    @Test
    void shouldFindExistingTicketById() {
        long id = createTicket("Erro ao acessar o sistema", "CRITICAL");

        given()
                .pathParam("id", id)
                .when()
                .get("/tickets/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo((int) id))
                .body("title", equalTo("Erro ao acessar o sistema"))
                .body("priority", equalTo("CRITICAL"));
    }

    @Test
    void shouldMapTicketNotFoundExceptionTo404() {
        given()
                .pathParam("id", 999_999)
                .when()
                .get("/tickets/{id}")
                .then()
                .statusCode(404)
                .body(equalTo("Ticket com id 999999 não encontrado."));
    }

    @Test
    void shouldUpdateExistingTicket() {
        long id = createTicket("Erro ao acessar o sistema", "HIGH");

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .body(updateRequest("Erro de acesso corrigido", "RESOLVED"))
                .when()
                .put("/tickets/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo((int) id))
                .body("title", equalTo("Erro de acesso corrigido"))
                .body("description", equalTo("Descrição atualizada do problema"))
                .body("requester", equalTo("suporte@empresa.com"))
                .body("status", equalTo("RESOLVED"))
                .body("priority", equalTo("HIGH"));
    }

    @Test
    void shouldReturn404WhenUpdatingNonexistentTicket() {
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", 999_999)
                .body(updateRequest("Erro de acesso corrigido", "RESOLVED"))
                .when()
                .put("/tickets/{id}")
                .then()
                .statusCode(404)
                .body(equalTo("Ticket com id 999999 não encontrado."));
    }

    @Test
    void shouldDeleteTicket() {
        long id = createTicket("Erro ao acessar o sistema", "MEDIUM");

        given()
                .pathParam("id", id)
                .when()
                .delete("/tickets/{id}")
                .then()
                .statusCode(200);

        given()
                .pathParam("id", id)
                .when()
                .get("/tickets/{id}")
                .then()
                .statusCode(404);
    }

    @Test
    void shouldFilterTicketsByStatus() {
        createTicket("Chamado ainda em aberto", "LOW");
        long resolvedId = createTicket("Chamado já resolvido", "HIGH");
        updateTicket(resolvedId, "Chamado já resolvido", "RESOLVED");

        given()
                .queryParam("status", "RESOLVED")
                .when()
                .get("/tickets")
                .then()
                .statusCode(200)
                .body("", hasSize(1))
                .body("[0].id", equalTo((int) resolvedId))
                .body("[0].status", equalTo("RESOLVED"));
    }

    @Test
    void shouldFilterTicketsByPriority() {
        long criticalId = createTicket("Falha crítica no servidor", "CRITICAL");
        createTicket("Solicitação de novo mouse", "LOW");

        given()
                .queryParam("priority", "CRITICAL")
                .when()
                .get("/tickets")
                .then()
                .statusCode(200)
                .body("", hasSize(1))
                .body("[0].id", equalTo((int) criticalId))
                .body("[0].priority", equalTo("CRITICAL"));
    }

    @Test
    void shouldFilterTicketsByStatusAndPriority() {
        long expectedId = createTicket("Falha crítica já resolvida", "CRITICAL");
        updateTicket(expectedId, "Falha crítica já resolvida", "RESOLVED");

        long otherResolvedId = createTicket("Dúvida simples já resolvida", "LOW");
        updateTicket(otherResolvedId, "Dúvida simples já resolvida", "RESOLVED");
        createTicket("Falha crítica ainda aberta", "CRITICAL");

        given()
                .queryParam("status", "RESOLVED")
                .queryParam("priority", "CRITICAL")
                .when()
                .get("/tickets")
                .then()
                .statusCode(200)
                .body("", hasSize(1))
                .body("[0].id", equalTo((int) expectedId))
                .body("[0].status", equalTo("RESOLVED"))
                .body("[0].priority", equalTo("CRITICAL"));
    }

    @ParameterizedTest(name = "deve rejeitar criação inválida: {0}")
    @MethodSource("invalidCreateRequests")
    void shouldReturn400ForInvalidCreateRequest(String scenario, Map<String, Object> request) {
        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/tickets")
                .then()
                .statusCode(400);
    }

    @ParameterizedTest(name = "deve rejeitar atualização inválida: {0}")
    @MethodSource("invalidUpdateRequests")
    void shouldReturn400ForInvalidUpdateRequest(String scenario, Map<String, Object> request) {
        long id = createTicket("Erro ao acessar o sistema", "HIGH");

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .body(request)
                .when()
                .put("/tickets/{id}")
                .then()
                .statusCode(400);
    }

    private static Stream<Arguments> invalidCreateRequests() {
        return Stream.of(
                Arguments.of("título em branco", createRequest(" ", "HIGH")),
                Arguments.of("título menor que cinco caracteres", createRequest("Erro", "HIGH")),
                Arguments.of("descrição em branco", requestWith("description", "")),
                Arguments.of("solicitante em branco", requestWith("requester", " ")),
                Arguments.of("prioridade ausente", requestWith("priority", null))
        );
    }

    private static Stream<Arguments> invalidUpdateRequests() {
        return Stream.of(
                Arguments.of("título em branco", updateRequest(" ", "OPEN")),
                Arguments.of("título menor que cinco caracteres", updateRequest("Erro", "OPEN")),
                Arguments.of("descrição em branco", updateRequestWith("description", "")),
                Arguments.of("solicitante em branco", updateRequestWith("requester", " ")),
                Arguments.of("status ausente", updateRequestWith("status", null))
        );
    }

    private static Map<String, Object> createRequest(String title, String priority) {
        Map<String, Object> request = new HashMap<>();
        request.put("title", title);
        request.put("description", "Descrição detalhada do problema");
        request.put("requester", "maria@empresa.com");
        request.put("priority", priority);
        return request;
    }

    private static Map<String, Object> requestWith(String field, Object value) {
        Map<String, Object> request = createRequest("Erro ao acessar o sistema", "HIGH");
        request.put(field, value);
        return request;
    }

    private static Map<String, Object> updateRequest(String title, String status) {
        Map<String, Object> request = new HashMap<>();
        request.put("title", title);
        request.put("description", "Descrição atualizada do problema");
        request.put("requester", "suporte@empresa.com");
        request.put("status", status);
        return request;
    }

    private static Map<String, Object> updateRequestWith(String field, Object value) {
        Map<String, Object> request = updateRequest("Erro de acesso corrigido", "IN_PROGRESS");
        request.put(field, value);
        return request;
    }

    private long createTicket(String title, String priority) {
        return given()
                .contentType(ContentType.JSON)
                .body(createRequest(title, priority))
                .when()
                .post("/tickets")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getLong("id");
    }

    private void updateTicket(long id, String title, String status) {
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .body(updateRequest(title, status))
                .when()
                .put("/tickets/{id}")
                .then()
                .statusCode(200);
    }
}
