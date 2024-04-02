package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.BookingRepositoryJpa;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepositoryJpa;
import ru.practicum.shareit.item.repository.ItemRepositoryJpa;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepositoryJpa itemRepository;
    @Mock
    private UserRepositoryJpa userRepository;

    @Mock
    private BookingRepositoryJpa bookingRepository;

    @Mock
    private CommentRepositoryJpa commentRepository;

    @InjectMocks
    private ItemServiceImpl itemService;


    @Test
    void addItem_itemSave() {
        int userId = 0;
        User user = new User();
        user.setId(userId);
        Item item = new Item(0, "item", user, "item for boking", true, null, null);
        ItemDto itemDto = new ItemDto(0, "item", "item for boking", true, null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.save(item)).thenReturn(item);
        Item itemCreate = itemService.add(itemDto, userId);

        assertEquals(item, itemCreate);
        verify(itemRepository).save(item);
    }

    @Test
    void addItem_isThrow() {
        int userId = 0;
        ItemDto itemDto = new ItemDto(0, "item", "item for boking", true, null);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> itemService.add(itemDto, userId));
    }


    @Test
    void updateItem_updateSave() {
        int userId = 0;
        ItemDto itemDto = new ItemDto(0, "item", "item before update", true, null);

        int itemId = 0;
        User oldUser = new User();
        oldUser.setId(userId);
        oldUser.setName("first name");
        oldUser.setEmail("set@email.com");
        Item item = new Item(0, "item", oldUser, "item for boking", true, null, null);
        Item itemUpdate = new Item(0, "item", oldUser, "item before update", true, null, null);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemRepository.save(itemUpdate)).thenReturn((itemUpdate));

        ItemDto itemActual = itemService.update(itemDto, itemId, userId);
        verify(itemRepository).save(item);

        assertNotNull(itemActual);
        assertEquals("item before update", itemActual.getDescription());
        verify(itemRepository, times(1)).save(new Item());

    }

    @Test
    void updateItem_updateToThrow() {
        int userId = 0;
        ItemDto itemDto = new ItemDto(0, "item", "item before update", true, null);
        int itemId = 0;
        User oldUser = new User();
        oldUser.setId(userId);
        oldUser.setName("first name");
        oldUser.setEmail("set@email.com");
        Item item = new Item(itemId, "item", oldUser, "item for boking", true, null, null);

        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> itemService.update(itemDto, userId, itemId));
        verify(itemRepository, never()).save(item);
        verify(itemRepository, times(0)).save(new Item());
    }

    @Test
    void updateItem_updateToThrowUserId() {
        int userId = 0;
        ItemDto itemDto = new ItemDto(0, "item", "item before update", true, null);
        int itemId = 0;
        User oldUser = new User();
        oldUser.setId(userId);
        oldUser.setName("first name");
        oldUser.setEmail("set@email.com");
        Item item = new Item(itemId, "item", oldUser, "item for boking", true, null, null);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        assertThrows(EntityNotFoundException.class, () -> itemService.update(itemDto, 3, itemId));
        verify(itemRepository, never()).save(item);
        verify(itemRepository, times(0)).save(new Item());
    }

    @Test
    void getForIdItem_get() {
        int userId = 0;
        int itemId = 0;
        ItemDto itemDto = new ItemDto(itemId, "item", "item for booking", true, null);
        User oldUser = new User();
        oldUser.setId(userId);
        oldUser.setName("first name");
        oldUser.setEmail("set@email.com");
        Item item = new Item(itemId, "item", oldUser, "item for booking", true, null, null);
        Item itemUpdate = new Item(itemId, "item", oldUser, "item for booking", true, null, null);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        Item itemActual = itemService.getForId(itemId);
        verify(itemRepository).findById(itemId);

        assertNotNull(itemActual);
        assertEquals("item for booking", itemActual.getDescription());
    }

    @Test
    void getForIdItem_notGetIsThrow() {
        int userId = 0;
        int itemId = 0;
        User oldUser = new User();
        oldUser.setId(userId);
        oldUser.setName("first name");
        oldUser.setEmail("set@email.com");
        Item item = new Item(itemId, "item", oldUser, "item for booking", true, null, null);

        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> itemService.getForId(itemId));
        verify(itemRepository, never()).save(item);
        verify(itemRepository, times(0)).save(new Item());
    }

    @Test
    void getForIdWithBooking_getItemDtoBooking() {
        int userId = 1;
        int itemId = 1;
        User oldUser = new User(2, "first name", "set@email.com");
        User userOwner = new User(userId, "owner@email.com", "nameUser");
        Item item = new Item(itemId, "item", userOwner, "item for booking", true, null, null);
        Booking booking = new Booking(1, LocalDateTime.now().minusHours(10), LocalDateTime.now().minusHours(9), item, Status.APPROVED, oldUser);
        Booking bookingTwo = new Booking(2, LocalDateTime.now().plusHours(10), LocalDateTime.now().plusHours(19), item, Status.APPROVED, oldUser);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(userRepository.findById(userId)).thenReturn(Optional.of(userOwner));
        when(commentRepository.findAllByItemOwnerId(userOwner.getId())).thenReturn((new HashSet<Comment>()));
        when(bookingRepository.findFirstByItemIdAndStartBefore(anyInt(), any(LocalDateTime.class),
                any(Sort.class))).thenReturn(booking);
        when(bookingRepository.findFirstByItemIdAndStartAfterAndStatus(anyInt(),
                any(LocalDateTime.class), any(Status.class), any(Sort.class))).thenReturn(bookingTwo);
        ItemDtoBooking itemActual = itemService.getForIdWithBooking(itemId, userId);

        assertNotNull(itemActual);
        assertEquals("item for booking", itemActual.getDescription());
    }

    @Test
    void getForIdWithBooking_getItemDtoBooking_withBookingNull() {
        int userId = 1;
        int itemId = 1;
        User oldUser = new User(2, "first name", "set@email.com");
        User userOwner = new User(userId, "owner@email.com", "nameUser");
        Item item = new Item(itemId, "item", userOwner, "item for booking", true, null, null);
        Booking booking = new Booking(1, LocalDateTime.now().minusHours(10), LocalDateTime.now().minusHours(9), item, Status.APPROVED, oldUser);
        Booking bookingTwo = new Booking(2, LocalDateTime.now().plusHours(10), LocalDateTime.now().plusHours(19), item, Status.APPROVED, oldUser);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(userRepository.findById(2)).thenReturn(Optional.of(oldUser));
        when(commentRepository.findAllByItemOwnerId(userOwner.getId())).thenReturn((new HashSet<Comment>()));
        ItemDtoBooking itemActual = itemService.getForIdWithBooking(itemId, 2);

        assertNotNull(itemActual);
        assertEquals("item for booking", itemActual.getDescription());
    }

    @Test
    void getForIdWithBooking_getItemDtoNotBooking() {
        int userIdNotOwner = 2;
        int userId = 1;
        int itemId = 1;
        User oldUser = new User(userIdNotOwner, "first name", "set@email.com");
        User userOwner = new User(userId, "owner@email.com", "nameUser");
        Item item = new Item(itemId, "item", userOwner, "item for booking", true, null, null);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(userRepository.findById(userIdNotOwner)).thenReturn(Optional.of(oldUser));
        when(commentRepository.findAllByItemOwnerId(userOwner.getId())).thenReturn((new HashSet<Comment>()));
        ItemDtoBooking itemActual = itemService.getForIdWithBooking(itemId, userIdNotOwner);

        assertNotNull(itemActual);
        verify(bookingRepository, never()).findFirstByItemIdAndStartBefore(anyInt(), any(LocalDateTime.class),
                any(Sort.class));
        verify(bookingRepository, never()).findFirstByItemIdAndStartAfterAndStatus(anyInt(), any(LocalDateTime.class),
                any(Status.class), any(Sort.class));
    }

    @Test
    void getForIdWithBooking_getItemIsThrow() {
        int userIdNotOwner = 2;
        int userId = 1;
        int itemId = 1;
        User oldUser = new User(userIdNotOwner, "first name", "set@email.com");
        User userOwner = new User(userId, "owner@email.com", "nameUser");
        Item item = new Item(itemId, "item", userOwner, "item for booking", true, null, null);

        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> itemService.getForIdWithBooking(itemId, userIdNotOwner));

        verify(bookingRepository, never()).findFirstByItemIdAndStartBefore(anyInt(), any(LocalDateTime.class),
                any(Sort.class));
        verify(bookingRepository, never()).findFirstByItemIdAndStartAfterAndStatus(anyInt(), any(LocalDateTime.class),
                any(Status.class), any(Sort.class));
    }

    @Test
    void getForIdWithBooking_getUserIsThrow() {
        int userIdNotOwner = 2;
        int userId = 1;
        int itemId = 1;
        User oldUser = new User(userIdNotOwner, "first name", "set@email.com");
        User userOwner = new User(userId, "owner@email.com", "nameUser");
        Item item = new Item(itemId, "item", userOwner, "item for booking", true, null, null);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(userRepository.findById(userIdNotOwner)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> itemService.getForIdWithBooking(itemId, userIdNotOwner));

        verify(bookingRepository, never()).findFirstByItemIdAndStartBefore(anyInt(), any(LocalDateTime.class),
                any(Sort.class));
        verify(bookingRepository, never()).findFirstByItemIdAndStartAfterAndStatus(anyInt(), any(LocalDateTime.class),
                any(Status.class), any(Sort.class));
    }

    @Test
    void getForIdWithBooking_getCommentIsThrow() {
        int userIdNotOwner = 2;
        int userId = 1;
        int itemId = 1;
        User oldUser = new User(userIdNotOwner, "first name", "set@email.com");
        User userOwner = new User(userId, "owner@email.com", "nameUser");
        Item item = new Item(itemId, "item", userOwner, "item for booking", true, null, null);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(userRepository.findById(userIdNotOwner)).thenReturn(Optional.of(oldUser));
        when(commentRepository.findAllByItemOwnerId(userOwner.getId())).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> itemService.getForIdWithBooking(itemId, userIdNotOwner));

        verify(bookingRepository, never()).findFirstByItemIdAndStartBefore(anyInt(), any(LocalDateTime.class),
                any(Sort.class));
        verify(bookingRepository, never()).findFirstByItemIdAndStartAfterAndStatus(anyInt(), any(LocalDateTime.class),
                any(Status.class), any(Sort.class));
    }

    @Test
    void getItemsForUser_getCollectionItem() {
        int id = 0;
        Item item = new Item(1, "item", new User(), "item for booking", true, null, null);
        Item itemTwo = new Item(2, "item2", new User(), "item for booking", true, null, null);
        Collection<Item> result = List.of(item, itemTwo);
        when(itemRepository.findByOwnerId(id)).thenReturn(result);

        Collection<Item> list = itemService.getItemsForUser(id);
        assertEquals(list, result);
    }

    @Test
    void getItemsForUser_getNullCollectionItem() {
        int id = 0;

        when(itemRepository.findByOwnerId(id)).thenReturn(new ArrayList<>());

        Collection<Item> list = itemService.getItemsForUser(id);
        assertEquals(list.size(), 0);
    }

    @Test
    void getItemsForUserWithBooking_getCollectionItemDtoBooking() {
        int userId = 1;
        int itemId = 1;
        User bookerUser = new User(2, "first name", "set@email.com");
        User userOwner = new User(userId, "owner@email.com", "nameUser");
        Item item = new Item(itemId, "item", userOwner, "item for booking", true, null, null);
        Booking booking = new Booking(1, LocalDateTime.now().minusHours(10), LocalDateTime.now().minusHours(9), item, Status.APPROVED, bookerUser);
        Booking bookingTwo = new Booking(2, LocalDateTime.now().plusHours(10), LocalDateTime.now().plusHours(19), item, Status.APPROVED, bookerUser);
        List<Item> list = List.of(item);
        ItemDtoBooking itemDtoBooking = new ItemDtoBooking(itemId, "item", "item for booking", true, null, BookingMapper.toBookingDtoForItem(booking), BookingMapper.toBookingDtoForItem(bookingTwo), new HashSet<>());
        Collection<ItemDtoBooking> dtoBookingCollection = List.of(itemDtoBooking);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userOwner));
        when(itemRepository.findByOwnerId(anyInt(), any(Pageable.class))).thenReturn(list);

        when(bookingRepository.findFirstByItemIdAndEndBefore(anyInt(), any(LocalDateTime.class),
                any(Sort.class))).thenReturn(booking);
        when(bookingRepository.findFirstByItemIdAndStartAfter(anyInt(),
                any(LocalDateTime.class), any(Sort.class))).thenReturn(bookingTwo);
        when(commentRepository.findAllByItemOwnerId(userOwner.getId())).thenReturn((new HashSet<Comment>()));
        Collection<ItemDtoBooking> itemActual = itemService.getItemsForUserWithBooking(userId, 0, 10);

        assertNotNull(itemActual);
        assertEquals(dtoBookingCollection, itemActual);
    }

    @Test
    void getItemsForUserWithBooking_getThrow() {
        int userId = 1;

        when(userRepository.findById(userId)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> itemService.getItemsForUserWithBooking(userId, 0, 10));

        verify(bookingRepository, never()).findFirstByItemIdAndEndBefore(anyInt(), any(LocalDateTime.class),
                any(Sort.class));
        verify(bookingRepository, never()).findFirstByItemIdAndStartAfter(anyInt(), any(LocalDateTime.class),
                any(Sort.class));
    }

    @Test
    void getAllItem_getCollectionItem() {
        Item item = new Item(1, "item", new User(), "item for booking", true, null, null);
        Item itemTwo = new Item(2, "itemTwo", new User(), "item for booking", true, null, null);
        List<Item> list = List.of(item, itemTwo);

        when(itemRepository.findAll()).thenReturn(list);
        Collection<Item> itemList = itemService.getAllItem();

        assertEquals(list, itemList);
    }

    @Test
    void searchItem_getCollectionItemDto() {
        String text = "ite";
        Item item = new Item(1, "item", new User(), "item for booking", true, null, null);
        Item itemTwo = new Item(2, "itemTwo", new User(), "item for booking", true, null, null);
        List<Item> list = List.of(item, itemTwo);

        when(itemRepository.search(anyString(), any(Pageable.class))).thenReturn(list);
        Collection<ItemDto> itemList = itemService.searchItem(text, 0, 10);

        assertEquals(ItemMapper.mapToItemDto(list), itemList);
    }

    @Test
    void searchItem_getEmptyCollection() {
        String text = "";
        Item item = new Item(1, "item", new User(), "item for booking", true, null, null);
        Item itemTwo = new Item(2, "itemTwo", new User(), "item for booking", true, null, null);
        List<Item> list = Collections.EMPTY_LIST;

        Collection<ItemDto> itemList = itemService.searchItem(text, 0, 10);

        assertEquals(ItemMapper.mapToItemDto(list), itemList);
    }

    @Test
    void searchItem_getEmptyList() {
        String text = " ";
        Item item = new Item(1, "item", new User(), "item for booking", true, null, null);
        Item itemTwo = new Item(2, "itemTwo", new User(), "item for booking", true, null, null);
        List<Item> list = Collections.EMPTY_LIST;

        Collection<ItemDto> itemList = itemService.searchItem(text, 0, 10);

        assertEquals(ItemMapper.mapToItemDto(list), itemList);
    }

    @Test
    void addComment_getComment() {
        String text = "item cool";
        int bookerId = 2;
        int userId = 1;
        int itemId = 1;
        User bookerUser = new User(bookerId, "first name", "set@email.com");
        User userOwner = new User(userId, "owner@email.com", "nameUser");
        Item item = new Item(itemId, "item", userOwner, "item for booking", true, null, null);
        Booking booking = new Booking(1, LocalDateTime.now().minusHours(10), LocalDateTime.now().minusHours(9), item, Status.APPROVED, bookerUser);
        Comment comment = new Comment(1, text, bookerUser, item, LocalDateTime.now());
        CommentDto commentDto = new CommentDto(text);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(userRepository.findById(bookerId)).thenReturn(Optional.of(bookerUser));
        when(bookingRepository.findFirstByBookerIdAndItemIdAndEndBefore(anyInt(), anyInt(), any(LocalDateTime.class))).thenReturn(Optional.of(booking));

        when(commentRepository.save(CommentMapper.toComment(commentDto, bookerUser, item))).thenReturn((comment));
        CommentForItem itemComment = itemService.addComment(commentDto, itemId, bookerId);

        assertNotNull(itemComment);
        assertEquals(CommentMapper.toCommentForItem(comment), itemComment);
    }

    @Test
    void addComment_getThrow() {
        String text = "item cool";
        int bookerId = 2;
        int itemId = 1;
        CommentDto commentDto = new CommentDto(text);
        when(itemRepository.findById(itemId)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> itemService.addComment(commentDto, itemId, bookerId));
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void addComment_getThrowUser() {
        String text = "item cool";
        int bookerId = 2;
        int userId = 1;
        int itemId = 1;
        User bookerUser = new User(bookerId, "first name", "set@email.com");
        User userOwner = new User(userId, "owner@email.com", "nameUser");
        Item item = new Item(itemId, "item", userOwner, "item for booking", true, null, null);
        Booking booking = new Booking(1, LocalDateTime.now().minusHours(10), LocalDateTime.now().minusHours(9), item, Status.APPROVED, bookerUser);
        Comment comment = new Comment(1, text, bookerUser, item, LocalDateTime.now());
        CommentDto commentDto = new CommentDto(text);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(userRepository.findById(bookerId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> itemService.addComment(commentDto, itemId, bookerId));
        verify(commentRepository, never()).save(any(Comment.class));
    }
}