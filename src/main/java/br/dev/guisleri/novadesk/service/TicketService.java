package br.dev.guisleri.novadesk.service;

import br.dev.guisleri.novadesk.model.Ticket;
import br.dev.guisleri.novadesk.model.TicketPriority;
import br.dev.guisleri.novadesk.model.TicketStatus;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TicketService {

    private final List<Ticket> tickets = new ArrayList<>();

    public TicketService() {
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

        tickets.add(ticket1);
        tickets.add(ticket2);
        tickets.add(ticket3);
        tickets.add(ticket4);
    }

    public Ticket createTicket(Ticket ticket) {
        long newId = (tickets.size() + 1);

        ticket.setId(newId);
        ticket.setCreatedAt(LocalDateTime.now());

        tickets.add(ticket);

        return ticket;
    }

    public Optional<Ticket> updateTicket(long id, Ticket ticket) {
        Optional<Ticket> ticketToUpdate = getTicketById(id);

        if (ticketToUpdate.isPresent()) {
            Ticket existingTicket = ticketToUpdate.get();

            existingTicket.setTitle(ticket.getTitle());
            existingTicket.setDescription(ticket.getDescription());
            existingTicket.setRequester(ticket.getRequester());
            existingTicket.setStatus(ticket.getStatus());
            existingTicket.setPriority(ticket.getPriority());

            return Optional.of(existingTicket);
        }

        return Optional.empty();
    }

    public boolean deleteTicketById(long id) {
        return tickets.removeIf(t -> t.getId() == id);
    }

    public List<Ticket> getAllTickets() {
        return tickets;
    }

    public Optional<Ticket> getTicketById(long id) {
        return tickets.stream()
                .filter(ticket -> ticket.getId() == id)
                .findFirst();
    }

}
