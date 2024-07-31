package org.klodnicki.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.klodnicki.model.Action;

import java.time.LocalDateTime;

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
    private String badgeNumber;
    private String location;
    private String deviceName;
    @Enumerated(EnumType.STRING)
    private Action action;
    private LocalDateTime timeStamp;
    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
