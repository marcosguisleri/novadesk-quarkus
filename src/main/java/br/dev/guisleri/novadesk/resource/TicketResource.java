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

import java.util.List;

@Path("/tickets")
public class TicketResource {

    @Inject
    TicketService ticketService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTicket(@Valid CreateTicketRequestDTO requestDTO) {
        Ticket createdTicket = ticketService.createTicket(requestDTO);

        return Response.status(Response.Status.CREATED)
                .entity(convertToResponseDTO(createdTicket))
                .build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTicket(
            @PathParam("id") long id,
            @Valid UpdateTicketRequestDTO requestDTO
    ) {
        Ticket updatedTicket = ticketService.updateTicket(id, requestDTO);

        return Response.ok(
                convertToResponseDTO(updatedTicket)
        ).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTicket(@PathParam("id") long id) {
        ticketService.deleteTicketById(id);

        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TicketResponseDTO> getTickets(
            @QueryParam("status") TicketStatus status,
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
    public Response getTicketById(@PathParam("id") long id) {
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
