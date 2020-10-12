package by.dziashko.frm.ui.views.order;

import by.dziashko.frm.backend.entity.OrderName;
import by.dziashko.frm.backend.entity.Seller;
import by.dziashko.frm.backend.service.OrderNameService;
import by.dziashko.frm.backend.service.SellerService;
import by.dziashko.frm.ui.forms.OrderNameForm;
import by.dziashko.frm.ui.views.main.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

    @Component
    @Scope("prototype")
    @Route(value ="orders", layout = MainView.class)
    @RouteAlias(value = "", layout = MainView.class)
    @PageTitle("Orders")
    @CssImport("./styles/views/order/order-view.css")
    public class OrderNameView extends VerticalLayout {

        OrderNameService orderNameService;
        SellerService sellerService;
        Grid<OrderName> grid = new Grid<>(OrderName.class);
        TextField filterText = new TextField();
        OrderNameForm form;
        Checkbox checkbox = new Checkbox();


        public OrderNameView(OrderNameService orderNameService, SellerService sellerService) {
            this.sellerService  = sellerService;
            this.orderNameService = orderNameService;
            addClassName("list-view");
            setSizeFull();
            getToolbar();
            configureGrid();

            form = new OrderNameForm(sellerService.findAll());
            form.addListener(OrderNameForm.SaveEvent.class, this::saveContact);
            form.addListener(OrderNameForm.DeleteEvent.class, this::deleteContact);
            form.addListener(OrderNameForm.CloseEvent.class, e -> closeEditor());

            Div content = new Div(grid, form);
            content.addClassName("content");
            content.setSizeFull();

            add(getToolbar(), content);
            updateList();
            closeEditor();
        }

        private void configureGrid() {
            grid.addClassName("contact-grid");
            grid.setSizeFull();
            grid.setColumns();
            grid.addColumn(orderName -> { Seller seller = orderName.getSeller(); return seller == null ? "-" : seller.getName();}).setHeader("Seller");
            grid.addColumn(OrderName::getClient).setHeader("Client");
            grid.addColumn(OrderName::getOrderNumber).setHeader("Order Number");
            grid.addColumn(OrderName::getOrderDate).setHeader("Order Date");
            grid.addColumn(OrderName::getOrderDeadLine).setHeader("Order Deadline");
            grid.addColumn(OrderName::getOrderDelay).setHeader("Order Delay");
            grid.addColumn(OrderName::getOrderReadiness).setHeader("Order Readiness");
            grid.addColumn(OrderName::getOrderSendDate).setHeader("Order Send Date");
            grid.getColumns().forEach(col -> col.setAutoWidth(true));

//            This allow to move columns
//            Span columnOrder = new Span();   grid.setColumnReorderingAllowed(true);
//            grid.addColumnReorderListener(event -> columnOrder.setText(event.getColumns().stream() .map(Grid.Column::getKey).collect(Collectors.joining(", "))));

            grid.asSingleSelect().addValueChangeListener(event -> navigateTo(event.getValue()));

        }

        private HorizontalLayout getToolbar() {
            filterText.setPlaceholder("Filter by order name...");
            filterText.setClearButtonVisible(true);
            filterText.setValueChangeMode(ValueChangeMode.LAZY);
            filterText.addValueChangeListener(e -> updateList());

            Button addContactButton = new Button("New Order");
            addContactButton.addClickListener(click -> addOrderName());

            checkbox.setLabel("Only Ready Orders");
            checkbox.setValue(false);
            checkbox.addValueChangeListener(e -> filterList(e.getValue()));

            HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton,checkbox);
            toolbar.addClassName("toolbar");
            return toolbar;
        }

        private void filterList(Boolean value) {
            grid.setItems(orderNameService.filterReady(value));
        }

        private void updateList() {
            grid.setItems(orderNameService.findAll(filterText.getValue()));
        }

        public void editOrderName(OrderName orderName) {
            if (orderName == null) {
                closeEditor();
            } else {
                form.setOrderName(orderName);
                form.setVisible(true);
                addClassName("editing");
            }
        }

    private void  navigateTo(OrderName orderName) {
        if (orderName == null) {

        }else {
            String s = orderName.getOrderNumber();
        grid.getUI().ifPresent(ui -> ui.navigate("details"+"/"+s));}
    }

        private void saveContact(OrderNameForm.SaveEvent event) {
            orderNameService.save(event.getOrderName());
            updateList();
            closeEditor();
        }

        private void deleteContact(OrderNameForm.DeleteEvent event) {
            orderNameService.delete(event.getOrderName());
            updateList();
            closeEditor();
        }

        void addOrderName() {
            grid.asSingleSelect().clear();
            editOrderName(new OrderName());
        }

        private void closeEditor() {
            form.setOrderName(null);
            form.setVisible(false);
            removeClassName("editing");
        }
}
