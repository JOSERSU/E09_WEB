package com.upiiz.SecurityInMemory.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/facturas")
public class FacturasController {

    @PutMapping
    public String actualizar(){
        return "Factura actializada - sin seguridad";
    }

    @GetMapping
    public String listar(){
        return "Lista de facturas - con seguridad";
    }

    @DeleteMapping
    public String eliminar(){
        return "Factura elimnada - con seguridad";
    }

    @PostMapping
    public String crear(){
        return "Factura creada - con seguridad";
    }

}
