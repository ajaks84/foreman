package by.dziashko.frm.ui.forms;

import by.dziashko.frm.backend.entity.OrderName;
import by.dziashko.frm.backend.entity.Seller;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class DetailsForm extends FormLayout {

    ComboBox<Seller> seller = new ComboBox<>("Seller");
    TextField client = new TextField("Client");
    TextField orderNumber = new TextField("Order Number");

//    DatePicker orderDate = new DatePicker("Order Date");
//    DatePicker orderDeadLine = new DatePicker("Order Send Date");
//    DatePicker orderSendDate = new DatePicker("Order Send Date");

    TextField orderDate = new TextField("Order Date");
    TextField orderDeadLine = new TextField("Order Send Date");
    TextField orderSendDate = new TextField("Order Send Date");

    TextField orderDelay = new TextField("Order Delay");
    ComboBox<OrderName.Readiness> orderReadiness = new ComboBox<>("Order Readiness");

    TextField cabinType = new TextField("Cabin Type");
    ComboBox<OrderName.Readiness> cabinReadiness = new ComboBox<>("Cabin readiness");
    TextField aspiratorType = new TextField("aspirator Type");
    ComboBox<OrderName.Readiness> aspiratorReadiness = new ComboBox<>("Aspirator Readiness");

    TextField separatorType = new TextField("Separator Type");
    ComboBox<OrderName.Readiness> separatorReadiness = new ComboBox<>("Separator Readiness");
    TextField additionalOptions = new TextField("Additional Options");
    ComboBox<OrderName.Readiness> additionalOptionsReadiness = new ComboBox<>("Additional Options Readiness");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<OrderName> binder = new Binder<>(OrderName.class);
    private OrderName orderName;

    public DetailsForm(List<Seller> sellers) {
        addClassName("contact-form");

        binder.bindInstanceFields(this);
        orderReadiness.setItems(OrderName.Readiness.values());
        seller.setItems(sellers);
        seller.setItemLabelGenerator(Seller::getName);
//        orderDate.getValue();
        add(
                seller,
                client,
                orderNumber,
                orderDate,
                orderDeadLine,
                orderSendDate,
                orderReadiness,
                createButtonsLayout()
        );
    }

    public void setOrderName(OrderName orderName) {
        this.orderName = orderName;
        binder.readBean(orderName);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
        delete.addClickListener(click -> fireEvent(new DetailsForm.DeleteEvent(this, orderName)));
        close.addClickListener(click -> fireEvent(new DetailsForm.CloseEvent(this)));

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(orderName);
            fireEvent(new DetailsForm.SaveEvent(this, orderName));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class DetailsFormEvent extends ComponentEvent<DetailsForm> {
        private OrderName orderName;

        protected DetailsFormEvent(DetailsForm source, OrderName orderName) {
            super(source, false);
            this.orderName = orderName;
        }

        public OrderName getOrderName() {
            return orderName;
        }
    }

    public static class SaveEvent extends DetailsForm.DetailsFormEvent {
        SaveEvent(DetailsForm source, OrderName orderName) {
            super(source, orderName);
        }
    }

    public static class DeleteEvent extends DetailsForm.DetailsFormEvent {
        DeleteEvent(DetailsForm source, OrderName orderName) {
            super(source, orderName);
        }

    }

    public static class CloseEvent extends DetailsForm.DetailsFormEvent {
        CloseEvent(DetailsForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
