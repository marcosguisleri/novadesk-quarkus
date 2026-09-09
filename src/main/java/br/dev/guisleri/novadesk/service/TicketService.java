package br.dev.guisleri.novadesk.service;

import br.dev.guisleri.novadesk.dto.CreateTicketRequestDTO;
import br.dev.guisleri.novadesk.dto.UpdateTicketRequestDTO;
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

    }

    public Ticket createTicket(CreateTicketRequestDTO requestDTO) {
        Ticket ticket = new Ticket();

        ticket.setTitle(requestDTO.title());
        ticket.setDescription(requestDTO.description());
        ticket.setRequester(requestDTO.requester());
        ticket.setPriority(requestDTO.priority());

        ticket.setId(tickets.size() + 1);
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCreatedAt(LocalDateTime.now());

        tickets.add(ticket);

        return ticket;
    }

    public Optional<Ticket> updateTicket(long id, UpdateTicketRequestDTO requestDTO) {
        Optional<Ticket> ticketToUpdate = getTicketById(id);

        if (ticketToUpdate.isPresent()) {
            Ticket existingTicket = ticketToUpdate.get();

            existingTicket.setTitle(requestDTO.title());
            existingTicket.setDescription(requestDTO.description());
            existingTicket.setRequester(requestDTO.requester());
            existingTicket.setStatus(requestDTO.status());

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
