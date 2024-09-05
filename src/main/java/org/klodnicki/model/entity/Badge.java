package org.klodnicki.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.klodnicki.model.Action;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(unique = true, nullable = false)
    private String badgeNumber;
    private String location;
    private String deviceName;
    @Enumerated(EnumType.STRING)
    private Action action;
    private LocalDateTime timeStamp;
    @OneToOne
    @JoinColumn(name = "employee_id", unique = true)
    private Employee employee;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Badge badge = (Badge) o;
        return Objects.equals(id, badge.id) && Objects.equals(badgeNumber, badge.badgeNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, badgeNumber);
    }
}
