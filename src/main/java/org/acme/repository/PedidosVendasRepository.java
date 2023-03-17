package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.entities.PedidosVendas;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PedidosVendasRepository implements PanacheRepository<PedidosVendas> {
    public PanacheQuery<PedidosVendas> listPedidosByCodigo(String codigopedido) {
        return find("codigopedido", codigopedido);
    }

}
