package br.dev.guisleri.novadesk.dto;

import br.dev.guisleri.novadesk.model.TicketPriority;
import br.dev.guisleri.novadesk.model.TicketStatus;

public record UpdateTicketRequestDTO(
        String title,
        String description,
        String requester,
        TicketStatus status
) {
}
