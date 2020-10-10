package by.dziashko.frm.ui.views.details;

import by.dziashko.frm.backend.entity.OrderName;
import by.dziashko.frm.backend.entity.Seller;
import by.dziashko.frm.backend.service.OrderNameService;
import by.dziashko.frm.backend.service.SellerService;
import by.dziashko.frm.ui.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.*;


@Route(value = "details", layout = MainView.class)
@PageTitle("Order Details")
@CssImport("./styles/views/details/details-view.css")
    public class DetailView extends HorizontalLayout implements HasUrlParameter<String> {

    OrderNameService orderNameService;
    SellerService sellerService;

    OrderName orderName;
    String searchParam;

    Binder<OrderName> binder = new Binder<>(OrderName.class);

    Dialog dialogSave = new Dialog();
    Dialog dialogDelete = new Dialog();

    ComboBox<Seller> seller = new ComboBox<>("Seller");
    TextField client = new TextField("client");
//    DatePicker orderDate = new DatePicker("order date");
//        orderDeadLine = new DatePicker("order deadline");
//        sendDate = new DatePicker("order send date");
    TextField orderNumber = new TextField("order number");
    TextField orderDate = new TextField("order date");
    TextField orderDeadLine = new TextField("order deadline");
    TextField sendDate = new TextField("order send date");
    TextField orderDelay = new TextField("order delay");
    ComboBox<OrderName.Readiness> orderReadiness = new ComboBox<>("Order Readiness");
    TextField cabinType = new TextField("cabin type");
    ComboBox<OrderName.Readiness> cabinReadiness = new ComboBox<>("Cabin Readiness");
    TextField aspiratorType = new TextField("aspirator type");
    ComboBox<OrderName.Readiness> aspiratorReadiness = new ComboBox<>("Aspirator Readiness");
    TextField separatorType = new TextField("separator type");
    ComboBox<OrderName.Readiness> separatorReadiness = new ComboBox<>("Separator Readiness");
    TextField additionalOptions = new TextField("additional options");
    ComboBox<OrderName.Readiness> additionalOptionsReadiness = new ComboBox<>("Additional Options Readiness");

    Button save = new Button("Save"); Button delete = new Button("Delete"); Button back = new Button("Back");


    public DetailView( SellerService sellerService, OrderNameService orderNameService ) {
        this.sellerService = sellerService;
        this.orderNameService = orderNameService;
        setId("details-view");

        seller.setItems(sellerService.findAll());
        seller.setItemLabelGenerator(Seller::getName);

        binder.bindInstanceFields(this);
        orderReadiness.setItems(OrderName.Readiness.values());
        cabinReadiness.setItems(OrderName.Readiness.values());
        aspiratorReadiness.setItems(OrderName.Readiness.values());
        separatorReadiness.setItems(OrderName.Readiness.values());
        additionalOptionsReadiness.setItems(OrderName.Readiness.values());

        HorizontalLayout layoutTop = new HorizontalLayout(seller, client, orderNumber);
        HorizontalLayout layoutMiddle = new HorizontalLayout(orderDate, orderDeadLine, orderDelay, orderReadiness, sendDate);
        HorizontalLayout layoutMiddle_2 = new HorizontalLayout(cabinType, cabinReadiness);
        HorizontalLayout layoutMiddle_3 = new HorizontalLayout(aspiratorType, aspiratorReadiness);
        HorizontalLayout layoutBottom = new HorizontalLayout(separatorType, separatorReadiness);
        HorizontalLayout layoutBottom_2 = new HorizontalLayout(additionalOptions, additionalOptionsReadiness);
        add(layoutTop,layoutMiddle,layoutMiddle_2, layoutMiddle_3, layoutBottom, layoutBottom_2, createButtonsLayout());

        binder.setBean(orderName);

    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String searchParam) {
        if (searchParam == null && orderNameService == null) {}
        else{
            this.orderName
                    = orderNameService.find(searchParam);
                this.searchParam=searchParam;
            binder.readBean(this.orderName);
        }
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        back.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        back.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> dialogSave.open());
        delete.addClickListener(click -> dialogDelete.open());
        back.addClickListener(click -> back.getUI().ifPresent(ui -> ui.navigate("orders")));

        binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));

        createSaveDialog();
        createDeleteDialog();
        add(dialogSave,dialogDelete);

        return new HorizontalLayout(save, delete, back);
    }

    private void createSaveDialog(){
        dialogSave.setCloseOnEsc(false);
        dialogSave.setCloseOnOutsideClick(false);

        Button confirmButtonThenSave = new Button("Confirm", event -> {
            saveOrderName();
            dialogSave.close();
        });
        Button cancelButtonThenSave = new Button("Cancel", event -> {
            dialogSave.close();
        });

        HorizontalLayout top = new HorizontalLayout();
        top.setJustifyContentMode(JustifyContentMode.CENTER);
        top.setPadding(true);
        top.setMargin(true);
        top.add(new Text("Confirm Saving"));
        HorizontalLayout bottom = new HorizontalLayout();
        bottom.add(new HorizontalLayout(confirmButtonThenSave, cancelButtonThenSave));
        bottom.setJustifyContentMode(JustifyContentMode.CENTER);
        dialogSave.add(top,bottom);
    }

    private void createDeleteDialog(){
        dialogDelete.setCloseOnEsc(false);
        dialogDelete.setCloseOnOutsideClick(false);

        Button confirmButtonThenDelete = new Button("Confirm", event -> {
            deleteOrderName();
            dialogDelete.close();
        });
        Button cancelButtonThenDelete = new Button("Cancel", event -> {
            dialogDelete.close();
        });
        HorizontalLayout top = new HorizontalLayout();
        top.setJustifyContentMode(JustifyContentMode.CENTER);
        top.setPadding(true);
        top.setMargin(true);
        top.add(new Text("Confirm Deleting"));
        HorizontalLayout bottom = new HorizontalLayout();
        bottom.add(new HorizontalLayout(confirmButtonThenDelete, cancelButtonThenDelete));
        bottom.setJustifyContentMode(JustifyContentMode.CENTER);
        dialogDelete.add(top,bottom);
    }

    private void goToPrevView() {
         this.getUI().ifPresent(ui -> ui.navigate("orders"));
    }

    private void saveOrderName() {
        try {
            binder.writeBean(orderName);
            orderNameService.save(orderName);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    private void deleteOrderName() {
        orderNameService.delete(orderName);
        goToPrevView();
    }


}
