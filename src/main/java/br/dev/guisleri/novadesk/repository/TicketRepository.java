package br.dev.guisleri.novadesk.repository;

import br.dev.guisleri.novadesk.model.Ticket;
import br.dev.guisleri.novadesk.model.TicketPriority;
import br.dev.guisleri.novadesk.model.TicketStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class TicketRepository implements PanacheRepository<Ticket> {

    public List<Ticket> findByStatus(TicketStatus status) {
        return find("status", status).list();
    }

    public List<Ticket> findByPriority(TicketPriority priority) {
        return find("priority", priority).list();
    }

    public List<Ticket> findByStatusAndPriority(TicketStatus status, TicketPriority priority) {
        return find("status = ?1 and priority = ?2", status, priority).list();
    }
}
