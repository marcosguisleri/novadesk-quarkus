package br.dev.guisleri.novadesk.exception;

public class TicketNotFoundException extends RuntimeException {

    public TicketNotFoundException(long id) {
        super("Ticket com id " + id + " não encontrado.");
    }
}
