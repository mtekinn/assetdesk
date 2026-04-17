package com.mtekinn.assetdesk.assignment;

import com.mtekinn.assetdesk.asset.Asset;
import com.mtekinn.assetdesk.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "asset_assignments")
@Getter
@Setter
@NoArgsConstructor
public class AssetAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime assignedAt;

    private LocalDateTime returnedAt;

    private String note;
}