package br.dev.guisleri.novadesk.resource;

import br.dev.guisleri.novadesk.model.Ticket;
import br.dev.guisleri.novadesk.model.TicketPriority;
import br.dev.guisleri.novadesk.model.TicketStatus;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Ticket> getTickets() {
        return tickets;
    }

}
