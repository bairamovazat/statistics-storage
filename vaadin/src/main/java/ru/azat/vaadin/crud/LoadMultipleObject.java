package ru.azat.vaadin.crud;

import com.vaadin.flow.data.provider.QuerySortOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.azat.vaadin.crud.api.Query;

import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoadMultipleObject< F> {

    private Optional<Query<F>> query;
    private List<QuerySortOrder> querySortOrders;

}
