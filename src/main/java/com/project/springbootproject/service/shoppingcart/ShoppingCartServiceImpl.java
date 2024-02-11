package com.project.springbootproject.service.shoppingcart;

import com.project.springbootproject.dto.shoppingcartdto.ShoppingCartDto;
import com.project.springbootproject.exception.EntityNotFoundException;
import com.project.springbootproject.mapper.ShoppingCartMapper;
import com.project.springbootproject.model.ShoppingCart;
import com.project.springbootproject.repository.shoppingcart.ShoppingCartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public List<ShoppingCartDto> findAll() {
        return shoppingCartRepository.findAll()
                .stream()
                .map(shoppingCartMapper::toShoppingCartDto)
                .collect(Collectors.toList());
    }

    @Override
    public ShoppingCartDto getById(Long id) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(id).orElseThrow(
                ()->new EntityNotFoundException("Can not find ShoppingCart with Id: " + id)
        );
        return shoppingCartMapper.toShoppingCartDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto save(ShoppingCartDto shoppingCartDto) {
        ShoppingCart shoppingCart = shoppingCartMapper.toModel(shoppingCartDto);
        return shoppingCartMapper.toShoppingCartDto(shoppingCart);
    }

    @Override
    public void update(Long id, ShoppingCartDto shoppingCartDto) {
        Optional<ShoppingCart>shoppingCart = shoppingCartRepository.findById(id);
        ShoppingCart updateCart = shoppingCart.orElseThrow(
                ()->new EntityNotFoundException("Can not find ShoppingCart with ID: " + id)
        );
        shoppingCartMapper.updateShoppingCartFromShoppingCartDto(shoppingCartDto, updateCart);
        shoppingCartRepository.save(updateCart);


    }

    @Override
    public void deleteBiID(Long id) {

    }
}
