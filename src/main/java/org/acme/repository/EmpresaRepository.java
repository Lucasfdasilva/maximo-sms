package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.entities.Empresa;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EmpresaRepository implements PanacheRepository<Empresa> {
    public Empresa findByCnpj(String cnpj){
        return find("cnpj", cnpj).firstResult();
    }
}
