package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.entities.PedidosCalibracao;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PedidosCalibracaoRepository implements PanacheRepository<PedidosCalibracao> {

    public PanacheQuery<PedidosCalibracao> listPedidosByCodigo(String codigopedido) {
        return find("codigopedido", codigopedido);
    }
}
