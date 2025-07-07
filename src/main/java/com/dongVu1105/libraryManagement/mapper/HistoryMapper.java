package com.dongVu1105.libraryManagement.mapper;

import com.dongVu1105.libraryManagement.dto.request.HistoryRequest;
import com.dongVu1105.libraryManagement.dto.response.HistoryResponse;
import com.dongVu1105.libraryManagement.entity.History;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HistoryMapper {
    public History toHistory(HistoryRequest request);
    public HistoryResponse toHistoryResponse (History history);
}
