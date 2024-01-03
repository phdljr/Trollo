package org.nbc.account.trollo.domain.worker.entity;

import java.io.Serializable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkerId implements Serializable {

    private Long user;
    private Long card;

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkerId workerId = (WorkerId) o;
        return Objects.equals(user, workerId.user) && Objects.equals(card,
            workerId.card);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, card);
    }
}