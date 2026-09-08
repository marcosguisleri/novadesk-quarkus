package br.dev.guisleri.novadesk.resource;

import br.dev.guisleri.novadesk.model.Ticket;
import br.dev.guisleri.novadesk.service.TicketService;
import jakarta.inject.Inject;
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
    public Response createTicket(Ticket ticket) {
        Ticket createdTicket = ticketService.createTicket(ticket);

        return Response.status(Response.Status.CREATED)
                .entity(createdTicket)
                .build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTicket(@PathParam("id") long id, Ticket ticket) {
        Optional<Ticket> updatedTicket = ticketService.updateTicket(id, ticket);

        if (updatedTicket.isPresent()) {
            return Response.ok(updatedTicket.get()).build();
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
    public List<Ticket> getTickets() {
        return ticketService.getAllTickets();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTicketById(@PathParam("id") long id) {
        Optional<Ticket> ticket = ticketService.getTicketById(id);

        return ticket.isPresent()
                ? Response.ok(ticket.get()).build()
                : Response.status(Response.Status.NOT_FOUND)
                .entity("Ticket com id " + id + " não encontrado.")
                .build();
    }

}
