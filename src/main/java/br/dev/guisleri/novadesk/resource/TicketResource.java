package br.dev.guisleri.novadesk.resource;

import br.dev.guisleri.novadesk.dto.CreateTicketRequestDTO;
import br.dev.guisleri.novadesk.dto.TicketResponseDTO;
import br.dev.guisleri.novadesk.dto.UpdateTicketRequestDTO;
import br.dev.guisleri.novadesk.model.Ticket;
import br.dev.guisleri.novadesk.service.TicketService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

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
    public Response updateTicket(@PathParam("id") long id,
                                 @Valid UpdateTicketRequestDTO requestDTO) {

        Optional<Ticket> updatedTicket = ticketService.updateTicket(id, requestDTO);

        if (updatedTicket.isPresent()) {
            return Response.ok(
                    convertToResponseDTO(updatedTicket.get())
            ).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTicket(@PathParam("id") long id) {
        boolean removed = ticketService.deleteTicketById(id);

        if (removed) {
            return Response.ok().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TicketResponseDTO> getTickets() {
        return ticketService.getAllTickets()
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTicketById(@PathParam("id") long id) {
        Optional<Ticket> ticket = ticketService.getTicketById(id);

        if (ticket.isPresent()) {
            return Response.ok(
                    convertToResponseDTO(ticket.get())
            ).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
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
