package br.dev.guisleri.novadesk.dto;

import br.dev.guisleri.novadesk.model.TicketPriority;

public record CreateTicketRequestDTO(
        String title,
        String description,
        String requester,
        TicketPriority priority
) {
}
