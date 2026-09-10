package br.dev.guisleri.novadesk.service;

import br.dev.guisleri.novadesk.dto.CreateTicketRequestDTO;
import br.dev.guisleri.novadesk.dto.UpdateTicketRequestDTO;
import br.dev.guisleri.novadesk.exception.TicketNotFoundException;
import br.dev.guisleri.novadesk.model.Ticket;
import br.dev.guisleri.novadesk.model.TicketPriority;
import br.dev.guisleri.novadesk.model.TicketStatus;
import br.dev.guisleri.novadesk.repository.TicketRepository;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestTransaction
class TicketServiceTest {

    @Inject
    TicketService ticketService;

    @Inject
    TicketRepository ticketRepository;

    @Test
    void shouldCreateTicketWithInitialState() {
        LocalDateTime beforeCreation = LocalDateTime.now();

        Ticket ticket = ticketService.createTicket(new CreateTicketRequestDTO(
                "Erro ao acessar o sistema",
                "O usuário não consegue autenticar.",
                "maria@empresa.com",
                TicketPriority.HIGH
        ));

        assertTrue(ticket.getId() > 0);
        assertEquals("Erro ao acessar o sistema", ticket.getTitle());
        assertEquals("O usuário não consegue autenticar.", ticket.getDescription());
        assertEquals("maria@empresa.com", ticket.getRequester());
        assertEquals(TicketPriority.HIGH, ticket.getPriority());
        assertEquals(TicketStatus.OPEN, ticket.getStatus());
        assertNotNull(ticket.getCreatedAt());
        assertFalse(ticket.getCreatedAt().isBefore(beforeCreation));
        assertNotNull(ticketRepository.findById(ticket.getId()));
    }

    @Test
    void shouldListAllTickets() {
        Ticket first = persistTicket("Falha no login", TicketStatus.OPEN, TicketPriority.HIGH);
        Ticket second = persistTicket("Impressora offline", TicketStatus.RESOLVED, TicketPriority.LOW);

        List<Ticket> tickets = ticketService.getTickets(null, null);

        assertEquals(2, tickets.size());
        assertTrue(tickets.stream().map(Ticket::getId).toList().containsAll(List.of(first.getId(), second.getId())));
    }

    @Test
    void shouldFindExistingTicketById() {
        Ticket persisted = persistTicket("Falha no login", TicketStatus.OPEN, TicketPriority.CRITICAL);

        Ticket found = ticketService.getTicketById(persisted.getId());

        assertEquals(persisted.getId(), found.getId());
        assertEquals("Falha no login", found.getTitle());
    }

    @Test
    void shouldThrowWhenTicketDoesNotExist() {
        TicketNotFoundException exception = assertThrows(
                TicketNotFoundException.class,
                () -> ticketService.getTicketById(999_999L)
        );

        assertEquals("Ticket com id 999999 não encontrado.", exception.getMessage());
    }

    @Test
    void shouldUpdateExistingTicket() {
        Ticket persisted = persistTicket("Falha no login", TicketStatus.OPEN, TicketPriority.HIGH);
        LocalDateTime createdAt = persisted.getCreatedAt();

        Ticket updated = ticketService.updateTicket(persisted.getId(), new UpdateTicketRequestDTO(
                "Falha de acesso corrigida",
                "A causa foi identificada e corrigida.",
                "suporte@empresa.com",
                TicketStatus.RESOLVED
        ));

        assertEquals("Falha de acesso corrigida", updated.getTitle());
        assertEquals("A causa foi identificada e corrigida.", updated.getDescription());
        assertEquals("suporte@empresa.com", updated.getRequester());
        assertEquals(TicketStatus.RESOLVED, updated.getStatus());
        assertEquals(TicketPriority.HIGH, updated.getPriority());
        assertEquals(createdAt, updated.getCreatedAt());
    }

    @Test
    void shouldThrowWhenUpdatingNonexistentTicket() {
        UpdateTicketRequestDTO request = new UpdateTicketRequestDTO(
                "Título atualizado",
                "Descrição atualizada",
                "suporte@empresa.com",
                TicketStatus.IN_PROGRESS
        );

        assertThrows(TicketNotFoundException.class, () -> ticketService.updateTicket(999_999L, request));
    }

    @Test
    void shouldDeleteExistingTicket() {
        Ticket persisted = persistTicket("Falha no login", TicketStatus.OPEN, TicketPriority.MEDIUM);

        ticketService.deleteTicketById(persisted.getId());

        assertFalse(ticketRepository.findByIdOptional(persisted.getId()).isPresent());
    }

    @Test
    void shouldFilterTicketsByStatus() {
        persistTicket("Falha no login", TicketStatus.OPEN, TicketPriority.HIGH);
        Ticket resolved = persistTicket("Impressora offline", TicketStatus.RESOLVED, TicketPriority.LOW);

        List<Ticket> tickets = ticketService.getTickets(TicketStatus.RESOLVED, null);

        assertEquals(List.of(resolved.getId()), tickets.stream().map(Ticket::getId).toList());
    }

    @Test
    void shouldFilterTicketsByPriority() {
        Ticket critical = persistTicket("Falha no login", TicketStatus.OPEN, TicketPriority.CRITICAL);
        persistTicket("Impressora offline", TicketStatus.OPEN, TicketPriority.LOW);

        List<Ticket> tickets = ticketService.getTickets(null, TicketPriority.CRITICAL);

        assertEquals(List.of(critical.getId()), tickets.stream().map(Ticket::getId).toList());
    }

    @Test
    void shouldFilterTicketsByStatusAndPriority() {
        Ticket expected = persistTicket("Falha no login", TicketStatus.RESOLVED, TicketPriority.HIGH);
        persistTicket("Impressora offline", TicketStatus.RESOLVED, TicketPriority.LOW);
        persistTicket("VPN indisponível", TicketStatus.OPEN, TicketPriority.HIGH);

        List<Ticket> tickets = ticketService.getTickets(TicketStatus.RESOLVED, TicketPriority.HIGH);

        assertEquals(List.of(expected.getId()), tickets.stream().map(Ticket::getId).toList());
    }

    private Ticket persistTicket(String title, TicketStatus status, TicketPriority priority) {
        Ticket ticket = new Ticket();
        ticket.setTitle(title);
        ticket.setDescription("Descrição do chamado");
        ticket.setRequester("usuario@empresa.com");
        ticket.setStatus(status);
        ticket.setPriority(priority);
        ticket.setCreatedAt(LocalDateTime.now());
        ticketRepository.persist(ticket);
        return ticket;
    }
}
