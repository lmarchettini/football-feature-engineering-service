package com.footballai.engineering.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "teams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    private String country;

    private Integer founded;

    @Column(columnDefinition = "TEXT")
    private String logo;

    private Long venueId;
}