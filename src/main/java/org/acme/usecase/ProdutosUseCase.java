package org.acme.usecase;

import org.acme.dtos.*;
import org.acme.entities.Produtos;
import org.acme.enumerations.MensagemKeyEnum;
import org.acme.exceptions.CoreRuleException;
import org.acme.repository.ProdutosRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@ApplicationScoped
public class ProdutosUseCase {

    private final ProdutosRepository repository;
    private final Validator validator;

    @Inject
    public ProdutosUseCase(ProdutosRepository repository, Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }
    public CadastrarProdutosResponse adicionarProdutos(CadastrarProdutosRequest request){
        validarRequest(request);
        CadastrarProdutosResponse cadastrarProdutosResponse = CadastrarProdutosResponse.builder()
                .nome(request.getNome())
                .tipo(request.getTipo())
                .estoque(request.getEstoque())
                .valor(request.getValor())
                .build();
        persistirDados(request);
        return cadastrarProdutosResponse;
    }
    public CadastrarProdutosResponse atualizarProdutos(CadastrarProdutosRequest request, Long id){
        Produtos produto = repository.findById(id);
        validarRequest(request);
        validarProduto(produto);
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
    public void deletarProduto(Long id){
        Produtos produto = repository.findById(id);
        validarProduto(produto);
        repository.delete(produto);
    }
    public void validarRequest(CadastrarProdutosRequest request){
        Set<ConstraintViolation<CadastrarProdutosRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()){
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.REQUEST_ERRO));
        }
    }
    public void validarProduto(Produtos produto){
        if (produto==null) {
            throw new CoreRuleException(MessagemResponse.error(MensagemKeyEnum.PRODUTO_INVALIDO));
        }
    }

}
