package br.dev.guisleri.novadesk.resource;

import br.dev.guisleri.novadesk.dto.CreateTicketRequestDTO;
import br.dev.guisleri.novadesk.dto.TicketResponseDTO;
import br.dev.guisleri.novadesk.dto.UpdateTicketRequestDTO;
import br.dev.guisleri.novadesk.model.Ticket;
import br.dev.guisleri.novadesk.model.TicketPriority;
import br.dev.guisleri.novadesk.model.TicketStatus;
import br.dev.guisleri.novadesk.service.TicketService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/tickets")
@Tag(name = "Tickets", description = "Operações para gerenciamento de chamados internos")
public class TicketResource {

    @Inject
    TicketService ticketService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Criar ticket", description = "Cria um novo chamado com status inicial OPEN")
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "Ticket criado com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = TicketResponseDTO.class)
                    )
            ),
            @APIResponse(responseCode = "400", description = "Dados do ticket inválidos")
    })
    public Response createTicket(
            @RequestBody(
                    description = "Dados necessários para criação do ticket",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CreateTicketRequestDTO.class)
                    )
            )
            @Valid CreateTicketRequestDTO requestDTO
    ) {
        Ticket createdTicket = ticketService.createTicket(requestDTO);

        return Response.status(Response.Status.CREATED)
                .entity(convertToResponseDTO(createdTicket))
                .build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Atualizar ticket", description = "Atualiza os dados e o status de um ticket existente")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Ticket atualizado com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = TicketResponseDTO.class)
                    )
            ),
            @APIResponse(responseCode = "400", description = "Dados do ticket inválidos"),
            @APIResponse(responseCode = "404", description = "Ticket não encontrado")
    })
    public Response updateTicket(
            @Parameter(description = "Identificador do ticket", required = true, example = "1")
            @PathParam("id") long id,
            @RequestBody(
                    description = "Novos dados do ticket",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = UpdateTicketRequestDTO.class)
                    )
            )
            @Valid UpdateTicketRequestDTO requestDTO
    ) {
        Ticket updatedTicket = ticketService.updateTicket(id, requestDTO);

        return Response.ok(
                convertToResponseDTO(updatedTicket)
        ).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Excluir ticket", description = "Exclui um ticket pelo identificador")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Ticket excluído com sucesso"),
            @APIResponse(responseCode = "404", description = "Ticket não encontrado")
    })
    public Response deleteTicket(
            @Parameter(description = "Identificador do ticket", required = true, example = "1")
            @PathParam("id") long id
    ) {
        ticketService.deleteTicketById(id);

        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Listar tickets", description = "Lista os tickets, com filtros opcionais por status e prioridade")
    @APIResponse(
            responseCode = "200",
            description = "Tickets encontrados",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.ARRAY, implementation = TicketResponseDTO.class)
            )
    )
    public List<TicketResponseDTO> getTickets(
            @Parameter(description = "Status do ticket", example = "OPEN")
            @QueryParam("status") TicketStatus status,
            @Parameter(description = "Prioridade do ticket", example = "HIGH")
            @QueryParam("priority") TicketPriority priority
    ) {
        return ticketService.getTickets(status, priority)
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Buscar ticket por ID", description = "Retorna um ticket pelo identificador")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Ticket encontrado",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = TicketResponseDTO.class)
                    )
            ),
            @APIResponse(responseCode = "404", description = "Ticket não encontrado")
    })
    public Response getTicketById(
            @Parameter(description = "Identificador do ticket", required = true, example = "1")
            @PathParam("id") long id
    ) {
        Ticket ticket = ticketService.getTicketById(id);

        return Response.ok(
                convertToResponseDTO(ticket)
        ).build();
    }

    private TicketResponseDTO convertToResponseDTO(Ticket ticket) {
        return new TicketResponseDTO(ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getRequester(),
                ticket.getStatus(),
                ticket.getPriority(),
                ticket.getCreatedAt());
    }

}
