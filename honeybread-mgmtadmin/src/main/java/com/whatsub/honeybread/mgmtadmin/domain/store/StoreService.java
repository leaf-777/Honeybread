package com.whatsub.honeybread.mgmtadmin.domain.store;

import com.whatsub.honeybread.common.util.Sha256Utils;
import com.whatsub.honeybread.core.domain.category.CategoryRepository;
import com.whatsub.honeybread.core.domain.store.Store;
import com.whatsub.honeybread.core.domain.store.StoreRepository;
import com.whatsub.honeybread.core.domain.user.UserRepository;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.mgmtadmin.domain.store.dto.StoreCreateRequest;
import com.whatsub.honeybread.mgmtadmin.domain.store.dto.StoreUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Long create(StoreCreateRequest storeCreateRequest) {
        checkSellerIdExistence(storeCreateRequest.getSellerId());
        checkStoreNameExistence(storeCreateRequest.getBasic().getName());
        checkCategoryIdsExistence(storeCreateRequest.getCategoryIds());

        Store saved = storeRepository.save(storeCreateRequest.createStore());
        saved.updateUuid(Sha256Utils.generate(saved.getId(), saved.getBasic().getName()));
        return saved.getId();
    }

    @Transactional
    public void update(long storeId, StoreUpdateRequest storeUpdateRequest) {
        checkStoreExistence(storeId);
        checkStoreNameExistence(storeId, storeUpdateRequest.getBasic().getName());
        checkCategoryIdsExistence(storeUpdateRequest.getCategoryIds());

        storeRepository.getOne(storeId).update(storeUpdateRequest.updateStore());
    }

    private void checkStoreExistence(long storeId) {
        if (!storeRepository.existsById(storeId)) {
            throw new HoneyBreadException(ErrorCode.STORE_NOT_FOUND);
        }
    }

    private void checkSellerIdExistence(long sellerId) {
        if (!userRepository.existsById(sellerId)) {
            throw new HoneyBreadException(ErrorCode.USER_NOT_FOUND);
        }
    }

    private void checkStoreNameExistence(String name) {
        if (storeRepository.existsByBasicName(name)) {
            throw new HoneyBreadException(ErrorCode.DUPLICATE_STORE_NAME);
        }
    }

    private void checkStoreNameExistence(long excludeStoreId, String name) {
        if (storeRepository.existsByIdNotAndBasicName(excludeStoreId, name)) {
            throw new HoneyBreadException(ErrorCode.DUPLICATE_STORE_NAME);
        }
    }

    private void checkCategoryIdsExistence(List<Long> categoryIds) {
        categoryIds.forEach(categoryId -> {
            if (!categoryRepository.existsById(categoryId)) {
                throw new HoneyBreadException(ErrorCode.CATEGORY_NOT_FOUND);
            }
        });
    }

}
