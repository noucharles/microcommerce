package com.ecommerce.microcommerce.controller;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.exception.ProduitIntrouvableException;
import com.ecommerce.microcommerce.model.Product;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Api(description = "Gestion de produits")
@RestController
public class ProductController {

    @Autowired
    private ProductDao productDao;

    //Récupérer les produits
    @GetMapping("/Produits")
    public List<Product> afficherLesProduits() {
        return productDao.findAll();
    }

    //Récupérer un produit par son Id
    @GetMapping(value = "/Produits/{id}")
    @ApiOperation("Récupére un produit selon son id")
    public Product afficherUnProduit(@PathVariable int id) throws ProduitIntrouvableException {

        Product produit = productDao.findById(id);

        if(produit==null) throw new ProduitIntrouvableException("Le produit avec l'id " + id + " est INTROUVABLE. Écran Bleu si je pouvais.");

        return produit;
    }

    //Enregistrer un produit
    @PostMapping("/Produits")
    public ResponseEntity<Void> EnregistrerUnProduit(@Validated @RequestBody Product product) {

         Product product1 = productDao.save(product);
         if (product == null) {
             return ResponseEntity.noContent().build();
         }

         URI location = ServletUriComponentsBuilder
                 .fromCurrentRequest()
                 .path("/{id}")
                 .buildAndExpand(product.getId())
                 .toUri();

         return ResponseEntity.created(location).build();
    }

    @GetMapping("test/Produits/{prixLimit}")
    public List<Product> testeDeRequetes(@PathVariable int prixLimit) {
        return productDao.findByPrixGreaterThan(prixLimit);
    }

    @DeleteMapping("produits/{id}")
    public Product supprimerProduit(@PathVariable int id) {
        return productDao.deleteProductById(id);
    }
}
