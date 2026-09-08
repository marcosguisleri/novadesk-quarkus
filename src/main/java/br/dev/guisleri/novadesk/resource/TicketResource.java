package br.dev.guisleri.novadesk.resource;

import br.dev.guisleri.novadesk.model.Ticket;
import br.dev.guisleri.novadesk.model.TicketPriority;
import br.dev.guisleri.novadesk.model.TicketStatus;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Path("/tickets")
public class TicketResource {

    List<Ticket> tickets = new ArrayList<>();

    Ticket ticket1 = new Ticket(
            1L, "Ticket1", "Descrição Ticket 1",
            "Marcos Guisleri", TicketStatus.OPEN,
            TicketPriority.HIGH
    );

    Ticket ticket2 = new Ticket(
            2L, "Ticket 2", "Descrição Ticket 2",
            "Marcos Guisleri", TicketStatus.OPEN,
            TicketPriority.HIGH
    );

    Ticket ticket3 = new Ticket(
            3L, "Ticket 3", "Descrição Ticket 3",
            "Marcos Guisleri", TicketStatus.OPEN,
            TicketPriority.HIGH
    );

    Ticket ticket4 = new Ticket(
            4L, "Ticket 4", "Descrição Ticket 4",
            "Marcos Guisleri", TicketStatus.OPEN,
            TicketPriority.HIGH
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

        tickets.add(ticket);

        return Response.status(Response.Status.CREATED).entity(ticket).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Ticket> getTickets() {
        return tickets;
    }

}
