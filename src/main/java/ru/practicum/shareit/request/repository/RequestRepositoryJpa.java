package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;
import java.util.List;

public interface RequestRepositoryJpa extends JpaRepository<ItemRequest, Integer> {
    public Collection<ItemRequest> findAllByRequestorId(Integer userId);

    public List<ItemRequest> findAllByRequestorIdNot(Integer RequestorId, Pageable pageable);

    public List<ItemRequest> findAllByRequestorIdNot(Integer RequestorId);

}
