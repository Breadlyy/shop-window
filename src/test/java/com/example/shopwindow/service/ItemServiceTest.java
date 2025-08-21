package com.example.shopwindow.service;

import com.example.shopwindow.entity.Item;
import com.example.shopwindow.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        List<Item> items = List.of(new Item(), new Item());
        when(itemRepository.findAll()).thenReturn(items);

        assertThat(itemService.findAll()).hasSize(2);
    }

    @Test
    void testFindById() {
        Item item = new Item();
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        Optional<Item> result = itemService.findById(1L);
        assertThat(result).isPresent();
    }

    @Test
    void testReduceCount() {
        Item item = new Item();
        item.setCount(10);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenAnswer(i -> i.getArgument(0));

        Map<Long, Integer> cart = Map.of(1L, 3);
        itemService.reduceCount(cart);

        assertThat(item.getCount()).isEqualTo(7);
        verify(itemRepository, times(1)).save(item);
    }
}
