package com.gtim.service_orders.service;

import java.util.List;

public interface CrudService<D, ID> {
    D create(D dto);
    D update(ID id, D dto);
    D findById(ID id);
    List<D> findAll();
    void delete(ID id);
}
