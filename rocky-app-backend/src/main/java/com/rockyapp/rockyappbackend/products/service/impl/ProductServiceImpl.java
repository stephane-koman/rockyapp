package com.rockyapp.rockyappbackend.products.service.impl;

import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.documents.dao.DocumentDAO;
import com.rockyapp.rockyappbackend.documents.dto.DocumentDTO;
import com.rockyapp.rockyappbackend.documents.entity.Document;
import com.rockyapp.rockyappbackend.products.dao.ProductDAO;
import com.rockyapp.rockyappbackend.products.dto.ProductCreaDTO;
import com.rockyapp.rockyappbackend.products.dto.ProductDTO;
import com.rockyapp.rockyappbackend.products.dto.ProductSearchCriteriaDTO;
import com.rockyapp.rockyappbackend.products.dto.SimpleProductDTO;
import com.rockyapp.rockyappbackend.products.entity.Product;
import com.rockyapp.rockyappbackend.products.exception.ProductAlreadyExistsException;
import com.rockyapp.rockyappbackend.products.exception.ProductNotFoundException;
import com.rockyapp.rockyappbackend.products.mapper.ProductCreaMapper;
import com.rockyapp.rockyappbackend.products.mapper.ProductMapper;
import com.rockyapp.rockyappbackend.products.mapper.SimpleProductMapper;
import com.rockyapp.rockyappbackend.products.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private ProductDAO productDAO;
    private SimpleProductMapper simpleProductMapper;
    private ProductMapper productMapper;
    private ProductCreaMapper productCreaMapper;

    private DocumentDAO documentDAO;


    @Override
    public ResultPagine<SimpleProductDTO> search(ProductSearchCriteriaDTO criteriaDTO, Pageable pageable) {
        Page<Product> productPage = productDAO.search(criteriaDTO, pageable);
        return simpleProductMapper.mapFromEntity(productPage);
    }

    @Override
    public Product findByNameAndProductTypeAndVolume(String name, Long productTypeId, Long volumeId) throws ProductNotFoundException {
        Product product = productDAO.findProductByNameAndProductTypeAndVolumeAndIsNotDelete(name, productTypeId, volumeId);
        if(product == null) throw new ProductNotFoundException();
        return product;
    }

    private Product findProductById(String id) throws ProductNotFoundException {
        Product product = productDAO.findProductByIdAndIsNotDelete(id);
        if(product == null) throw new ProductNotFoundException();
        return product;
    }

    @Override
    public ProductDTO findById(String id) throws ProductNotFoundException {
        Product product = this.findProductById(id);
        return productMapper.mapFromEntity(product);
    }

    @Transactional
    @Override
    public void create(ProductCreaDTO productDTO) throws ProductAlreadyExistsException {
        Product productExists = productDAO.findProductByNameAndProductTypeAndVolumeAndIsNotDelete(productDTO.getName(), productDTO.getProductTypeId(), productDTO.getVolumeId());

        if (productExists != null) throw new ProductAlreadyExistsException(productDTO.getName().concat(" avec le type de produit ").concat(productExists.getProductType().getName()).concat(" et le volume ").concat(productExists.getVolume().getQuantity().toString().concat(productExists.getVolume().getMesure())));

        Product product = new Product();
        product = productCreaMapper.mapToEntity(productDTO, product);

        addProductDocuments(productDTO, product);

        productDAO.save(product);
    }

    @Transactional
    @Override
    public void update(String productId, ProductCreaDTO productDTO) throws ProductAlreadyExistsException, ProductNotFoundException {
        Product product = this.findByNameAndProductTypeAndVolume(productDTO.getName(), productDTO.getProductTypeId(), productDTO.getVolumeId());

        if(product != null && !product.getId().equals(productId)) throw new ProductAlreadyExistsException(productDTO.getName().concat(" avec le type de produit ").concat(product.getProductType().getName()).concat(" et le volume ").concat(product.getVolume().getQuantity().toString().concat(product.getVolume().getMesure())));

        product = this.findProductById(productId);
        product = productCreaMapper.mapToEntity(productDTO, product);
        product.setUpdatedAt(LocalDateTime.now());

        addProductDocuments(productDTO, product);

        productDAO.save(product);
    }

    private void addProductDocuments(ProductCreaDTO productDTO, Product product) {

        deleteAllProductDocuments(productDTO, product);

        deleteProductDocuments(productDTO, product);

        addNewProductDocuments(productDTO, product);
    }

    private static void addNewProductDocuments(ProductCreaDTO productDTO, Product product) {
        if(productDTO.getDocumentList() != null && !productDTO.getDocumentList().isEmpty()) {
            Set<Document> documentSet = new HashSet<>();
            productDTO.getDocumentList().forEach(doc -> {
                Document document = new Document();
                if(!product.getDocuments().isEmpty()){
                    Optional<Document> document2 =  product.getDocuments().stream().filter(document1 -> document1.getContent().equals(doc.getContent())).findFirst();
                    document2.ifPresent(value -> document.setId(value.getId()));
                }
                document.setProduct(product);
                document.setContent(doc.getContent());
                document.setFileName(doc.getFileName());
                document.setMimeType(doc.getMimeType());
                documentSet.add(document);
            });
            product.setDocuments(documentSet);
        }
    }

    private void deleteProductDocuments(ProductCreaDTO productDTO, Product product) {
        if(product.getDocuments() != null && !product.getDocuments().isEmpty()){
            product.getDocuments().forEach(document -> {
                if(productDTO.getDocumentList() != null && !productDTO.getDocumentList().isEmpty()){
                    Optional<DocumentDTO> document2 =  productDTO.getDocumentList().stream().filter(document1 -> document1.getContent().equals(document.getContent())).findFirst();
                    if(document2.isEmpty()){
                        documentDAO.deleteById(document.getId());
                    }
                }
            });
        }
    }

    private void deleteAllProductDocuments(ProductCreaDTO productDTO, Product product) {
        if(productDTO.getDocumentList() == null || productDTO.getDocumentList().isEmpty()){
            if(product.getDocuments() != null && !product.getDocuments().isEmpty()){
                product.getDocuments().forEach(document -> documentDAO.deleteById(document.getId()));
            }
        }
    }


    @Override
    public void delete(String productId) throws ProductNotFoundException {
        Product product = this.findProductById(productId);
        product.setDelete(1);
        product.setActive(0);
        product.setUpdatedAt(LocalDateTime.now());
        product.setDeletedAt(LocalDateTime.now());
        productDAO.save(product);
    }

    @Override
    public void changeStatus(String id, boolean active) throws ProductNotFoundException {
        Product product = this.findProductById(id);
        product.setActive(Boolean.TRUE.equals(active) ? 1 : 0);
        product.setUpdatedAt(LocalDateTime.now());
        productDAO.save(product);
    }
}
