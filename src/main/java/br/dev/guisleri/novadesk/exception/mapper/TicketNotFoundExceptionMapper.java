package br.dev.guisleri.novadesk.exception.mapper;

import br.dev.guisleri.novadesk.exception.TicketNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class TicketNotFoundExceptionMapper
        implements ExceptionMapper<TicketNotFoundException> {

    @Override
    public Response toResponse(TicketNotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(exception.getMessage())
                .build();
    }
}
