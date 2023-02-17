package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.entities.Pedidos;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PedidosRepository implements PanacheRepository<Pedidos> {
}
