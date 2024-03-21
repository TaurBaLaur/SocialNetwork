package map.lab11.domain;

import java.util.Objects;

public class Request extends Entity<Long>{
    private Long id1;
    private Long id2;
    private RequestStatus status;

    public Request(Long id1, Long id2) {
        this.id1 = id1;
        this.id2 = id2;
        this.status = RequestStatus.PENDING;
    }
    public Request(Long id1, Long id2, RequestStatus status) {
        this.id1 = id1;
        this.id2 = id2;
        this.status = status;
    }

    public Long getId1() {
        return id1;
    }

    public void setId1(Long id1) {
        this.id1 = id1;
    }

    public Long getId2() {
        return id2;
    }

    public void setId2(Long id2) {
        this.id2 = id2;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Request{" +
                "id1=" + id1 +
                ", id2=" + id2 +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Request request = (Request) o;
        return Objects.equals(id1, request.id1) && Objects.equals(id2, request.id2) && status == request.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id1, id2, status);
    }
}
