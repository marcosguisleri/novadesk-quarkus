package br.dev.guisleri.novadesk.model;

public class Ticket {

    private long id;
    private String title;
    private String description;
    private String requester;
    private TicketStatus status;
    private TicketPriority priority;

    public Ticket(long id, String title, String description, String requester, TicketStatus status, TicketPriority priority) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.requester = requester;
        this.status = status;
        this.priority = priority;
    }

    public Ticket() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public TicketPriority getPriority() {
        return priority;
    }

    public void setPriority(TicketPriority priority) {
        this.priority = priority;
    }

}
