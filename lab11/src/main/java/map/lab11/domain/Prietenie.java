package map.lab11.domain;

import java.time.LocalDateTime;

public class Prietenie extends Entity<map.lab11.domain.Tuple<Long,Long>> {

    LocalDateTime date;
    private Long id1;
    private Long id2;

    public Prietenie(LocalDateTime date) {
        this.date=date;
    }

    /**
     *
     * @return the date when the friendship was created
     */
    public LocalDateTime getDate() {
        return date;
    }

    public void setId1(Long id1) {
        this.id1 = id1;
    }

    public void setId2(Long id2) {
        this.id2 = id2;
    }

    public Long getId1() {
        return id1;
    }

    public Long getId2() {
        return id2;
    }
}

