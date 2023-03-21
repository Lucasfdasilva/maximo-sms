package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.entities.PedidosCalibracao;
import org.acme.entities.Usuario;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PedidosCalibracaoRepository implements PanacheRepository<PedidosCalibracao> {
    public PedidosCalibracao findByCodigo(String codigoPedido){
        return find("codigoPedido", codigoPedido).firstResult();
    }
}
