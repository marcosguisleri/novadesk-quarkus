package br.dev.guisleri.novadesk.dto;

import br.dev.guisleri.novadesk.model.TicketPriority;
import br.dev.guisleri.novadesk.model.TicketStatus;

import java.time.LocalDateTime;

public record TicketResponseDTO(
        long id,
        String title,
        String description,
        String requester,
        TicketStatus status,
        TicketPriority priority,
        LocalDateTime createdAt
) {
}
