package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import org.acme.entities.Pedidos;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class PedidosRepository implements PanacheRepository<Pedidos> {
    public PanacheQuery<Pedidos> listPedidosByCodigo(String codigopedido) {
        return find("codigopedido", codigopedido);
    }

}
