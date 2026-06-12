package com.acrobtw.elei.domain.economy;

import lombok.Setter;

import com.acrobtw.elei.domain.user.User;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Setter
@Entity
@Table(name = "wallets")
@NoArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "balance", nullable = false)
    private Long balance = 0L;
}
