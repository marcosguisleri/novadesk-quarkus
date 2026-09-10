package br.dev.guisleri.novadesk.service;

import br.dev.guisleri.novadesk.dto.CreateTicketRequestDTO;
import br.dev.guisleri.novadesk.dto.UpdateTicketRequestDTO;
import br.dev.guisleri.novadesk.exception.TicketNotFoundException;
import br.dev.guisleri.novadesk.model.Ticket;
import br.dev.guisleri.novadesk.model.TicketPriority;
import br.dev.guisleri.novadesk.model.TicketStatus;
import br.dev.guisleri.novadesk.repository.TicketRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public Ticket createTicket(CreateTicketRequestDTO requestDTO) {
        Ticket ticket = new Ticket();

        ticket.setTitle(requestDTO.title());
        ticket.setDescription(requestDTO.description());
        ticket.setRequester(requestDTO.requester());
        ticket.setPriority(requestDTO.priority());

        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCreatedAt(LocalDateTime.now());

        ticketRepository.persist(ticket);

        return ticket;
    }

    @Transactional
    public Ticket updateTicket(long id, UpdateTicketRequestDTO requestDTO) {
        Ticket existingTicket = getTicketById(id);

        existingTicket.setTitle(requestDTO.title());
        existingTicket.setDescription(requestDTO.description());
        existingTicket.setRequester(requestDTO.requester());
        existingTicket.setStatus(requestDTO.status());

        return existingTicket;
    }

    @Transactional
    public void deleteTicketById(long id) {
        Ticket ticketToDelete = getTicketById(id);

        ticketRepository.delete(ticketToDelete);
    }

    public List<Ticket> getTickets(TicketStatus status, TicketPriority priority) {

        if (status != null && priority != null) {
            return ticketRepository.findByStatusAndPriority(status, priority);
        }

        if (status != null) {
            return ticketRepository.findByStatus(status);
        }

        if (priority != null) {
            return ticketRepository.findByPriority(priority);
        }

        return ticketRepository.listAll();

    }

    public Ticket getTicketById(long id) {
        return ticketRepository.findByIdOptional(id)
                .orElseThrow(() -> new TicketNotFoundException(id));
    }

}
