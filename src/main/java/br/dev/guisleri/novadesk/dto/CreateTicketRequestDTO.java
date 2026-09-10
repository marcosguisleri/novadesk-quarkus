package br.dev.guisleri.novadesk.dto;

import br.dev.guisleri.novadesk.model.TicketPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTicketRequestDTO(

        @NotBlank
        @Size(min = 5, max = 100)
        String title,

        @NotBlank
        @Size(max = 1_000)
        String description,

        @NotBlank
        String requester,

        @NotNull
        TicketPriority priority
) {
}
