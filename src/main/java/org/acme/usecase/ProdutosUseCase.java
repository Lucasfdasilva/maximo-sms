package org.acme.usecase;

import org.acme.dtos.CadastrarProdutosRequest;
import org.acme.dtos.CadastrarProdutosResponse;
import org.acme.dtos.CriarUsuarioRequest;
import org.acme.entities.Produtos;
import org.acme.entities.Usuario;
import org.acme.repository.ProdutosRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ProdutosUseCase {

    private ProdutosRepository repository;

    @Inject
    public ProdutosUseCase(ProdutosRepository repository) {
        this.repository = repository;
    }
    public CadastrarProdutosResponse adicionarProdutos(CadastrarProdutosRequest request){
        CadastrarProdutosResponse cadastrarProdutosResponse = CadastrarProdutosResponse.builder()
                .nome(request.getNome())
                .tipo(request.getTipo())
                .estoque(request.getEstoque())
                .valor(request.getValor())
                .build();
        persistirDados(request);
        return cadastrarProdutosResponse;
    }
    public CadastrarProdutosResponse atualizarProdutos(CadastrarProdutosRequest request, Produtos produto){
        CadastrarProdutosResponse cadastrarProdutosResponse = CadastrarProdutosResponse.builder()
                .nome(request.getNome())
                .tipo(request.getTipo())
                .estoque(request.getEstoque())
                .valor(request.getValor())
                .build();
        atualizarDados(request, produto);
        return cadastrarProdutosResponse;
    }
    public void persistirDados(CadastrarProdutosRequest request){
        Produtos produtos = new Produtos();
        produtos.setNome(request.getNome());
        produtos.setTipo(request.getTipo());
        produtos.setEstoque(request.getEstoque());
        produtos.setValor(request.getValor());
        repository.persist(produtos);
    }

    public void atualizarDados(CadastrarProdutosRequest request, Produtos produto){
        produto.setNome(request.getNome());
        produto.setTipo(request.getTipo());
        produto.setEstoque(request.getEstoque());
        produto.setValor(request.getValor());
    }
}
