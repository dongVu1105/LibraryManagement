package com.dongVu1105.libraryManagement.service;

import com.dongVu1105.libraryManagement.dto.request.HistoryRequest;
import com.dongVu1105.libraryManagement.dto.response.HistoryResponse;
import com.dongVu1105.libraryManagement.entity.Book;
import com.dongVu1105.libraryManagement.entity.History;
import com.dongVu1105.libraryManagement.exception.AppException;
import com.dongVu1105.libraryManagement.exception.ErrorCode;
import com.dongVu1105.libraryManagement.mapper.HistoryMapper;
import com.dongVu1105.libraryManagement.repository.BookRepository;
import com.dongVu1105.libraryManagement.repository.HistoryRepository;
import com.dongVu1105.libraryManagement.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleState;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class HistoryService {
    HistoryRepository historyRepository;
    HistoryMapper historyMapper;
    BookRepository bookRepository;
    UserRepository userRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public HistoryResponse create (HistoryRequest request) throws AppException {
        History history = historyMapper.toHistory(request);
        history.setReturned(false);
        updateBook(history, history.isReturned());
        userRepository.findByUsername(history.getUsername()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));
        String creator = SecurityContextHolder.getContext().getAuthentication().getName();
        history.setCreatedBy(creator);
        history.setBorrowDate(LocalDate.now());
        return historyMapper.toHistoryResponse(historyRepository.save(history));
    }

    private void updateBook (History history, boolean returned) throws AppException {
        Book book = bookRepository.findById(history.getBookID()).orElseThrow(
                () -> new AppException(ErrorCode.BOOK_NOT_EXISTED));
        if(history.isReturned()){
            book.setQuantity(book.getQuantity() + 1);
        } else if (book.getQuantity()<=0){
            throw new AppException(ErrorCode.OUT_OF_BOOK);
        } else {
            book.setQuantity(book.getQuantity() - 1);
        }
        bookRepository.save(book);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public HistoryResponse update (HistoryRequest request) throws AppException {
        try {
            History history = historyRepository.findByUsernameAndBookID(request.getUsername(), request.getBookID());
            history.setReturned(true);
            updateBook(history, history.isReturned());
            String modifier = SecurityContextHolder.getContext().getAuthentication().getName();
            history.setModifiedBy(modifier);
            history.setReturnDate(LocalDate.now());
            return historyMapper.toHistoryResponse(historyRepository.save(history));
        } catch (Exception e){
            throw new AppException(ErrorCode.HISTORY_NOT_EXISTED);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<HistoryResponse> getAll (){
        return historyRepository.findAll().stream().map(historyMapper::toHistoryResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<HistoryResponse> getAllByUsername (String username){
        System.out.println(historyRepository.findAllByUsername(username));
        return historyRepository.findAllByUsername(username).stream().map(historyMapper::toHistoryResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<HistoryResponse> getAllByBookID (String bookID){
        return historyRepository.findAllByBookID(bookID).stream().map(historyMapper::toHistoryResponse).toList();
    }

    @PreAuthorize("hasRole('USER')")
    public List<HistoryResponse> getHistoryByUser (){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return historyRepository.findAllByUsername(username).stream().map(historyMapper::toHistoryResponse).toList();
    }

}
