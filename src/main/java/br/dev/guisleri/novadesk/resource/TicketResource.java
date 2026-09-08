package br.dev.guisleri.novadesk.resource;

import br.dev.guisleri.novadesk.model.Ticket;
import br.dev.guisleri.novadesk.model.TicketPriority;
import br.dev.guisleri.novadesk.model.TicketStatus;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Path("/tickets")
public class TicketResource {

    List<Ticket> tickets = new ArrayList<>();

    Ticket ticket1 = new Ticket(
            1L, "Ticket1", "Descrição Ticket 1",
            "Marcos Guisleri", TicketStatus.OPEN,
            TicketPriority.HIGH, LocalDateTime.now()
    );

    Ticket ticket2 = new Ticket(
            2L, "Ticket 2", "Descrição Ticket 2",
            "Marcos Guisleri", TicketStatus.OPEN,
            TicketPriority.HIGH, LocalDateTime.now()
    );

    Ticket ticket3 = new Ticket(
            3L, "Ticket 3", "Descrição Ticket 3",
            "Marcos Guisleri", TicketStatus.OPEN,
            TicketPriority.HIGH, LocalDateTime.now()
    );

    Ticket ticket4 = new Ticket(
            4L, "Ticket 4", "Descrição Ticket 4",
            "Marcos Guisleri", TicketStatus.OPEN,
            TicketPriority.HIGH, LocalDateTime.now()
    );

    public TicketResource() {
        tickets.add(ticket1);
        tickets.add(ticket2);
        tickets.add(ticket3);
        tickets.add(ticket4);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTicket(Ticket ticket) {
        long newId = (tickets.size() + 1);

        ticket.setId(newId);
        ticket.setCreatedAt(LocalDateTime.now());

        tickets.add(ticket);

        return Response.status(Response.Status.CREATED).entity(ticket).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTicket(@PathParam("id") long id, Ticket ticket) {
        Optional<Ticket> ticketToUpdate = tickets.stream()
                .filter(t -> t.getId() == id)
                .findFirst();

        if (ticketToUpdate.isPresent()) {
            Ticket existingTicket = ticketToUpdate.get();

            existingTicket.setTitle(ticket.getTitle());
            existingTicket.setDescription(ticket.getDescription());
            existingTicket.setRequester(ticket.getRequester());
            existingTicket.setStatus(ticket.getStatus());
            existingTicket.setPriority(ticket.getPriority());

            return Response.ok(existingTicket).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Ticket> getTickets() {
        return tickets;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTicketById(@PathParam("id") long id) {
        Optional<Ticket> ticket = tickets.stream()
                .filter(t -> t.getId() == id)
                .findFirst();

        return ticket.isPresent()
                ? Response.ok(ticket.get()).build()
                : Response.status(Response.Status.NOT_FOUND)
                .entity("Ticket " + " com id " + id + " não encontrado.")
                .build();
    }

}
