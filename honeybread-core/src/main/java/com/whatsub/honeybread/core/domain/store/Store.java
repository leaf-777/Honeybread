package com.whatsub.honeybread.core.domain.store;

import com.whatsub.honeybread.core.domain.base.BaseEntity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "stores")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
public class Store extends BaseEntity {

    // 스토어 텍스트형식 id
    private String uuid;

    // 판매자
    private Long sellerId;

    // 기본 정보
    private StoreBasic basic;

    // 영업 정보
    private StoreOperation operation;

    // 상태
    @Enumerated(EnumType.STRING)
    private StoreStatus status;

    // 카테고리
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private List<StoreCategory> categories;

    // 지원하는 결제 방식
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private List<StorePayMethod> payMethods;

}
